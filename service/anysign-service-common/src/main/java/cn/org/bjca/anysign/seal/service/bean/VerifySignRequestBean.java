package cn.org.bjca.anysign.seal.service.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Getter
@Setter
public class VerifySignRequestBean extends CommonRequestParam {

    @Autowired
    private HttpConfig httpConfig;

    /**
     * 原文（UTF-8编码的String,最大5M）或hash值的base64
     */
    private String data;
    /**
     * 参考数据类型-DataType  原文/哈希值
     */
    private String dataType;
    /**
     * 签名值base64
     */
    private String signValue;
    /**
     * 参考数据类型-SignResultType P1/P7
     */
    private String signResultType;
    /**
     * 参考签名算法-SignAlgIdentifier
     */
    private String signAlgIdentifier;
    /**
     * 签名证书 签名值类型为P1时需要
     */
    private String base64Cert;
    /**
     * 验证证书的策略Id，支持不同应用使用不同的证书验证策略；当verifyCert为true时，此参数不能为空
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
        param.put("base64Cert", base64Cert);
        param.put("signAlgIdentifier", signAlgIdentifier);
        param.put("signResultType", signResultType);
        param.put("transId", transId);
        param.put("signValue", signValue);
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
        return JSONObject.parseObject(JSON.toJSONString(param));
    }


    public static VerifySignRequestBean build(String appId, String deviceId, String signAlgo, String version, String data, String dataType, String base64Cert, String signAlgIdentifier, String signResultType, String transId, String signValue, String verifyCertPolicyId) {
        VerifySignRequestBean verifySignRequestBean = new VerifySignRequestBean();
        verifySignRequestBean.setAppId(appId);
        verifySignRequestBean.setDeviceId(deviceId);
        verifySignRequestBean.setSignAlgo(signAlgo);
        verifySignRequestBean.setVersion(version);
        verifySignRequestBean.setData(data);
        verifySignRequestBean.setDataType(dataType);
        verifySignRequestBean.setBase64Cert(base64Cert);
        verifySignRequestBean.setSignAlgIdentifier(signAlgIdentifier);
        verifySignRequestBean.setSignResultType(signResultType);
        verifySignRequestBean.setTransId(transId);
        verifySignRequestBean.setSignValue(signValue);
        verifySignRequestBean.setVerifyCertPolicyId(verifyCertPolicyId);
        return verifySignRequestBean;
    }
}
