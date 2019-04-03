package cn.org.bjca.anysign.seal.moulage.tools;

/***************************************************************************
 * <pre>图片转换工具类</pre>
 *
 * @author july_whj
 * @文件名称: ImageConverterTools
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/27 16:15
 ***************************************************************************/
public class ImageConverterTools {

  private static final double DPI = 600;
  /**
   * 1英寸是2.54CM
   */
  private static final double INCH_2_CM = 2.54d;

  /**
   * 获取打印尺寸
   *
   * @param pixel 像素
   * @param dpi 分辨率
   */
  public static double getMeasure(int pixel, double dpi) {
    return (pixel / dpi) * INCH_2_CM;
  }

  /**
   * 获取打印尺寸
   *
   * @param pixel 像素
   */
  public static double getMeasure(int pixel) {
    return getMeasure(pixel, DPI);
  }

  /**
   * 获取像素
   *
   * @param measure 尺寸 cm
   */
  public static double getPixel(double measure) {
    return getPixel(measure, DPI);
  }

  /**
   * 获取像素
   *
   * @param measure 尺寸 cm
   * @param dpi 分辨率
   */
  public static double getPixel(double measure, double dpi) {
    return Math.round((measure / INCH_2_CM) * dpi);
  }

  public static double getDPI() {
    return DPI;
  }

  public static void main(String[] args) {
    System.out.println(getPixel(0.1));
  }

}