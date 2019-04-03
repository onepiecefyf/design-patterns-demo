package cn.org.bjca.anysign.seal.service.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjgao
 * @create 2018/10/13.
 * @description
 */
@Getter
@Setter
public class VerifyTsRespRequestBean {
    /**
     * 业务ID，传递15-64位的随机数（建议业务系统进行日志记录，方便根据id排查问题）
     */
    private String transId;

    private String appId;
    /**
     * 时间戳响应的Base64编码字符串
     */
    private String tsResp;
    /**
     * 原文字符串，使用UTF-8编码，原文字节长度限制为1024*1024*5=5M
     */
    private String oriData;

    /**
     *  base64编码格式的时间戳签名证书，非BJCA签发的不带证书时间戳验证需要传入证书。
     */
    private String base64Cert;

    /**
     * 是否验证证书，为true时验证证书有效性，如果时间戳不包含证书，需要传入证书；为false时，不验证证书
     */
    private String verifyCert;

    public Map<String, String> toMap() {
        Map<String, String> param = new HashMap<String, String>(16);
        param.put("transId", transId);
        param.put("appId", appId);
        param.put("tsResp", tsResp);
        param.put("oriData",oriData );
        param.put("base64Cert",base64Cert );
        param.put("verifyCert",verifyCert );
        return param;
    }
    public String toJSONString(){
        Map<String, String> param = toMap();
        return JSON.toJSONString(param);
    }

    public JSONObject toJsonObject() {
        Map<String, String> param = toMap();
        JSONObject jsonObject = JSONObject.parseObject(JSON.toJSONString(param));
        return jsonObject;
    }

    public static VerifyTsRespRequestBean build(String transId,String appId,String tsResp,String oriData,String base64Cert ,String verifyCert){
        VerifyTsRespRequestBean verifyTsRespRequestBean = new VerifyTsRespRequestBean();
        verifyTsRespRequestBean.setTransId(transId);
        verifyTsRespRequestBean.setAppId(appId);
        verifyTsRespRequestBean.setTsResp(tsResp);
        verifyTsRespRequestBean.setOriData(oriData);
        verifyTsRespRequestBean.setBase64Cert(base64Cert);
        verifyTsRespRequestBean.setVerifyCert(verifyCert);
        return verifyTsRespRequestBean;
    }

}
