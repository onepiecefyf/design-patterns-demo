package cn.org.bjca.anysign.cloud.template.controller;

import cn.org.bjca.anysign.cloud.document.service.IDocxTemplateService;
import cn.org.bjca.anysign.cloud.template.message.TemplateReqMessage;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/***************************************************************************
 * <pre>基类Controller</pre>
 * @文件名称: BaseController
 * @包路径: cn.org.bjca.anysign.cloud.template.controller
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/10/27 15:09
 ***************************************************************************/
public abstract class BaseController {

  Logger logger = LoggerFactory.getLogger(this.getClass());

  @Autowired
  HttpConfig httpConfig;

  @Autowired
  SystemRequestService systemRequestService;

  @Autowired
  IDocxTemplateService iDocxTemplateService;

  void checkTemplateReqParams(TemplateReqMessage templateReqMessage) {
    // transId 不为空
    Assert.notNull(templateReqMessage.getTransId(),
        StatusConstants.TEMPLATE_PARAMETER_ERROR, "The transId is null.",
        "模板服务", "填充模板");
    // 传输类型
    Assert.state(judgeTransmissionFileType(templateReqMessage.getFileTransmissionType()),
        StatusConstants.TEMPLATE_PARAMETER_ERROR,
        String.format("The fileTransmissionType %s is wrong.",
            templateReqMessage.getFileTransmissionType()),
        "模板服务", "填充模板");
    // 请求报文内容类型只能为JSON 或 XML
    Assert.state(judgeDataStringType(templateReqMessage.getDataType()),
        StatusConstants.TEMPLATE_PARAMETER_ERROR,
        String.format("The dataType %s is wrong.", templateReqMessage.getDataType()),
        "模板服务", "填充模板");
    // 请求报文内容为空
    Assert.notNull(templateReqMessage.getDataString(),
        StatusConstants.TEMPLATE_PARAMETER_ERROR, "The dataString is null.",
        "模板服务", "填充模板");
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
   * <p>判断是否XML或JSON</p>
   *
   * @param reqFileType 数据类型
   */
  private boolean judgeDataStringType(String reqFileType) {
    String[] picNames = {DocType.XML.name(), DocType.JSON.name()};
    return Arrays.stream(picNames)
        .anyMatch(picName -> picName.equalsIgnoreCase(reqFileType));
  }
}
