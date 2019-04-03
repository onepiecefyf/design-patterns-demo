package cn.org.bjca.anysign.seal.service.impl;


import cn.org.bjca.anysign.components.bean.ESSPdfConstants;
import cn.org.bjca.anysign.pki.MessageDigest.MessageDigest;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.constant.SystemConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.CertUtil;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.*;
import cn.org.bjca.anysign.seal.service.ISealService;
import cn.org.bjca.anysign.seal.service.bean.*;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import cn.org.bjca.anysign.seal.signature.bean.*;

import cn.org.bjca.seal.esspdf.itext.bean.CertInfoBean;
import cn.org.bjca.seal.esspdf.itext.bean.RectangleBean;
import cn.org.bjca.seal.esspdf.itext.bean.SignatureField;
import cn.org.bjca.seal.esspdf.itext.bean.VerifyMessage;
import cn.org.bjca.seal.esspdf.itext.rectangle.FindTextRectangleInPDF;
import cn.org.bjca.seal.esspdf.itext.security.*;
import cn.org.bjca.seal.esspdf.itext.tools.*;
import cn.org.bjca.seal.esspdf.itext.utils.ArrayUtil;
import cn.org.bjca.seal.esspdf.itext.utils.ImageUtil;
import cn.org.bjca.seal.esspdf.itext.utils.ItextUtil;
import com.alibaba.fastjson.JSONObject;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.security.DigestAlgorithms;
import com.itextpdf.text.pdf.security.PdfPKCS7;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bjca.jce.CNPKCS7SignedData;
import org.bjca.jce.PKCS7SignedData;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.sql.Timestamp;
import java.util.*;

import static cn.org.bjca.anysign.seal.global.tools.constant.SystemConstants.DATATYPE_HASH;
import static cn.org.bjca.anysign.seal.global.tools.utils.CertUtil.getCertKeyType;

/**
 * @author zjgao
 * @create 2018/9/12.
 * @description 签章接口实现
 */

@Service
@Slf4j
public class SealServiceImpl implements ISealService {

    @Autowired
    private HttpConfig httpConfig;

    @Autowired
    private SystemRequestService systemRequestService;
    /**
     * 文件结尾标志
     */
    public static final String EOF = "%%EOF";
    /**
     *
     */
    private int counter = 0;

    @Override
    public byte[] pdfSignature(byte[] pdfBty, String appId, String deviceId, String signature,
                               String transId, FileTransmissionType fileTransmissionType, List<Seal> seals) {
        boolean isFirst = true;
        byte[] result = null;
        for (Seal seal : seals) {
            if (FileTransmissionType.PATH.ordinal() == fileTransmissionType.ordinal()) {
                DownloadRequestBean downloadRequestBean = DownloadRequestBean.build(httpConfig.appId, httpConfig.deviceId,
                        httpConfig.version, signature, seal.getSealPicId(), seal.getSealPicToken(), transId);
                byte[] sealPicContent = systemRequestService.download(downloadRequestBean);
                seal.setSealPicContent(Base64Utils.byte2Base64StringFun(sealPicContent));
            }
            if (isFirst) {
                preSign(pdfBty, seal);
                isFirst = false;
            }
            result = pdfAllSign(pdfBty, seal, appId, deviceId, transId);
            pdfBty = result;
        }
        return result;

    }

    /**
     * 处理添加水印
     *
     * @param pdfBty pdf
     * @param seal   签名实体
     * @return
     */
    private byte[] preSign(byte[] pdfBty, Seal seal) {
        try {
            boolean hasSigned = false;
            hasSigned = isSigned(pdfBty);
            // 当没有被签过名、水印模板不为空时，添加水印
            if (StringUtils.isNotEmpty(seal.getWaterMarkTemplate()) && !hasSigned) {
                pdfBty = insertWaterMark(pdfBty, seal.getWaterMarkTemplate(), seal.getWaterMarkContent());
            }
            return pdfBty;
        } catch (Exception ex) {
            log.error("PDF Presign error:", ex);
            throw new BaseRuntimeException(StatusConstants.WATERMARK_DEAL_ERROR, null, "PDF签章");
        }
    }

    /**
     * <p>产生PDF数字签名</p>
     *
     * @param pdfBty
     * @throws Exception
     * @Description:
     */
    private byte[] pdfAllSign(byte[] pdfBty, Seal seal, String appId, String deviceId, String transId) {
        log.debug("**********************************pdfAllSign begin");
        byte[] signPdf = null;
        PdfReader pdfReader = null;
        int pdfPageNum = 0;
        try {
            // **********************************PDF加密判断和获取总页码
            // 判断文档保护限制
            if (ProtectedMode.NOLIMITED.ordinal() != seal.getProtectedMode().ordinal()) {
                pdfBty = encryptedPdfByte(seal.getProtectedMode(), pdfBty);
            }
            pdfReader = new PdfReader(pdfBty);
            pdfPageNum = pdfReader.getNumberOfPages();
        } catch (Exception ex) {
            log.error("PDF File protected deal error:", ex);
            throw new BaseRuntimeException(StatusConstants.PROTECTED_DEAL_ERROR, null, "PDF签章");
        }
        // 签发骑缝章
        if (SealPositionType.CROSS_PAGE.ordinal() == seal.getSealPosition().getPositionType().ordinal()) {
            signPdf = acrossPageSign(pdfBty, pdfReader, pdfPageNum, seal, appId, deviceId, transId);
        } else { // 签发普通签章
            signPdf = commonSign(pdfBty, pdfReader, pdfPageNum, seal, appId, deviceId, transId);
        }
        if (pdfReader != null) {
            pdfReader.close();
        }

        return signPdf;
    }


    /**
     * <p>判断文档是否已签名</p>
     *
     * @param pdfBty
     * @return
     * @throws IOException
     * @Description:
     */
    public boolean isSigned(byte[] pdfBty) throws IOException {
        // 判断签名
        PdfReader reader = new PdfReader(pdfBty);
        AcroFields af = reader.getAcroFields();
        if (af.getSignatureNames().size() != 0) { // 文档已签名
            return true;
        }
        return false;
    }

    /**
     * <p>产生摘要</p>
     *
     * @param data 数据
     * @return
     * @throws Exception
     * @Description:
     */
    public static String digestSHA256(byte[] data) throws Exception {
        MessageDigest mg = MessageDigest.getInstance("SHA256", data);
        return Base64.toBase64String(mg.digest(null));
    }

    /**
     * <p>产生摘要</p>
     *
     * @param data    数据
     * @param certBty 证书
     * @return
     * @throws Exception
     * @Description:
     */
    public static String digestSM3(byte[] data, byte[] certBty) throws Exception {
        SM2PKITool tool = new SM2PKITool();
        return Base64.toBase64String(tool.digestWithCert(data, certBty));
    }


    /**
     * <p>添加水印</p>
     *
     * @param pdfBty
     * @return
     * @throws Exception
     * @Description:
     */
    private byte[] insertWaterMark(byte[] pdfBty, String waterMarkTemplate, String contents) throws Exception {
        // 添加文字水印
        CustomWaterMarkTool waterMarkTool = new CustomWaterMarkTool(pdfBty);
        waterMarkTool.addWaterMarkList(CustomWaterMarkTool.parseWaterMarkList(waterMarkTemplate, contents));
        return waterMarkTool.genPDFWaterMark();
    }


    /**
     * <p>普通签章处理</p>
     *
     * @param pdfBty     pdf字节数组
     * @param pdfReader  pdf读取对象
     * @param pdfPageNum pdf页数
     * @param seal       签章对象
     * @return byte[]  pdf签名结果
     * @Description:
     */

    private byte[] commonSign(byte[] pdfBty, PdfReader pdfReader, int pdfPageNum,
                              Seal seal, String appId, String deviceId, String transId) {

        byte[] signCertBty = Base64Utils.base64String2ByteFun(seal.getSignCert());
        String keyAlg = null;
        try {
            keyAlg = getCertKeyType(signCertBty);
        } catch (Exception e) {
            throw new BaseRuntimeException(StatusConstants.CERT_ERROR, null, "PDF签章");
        }
        String digestAlg = null;
        if (Alg.RSA.name().equals(keyAlg)) {
            digestAlg = Alg.SHA256.name();
        }
        byte[] imgBty = Base64Utils.base64String2ByteFun(seal.getSealPicContent());
        IPDFSign pdfSign = null;
        try {
            pdfSign = getCommonPDFSign(pdfBty, pdfReader, signCertBty, digestAlg, seal.isTss(), seal.getProtectedMode());
        } catch (Exception e) {
            log.error("pdf  signature  getCommonPDFSign error", e);
            throw new BaseRuntimeException(StatusConstants.GETPDFSIGN_ERROR, null, "PDF签章");
        }
        SealPosition sealPosition = seal.getSealPosition();
        SealPositionType sealPositionType = sealPosition.getPositionType();
        RelativePositionType relativePositionType = null;
        int xOffset = 0;
        int yOffset = 0;
        RectangleBean rectangle = null;
        if (SealPositionType.COORDINATE.name().equalsIgnoreCase(sealPositionType.name())) {
            //坐标
            rectangle = new RectangleBean();
            XYZRuleInfo xyzRuleInfo = sealPosition.getXyzRuleInfo();
            rectangle.setBottom(xyzRuleInfo.getBottom());
            rectangle.setLeft(xyzRuleInfo.getLeft());
            rectangle.setRight(xyzRuleInfo.getRight());
            rectangle.setTop(xyzRuleInfo.getTop());
            rectangle.setPageNo(xyzRuleInfo.getPageNo());
            relativePositionType = xyzRuleInfo.getRelativePosition();
            xOffset = xyzRuleInfo.getRightDeviation();
            yOffset = xyzRuleInfo.getLowerDeviation();
        } else if (SealPositionType.KEYWORD.name().equalsIgnoreCase(sealPositionType.name())) {
            //关键字
            KwRule kwRule = sealPosition.getKwRule();
            rectangle = this.findRectangByKeyword(pdfBty, kwRule.getKw(), kwRule.getPageNo());
            relativePositionType = kwRule.getRelativePosition();
            xOffset = kwRule.getRightDeviation();
            yOffset = kwRule.getLowerDeviation();
            if (rectangle == null) {
                throw new BaseRuntimeException(StatusConstants.KEYWORD_ERROR, null, "PDF签章");
            }
        }
        // 移动类型1:重叠、2: 居下、3:居右,默认居,右、
        //客户端上送枚举值从0开始入参需枚举值+1
        pdfSign.setMoveType(String.valueOf(relativePositionType.ordinal() + 1));
        // 上下偏移
        pdfSign.setHeightMoveSize(yOffset);
        // 左右偏移
        pdfSign.setMoveSize(xOffset);
        //是否打印印章
        if (!seal.isPrintable()) {
            pdfSign.setPrintSeal(false);
        }
        //设置签章图片
        if (imgBty != null) {
            try {
                pdfSign.setSignImg(imgBty, seal.getSealWidth(), seal.getSealHeight(), rectangle);
            } catch (Exception e) {
                log.error("set sign image error ", e);
                throw new BaseRuntimeException(StatusConstants.ADDIMAGETOPDF_ERROR, null, "PDF签章");
            }
        }
        //设置展示信息：REASON、REASON、CONTACT等
        pdfSign.setSignAppearanceInfo(seal.getReason(), seal.getLocation(), seal.getContact(), null);
        byte[] hashBty = null;
        try {
            hashBty = pdfSign.genSignHash();
        } catch (Exception e) {
            log.error("pdf signature genSignHash error ", e);
            throw new BaseRuntimeException(StatusConstants.GENSIGNHASH_ERROR, null, "PDF签章");
        }
        byte[] result = null;
        try {
            result = sign(hashBty, pdfSign, signCertBty, transId, seal.getVerifyCode(), seal.getVerifyCertPolicyId(),
                    seal.getKeyId(), appId, deviceId, keyAlg, seal.isTss());
        } catch (Exception e) {
            log.error("pdf signature sign error ", e);
            throw new BaseRuntimeException(StatusConstants.PDF_SIGN_ERROR, e.getMessage(), null, "PDF签章");
        }
        return result;

    }

    /**
     * <p>骑缝章处理</p>
     *
     * @throws Exception
     * @Description:
     */
    private byte[] acrossPageSign(byte[] pdfBty, PdfReader pdfReader, int pdfPageNum, Seal seal, String appId, String deviceId, String transId) {

        AcrossPage acrossPage = seal.getSealPosition().getAcrossPage();

        byte[] imgBty = Base64Utils.base64String2ByteFun(seal.getSealPicContent());

        List<byte[]> imgList = new ArrayList<>();
        int startWidth = 0;
        if ("L".equals(acrossPage.getAcrossPagePattern())) {
            // 左骑缝
            startWidth = 100 - acrossPage.getStartWidth();
        } else {
            //右骑缝
            startWidth = acrossPage.getStartWidth();
        }
        List<byte[]> subImglist = null;
        try {
            subImglist = ImageUtil.cutImage(imgBty, DocType.PNG.name(), startWidth);
        } catch (Exception e) {
            log.error("acrossPageSign cutImage error", e);
            throw new BaseRuntimeException(StatusConstants.ACROSS_CUTIMAGE_ERROR, null, "PDF签章");
        }
        if ("L".equals(acrossPage.getAcrossPagePattern())) {
            // 左骑缝
            imgList.add(subImglist.get(1));
            if (pdfPageNum > 1) {
                try {
                    subImglist = ImageUtil.averageCutImage(subImglist.get(0), DocType.PNG.name(), pdfPageNum - 1, false);
                } catch (Exception e) {
                    log.error("acrossPageSign averageCutImage error", e);
                    throw new BaseRuntimeException(StatusConstants.ACROSS_AVERAGE_CUTIMAGE_ERROR, null, "PDF签章");
                }
                for (int j = subImglist.size() - 1; j >= 0; j--) {
                    imgList.add(subImglist.get(j));
                }
            }
        } else {
            // 右骑缝
            imgList.add(subImglist.get(0));
            if (pdfPageNum > 1) {
                try {
                    subImglist = ImageUtil.averageCutImage(subImglist.get(1), DocType.PNG.name(), pdfPageNum - 1, true);
                } catch (Exception e) {
                    log.error("acrossPageSign averageCutImage error", e);
                    throw new BaseRuntimeException(StatusConstants.ACROSS_AVERAGE_CUTIMAGE_ERROR, null, "PDF签章");
                }
                for (byte[] b : subImglist) {
                    imgList.add(b);
                }
            }
        }
        float cutImageWidth = 0;
        float sealWidth = seal.getSealWidth();
        float sealHeight = seal.getSealHeight();
        Image img = null;
        try {
            img = Image.getInstance(imgBty);
        } catch (Exception e) {
            log.error("acrossPageSign instance image error", e);
            throw new BaseRuntimeException(StatusConstants.INSTANCEIMAGE_ERROR, null, "PDF签章");
        }
        if (sealWidth == 0) {
            sealWidth = img.getWidth();
        }
        if (sealHeight == 0) {
            sealHeight = img.getHeight();
        }

        String signCert = seal.getSignCert();
        byte[] signCertBty = Base64Utils.base64String2ByteFun(signCert);
        String alg = null;
        String digestAlg = null;
        try {
            alg = getCertKeyType(signCertBty);
        } catch (Exception e) {
            log.error("parse cert algorithm error ", e);
            throw new BaseRuntimeException(StatusConstants.DIGEST_DECRYPTION_ERROR, null, "PDF签章");
        }
        //rsa 需要设置算法，sm2则默认是sm3算法
        if (Alg.RSA.name().equalsIgnoreCase(alg)) {
            digestAlg = Alg.SHA256.name();
        }
        for (int k = 1; k <= pdfPageNum; k++) {
            cutImageWidth = this.getCutImageWidth(k, pdfPageNum, img.getWidth(), sealWidth, imgList);
            RectangleBean bean = this.getAcrossRectangle(acrossPage.getAcrossPagePattern(), acrossPage.getPosCoord(), pdfReader.getPageSizeWithRotation(k), k, cutImageWidth, sealHeight);

            // 支持首页签名和多页签名
            // 骑缝章签名方式，单页签名, 多页签名，默认为单页签名
            // 多页签名
            if (AcrossPageType.MULTI_PAGE.name().equalsIgnoreCase(acrossPage.getAcrossPageType().name())) {
                byte[] signPart = acrossPageSignPart(pdfBty, seal.getProtectedMode(), bean, pdfPageNum, signCertBty,
                        seal.getKeyId(), seal.getVerifyCode(), seal.getVerifyCertPolicyId(), appId, deviceId, transId,
                        alg, digestAlg, imgList.get(k - 1), pdfReader, seal.isTss(), seal.isPrintable(), seal.getReason(), seal.getLocation(), seal.getContact());
                pdfBty = signPart;
            } else { // 首页签名其他页只加印章图片
                if (k > 1) {
                    try {
                        pdfBty = insertImgFromPdfGeneral(pdfBty, imgList.get(k - 1), cutImageWidth, sealHeight, bean);
                    } catch (Exception e) {
                        throw new BaseRuntimeException("insert image to pdf error ", e);
                    }

                }
                // 最后一页，加上首页签名
                if (k == pdfPageNum) {
                    cutImageWidth = this.getCutImageWidth(1, pdfPageNum, img.getWidth(), sealWidth, imgList);
                    bean = this.getAcrossRectangle(acrossPage.getAcrossPagePattern(), acrossPage.getPosCoord(), pdfReader.getPageSizeWithRotation(1), 1, cutImageWidth, sealHeight);
                    byte[] signPart = acrossPageSignPart(pdfBty, seal.getProtectedMode(), bean, pdfPageNum, signCertBty, seal.getKeyId(), seal.getVerifyCode(),
                            seal.getVerifyCertPolicyId(), appId, deviceId, transId, alg, digestAlg, imgList.get(0), pdfReader, seal.isTss(), seal.isPrintable(),
                            seal.getReason(), seal.getLocation(), seal.getContact());
                    pdfBty = signPart;
                }
            }
        }
        return pdfBty;
    }


    /**
     * <p>插入图片到pdf,根据传入的矩形区域</p>
     *
     * @param pdfBty
     * @param imgBty
     * @param rectangleBean
     * @return
     * @throws Exception
     * @Description:
     */
    protected byte[] insertImgFromPdfGeneral(byte[] pdfBty, byte[] imgBty, float imgWidth, float imgHeight, RectangleBean rectangleBean) throws Exception {
        ByteArrayOutputStream out = null;
        byte[] outPdfBty = null;
        PdfReader pdfReader = null;
        try {
            Image image = Image.getInstance(imgBty);
            Rectangle rectangle = new Rectangle(rectangleBean.getLeft(), rectangleBean.getTop(), rectangleBean.getRight(), rectangleBean.getBottom());
            out = new ByteArrayOutputStream();
            pdfReader = new PdfReader(pdfBty);

            //页码如果为负数时，则定位到倒数的页数。 例如-1，则是倒数第一页
            int pdfPageNum = pdfReader.getNumberOfPages();
            if (rectangleBean.getPageNo() < 0) {
                rectangleBean.setPageNo(pdfPageNum + rectangleBean.getPageNo() + 1);
            }

            final PdfStamper pdfStamper = new PdfStamper(pdfReader, out, (char) 0, true);
            PdfWriter writer = pdfStamper.getWriter();
            PdfTemplate template = PdfTemplate.createTemplate(writer, imgWidth, imgHeight);
            template.addImage(image, imgWidth, 0, 0, imgHeight, 0, 0);
            PdfAnnotation annotation = PdfAnnotation.createStamp(writer, rectangle, null, "img");
            annotation.setFlags(PdfAnnotation.FLAGS_PRINT | PdfAnnotation.FLAGS_LOCKED);
            annotation.put(PdfName.SUBTYPE, new PdfName("BJCA:Image"));
            annotation.setAppearance(PdfName.N, template);
            pdfStamper.addAnnotation(annotation, rectangleBean.getPageNo());
            if (pdfStamper != null) {
                pdfStamper.close();
            }
            outPdfBty = out.toByteArray();
        } finally {
            if (pdfReader != null) {
                pdfReader.close();
            }
            IOUtils.closeQuietly(out);
        }
        return outPdfBty;
    }

    private byte[] acrossPageSignPart(byte[] pdfBty, ProtectedMode protectedMode, RectangleBean bean, int pdfPageNum,
                                      byte[] signCertBty, String keyId, String verifyCode, String verifyCertPolicyId, String appId,
                                      String deviceId, String transId, String alg, String digestAlg, byte[] imgBty, PdfReader pdfReader,
                                      boolean isTss, boolean isPrintable, String reason, String location, String contact) {
        //protectedMode 保护模式枚举从0开始，业务上从1开始，枚举需要+1
        IPDFSign pdfSign = null;
        try {
            pdfSign = getAcrossPDFSign(pdfBty, pdfReader, signCertBty, digestAlg, isTss, protectedMode.ordinal());
        } catch (Exception e) {
            log.error("across page sign getAcrossPDFSign error ", e);
            throw new BaseRuntimeException(StatusConstants.GETPDFSIGN_ERROR, null, "PDF签章");
        }
        pdfSign.setPrintSeal(isPrintable);
        // *****************设置图片及坐标
        try {
            pdfSign.setAcrossSignImg(imgBty, bean);
        } catch (Exception e) {
            log.error("across page sign setAcrossSignImg error ", e);
            throw new BaseRuntimeException(StatusConstants.ADDIMAGETOPDF_ERROR, null, "PDF签章");
        }
        // **********************展示信息
        pdfSign.setSignAppearanceInfo(reason, location, contact, null);
        // *********************产生hash数据
        byte[] hashBty = null;
        try {
            hashBty = pdfSign.genSignHash();
        } catch (Exception e) {
            log.error("pdf signature genSignHash error ", e);
            throw new BaseRuntimeException(StatusConstants.GENSIGNHASH_ERROR, null, "PDF签章");
        }
        byte[] signedBty = null;
        try {
            signedBty = sign(hashBty, pdfSign, signCertBty, transId, verifyCode, verifyCertPolicyId, keyId, appId, deviceId, alg, isTss);
        } catch (Exception e) {
            log.error("pdf signature sign error ", e);
            throw new BaseRuntimeException(StatusConstants.PDF_SIGN_ERROR, null, "PDF签章");
        }
        return signedBty;
    }


    /**
     * @param hashBty            hash值
     * @param pdfSign            pdf签名对象
     * @param signCertBty        签名证书
     * @param transId
     * @param verifyCode
     * @param verifyCertPolicyId 验证策略
     * @param keyId              秘钥id
     * @param appId
     * @param deviceId
     * @param keyAlg             秘钥算法
     * @param isTSSign           是否时间戳签名
     * @return 返回签名pdf信息
     * @throws Exception
     */
    private byte[] sign(byte[] hashBty, IPDFSign pdfSign, byte[] signCertBty, String transId, String verifyCode,
                        String verifyCertPolicyId, String keyId, String appId, String deviceId, String keyAlg,
                        boolean isTSSign) throws Exception {
        // **********************签名
        // 签名数据
        byte[] signData = null;
        // 签名后的pdf
        byte[] result = null;
        String data = null;
        DepositCertSignRequestBean depositCertSignRequestBean = null;
        if (Alg.RSA.name().equals(keyAlg)) {
            data = digestSHA256(hashBty);
            depositCertSignRequestBean = DepositCertSignRequestBean.build(appId, deviceId, SystemConstants.SIGNALGO_HMAC,
                    SystemConstants.VERSION_1, data, SystemConstants.DATATYPE_HASH, keyId, Alg.SHA256withRSA.name(),
                    SystemConstants.SIGNRESULTTYPE_P1, transId, verifyCode, verifyCertPolicyId);
        } else {
            data = digestSM3(hashBty, signCertBty);
            depositCertSignRequestBean = DepositCertSignRequestBean.build(appId, deviceId, SystemConstants.SIGNALGO_HMAC,
                    SystemConstants.VERSION_1, data, DATATYPE_HASH, keyId, Alg.SM3withSM2.name(),
                    SystemConstants.SIGNRESULTTYPE_P1, transId, verifyCode, verifyCertPolicyId);
        }
        signData = this.localSign(hashBty, depositCertSignRequestBean, keyAlg, getPKITool(signCertBty), signCertBty, isTSSign);
        if (signData == null) {
            log.error("local sign error,result（signData） is null");
            throw new BaseRuntimeException(StatusConstants.PDF_SIGNDATA_ISNULL, null, "PDF签章");
        }
        // RSA、SM2时间戳签名
        byte[] tsRespBty = null;
        // 时间戳签名
        if (isTSSign) {
            // 时间戳请求
            byte[] reqBty = pdfSign.genTSAReq(signData);
            tsRespBty = this.tssServerSign(reqBty, keyAlg, transId);
            if (tsRespBty == null) {
                log.error("tssServerSign sign error,result（tsRespBty） is null");
                throw new BaseRuntimeException(StatusConstants.TSS_SIGNATURE_PARSE_ERROR, null, "PDF签章");
            }
        }
        result = pdfSign.signDetached(signData, tsRespBty);
        return result;
    }

    /**
     * <p>PDF服务器签名（本地签名）</p>
     *
     * @param hashBty                    hash数据
     * @param depositCertSignRequestBean 签名请求bean
     * @param alg                        密钥算法
     * @param certBty                    证书字节数组
     * @param isTSSign                   是否进行时间戳签名，true：是，false：否
     * @return
     * @Description:用于PDF签章
     */
    protected byte[] localSign(byte[] hashBty, DepositCertSignRequestBean depositCertSignRequestBean, String alg,
                               IPKITool tool, byte[] certBty, boolean isTSSign) {
        byte[] signData = null;
        PKIToolTemplate pkiToolTemplate = new PKIToolTemplate(systemRequestService);
        try {
            pkiToolTemplate.setDepositCertSignRequestBean(depositCertSignRequestBean);
            // RSA
            if (Alg.RSA.name().equals(alg)) {
                signData = tool.sign(hashBty, pkiToolTemplate);
            } else { // SM2 不带属性的签名
                if (!isTSSign) {
                    signData = tool.p7Sign(hashBty, certBty, false, pkiToolTemplate);
                } else { // 带属性的签名
                    signData = tool.p7SignWithAttr(hashBty, certBty, pkiToolTemplate);
                }
            }
        } catch (Exception ex) {
            log.error("localSign error:", ex);
            throw new BaseRuntimeException(ex);
        }
        return signData;
    }

    /**
     * <p>时间戳签名</p>
     *
     * @param tsReq 时间戳签名请求对象
     * @param alg   算法
     * @return
     * @Description:
     */
    protected byte[] tssServerSign(byte[] tsReq, String alg, String transId) throws Exception {
        JSONObject jsonObject = null;
        byte[] tspResponseBty = null;
        if (Alg.RSA.name().equals(alg)) {
            GenTsRespRequestBean genTsRespRequestBean = GenTsRespRequestBean.build(transId, httpConfig.appId, Alg.SHA256withRSA.name(),
                    Base64Utils.byte2Base64StringFun(tsReq));
            jsonObject = systemRequestService.genTsResp(genTsRespRequestBean);
        } else {
            GenTsRespRequestBean genTsRespRequestBean = GenTsRespRequestBean.build(transId, httpConfig.appId, Alg.SM3withSM2.name(),
                    Base64Utils.byte2Base64StringFun(tsReq));
            jsonObject = systemRequestService.genTsResp(genTsRespRequestBean);
        }
        if (null != jsonObject) {
            int status = Integer.parseInt(jsonObject.getString("status"));
            if (Integer.parseInt(StatusConstants.SUCCESS.getStatus()) == status) {
                JSONObject data = jsonObject.getJSONObject("data");
                String signValue = data.getString("result");
                tspResponseBty = Base64Utils.base64String2ByteFun(signValue);
            } else {
                log.error("invoke tss micro service error, status code : {},message : {}", status, jsonObject.getString("message"));
                throw new BaseRuntimeException(StatusConstants.TSS_SIGNATURE_ERROR, null, "PDF签章");
            }
        }
        return tspResponseBty;
    }


    /**
     * <p>根据证书获取相应的PKI工具</p>
     *
     * @param signCertBty 证书字节数组
     * @return
     * @Description:
     */
    protected IPKITool getPKITool(byte[] signCertBty) throws Exception {
        if (getCertKeyType(signCertBty).equals(ESSPdfConstants.CERT_TYPE_RSA)) {
            return new RSAPKITool(Alg.SHA256WithRSA.name());
        } else {
            return new SM2PKITool();
        }
    }

    private float getCutImageWidth(int k, int pdfPageNum, float imageWidth, float sealWidth, List<byte[]> splitImages) {
        float cutImageWidth = 0f;
        InputStream in = null;
        BufferedImage image = null;
        try {
            in = new ByteArrayInputStream(splitImages.get(k - 1));
            image = ImageIO.read(in);
            cutImageWidth = sealWidth / imageWidth * image.getWidth();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(in);
        }
        return cutImageWidth;
    }


    /**
     * <p>骑缝章获取矩形区域</p>
     *
     * @param acrossPagePattern 骑缝类型，左骑缝或右骑缝
     * @param posCoord          高度比例
     * @param rectangle         PDF页面矩形
     * @param k                 页码
     * @param cutImageWidth     宽度
     * @param imageHeight       高度
     * @return 矩形区域
     * @throws
     * @Description:
     */
    private RectangleBean getAcrossRectangle(String acrossPagePattern, int posCoord, Rectangle rectangle, int k, float cutImageWidth, float imageHeight) {
        float pageWidth = rectangle.getWidth();
        float yDistanceOrigin = 0;
        // 如果Y轴高度为100%时，印章图片置顶
        if (posCoord == 10000) {
            yDistanceOrigin = rectangle.getHeight() - imageHeight;
        } else {
            // Y轴高度
            yDistanceOrigin = rectangle.getHeight() * posCoord / 10000;
            if ((yDistanceOrigin + imageHeight) > rectangle.getHeight()) {
                yDistanceOrigin -= imageHeight;
            }
        }
        RectangleBean bean = createRectangleBean(k, pageWidth - cutImageWidth, yDistanceOrigin, pageWidth, yDistanceOrigin + imageHeight);
        // 左骑缝
        if ("L".equals(acrossPagePattern)) { // 左骑缝
            bean = createRectangleBean(k, 0, yDistanceOrigin, cutImageWidth, yDistanceOrigin + imageHeight);
        }
        return bean;
    }

    /**
     * <p>创建矩形区域</p>
     *
     * @param pageNum 页码
     * @param llx     最左端坐标
     * @param lly     最上端坐标
     * @param urx     最右端坐标
     * @param ury     最底端坐标
     * @return
     * @throws
     * @Description:
     */
    protected RectangleBean createRectangleBean(int pageNum, float llx, float lly, float urx, float ury) {
        RectangleBean rectangleBean = new RectangleBean();
        rectangleBean.setPageNo(pageNum);
        rectangleBean.setLeft(llx);
        rectangleBean.setTop(lly);
        rectangleBean.setRight(urx);
        rectangleBean.setBottom(ury);
        return rectangleBean;
    }

    @Override
    public PdfDigest pdfDigest(String appId, String transId, byte[] pdfBty,
                               FileTransmissionType fileTransmissionType,
                               ProtectedMode protectedMode, Seal seal) {
        PdfDigest pdfDigest = new PdfDigest();
        String alg = null;
        int keySize = 0;
        String digestAlg = null;
        String signCertBty = seal.getSignCert();
        if (StringUtils.isNotEmpty(signCertBty)) {
            try {
                alg = CertUtil.getCertKeyType(Base64Utils.base64String2ByteFun(signCertBty));
                keySize = CertUtil.getCertKeySize(Base64Utils.base64String2ByteFun(signCertBty));
                if (Alg.RSA.name().equals(alg)) {
                    digestAlg = Alg.SHA256.name();
                } else {
                    digestAlg = Alg.SM3.name();
                }
            } catch (Exception e) {
                log.error("transId is {} , Certificate decryption failed ! ", transId);
                throw new BaseRuntimeException(StatusConstants.DIGEST_DECRYPTION_ERROR, null, "PDF文档摘要");
            }
        } else {
            log.error("pdfDigest SignCert is not null ! transId is {}", transId);
            throw new BaseRuntimeException(StatusConstants.APPENDIXCONTENT_NOTNULL, null, "PDF文档摘要");
        }
        PdfReader pdfReader = null;
        try {
            pdfReader = new PdfReader(pdfBty);
        } catch (IOException e) {
            log.error("build PdfReader error ", e);
            throw new BaseRuntimeException(StatusConstants.PDF_READER_ERROR, null, "PDF文档摘要");
        }
        byte[] sealImg = null;
        if (FileTransmissionType.PATH.name().equalsIgnoreCase(fileTransmissionType.name())) {
            DownloadRequestBean downloadRequestBean = DownloadRequestBean.build(httpConfig.appId,
                    httpConfig.deviceId, httpConfig.version, httpConfig.secret, seal.getSealPicId(),
                    seal.getSealPicToken(), transId);
            sealImg = systemRequestService.download(downloadRequestBean);
        } else {
            sealImg = Base64Utils.base64String2ByteFun(seal.getSealPicContent());
        }
        //根据算法预留签名结果长度128,256,72
        byte[] signBty = null;
        if (Alg.RSA.name().equals(alg)) {
            if (1024 == keySize) {
                signBty = new byte[128];
            } else {
                signBty = new byte[256];
            }
        } else {
            signBty = new byte[72];
        }
        //构造PDFSign,设置签名算法
        IPDFSign pdfSign = null;
        try {
            //protectedMode 保护模式枚举从0开始，业务上从1开始，枚举需要+1
            pdfSign = getCommonPDFSign(pdfBty, pdfReader, Base64Utils.base64String2ByteFun(signCertBty), digestAlg,
                    seal.isTss(), protectedMode);
        } catch (Exception e) {
            log.error("build pdfSign error ", e);
            throw new BaseRuntimeException(StatusConstants.GETPDFSIGN_ERROR, null, "PDF文档摘要");
        }
        SealPosition sealPosition = seal.getSealPosition();
        RectangleBean rectangle = null;
        SealPositionType sealPositionType = sealPosition.getPositionType();
        RelativePositionType relativePositionType = null;
        int xOffset = 0;
        int yOffset = 0;
        //设置签章图片位置及偏移信息 （区分骑缝还是非骑缝）
        if (SealPositionType.CROSS_PAGE.name().equalsIgnoreCase(sealPositionType.name())) {
            int pdfPageNum = pdfReader.getNumberOfPages();
            AcrossPage acrossPage = seal.getSealPosition().getAcrossPage();

            byte[] imgBty = Base64Utils.base64String2ByteFun(seal.getSealPicContent());

            List<byte[]> imgList = new ArrayList<>();
            int startWidth = 0;
            if ("L".equals(acrossPage.getAcrossPagePattern())) {
                // 左骑缝
                startWidth = 100 - acrossPage.getStartWidth();
            } else {
                //右骑缝
                startWidth = acrossPage.getStartWidth();
            }
            List<byte[]> subImglist = null;
            try {
                subImglist = ImageUtil.cutImage(imgBty, DocType.PNG.name(), startWidth);
            } catch (Exception e) {
                log.error("acrossPageSign cutImage error", e);
                throw new BaseRuntimeException(StatusConstants.ACROSS_CUTIMAGE_ERROR, null, "PDF签章");
            }
            if ("R".equals(acrossPage.getAcrossPagePattern())) {
                // 右骑缝
                imgList.add(subImglist.get(0));
                if (pdfPageNum > 1) {
                    try {
                        subImglist = ImageUtil.averageCutImage(subImglist.get(1), DocType.PNG.name(), pdfPageNum - 1, true);
                    } catch (Exception e) {
                        log.error("acrossPageSign averageCutImage error", e);
                        throw new BaseRuntimeException(StatusConstants.ACROSS_AVERAGE_CUTIMAGE_ERROR, null, "PDF签章");
                    }
                    for (byte[] b : subImglist) {
                        imgList.add(b);
                    }
                }
            } else {
                // 左骑缝
                imgList.add(subImglist.get(1));
                if (pdfPageNum > 1) {
                    try {
                        subImglist = ImageUtil.averageCutImage(subImglist.get(0), DocType.PNG.name(), pdfPageNum - 1, false);
                    } catch (Exception e) {
                        log.error("acrossPageSign averageCutImage error", e);
                        throw new BaseRuntimeException(StatusConstants.ACROSS_AVERAGE_CUTIMAGE_ERROR, null, "PDF签章");
                    }
                    for (int j = subImglist.size() - 1; j >= 0; j--) {
                        imgList.add(subImglist.get(j));
                    }
                }
            }
            float cutImageWidth = 0;
            float sealWidth = seal.getSealWidth();
            float sealHeight = seal.getSealHeight();
            Image img = null;
            try {
                img = Image.getInstance(imgBty);
            } catch (Exception e) {
                log.error("acrossPageSign instance image error", e);
                throw new BaseRuntimeException(StatusConstants.INSTANCEIMAGE_ERROR, null, "PDF文档摘要");
            }
            if (sealWidth == 0) {
                sealWidth = img.getWidth();
            }
            if (sealHeight == 0) {
                sealHeight = img.getHeight();
            }
            for (int k = 1; k <= pdfPageNum; k++) {
                cutImageWidth = this.getCutImageWidth(k, pdfPageNum, img.getWidth(), sealWidth, imgList);
                RectangleBean bean = this.getAcrossRectangle(acrossPage.getAcrossPagePattern(), acrossPage.getPosCoord(), pdfReader.getPageSizeWithRotation(k), k, cutImageWidth, sealHeight);
                // 支持首页签名和多页签名
                // 骑缝章签名方式，单页签名, 多页签名，默认为单页签名
                // 多页签名
                if (AcrossPageType.MULTI_PAGE.name().equalsIgnoreCase(acrossPage.getAcrossPageType().toString())) {
                    try {
                        pdfSign = getAcrossPDFSign(pdfBty, pdfReader, Base64Utils.base64String2ByteFun(signCertBty), digestAlg, seal.isTss(), protectedMode.ordinal());
                    } catch (Exception e) {
                        log.error("across page sign getAcrossPDFSign error ", e);
                        throw new BaseRuntimeException(StatusConstants.GETPDFSIGN_ERROR, null, "PDF文档摘要");
                    }
                    pdfSign.setPrintSeal(seal.isPrintable());
                    // *****************设置图片及坐标
                    try {
                        pdfSign.setAcrossSignImg(imgBty, bean);
                    } catch (Exception e) {
                        log.error("across page sign setAcrossSignImg error ", e);
                        throw new BaseRuntimeException(StatusConstants.ADDIMAGETOPDF_ERROR, null, "PDF文档摘要");
                    }
                    // **********************展示信息
                    pdfSign.setSignAppearanceInfo(seal.getReason(), seal.getLocation(), seal.getContact(), null);
                } else { // 首页签名其他页只加印章图片
                    if (k > 1) {
                        try {
                            pdfBty = insertImgFromPdfGeneral(pdfBty, imgList.get(k - 1), cutImageWidth, sealHeight, bean);
                        } catch (Exception e) {
                            throw new BaseRuntimeException("insert image to pdf error ", e);
                        }

                    }
                    // 最后一页，加上首页签名
                    if (k == pdfPageNum) {
                        cutImageWidth = this.getCutImageWidth(1, pdfPageNum, img.getWidth(), sealWidth, imgList);
                        bean = this.getAcrossRectangle(acrossPage.getAcrossPagePattern(), acrossPage.getPosCoord(), pdfReader.getPageSizeWithRotation(1), 1, cutImageWidth, sealHeight);
                        try {
                            pdfSign = getAcrossPDFSign(pdfBty, pdfReader, Base64Utils.base64String2ByteFun(signCertBty), digestAlg, seal.isTss(), protectedMode.ordinal());
                        } catch (Exception e) {
                            log.error("across page sign getAcrossPDFSign error ", e);
                            throw new BaseRuntimeException(StatusConstants.GETPDFSIGN_ERROR, null, "PDF文档摘要");
                        }
                        pdfSign.setPrintSeal(seal.isPrintable());
                        // *****************设置图片及坐标
                        try {
                            pdfSign.setAcrossSignImg(imgList.get(0), bean);
                        } catch (Exception e) {
                            log.error("across page sign setAcrossSignImg error ", e);
                            throw new BaseRuntimeException(StatusConstants.ADDIMAGETOPDF_ERROR, null, "PDF文档摘要");
                        }
                        // **********************展示信息
                        pdfSign.setSignAppearanceInfo(seal.getReason(), seal.getLocation(), seal.getContact(), null);
                    }
                }
            }
        } else {
            if (SealPositionType.COORDINATE.name().equalsIgnoreCase(sealPositionType.name())) {
                //坐标
                rectangle = new RectangleBean();
                XYZRuleInfo xyzRuleInfo = sealPosition.getXyzRuleInfo();
                rectangle.setBottom(xyzRuleInfo.getBottom());
                rectangle.setLeft(xyzRuleInfo.getLeft());
                rectangle.setRight(xyzRuleInfo.getRight());
                rectangle.setTop(xyzRuleInfo.getTop());
                rectangle.setPageNo(xyzRuleInfo.getPageNo());
                relativePositionType = xyzRuleInfo.getRelativePosition();
                xOffset = xyzRuleInfo.getRightDeviation();
                yOffset = xyzRuleInfo.getLowerDeviation();
            } else if (SealPositionType.KEYWORD.name().equalsIgnoreCase(sealPositionType.name())) {
                //关键字
                KwRule kwRule = sealPosition.getKwRule();
                rectangle = this.findRectangByKeyword(pdfBty, kwRule.getKw(), kwRule.getPageNo());
                relativePositionType = kwRule.getRelativePosition();
                xOffset = kwRule.getRightDeviation();
                yOffset = kwRule.getLowerDeviation();
                if (rectangle == null) {
                    throw new BaseRuntimeException(StatusConstants.KEYWORD_ERROR, null, "PDF签章");
                }
            }
            // 移动类型1:重叠、2: 居下、3:居右,默认居右、4:居右下
            //客户端上送枚举值从0开始入参需枚举值+1
            pdfSign.setMoveType(String.valueOf(relativePositionType.ordinal() + 1));
            // 上下偏移
            pdfSign.setHeightMoveSize(yOffset);
            // 左右偏移
            pdfSign.setMoveSize(xOffset);
            //是否打印印章
            pdfSign.setPrintSeal(seal.isPrintable());
            //设置签章图片
            if (sealImg != null && sealImg.length > 0) {
                try {
                    pdfSign.setSignImg(sealImg, seal.getSealWidth(), seal.getSealHeight(), rectangle);
                } catch (Exception e) {
                    log.error("set sign image error ", e);
                    throw new BaseRuntimeException(StatusConstants.ADDIMAGETOPDF_ERROR, null, "PDF文档摘要");
                }
            }
            //设置展示信息：REASON、REASON、CONTACT等
            pdfSign.setSignAppearanceInfo(seal.getReason(), seal.getLocation(), seal.getContact(), "".getBytes());
        }
        // 插入临时签名值
        for (int i = 0; i < signBty.length; i++) {
            signBty[i] = 16;
        }
        // 设置时间戳
        if (seal.isTss()) {
            TSAClient tsaClient = new TSAClientImpl();
            pdfSign.setTsaClient(tsaClient);
        }
        //产生pdf 文档 待hash数据
        byte[] hashBty = null;
        try {
            hashBty = pdfSign.genSignHash();
        } catch (Exception e) {
            log.error("generate digest error step 1 ：", e);
            throw new BaseRuntimeException(StatusConstants.GENSIGNHASH_ERROR, null, "PDF文档摘要");
        }
        try {
            // RSA
            if (Alg.RSA.name().equals(alg)) {
                hashBty = DigestAlgorithms.digest(new ByteArrayInputStream(hashBty), digestAlg, "BC");
            } else { // SM2
                SM2PKITool sm2Tool = new SM2PKITool();
                hashBty = sm2Tool.digestWithCert(hashBty, Base64.decode(signCertBty));
                signBty = sm2Tool.p7SignWithP1(signBty, Base64.decode(signCertBty));
            }
        } catch (Exception e) {
            log.error("generate digest error step 2 for {}", alg, e);
            throw new BaseRuntimeException(StatusConstants.FAIL, null, "PDF签章");
        }

        byte[] signPdf = null;
        try {
            signPdf = pdfSign.signDetached(signBty, null);
        } catch (Exception e) {
            log.error("combine pdf and digest error ", e);
            throw new BaseRuntimeException(StatusConstants.PDF_SIGN_ERROR, null, "PDF文档摘要");
        }
        if (null != pdfReader) {
            pdfReader.close();
        }
        pdfDigest.setHash(hashBty);
        pdfDigest.setPdfBty(signPdf);
        return pdfDigest;
    }

    @Override
    public byte[] sealMerging(byte[] signBty, byte[] pdfBty) {
        byte[] allBty = null;
        try {
            int n = 0;
            StringBuffer signTem = new StringBuffer();
            if (signBty.length > 72 && signBty.length == 128) {
                n = 128;
            } else if (signBty.length > 72 && signBty.length == 256) {
                n = 256;
            } else { // SM2 签名不足72，进行补位
                n = 72;
                if (signBty.length < 72) {
                    byte[] bty = new byte[72];
                    System.arraycopy(signBty, 0, bty, 0, signBty.length);
                    for (int k = signBty.length; k < 72; k++) {
                        bty[k] = 0;
                    }
                    signBty = bty;
                }
            }
            for (int j = 0; j < n; j++) {
                signTem.append("10");
            }

            String sign = new DEROctetString(signBty).toString();
            sign = sign.replaceAll("#", "").trim();
            allBty = getPDF(signBty, pdfBty, signTem, sign);

        } catch (Exception e) {
            log.error("merge pdf error ", e);
            throw new BaseRuntimeException(StatusConstants.FAIL, "PDF文档合章错误", null, "PDF文档合章");
        }
        return allBty;
    }

    /**
     * PDF 保护模式处理
     *
     * @param protectedMode 类型
     * @param pdfBty        PDF
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    private byte[] encryptedPdfByte(ProtectedMode protectedMode, byte[] pdfBty) throws DocumentException, IOException {
        ByteArrayOutputStream out = null;
        PdfReader reader = new PdfReader(pdfBty);
        int order = protectedMode.ordinal();
        int permissions = 0;
        try {

            if (ProtectedMode.NOLIMITED.ordinal() != order) {
                //0 无限制
                if (ProtectedMode.COPYABLE.ordinal() == order) {// 1、复制
                    permissions = PdfWriter.ALLOW_COPY;
                } else if (ProtectedMode.PRINTABLE.ordinal() == order) {// 2、打印
                    permissions = PdfWriter.ALLOW_PRINTING;
                } else if (ProtectedMode.COPYABLEANDPRINTABLE.ordinal() == order) {// 3、复制与打印
                    permissions = PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING;
                } else {// 4、全部禁止
                    permissions = PdfWriter.ALLOW_FILL_IN;
                }
            }
            out = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, out);
            stamper.setEncryption(null, null, permissions, PdfWriter.ENCRYPTION_AES_128 | PdfWriter.DO_NOT_ENCRYPT_METADATA);
            stamper.close();
            pdfBty = out.toByteArray();
        } finally {
            reader.close();
            IOUtils.closeQuietly(out);
        }
        return pdfBty;
    }

    /**
     * <p>PDF客户端签章处理</p>
     *
     * @param clientSignBty 客户端签名数据
     * @return
     * @Description:
     */
    private byte[] getPDF(byte[] clientSignBty, byte[] pdfBty, StringBuffer signTem, String sign) throws Exception {
        byte[] allBty = null;// 客户端pdf签名文件
        int begin = 0;
        int end = 0;
        if (clientSignBty.length > 72) {
            PdfReader reader = new PdfReader(pdfBty);
            AcroFields af = reader.getAcroFields();
            ArrayList<String> names = af.getSignatureNames();
            PdfDictionary pdfDictionary = af.getSignatureDictionary(names.get(names.size() - 1));
            PdfArray arr = pdfDictionary.getAsArray(new PdfName("ByteRange"));
            String arrData = arr.toString();
            String[] data = arrData.split(",");
            begin = Integer.parseInt(data[1].trim());
            end = Integer.parseInt(data[2].trim());
        } else {
            PDFSM2VerifyTool tool = new PDFSM2VerifyTool();
            List<SignatureField> beanList = tool.getFields(pdfBty);
            begin = beanList.get(beanList.size() - 1).getByteRange()[1];
            end = beanList.get(beanList.size() - 1).getByteRange()[2];
        }
        int length = end - begin;
        allBty = new byte[pdfBty.length];
        System.arraycopy(pdfBty, 0, allBty, 0, begin);
        byte[] optBty = new byte[length];
        System.arraycopy(pdfBty, begin, optBty, 0, length);
        String optStr = new String(optBty);
        int signLength = sign.toString().length();
        for (int k = signLength; k < signTem.toString().length(); k++) {
            sign += "0";
        }
        optStr = optStr.replace(signTem, sign);
        optBty = optStr.getBytes();
        System.arraycopy(optBty, 0, allBty, begin, length);
        System.arraycopy(pdfBty, end, allBty, end, allBty.length - end);
        return allBty;
    }

    /**
     * <p>普通签章获取PDF签名对象</p>
     *
     * @param pdfBty      pdf文档内容
     * @param pdfReader   pdf读取对象
     * @param signCertBty 签名证书
     * @return IPDFSign pdf签名对象
     * @throws Exception
     * @Description:适用于无签章规则的签章业务
     */
    protected IPDFSign getCommonPDFSign(byte[] pdfBty, PdfReader pdfReader, byte[] signCertBty, String digestAlg, boolean isTss, ProtectedMode protectedMode) throws Exception {
        IPDFSign pdfSign = null;
        int permissions = 0;
        if (ProtectedMode.COPYABLE.name().equals(protectedMode.name())) {// 2、复制
            permissions = PdfWriter.ALLOW_COPY;
        } else if (ProtectedMode.PRINTABLE.name().equals(protectedMode.name())) {// 3、打印
            permissions = PdfWriter.ALLOW_PRINTING;
        } else if (ProtectedMode.COPYABLEANDPRINTABLE.name().equals(protectedMode.name())) {// 4、复制与打印
            permissions = PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING;
        } else if (ProtectedMode.ALLFORBIDEN.name().equals(protectedMode.name())) {// 5、全部禁止
            permissions = PdfWriter.ALLOW_FILL_IN;
        }
        // RSA
        if (getCertKeyType(signCertBty).equals(Alg.RSA.name())) {
            pdfSign = new PDFSign(pdfReader, CertUtil.getSelfChain(signCertBty), permissions);
            if (StringUtils.isNotBlank(digestAlg)) {
                pdfSign.setHashAlgorithm(digestAlg);
            } else {
                pdfSign.setHashAlgorithm(CertUtil.HASH_SHA256);
            }
            // 时间戳签名
            if (isTss) {
                TSAClient tsaClient = new TSAClientImpl();
                // 设置时间戳
                pdfSign.setTsaClient(tsaClient);
            }
        } else { // SM2
            pdfSign = new PDFSM2Sign(pdfBty, signCertBty, permissions);
        }
        return pdfSign;
    }

    /**
     * <p>骑缝章获取PDF签名对象</p>
     *
     * @throws Exception
     * @Description:
     */
    protected IPDFSign getAcrossPDFSign(byte[] pdfBty, PdfReader pdfReader, byte[] signCertBty, String digestAlg, boolean isTss, int permissions) throws Exception {
        IPDFSign pdfSign = null;
        pdfReader = new PdfReader(pdfBty);
        permissions = 0;
        if (getCertKeyType(signCertBty).equals(ESSPdfConstants.CERT_TYPE_RSA)) { // RSA
            pdfSign = new PDFSign(pdfReader, CertUtil.getSelfChain(signCertBty), permissions);
            pdfSign.setHashAlgorithm(digestAlg);
            if (isTss) { // 时间戳签名
                TSAClient tsaClient = new TSAClientImpl();
                pdfSign.setTsaClient(tsaClient); // 设置时间戳
            }
        } else { // SM2
            pdfSign = new PDFSM2Sign(pdfBty, signCertBty, permissions);
        }
        return pdfSign;
    }

    /**
     * <p>根据关键字搜索坐标</p>
     *
     * @param pdfBty      PDF文件
     * @param keyword     关键字
     * @param searchOrder 搜索顺序( 2倒叙搜索)
     * @return
     * @Description:查找匹配的第一个关键字
     */
    protected RectangleBean findRectangByKeyword(byte[] pdfBty, String keyword, String searchOrder) {
        RectangleBean rectangle = null;
        try {
            FindTextRectangleInPDF ft = new FindTextRectangleInPDF(pdfBty);
            if (ESSPdfConstants.SEARCHORDER_REVERSE.equals(searchOrder)) {
                // 倒序搜索
                ft.setPositive(false);
            }
            ft.setKeyWord(keyword);
            rectangle = ft.getPDFRectang();
        } catch (Exception ex) {
            log.error("findRectangByKeyword error:", ex);
        }
        return rectangle;
    }

    @Override
    public List<VerifyMessage> sealVerify(String appId, String transId, byte[] pdfBty, Alg alg) {
        //返回对象
        List<VerifyMessage> verifyMessages = new LinkedList<VerifyMessage>();
        // 验证是否为PDF文件
        try {
            ItextUtil.getPDFPageNo(pdfBty);
        } catch (Exception ex) {
            log.error("PDF File error:", ex);
            throw new BaseRuntimeException(StatusConstants.FILE_ERROR, null, "PDF签章");
        }
        PdfReader reader = null;
        try {
            reader = new PdfReader(pdfBty);
            List<SignatureField> fields = new ArrayList<SignatureField>();
            PDFRSAVerifyTool rsaTool = new PDFRSAVerifyTool();
            PDFSM2VerifyTool sm2Tool = new PDFSM2VerifyTool();
            if (Alg.RSA.name().equalsIgnoreCase(alg.name())) {
                rsaTool.getFields(reader, fields);
            } else if (Alg.SM2.name().equalsIgnoreCase(alg.name())) {
                sm2Tool.getFields(reader, fields);
            } else {
                log.error("算法参数错误,alg = {}", alg);
                throw new BaseRuntimeException(StatusConstants.ALG_ERROR, null, "PDF签章");
            }
            Collections.sort(fields, new SignatureField().new SorterComparator());
            if (!fields.isEmpty()) {
                VerifyMessage message = null;
                int counter = 0;
                for (int i = 0; i < fields.size(); i++) {
                    SignatureField field = fields.get(i);
                    if (field.getType().equals(SignatureField.FIELD_TYPE_SM2)) { // SM2签章
//                        message = sm2Tool.getVerifyMessage(pdfBty, field, sm2RootCerts, isContainCert);
                        message = getVerifyMessageSM2(pdfBty, field);

                    } else if (field.getType().equals(SignatureField.FIELD_TYPE_RSA)) { // RSA标准签章
//                        message = rsaTool.getVerifyMessage(reader, field, rsaRootCerts, crls, isContainCert);
                        message = getVerifyMessageRSA(appId, transId, reader, field);
                    } else {
                        log.error("签名域算法错误,field.getType() = {}", alg);
                        throw new BaseRuntimeException(StatusConstants.ALG_ERROR, null, "PDF签章");
                    }
                    //验证失败直接抛出运行时异常，能运行至此的都是验证成功的
                    // 验证签名域后是否有追加更改
                    int fieldCoverFileSize = field.getByteRange()[2] + field.getByteRange()[3];
                    byte[] appendBody = Arrays.copyOfRange(pdfBty, fieldCoverFileSize, pdfBty.length);
                    countEOF(appendBody);
                    if (counter > (fields.size() - (i + 1))) {
                        throw new BaseRuntimeException(StatusConstants.SIGNATURE_VERIFY_ERROR, null, "PDF签章");
                    }
                    verifyMessages.add(message);
                }
            } else {
                throw new BaseRuntimeException("", "PDF无签名或签名算法输入错误。");
            }
        } catch (Exception e) {
            log.error("signature verification Internal error!", e);
            throw new BaseRuntimeException(StatusConstants.SIGNATURE_VERIFY_ERROR, e.getMessage(), null, "PDF签章");
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return verifyMessages;
    }


    private VerifyMessage getVerifyMessageRSA(String appId, String transId, PdfReader reader, SignatureField field) throws Exception {
        VerifyMessage message = new VerifyMessage();
        message.setFieldName(field.getFieldName());
        message.setSignAlg(field.getSignAlg());
        //esspdf-itext 1.4.8之后的版本支持
        if (field.getPageNo() != null)
            message.setPageNo(field.getPageNo());//所在页码
        AcroFields af = reader.getAcroFields();
        PdfDictionary v = af.getSignatureDictionary(field.getFieldName());
        PdfString extendsStr = v.getAsString(PdfName.EXTENDS);
        if (extendsStr != null) {
            message.setExtendsBty(extendsStr.getOriginalBytes());
        }
        PdfPKCS7 pk = af.verifySignature(field.getFieldName(), "BC");
        Calendar cal = pk.getSignDate();
        message.setSignDate((new Timestamp(cal.getTime().getTime())).toString());
        if (pk.getTimeStampDate() != null) {
            message.setTimeStampDate((new Timestamp(pk.getTimeStampDate().getTime().getTime())).toString());
        }
        Certificate[] pkc = pk.getCertificates();
        String[] certDNs = new String[1];
        StringBuffer sb = new StringBuffer();
        X509Certificate certObj = null;
        try {
            certObj = ItextUtil.readBytesToX509Certificate(field.getSignCert());
        } catch (Exception e) {
            log.error("getVerifyMessageRSA read BytesToX509Certificate error!", e);
            throw new BaseRuntimeException(StatusConstants.CERT_ERROR, null, "PDF验章");
        }
        certDNs[0] = certObj.getSubjectDN().getName();
        sb.append(certObj.getSerialNumber().toString(16).toUpperCase()).append(" ");
        message.setCertSN(sb.toString().trim());
        message.setCommonName(ItextUtil.getFieldFromDN(certDNs[0]).get("CN"));
        message.setCertDNs(certDNs);
        message.setSignCerts(new String(Base64.encode(field.getSignCert())));
        boolean result = pk.verify();
        boolean resultCert = VerifyCert(appId, transId, field.getSignCert());
        //返回前释放资源
        if (null != reader) {
            reader.close();
        }
        if (!result || !resultCert) {
            throw new BaseRuntimeException(StatusConstants.SIGNATURE_VERIFY_ERROR, null, "PDF验章");
        }
        return message;
    }

    /**
     * 证书验证
     *
     * @param cert 证书信息
     * @return 成功失败
     */
    private boolean VerifyCert(String appId, String transId, byte[] cert) {
        VerifyCertRequesrBean verifyCertRequesrBean = VerifyCertRequesrBean.build(appId, Base64Utils.byte2Base64StringFun(cert),
                transId, "TestCloudSign");
        PKIToolTemplate pkiToolTemplate = new PKIToolTemplate(systemRequestService);
        return pkiToolTemplate.verifyCert(verifyCertRequesrBean);
    }

    private VerifyMessage getVerifyMessageSM2(byte[] pdfBty, SignatureField field) throws Exception {
        VerifyMessage message = new VerifyMessage();
        SM2PKITool sm2PKITool = new SM2PKITool();
        CertInfoBean certBean = sm2PKITool.getCertInfo(field.getSignCert());
        message.setExtendsData(field.getExtendsData());
        message.setSignAlg(Alg.SM3WithSM2.name());
        message.setCertDNs(certBean.getCertDN());
        message.setCertSN(certBean.getSerialNumber());
        message.setCommonName(ItextUtil.getFieldFromDN(certBean.getCertDN()).get("CN"));
        message.setSignDate(field.getSignDate());
        byte[] tssSign = sm2PKITool.getTSDataFromP7Sign(field.getP7Data());
        String certStr;
        if (tssSign != null) {
            certStr = sm2PKITool.getSignTimeFromTSSSign(tssSign);
            message.setTimeStampDate(certStr);
        }
        //验证签名及证书
        certStr = this.verifySM2Signature(pdfBty, field);
        //返回验证错误
        if (StringUtils.isBlank(certStr)) {
            throw new BaseRuntimeException(StatusConstants.SIGNATURE_VERIFY_ERROR, null, "PDF验章");
        }
        //返回证书
        byte[] signCertBytes = field.getSignCert();
        if (null != signCertBytes) {
            String[] signCerts1 = new String[]{new String(org.bouncycastle.util.encoders.Base64.encode(signCertBytes))};
            message.setSignCerts(signCerts1);
        }
        return message;
    }

    /**
     * 验证sm2 p7签名
     *
     * @param pdfBty
     * @param field
     * @return 验证通过返回证书， 验证不通过返回null
     * @throws Exception
     */

    public String verifySM2Signature(byte[] pdfBty, SignatureField field) throws Exception {
        byte[] bty1 = ArrayUtil.subArray(pdfBty, field.getByteRange()[0], field.getByteRange()[1]);
        byte[] bty2 = ArrayUtil.subArray(pdfBty, field.getByteRange()[2], field.getByteRange()[2] + field.getByteRange()[3]);
        byte[] plainBty = ArrayUtil.mergeArray(bty1, bty2);
        SM2PKITool tool = new SM2PKITool();
        PKCS7SignedData pkcs7_rsa = new PKCS7SignedData();
        byte[][] certAndsignedValue = pkcs7_rsa.getAttributeSignValueAndCert(field.getP7Data());
        byte[] signedValue = null;
        byte[] contentData = certAndsignedValue[2];
        if (null == contentData) {
            CNPKCS7SignedData cert = new CNPKCS7SignedData(field.getP7Data());
            signedValue = cert.getSignValue();
        } else {
            signedValue = certAndsignedValue[1];
        }
        VerifySignRequestBean verifySignRequestBean = VerifySignRequestBean.build(httpConfig.appId, httpConfig.deviceId,
                SystemConstants.SIGNALGO_HMAC, httpConfig.version, Base64Utils.byte2Base64StringFun("".getBytes()),
                SystemConstants.DATATYPE_HASH, Base64Utils.byte2Base64StringFun(field.getSignCert()), Alg.SM3withSM2.name(),
                SystemConstants.SIGNRESULTTYPE_P1, "TRANS_15267543456789", Base64Utils.byte2Base64StringFun(signedValue),
                "TestCloudSign");
        PKIToolTemplate pkiToolTemplate = new PKIToolTemplate(systemRequestService);
        pkiToolTemplate.setVerifySignRequestBean(verifySignRequestBean);
        String result = tool.verifyP7Sign(plainBty, field.getP7Data(), pkiToolTemplate);
        if (StringUtils.isBlank(result)) {
            byte[] hash = tool.digestWithCert(plainBty, field.getSignCert());
            result = tool.verifyP7Sign(hash, field.getP7Data(), pkiToolTemplate);
            if (StringUtils.isBlank(result)) {
                hash = tool.digest(plainBty);
                result = tool.verifyP7Sign(hash, field.getP7Data(), pkiToolTemplate);
            }
        }
        return result;
    }

    /**
     * <p>计算%%EOF出现的次数，每次使用前必须counter = 0</p>
     *
     * @param pdfBody
     * @Description:
     */
    private void countEOF(byte[] pdfBody) {
        int index = ArrayUtil.indexOf(pdfBody, EOF.getBytes());
        if (ArrayUtil.indexOf(pdfBody, EOF.getBytes()) != -1) {
            counter++;
            countEOF(ArrayUtil.subArray(pdfBody, index + EOF.length()));
        }
    }

}