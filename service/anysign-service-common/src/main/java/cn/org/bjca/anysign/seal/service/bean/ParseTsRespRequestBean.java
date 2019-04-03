package cn.org.bjca.anysign.seal.service.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author zjgao
 * @create 2018/10/13.
 * @description
 */
@Data
public class ParseTsRespRequestBean implements Serializable {

  /**
   * 业务ID，传递15-64位的随机数（建议业务系统进行日志记录，方便根据id排查问题）
   */
  private String transId;
  /**
   * 接口调用方（业务系统）的ID标识
   */
  private String appId;
  /**
   * 时间戳响应的Base64编码字符串
   */
  private String tsResp;

  public Map<String, String> toMap() {
    Map<String, String> param = new HashMap<String, String>(16);
    param.put("transId", transId);
    param.put("appId", appId);
    param.put("tsResp", tsResp);
    return param;
  }

  public String toJSONString() {
    Map<String, String> param = toMap();
    return JSON.toJSONString(param);
  }

  public JSONObject toJsonObject() {
    Map<String, String> param = toMap();
    JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(param));
    return jsonObject;
  }

  public static ParseTsRespRequestBean build(String transId, String appId, String tsResp) {
    ParseTsRespRequestBean parseTsRespRequestBean = new ParseTsRespRequestBean();
    parseTsRespRequestBean.setTransId(transId);
    parseTsRespRequestBean.setAppId(appId);
    parseTsRespRequestBean.setTsResp(tsResp);
    return parseTsRespRequestBean;
  }

}
