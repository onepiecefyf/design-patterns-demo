package cn.org.bjca.anysign.seal.service.system;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.global.tools.utils.HttpUtils;
import cn.org.bjca.anysign.seal.service.bean.DepositCertApplyRequestBean;
import cn.org.bjca.anysign.seal.service.bean.DepositCertSignRequestBean;
import cn.org.bjca.anysign.seal.service.bean.DownloadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.GenTsReqRequestBean;
import cn.org.bjca.anysign.seal.service.bean.GenTsRespRequestBean;
import cn.org.bjca.anysign.seal.service.bean.ParseTsRespRequestBean;
import cn.org.bjca.anysign.seal.service.bean.UploadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.VerifyCertRequesrBean;
import cn.org.bjca.anysign.seal.service.bean.VerifySignRequestBean;
import cn.org.bjca.anysign.seal.service.bean.VerifyTsRespRequestBean;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import cn.org.bjca.footstone.metrics.client.metrics.Metrics;
import cn.org.bjca.footstone.metrics.client.metrics.MetricsClient;
import cn.org.bjca.footstone.metrics.client.metrics.QPS;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.IOException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zjgao
 * @create 2018/9/26.
 * @description
 */
@Service
@Slf4j
public class SystemRequestService {


  private static final String STATUS = "status";
  private static final String MESSAGE = "message";

  @Autowired
  private HttpConfig httpConfig;


  /**
   * 文件上传
   */
  public String upload(UploadRequestBean uploadRequestBean) {
    String message = null;
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "存储服务", "上传文件");
    Map<String, String> param = uploadRequestBean.toMap(httpConfig.secret);
    String url = httpConfig.fileAddress.concat(httpConfig.fileUpload);
    log.info("fileServer upload address is {}", url);
    log.info("request parameter :{} , TransId is {}",
        uploadRequestBean.toJSONString(httpConfig.secret),
        uploadRequestBean.getTransId());
    String fileName = uploadRequestBean.getFileName();
    try {
      message = HttpUtils.post(url, fileName, uploadRequestBean.getFileBytes(), param);
      JSONObject jsonObject = JSON.parseObject(message);
      if (String.valueOf(HttpStatus.SC_OK).equals(jsonObject.get(STATUS).toString())) {
        message = JSON.parseObject(jsonObject.getString("data")).getString("id");
        metrics.sr_incrSuccess();
      } else {
        String value = jsonObject.getString(STATUS).concat("-")
            .concat(jsonObject.getString(MESSAGE));
        QPS qps = Metrics.QPS("第三方服务存储服务", "上传文件", value);
        qps.record();
      }
    } catch (IOException e) {
      log.error("upload file fail ! ,TransId is {} ", uploadRequestBean.getTransId(), e);
    } finally {
      metrics.rt();
    }
    return message;
  }

  /**
   * 文件下载
   */
  public byte[] download(DownloadRequestBean downloadRequestBean) {
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "存储服务", "下载文件");
    String url = httpConfig.fileAddress.concat(httpConfig.fileDownload);
    log.info("fileServer download address is {}", url);
    log.info("request parameter :{}", downloadRequestBean.toJSONString(httpConfig.secret));
    Map<String, String> param = downloadRequestBean.toMap(httpConfig.secret);
    byte[] message = HttpUtils.doGetByte(url, param);
    if (null == message || message.length == 0) {
      log.error("file download error ! transId is {}", downloadRequestBean.getTransId());
      throw new BaseRuntimeException(StatusConstants.FILE_DOWNLOAD_ERROR, null, "下载文件");
    }
    metrics.rt().sr_incrSuccess();
    return message;
  }

  /**
   * 申请证书
   *
   * @param depositCertApplyRequestBean 请求对象Bean
   */
  public JSONObject userApplyCert(DepositCertApplyRequestBean depositCertApplyRequestBean) {
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "证书签名服务", "申请个人托管证书");
    String url = httpConfig.signatureAddress.concat(httpConfig.signatureUserApplyCert);
    log.info("userApplyCert address is {}", url);
    log.info("userApplyCert transId is : {} , request parameter :{}",
        depositCertApplyRequestBean.getTransId(),
        depositCertApplyRequestBean.toJSONString(httpConfig.secret));
    JSONObject request = depositCertApplyRequestBean.toJsonObject(httpConfig.secret);
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("userApplyCert transId is : {} , response data : {}",
        depositCertApplyRequestBean.getTransId(), result.toJSONString());
    if (String.valueOf(HttpStatus.SC_OK).equals(result.get(STATUS).toString())) {
      metrics.sr_incrSuccess();
    } else {
      String value = result.getString(STATUS).concat("-").concat(result.getString(MESSAGE));
      QPS qps = Metrics.QPS("第三方证书签名服务", "申请个人托管证书", value);
      qps.record();
    }
    metrics.rt();
    return result;
  }


  /**
   * 托管证书签名
   * <p>
   * <p>
   * response format : success { "status": 200, "message": "证书签名成功", "data": { "signValue":
   * "AeJsnvxIk9j3d+eozIBEp6QZ9hFUMNMOi6C21XXI1pHpuVet2ps4Abk3iN+6mDgc+ici5dVH82zjYKz92/8TUaW4RIAcKhRt4xtSSO6A7ouZ2h32debMxtjtZmUcK6p91qHpXHZjAOGz0WL+DbYr5j8F24iwwHROVFxze6gxl/M=",
   * "cert": "MIIFQjCCBCqgAwIBAgIKGzAAAAAAADfXvDANBgkqhkiG9w0BAQUFADBSMQswCQYDVQQGEwJDTjENMAsGA1UECgwEQkpDQTEYMBYGA1UECwwPUHVibGljIFRydXN0IENBMRowGAYDVQQDDBFQdWJsaWMgVHJ1c3QgQ0EtMTAeFw0xODA2MjUxNjAwMDBaFw0xOTA2MjYxNTU5NTlaMIGUMQswCQYDVQQGEwJDTjEtMCsGA1UECgwk5YyX5Lqs5pWw5a2X6K6k6K+B6IKh5Lu95pyJ6ZmQ5YWs5Y+4MRIwEAYDVQQDDAnmm7nnp4DlqJ8xJTAjBgkqhkiG9w0BCQEWFmNhb3hpdWp1YW5AYmpjYS5vcmcuY24xGzAZBgoJkiaJk/IsZAEpDAsxODgxMDE2NTkyMjCBnzANBgkqhkiG9w0BAQEFAAOBjQAwgYkCgYEAuGu4/aUSApeBUxelAB+u1ffOskf+7QoWigSdWG24eXoq62/PF+SpI/yWVFNuqPtI4wcbzOG4N5lltIaub2e/1/VdDHadsOC4O5Cub8Ze5BoIiR+DtwM5sIxs7lRunhxn12XT5zFQbuh8rTg2RnShHhaDvLVuc1UBTDAzP55FoqkCAwEAAaOCAlkwggJVMB8GA1UdIwQYMBaAFKw77K8Mo1AO76+vtE9sO9vRV9KJMB0GA1UdDgQWBBQQEKESghO2A3tUo4TVoNiA9MhHWDALBgNVHQ8EBAMCBsAwga8GA1UdHwSBpzCBpDBtoGugaaRnMGUxCzAJBgNVBAYTAkNOMQ0wCwYDVQQKDARCSkNBMRgwFgYDVQQLDA9QdWJsaWMgVHJ1c3QgQ0ExGjAYBgNVBAMMEVB1YmxpYyBUcnVzdCBDQS0xMREwDwYDVQQDEwhjYTNjcmwxNzAzoDGgL4YtaHR0cDovL2xkYXAuYmpjYS5vcmcuY24vY3JsL3B0Y2EvY2EzY3JsMTcuY3JsMAkGA1UdEwQCMAAwEQYJYIZIAYb4QgEBBAQDAgD/MB0GBSpWCwcBBBRTRjM0MTIyMzE5OTMwOTE3NTUyNjAdBgUqVgsHCAQUU0YzNDEyMjMxOTkzMDkxNzU1MjYwIAYIYIZIAYb4RAIEFFNGMzQxMjIzMTk5MzA5MTc1NTI2MBsGCCpWhkgBgTABBA8xMDIwMDAwMDk2ODI0NzEwJQYKKoEchu8yAgEEAQQXMUNAU0YzNDEyMjMxOTkzMDkxNzU1MjYwKgYLYIZIAWUDAgEwCQoEG2h0dHA6Ly9iamNhLm9yZy5jbi9iamNhLmNydDAPBgUqVhUBAQQGMTAwMDgwMEAGA1UdIAQ5MDcwNQYJKoEchu8yAgIBMCgwJgYIKwYBBQUHAgEWGmh0dHA6Ly93d3cuYmpjYS5vcmcuY24vY3BzMBMGCiqBHIbvMgIBAR4EBQwDNTIwMA0GCSqGSIb3DQEBBQUAA4IBAQCb/qU37lNd0yD9S33XEcPaPzjFgPhRrXTKQffPgJsfWpCw6L2UfZaNcdXF/9xhkD++jA8fjLPvGaXSak89PeHQeyJSRfqESbMFju6EIg1grhyyRdI3UoWLBNUlEDmByqtDbGGtrW6BkSo290rWaTCjKmpzoUyM+9thurYW+LATm/tknZesyC48BBFFe+o7scuoRqxTtBD7FxlS8RsC8foke3oolz0SKTPFAbHLt2rJWmhPoYpFVoLkU2ZzioCqjOeEbb04DnbmmeLJwXafE8ueAtvylwiW8JiQX+hZmjra7GVuBCgyo3fUFTgMw+eKnX95EvEfndi3m2KBvLkhD7ji",
   * "transId": "TRANS_1526754312345", "requestId": "71846741-7c60-4387-b320-f7d97580996a" } }
   * response format : error { "status": 70000023, "message": "DATATYPE错误" }
   */
  public JSONObject depositCertSign(DepositCertSignRequestBean depositCertSignRequestBean) {
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "证书签名服务", "托管证书签名");
    String url = httpConfig.signatureAddress.concat(httpConfig.signatureDepositCertSign);
    log.info("depositCertSign address is {}", url);
    log.info("depositCertSign transId is : {} , request parameter :{}",
        depositCertSignRequestBean.getTransId(),
        depositCertSignRequestBean.toJSONString(httpConfig.secret));
    JSONObject request = depositCertSignRequestBean.toJsonObject(httpConfig.secret);
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("depositCertSign transId is : {} , response data : {}",
        depositCertSignRequestBean.getTransId(), result);
    if (null != result) {
      if (String.valueOf(HttpStatus.SC_OK).equals(result.get(STATUS).toString())) {
        metrics.sr_incrSuccess();
      } else {
        String value = result.getString(STATUS).concat("-").concat(result.getString(MESSAGE));
        QPS qps = Metrics.QPS("第三方证书签名服务", "托管证书签名", value);
        qps.record();
      }
    }
    metrics.rt().sr_incrTotal();
    Assert.notNull(result, StatusConstants.PDF_SIGNATURE_ERROR, null, "PDF签章");
    return result;
  }

  /**
   * 验签（先验签名值再验证书） response format : success { "status": 200, "message": "证书签名验证成功", "data":{
   * "transId": "TRANS_15267543456789", "requestId": "7e9a2451-5562-4c22-8746-10a911b2bdcf" } }
   * response format : error { "status": 85006002, "message": "签名值验证不通过", "data": { "transId":
   * "TRANS_15267543456789", "requestId": "a6dea2c4-1a88-429a-8dcf-210af522b7bb" } }
   */
  public JSONObject verifySign(VerifySignRequestBean verifySignRequestBean) {
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "证书签名服务", "验签");
    String url = httpConfig.signatureAddress.concat(httpConfig.signatureVerifySign);
    JSONObject request = verifySignRequestBean.toJsonObject(httpConfig.secret);
    log.info("verifySign transId is : {} , request parameter :{}",
        verifySignRequestBean.getTransId(), request);
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("verifySign transId is : {} ,response data : {}",
        verifySignRequestBean.getTransId(), result);
    if (null != result) {
      if (String.valueOf(HttpStatus.SC_OK).equals(result.get(STATUS).toString())) {
        metrics.sr_incrSuccess();
      } else {
        String value = result.getString(STATUS).concat("-").concat(result.getString(MESSAGE));
        QPS qps = Metrics.QPS("第三方证书签名服务", "验签", value);
        qps.record();
      }
    }
    metrics.rt().sr_incrTotal();
    Assert.notNull(result, StatusConstants.PDF_SIGNATURE_VERIFY_ERROR, null, "PDF签章");
    return result;
  }

  /**
   * 产生时间戳请求
   *
   * @param genTsReqRequestBean 请求对象
   */
  public JSONObject genTsReq(GenTsReqRequestBean genTsReqRequestBean) {
    String url = httpConfig.tssAddress.concat(httpConfig.tssGenTsReq);
    JSONObject request = genTsReqRequestBean.toJsonObject();
    log.info("genTsReq address is : {} , transId is {}", url, genTsReqRequestBean.getTransId());
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("genTsReq response data : {}, transId is {}", result.toJSONString(),
        genTsReqRequestBean.getTransId());
    return result;
  }

  /**
   * 时间戳响应
   *
   * @param genTsRespRequestBean 请求对象
   */
  public JSONObject genTsResp(GenTsRespRequestBean genTsRespRequestBean) {
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "时间戳服务", "时间戳签名");
    String url = httpConfig.tssAddress.concat(httpConfig.tssGenTsResp);
    JSONObject request = genTsRespRequestBean.toJsonObject();
    log.info("genTsResp address is : {} , transId is {} , request parameter is {} ", url,
        genTsRespRequestBean.getTransId(), request);
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("genTsResp response data : {} , transId is {}", result,
        genTsRespRequestBean.getTransId());
    if (null != result) {
      if (String.valueOf(HttpStatus.SC_OK).equals(result.get(STATUS).toString())) {
        metrics.sr_incrSuccess();
      } else {
        String value = result.getString(STATUS).concat("-").concat(result.getString(MESSAGE));
        QPS qps = Metrics.QPS("第三方时间戳服务", "时间戳签名", value);
        qps.record();
      }
    }
    metrics.rt().sr_incrTotal();
    Assert.notNull(result, StatusConstants.TSS_SIGNATURE_ERROR, null, "PDF签章");
    return result;
  }

  /**
   * 解析时间戳
   *
   * @param parseTsRespRequestBean 请求对象
   */
  public JSONObject parseTsResp(ParseTsRespRequestBean parseTsRespRequestBean) {
    String url = httpConfig.tssAddress.concat("/parseTsResp");
    JSONObject request = parseTsRespRequestBean.toJsonObject();
    log.info("parseTsResp address is : {} , transId is {}",
        url, parseTsRespRequestBean.getTransId());
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("parseTsResp response data : {}, transId is {}",
        result.toJSONString(), parseTsRespRequestBean.getTransId());
    return result;
  }

  /**
   * 验证时间戳
   *
   * @param verifyTsRespRequestBean 请求对象
   */
  public JSONObject verifyTsResp(VerifyTsRespRequestBean verifyTsRespRequestBean) {
    String url = httpConfig.tssAddress.concat("/verifyTsResp");
    JSONObject request = verifyTsRespRequestBean.toJsonObject();
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("verifyTsResp transId is : {}, response data : {}",
        verifyTsRespRequestBean.getTransId(), result.toJSONString());
    return result;
  }

  /**
   * 证书验证接口
   *
   * @param verifyCertRequesrBean 证书的策略Id
   * @return JSONObject http response data
   */
  public JSONObject verifyCert(VerifyCertRequesrBean verifyCertRequesrBean) {
    MetricsClient metrics = MetricsClient.newInstance("第三方服务", "证书签名服务", "证书验证");
    String url = httpConfig.dsvsAddress.concat(httpConfig.dsvsCertValidate);
    JSONObject request = verifyCertRequesrBean.toJSONString();
    log.info("verifyCert transId is : {} , request data : {}",
        verifyCertRequesrBean.getTransId(), request.toJSONString());
    JSONObject result = HttpUtils.doPost(url, request);
    log.info("verifyCert transId is : {} , response data : {}",
        verifyCertRequesrBean.getTransId(), result);
    if (null != result) {
      if (String.valueOf(HttpStatus.SC_OK).equals(result.get(STATUS).toString())) {
        metrics.sr_incrSuccess();
      } else {
        String value = result.getString(STATUS).concat("-").concat(result.getString(MESSAGE));
        QPS qps = Metrics.QPS("第三方证书签名服务", "证书验证", value);
        qps.record();
      }
    }
    metrics.rt().sr_incrSuccess();
    Assert.notNull(result, StatusConstants.TSS_SIGNATURE_VERIFY_ERROR, null, "PDF签章");
    return result;
  }

}
