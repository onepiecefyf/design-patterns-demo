package cn.org.bjca.anysign.seal.server.common.message;

import java.io.Serializable;

/***************************************************************************
 * <pre>请求父报文</pre>
 *
 * @文件名称: BaseRequest.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/5 19:28
 ***************************************************************************/
public class BaseRequest implements Serializable{
    /**
     * 版本（*）
     */
    private String version;
    /**
     * 签名算法。推荐使用HMAC（HamcSHA256算法）计算HMAC值。
     * 开放给外部客户调用的接口必须要签名。
     */
    private String signAlgo;
    /**
     *签名数据。开放给外部客户调用的接口必须要签名。
     */
    private String signature;
    /**
     * 网关设备ID （*）
     */
    private String deviceId;
    /**
     * 应用ID （*）
     */
    private String appId;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSignAlgo() {
        return signAlgo;
    }

    public void setSignAlgo(String signAlgo) {
        this.signAlgo = signAlgo;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}