package cn.org.bjca.anysign.seal.service.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import com.alibaba.fastjson.JSON;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Data
@Slf4j
@Accessors(chain = true)
public class UploadRequestBean extends CommonRequestParam {


  private byte[] fileBytes;
  /**
   * 上传文件的mdf
   */
  private String md5;
  /**
   * 文件名
   */
  private String fileName;
  /**
   * 文件归属平台id 非必填
   */
  private String ownAppId;
  /**
   * 文件归属组
   */
  private String group;
  /**
   * 文件归属用户
   */
  private String user;
  /**
   * 文件有效期
   */
  private Integer expire;
  /**
   * 签名值
   */
  private String signature;

  public static UploadRequestBean build(String appId, String ownAppId, String deviceId,
      String version, String group, String user,
      int expire, String signature, String fileName, byte[] fileBytes) {
    UploadRequestBean uploadRequestBean = new UploadRequestBean();
    uploadRequestBean.setExpire(expire);
    uploadRequestBean.setAppId(appId);
    uploadRequestBean.setDeviceId(deviceId);
    uploadRequestBean.setVersion(version);
    uploadRequestBean.setGroup(group);
    uploadRequestBean.setOwnAppId(ownAppId);
    uploadRequestBean.setUser(user);
    uploadRequestBean.setFileBytes(fileBytes);
    uploadRequestBean.setFileName(fileName);
    uploadRequestBean.setSignature(signature);
    return uploadRequestBean;
  }


  public Map<String, String> toMap(String signatureKey) {
    signAlgo = "HmacSHA256";
    Map<String, String> param = new HashMap<String, String>(16);
    param.put("appId", appId);
    param.put("deviceId", deviceId);
    param.put("version", version);
    param.put("signAlgo", signAlgo);
    param.put("ownAppId", ownAppId);
    param.put("expire", String.valueOf(expire));
    try {
      param.put("md5", DigestUtils.md5Hex(new ByteArrayInputStream(fileBytes)));
    } catch (IOException e) {
      log.error("digest error ", e);
    }
    if (StringUtils.isEmpty(signature)) {
      param.put("signature", generateSignature(param, signatureKey));
    } else {
      param.put("signature", generateSignature(param, signature));
    }
    return param;
  }

  public String toJSONString(String signatureKey) {
    Map<String, String> param = toMap(signatureKey);
    return JSON.toJSONString(param);
  }

}
