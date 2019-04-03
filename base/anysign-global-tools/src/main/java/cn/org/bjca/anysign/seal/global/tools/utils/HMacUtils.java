package cn.org.bjca.anysign.seal.global.tools.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zjgao
 * @create 2018/9/20.
 * @description
 */
@Slf4j
public class HMacUtils {


  /**
   * @param message 签名原文（可见字符串）
   * @param secret 密钥原文（可见字符串）
   * @return 签名结果
   */
  public static byte[] sha256_HMAC(String message, String secret) {
    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
      sha256_HMAC.init(secret_key);
      byte[] bytes = sha256_HMAC.doFinal(message.getBytes());
      return bytes;
    } catch (Exception e) {
      log.error("compute HMACSHA256 error ", e);
    }
    return null;
  }

  /**
   * @param message 签名原文字节数组
   * @param secret 密钥原文字节数组
   */
  public static byte[] sha256_HMAC(byte[] message, byte[] secret) {
    try {
      Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secret_key = new SecretKeySpec(secret, "HmacSHA256");
      sha256_HMAC.init(secret_key);
      byte[] bytes = sha256_HMAC.doFinal(message);
      return bytes;
    } catch (Exception e) {
      log.error("compute HMACSHA256 error ", e);
    }
    return null;
  }

}
