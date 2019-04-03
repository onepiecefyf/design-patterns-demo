package cn.org.bjca.anysign.seal.signature;

import cn.org.bjca.anysign.pki.MessageDigest.MessageDigest;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.Alg;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.ProtectedMode;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.RelativePositionType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.SealPositionType;
import cn.org.bjca.anysign.seal.signature.bean.DigestRequest;
import cn.org.bjca.anysign.seal.signature.bean.KwRule;
import cn.org.bjca.anysign.seal.signature.bean.MergingRequest;
import cn.org.bjca.anysign.seal.signature.bean.Seal;
import cn.org.bjca.anysign.seal.signature.bean.SealPosition;
import cn.org.bjca.anysign.seal.signature.bean.SignatureRequest;
import cn.org.bjca.anysign.seal.verify.bean.SignatureVerifyRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.util.encoders.Base64;
import org.junit.Test;

/**
 * @author zjgao
 * @create 2018/10/8.
 * @description
 */
public class SignatureTest {

  private byte[] picContent;

  public static void main(String[] args) {
    for (int i = 0; i < 1; i++) {
      try {
        testSignature("RSA");
      } catch (IOException e) {
        e.printStackTrace();
      }
//            String url = "http://beta.isignet.cn:28081/seal/v1/pdfSignature";
//            JSONObject object = HttpUtils.doPost(url, JSON.parseObject(JSON.toJSONString(buildSignatureRequest2("RSA"))));
//            System.out.println(object.toString());
//            System.out.println(object.getString("status"));
    }
//        testVerifiy("RSA");
//        testDigest("RSA");
//        testMerging();
//        try {
//            byte[] rsaTssDsvsWithCertBytes = Base64Utils.base64String2ByteFun("MIIHYgYJKoZIhvcNAQcCoIIHUzCCB08CAQMxDzANBglghkgBZQMEAgEFADBrBgsqhkiG9w0BCRABBKBcBFowWAIBAQYBKjAxMA0GCWCGSAFlAwQCAQUABCDR1Cf+DSQymVqSAcFDUpAwtLVxxuwJcXFw2GkIwFXzUAIEBfXhARgPMjAxODEwMTAxMzExMzRaAgYBZl4am+egggSgMIIEnDCCA4SgAwIBAgIKG0AAAAAAAAdzNTANBgkqhkiG9w0BAQUFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMjAeFw0xODA5MjcxNjAwMDBaFw0yNDEyMzExNTU5NTlaMFUxCzAJBgNVBAYTAkNOMRIwEAYDVQQKDAlCSkNBQ2xvdWQxDDAKBgNVBAsMA1RTUzEkMCIGA1UEAwwb5LqR5pyN5Yqh5pe26Ze05oizUlNB6K+B5LmmMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApSWio1vzDNiTm6lP+DLeg/bZGmxoLBKIAr7Tk8evQ5kdtAD0YsoGwZnrpScTG8U6vtApxy9NTyTMWwkwWhN0L4PHjDhmpKqiO0yHYpeZcxpUyOunORUbdHjEJAiT76SN97k+wH/p/8zUpOh5VmOoNuWY9mRTLcVV0cp8RjxIfQ7LU3Ugzn1Xojrg2OX6scawMCq27vAhbP+VdwB993wVZ006T1ldJDes3vKqNvPcQaXD/ncxo+U/xcjGoFifZu6KoPK/Jjj5Hrb0ssjviR1uxouszGom6SZdEnV1ixn0kLSlP1wxMIn/vxaulwMZjZ125XN94e17CsADlaj78eviVQIDAQABo4IBbzCCAWswHwYDVR0jBBgwFoAU+7fUVhdYjCN91fhCAdTtd5tX6+kwga0GA1UdHwSBpTCBojBsoGqgaKRmMGQxCzAJBgNVBAYTAkNOMQ0wCwYDVQQKDARCSkNBMRgwFgYDVQQLDA9QdWJsaWMgVHJ1c3QgQ0ExGjAYBgNVBAMMEVB1YmxpYyBUcnVzdCBDQS0yMRAwDgYDVQQDEwdjYTRjcmwyMDKgMKAuhixodHRwOi8vbGRhcC5iamNhLm9yZy5jbi9jcmwvcHRjYS9jYTRjcmwyLmNybDAJBgNVHRMEAjAAMBEGCWCGSAGG+EIBAQQEAwIA/zAWBgNVHSUBAf8EDDAKBggrBgEFBQcDCDBABgNVHSAEOTA3MDUGCSqBHIbvMgICATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczALBgNVHQ8EBAMCA/gwEwYKKoEchu8yAgEBHgQFDAM2NTQwDQYJKoZIhvcNAQEFBQADggEBAAzr8v7B9RlLby2bONaUCGyIZnPDdEswFonKqwK2d+8u/LaUghFZ+b1meLFeQqETHU8tM0RQgzir1+zdEIb91Dg77Bz45Trjnb4vRXHb0YvEIqX5yBgjW7xH0AE76xgcABBLyanVjCUIbioHe5gk9g4BNz0G83tE75MBUyc+jci4ZJ6VDQcJPPeRA2ahaSNmtGy3QWgim0dd2AM7DBb0Pr2Qketshw8IIKcmBCuXXkXShU1NORby6JoOmJ3pEnqewBYcVQtJM0xJwyRWaV5Dsy+0GPhlCX+u065ahHY9NBcnFJGKzuZb6qo5fithrgYQn8DKDoBfE03CNL3aHcCvUNYxggImMIICIgIBATBgMFIxCzAJBgNVBAYTAkNOMQ0wCwYDVQQKDARCSkNBMRgwFgYDVQQLDA9QdWJsaWMgVHJ1c3QgQ0ExGjAYBgNVBAMMEVB1YmxpYyBUcnVzdCBDQS0yAgobQAAAAAAAB3M1MA0GCWCGSAFlAwQCAQUAoIGYMBoGCSqGSIb3DQEJAzENBgsqhkiG9w0BCRABBDAcBgkqhkiG9w0BCQUxDxcNMTgxMDEwMTMxMTM0WjArBgsqhkiG9w0BCRACDDEcMBowGDAWBBQzqmjshJVHJHhDEiCUU/NqmoUKyzAvBgkqhkiG9w0BCQQxIgQgio7KDf+o6Uus+GZ7ZGI9SJSA/WObTclMUtmo5EeIG7kwDQYJKoZIhvcNAQEBBQAEggEAaRyxM/kAL7aWIHO3Tc1f45OyLfFbCCQNTKRV0GqSxrgsDqqJupGxcSLk6alGLfkyNWx4vlFxDLp4BpU6R0FJvm9kejC1v9RdMLEVum0As/+EGWeHH02kSJJdtWvhpQNeCTvJ2uvXx6AxtcIiou+G3kjCkeGiDgejoVwB5gTvWQx8jYATX4sTcnmsCPya8TzB8RkNC414yzEtc6hQOabRVfdchYYPYEFPLTbyBwI0wUvoV2HPEUzEQoEJ3/BRu0C6UtfX4uCCy0wVdesjDsuL6UWKaWHTw/b28UMG7U0hspHFqEnbhe+vyFM99afnYO2WOgGs3Uqs3OBwESR3QXRAAg==");
//            OutputStream o1 = new FileOutputStream("d:/rsatss-dsvs-withcert.der");
//            o1.write(rsaTssDsvsWithCertBytes);
//            o1.close();
//
//            byte[] rsaTssDsvsWithoutCertBytes = Base64Utils.base64String2ByteFun("MIICvgYJKoZIhvcNAQcCoIICrzCCAqsCAQMxDzANBglghkgBZQMEAgEFADBrBgsqhkiG9w0BCRABBKBcBFowWAIBAQYBKjAxMA0GCWCGSAFlAwQCAQUABCDR1Cf+DSQymVqSAcFDUpAwtLVxxuwJcXFw2GkIwFXzUAIEBfXhARgPMjAxODEwMTAxMzE5MzJaAgYBZl4h5tMxggImMIICIgIBATBgMFIxCzAJBgNVBAYTAkNOMQ0wCwYDVQQKDARCSkNBMRgwFgYDVQQLDA9QdWJsaWMgVHJ1c3QgQ0ExGjAYBgNVBAMMEVB1YmxpYyBUcnVzdCBDQS0yAgobQAAAAAAAB3M1MA0GCWCGSAFlAwQCAQUAoIGYMBoGCSqGSIb3DQEJAzENBgsqhkiG9w0BCRABBDAcBgkqhkiG9w0BCQUxDxcNMTgxMDEwMTMxOTMyWjArBgsqhkiG9w0BCRACDDEcMBowGDAWBBQzqmjshJVHJHhDEiCUU/NqmoUKyzAvBgkqhkiG9w0BCQQxIgQgdGiYqRbwrHwlYwHqfHLoXppeJ2GV1iPcgQdl9uH/MqwwDQYJKoZIhvcNAQEBBQAEggEAoLO44uUu+72em7666wdMnXMTAyowNp2xhYtfnMS+tbt2PwQzedCEdxu8RKzaJJPMFd4XFJFjBsMquoJj1qT2SCv4ul1lGeYozRevQLaL/vHu8KVqBtClYg4pwEKwWdMk9f7YSPnvnacjsQEsg3Kg6Sr9A8MQ4Q5qYbZnRXDn6bUKJTpdseK/tZgctaF+IjQqmAw2gjijO6FeO+azpGMteT7nae9hRnUeR7Z1HhsUMq+CJXYN7ErIk4Ygs3ZlfhsOjibhaBgCoAsV+XElfmIKtp2FFVJc4Md7bf9bPwn9RiLG6RSQf1wIvJtOeZ2CtZj+vS9vVdS3OxU7/RT+eDk9rQ==");
//             OutputStream o2 = new FileOutputStream("d:/rsatss-dsvs-withoutcert.der");
//            o2.write(rsaTssDsvsWithoutCertBytes);
//            o2.close();
//
//            byte[] sm2TssDsvsWithCertBytes = Basujm,e64Utils.base64String2ByteFun("MIIEowYKKoEcz1UGAQQCAqCCBJMwggSPAgEBMQ8wDQYJKoEcz1UBgxEBBQAwZgYLKoZIhvcNAQkQAQSgVwRVMFMCAQEGCCsGAQUFBwMIMC0GCSqBHM9VAYMRAQQgyRTV3+zGX/FQNkhUBfVutgMg3ZAyWY2s912Mbxhm4CUCBAX14QEYDzIwMTgxMDEwMTMyMzA1WqCCAt0wggLZMIICgKADAgECAgoaEAAAAAAAyOGrMAoGCCqBHM9VAYN1MEQxCzAJBgNVBAYTAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRcwFQYDVQQDDA5CZWlqaW5nIFNNMiBDQTAeFw0xODA5MjcxNjAwMDBaFw0yODA5MjgxNTU5NTlaMIGJMSQwIgYDVQQDDBvkupHmnI3liqHml7bpl7TmiLNTTTLor4HkuaYxDDAKBgNVBAsMA1RTUzENMAsGA1UECgwEQkpDQTESMBAGA1UECgwJQkpDQUNsb3VkMREwDwYDVQQHDAggQmVpSmluZzEQMA4GA1UECAwHQmVpSmluZzELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAAT8eKJQ86qUSys6L0Buun0NmLFbh0q6HPMTdwhL2QYL2NNAEtBB/IKs+oNVjsPjG2KubOwYeg0qRgpuoo3wF5wZo4IBEjCCAQ4wHwYDVR0jBBgwFoAUH+bP1I/FIiqXSimKFecWyZI0xLYwgZ0GA1UdHwSBlTCBkjBgoF6gXKRaMFgxCzAJBgNVBAYTAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRcwFQYDVQQDDA5CZWlqaW5nIFNNMiBDQTESMBAGA1UEAxMJY2EyMWNybDY3MC6gLKAqhihodHRwOi8vY3JsLmJqY2Eub3JnLmNuL2NybC9jYTIxY3JsNjcuY3JsMBEGCWCGSAGG+EIBAQQEAwIA/zALBgNVHQ8EBAMCA/gwFgYDVR0lAQH/BAwwCgYIKwYBBQUHAwgwEwYKKoEchu8yAgEBHgQFDAM2NTQwCgYIKoEcz1UBg3UDRwAwRAIgFMXAjUPcI8smRaMtbzJJ0rraaWOMDewC59DLgXaNOD4CICDg2CXDklJN4t6d7sRvGaaeHmny7zTzMvxsnZspkQDYMYIBLjCCASoCAQEwUjBEMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0ECChoQAAAAAADI4aswDAYIKoEcz1UBgxEFAKBrMBoGCSqGSIb3DQEJAzENBgsqhkiG9w0BCRABBDAcBgkqhkiG9w0BCQUxDxcNMTgxMDEwMTMyMzA1WjAvBgkqhkiG9w0BCQQxIgQgPt6jp4RgnALPC12I7pikIcy94ZXAGmJSe32M/ogn0UwwDQYJKoEcz1UBgi0BBQAERzBFAiEAyiAaYXpOrz+4mVloHTruY+65HsahYcDiMTEYEjFyhp0CIALRD+6m8l50sQV/dD+QFWUUnla5FA72Q1QokcXRH6aI");
//            OutputStream o3 = new FileOutputStream("d:/sm2tss-dsvs-withcert.der");
//            o3.write(sm2TssDsvsWithCertBytes);
//            o3.close();
//
//
//            byte[] sm2TssDsvsWithoutCertBytes = Base64Utils.base64String2ByteFun("MIIBwgYKKoEcz1UGAQQCAqCCAbIwggGuAgEBMQ8wDQYJKoEcz1UBgxEBBQAwZgYLKoZIhvcNAQkQAQSgVwRVMFMCAQEGCCsGAQUFBwMIMC0GCSqBHM9VAYMRAQQgyRTV3+zGX/FQNkhUBfVutgMg3ZAyWY2s912Mbxhm4CUCBAX14QEYDzIwMTgxMDEwMTMyMzQ3WjGCAS4wggEqAgEBMFIwRDELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExFzAVBgNVBAMMDkJlaWppbmcgU00yIENBAgoaEAAAAAAAyOGrMAwGCCqBHM9VAYMRBQCgazAaBgkqhkiG9w0BCQMxDQYLKoZIhvcNAQkQAQQwHAYJKoZIhvcNAQkFMQ8XDTE4MTAxMDEzMjM0N1owLwYJKoZIhvcNAQkEMSIEIMUw15vc15g5lE2LC8riDV2NVLKi/jjT2VFhM5FRegH6MA0GCSqBHM9VAYItAQUABEcwRQIhANJs8cHhfLEGPHIRKRCRkNSLnXteZq4gEl2ag0DrMhurAiALOfT/AoKUJ16WFeCByYrcs57Cv/hcTptm+4Co/DN/3g==");
//            OutputStream o4 = new FileOutputStream("d:/sm2tss-dsvs-withoutcert.der");
//            o4.write(rsaTssDsvsWithoutCertBytes);
//            o4.close();
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }

  }

  public static void testSignature(String alg) throws IOException {
    System.out.println(JSON.toJSONString(buildSignatureRequest(alg)));
    String url = "http://127.0.0.1:10501/seal/v1/pdfSignature";
//        String url = "http://192.168.214.24:10501/seal/v1/pdfSignature";
//        String url = "http://beta.isignet.cn:28081/seal/v1/pdfSignature";
    JSON.parseObject(JSON.toJSONString(buildSignatureRequest(alg)), SignatureRequest.class);
//        String url = "https://api-sit.isignet.cn:8082/seal/v1/pdfSignature";
//        String url = "https://api.isignet.cn/seal/v1/pdfSignature";
    JSONObject object = HttpUtils
        .doPost(url, JSON.parseObject(JSON.toJSONString(buildSignatureRequest(alg))));
    System.out.println(object.toString());
    System.out.println(object.getString("status"));
  }

  public static void testVerifiy(String alg) {
    System.out.println(JSON.toJSONString(buildSignatureVerifyRequest(alg)));
//        String url = "http://127.0.0.1:10501/seal/v1//sealVerify";
    String url = "https://api.isignet.cn//seal/v1/sealVerify";
//        String url = "http://beta.isignet.cn:28081/seal/v1/sealVerify";
    JSONObject object = HttpUtils
        .doPost(url, JSON.parseObject(JSON.toJSONString(buildSignatureVerifyRequest(alg))));
    System.out.println(object.getString("status"));
  }

  /**
   * 摘要测试
   *
   * @param alg 签名算法
   */
  public static void testDigest(String alg) {
    System.out.println(JSON.toJSONString(buildDigestRequestRequest(alg)));
//        String url = "http://beta.isignet.cn:28081/seal/v1/pdfDigest";
    String url = "http://127.0.0.1:10501/seal/v1/pdfDigest";
//        String url = "https://api.isignet.cn/seal/v1/pdfDigest";

    JSONObject object = HttpUtils
        .doPost(url, JSON.parseObject(JSON.toJSONString(buildDigestRequestRequest(alg))));
    System.out.println(object.getString("status"));
  }

  public static void testMerging() {
    System.out.println(JSON.toJSONString(mergingRequest()));
//        String url = "http://127.0.0.1:10501/seal/v1/pdfMerging";
    String url = "http://beta.isignet.cn:28081/seal/v1/sealMerging";
    JSONObject object = HttpUtils
        .doPost(url, JSON.parseObject(JSON.toJSONString(mergingRequest())));
    System.out.println(object.getString("status"));
  }


  public static MergingRequest mergingRequest() {
    MergingRequest mergingRequest = new MergingRequest();
    /****/
    mergingRequest.setAppId("APP_5439463B5AA7450EA8D784E4AF70D33C");
    mergingRequest.setDeviceId("DEV_E745E51F740E4472AB55D00F3A77163C");
    mergingRequest.setSignAlgo("HMAC");
    mergingRequest.setSignature("Tqn1zejjneKdpqccW2dVW8fpWjRt40CN");
    mergingRequest.setVersion("1.0");
    mergingRequest.setExpire(5000);
    /*******/
    mergingRequest.setTransId("TRANS_1526754312345");
    mergingRequest.setFileTransmissionType(FileTransmissionType.CONTENT);
    mergingRequest.setSignData(
        "QqyHwXecNz3dtqpYuqS7HAZH8yfShisEePPZE7dvtfDzPqa1gn56Yidm0hnyjUnqOiRv+tGAj/crkC2t/QTN6F3Fq14CyPu+hZCZlCtKymN3thfSplZ/z0wH5YTTMT2U+2NbmM8ZZtxPzn42BI4vDEuqd4B2HI6264EAS7BqJB0=");
    byte[] buffer = null;
    InputStream is = null;
    try {
      is = new FileInputStream("E:\\test\\22.pdf");
      buffer = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    mergingRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));
//        mergingRequest.setRequestFileId("72c93f82ce0e4b4c9eba24f35101fcc2");
    return mergingRequest;
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
    signatureRequest.setTransId("TRANSID_21081213152100");
    signatureRequest.setFileTransmissionType(FileTransmissionType.CONTENT);
    byte[] buffer = null;
    InputStream is = null;
    try {
      is = new FileInputStream("E:/test.pdf");
      buffer = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    signatureRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));
    signatureRequest.setRequestFileId("7ad50127fb4f47a3bc1eb49580c3778d");
    signatureRequest.setRequestFileToken("3efd5480f01e4ca09821f8e7273a07cb");
    List<Seal> sealList = new LinkedList<Seal>();
    Seal seal = new Seal();
    if ("RSA".equalsIgnoreCase(keyAlg)) {
      seal.setKeyId("KEY_e893a232-3e9d-4f8f-97e8-cc0096f5841a");
      seal.setVerifyCode("1221");
//            seal.setSignCert("MIIEETCCAvmgAwIBAgISIAGfhu1G5mVA+yFftMMxL9faMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwNTIzMTYwMDAwWhcNMTkwNjI1MTU1OTU5WjBNMTAwLgYKCZImiZPyLGQBAQwgNTBBQTQxRTFEM0I0MjdEQkRFODg5MDk0RDAxM0EwNjYxDDAKBgNVBAMMA2N4ajELMAkGA1UEBgwCQ04wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCWsQMI+ZlZJMtDie5XsjDhRdkgQWDSD9zBiN/b+rs5Viv/a+qlX6DpYCjMRjAZAHRTNNSYiUdbEwpWq5PVy0nzaUpwaO/m6FbvQrkNn15b/On674AI6A/NTBkd8BfilxxhfkePYL88lzLhUfE6guQjfuUaLs4ImVrdOCucIa2Z1vqurxx/Rfmb+tCgRA90FexnSZ8IDDku9korrwFg0CXRMWFgtqlLYq9VLXZ63hPG/2XpmHABG28tZV1JBAAJ6Ya/wuRJ84SjZudNPqyAAlgDRZvJObrGHz/MWIwsdddEu2Vh7Yzkag7L+wtIIkTbSyZRkcTKaFZpSzHiMPuF1C3BAgMBAAGjgfUwgfIwCwYDVR0PBAQDAgSwMAkGA1UdEwQCMAAwgZcGA1UdHwSBjzCBjDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxSU0EyMDQ4L0xPQ0FMUlNBMjA0OF8wLmNybDBIoEagRIZCaHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxSU0EyMDQ4L2luYy9MT0NBTFJTQTIwNDhfaW5jXzAuY3JsMB0GA1UdDgQWBBR6DyZBkgI//MGvfbjUCocPWucEWTAfBgNVHSMEGDAWgBTEpQM1jPZ6+0tnIMy60SmRASP4lTANBgkqhkiG9w0BAQUFAAOCAQEAK0HMQjkUgmamMa9ot3Sf5w7EmXjA3tZtBtBZSMQ6Z2/YG+o+Rl4AEybHQ+aDV38C+CKDbS4a++a+dgQw3kBFl0/+ED8+VNN9Yzj3xl2FdLJ1GqndT9UhOysr2SNxui1Xy7Zm3qP1s1kovOhJCa2lQ3K0GyIaatKYvV1DnZyI8oQWdDpYl9IRJP08XLZXIiTbyxpEzciKCGgpcrb7JPU/9sSHinEd8cuaYRCkbv8rQo5gZLVF4nzXTZ9mnqo0ec4yRy+ECxQ2xE/p9UdHaUyGcU0W8XDJ6IP/1/ZZ72T5iVfeGbVk4RuOwKlDmJYakyVo3OAuzX5SoSPgkQgOhDZ0tQ==");
      seal.setSignCert(
          "MIIDjTCCAnWgAwIBAgISIAHwi17PdnDkQc2asSgFGLMwMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwNTIzMTYwMDAwWhcNMTkwNjI1MTU1OTU5WjBNMTAwLgYKCZImiZPyLGQBAQwgN0M4RjQ0RTc0MTRCMjdFMDg0MTJFOTA0MjAwRkUzQ0QxDDAKBgNVBAMMA3doajELMAkGA1UEBgwCQ04wgZ8wDQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAOAPMzp+zWk7dM5GRgtvmZswL1VolIf6gjGHfra0EYLUctViAnYgx5vGnGehEoLdVvP1/bN6aEbLBCAoSgnl0PYn94UIkNm+XeQsFC7yXimKdJG3U8eogunLMuXk6L7t13BuJL6U86WVQYZqFVGiuOI36Ykt56jezQRFyjc84xNDAgMBAAGjgfUwgfIwCwYDVR0PBAQDAgSwMAkGA1UdEwQCMAAwgZcGA1UdHwSBjzCBjDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxSU0EyMDQ4L0xPQ0FMUlNBMjA0OF8wLmNybDBIoEagRIZCaHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxSU0EyMDQ4L2luYy9MT0NBTFJTQTIwNDhfaW5jXzAuY3JsMB0GA1UdDgQWBBSVLdkRe7wpHbxqZ9vQrOCc4CgFDTAfBgNVHSMEGDAWgBTEpQM1jPZ6+0tnIMy60SmRASP4lTANBgkqhkiG9w0BAQUFAAOCAQEAfzS1OIRPS3uvqUbQTI649GamB1HuAkmw68yN26/ho7scHcA8WN5sorB3JsyNsKw3nVKJ1Eo0BAA08AxPiRyh5hBew4eVBbufbLLJNm8KSE9nxG3ZavazoAMEZHvZmF98S5qgdZjE20zhEvr6M1teS/ytBAkxQtnN24C4y6atgdwxLkgHnyvvz0Zohw/nB4sp2tbPk7AxM/ggDEq7GOBgK9h30dkBUzqkJMxPbd/ImAdtryXkvsh9KzJXdK9Tm6ewvNM6PtH5VedZUxMKNOsgsfOyGKQJtQJyiP8X4ERHH7YnGlaLQSglQwMTDf8XyQgQ43ydjZ/jEVTmspX0c1sAnw==");
    } else {
      seal.setKeyId("KEY_ed8cbbd3-5ea8-4e6c-8c5b-caef8a207be5");
      seal.setVerifyCode("1111");
      seal.setSignCert(
          "MIICXjCCAgGgAwIBAgISIAJEFwbirFOXiGOqvU87o4iRMAwGCCqBHM9VAYN1BQAwPjELMAkGA1UEBgwCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExETAPBgNVBAMMCExPQ0FMU00yMB4XDTE4MDUyMzE2MDAwMFoXDTE5MDQyNTE1NTk1OVowNzETMBEGA1UEAwwKQkpDQUp1bGlhbjETMBEGA1UECgwKQkpDQUp1bGlhbjELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATPizdys4XTyeCxIJnHBi8x5PAQ8k7Vq0MoVNwocHhXmzlhK02GJ4QnhL1zWwHRxsnxPuNBNT2CNgutuiEXNDzmo4HjMIHgMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGFBgNVHR8EfjB8MDigNqA0hjJodHRwOi8vMTkyLjE2OC4xMzYuMTQ5L2NybC9MT0NBTFNNMi9MT0NBTFNNMl8wLmNybDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxTTTIvaW5jL0xPQ0FMU00yX2luY18wLmNybDAdBgNVHQ4EFgQU5zS18Y9tiycf4+OIv2Hn8q30lIkwHwYDVR0jBBgwFoAUO/H7Ikz7oMn4CK0vrQqn96E6c9cwDAYIKoEcz1UBg3UFAANJADBGAiEAyfySOOCp4vmsXVDMKeN3VpoftIqJblKScOcNmjEqPEECIQCelIfxEgo1DBqjYuhQvpyOuq7oy10nqe5rLYYl5TSpUQ==");
//            seal.setSignCert("MIIDLTCCAtSgAwIBAgIKGhAAAAAAAHaUYTAKBggqgRzPVQGDdTBEMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0EwHhcNMTcxMTE1MTYwMDAwWhcNMjcxMTE2MTU1OTU5WjCBhzEeMBwGA1UEAwwVU00y5rWL6K+V5Y2V5L2N6K+B5LmmMQ0wCwYDVQQKDARCSkNBMS0wKwYDVQQKDCTljJfkuqzmlbDlrZforqTor4HogqHku73mnInpmZDlhazlj7gxDDAKBgNVBAcMAyBCSjEMMAoGA1UECAwDIEJKMQswCQYDVQQGDAJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABDhtl+UiaBbErGFHu735oy/iQ8z4mJ8r9EIoqxgrD8jWHVqWbUgruzVnPMMHkKzpvOEgiM4MFOvS9oFtUTc59O6jggFoMIIBZDAfBgNVHSMEGDAWgBQf5s/Uj8UiKpdKKYoV5xbJkjTEtjAdBgNVHQ4EFgQUDu029omYqyKz7jGZd7V5igSWIaswCwYDVR0PBAQDAgbAMIGdBgNVHR8EgZUwgZIwYKBeoFykWjBYMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0ExEjAQBgNVBAMTCWNhMjFjcmw0MDAuoCygKoYoaHR0cDovL2NybC5iamNhLm9yZy5jbi9jcmwvY2EyMWNybDQwLmNybDBgBggrBgEFBQcBAQRUMFIwIwYIKwYBBQUHMAGGF09DU1A6Ly9vY3NwLmJqY2Eub3JnLmNuMCsGCCsGAQUFBzAChh9odHRwOi8vY3JsLmJqY2Eub3JnLmNuL2NhaXNzdWVyMBMGCiqBHIbvMgIBAR4EBQwDNjU0MAoGCCqBHM9VAYN1A0cAMEQCICOCe1ED02TdZDs4nLEcyqj5Ung875YQlnRFZXtJ1aMtAiBYu9Vov8dLCscAmD1HpX2iO13JF4cT2M8RgSyYZC4Bog==");
    }
    seal.setVerifyCertPolicyId("TestCloudSign");
    seal.setProtectedMode(ProtectedMode.NOLIMITED);
    seal.setPrintable(true);
    seal.setTss(false);
    seal.setReason("test");
    seal.setLocation("bj");
    seal.setContact("www.bjca.org.cn");
    byte[] picContent = null;
    try {
      is = new FileInputStream("E:\\test\\1015.png");
      picContent = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    seal.setSealPicContent(Base64Utils.byte2Base64StringFun(picContent));
//        seal.setSealPicId("8086b4cf345f43d9b0aef492503e8858");
//        seal.setSealPicToken("c042598154b949bfa84b212b96eb907f");
    seal.setSealWidth(113.2f);
    seal.setSealHeight(113.2f);
    SealPosition sealPosition = new SealPosition();
    sealPosition.setPositionType(SealPositionType.KEYWORD);
//        XYZRuleInfo xyzRuleInfo = new XYZRuleInfo();
//        xyzRuleInfo.setPageNo(2);
//        xyzRuleInfo.setTop(200);
//        xyzRuleInfo.setRight(200);
//        xyzRuleInfo.setBottom(100);
//        xyzRuleInfo.setLeft(100);
//        xyzRuleInfo.setRelativePosition(RelativePositionType.LOWER);
//        xyzRuleInfo.setRightDeviation(200);
//        xyzRuleInfo.setLowerDeviation(200);
//        sealPosition.setXyzRuleInfo(xyzRuleInfo);

    KwRule kwRule = new KwRule();
    kwRule.setKw("的");
    kwRule.setKwOffSet("200");
    kwRule.setRelativePosition(RelativePositionType.LOWER);
    kwRule.setLowerDeviation(8);
    kwRule.setRightDeviation(89);
    sealPosition.setKwRule(kwRule);

//        AcrossPage acrossPage = new AcrossPage();
//        acrossPage.setAcrossPagePattern("R");
//        acrossPage.setStartWidth(20);
//        acrossPage.setPosCoord(100);
//        acrossPage.setAcrossPageType(AcrossPageType.MULTI_PAGE);
//        sealPosition.setAcrossPage(acrossPage);
    seal.setSealPosition(sealPosition);
    sealList.add(seal);
    signatureRequest.setSealList(sealList);
    return signatureRequest;
  }


  public static SignatureRequest buildSignatureRequest2(String keyAlg) {
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
      is = new FileInputStream("E:/tttt.pdf");
      buffer = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    signatureRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));
    signatureRequest.setRequestFileId("7ad50127fb4f47a3bc1eb49580c3778d");
    signatureRequest.setRequestFileToken("3efd5480f01e4ca09821f8e7273a07cb");
    List<Seal> sealList = new LinkedList<Seal>();
    Seal seal = new Seal();
    if ("RSA".equalsIgnoreCase(keyAlg)) {
      seal.setKeyId("KEY_6b411f96-aaf3-42f6-b8ff-a67ad1a508e4");
      seal.setVerifyCode("1221");
//            seal.setSignCert("MIIEETCCAvmgAwIBAgISIAGfhu1G5mVA+yFftMMxL9faMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwNTIzMTYwMDAwWhcNMTkwNjI1MTU1OTU5WjBNMTAwLgYKCZImiZPyLGQBAQwgNTBBQTQxRTFEM0I0MjdEQkRFODg5MDk0RDAxM0EwNjYxDDAKBgNVBAMMA2N4ajELMAkGA1UEBgwCQ04wggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCWsQMI+ZlZJMtDie5XsjDhRdkgQWDSD9zBiN/b+rs5Viv/a+qlX6DpYCjMRjAZAHRTNNSYiUdbEwpWq5PVy0nzaUpwaO/m6FbvQrkNn15b/On674AI6A/NTBkd8BfilxxhfkePYL88lzLhUfE6guQjfuUaLs4ImVrdOCucIa2Z1vqurxx/Rfmb+tCgRA90FexnSZ8IDDku9korrwFg0CXRMWFgtqlLYq9VLXZ63hPG/2XpmHABG28tZV1JBAAJ6Ya/wuRJ84SjZudNPqyAAlgDRZvJObrGHz/MWIwsdddEu2Vh7Yzkag7L+wtIIkTbSyZRkcTKaFZpSzHiMPuF1C3BAgMBAAGjgfUwgfIwCwYDVR0PBAQDAgSwMAkGA1UdEwQCMAAwgZcGA1UdHwSBjzCBjDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxSU0EyMDQ4L0xPQ0FMUlNBMjA0OF8wLmNybDBIoEagRIZCaHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxSU0EyMDQ4L2luYy9MT0NBTFJTQTIwNDhfaW5jXzAuY3JsMB0GA1UdDgQWBBR6DyZBkgI//MGvfbjUCocPWucEWTAfBgNVHSMEGDAWgBTEpQM1jPZ6+0tnIMy60SmRASP4lTANBgkqhkiG9w0BAQUFAAOCAQEAK0HMQjkUgmamMa9ot3Sf5w7EmXjA3tZtBtBZSMQ6Z2/YG+o+Rl4AEybHQ+aDV38C+CKDbS4a++a+dgQw3kBFl0/+ED8+VNN9Yzj3xl2FdLJ1GqndT9UhOysr2SNxui1Xy7Zm3qP1s1kovOhJCa2lQ3K0GyIaatKYvV1DnZyI8oQWdDpYl9IRJP08XLZXIiTbyxpEzciKCGgpcrb7JPU/9sSHinEd8cuaYRCkbv8rQo5gZLVF4nzXTZ9mnqo0ec4yRy+ECxQ2xE/p9UdHaUyGcU0W8XDJ6IP/1/ZZ72T5iVfeGbVk4RuOwKlDmJYakyVo3OAuzX5SoSPgkQgOhDZ0tQ==");
      seal.setSignCert(
          "MIIEEjCCAvqgAwIBAgISIAFH7xi4vUAlybn00ECfZ7F6MA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwNTIzMTYwMDAwWhcNMTkwNjI1MTU1OTU5WjBOMTAwLgYKCZImiZPyLGQBAQwgMTE1MDM5OUFCNDA5NzRGMzhCOEQ2NkY4OThBMDlGMjcxDTALBgNVBAMMBHRlc3QxCzAJBgNVBAYMAkNOMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmvwH4Xfgkjg833mwQF7L/P9x7cFe6i1Z3DlV0JddzrtXLY9+ptPIq/o57iMcCVmoDmnK0AGYKGMrpZ64jdkq9EwVR/gxSoaKRketFClv+8faM/K8dltNH/Hr+To/TaPgaU3N5cJ8QIPl7L40qUNLQ82/0OAKps87pOjUWRPfrIN8c377/7Tz3SQSFP64WX+VEk/OC5FmLI+hAQv0gFqP21XHZY46pQTqo6wAWtlw6Hb6ZSDpUR+5soPUTwcdJvRGvTjy3jofEwdN/h0HlYZ1lq+wL6bjH1xAZgA42NOBATYALX2dXRulZZXCgwTJaQtQ9AzesvAI5dWVHUAfgdk3CwIDAQABo4H1MIHyMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGXBgNVHR8EgY8wgYwwQKA+oDyGOmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9MT0NBTFJTQTIwNDhfMC5jcmwwSKBGoESGQmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9pbmMvTE9DQUxSU0EyMDQ4X2luY18wLmNybDAdBgNVHQ4EFgQUFZtUNA2gvlqy784LQ05+j9zF7qkwHwYDVR0jBBgwFoAUxKUDNYz2evtLZyDMutEpkQEj+JUwDQYJKoZIhvcNAQEFBQADggEBAH9Yb6G3PkvBeohmyojAJlePerPzrD8ECycUvKHxKz5WKHCjVqGofUNQ/k/i+AnnC2KnT7kY0S477a2pswmQFWfCDQl2VPOtRmE9u0a3OUH/rugLvCugq/+VJqitqggaHWgUYX+tAO6mmKQSf/HzmNRlxJ28tUwdiTi5pCbI1alqi0z7wLCJXszBznLiu4a3GmQir7CiRckl2sdqvkabXGByfwP3wmetL0SGf3I6jlrWTUC+qk19Gmb3n8DX2LjUAbphdn4Zx0ixLFSdq7BqIL1OqzE/RKXyvKHLmzMlxRGM0W0eZDLL0PeodirSSsdgrcOrdNFVxVwbiSGrrd1loVM=");
    } else {
      seal.setKeyId("KEY_ed8cbbd3-5ea8-4e6c-8c5b-caef8a207be5");
      seal.setVerifyCode("1111");
      seal.setSignCert(
          "MIICXjCCAgGgAwIBAgISIAJEFwbirFOXiGOqvU87o4iRMAwGCCqBHM9VAYN1BQAwPjELMAkGA1UEBgwCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExETAPBgNVBAMMCExPQ0FMU00yMB4XDTE4MDUyMzE2MDAwMFoXDTE5MDQyNTE1NTk1OVowNzETMBEGA1UEAwwKQkpDQUp1bGlhbjETMBEGA1UECgwKQkpDQUp1bGlhbjELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATPizdys4XTyeCxIJnHBi8x5PAQ8k7Vq0MoVNwocHhXmzlhK02GJ4QnhL1zWwHRxsnxPuNBNT2CNgutuiEXNDzmo4HjMIHgMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGFBgNVHR8EfjB8MDigNqA0hjJodHRwOi8vMTkyLjE2OC4xMzYuMTQ5L2NybC9MT0NBTFNNMi9MT0NBTFNNMl8wLmNybDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxTTTIvaW5jL0xPQ0FMU00yX2luY18wLmNybDAdBgNVHQ4EFgQU5zS18Y9tiycf4+OIv2Hn8q30lIkwHwYDVR0jBBgwFoAUO/H7Ikz7oMn4CK0vrQqn96E6c9cwDAYIKoEcz1UBg3UFAANJADBGAiEAyfySOOCp4vmsXVDMKeN3VpoftIqJblKScOcNmjEqPEECIQCelIfxEgo1DBqjYuhQvpyOuq7oy10nqe5rLYYl5TSpUQ==");
//            seal.setSignCert("MIIDLTCCAtSgAwIBAgIKGhAAAAAAAHaUYTAKBggqgRzPVQGDdTBEMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0EwHhcNMTcxMTE1MTYwMDAwWhcNMjcxMTE2MTU1OTU5WjCBhzEeMBwGA1UEAwwVU00y5rWL6K+V5Y2V5L2N6K+B5LmmMQ0wCwYDVQQKDARCSkNBMS0wKwYDVQQKDCTljJfkuqzmlbDlrZforqTor4HogqHku73mnInpmZDlhazlj7gxDDAKBgNVBAcMAyBCSjEMMAoGA1UECAwDIEJKMQswCQYDVQQGDAJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABDhtl+UiaBbErGFHu735oy/iQ8z4mJ8r9EIoqxgrD8jWHVqWbUgruzVnPMMHkKzpvOEgiM4MFOvS9oFtUTc59O6jggFoMIIBZDAfBgNVHSMEGDAWgBQf5s/Uj8UiKpdKKYoV5xbJkjTEtjAdBgNVHQ4EFgQUDu029omYqyKz7jGZd7V5igSWIaswCwYDVR0PBAQDAgbAMIGdBgNVHR8EgZUwgZIwYKBeoFykWjBYMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0ExEjAQBgNVBAMTCWNhMjFjcmw0MDAuoCygKoYoaHR0cDovL2NybC5iamNhLm9yZy5jbi9jcmwvY2EyMWNybDQwLmNybDBgBggrBgEFBQcBAQRUMFIwIwYIKwYBBQUHMAGGF09DU1A6Ly9vY3NwLmJqY2Eub3JnLmNuMCsGCCsGAQUFBzAChh9odHRwOi8vY3JsLmJqY2Eub3JnLmNuL2NhaXNzdWVyMBMGCiqBHIbvMgIBAR4EBQwDNjU0MAoGCCqBHM9VAYN1A0cAMEQCICOCe1ED02TdZDs4nLEcyqj5Ung875YQlnRFZXtJ1aMtAiBYu9Vov8dLCscAmD1HpX2iO13JF4cT2M8RgSyYZC4Bog==");
    }
    seal.setVerifyCertPolicyId("TestCloudSign");
    seal.setProtectedMode(ProtectedMode.NOLIMITED);
    seal.setPrintable(true);
    seal.setTss(false);
    seal.setReason("test");
    seal.setLocation("bj");
    seal.setContact("www.bjca.org.cn");
    byte[] picContent = null;
    try {
      is = new FileInputStream("E:\\test\\1015.png");
      picContent = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    seal.setSealPicContent(Base64Utils.byte2Base64StringFun(picContent));
//        seal.setSealPicId("8086b4cf345f43d9b0aef492503e8858");
//        seal.setSealPicToken("c042598154b949bfa84b212b96eb907f");
    seal.setSealWidth(113.2f);
    seal.setSealHeight(113.2f);
    SealPosition sealPosition = new SealPosition();
    sealPosition.setPositionType(SealPositionType.KEYWORD);
//        XYZRuleInfo xyzRuleInfo = new XYZRuleInfo();
//        xyzRuleInfo.setPageNo(2);
//        xyzRuleInfo.setTop(200);
//        xyzRuleInfo.setRight(200);
//        xyzRuleInfo.setBottom(100);
//        xyzRuleInfo.setLeft(100);
//        xyzRuleInfo.setRelativePosition(RelativePositionType.LOWER);
//        xyzRuleInfo.setXOffset(200);
//        xyzRuleInfo.setYOffset(200);
//        sealPosition.setXyzRuleInfo(xyzRuleInfo);

    KwRule kwRule = new KwRule();
    kwRule.setKw("的");
    kwRule.setKwOffSet("200");
    kwRule.setRelativePosition(RelativePositionType.LOWER);
//        AcrossPage acrossPage = new AcrossPage();
//        acrossPage.setAcrossPagePattern("R");
//        acrossPage.setStartWidth(20);
//        acrossPage.setPosCoord(100);
//        acrossPage.setAcrossPageType(AcrossPageType.MULTI_PAGE);
//        sealPosition.setAcrossPage(acrossPage);
    seal.setSealPosition(sealPosition);
    sealList.add(seal);
    signatureRequest.setSealList(sealList);
    return signatureRequest;
  }


  public static DigestRequest buildDigestRequestRequest(String keyAlg) {
    DigestRequest digestRequest = new DigestRequest();
    /****/
    digestRequest.setAppId("APP_99A43C05454F4608B40D724521C36767");
    digestRequest.setDeviceId("DEV_E745E51F740E4472AB55D00F3A77163C");
    digestRequest.setSignAlgo("HMAC");
    digestRequest.setSignature("Tqn1zejjneKdpqccW2dVW8fpWjRt40CN");
    digestRequest.setVersion("1.0");
    digestRequest.setExpire(5000);
    /*******/
    digestRequest.setTransId("TRANS_1526754312345");
    digestRequest.setFileTransmissionType(FileTransmissionType.CONTENT);
    byte[] buffer = null;
    InputStream is = null;
    try {
      is = new FileInputStream("E:/test.pdf");
      buffer = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    digestRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));
//        digestRequest.setRequestFileId("7ad50127fb4f47a3bc1eb49580c3778d");
//        digestRequest.setRequestFileToken("3efd5480f01e4ca09821f8e7273a07cb");
    Seal seal = new Seal();
    if ("RSA".equalsIgnoreCase(keyAlg)) {
//            seal.setKeyId("KEY_7e15bc4b-1c9d-442c-af17-00a447549e6d");
//            seal.setVerifyCode("1231");
      seal.setSignCert(
          "MIIDczCCAlugAwIBAgISIAFMDW8YhPLUHvG4o4Q06xvfMA0GCSqGSIb3DQEBBQUAMEIxCzAJBgNVBAYMAkNOMQ0wCwYDVQQKDARCSkNBMQ0wCwYDVQQLDARCSkNBMRUwEwYDVQQDDAxMT0NBTFJTQTIwNDgwHhcNMTgwOTE5MTYwMDAwWhcNMjAwNDI1MTU1OTU5WjAzMREwDwYDVQQDDAhCSkNBYmpjYTERMA8GA1UECgwIQkpDQWJqY2ExCzAJBgNVBAYMAkNOMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYquj6EiELhzqLAXtOFzp96ja50mShcSd8DTtDZN2B1dI64+tkOmehqskEtPN637lQxmyMZVBV9iWftPB2+wW+uhnr9fbkYz9gVmxORRlNjLbgKJKmwk/1kf2ZYP81cv1vMwlYg2BvxkUMlWg0Rnvw/eotDnn0BaP3GSYTScuOjQIDAQABo4H1MIHyMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGXBgNVHR8EgY8wgYwwQKA+oDyGOmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9MT0NBTFJTQTIwNDhfMC5jcmwwSKBGoESGQmh0dHA6Ly8xOTIuMTY4LjEzNi4xNDkvY3JsL0xPQ0FMUlNBMjA0OC9pbmMvTE9DQUxSU0EyMDQ4X2luY18wLmNybDAdBgNVHQ4EFgQU5oBNMBA0lOTb3bJxkjJLacAHv2MwHwYDVR0jBBgwFoAUxKUDNYz2evtLZyDMutEpkQEj+JUwDQYJKoZIhvcNAQEFBQADggEBAK5E3UQLj4dwqqw3McSW0RqzURPZW5evvXyCypXWBvCu48SIYd6GmJ2rz4o+pu4LSCmroCXnmnY8cH98lKvkpTh7k3zOgK0FoeY6KoqJFgh6FOjCqXLJQ3zZDZkPOzjMhZOCcX2h89Xd3eG+t6ezXjEyMnmCB3SALh8SPwWdlFcG1EMHAlwEKJESKTcsUaGda2ExvVCy3V7sVnUVJTwa4yCA2jBrZRt2kgF74ddKUaH/Pkuu2zrO/PG9SMS2GY6v8N36V8vbQ5f4O3Ju4W0s+rAA0TRC/0X+DAsNqlsnLBEbIsWInDgB/Jh55MSxXrZNrNUv/RNURD9+i2OdfHO8ljQ=");
//            seal.setSignCert("MIIFhDCCBGygAwIBAgIKG0AAAAAAAAXauDANBgkqhkiG9w0BAQsFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMjAeFw0xNzExMTUxNjAwMDBaFw0yNDEyMzAxNTU5NTlaMFwxCzAJBgNVBAYTAkNOMS0wKwYDVQQKDCTljJfkuqzmlbDlrZforqTor4HogqHku73mnInpmZDlhazlj7gxHjAcBgNVBAMMFVJTQea1i+ivleWNleS9jeivgeS5pjCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEBAMF5K6pfkByAxm6N9fOu6Byd0QAht3P1/rTNQS/lVLxZpdkByvMuKrgTJC13njpcovrqH7udywO3p1kFQSbo4pJAQKtxq/UpqNqkEOxsur82TQn2bu2nuYm80yvTJ8355gbF69qvouSRNNRaDCFQdkBtVCXnJbQidVo4PQS+cqD6Stj8tMQRsJyIMHPmLO1ktsn8ws2r52YSs9F2PjVNwjqMfGd3ugd3LfT3djCQ/tazFEYTsbnE4I4MkN6vsHJ+I2Y0HYDZZK4T77pK6A4cMd5jBJdk1EolHWpeOSSMw3Hn7p7UbgvZ0WebjXNH2/FklDbaoMdexJIq7XCmm+WucNsCAwEAAaOCAlAwggJMMB8GA1UdIwQYMBaAFPu31FYXWIwjfdX4QgHU7XebV+vpMAsGA1UdDwQEAwIGwDCBrQYDVR0fBIGlMIGiMGygaqBopGYwZDELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExGDAWBgNVBAsMD1B1YmxpYyBUcnVzdCBDQTEaMBgGA1UEAwwRUHVibGljIFRydXN0IENBLTIxEDAOBgNVBAMTB2NhNGNybDIwMqAwoC6GLGh0dHA6Ly9sZGFwLmJqY2Eub3JnLmNuL2NybC9wdGNhL2NhNGNybDIuY3JsMAkGA1UdEwQCMAAwgecGA1UdIASB3zCB3DA1BgkqgRwBxTiBFQEwKDAmBggrBgEFBQcCARYaaHR0cDovL3d3dy5iamNhLm9yZy5jbi9jcHMwNQYJKoEcAcU4gRUCMCgwJgYIKwYBBQUHAgEWGmh0dHA6Ly93d3cuYmpjYS5vcmcuY24vY3BzMDUGCSqBHAHFOIEVAzAoMCYGCCsGAQUFBwIBFhpodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczA1BgkqgRwBxTiBFQQwKDAmBggrBgEFBQcCARYaaHR0cDovL3d3dy5iamNhLm9yZy5jbi9jcHMwYgYIKwYBBQUHAQEEVjBUMCgGCCsGAQUFBzABhhxPQ1NQOi8vb2NzcC5iamNhLm9yZy5jbjo5MDEyMCgGCCsGAQUFBzAChhxodHRwOi8vY3JsLmJqY2Eub3JnL2NhaXNzdWVyMBMGCiqBHIbvMgIBAR4EBQwDNjU0MA0GCSqGSIb3DQEBCwUAA4IBAQCagto1m9FSAnuo9rBVuKcloTJcRxYWpah4XeLms2TByVk8+ui8eiAlzySgutAc+CJQ6kPqvjyHKIiDGRQII0r4DZF0lKQSiBlotazksNnBkRKbbLmesI5T6omDQh+z7NCKCc3yJn770uVIe7+HsEusHysb7FcRQzkD5b7F/ZJJe/StlXm0DHw4DXtbP5Brt0W0M1EIOzIV4aGd1AxNVPfCt6kuVZ3WyyO8bZOLwzI63e0h1sO/mNwR1s4ouW5DrAfKPI1jqelD7Xgyh9knAyjmvqh1ypJdL6GIZwPziq6exIyb1Jt3Atvpe93tFGUWYLqXxWxy1s/zGfKtxPOYeLGg");
    } else {
      seal.setKeyId("KEY_ed8cbbd3-5ea8-4e6c-8c5b-caef8a207be5");
      seal.setVerifyCode("1111");
      seal.setSignCert(
          "MIICXjCCAgGgAwIBAgISIAJEFwbirFOXiGOqvU87o4iRMAwGCCqBHM9VAYN1BQAwPjELMAkGA1UEBgwCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExETAPBgNVBAMMCExPQ0FMU00yMB4XDTE4MDUyMzE2MDAwMFoXDTE5MDQyNTE1NTk1OVowNzETMBEGA1UEAwwKQkpDQUp1bGlhbjETMBEGA1UECgwKQkpDQUp1bGlhbjELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAATPizdys4XTyeCxIJnHBi8x5PAQ8k7Vq0MoVNwocHhXmzlhK02GJ4QnhL1zWwHRxsnxPuNBNT2CNgutuiEXNDzmo4HjMIHgMAsGA1UdDwQEAwIEsDAJBgNVHRMEAjAAMIGFBgNVHR8EfjB8MDigNqA0hjJodHRwOi8vMTkyLjE2OC4xMzYuMTQ5L2NybC9MT0NBTFNNMi9MT0NBTFNNMl8wLmNybDBAoD6gPIY6aHR0cDovLzE5Mi4xNjguMTM2LjE0OS9jcmwvTE9DQUxTTTIvaW5jL0xPQ0FMU00yX2luY18wLmNybDAdBgNVHQ4EFgQU5zS18Y9tiycf4+OIv2Hn8q30lIkwHwYDVR0jBBgwFoAUO/H7Ikz7oMn4CK0vrQqn96E6c9cwDAYIKoEcz1UBg3UFAANJADBGAiEAyfySOOCp4vmsXVDMKeN3VpoftIqJblKScOcNmjEqPEECIQCelIfxEgo1DBqjYuhQvpyOuq7oy10nqe5rLYYl5TSpUQ==");
//            seal.setSignCert("MIIDLTCCAtSgAwIBAgIKGhAAAAAAAHaUYTAKBggqgRzPVQGDdTBEMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0EwHhcNMTcxMTE1MTYwMDAwWhcNMjcxMTE2MTU1OTU5WjCBhzEeMBwGA1UEAwwVU00y5rWL6K+V5Y2V5L2N6K+B5LmmMQ0wCwYDVQQKDARCSkNBMS0wKwYDVQQKDCTljJfkuqzmlbDlrZforqTor4HogqHku73mnInpmZDlhazlj7gxDDAKBgNVBAcMAyBCSjEMMAoGA1UECAwDIEJKMQswCQYDVQQGDAJDTjBZMBMGByqGSM49AgEGCCqBHM9VAYItA0IABDhtl+UiaBbErGFHu735oy/iQ8z4mJ8r9EIoqxgrD8jWHVqWbUgruzVnPMMHkKzpvOEgiM4MFOvS9oFtUTc59O6jggFoMIIBZDAfBgNVHSMEGDAWgBQf5s/Uj8UiKpdKKYoV5xbJkjTEtjAdBgNVHQ4EFgQUDu029omYqyKz7jGZd7V5igSWIaswCwYDVR0PBAQDAgbAMIGdBgNVHR8EgZUwgZIwYKBeoFykWjBYMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0ExEjAQBgNVBAMTCWNhMjFjcmw0MDAuoCygKoYoaHR0cDovL2NybC5iamNhLm9yZy5jbi9jcmwvY2EyMWNybDQwLmNybDBgBggrBgEFBQcBAQRUMFIwIwYIKwYBBQUHMAGGF09DU1A6Ly9vY3NwLmJqY2Eub3JnLmNuMCsGCCsGAQUFBzAChh9odHRwOi8vY3JsLmJqY2Eub3JnLmNuL2NhaXNzdWVyMBMGCiqBHIbvMgIBAR4EBQwDNjU0MAoGCCqBHM9VAYN1A0cAMEQCICOCe1ED02TdZDs4nLEcyqj5Ung875YQlnRFZXtJ1aMtAiBYu9Vov8dLCscAmD1HpX2iO13JF4cT2M8RgSyYZC4Bog==");
//            seal.setSignCert("MIIETjCCA/WgAwIBAgIKGhAAAAAAAAXMtDAKBggqgRzPVQGDdTBEMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTENMAsGA1UECwwEQkpDQTEXMBUGA1UEAwwOQmVpamluZyBTTTIgQ0EwHhcNMTgwODAxMTYwMDAwWhcNMTkwODAyMTU1OTU5WjCBnTENMAsGA1UEKQwEMTIzNDEgMB4GA1UEAwwXc20y5Y2V5L2N6K+B5LmmKOa1i+ivlSkxGDAWBgNVBAsMDzYyMjAxODA1MDEwMDAwMTENMAsGA1UECgwEQkpDQTESMBAGA1UECgwJc20y5Y2V5L2NMQ8wDQYDVQQHDAblpKflhbQxDzANBgNVBAgMBuWMl+S6rDELMAkGA1UEBgwCQ04wWTATBgcqhkjOPQIBBggqgRzPVQGCLQNCAAT/AIqqfX+tIyFC444Mn/dsljaf2901EVOET49kxFmG/LQPfP7tKztqDmrsO04uXlj1UWDpQnH9NXrNT3/jcq9so4ICczCCAm8wHwYDVR0jBBgwFoAUH+bP1I/FIiqXSimKFecWyZI0xLYwHQYDVR0OBBYEFBVUR85dCMAzZEFGyCwNs4uaMUbHMAsGA1UdDwQEAwIGwDCBnQYDVR0fBIGVMIGSMGCgXqBcpFowWDELMAkGA1UEBhMCQ04xDTALBgNVBAoMBEJKQ0ExDTALBgNVBAsMBEJKQ0ExFzAVBgNVBAMMDkJlaWppbmcgU00yIENBMRIwEAYDVQQDEwljYTIxY3JsMjcwLqAsoCqGKGh0dHA6Ly9jcmwuYmpjYS5vcmcuY24vY3JsL2NhMjFjcmwyNy5jcmwwFgYKKoEchu8yAgEBAQQIDAZKSjEyMzQwYAYIKwYBBQUHAQEEVDBSMCMGCCsGAQUFBzABhhdPQ1NQOi8vb2NzcC5iamNhLm9yZy5jbjArBggrBgEFBQcwAoYfaHR0cDovL2NybC5iamNhLm9yZy5jbi9jYWlzc3VlcjBABgNVHSAEOTA3MDUGCSqBHIbvMgICATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vd3d3LmJqY2Eub3JnLmNuL2NwczARBglghkgBhvhCAQEEBAMCAP8wFAYKKoEchu8yAgEBCAQGDAQxMjM0MBYGCiqBHIbvMgIBAgIECAwGSkoxMjM0MB8GCiqBHIbvMgIBAQ4EEQwPMTAyMDAwMTAwMTI3NzI0MBYGCiqBHIbvMgIBAQQECAwGSkoxMjM0MCAGCiqBHIbvMgIBARcEEgwQMjRAMjE1MDA5SkowMTIzNDASBggqgRzQFAQBBAQGDAQxMjM0MBQGCiqBHIbvMgIBAR4EBgwEMTA1MDAKBggqgRzPVQGDdQNHADBEAiBMoXN9VE/ykYixSoOc3l7wdknpXSN5oTnFK/17wtd5ogIgdj5blFAfR13F9qeBnWEpkRb0MEcZJlnO1J6OjpN9pJ8=");
    }
    seal.setVerifyCertPolicyId("TestCloudSign");
    seal.setProtectedMode(ProtectedMode.NOLIMITED);
    seal.setPrintable(true);
    seal.setTss(false);
    seal.setReason("test");
    seal.setLocation("bj");
    seal.setContact("www.bjca.org.cn");
    byte[] picContent = null;
    try {
      is = new FileInputStream("E:/test.gif");
      picContent = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    seal.setSealPicContent(Base64Utils.byte2Base64StringFun(picContent));
//        seal.setSealPicId("8086b4cf345f43d9b0aef492503e8858");
//        seal.setSealPicToken("c042598154b949bfa84b212b96eb907f");
    seal.setSealWidth(0.0f);
    seal.setSealHeight(0.0f);
    SealPosition sealPosition = new SealPosition();
    sealPosition.setPositionType(SealPositionType.KEYWORD);
//        XYZRuleInfo xyzRuleInfo = new XYZRuleInfo();
//        xyzRuleInfo.setPageNo(1);
//        xyzRuleInfo.setTop(200);
//        xyzRuleInfo.setRight(200);
//        xyzRuleInfo.setBottom(100);
//        xyzRuleInfo.setLeft(100);
//        xyzRuleInfo.setRelativePosition(RelativePositionType.CENTER);
//        xyzRuleInfo.setXOffset(0);
//        xyzRuleInfo.setYOffset(0);
//        sealPosition.setXyzRuleInfo(xyzRuleInfo);

//        AcrossPage acrossPage = new AcrossPage();
//        acrossPage.setAcrossPagePattern("L");
//        acrossPage.setStartWidth(20);
//        acrossPage.setPosCoord(100);
//        acrossPage.setAcrossPageType(AcrossPageType.SINGLE_PAGE);
//        sealPosition.setAcrossPage(acrossPage);
    KwRule kwRule = new KwRule();
    kwRule.setKw("的");
    kwRule.setKwOffSet("200");
    kwRule.setRelativePosition(RelativePositionType.LOWER);
//        kwRule.setRExcursion(0);
//        kwRule.setLExcursion(-200);
    sealPosition.setKwRule(kwRule);

    seal.setSealPosition(sealPosition);
    digestRequest.setSeal(seal);
    return digestRequest;
  }


  public static SignatureVerifyRequest buildSignatureVerifyRequest(String alg) {
    SignatureVerifyRequest signatureVerifyRequest = new SignatureVerifyRequest();
    signatureVerifyRequest.setAppId("APP_CFFC7069B75241DBBEF84737F452FE2B");
    signatureVerifyRequest.setDeviceId("DEV_030C5011EDBB494E9DE654D22B19A6C6");
    signatureVerifyRequest.setSignAlgo("HMAC");
    signatureVerifyRequest.setSignature("Tqn1zejjneKdpqccW2dVW8fpWjRt40CN");
    signatureVerifyRequest.setTransId("TRANS_1526754312345");
    signatureVerifyRequest.setFileTransmissionType(FileTransmissionType.CONTENT);
    byte[] buffer = null;
    InputStream is = null;
    try {
      is = new FileInputStream("E://test//33.pdf");
      buffer = IOUtils.toByteArray(is);
    } catch (Exception e) {
      IOUtils.closeQuietly(is);
    }
    signatureVerifyRequest.setRequestFileContent(Base64Utils.byte2Base64StringFun(buffer));
    signatureVerifyRequest.setRequestFileId("ccd4e1eb5254411ca6dcc6f5cd5be000");
    signatureVerifyRequest.setRequestFileToken("ccd4e1eb5254411ca6dcc6f5cd5be000");
    signatureVerifyRequest.setAlg(Alg.valueOf(alg));
    return signatureVerifyRequest;
  }

  @Test
  public void cheakpdf() throws IOException, NoSuchProviderException, NoSuchAlgorithmException {
    byte[] bytes = FileUtils.readFileToByteArray(new File("E:\\test\\77.pdf"));
    System.out.println(bytes.length);
    byte[] oneBytes = new byte[222981];
    System.arraycopy(bytes, 0, oneBytes, 0, 222981);
    byte[] twoBytes = new byte[4235];
    System.arraycopy(bytes, 233001, twoBytes, 0, 4235);

    System.out.println(oneBytes.length);
    System.out.println(twoBytes.length);

    byte[] pdfBytes = byteMerger(oneBytes, twoBytes);
    System.out.println(pdfBytes.length);

    MessageDigest mg = MessageDigest.getInstance(MessageDigest.SHA256, pdfBytes);
    StringBuffer sb = new StringBuffer();

    for (byte b : mg.digest(null)) {
      sb.append(b);
      sb.append(",");
    }
    System.out.println(sb.toString());
    System.out.println(Base64.toBase64String(mg.digest(null)));

  }

  @Test
  public void Base64ToByte() {
    byte[] bytes = Base64Utils.base64String2ByteFun(
        "QqyHwXecNz3dtqpYuqS7HAZH8yfShisEePPZE7dvtfDzPqa1gn56Yidm0hnyjUnqOiRv+tGAj/crkC2t/QTN6F3Fq14CyPu+hZCZlCtKymN3thfSplZ/z0wH5YTTMT2U+2NbmM8ZZtxPzn42BI4vDEuqd4B2HI6264EAS7BqJB0=");
    StringBuffer sb = new StringBuffer();
    for (byte b : bytes) {
      sb.append(b);
      sb.append(",");
    }
    System.out.println(sb.toString());
  }

  @Test
  public void arrayToBytes() {
    byte[] bytes = {-47, -123, -79, 39, -67, -123, -124, -116, 120, -51, 124, 93, -118, 79, -123,
        -3, 18, 5, -42, -37, -81, 13, 25, -79, 75, -35, -11, 71, -108, 14, -91, -42, -90, 109, -99,
        -43, 49, -65, -64, -93, -18, 104, 88, 9, 10, -65, -24, 53, 110, 23, -82, -46, -27, -100, 25,
        72, -67, -1, 117, 101, -111, 14, -30, 84, 7, 47, 6, -73, 19, 106, 9, 4, 27, -7, -2, -99,
        -105, 53, -121, 103, 73, -109, -105, -35, -117, 103, -122, 59, -127, 61, 121, 112, 40, -88,
        -50, -107, -26, 92, -120, 39, 0, 62, 96, 49, 125, 58, -20, -13, -35, -91, -55, 88, -10, 113,
        -6, 45, -11, -89, -64, -98, -123, -48, 123, 90, 31, -2, 55, -3};
    String signValue = Base64Utils.byte2Base64StringFun(bytes);
    System.out.println(signValue);
    byte[] bytes1 = {49, 105, 48, 24, 6, 9, 42, -122, 72, -122, -9, 13, 1, 9, 3, 49, 11, 6, 9, 42,
        -122, 72, -122, -9, 13, 1, 7, 1, 48, 28, 6, 9, 42, -122, 72, -122, -9, 13, 1, 9, 5, 49, 15,
        23, 13, 49, 56, 49, 50, 49, 51, 48, 50, 48, 48, 49, 48, 90, 48, 47, 6, 9, 42, -122, 72,
        -122, -9, 13, 1, 9, 4, 49, 34, 4, 32, -4, 19, 14, -36, 19, 83, 78, -72, -21, 81, -18, 43,
        55, 115, 47, 83, -120, -6, -96, 90, -29, 65, 19, -9, 7, 127, -5, 27, -58, -82, -24, -109};
    String hash = Base64Utils.byte2Base64StringFun(bytes1);
    System.out.println(hash);


  }


  public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
    byte[] byte_3 = new byte[byte_1.length + byte_2.length];
    System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
    System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
    return byte_3;
  }


}
