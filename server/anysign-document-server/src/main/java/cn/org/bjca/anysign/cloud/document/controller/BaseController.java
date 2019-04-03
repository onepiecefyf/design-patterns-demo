package cn.org.bjca.anysign.cloud.document.controller;

import static cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType.PATH;

import cn.org.bjca.anysign.cloud.document.message.DocumentReqMessage;
import cn.org.bjca.anysign.cloud.document.message.DocumentRespMessage;
import cn.org.bjca.anysign.cloud.document.service.IDocumentFormatService;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils;
import cn.org.bjca.anysign.seal.service.bean.UploadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import com.itextpdf.text.pdf.PdfReader;
import java.io.IOException;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/***************************************************************************
 * <pre>基类Controller</pre>
 * @文件名称: BaseController
 * @包路径: cn.org.bjca.anysign.seal.document.controller
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 18:10
 ***************************************************************************/
public abstract class BaseController {

  @Autowired
  protected IDocumentFormatService iDocumentFormatService;

  @Autowired
  protected SystemRequestService systemRequestService;

  @Autowired
  protected HttpConfig httpConfig;

  Logger logger = LoggerFactory.getLogger(this.getClass());

  /**
   * <p>判断请求/响应文件类型是否支持</p>
   *
   * @param fileType 请求文件类型
   * @return true | false
   */
  private boolean judgeFileType(String fileType) {
    return Arrays.stream(DocType.values())
        .map(Enum::name)
        .anyMatch(docTypeName -> docTypeName.equalsIgnoreCase(fileType));
  }

  /**
   * <p>检测传输文件类型是否枚举类型</p>
   *
   * @param transmissionFileType 传输文件类型
   * @return true | false
   */
  private boolean judgeTransmissionFileType(String transmissionFileType) {
    return Arrays.stream(FileTransmissionType.values())
        .map(Enum::name)
        .anyMatch(fileTypeName -> fileTypeName.equalsIgnoreCase(transmissionFileType));
  }

  /**
   * <p>判断是否图片类型</p>
   *
   * @param reqFileType 请求文件类型
   * @param respFileType 响应文件类型
   */
  boolean judgeImageFileType(String reqFileType, String respFileType) {
    // 图片类型数组
    String[] picNames = {DocType.JPG.name(), DocType.PNG.name(), DocType.GIF.name(),
        DocType.TIFF.name()};
    // 是否图片类型 （请求/响应文件类型）
    return Arrays.stream(picNames)
        .anyMatch(picName -> picName.equalsIgnoreCase(reqFileType) || picName
            .equalsIgnoreCase(respFileType));
  }

  /**
   * <p>检测基础请求消息参数</p>
   *
   * @param documentReqMessage 请求消息
   */
  void checkDocumentReqParams(DocumentReqMessage documentReqMessage) {
    Assert.notNull(documentReqMessage.getTransId(),
        StatusConstants.DOCUMENT_PARAMETER_ERROR, "The transId is null.", "文档格式化服务", "文档转换");
    Assert.notNull(documentReqMessage.getRequestFileType(),
        StatusConstants.DOCUMENT_PARAMETER_ERROR, "The requestFileType is null.", "文档格式化服务",
        "文档转换");
    Assert.state(judgeFileType(documentReqMessage.getRequestFileType()),
        StatusConstants.DOCUMENT_PARAMETER_ERROR,
        String.format("The requestFileType %s is wrong.", documentReqMessage.getRequestFileType()),
        "文档格式化服务", "文档转换");
    Assert.state(judgeFileType(documentReqMessage.getResponseFileType()),
        StatusConstants.DOCUMENT_PARAMETER_ERROR,
        String
            .format("The responseFileType %s is wrong.", documentReqMessage.getResponseFileType()),
        "文档格式化服务", "文档转换");
    Assert.state(judgeTransmissionFileType(documentReqMessage.getFileTransmissionType()),
        StatusConstants.DOCUMENT_PARAMETER_ERROR,
        String.format("The fileTransmissionType %s is wrong.",
            documentReqMessage.getFileTransmissionType()), "文档格式化服务", "文档转换");
  }

  /**
   * <p>检测PDF文档格式</p>
   *
   * @param pdfBty PDF文档
   * @return 参数是否正确(布尔值)
   */
  boolean checkPdfType(byte[] pdfBty) {
    PdfReader reader = null;
    try {
      reader = new PdfReader(pdfBty);
      reader.getNumberOfPages();
    } catch (IOException e) {
      logger.error("************ It's a wrong pdf format. " + e);
      return true;
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    return false;
  }

  /**
   * <p>构建响应消息</p>
   *
   * @param reqMessage 请求消息
   * @param respMessage 响应消息
   * @param resultPdfBty 结果文件Bty
   */
  void buildRespInfo(DocumentReqMessage reqMessage, DocumentRespMessage respMessage,
      byte[] resultPdfBty) {
    if (PATH.name().equalsIgnoreCase(reqMessage.getFileTransmissionType())) {
      UploadRequestBean uploadRequestBean =
          UploadRequestBean.build(httpConfig.appId, reqMessage.getAppId(), httpConfig.deviceId,
              httpConfig.version,
              reqMessage.getGroup(), reqMessage.getUser(), Integer.MAX_VALUE, httpConfig.secret,
              reqMessage.getTransId().concat(".").concat(reqMessage.getResponseFileType()),
              resultPdfBty);
      String resultFileId = systemRequestService.upload(uploadRequestBean);
      respMessage.setFileId(resultFileId);
    } else {
      respMessage.setFileContent(Base64Utils.byte2Base64StringFun(resultPdfBty));
    }
  }
}
