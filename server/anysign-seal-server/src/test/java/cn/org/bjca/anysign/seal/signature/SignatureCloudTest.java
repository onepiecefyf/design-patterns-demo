package cn.org.bjca.anysign.seal.signature;

import cn.org.bjca.anysign.seal.Application;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import cn.org.bjca.anysign.seal.service.bean.DepositCertApplyRequestBean;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.ProtectedMode;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.RelativePositionType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.SealPositionType;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import cn.org.bjca.anysign.seal.signature.bean.Seal;
import cn.org.bjca.anysign.seal.signature.bean.SealPosition;
import cn.org.bjca.anysign.seal.signature.bean.SignatureRequest;
import cn.org.bjca.anysign.seal.signature.bean.XYZRuleInfo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author july_whj
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class SignatureCloudTest {

  private static String PDFPATH = "";
  private static String IMAGEPATH = "";
  private static String URL = "http://beta.isignet.cn:28081/";
  private static String URL2 = "http://127.0.0.1:10501/";

  @Autowired
  private SystemRequestService systemRequestService;

  @Test
  public void userApplyCert() {
    System.out.println(JSON.toJSON(buildDepositCertApplyRequestBean()));
    JSONObject object = systemRequestService.userApplyCert(buildDepositCertApplyRequestBean());
    System.out.println(object.toString());
    System.out.println(object.getString("status"));
    System.out.println(object.getString("data"));
    System.out.println(object.getJSONObject("data").getString("cert"));
  }


  @Test
  public void signatureRSA() {
    System.out.println(JSON.toJSONString(buildSignatureRequest("RSA")));
    String url = URL.concat("seal/v1/pdfSignature");
    JSONObject object = HttpUtils
        .doPost(url, JSON.parseObject(JSON.toJSONString(buildSignatureRequest("RSA"))));
    System.out.println(object.toString());
    System.out.println(object.getString("status"));
  }


  public static DepositCertApplyRequestBean buildDepositCertApplyRequestBean() {
    DepositCertApplyRequestBean depositCertApplyRequestBean =
        DepositCertApplyRequestBean
            .build("APP_5439463B5AA7450EA8D784E4AF70D33C", "DEV_E745E51F740E4472AB55D00F3A77163C",
                "HMAC", "1.0", "RSA", "2048", "", null, null
                , "张三", "USER_IDCARD", "13256423542651", "1223254226", null, "BJCA_RSA_SHA1",
                "TRANS_1234567910");
    return depositCertApplyRequestBean;
  }


  public static SignatureRequest buildSignatureRequest(String keyAlg) {
    SignatureRequest signatureRequest = new SignatureRequest();
    /****/
    signatureRequest.setAppId("APP_5439463B5AA7450EA8D784E4AF70D33C");
    signatureRequest.setDeviceId("DEV_E745E51F740E4472AB55D00F3A77163C");
    signatureRequest.setSignAlgo("HMAC");
    signatureRequest.setSignature("Tqn1zejjneKdpqccW2dVW8fpWjRt40CN");
    signatureRequest.setVersion("1.0");
    signatureRequest.setExpire(5000);
    /*******/
    signatureRequest.setTransId("TRANS_1526754312345");
    signatureRequest.setFileTransmissionType(FileTransmissionType.CONTENT);
    byte[] buffer = null;
    InputStream is = null;
    try {
      is = new FileInputStream(PDFPATH);
      buffer = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    signatureRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));
    List<Seal> sealList = new LinkedList<Seal>();
    Seal seal = new Seal();
    if ("RSA".equalsIgnoreCase(keyAlg)) {
      seal.setKeyId("KEY_7e15bc4b-1c9d-442c-af17-00a447549e6d");
      seal.setVerifyCode("1231");
      seal.setSignCert(
          "MIIDczCCAlugAwIBAgISIAFMDW8YhPLUHvG4o4Q06xvfMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwOTE5MTYwMDAwWhcNMjAwNDI1MTU1OTU5WjAzMREwDwYDVQQDDAhCSkNBYmpjYTERMA8GA1UECgwIQkpDQWJqY2ExCzAJBgNVBAYMAkNOMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYquj6EiELhzqLAXtOFzp96ja50mShcSd8DTtDZN2B1dI64+tkOmehqskEtPN637lQxmyMZVBV9iWftPB2+wW+uhnr9fbkYz9gVmxORRlNjLbgKJKmwk/1kf2ZYP81cv1vMwlYg2BvxkUMlWg0Rnvw/eotDnn0BaP3GSYTScuOjQIDAQABo4H1MIHyMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGXBgNVHR8EgY8wgYwwQKA+oDyGOmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9MT0NBTFJTQTIwNDhfMC5jcmwwSKBGoESGQmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9pbmMvTE9DQUxSU0EyMDQ4X2luY18wLmNybDAdBgNVHQ4EFgQU5oBNMBA0lOTb3bJxkjJLacAHv2MwHwYDVR0jBBgwFoAUxKUDNYz2evtLZyDMutEpkQEj+JUwDQYJKoZIhvcNAQEFBQADggEBAK5E3UQLj4dwqqw3McSW0RqzURPZW5evvXyCypXWBvCu48SIYd6GmJ2rz4o+pu4LSCmroCXnmnY8cH98lKvkpTh7k3zOgK0FoeY6KoqJFgh6FOjCqXLJQ3zZDZkPOzjMhZOCcX2h89Xd3eG+t6ezXjEyMnmCB3SALh8SPwWdlFcG1EMHAlwEKJESKTcsUaGda2ExvVCy3V7sVnUVJTwa4yCA2jBrZRt2kgF74ddKUaH/Pkuu2zrO/PG9SMS2GY6v8N36V8vbQ5f4O3Ju4W0s+rAA0TRC/0X+DAsNqlsnLBEbIsWInDgB/Jh55MSxXrZNrNUv/RNURD9+i2OdfHO8ljQ=");
    } else {
      seal.setKeyId("KEY_ed8cbbd3-5ea8-4e6c-8c5b-caef8a207be5");
      seal.setVerifyCode("1111");
      seal.setSignCert(
          "MIICXjCCAgGgAwIBAgISIAJEFwbirFOXiGOqvU87o4iRMAwGCCqBHM9VAYN1BQAwPjELMAkGA1UEBgwCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExETAPBgNVBAMMCExPQ0FMU00yMB4XDTE4MDUyMzE2MDAwMFoXDTE5MDQyNTE1NTk1OVowNzETMBEGA1UEAwwKQkpDQUp1bGlhbjETMBEGA1UECgwKQkpDQUp1bGlhbjELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATPizdys4XTyeCxIJnHBi8x5PAQ8k7Vq0MoVNwocHhXmzlhK02GJ4QnhL1zWwHRxsnxPuNBNT2CNgutuiEXNDzmo4HjMIHgMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGFBgNVHR8EfjB8MDigNqA0hjJodHRwOi8vMTkyLjE2OC4xMzYuMTQ5L2NybC9MT0NBTFNNMi9MT0NBTFNNMl8wLmNybDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxTTTIvaW5jL0xPQ0FMU00yX2luY18wLmNybDAdBgNVHQ4EFgQU5zS18Y9tiycf4+OIv2Hn8q30lIkwHwYDVR0jBBgwFoAUO/H7Ikz7oMn4CK0vrQqn96E6c9cwDAYIKoEcz1UBg3UFAANJADBGAiEAyfySOOCp4vmsXVDMKeN3VpoftIqJblKScOcNmjEqPEECIQCelIfxEgo1DBqjYuhQvpyOuq7oy10nqe5rLYYl5TSpUQ==");
    }
    seal.setVerifyCertPolicyId("TestCloudSign");
    seal.setProtectedMode(ProtectedMode.NOLIMITED);
    seal.setPrintable(true);
    seal.setTss(true);
    seal.setReason("test");
    seal.setLocation("bj");
    seal.setContact("www.bjca.org.cn");
    byte[] picContent = null;
    try {
      is = new FileInputStream(IMAGEPATH);
      picContent = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    seal.setSealPicContent(Base64Utils.byte2Base64StringFun(picContent));
    seal.setSealWidth(113.2f);
    seal.setSealHeight(113.2f);
    SealPosition sealPosition = new SealPosition();
    sealPosition.setPositionType(SealPositionType.COORDINATE);
    XYZRuleInfo xyzRuleInfo = new XYZRuleInfo();
    xyzRuleInfo.setPageNo(2);
    xyzRuleInfo.setTop(200);
    xyzRuleInfo.setRight(200);
    xyzRuleInfo.setBottom(100);
    xyzRuleInfo.setLeft(100);
    xyzRuleInfo.setRelativePosition(RelativePositionType.LOWER);
    xyzRuleInfo.setRightDeviation(200);
    xyzRuleInfo.setLowerDeviation(200);
    sealPosition.setXyzRuleInfo(xyzRuleInfo);
    seal.setSealPosition(sealPosition);
    sealList.add(seal);
    signatureRequest.setSealList(sealList);
    return signatureRequest;
  }
}
