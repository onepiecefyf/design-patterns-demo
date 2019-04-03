package cn.org.bjca.anysign.seal.service.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @description
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Getter
@Setter
public class GenTsRespRequestBean  {
    /**
     * 业务ID，传递15-64位的随机数（建议业务系统进行日志记录，方便根据id排查问题）
     */
    private String transId;
    private String appId;
    /**
     * 签名算法标识，主要由SHA1withRSA、SHA256withRSA、SM3withSM2
     */
    private String signAlgIdentifier;
    /**
     * Base64编码格式的时间戳请求
     */
    private String tsReq;


    public Map<String, String> toMap() {
        Map<String, String> param = new HashMap<String, String>(16);
        param.put("transId", transId);
        param.put("appId", appId);
        param.put("signAlgIdentifier", signAlgIdentifier);
        param.put("tsReq",tsReq );
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

    public static GenTsRespRequestBean build(String transId,String appId,String signAlgIdentifier,String tsReq){
        GenTsRespRequestBean genTsRespRequestBean = new GenTsRespRequestBean();
        genTsRespRequestBean.setTransId(transId);
        genTsRespRequestBean.setAppId(appId);
        genTsRespRequestBean.setSignAlgIdentifier(signAlgIdentifier);
        genTsRespRequestBean.setTsReq(tsReq);
        return genTsRespRequestBean;
    }


}
