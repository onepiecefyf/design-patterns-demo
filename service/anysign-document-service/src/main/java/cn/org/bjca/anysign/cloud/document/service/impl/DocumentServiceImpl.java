package cn.org.bjca.anysign.cloud.document.service.impl;

import cn.org.bjca.anysign.cloud.document.bean.DocumentFormatBean;
import cn.org.bjca.anysign.cloud.document.service.IDocumentFormatService;
import cn.org.bjca.anysign.cloud.document.utils.ImageHandleUtil;
import cn.org.bjca.seal.esspdf.convert.PDFConvertTool;
import com.itextpdf.text.pdf.PdfReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.zefer.pd4ml.PD4Constants;
import org.zefer.pd4ml.PD4ML;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.util.stream.Collectors.toList;

/***************************************************************************
 * <pre>文档服务</pre>
 * @文件名称: DocumentServiceImpl
 * @包路径: cn.org.bjca.anysign.cloud.document.service.impl
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/12 14:40
 ***************************************************************************/
@Service
@Slf4j
public class DocumentServiceImpl implements IDocumentFormatService {

    /**
     * <p>PDF格式化为图片</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    @Override
    public byte[] pdf2Img(DocumentFormatBean documentFormatBean) {
        byte[] pdfBty = documentFormatBean.getSourceFileBty();
        int start = documentFormatBean.getStartIndex();
        int end = documentFormatBean.getEndIndex();
        boolean judgeScope = documentFormatBean.isJudgeScope();
        String imgType = documentFormatBean.getImgType();
        int imgMergeType = documentFormatBean.getImgMergeType();
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            List<byte[]> imgList = convert2ImgList(pdfBty, start, end, judgeScope);
            BufferedImage bImage = ImageHandleUtil.mergeImage(imgList, imgMergeType);
            byteArrayOutputStream = new ByteArrayOutputStream();
            ImageHandleUtil.writeBufferedImage2File(bImage, imgType, byteArrayOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF convert 2 image error!");
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * <p>PDF格式化为图片列表</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    @Override
    public List<byte[]> pdf2ImgList(DocumentFormatBean documentFormatBean) {
        byte[] pdfBty = documentFormatBean.getSourceFileBty();
        int start = documentFormatBean.getStartIndex();
        int end = documentFormatBean.getEndIndex();
        boolean judgeScope = documentFormatBean.isJudgeScope();
        try {
            return convert2ImgList(pdfBty, start, end, judgeScope);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("PDF convert 2 image list error!");
        }
    }

    /**
     * <p>Html 文档转换Pdf</p>
     *
     * @param documentFormatBean
     * @return
     */
    @Override
    public byte[] html2Pdf(DocumentFormatBean documentFormatBean) {
        byte[] htmlBty = documentFormatBean.getSourceFileBty();
        ByteArrayOutputStream bOutStream = new ByteArrayOutputStream();
        ByteArrayInputStream bInputStream = new ByteArrayInputStream(htmlBty);
        InputStreamReader reader = new InputStreamReader(bInputStream, StandardCharsets.UTF_8);
        PD4ML pd4ml = new PD4ML();
        try {
            pd4ml.useTTF("java:fonts", true);
            pd4ml.setDefaultTTFs("SimSun", "SimSun", "SimSun");
            pd4ml.setPageInsets(new Insets(15, 20, 15, 20));
            pd4ml.setHtmlWidth(documentFormatBean.getHtmlWidth());
            pd4ml.setPageSize(PD4Constants.A4);
            pd4ml.render(reader, bOutStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("HTML convert PDF error!");
        }
        return bOutStream.toByteArray();
    }

    /**
     * <p>Word文档转换Pdf</p>
     *
     * @param documentFormatBean Word文档
     * @return
     */
    @Override
    public byte[] word2Pdf(DocumentFormatBean documentFormatBean) {
        PDFConvertTool pdfConvertTool = new PDFConvertTool();
        byte[] wordBty = documentFormatBean.getSourceFileBty();
        return pdfConvertTool.docConvertToPDF(wordBty);
    }

    /**
     * <p>Image文档转换Pdf</p>
     *
     * @param documentFormatBean 格式化对象Bean
     * @return
     */
    @Override
    public byte[] img2Pdf(DocumentFormatBean documentFormatBean) {
        PDFConvertTool pdfConvertTool = new PDFConvertTool();
        pdfConvertTool.addImgToPDF(documentFormatBean.getSourceFileBty());
        return pdfConvertTool.mergePDF();
    }

    /**
     * <p>合并PDF</p>
     *
     * @param pdfBtyList PDF文档Bty列表
     * @return
     */
    @Override
    public byte[] mergePdf(List<byte[]> pdfBtyList) {
        PDFConvertTool tool = new PDFConvertTool();
        tool.addPDF(pdfBtyList);
        return tool.mergePDF();
    }

    /**
     * <p>获取可用的图片信息</p>
     *
     * @param imgList      原始图片列表
     * @param startPageNum 开始页码
     * @param endPageNum   结束页码
     * @return 有效图片列表
     */
    private List<byte[]> getValidImgs(List<byte[]> imgList, final int startPageNum, final int endPageNum) {
        return imgList.stream()
                .filter(imgBty ->
                        imgList.indexOf(imgBty) >= startPageNum && imgList.indexOf(imgBty) <= endPageNum)
                .collect(toList());
    }

    /**
     * <p>计算转换范围</p>
     *
     * @param startPageNum 开始页码
     * @param endPageNum   结束页码
     * @param totalPageNum 总页码
     * @return 范围
     */
    private int[] calcScope(int startPageNum, int endPageNum, int totalPageNum) {
        int[] scope = new int[2];
        if (startPageNum < 0 || startPageNum > totalPageNum) {
            startPageNum = 0;
        }
        if (endPageNum < startPageNum) {
            endPageNum = startPageNum;
        } else if (endPageNum > totalPageNum) {
            endPageNum = totalPageNum - 1;
        }
        scope[0] = startPageNum;
        scope[1] = endPageNum;
        return scope;
    }

    /**
     * <p>转换图片list</p>
     *
     * @param pdfBty     PDF文档
     * @param start      起始位置
     * @param end        结束位置
     * @param judgeScope 是否判断范围
     * @return
     * @throws IOException
     */
    private List<byte[]> convert2ImgList(byte[] pdfBty, int start, int end, boolean judgeScope) throws Exception {
        PDFConvertTool pdfConvertTool = new PDFConvertTool();
        @SuppressWarnings("unchecked")
        List<byte[]> imgList = pdfConvertTool.pdfConvertToImage(pdfBty, "");
        if (judgeScope) { // 截取指定范围内的图片
            PdfReader reader = new PdfReader(pdfBty);
            int totalPageNum = reader.getNumberOfPages();
            int[] scope = calcScope(start, end, totalPageNum);
            start = scope[0];
            end = scope[1];
            imgList = getValidImgs(imgList, start, end);
        }
        return imgList;
    }
}
