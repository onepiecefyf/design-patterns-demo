package cn.org.bjca.anysign.seal.moulage.template.impl;

import cn.org.bjca.anysign.seal.moulage.tools.PngDPIProcessor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/***************************************************************************
 * <pre>圆形印模生成工具</pre>
 *
 * @author july_whj
 * @文件名称: CircleImpression.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/27 16:04
 ***************************************************************************/
public class CircleImpression {

  /**
   * 默认dpi
   */
  private static final int DPI = 600;
  /**
   * 默认圆章尺寸40mm
   */
  private static final int R = 945;
  /**
   * 默认字体
   */
  private static final String FONTFIM = "ChangFangSong";
  /**
   * 默认颜色 红色
   */
  private static final Color DEFCOLOR = Color.RED;
  /**
   * 默认边框厚度 1 mm
   */
  private static final int THICK = 28;
  /**
   * 默认字体 加粗
   */
  private static final int FONTWITH = Font.BOLD;

  /**
   * 创建圆形公章
   *
   * @param head 上弦文
   * @param foot 中附文
   * @param value 下弦文
   * @param start 是否星星
   */
  public static byte[] createCircleImpression(String head, String foot, String value,
      boolean start) {
    return createCircleImpression(head, foot, value, R, R, THICK, R / 10, R / 10, 50, FONTFIM,
        FONTFIM, FONTFIM
        , FONTWITH, FONTWITH, FONTWITH, start, R / 3);
  }

  /**
   * 兼容接口
   *
   * @param strings 附文数组
   * @return bytes
   */
  public static byte[] createCircleImpression(String[] strings) {
    return createCircleImpression(strings[0], strings[1], strings[2], R, R, THICK, R / 10, R / 10,
        50, FONTFIM, FONTFIM, FONTFIM
        , FONTWITH, FONTWITH, FONTWITH, Boolean.valueOf(strings[3]), R / 3);
  }


  /**
   * 创建圆形公章
   *
   * @param head 上弦文
   * @param foot 中附文
   * @param value 下弦文
   * @param canvasWidth 长
   * @param canvasHeight 宽
   * @param thick 厚度
   * @param headSize 上弦文字体大小
   * @param footSize 中附文字体大小
   * @param valueSize 下弦文字体大小
   * @param headFont 上弦文字体
   * @param footFont 中附文字体
   * @param valueFont 下弦文字体
   * @param headFontBOLD 上弦文加粗
   * @param footBOLD 中附文加粗
   * @param valueBOLD 下弦文加粗
   * @param stars 是否有星星
   * @param starsSize 星星大小
   * @return image bytes
   */
  public static byte[] createCircleImpression(String head, String foot, String value,
      int canvasWidth, int canvasHeight, int thick,
      int headSize, int footSize, int valueSize,
      String headFont, String footFont, String valueFont,
      int headFontBOLD, int footBOLD, int valueBOLD,
      boolean stars, int starsSize) {
    BufferedImage bi = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = bi.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    /***************画笔**************/
    int circleRadius = Math.min(canvasWidth, canvasHeight) / 2 - thick / 2;
    int centerX = canvasWidth / 2;
    int centerY = canvasHeight / 2;
    /***************画圆**************/
    g2.setPaint(Color.RED);
    g2.setStroke(new BasicStroke(thick));
    Ellipse2D circle = new Ellipse2D.Double();
    circle.setFrameFromCenter(centerX, centerY, centerX + circleRadius, centerY + circleRadius);
    g2.draw(circle);
    /***************设置样式**************/
    //设置字体样式
    Font f = new Font(footFont, footBOLD, footSize);
    //获取font呈现的上下文
    FontRenderContext context;
    g2.setFont(f);
    f = new Font(footFont, footBOLD, footSize);
    context = g2.getFontRenderContext();
    Rectangle2D bounds = f.getStringBounds(foot, context);
    g2.setFont(f);
    g2.drawString(foot, (float) (circleRadius - bounds.getCenterX() + thick),
        (float) (circleRadius * 1.6 - bounds.getCenterY()));
    /***************center**************/
    if (stars) {
      Font starFont = new Font("宋体", Font.BOLD, starsSize);
      g2.setFont(starFont);
      FontMetrics fm = g2.getFontMetrics(starFont);
      int textWidth = fm.stringWidth("★");
      int textHeight = fm.getAscent();
      g2.drawString("★", canvasWidth / 2 - textWidth / 2,
          (canvasHeight - textHeight) / 2 + textHeight - THICK);
    }
    /***************head**************/
    f = new Font(headFont, headFontBOLD, headSize);
    context = g2.getFontRenderContext();
    bounds = f.getStringBounds(head, context);
    double msgWidth = bounds.getWidth();
    int countOfMsg = head.length();
    //计算间距
    double interval;
    if ((canvasWidth + canvasHeight) <= 400) {
      interval = msgWidth / (countOfMsg + 1);
    } else if ((canvasWidth + canvasHeight) >= 800) {
      if (countOfMsg >= 17) {
        interval = msgWidth / (countOfMsg + 3);
      } else {
        interval = msgWidth / (countOfMsg - 2);
      }
    } else {
      interval = msgWidth / (countOfMsg - 1);
    }
    //bounds.getY()是负数，这样可以将弧形文字固定在圆内了。-5目的是离圆环稍远一点
    double newRadius = circleRadius + bounds.getY() - thick;
    //每个间距对应的角度
    double radianPerInterval = 2 * Math.asin(interval / (2 * newRadius));
    //第一个元素的角度
    double firstAngle;
    //每个间距对应的角度
    if (countOfMsg % 2 == 1) {
      firstAngle = (countOfMsg - 1) * radianPerInterval / 2.0 + Math.PI / 2 + 0.1;
    } else {//偶数
      firstAngle =
          (countOfMsg / 2.0 - 1) * radianPerInterval + radianPerInterval / 2.0 + Math.PI / 2 + 0.1;
    }
    for (int i = 0; i < countOfMsg; i++) {
      double aa = firstAngle - i * radianPerInterval;
      double ax = newRadius * Math.sin(Math.PI / 2 - aa);
      double ay = newRadius * Math.cos(aa - Math.PI / 2);
      AffineTransform transform = AffineTransform.getRotateInstance(Math.PI / 2 - aa);
      Font f2 = f.deriveFont(transform);
      g2.setFont(f2);
      g2.drawString(head.substring(i, i + 1), (float) (centerX + ax), (float) (centerY - ay));
    }
    // 下附文（90度）
    f = new Font(valueFont, valueBOLD, valueSize);
    context = g2.getFontRenderContext();
    bounds = f.getStringBounds(value, context);
    double msgWidths = bounds.getWidth();
    int countOfValue = value.length();
    //计算间距
    double intervalValue = 0;
    if ((canvasWidth + canvasHeight) <= 400) {
      intervalValue = msgWidths / (countOfValue + 1);
    } else if ((canvasWidth + canvasHeight) >= 800) {
      intervalValue = msgWidths / (countOfValue - 2);
    } else {
      intervalValue = msgWidths / (countOfValue - 1);
    }
    double newValueRadius = circleRadius - thick;
    double radianValueInterval = 2 * Math.asin(intervalValue / (2 * newValueRadius));
    double firstAngleValue;
    if (countOfValue % 2 == 1) {
      firstAngleValue = -(countOfValue - 1) * radianValueInterval / 2.0 + Math.PI * 1.5 - 0.11;
    } else {
      firstAngleValue = -(countOfValue / 2.0 - 1) * radianValueInterval + radianValueInterval / 2.0
          + Math.PI * 1.5 - 0.11;
    }
    for (int i = 0; i < countOfValue; i++) {
      double aa = firstAngleValue + i * radianValueInterval;
      double ax = newValueRadius * Math.sin(Math.PI / 2 - aa);
      double ay = newValueRadius * Math.cos(aa - Math.PI / 2);
      AffineTransform transform = AffineTransform.getRotateInstance(1.5 * Math.PI - aa);
      Font f2 = f.deriveFont(transform);
      g2.setFont(f2);
      g2.drawString(value.substring(i, i + 1), (float) (centerX + ax), (float) (centerY - ay));
    }
    PngDPIProcessor process = new PngDPIProcessor();
    byte[] bytes = new byte[0];
    try {
      bytes = process.process(bi, DPI);
    } catch (IOException e) {
      e.printStackTrace();
    }
    g2.dispose();//销毁资源
    return bytes;
  }


}