package cn.org.bjca.anysign.seal.service;


import cn.org.bjca.anysign.seal.service.bean.enumpackage.Alg;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.ProtectedMode;
import cn.org.bjca.anysign.seal.signature.bean.PdfDigest;
import cn.org.bjca.anysign.seal.signature.bean.Seal;
import cn.org.bjca.seal.esspdf.itext.bean.VerifyMessage;

import java.util.List;

/**
 * @author zjgao
 * @create 2018/9/12.
 * @description 签章接口
 */
public interface ISealService {
    /**
     * PDF签章接口
     *
     * @param pdfBty               pdf文件
     * @param appId                appId
     * @param deviceId             设备ID
     * @param signature            签名值
     * @param transId              业务ID
     * @param fileTransmissionType 文件类型
     * @param seals                签名对象集合
     * @return 签名后文件
     */
    byte[] pdfSignature(byte[] pdfBty, String appId, String deviceId, String signature, String transId,
                        FileTransmissionType fileTransmissionType, List<Seal> seals);

    /**
     * PDF计算摘要
     *
     * @param appId                appID
     * @param transId              业务ID
     * @param pdfBty               pdf文件
     * @param fileTransmissionType 文件传输类型
     * @param protectedMode        文件保护模式
     * @param seal                 签名对象
     * @return 摘要对象
     */
    PdfDigest pdfDigest(String appId, String transId, byte[] pdfBty,
                        FileTransmissionType fileTransmissionType, ProtectedMode protectedMode,
                        Seal seal);

    /**
     * PDF合章
     *
     * @param signBty 签名结果
     * @param pdfBty  pdf文件
     * @return
     */
    byte[] sealMerging(byte[] signBty, byte[] pdfBty);

    /**
     * PDF验证签名
     *
     * @param appId       appID
     * @param transId     业务ID
     * @param fileContent pdf文档内容
     * @param alg         签名算法
     * @return 签名信息集合
     */
    List<VerifyMessage> sealVerify(String appId, String transId, byte[] fileContent, Alg alg);

}
