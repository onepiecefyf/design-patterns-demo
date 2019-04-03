package cn.org.bjca.anysign.seal.verify.bean;


import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.Alg;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import java.io.Serializable;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description
 */

public class SignatureVerifyRequest extends BaseRequest implements Serializable {

  /**
   * 交易参考号
   */
  private String transId;
  /**
   * 文档传输类型
   */
  private FileTransmissionType fileTransmissionType;

  /**
   * 请求文件下载token
   */
  private String requestFileToken;

  /**
   * 请求文件id
   */
  private String requestFileId;

  /**
   * 文件base64编码
   */
  private String requestFileContent;

  /**
   * 签名密钥算法
   */
  private Alg alg;

  public String getTransId() {
    return transId;
  }

  public void setTransId(String transId) {
    this.transId = transId;
  }

  public FileTransmissionType getFileTransmissionType() {
    return fileTransmissionType;
  }

  public void setFileTransmissionType(FileTransmissionType fileTransmissionType) {
    this.fileTransmissionType = fileTransmissionType;
  }

  public String getRequestFileToken() {
    return requestFileToken;
  }

  public void setRequestFileToken(String requestFileToken) {
    this.requestFileToken = requestFileToken;
  }

  public String getRequestFileId() {
    return requestFileId;
  }

  public void setRequestFileId(String requestFileId) {
    this.requestFileId = requestFileId;
  }

  public String getRequestFileContent() {
    return requestFileContent;
  }

  public void setRequestFileContent(String requestFileContent) {
    this.requestFileContent = requestFileContent;
  }

  public Alg getAlg() {
    return alg;
  }

  public void setAlg(Alg alg) {
    this.alg = alg;
  }
}
