package cn.org.bjca.anysign.seal.moulage.defaultImpl;

import cn.org.bjca.anysign.seal.moulage.ISealImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class EllipseImage implements ISealImage {

  /**
   * 五角星
   */
  private static String FIVE_POINTED_STAR = "<svg x=\"66.67\" y=\"66.67\">\n"
      + "    <polygon points=\"100,00 40,180 190,60 10,60 160,180 100,0\" style=\"fill:red;\" transform=\"scale(0.333,0.333)\"/>\n"
      + "    </svg>\n";

  /**
   * 椭圆
   */
  private static String templateEllipse =
      "<svg contentScriptType=\"text/ecmascript\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.w3.org/2000/svg\"\n"
          + "     baseProfile=\"full\" zoomAndPan=\"magnify\" contentStyleType=\"text/css\" preserveAspectRatio=\"xMidYMid meet\" width=\"400.0px\" height=\"300.0px\"\n"
          + "     version=\"1.0\"><ellipse stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" cx=\"200\" cy=\"150\" rx=\"160\"  ry=\"120\" stroke=\"#ff3333\"\n"
          + "     stroke-width=\"7\"/>" + "%s%s%s%s%s" + "</svg>";

  /**
   * 生成上弦文
   */
  private String genHead(String htext) {
    if (htext == null || htext.length() == 0) {
      return "";
    }
    return
        "<defs><path id=\"pathhead\" d=\"M110,212 A128,88 0 1,1,288,212\"/></defs><text style=\"fill:red;\" font-size=\"25\" font-family=\"公章刻章字体\" font-weight=\"bold\"><textPath xlink:href=\"#pathhead\" text-anchor=\"middle\"  startOffset=\"50%\"  lengthAdjust=\"spacing\">"
            + htext + "</textPath></text>";
  }

  /**
   * 中心文字
   */
  private String genCenter(String text, int offset) {
    if (text == null || text.length() == 0) {
      return "";
    }
    return
        "<defs><path id=\"center\" d=\"M102,160 L300,160\"/></defs><text style=\"fill:red;\">\r\n" +
            "<textPath xlink:href=\"#center\" lengthAdjust=\"spacing\" style=\"font-family: 公章刻章字体;font-size:22;font-weight:bold;\" >"
            + text + "</textPath></text>";
  }

  /**
   * 附属文字
   */
  private String genTextFoot1(String text, int offset) {
    if (text == null || text.length() == 0) {
      return "";
    }
    return "<defs><path id=\"pathfoot1\" d=\"M137,208 L300,208\"/></defs>\r\n" +
        "<text style=\"fill:red;\">\r\n" +
        "<textPath xlink:href=\"#pathfoot1\" lengthAdjust=\"spacing\" style=\"font-family: 公章刻章字体;font-size:30;font-weight:bold;\">"
        + text + "</textPath></text>";
  }

  /**
   * 下弦文
   */
  @SuppressWarnings("unused")
  private String genTextFoot2(String text, int offset) {
    if (text == null || text.length() == 0) {
      return "";
    }
    return "<defs><path id=\"pathfoot1\" d=\"M145,210 L300,210\"/></defs>\r\n"
        + "	<text style=\"fill:red;\">\n"
        + "		<textPath xlink:href=\"#pathfoot1\" lengthAdjust=\"spacing\">\n"
        + "                  style=\"font-family: 公章刻章字体;font-size:20;font-weight:bold; \">\n"
        + text
        + "</textPath></text>";
  }

  @Override
  public void createSeal(OutputStream outputStream, List<String> imageTexts, String centerFlag,
      Float width,
      Float height, boolean equalProportion) throws Exception {
    // TODO Auto-generated method stub
    String svgStr = genSvg(imageTexts, centerFlag);
    ByteArrayInputStream bais = null;
    try {
      bais = new ByteArrayInputStream(svgStr.getBytes("UTF-8"));
      TranscoderInput input_svg_image = new TranscoderInput(bais);
      TranscoderOutput output_png_image = new TranscoderOutput(outputStream);
      PNGTranscoder my_converter = new PNGTranscoder();
      my_converter.addTranscodingHint(PNGTranscoder.KEY_WIDTH, width);
      my_converter.transcode(input_svg_image, output_png_image);
      outputStream.flush();
    } catch (Exception e) {
      throw new Exception("产生印章失败。", e);
    } finally {
      IOUtils.closeQuietly(bais);
    }
  }

  @Override
  public String genSvg(List<String> imageTexts, String centerFlag) {
    // TODO Auto-generated method stub
    String head = "";
    String center = "";
    String foot1 = "";
    String foot2 = "";
    String foot3 = "";
    String cenFlag = "";
    if ("star".equalsIgnoreCase(centerFlag)) { // 五角星
      cenFlag = FIVE_POINTED_STAR;
    } else { // 无图标
      cenFlag = "";
    }
    if (imageTexts.size() > 0) {
      head = genHead(imageTexts.get(0));
    }
    if (imageTexts.size() > 1) {
      center = genCenter(imageTexts.get(1), 0);
    }
    if (imageTexts.size() > 2) {
      foot1 = genTextFoot1(imageTexts.get(2), 0);
    }
    return String.format(templateEllipse, cenFlag, head, center, foot1, foot2);
  }

  public static void main(String[] args) throws Exception {
    ISealImage ei = new EllipseImage();
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    List<String> strings = new ArrayList<String>(3);
    strings.add("北京数字认证股份有限公司");
    strings.add("123456789123456789");
    strings.add("发票专用章");
    ei.createSeal(bao, strings, "", 262f, 202f, true);
    FileUtils.writeByteArrayToFile(new File("d:/testEllipse.png"), bao.toByteArray());
  }
}
