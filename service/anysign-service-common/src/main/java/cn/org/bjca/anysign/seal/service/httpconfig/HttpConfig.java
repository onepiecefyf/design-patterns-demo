package cn.org.bjca.anysign.seal.service.httpconfig;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/***************************************************************************
 * <pre>http服务配置类</pre>
 *
 * @author july_whj
 * @文件名称: HttpConfig.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.constant
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/19 19:57
 ***************************************************************************/
@Component
@Data
@PropertySource(value = "classpath:httpconfig/httpconfig.properties")
public class HttpConfig {

  /**
   * 本服务appId
   */
  @Value("${appId}")
  public String appId;
  /**
   * 设备Id
   */
  @Value("${device-id}")
  public String deviceId;
  /**
   * secret
   */
  @Value("${secret}")
  public String secret;
  /**
   * 版本号
   */
  @Value("${version}")
  public String version;

  /**
   * 文件服务
   */
  @Value("${file.address}")
  public String fileAddress;
  /**
   * 文件上传
   */
  @Value("${file.upload}")
  public String fileUpload;
  /**
   * 文件下载
   */
  @Value("${file.download}")
  public String fileDownload;
  /**
   * 签名服务
   */
  @Value("${signature.address}")
  public String signatureAddress;

  /**
   * 申请个人托管证书
   */
  @Value("${signature.user-apply-cert}")
  public String signatureUserApplyCert;

  /**
   * 托管证书签名
   */
  @Value("${signature.deposit-cert-sign}")
  public String signatureDepositCertSign;
  /**
   * 验签
   */
  @Value("${signature.verify-sign}")
  public String signatureVerifySign;

  /**
   * TSS时间戳服务
   */
  @Value("${tss.address}")
  public String tssAddress;
  /**
   * 产生时间戳请求
   */
  @Value("${tss.gen-ts-req}")
  public String tssGenTsReq;
  /**
   * 时间戳响应
   */
  @Value("${tss.gen-ts-resp}")
  public String tssGenTsResp;
  /**
   * DSVS服务
   */
  @Value("${dsvs.address}")
  public String dsvsAddress;
  /**
   * 证书验证
   */
  @Value("${dsvs.cert-validate}")
  public String dsvsCertValidate;

}

