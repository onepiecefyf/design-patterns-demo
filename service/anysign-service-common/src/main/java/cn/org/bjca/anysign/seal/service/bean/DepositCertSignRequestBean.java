package cn.org.bjca.anysign.seal.service.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

/**
 * @author zjgao
 * @description 企业/个人托管证书签名
 * @create 2018/9/26.
 * @description
 */
@Data
public class DepositCertSignRequestBean extends CommonRequestParam implements Serializable {

  /**
   * 密钥对Id
   */
  private String keyId;
  /**
   * 原文（UTF-8编码的String,最大5M）或hash值的base64
   */
  private String data;
  /**
   * 参考数据类型-DataType  PLAIN/HASH
   */
  private String dataType;
  /**
   * 参考签名算法-SignAlgIdentifier SHA1WithRSA  SHA256WithRSA  SM3WithSM2
   */
  private String signAlgIdentifier;

  /**
   * 验证信息 申请证书时设置的值
   */
  private String verifyCode;

  /**
   * 参考签名值类型SignResultType P1/P7
   */
  private String signResultType;

  /**
   * P7带不带原文 false,true
   */
  private String attach;

  /**
   * 验证证书的策略Id，支持不同应用使用不同的证书验证策略；当verifyCert为true时，此参数不能为空；
   */
  private String verifyCertPolicyId;


  public Map<String, String> toMap(String signatureKey) {
    signAlgo = "HMAC";
    Map<String, String> param = new HashMap<String, String>(16);
    param.put("appId", appId);
    param.put("deviceId", deviceId);
    param.put("signAlgo", signAlgo);
    param.put("version", version);
    param.put("data", data);
    param.put("dataType", dataType);
    param.put("keyId", keyId);
    param.put("signAlgIdentifier", signAlgIdentifier);
    param.put("signResultType", signResultType);
    param.put("transId", transId);
    param.put("verifyCode", verifyCode);
    param.put("verifyCertPolicyId", verifyCertPolicyId);
    if (null != signatureKey && !"".equals(signatureKey)) {
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

  public static DepositCertSignRequestBean build(String appId, String deviceId, String signAlgo,
      String version, String data,
      String dataType, String keyId, String signAlgIdentifier, String signResultType,
      String transId, String verifyCode, String verifyCertPolicyId) {
    DepositCertSignRequestBean depositCertSignRequestBean = new DepositCertSignRequestBean();
    depositCertSignRequestBean.setAppId(appId);
    depositCertSignRequestBean.setDeviceId(deviceId);
    depositCertSignRequestBean.setSignAlgo(signAlgo);
    depositCertSignRequestBean.setVersion(version);
    depositCertSignRequestBean.setData(data);
    depositCertSignRequestBean.setDataType(dataType);
    depositCertSignRequestBean.setKeyId(keyId);
    depositCertSignRequestBean.setSignAlgIdentifier(signAlgIdentifier);
    depositCertSignRequestBean.setSignResultType(signResultType);
    depositCertSignRequestBean.setTransId(transId);
    depositCertSignRequestBean.setVerifyCode(verifyCode);
    depositCertSignRequestBean.setVerifyCertPolicyId(verifyCertPolicyId);
    return depositCertSignRequestBean;
  }
}
