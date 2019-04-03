package cn.org.bjca.anysign.seal.service.impl;

import static cn.org.bjca.anysign.seal.global.tools.utils.Base64Utils.byte2Base64StringFun;

import cn.org.bjca.anysign.seal.moulage.ICreateImage;
import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.Template;
import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import cn.org.bjca.anysign.seal.moulage.convert.ITemplateService;
import cn.org.bjca.anysign.seal.moulage.defaultImpl.SealImageFactory;
import cn.org.bjca.anysign.seal.service.ISignImageService;
import cn.org.bjca.anysign.seal.service.bean.UploadRequestBean;
import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import cn.org.bjca.anysign.seal.service.httpconfig.HttpConfig;
import cn.org.bjca.anysign.seal.service.system.SystemRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/***************************************************************************
 * <pre>生成签章图片</pre>
 *
 * @author july_whj
 * @文件名称: SignImageServiceImpl.class
 * @包 路   径：  cn.org.bjca.anysign.seal.service.impl
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/12 18:57
 ***************************************************************************/
@Service
@Slf4j
public class SignImageServiceImpl implements ISignImageService {

  @Autowired
  private HttpConfig httpConfig;

  @Autowired
  private ITemplateService templateConvert;

  @Autowired
  private ICreateImage createImage;

  @Autowired
  private SystemRequestService systemRequestService;

  @Override
  public String createImageByPath(SealImageBean sealImageBean) {
    byte[] image = SealImageFactory.creatSealImage(sealImageBean);
    String message = createFile(image, sealImageBean);
    return message;
  }

  @Override
  public String createImageByContent(SealImageBean sealImageBean) {
    byte[] image = SealImageFactory.creatSealImage(sealImageBean);
    return byte2Base64StringFun(image);
  }

  @Override
  public String createImageTemplateByContent(Template template) {
    TemplateBean templateBean = templateConvert.readTemplateById(template);
    byte[] bytes = null;
    if (null != templateBean) {
      bytes = createImage.creatrImage(templateBean);
    }
    return byte2Base64StringFun(bytes);
  }

  @Override
  public String createImageTemplateByPath(Template template) {
    TemplateBean templateBean = templateConvert.readTemplateById(template);
    byte[] bytes = null;
    if (null != templateBean) {
      bytes = createImage.creatrImage(templateBean);
    }
    String message = createFile(bytes, template);
    return message;
  }

  /**
   * 生成文件并上传
   *
   * @param bytes 文件流
   * @param commonRequestParam 上传参数
   * @return file ID
   */
  private String createFile(byte[] bytes, CommonRequestParam commonRequestParam) {
    String message;
    String fileName = commonRequestParam.getTransId();
    UploadRequestBean uploadRequestBean = UploadRequestBean
        .build(httpConfig.appId, commonRequestParam.getAppId(),
            httpConfig.deviceId, httpConfig.version, null, null, Integer.MAX_VALUE,
            httpConfig.secret, fileName.concat(".png"),
            bytes);
    message = systemRequestService.upload(uploadRequestBean);
    return message;
  }


}