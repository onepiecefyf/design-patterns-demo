package cn.org.bjca.anysign.seal.convert;

import static cn.org.bjca.anysign.seal.convert.SignatureVerifyWrapper.crossPageVerification;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.image.bean.MoulageShape;
import cn.org.bjca.anysign.seal.image.bean.RidersBean;
import cn.org.bjca.anysign.seal.image.bean.SignImage;
import cn.org.bjca.anysign.seal.image.bean.SignImageMessage;
import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import cn.org.bjca.anysign.seal.moulage.defaultImpl.SealImage;
import cn.org.bjca.anysign.seal.server.common.message.ObjectRestResponse;
import cn.org.bjca.anysign.seal.signature.bean.Seal;
import cn.org.bjca.anysign.seal.signature.bean.SealPosition;
import cn.org.bjca.anysign.seal.signature.bean.SignatureRequest;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/***************************************************************************
 * <pre>印模对象转化类</pre>
 *
 * @author july_whj
 * @文件名称: MoulageConvertWrapper.class
 * @包 路   径：  cn.org.bjca.anysign.seal.wrapper
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/13 10:12
 ***************************************************************************/
@Slf4j
public class MoulageConvertWrapper {


  /**
   * 校验请求参数
   */
  public static void verification(SignImage request) {
    Assert.notNull(request, StatusConstants.IMAGE_PARAMETER_NOTNULL, null, "生成印章");
    Assert.notEntity(request.getTransId(), StatusConstants.TRANSID_NOTNULL, null, "生成印章");
    Assert.notNull(request.getFileTransmissionType(), StatusConstants.FILETRANSMISSIONTYPE_NOTNULL,
        null, "生成印章");
    Assert.notNull(request.getMoulage(), StatusConstants.IMAGE_PARAMETER_NOTNULL, null, "生成印章");
    Assert
        .notNull(request.getMoulage().getBorderWeight(), StatusConstants.BORDERWEIGHT_NOTNULL, null,
            "生成印章");
    Assert.notNull(request.getMoulage().getFileType(), StatusConstants.FILETYPE_NOTNULL, null,
        "生成印章");
    Assert.notNull(request.getMoulage().getSealWidth(), StatusConstants.SEALWIDTH_NOTNULL, null,
        "生成印章");
    Assert.notNull(request.getMoulage().getSealHeight(), StatusConstants.SEALHEIGHT_NOTNULL, null,
        "生成印章");
    Assert
        .notNull(request.getMoulage().getBorderWeight(), StatusConstants.BORDERWEIGHT_NOTNULL, null,
            "生成印章");
    Assert.notNull(request.getMoulage().getSealColor(), StatusConstants.SEALCOLOR_NOTNULL, null,
        "生成印章");
    Assert.notNull(request.getMoulage().getSealshape(), StatusConstants.SEALSHAPE_NOTNULL, null,
        "生成印章");
    Assert.notNull(request.getMoulage().getFontFamily(), StatusConstants.FONTFAMILY_NOTNULL, null,
        "生成印章");
    Assert.notNull(request.getMoulage().getRiders(), StatusConstants.RIDERS_NOTNULL, null, "生成印章");
    riderVerification(request.getMoulage().getRiders());
  }

  /**
   * 模板信息校验
   */
  public static void templeVerification(SignImage request) {
    Assert.notNull(request.getTemplate(), StatusConstants.PARAMETER_ERROR);
    Assert.notNull(request.getTemplate().getTempId(), StatusConstants.TEMPLATEID_NOTNULL);
  }

  /**
   * 文件校验
   */
  public static void fileVerification(String fileId, String fileToken) {
    Assert.notNull(fileId, StatusConstants.FILE_ID_NOTNUll);
    Assert.notNull(fileToken, StatusConstants.FILE_TOKEN_NOTNUll);
  }

  /**
   * 印模附文校验
   */
  public static void riderVerification(List<RidersBean> ridersBeens) {
    ridersBeens.forEach(ridersBean -> {
      Assert.notNull(ridersBean.getAppendixContent(), StatusConstants.APPENDIXCONTENT_NOTNULL, null,
          "生成印章");
      Assert.notNull(ridersBean.getRadian(), StatusConstants.RADIAN_NOTNULL, null, "生成印章");
      Assert.notNull(ridersBean.getFontWeight(), StatusConstants.FONTWEIGHT_NOTNULL, null, "生成印章");
      Assert.notNull(ridersBean.getFontSize(), StatusConstants.FONTSIZE_NOTNULL, null, "生成印章");
    });
  }

  /**
   * 对象转换
   */
  public static SealImageBean signImage2SealImageBean(SignImage request) {
    SealImageBean sealImageBean = new SealImageBean();
    sealImageBean.setAppId(request.getAppId());
    sealImageBean.setDeviceId(request.getDeviceId());
    sealImageBean.setVersion(request.getVersion());
    sealImageBean.setSignAlgo(request.getSignAlgo());
    sealImageBean.setSignature(request.getSignature());
    log.info("TransId is {}", request.getTransId());
    sealImageBean.setTransId(request.getTransId());
    ShapeEnum shapeEnum = shapeEnumChange(request.getMoulage().getSealshape());
    log.info("ShapeEnum is {}", shapeEnum.name());
    sealImageBean.setShape(shapeEnum);
    sealImageBean.setBorderColor(request.getMoulage().getSealColor());
    sealImageBean.setFontColor(request.getMoulage().getSealColor());
    sealImageBean.setBorderWeight((int) request.getMoulage().getBorderWeight());
    sealImageBean.setCanvasHeight(request.getMoulage().getSealHeight());
    sealImageBean.setCanvasWidth(request.getMoulage().getSealWidth());
    sealImageBean.setFontFamily(request.getMoulage().getFontFamily().name());
    // 后期优化，设置中心图标。Center -> bean处理
    sealImageBean.setStar(StringUtils.isNotBlank(request.getMoulage().getCenter()));
    List<Map<String, Object>> txtDescs = getTxtDescs(request);
    sealImageBean.setTxtDescs(txtDescs);
    log.info("sealImageBean is {}", sealImageBean.toString());
    return sealImageBean;
  }

  /**
   * 附文信息转换
   */
  private static List<Map<String, Object>> getTxtDescs(SignImage request) {
    List<Map<String, Object>> txtDescs = new LinkedList<>();
    for (RidersBean ridersBean : request.getMoulage().getRiders()) {
      Map<String, Object> topTxt = SealImage
          .buildText(ridersBean.getAppendixContent(), request.getMoulage().getSealColor(),
              (int) ridersBean.getFontSize(), request.getMoulage().getFontFamily().name(),
              changeFontWeight(ridersBean.getFontWeight()), ridersBean.getRadian());
      txtDescs.add(topTxt);
    }
    return txtDescs;
  }

  /**
   * 字体加粗转化
   */
  private static String changeFontWeight(Boolean weight) {
    return weight ? "bold" : "normal";
  }

  /**
   * 图形枚举转换
   */
  private static ShapeEnum shapeEnumChange(MoulageShape shape) {
    switch (shape) {
      case ELLIPSE:
        return ShapeEnum.ellipse;
      case CIRCLE:
        return ShapeEnum.circle;
      case RECTANGLE:
        return ShapeEnum.rectangle;
      case SQUARE:
        return ShapeEnum.square;
      default:
        throw new BaseRuntimeException(StatusConstants.SEALSHAPE_ERROR, null, "生成印章");
    }

  }


  /**
   * 返回结果校验
   */
  public static void verification(ObjectRestResponse<SignImageMessage> message) {
    Assert.notNull(message.getStatus(), StatusConstants.RESULTS_STATUS.getMessage());
    Assert.notNull(message.getMessage(), StatusConstants.RESULTS_MESSAGE.getMessage());
    if (StatusConstants.SUCCESS.getStatus().equals(message.getStatus())) {
      Assert.notNull(message.getData(), StatusConstants.IMAGE_ERROR.getMessage());
      if (StringUtils.isEmpty(message.getData().getFileId())
          && StringUtils.isEmpty(message.getData().getSealContent())) {
        throw new BaseRuntimeException(StatusConstants.IMAGE_ERROR, null, "生成印章");
      }
    }
  }

  /**
   * PDF签章校验
   *
   * @param request 请求参数
   */
  public static void pdfSignatureverification(SignatureRequest request) {
    Assert.notEntity(request.getTransId(), StatusConstants.PARAMETER_ERROR, null, "PDF签章");
    Assert.notNull(request.getFileTransmissionType(), StatusConstants.FILETYPE_NOTNULL, null,
        "PDF签章");
    Assert.notNull(request.getSealList(), StatusConstants.PARAMETER_ERROR, null, "PDF签章");
    sealVerification(request.getSealList());
  }

  /**
   * 签名实体校验
   *
   * @param seals 签名集合
   */
  private static void sealVerification(List<Seal> seals) {
    seals.forEach(seal -> {
      Assert.notNull(seal.getSignCert(), StatusConstants.SIGNCERT_NOTNULL, null, "PDF签章");
      Assert.notNull(seal.getProtectedMode(), StatusConstants.PROTECTEDMODE_NOTNULL, null, "PDF签章");
      Assert.notNull(seal.getSealPosition(), StatusConstants.SEALPOSITION_NOTNULL, null, "PDF签章");
      sealPositionVerification(seal.getSealPosition());
    });
  }

  /**
   * 签章定位参数校验
   *
   * @param sealPosition 定位实体
   */
  private static void sealPositionVerification(SealPosition sealPosition) {
    Assert.notNull(sealPosition.getPositionType(), StatusConstants.POSITIONTYPE_NOTNULL);
    switch (sealPosition.getPositionType()) {
      case KEYWORD:
        Assert.notNull(sealPosition.getKwRule(), StatusConstants.KWRULE_ERROR, null, "PDF签章");
        Assert.notNull(sealPosition.getKwRule().getKw(), StatusConstants.KW_ERROR, null, "PDF签章");
        break;
      case COORDINATE:
        Assert.notNull(sealPosition.getXyzRuleInfo(), StatusConstants.XYZRULEINFO_ERROR, null,
            "PDF签章");
        break;
      case CROSS_PAGE:
        Assert
            .notNull(sealPosition.getAcrossPage(), StatusConstants.ACROSSPAGE_ERROR, null, "PDF签章");
        crossPageVerification(sealPosition.getAcrossPage());
        break;
      default:
        throw new BaseRuntimeException(StatusConstants.POSITIONTYPE_ERROR, null, "PDF签章");
    }
  }


}