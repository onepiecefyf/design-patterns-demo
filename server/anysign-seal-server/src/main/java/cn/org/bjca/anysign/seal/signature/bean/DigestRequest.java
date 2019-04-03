package cn.org.bjca.anysign.seal.signature.bean;

import cn.org.bjca.anysign.seal.server.common.message.BaseRequest;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import lombok.Data;

/***************************************************************************
 * <pre>pdf摘要请求参数</pre>
 *
 * @文件名称: DigestRequest.cls
 * @包 路   径：  cn.org.bjca.anysign.seal.signature.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/18 15:47
 ***************************************************************************/
@Data
public class DigestRequest extends BaseRequest {

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
  private int expire;

  /**
   * 签章规则实体-seal
   */
  private Seal seal;
}