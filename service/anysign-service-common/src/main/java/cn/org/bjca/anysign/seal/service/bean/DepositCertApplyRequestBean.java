package cn.org.bjca.anysign.seal.service.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

/**
 * @author july_whj
 */
@Data
@Accessors(chain = true)
public class DepositCertApplyRequestBean extends CommonRequestParam {
    /**
     * 证书算法类型-RSA/SM2
     */
    private String keyType;
    /**
     * 密钥长度
     */
    private String keyLength;
    /**
     * 证书扩展性
     */
    private String certExtensions;
    /**
     * 证书有效开始时间
     */
    private String notBefore;
    /**
     * 证书有效结束时间
     */
    private String notAfter;
    /**
     * 姓名
     */
    private String name;
    /**
     * 个人证件类型
     */
    private String idType;
    /**
     * 证件编号
     */
    private String idCode;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 验证信息
     */
    private String verifyCode;
    /**
     * CA策略ID
     */
    private String templatePolicyID;
    /**
     * 业务ID
     */
    private String transId;

    public Map<String, String> toMap(String signatureKey) {
        signAlgo = "HMAC";
        Map<String, String> param = new HashMap<String, String>(16);
        param.put("appId", appId);
        param.put("deviceId", deviceId);
        param.put("signAlgo", signAlgo);
        param.put("version", version);
        param.put("keyType", keyType);
        param.put("keyLength", keyLength);
        param.put("certExtensions", certExtensions);
        param.put("notBefore", notBefore);
        param.put("notAfter", notAfter);
        param.put("name", name);
        param.put("idType", idType);
        param.put("idCode", idCode);
        param.put("mobile", mobile);
        param.put("verifyCode", verifyCode);
        param.put("templatePolicyID", templatePolicyID);
        param.put("transId", transId);
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

    /**
     * DepositCertApplyRequestBean 对象构建
     *
     * @param appId            appId
     * @param deviceId         deviceId
     * @param signAlgo         签名算法
     * @param version          版本
     * @param keyType          证书算法类型-RSA/SM2
     * @param keyLength        证书密钥长度
     * @param certExtensions   证书扩展项
     * @param notBefore        证书生效时间
     * @param notAfter         证书过期时间
     * @param name             签名人
     * @param idType           签名人类型
     * @param idCode           签名人证件编号
     * @param mobile           电话号
     * @param verifyCode       验证信息
     * @param templatePolicyID CA策略ID
     * @param transId          业务ID
     * @return DepositCertApplyRequestBeand对象
     */
    public static DepositCertApplyRequestBean build(String appId, String deviceId, String signAlgo, String version, String keyType,
                                                    String keyLength, String certExtensions, String notBefore, String notAfter,
                                                    String name, String idType, String idCode, String mobile, String verifyCode,
                                                    String templatePolicyID, String transId) {
        DepositCertApplyRequestBean depositCertApplyRequestBean = new DepositCertApplyRequestBean();
        depositCertApplyRequestBean.setAppId(appId).setDeviceId(deviceId).setSignAlgo(signAlgo).setTransId(transId).setVersion(version);
        depositCertApplyRequestBean.setKeyType(keyType).setKeyLength(keyLength).setCertExtensions(certExtensions).setNotBefore(notBefore)
                .setNotAfter(notAfter).setName(name).setIdType(idType).setIdCode(idCode).setMobile(mobile).setVerifyCode(verifyCode).setTemplatePolicyID(templatePolicyID);
        return depositCertApplyRequestBean;
    }

}
