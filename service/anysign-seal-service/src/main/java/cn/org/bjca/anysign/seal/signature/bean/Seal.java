package cn.org.bjca.anysign.seal.signature.bean;

import cn.org.bjca.anysign.seal.service.bean.enumpackage.ProtectedMode;
import java.io.Serializable;
import lombok.Data;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description
 */
@Data
public class Seal implements Serializable {

  /**
   * 密钥id
   */
  private String keyId;

  /**
   * 验证信息
   */
  private String verifyCode;

  /**
   * 验证证书策略id
   */
  private String verifyCertPolicyId;


  /**
   * 证书 base64编码
   */
  private String signCert;

  /**
   * 文档保护模式
   */
  ProtectedMode protectedMode = ProtectedMode.NOLIMITED;
  /**
   * 是否打印印章
   */
  private boolean isPrintable;

  /**
   * 是否时间戳签名
   */
  private boolean isTss;

  /**
   * 签名原因
   */
  private String reason;

  /**
   * 签名地点
   */
  private String location;

  /**
   * 联系方式
   */
  private String contact;

  /**
   * 签章图片base64编码
   */
  private String sealPicContent;

  /**
   * 签章图片文件下载token
   */
  private String sealPicToken;

  /**
   * 签章图片文件下载id
   */
  private String sealPicId;

  /**
   * 签章图片宽度，传0.0f时，以图片实际宽度为准
   */
  private float sealWidth;

  /**
   * 签章图片高度，传0.0f时，以图片实际高度为
   */
  private float sealHeight;

  /**
   * 签章定位规则，参考SealPosition对象
   */
  private SealPosition sealPosition;

  /**
   * 水印模板(xml)字符串
   */
  private String waterMarkTemplate;

  /**
   * 水印文本内容
   */
  private String waterMarkContent;

}
