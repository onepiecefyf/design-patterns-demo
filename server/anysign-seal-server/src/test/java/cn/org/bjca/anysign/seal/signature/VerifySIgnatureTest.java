package cn.org.bjca.anysign.seal.signature;

import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.Alg;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.verify.bean.SignatureVerifyRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;

/**
 * @author zjgao
 * @create 2018/10/8.
 * @description
 */
public class VerifySIgnatureTest {

  public static void main(String[] args) {
    System.out.println(JSON.toJSONString(buildSignatureVerifyRequest()));
    String url = "http://127.0.0.1:80/seal/v1/sealVerify";
    JSONObject object = HttpUtils
        .doPost(url, JSON.parseObject(JSON.toJSONString(buildSignatureVerifyRequest())));
    System.out.println(object);

  }

  public static SignatureVerifyRequest buildSignatureVerifyRequest() {
    SignatureVerifyRequest signatureVerifyRequest = new SignatureVerifyRequest();
    /****/
    signatureVerifyRequest.setAppId("test");
    signatureVerifyRequest.setDeviceId("test");
    signatureVerifyRequest.setSignAlgo("HMAC");
    signatureVerifyRequest.setSignature("test");
    /*******/
    signatureVerifyRequest.setAlg(Alg.RSA);
    signatureVerifyRequest.setTransId("1111");
    signatureVerifyRequest.setFileTransmissionType(FileTransmissionType.CONTENT);
    byte[] buffer = null;
    InputStream is = null;
    try {
      is = new FileInputStream("d:/test.pdf");
      IOUtils.read(is, buffer);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    signatureVerifyRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));

    return signatureVerifyRequest;
  }
}
