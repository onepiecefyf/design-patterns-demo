package cn.org.bjca.anysign.seal.image.bean;

import java.io.Serializable;

/***************************************************************************
 * <pre>附文bean</pre>
 *
 * @文件名称: RidersBean.class
 * @包 路   径：  cn.org.bjca.anysign.image.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V1.0
 * @创建人： july_whj
 * @创建时间：2018/9/7 14:20
 ***************************************************************************/
public class RidersBean implements Serializable {

  /**
   * 附文(*)
   */
  private String appendixContent;
  /**
   * 弧度(*)
   */
  private String radian;
  /**
   * 加粗(*)
   */
  private Boolean fontWeight;
  /**
   * 字体大小(*)
   */
  private float fontSize;

  public String getAppendixContent() {
    return appendixContent;
  }

  public void setAppendixContent(String appendixContent) {
    this.appendixContent = appendixContent;
  }

  public String getRadian() {
    return radian;
  }

  public void setRadian(String radian) {
    this.radian = radian;
  }

  public Boolean getFontWeight() {
    return fontWeight;
  }

  public void setFontWeight(Boolean fontWeight) {
    this.fontWeight = fontWeight;
  }

  public float getFontSize() {
    return fontSize;
  }

  public void setFontSize(float fontSize) {
    this.fontSize = fontSize;
  }
}