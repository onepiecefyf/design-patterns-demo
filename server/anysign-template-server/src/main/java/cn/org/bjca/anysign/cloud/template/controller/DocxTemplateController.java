package cn.org.bjca.anysign.cloud.template.controller;

import static java.util.stream.Collectors.toList;

import cn.org.bjca.anysign.cloud.document.bean.FillTemplateBean;
import cn.org.bjca.anysign.cloud.template.message.TemplateImgMessage;
import cn.org.bjca.anysign.cloud.template.message.TemplateReqMessage;
import cn.org.bjca.anysign.cloud.template.message.TemplateRespMessage;
import cn.org.bjca.anysign.cloud.template.util.JsonXmlConvertUtil;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import cn.org.bjca.anysign.seal.service.bean.DownloadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.UploadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.seal.esspdf.bean.ImageAppearanceBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <pre>DOCX模板服务</pre>
 * @文件名称: DocxTemplateController
 * @包路径: cn.org.bjca.anysign.seal.template.controller
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/12 11:11
 ***************************************************************************/
@RestController
@Api(value = "模板相关接口", consumes = MediaType.APPLICATION_JSON_VALUE, protocols = "HTTP", description = "模板相关接口")
public class DocxTemplateController extends BaseController {

  /**
   * <p>模板生成填充</p>
   *
   * @param templateReqMessage 请求数据
   * @return 填充结果
   */
  @PostMapping(value = "template/v1/docGeneration", produces = "application/json;charset=UTF-8")
  @ApiOperation(value = "填充模板", produces = MediaType.APPLICATION_JSON_VALUE, httpMethod = "POST", notes = "根据数据报文填充模板")
  public ObjectRestResponse<TemplateRespMessage> docGeneration(
      @RequestBody TemplateReqMessage templateReqMessage) {
    // 模板服务响应消息
    TemplateRespMessage respMessage = new TemplateRespMessage();

    // 检测请求参数
    checkTemplateReqParams(templateReqMessage);

    respMessage.setTransId(templateReqMessage.getTransId());
    /* 获取模板文件*/
    // 模板文件
    byte[] templateBty;
    // 文件传输类型
    String fileTransmissionType = templateReqMessage.getFileTransmissionType();
    if (FileTransmissionType.PATH.name().equalsIgnoreCase(fileTransmissionType)) {
      String reqFileId = templateReqMessage.getRequestFileId();
      String reqFileToken = templateReqMessage.getRequestFileToken();
      logger.info("************* The transId is [{}], reqFileId is [{}] and reqFileToken is [{}].",
          templateReqMessage.getTransId(), reqFileId, reqFileToken);
      DownloadRequestBean downloadRequestBean = DownloadRequestBean.build(
          httpConfig.appId, httpConfig.deviceId, httpConfig.version, httpConfig.secret, reqFileId,
          reqFileToken, templateReqMessage.getTransId());
      templateBty = systemRequestService.download(downloadRequestBean);
    } else {
      // 文件base64内容
      String reqFileContent = templateReqMessage.getRequestFileContent();
      // Base64解码获取模板文件
      templateBty = Base64Utils.base64String2ByteFun(reqFileContent);
    }
    Assert.notNull(templateBty, StatusConstants.TEMPLATE_FILE_NOTNULL, "模板服务", "填充模板");

    /* 获取请求报文信息*/
    // 请求数据类型
    String dataType = templateReqMessage.getDataType();
    // 请求数据内容
    String dataString = templateReqMessage.getDataString();
    // 请求数据内容base解码
    dataString = new String(Base64Utils.base64String2ByteFun(dataString), StandardCharsets.UTF_8);
    if (DocType.JSON.name().equalsIgnoreCase(dataType)) { // JSON格式，需转换成XML格式
      dataString = JsonXmlConvertUtil.json2Xml(dataString);
    }
    // 报文内容
    byte[] dataBty = dataString.getBytes(StandardCharsets.UTF_8);

    /* 模板插入图片收集*/
    List<TemplateImgMessage> templateImgList = templateReqMessage.getTemplateImgList();
    // 图片显示Bean列表
    List<ImageAppearanceBean> imageApprs = null;
    if (templateImgList != null && !templateImgList.isEmpty()) {
      // 构建图片显示Bean列表
      imageApprs = templateImgList.stream().map(tempImgMess -> {
        ImageAppearanceBean imageAppearanceBean = new ImageAppearanceBean();
        // 模板书签名称（定位）
        imageAppearanceBean.setMarkName(tempImgMess.getMarkName());
        // 插入图片内容
        imageAppearanceBean
            .setImageBty(Base64Utils.base64String2ByteFun(tempImgMess.getBase64Img()));
        // 图片显示宽度
        imageAppearanceBean
            .setWidth(tempImgMess.getDisplayWidth() != 0f ? tempImgMess.getDisplayWidth() : 400f);
        // 图片显示高度
        imageAppearanceBean.setHeight(
            tempImgMess.getDisplayHeight() != 0f ? tempImgMess.getDisplayHeight() : 400f);
        return imageAppearanceBean;
      }).collect(toList());
    }

    /* 模版数据填充*/
    // 填充数据
    FillTemplateBean bean = new FillTemplateBean();
    bean.setDocxBty(templateBty);
    bean.setXmlBty(dataBty);
    bean.setImageAppearanceBeanList(imageApprs);
    byte[] mergeDocx = iDocxTemplateService.docxTemplateFill(bean);
    Assert.notNull(mergeDocx, StatusConstants.TEMPLATE_XML_FILLERROR, "模板服务", "填充模板");

    if (FileTransmissionType.PATH.name().equalsIgnoreCase(fileTransmissionType)) {
      UploadRequestBean uploadRequestBean = UploadRequestBean.build(
          httpConfig.appId, templateReqMessage.getAppId(), httpConfig.deviceId, httpConfig.version,
          templateReqMessage.getGroup(), templateReqMessage.getUser(), Integer.MAX_VALUE,
          httpConfig.secret,
          templateReqMessage.getTransId().concat(".docx"), mergeDocx);
      String resultFileId = systemRequestService.upload(uploadRequestBean);
      respMessage.setFileId(resultFileId);
    } else {
      respMessage.setFileContent(Base64Utils.byte2Base64StringFun(mergeDocx));
    }
    return new ObjectRestResponse<>(respMessage);
  }

  /**
   * <p>检测请求消息参数</p>
   *
   * @param templateReqMessage 模板请求消息
   */
  @Override
  void checkTemplateReqParams(TemplateReqMessage templateReqMessage) {
    if (FileTransmissionType.PATH.name()
        .equalsIgnoreCase(templateReqMessage.getFileTransmissionType())) {
      // 文件传输类型为PATH，则fileId不能为空
      Assert.notNull(templateReqMessage.getRequestFileId(),
          StatusConstants.TEMPLATE_PARAMETER_ERROR, "The requestFileId is null.",
          "模板服务", "填充模板");
    } else {
      // 文件传输类型为PATH，则fileContent不能为空
      Assert.notNull(templateReqMessage.getRequestFileContent(),
          StatusConstants.TEMPLATE_PARAMETER_ERROR, "The requestFileContent is null.",
          "模板服务", "填充模板");
    }
  }

}
