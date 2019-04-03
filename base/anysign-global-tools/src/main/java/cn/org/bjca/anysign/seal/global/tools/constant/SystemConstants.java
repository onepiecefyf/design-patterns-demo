package cn.org.bjca.anysign.seal.global.tools.constant;

/***************************************************************************
 * <pre>系统常类类</pre>
 *
 * @author july_whj
 * @文件名称: SystemConstants.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.constant
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/19 18:24
 ***************************************************************************/
public class SystemConstants {

  /**
   * 用户目录
   */
  public static final String HOMEPATH = System.getProperty("user.home");
  /**
   * 文件编码
   */
  public static final String ENCODING = "UTF-8";

  /**
   * 请求协议版本号
   */
  public static final String VERSION_1 = "1.0";
  /**
   * 请求报文签名算法
   */
  public static final String SIGNALGO_HMAC = "HMAC";
  /**
   * 签名类型 p1
   */
  public static final String SIGNRESULTTYPE_P1 = "P1";
  /**
   * 签名类型 P7
   */
  public static final String SIGNRESULTTYPE_P7 = "P7";

  /**
   * 原文类型 HASH
   */
  public static final String DATATYPE_HASH = "HASH";

  /**
   * 原文类型 PLAIN
   */
  public static final String DATATYPE_PLAIN = "PLAIN";

}