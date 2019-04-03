package cn.org.bjca.anysign.seal.moulage.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import com.alibaba.fastjson.JSON;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***************************************************************************
 * <pre>印模生成bean</pre>
 *
 * @author july_whj
 * @文件名称: SealImageBean
 * @包 路   径：  cn.org.bjca.anysign.commponents.seal.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/5/18 16:17
 ***************************************************************************/
public class SealImageBean extends CommonRequestParam implements Serializable {

  /**
   * 形状
   */
  private ShapeEnum shape;

  /**
   * 字体
   */
  private String fontFamily;

  /**
   * 章高度
   */
  private float canvasHeight = 0;

  /**
   * 章宽度
   */
  private float canvasWidth = 0;

  /**
   * 边框颜色
   */
  private String borderColor;

  /**
   * 边框粗度
   */
  private int borderWeight = 0;

  /**
   * 颜色
   */
  private String fontColor;

  /**
   * 字体粗度
   */
  private String fontWeight;

  /**
   * 字体大小
   */
  private int fontSize = 0;

  /**
   * 是否需要五角星
   */
  private boolean isStar;

  /**
   * 模板编号
   */
  private String templateNum;

  /**
   * 印象文本内容
   */
  private List<Map<String, Object>> txtDescs = new LinkedList<>();


  public ShapeEnum getShape() {
    return shape;
  }

  public void setShape(ShapeEnum shape) {
    this.shape = shape;
  }

  public String getFontFamily() {
    return fontFamily;
  }

  public void setFontFamily(String fontFamily) {
    this.fontFamily = fontFamily;
  }

  public float getCanvasHeight() {
    return canvasHeight;
  }

  public void setCanvasHeight(float canvasHeight) {
    this.canvasHeight = canvasHeight;
  }

  public float getCanvasWidth() {
    return canvasWidth;
  }

  public void setCanvasWidth(float canvasWidth) {
    this.canvasWidth = canvasWidth;
  }

  public String getBorderColor() {
    return borderColor;
  }

  public void setBorderColor(String borderColor) {
    this.borderColor = borderColor;
  }

  public int getBorderWeight() {
    return borderWeight;
  }

  public void setBorderWeight(int borderWeight) {
    this.borderWeight = borderWeight;
  }

  public String getFontColor() {
    return fontColor;
  }

  public void setFontColor(String fontColor) {
    this.fontColor = fontColor;
  }

  public String getFontWeight() {
    return fontWeight;
  }

  public void setFontWeight(String fontWeight) {
    this.fontWeight = fontWeight;
  }

  public int getFontSize() {
    return fontSize;
  }

  public void setFontSize(int fontSize) {
    this.fontSize = fontSize;
  }


  public boolean isStar() {
    return isStar;
  }

  public void setStar(boolean star) {
    isStar = star;
  }

  public List<Map<String, Object>> getTxtDescs() {
    return txtDescs;
  }

  public void setTxtDescs(List<Map<String, Object>> txtDescs) {
    this.txtDescs = txtDescs;
  }

  public String getTemplateNum() {
    return templateNum;
  }

  public void setTemplateNum(String templateNum) {
    this.templateNum = templateNum;
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }
}