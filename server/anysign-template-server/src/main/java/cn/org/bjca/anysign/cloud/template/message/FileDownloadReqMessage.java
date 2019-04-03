package cn.org.bjca.anysign.cloud.template.message;

import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;
import java.io.Serializable;

/***************************************************************************
 * <pre>文件下载请求消息</pre>
 * @文件名称: FileUploadReqMessage
 * @包路径: cn.org.bjca.anysign.seal.template.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 11:07
 ***************************************************************************/
public class FileDownloadReqMessage extends BaseRequest implements Serializable {

  /**
   * 文件ID
   */
  private String id;

  /**
   * 文件授权访问token
   */
  private String token;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
