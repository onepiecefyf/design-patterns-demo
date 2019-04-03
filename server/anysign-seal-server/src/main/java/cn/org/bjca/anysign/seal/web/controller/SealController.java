package cn.org.bjca.anysign.seal.web.controller;

import static cn.org.bjca.anysign.seal.convert.MoulageConvertWrapper.pdfSignatureverification;

import cn.org.bjca.anysign.seal.convert.MoulageConvertWrapper;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.image.bean.SignImage;
import cn.org.bjca.anysign.seal.image.bean.SignImageMessage;
import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.Template;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import cn.org.bjca.anysign.seal.service.ISealService;
import cn.org.bjca.anysign.seal.service.ISignImageService;
import cn.org.bjca.anysign.seal.service.bean.DownloadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.UploadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import cn.org.bjca.anysign.seal.signature.bean.DigestRequest;
import cn.org.bjca.anysign.seal.signature.bean.MergingRequest;
import cn.org.bjca.anysign.seal.signature.bean.PdfDigest;
import cn.org.bjca.anysign.seal.signature.bean.SignatureRequest;
import cn.org.bjca.anysign.seal.signature.bean.SignatureResponse;
import cn.org.bjca.anysign.seal.verify.bean.SignatureVerifyRequest;
import cn.org.bjca.anysign.seal.verify.bean.SignatureVerifyResponse;
import cn.org.bjca.seal.esspdf.itext.bean.VerifyMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zjgao
 * @create 2018/8/31.
 * @description
 */
@Api(value = "签章相关接口", consumes = MediaType.APPLICATION_JSON_VALUE, protocols = "HTTP", description = "签章相关接口")
@RestController
@RequestMapping("seal/v1")
@Slf4j
public class SealController {

  @Autowired
  private ISignImageService signImageService;
  @Autowired
  private ISealService sealService;
  @Autowired
  private SystemRequestService systemRequestService;
  @Autowired
  private HttpConfig httpConfig;

  @RequestMapping(value = "/sealGeneration", consumes = "application/json;charset=utf-8")
  @ApiOperation(value = "生成印章", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "根据印章规格信息生成印章")
  @ApiImplicitParam(name = "request", value = "印模实体bean详细信息", required = true, dataType = "SignImage")
  public ObjectRestResponse<SignImageMessage> sealGeneration(@RequestBody SignImage request) {
    if (null != request.getMoulage()) {
      // 默认处理方式
      log.info("sealGeneration in Default");
      return sealGenerationDefault(request);
    } else if (null != request.getTemplate()) {
      // 模板处理方式
      log.info("sealGeneration in Template");
      return sealGenerationTemplate(request);
    } else {
      log.error("sealGeneration fail ! Moulage or Template is null");
      throw new BaseRuntimeException(StatusConstants.IMAGE_PARAMETER_NOTNULL, null, "生成印章");
    }
  }

  /**
   * 默认实现
   *
   * @param request SignImage 印模实体类
   */
  private ObjectRestResponse<SignImageMessage> sealGenerationDefault(SignImage request) {
    MoulageConvertWrapper.verification(request);
    SealImageBean sealImageBean = MoulageConvertWrapper.signImage2SealImageBean(request);
    SignImageMessage imageMessage = new SignImageMessage();
    switch (request.getFileTransmissionType()) {
      case PATH:
        String message = signImageService.createImageByPath(sealImageBean);
        getFileIdParsingDate(imageMessage, message);
        break;
      case CONTENT:
        String content = signImageService.createImageByContent(sealImageBean);
        imageMessage.setSealContent(content);
        break;
      default:
        log.error("文件传输类型不正确");
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "生成印章");
    }
    imageMessage.setTransId(request.getTransId());
    ObjectRestResponse<SignImageMessage> restResponse = new ObjectRestResponse<>(imageMessage);
    MoulageConvertWrapper.verification(restResponse);
    return restResponse;
  }

  /**
   * 模板实现
   *
   * @param request SignImage
   */
  private ObjectRestResponse<SignImageMessage> sealGenerationTemplate(SignImage request) {
    MoulageConvertWrapper.templeVerification(request);
    Assert.notEntity(request.getTransId(), StatusConstants.TRANSID_NOTNULL, null, "生成印章");
    SignImageMessage imageMessage = new SignImageMessage();
    switch (request.getFileTransmissionType()) {
      case PATH:
        Template template = request.getTemplate();
        template.setAppId(request.getAppId());
        template.setTransId(request.getTransId());
        template.setSignature(request.getSignature());
        template.setDeviceId(request.getDeviceId());
        template.setVersion(request.getVersion());
        template.setSignAlgo(request.getSignAlgo());
        String message = signImageService.createImageTemplateByPath(template);
        getFileIdParsingDate(imageMessage, message);
        break;
      case CONTENT:
        String content = signImageService.createImageTemplateByContent(request.getTemplate());
        imageMessage.setSealContent(content);
        break;
      default:
        log.error("文件传输类型不正确");
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "生成印章");
    }
    imageMessage.setTransId(request.getTransId());
    ObjectRestResponse<SignImageMessage> restResponse = new ObjectRestResponse<>(imageMessage);
    MoulageConvertWrapper.verification(restResponse);
    return restResponse;
  }

  /**
   * 解析文件上传ID
   *
   * @param imageMessage 返回信息
   * @param message 文件 http 返回值
   * @return fileId
   */
  private void getFileIdParsingDate(SignImageMessage imageMessage, String message) {
    if (!StringUtils.isEmpty(message)) {
      imageMessage.setFileId(message);
    } else {
      log.error("file upload error ! message is {}", message);
      throw new BaseRuntimeException(StatusConstants.FILE_UPLOAD_ERROR, null, "生成印章");
    }
  }


  @RequestMapping(value = "/sealVerify", consumes = "application/json;charset=utf-8")
  @ApiOperation(value = "验证签章", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "pdf文件验证签章")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "transId", value = "交易参考号，传递64位的随机数，全局唯一", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "fileTransmissionType", value = "文件传输类型", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileToken", value = "请求文件下载token", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileId", value = "请求文件id", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileContent", value = "文件base64编码", required = false, paramType = "query", dataType = "string")
  })
  public ObjectRestResponse<SignatureVerifyResponse> pdfVerify(
      @RequestBody SignatureVerifyRequest request) {
    Assert.notEntity(request.getTransId(), StatusConstants.TRANSID_NOTNULL, null, "验证签章");
    byte[] pdfBty = null;
    switch (request.getFileTransmissionType()) {
      case PATH:
        MoulageConvertWrapper
            .fileVerification(request.getRequestFileId(), request.getRequestFileToken());
        DownloadRequestBean downloadRequestBean = DownloadRequestBean
            .build(httpConfig.appId, httpConfig.deviceId,
                httpConfig.version, httpConfig.secret, request.getRequestFileId(),
                request.getRequestFileToken(), request.getTransId());
        pdfBty = systemRequestService.download(downloadRequestBean);
        break;
      case CONTENT:
        pdfBty = Base64Utils.base64String2ByteFun(request.getRequestFileContent());
        break;
      default:
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "验证签章");
    }
    Assert.notNullBytes(pdfBty, StatusConstants.FILE_CONTENT_NOTNUll, null, "验证签章");
    List<VerifyMessage> verifyMessages = sealService
        .sealVerify(request.getAppId(), request.getTransId(), pdfBty, request.getAlg());
    SignatureVerifyResponse signatureVerifyResponse = new SignatureVerifyResponse();
    signatureVerifyResponse.setTransId(request.getTransId());
    signatureVerifyResponse.setVerifyMessages(verifyMessages);
    ObjectRestResponse<SignatureVerifyResponse> response = new ObjectRestResponse<>(
        signatureVerifyResponse);
    return response;
  }


  @RequestMapping(value = "/pdfSignature", consumes = "application/json;charset=utf-8")
  @ApiOperation(value = "pdf签章", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "pdf文件签章")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "transId", value = "交易参考号，传递64位的随机数，全局唯一", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "fileTransmissionType", value = "文件传输类型", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileToken", value = "请求文件下载token", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileId", value = "请求文件id", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileContent", value = "文件base64编码", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "group", value = "文件归属组", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "user", value = "文件归属用户", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "expire", value = "文件有效期", required = false, paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "sealList", value = "签章列表", required = false, paramType = "query", dataType = "string")
  })
  public ObjectRestResponse<SignatureResponse> pdfSignature(@RequestBody SignatureRequest request) {
    pdfSignatureverification(request);
    byte[] pdfBty = getPdfBty(request.getFileTransmissionType(), request.getRequestFileId(),
        request.getRequestFileToken(), request.getRequestFileContent(), request.getTransId());
    Assert.notNullBytes(pdfBty, StatusConstants.FILE_CONTENT_NOTNUll, null, "PDF签章");
    byte[] signedBty = sealService
        .pdfSignature(pdfBty, request.getAppId(), request.getDeviceId(), request.getSignature(),
            request.getTransId(), request.getFileTransmissionType(), request.getSealList());
    SignatureResponse signatureResponse = new SignatureResponse();
    signatureResponse.setTransId(request.getTransId());
    switch (request.getFileTransmissionType()) {
      case PATH:
        UploadRequestBean uploadRequestBean = UploadRequestBean
            .build(httpConfig.appId, request.getAppId(), httpConfig.deviceId,
                httpConfig.version, null, null, Integer.MAX_VALUE, httpConfig.secret,
                request.getTransId().concat(".pdf"), signedBty);
        uploadRequestBean.setTransId(request.getTransId());
        String fileId = systemRequestService.upload(uploadRequestBean);
        signatureResponse.setFileId(fileId);
        break;
      case CONTENT:
        signatureResponse.setFileContent(Base64Utils.byte2Base64StringFun(signedBty));
        break;
      default:
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "pdf签章");
    }
    return new ObjectRestResponse<>(signatureResponse);
  }

  @RequestMapping(value = "/pdfDigest", consumes = "application/json;charset=utf-8")
  @ApiOperation(value = "pdf生成摘要", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "pdf文件签章")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "transId", value = "交易参考号，传递64位的随机数，全局唯一", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "fileTransmissionType", value = "文件传输类型", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileToken", value = "请求文件下载token", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileId", value = "请求文件id", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileContent", value = "文件base64编码", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "group", value = "文件归属组", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "user", value = "文件归属用户", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "expire", value = "文件有效期", required = false, paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "seal", value = "签章信息", required = false, paramType = "query", dataType = "string")
  })
  public ObjectRestResponse<SignatureResponse> pdfDigest(@RequestBody DigestRequest request) {
    Assert.notEntity(request.getTransId(), StatusConstants.TRANSID_NOTNULL, null, "pdf生成摘要");
    String appId = request.getAppId();
    String transId = request.getTransId();
    byte[] pdfBty = getPdfBty(request.getFileTransmissionType(), request.getRequestFileId(),
        request.getRequestFileToken(), request.getRequestFileContent(), request.getTransId());
    Assert.notNullBytes(pdfBty, StatusConstants.FILE_CONTENT_NOTNUll, null, "pdf生成摘要");
    PdfDigest pdfDigest = sealService.pdfDigest(appId, transId, pdfBty,
        request.getFileTransmissionType(), request.getSeal().getProtectedMode(), request.getSeal());
    Assert.notNull(pdfDigest, StatusConstants.FAIL, null, "pdf生成摘要");
    SignatureResponse signatureResponse = new SignatureResponse();
    switch (request.getFileTransmissionType()) {
      case PATH:
        UploadRequestBean uploadRequestBean = UploadRequestBean
            .build(httpConfig.appId, request.getAppId(), httpConfig.deviceId,
                httpConfig.version, null, null, Integer.MAX_VALUE, httpConfig.secret,
                request.getTransId().concat(".pdf"), pdfDigest.getPdfBty());
        String fileId = systemRequestService.upload(uploadRequestBean);
        signatureResponse.setFileId(fileId);
        break;
      case CONTENT:
        signatureResponse.setFileContent(Base64Utils.byte2Base64StringFun(pdfDigest.getPdfBty()));
        break;
      default:
        log.error("文件传输类型不正确");
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "pdf生成摘要");
    }
    signatureResponse.setTransId(transId);
    signatureResponse.setSignValue(Base64Utils.byte2Base64StringFun(pdfDigest.getHash()));
    return new ObjectRestResponse<>(signatureResponse);
  }

  @RequestMapping(value = "/pdfMerging", consumes = "application/json;charset=utf-8")
  @ApiOperation(value = "pdf合成", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "pdf文件签章")
  @ApiImplicitParams(value = {
      @ApiImplicitParam(name = "transId", value = "交易参考号，传递64位的随机数，全局唯一", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "fileTransmissionType", value = "文件传输类型", required = true, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileToken", value = "请求文件下载token", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileId", value = "请求文件id", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "requestFileContent", value = "文件base64编码", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "group", value = "文件归属组", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "user", value = "文件归属用户", required = false, paramType = "query", dataType = "string"),
      @ApiImplicitParam(name = "expire", value = "文件有效期", required = false, paramType = "query", dataType = "int"),
      @ApiImplicitParam(name = "signData", value = "签名数据", required = false, paramType = "query", dataType = "string")
  })
  public ObjectRestResponse<SignatureResponse> pdfMerging(@RequestBody MergingRequest request) {
    byte[] pdfBty = getPdfBty(request.getFileTransmissionType(), request.getRequestFileId(),
        request.getRequestFileToken(), request.getRequestFileContent(), request.getTransId());
    Assert.notNullBytes(pdfBty, StatusConstants.FILE_CONTENT_NOTNUll, null, "pdf合章");
    byte[] AllpdfByt = sealService
        .sealMerging(Base64Utils.base64String2ByteFun(request.getSignData()), pdfBty);
    SignatureResponse signatureResponse = new SignatureResponse();
    switch (request.getFileTransmissionType()) {
      case PATH:
        UploadRequestBean uploadRequestBean = UploadRequestBean
            .build(httpConfig.appId, request.getAppId(), httpConfig.deviceId,
                httpConfig.version, null, null, Integer.MAX_VALUE, httpConfig.secret,
                request.getTransId().concat(".pdf"), AllpdfByt);
        String fileId = systemRequestService.upload(uploadRequestBean);
        signatureResponse.setFileId(fileId);
        break;
      case CONTENT:
        signatureResponse.setFileContent(Base64Utils.byte2Base64StringFun(AllpdfByt));
        break;
      default:
        log.error("文件传输类型不正确");
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "pdf合章");
    }
    signatureResponse.setTransId(request.getTransId());
    return new ObjectRestResponse<>(signatureResponse);
  }


  /**
   * 获取文件
   *
   * @param fileTransmissionType 文件传输类型
   * @param fileId 文件ID
   * @param fileToken 文件token
   * @param fileContent 文件base64
   * @return 文件流
   */
  private byte[] getPdfBty(FileTransmissionType fileTransmissionType, String fileId,
      String fileToken, String fileContent, String transId) {
    byte[] pdfBty;
    switch (fileTransmissionType) {
      case PATH:
        MoulageConvertWrapper.fileVerification(fileId, fileToken);
        DownloadRequestBean downloadRequestBean = DownloadRequestBean
            .build(httpConfig.appId, httpConfig.deviceId,
                httpConfig.version, httpConfig.secret, fileId, fileToken, transId);
        pdfBty = systemRequestService.download(downloadRequestBean);
        break;
      case CONTENT:
        pdfBty = Base64Utils.base64String2ByteFun(fileContent);
        break;
      default:
        throw new BaseRuntimeException(StatusConstants.FILETRANSMISSIONTYPE_FAIL, null, "PDF签章");
    }
    return pdfBty;
  }

}
