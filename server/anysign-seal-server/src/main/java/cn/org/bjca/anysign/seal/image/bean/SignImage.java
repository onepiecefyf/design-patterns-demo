package cn.org.bjca.anysign.seal.image.bean;

import cn.org.bjca.anysign.seal.moulage.bean.Template;
import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import java.io.Serializable;

/***************************************************************************
 * <pre>印模bean</pre>
 *
 * @author july_whj
 * @文件名称: SignImage.class
 * @包 路   径：  cn.org.bjca.anysign.image.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/7 14:03
 ***************************************************************************/
public class SignImage extends BaseRequest implements Serializable {

  /**
   * 交易参考号
   */
  private String transId;
  /**
   * fileTransmissionType(*)
   */
  private FileTransmissionType fileTransmissionType;
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
  private Integer expire;
  /**
   * 印模实体
   */
  private Moulage moulage;
  /**
   * 模板对象
   */
  private Template template;

  public Template getTemplate() {
    return template;
  }

  public void setTemplate(Template template) {
    this.template = template;
  }

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

  public Integer getExpire() {
    return expire;
  }

  public void setExpire(Integer expire) {
    this.expire = expire;
  }

  public Moulage getMoulage() {
    return moulage;
  }

  public void setMoulage(Moulage moulage) {
    this.moulage = moulage;
  }
}