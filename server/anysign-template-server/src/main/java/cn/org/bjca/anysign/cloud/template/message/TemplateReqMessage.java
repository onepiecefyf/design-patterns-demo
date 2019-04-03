package cn.org.bjca.anysign.cloud.template.message;

import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;
import java.io.Serializable;
import java.util.List;

/***************************************************************************
 * <pre>模板服务请求消息</pre>
 * @文件名称: TemplateReqMessage
 * @包路径: cn.org.bjca.anysign.seal.template.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/12 11:11
 ***************************************************************************/
public class TemplateReqMessage extends BaseRequest implements Serializable {

  /**
   * 交易参考号
   */
  private String transId;

  /**
   * 文件传输类型 必填
   */
  private String fileTransmissionType;

  /**
   * 请求文件token
   */
  private String requestFileToken;

  /**
   * 请求文件ID
   */
  private String requestFileId;

  /**
   * 文件base64编码
   */
  private String requestFileContent;

  /**
   * 文档格式类型 必填
   */
  private String dataType;

  /**
   * 内容字符串 必填
   */
  private String dataString;

  /**
   * 文件归属组
   */
  private String group;

  /**
   * 文件归属用户
   */
  private String user;

  /**
   * 文件有效期
   */
  private String expire;

  /**
   * 模板动态图片列表
   */
  private List<TemplateImgMessage> templateImgList;

  public String getTransId() {
    return transId;
  }

  public void setTransId(String transId) {
    this.transId = transId;
  }

  public String getFileTransmissionType() {
    return fileTransmissionType;
  }

  public void setFileTransmissionType(String fileTransmissionType) {
    this.fileTransmissionType = fileTransmissionType;
  }

  public String getRequestFileToken() {
    return requestFileToken;
  }

  public void setRequestFileToken(String requestFileToken) {
    this.requestFileToken = requestFileToken;
  }

  public String getRequestFileContent() {
    return requestFileContent;
  }

  public void setRequestFileContent(String requestFileContent) {
    this.requestFileContent = requestFileContent;
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public String getDataString() {
    return dataString;
  }

  public void setDataString(String dataString) {
    this.dataString = dataString;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getExpire() {
    return expire;
  }

  public void setExpire(String expire) {
    this.expire = expire;
  }

  public String getRequestFileId() {
    return requestFileId;
  }

  public void setRequestFileId(String requestFileId) {
    this.requestFileId = requestFileId;
  }

  public List<TemplateImgMessage> getTemplateImgList() {
    return templateImgList;
  }

  public void setTemplateImgList(List<TemplateImgMessage> templateImgList) {
    this.templateImgList = templateImgList;
  }
}
