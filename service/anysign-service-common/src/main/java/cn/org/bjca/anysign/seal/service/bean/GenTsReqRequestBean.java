package cn.org.bjca.anysign.seal.service.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @description 时间戳请求对象
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Data
public class GenTsReqRequestBean implements Serializable {

    private String transId;

    private String appId;
    /**
     * 摘要算法,SM3,SHA256，SHA1
     */
    private String digestAlg;
    /**
     *  原文字符串，必须使用UTF-8编码，原文字节长度限制为1024*1024*5=5M
     */
    private String oriData;
    /**
     *  签发出来的时间戳是否带证书，为true时表示带证书，为false表示不带证书
     */
    private String attachCert;

    public Map<String, String> toMap()  {
        Map<String, String> param = new HashMap<String, String>(16);
        param.put("transId", transId);
        param.put("appId", appId);
        param.put("digestAlg", digestAlg);
        param.put("oriData", oriData);
        param.put("attachCert", attachCert);
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

    public static GenTsReqRequestBean build(String transId,String appId,String digestAlg,String oriData,String attachCert){
        GenTsReqRequestBean genTsReqRequestBean = new GenTsReqRequestBean();
        genTsReqRequestBean.setTransId(transId);
        genTsReqRequestBean.setAppId( appId);
        genTsReqRequestBean.setDigestAlg(digestAlg);
        genTsReqRequestBean.setOriData(oriData);
        genTsReqRequestBean.setAttachCert(attachCert);
        return genTsReqRequestBean;
    }

}
