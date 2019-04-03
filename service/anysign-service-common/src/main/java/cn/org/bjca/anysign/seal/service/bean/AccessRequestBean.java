package cn.org.bjca.anysign.seal.service.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/***************************************************************************
 * <pre>文件授权请求对象</pre>
 *
 * @author july_whj
 * @文件名称: AccessRequestBean.cls
 * @包 路   径：  cn.org.bjca.anysign.server.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/25 9:53
 ***************************************************************************/
@Data
public class AccessRequestBean extends CommonRequestParam {

  private String id;
  private int expire = 36000;

  public static AccessRequestBean build(String appId, String deviceId, String version,
      String signAlgo, String signature,
      String id, String transId) {
    AccessRequestBean accessRequestBean = new AccessRequestBean();
    accessRequestBean.setAppId(appId);
    accessRequestBean.setDeviceId(deviceId);
    accessRequestBean.setVersion(version);
    accessRequestBean.setSignAlgo(signAlgo);
    accessRequestBean.setSignature(signature);
    accessRequestBean.setId(id);
    accessRequestBean.setTransId(transId);
    return accessRequestBean;
  }

  public Map<String, String> toMap(String signatureKey) {
    Map<String, String> param = new HashMap<String, String>(16);
    param.put("id", id);
    param.put("version", version);
    param.put("signAlgo", signAlgo);
    param.put("deviceId", deviceId);
    param.put("appId", appId);
    param.put("expire", "14400");
    if (StringUtils.isNotEmpty(signature)) {
      param.put("signature", generateSignature(param, signature));
    } else {
      param.put("signature", generateSignature(param, signatureKey));
    }
    return param;
  }

  public JSONObject toJsonObject(String signatureKey) {
    Map<String, String> param = toMap(signatureKey);
    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(param));
    return jsonObject;
  }

  public JSONObject toJsonObject() {
    Map<String, String> param = toMap(null);
    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(param));
    return jsonObject;
  }

}