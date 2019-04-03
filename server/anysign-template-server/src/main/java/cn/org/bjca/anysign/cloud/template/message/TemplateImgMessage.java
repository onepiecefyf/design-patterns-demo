package cn.org.bjca.anysign.cloud.template.message;

/***************************************************************************
 * <pre>模板动态图片信息</pre>
 * @文件名称: TemplateImgMessage
 * @包路径: cn.org.bjca.anysign.cloud.template.message
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/11/8 17:25
 ***************************************************************************/
public class TemplateImgMessage {

  /**
   * 模板书签名称
   */
  private String markName;

  /**
   * 插入图片base64编码内容
   */
  private String base64Img;

  /**
   * 图片显示宽度
   */
  private float displayWidth;

  /**
   * 图片显示高度
   */
  private float displayHeight;

  public String getMarkName() {
    return markName;
  }

  public void setMarkName(String markName) {
    this.markName = markName;
  }

  public String getBase64Img() {
    return base64Img;
  }

  public void setBase64Img(String base64Img) {
    this.base64Img = base64Img;
  }

  public float getDisplayWidth() {
    return displayWidth;
  }

  public void setDisplayWidth(float displayWidth) {
    this.displayWidth = displayWidth;
  }

  public float getDisplayHeight() {
    return displayHeight;
  }

  public void setDisplayHeight(float displayHeight) {
    this.displayHeight = displayHeight;
  }
}
