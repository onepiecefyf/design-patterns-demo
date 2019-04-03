package cn.org.bjca.anysign.seal.service.bean.base;

import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HMacUtils;
import cn.org.bjca.anysign.seal.global.tools.utils.SortedMapUtils;
import java.io.Serializable;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;

/***************************************************************************
 * <pre>公共请求参数</pre>
 *
 * @author july_whj
 * @文件名称: CommonRequestParam.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.message
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/19 18:38
 ***************************************************************************/
@Data
@Accessors(chain = true)
public class CommonRequestParam implements Serializable {


  protected String version = "1.0";

  protected String signAlgo;

  protected String signature;

  protected String deviceId;

  protected String appId;
  /**
   * 业务流水
   */
  protected String transId;


  /**
   * 参数签名
   *
   * @param param 参数值
   * @param key 签名密钥
   * @return 签名值
   */
  protected String generateSignature(Map<String, String> param, String key) {
    SortedMapUtils sortedMapUtils = new SortedMapUtils(param);
    String signValue = sortedMapUtils.toString();
    byte[] bytes = HMacUtils.sha256_HMAC(signValue, key);
    return Base64Utils.byte2Base64StringFun(bytes);
  }


}