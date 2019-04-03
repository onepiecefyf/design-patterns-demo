package cn.org.bjca.anysign.seal.moulage.template.impl;

import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.moulage.tools.PngDPIProcessor;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

/***************************************************************************
 * <pre>长章</pre>
 *
 * @author july_whj
 * @文件名称: RectangleImpression.cls
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/28 14:20
 ***************************************************************************/
@Slf4j
public class RectangleImpression {

  /**
   * 默认dpi
   */
  private static final int DPI = 600;
  /**
   * 宽度
   */
  private static final int width = 472;
  /**
   * 高度
   */
  private static final int height = 236;

  /**
   * 默认边框厚度 1 mm
   */
  private static final int THICK = 24;
  /**
   * 默认字体
   */
  private static final String FONTFIM = "楷体";
  /**
   * 默认颜色 红色
   */
  private static final String DEFCOLOR = "rgb(255,0,0)";

  /**
   * 默认字体 加粗
   */
  private static final boolean FONTWITH = true;
  /**
   * 字体大小
   */
  private static final int fontSize = 135;

  /**
   * @param name 姓名
   */
  public static byte[] createRectangle(String name) {
    return createRectangle(name, width, height, THICK, fontSize, FONTFIM, DEFCOLOR, FONTWITH);
  }

  /**
   * 创建长章
   *
   * @param name 姓名
   * @param thick 边框厚度
   */
  public static byte[] createRectangle(String name, float thick) {
    return createRectangle(name, width, height, thick, fontSize, FONTFIM, DEFCOLOR, FONTWITH);
  }

  /**
   * 创建长章
   *
   * @param name 姓名
   * @param thick 边框厚度
   * @param fontFim 字体
   * @param fontSize 字体大小
   */
  public static byte[] createRectangle(String name, float thick, String fontFim, int fontSize) {
    return createRectangle(name, width, height, thick, fontSize, fontFim, DEFCOLOR, FONTWITH);
  }

  /**
   * 长章工具类
   *
   * @param head 姓名
   * @param canvasWidth 宽度
   * @param canvasHeight 高度
   * @param thick 厚度
   * @param fontSize 字体大小
   * @param font 字体
   * @param rgb 颜色
   * @param fontWeight 是否加粗
   */
  public static byte[] createRectangle(String head, int canvasWidth, int canvasHeight, float thick,
      int fontSize, String font, String rgb, boolean fontWeight) {
    BufferedImage bi = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = bi.createGraphics();
    //设置透明背景
    bi = g2.getDeviceConfiguration()
        .createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
    g2 = bi.createGraphics();
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2.setPaint(changeRGB2Color(rgb));
    g2.setStroke(new BasicStroke(thick * 2));
    g2.drawRect(0, 0, canvasWidth - 1, canvasHeight - 1);
    Font starFont;
    if (fontWeight) {
      starFont = new Font(font, Font.BOLD, fontSize);
    } else {
      starFont = new Font(font, Font.PLAIN, fontSize);
    }
    g2.setFont(starFont);
    g2.setColor(changeRGB2Color(rgb));
    FontMetrics fm = g2.getFontMetrics(starFont);
    int textWidth = fm.stringWidth(head);
    int textHeight = fm.getAscent();
    int widthX = (canvasWidth - textWidth) / 2;
    int heightY = (canvasHeight - textHeight) / 2 + textHeight - (int) thick;
    g2.drawString(head, widthX, heightY);
    PngDPIProcessor process = new PngDPIProcessor();
    byte[] bytes = new byte[0];
    try {
      bytes = process.process(bi, DPI);
    } catch (IOException e) {
      e.printStackTrace();
    }
    g2.dispose();
    return bytes;
  }

  /**
   * reb -> color
   */
  private static Color changeRGB2Color(String rgb) {
    int[] ints = new int[3];
    if (StringUtils.isEmpty(rgb)) {
      log.warn("rgb is null default rgb(255,0,0)");
      ints[0] = 255;
      ints[1] = 0;
      ints[2] = 0;
    } else {
      String[] strings = rgb.split(",");
      for (int i = 0; i < strings.length; i++) {
        String number = strings[i].replaceAll("[^0-9]", "");
        if (StringUtils.isEmpty(number)) {
          throw new BaseRuntimeException(StatusConstants.PARAMETER_ERROR.getStatus(),
              "印模颜色参数设置错误！");
        }
        ints[i] = Integer.valueOf(number);
      }
    }
    return new Color(ints[0], ints[1], ints[2]);
  }

}