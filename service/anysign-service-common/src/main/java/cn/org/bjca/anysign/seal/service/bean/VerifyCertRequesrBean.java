package cn.org.bjca.anysign.seal.service.bean;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/***************************************************************************
 * <pre>证书验证请求Bean</pre>
 *
 * @文件名称: VerifyCertRequesrBean.class
 * @包 路   径：  cn.org.bjca.anysign.server.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/10/18 10:26
 ***************************************************************************/
@Setter
@Getter
public class VerifyCertRequesrBean {
    /**
     * 业务ID
     */
    @Setter
    @Getter
    @NonNull
    private String transId;
    /**
     * appId
     */
    @Setter
    @Getter
    @NonNull
    private String appId;
    /**
     * base64Cert
     */
    @Setter
    @Getter
    @NonNull
    private String base64Cert;
    /**
     * 证书的策略Id
     */
    @Setter
    @Getter
    private String verifyCertPolicyId;

    public JSONObject toJSONString() {
        return JSONObject.parseObject(JSON.toJSONString(this));
    }

    /**
     * VerifyCertRequesrBean 构建
     *
     * @param appId              appId
     * @param base64Cert         证书base64
     * @param transId            业务ID
     * @param verifyCertPolicyId 证书策略
     * @return
     */
    public static VerifyCertRequesrBean build(String appId, String base64Cert, String transId, String verifyCertPolicyId) {
        VerifyCertRequesrBean verifyCertRequesrBean = new VerifyCertRequesrBean();
        verifyCertRequesrBean.setAppId(appId);
        verifyCertRequesrBean.setTransId(transId);
        verifyCertRequesrBean.setBase64Cert(base64Cert);
        verifyCertRequesrBean.setVerifyCertPolicyId(verifyCertPolicyId);
        return verifyCertRequesrBean;
    }


}