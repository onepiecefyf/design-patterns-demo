package cn.org.bjca.anysign.seal.service.bean;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Data
@Slf4j
public class DownloadRequestBean extends CommonRequestParam implements Serializable {

  private String id;
  private String token;

  public static DownloadRequestBean build(String appId, String deviceId, String version,
      String signature,
      String id, String token, String transId) {
    Assert.notNull(token, StatusConstants.FILE_TOKEN_NOTNUll);
    Assert.notNull(id, StatusConstants.FILE_ID_NOTNUll);
    DownloadRequestBean downloadRequestBean = new DownloadRequestBean();
    downloadRequestBean.setAppId(appId);
    downloadRequestBean.setDeviceId(deviceId);
    downloadRequestBean.setVersion(version);
    downloadRequestBean.setSignAlgo("HmacSHA256");
    downloadRequestBean.setSignature(signature);
    downloadRequestBean.setId(id);
    downloadRequestBean.setToken(token);
    downloadRequestBean.setTransId(transId);
    return downloadRequestBean;
  }

  public Map<String, String> toMap(String signatureKey) {
    Map<String, String> param = new HashMap<String, String>(16);
    param.put("id", id);
    param.put("token", token);
    param.put("version", version);
    param.put("signAlgo", signAlgo);
    param.put("deviceId", deviceId);
    param.put("appId", appId);
    if (StringUtils.isNotEmpty(signature)) {
      param.put("signature", generateSignature(param, signature));
    } else {
      param.put("signature", generateSignature(param, signatureKey));
    }
    return param;
  }

  public String toJSONString(String signatureKey) {
    Map<String, String> param = toMap(signatureKey);
    return JSON.toJSONString(param);
  }


  public JSONObject toJsonObject(String signatureKey) {
    Map<String, String> param = toMap(signatureKey);
    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(param));
    return jsonObject;
  }
}
