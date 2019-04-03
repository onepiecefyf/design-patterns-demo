package cn.org.bjca.anysign.seal.signature.bean;

/***************************************************************************
 * <pre>水印配置类</pre>
 *
 * @文件名称: WaterMarkBean.java
 * @包 路 径： cn.org.bjca.seal.esspdf.platform.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2015
 *
 * @类描述:
 * @版本: V2.0
 * @创建人： guzhixian
 * @创建时间：2015-8-27 下午4:35:05
 ***************************************************************************/
public class WaterMarkBean {

  private String content = null; // 内容，文本、图片(base64)

  private int positionX = 300; // X点坐标值

  private int positionY = 400; // Y点坐标值

  private int rotation = 0; // 旋转角度

  private int fontSize = 18; // 字体大小

  private int alignment = 1; // 对齐方式ALIGN_LEFT = 0;ALIGN_CENTER = 1;ALIGN_RIGHT = 2;

  private int floatingType = 1; // 浮动类型：置上1、置下2

  private String fontColor = "#808080"; // 颜色值，默认灰色Color.GRAY

  private int type = 1; // 水印类型：文字 =1、图片 =2

  private int indexNum = 0; // 客户端传递内容数组的索引

  private int percent = 100; // 图片百分比

  private String fillOpacity = "1.0";

  private float strokeOpacity = 1.0f;

  public int getFontSize() {
    return fontSize;
  }

  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }

  public int getPercent() {
    return percent;
  }

  public void setPercent(int percent) {
    this.percent = percent;
  }

  public float getStrokeOpacity() {
    return strokeOpacity;
  }

  public void setStrokeOpacity(float strokeOpacity) {
    this.strokeOpacity = strokeOpacity;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public int getPositionX() {
    return positionX;
  }

  public void setPositionX(int positionX) {
    this.positionX = positionX;
  }

  public int getPositionY() {
    return positionY;
  }

  public void setPositionY(int positionY) {
    this.positionY = positionY;
  }

  public int getRotation() {
    return rotation;
  }

  public void setRotation(int rotation) {
    this.rotation = rotation;
  }

  public int getAlignment() {
    return alignment;
  }

  public void setAlignment(int alignment) {
    this.alignment = alignment;
  }

  public String getFontColor() {
    return fontColor;
  }

  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }

  public int getIndexNum() {
    return indexNum;
  }

  public void setIndexNum(int indexNum) {
    this.indexNum = indexNum;
  }

  public int getFloatingType() {
    return floatingType;
  }

  public void setFloatingType(int floatingType) {
    this.floatingType = floatingType;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getFillOpacity() {
    return fillOpacity;
  }

  public void setFillOpacity(String fillOpacity) {
    this.fillOpacity = fillOpacity;
  }

}
