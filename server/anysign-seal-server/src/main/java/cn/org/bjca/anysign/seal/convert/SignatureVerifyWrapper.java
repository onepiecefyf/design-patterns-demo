package cn.org.bjca.anysign.seal.convert;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.service.bean.enumpackage.FileTransmissionType;
import cn.org.bjca.anysign.seal.signature.bean.AcrossPage;
import cn.org.bjca.anysign.seal.verify.bean.SignatureVerifyRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description
 */
@Slf4j
public class SignatureVerifyWrapper {

  /**
   * 校验请求参数
   */
  public static void verification(SignatureVerifyRequest request) {
    Assert.notNull(request, StatusConstants.IMAGE_PARAMETER_NOTNULL);
    Assert.notNull(request.getTransId(), StatusConstants.TRANSID_NOTNULL);
    Assert.notNull(request.getFileTransmissionType(), StatusConstants.FILETRANSMISSIONTYPE_NOTNULL);
    if (FileTransmissionType.PATH.name()
        .equalsIgnoreCase(request.getFileTransmissionType().name())) {
      Assert.notNull(request.getRequestFileToken(), StatusConstants.FILE_TOKEN_NOTNUll);
      Assert.notNull(request.getRequestFileId(), StatusConstants.FILE_ID_NOTNUll);
    } else {
      Assert.notNull(request.getRequestFileContent(), StatusConstants.FILE_CONTENT_NOTNUll);
    }
    Assert.notNull(request.getAlg(), StatusConstants.ALG_ERROR);
  }

  public static void crossPageVerification(AcrossPage acrossPage) {
    Assert.notNull(acrossPage.getAcrossPageType(), StatusConstants.PARAMETER_ERROR, "骑缝章章签名类型不能为空");
    Assert
        .notNull(acrossPage.getAcrossPagePattern(), StatusConstants.PARAMETER_ERROR, "骑缝章模式不能为空。");
    if (0 > acrossPage.getStartWidth() || 100 <= acrossPage.getStartWidth()) {
      Assert.state(true, StatusConstants.PARAMETER_ERROR, "首页宽度为100内的整数");
    }
    if (acrossPage.getPosCoord() > 10000) {
      Assert.state(true, StatusConstants.PARAMETER_ERROR, "高度比例为10000内的整数");
    }
  }

}
