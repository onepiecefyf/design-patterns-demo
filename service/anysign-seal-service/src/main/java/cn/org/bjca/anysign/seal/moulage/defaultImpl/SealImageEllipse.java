package cn.org.bjca.anysign.seal.moulage.defaultImpl;


import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

/***************************************************************************
 * <pre>椭圆章</pre>
 *
 * @文件名称: SealImageEllipse
 * @包 路   径：  cn.org.bjca.anysign.commponents.seal
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/5/18 15:12
 ***************************************************************************/
@Slf4j
public class SealImageEllipse extends SealImage {

  /**
   * 计算椭圆两个半轴
   *
   * @param width 画布宽度
   * @param height 画布高度
   * @param strokeWidth 线条宽度
   */
  private static float[] ellipseR(float width, float height, float strokeWidth) {
    float[] floats = new float[2];
    floats[0] = width * 0.5f - strokeWidth;
    floats[1] = height * 0.5f - strokeWidth;
    return floats;
  }

  /**
   * 获取椭圆中心坐标
   *
   * @param width 画布宽度
   * @param height 画布高度
   */
  private static float[] ellipseO(float width, float height) {
    float[] intArray = new float[2];
    intArray[0] = width * 0.5f;
    intArray[1] = height * 0.5f;
    return intArray;
  }


  /**
   * 创建椭圆形公章
   */
  public static byte[] creatEllipse(SealImageBean sealImageBean) {
    byte[] imageByte = null;
    if (null == sealImageBean) {
      log.error("SealImageBean cannot null !");
      return null;
    }
    SealImage ei = new SealImage();
    ByteArrayOutputStream bao = new ByteArrayOutputStream();
    float width = sealImageBean.getCanvasWidth();
    float height = sealImageBean.getCanvasHeight();
    float borderWeight = sealImageBean.getBorderWeight();
    float[] o = ellipseO(width, height);
    float[] r = ellipseR(width, height, borderWeight);
    if (0 == width || 0 == height) {
      log.error("The canvas width and height cannot be zero !");
      return null;
    }
    Map<String, Object> shapeDec = buildShape(sealImageBean.getShape().ordinal(),
        sealImageBean.getBorderColor(), sealImageBean.getBorderWeight(), o[0], o[1], r[0], r[1]);
    try {
      ei.createSeal(bao, width, height, sealImageBean.getTxtDescs(), shapeDec);
    } catch (Exception e) {
      e.printStackTrace();
    }
    imageByte = bao.toByteArray();
    try {
      bao.close();
    } catch (IOException e) {
    }
    return imageByte;
  }

  public static void main(String[] args) throws IOException {
    SealImageBean sealImageBean = new SealImageBean();
    sealImageBean.setStar(false);
    sealImageBean.setShape(ShapeEnum.ellipse);
    sealImageBean.setBorderColor("#ff0000");
    sealImageBean.setBorderWeight(5);
    sealImageBean.setCanvasWidth(400);
    sealImageBean.setCanvasHeight(300);
    List<Map<String, Object>> txtDescs = new LinkedList<Map<String, Object>>();
    Map<String, Object> topTxt = buildText("北京数字认证股份有限公司", "#ff0000", 25, "公章刻章字体", "bold",
        "M110,212 A128,88 0 1,1,288,212");
//        Map<String, Object> topTxt = buildText(name, "#ff0000", 25, "公章刻章字体", "bold", path);
    Map<String, Object> centerTxt1 = buildText("12346789", "#ff0000", 22, "公章刻章字体", "bold",
        "M102,160 L300,160");
    Map<String, Object> centerTxt2 = buildText("发票专用章", "#ff0000", 30, "公章刻章字体", "bold",
        "M102,208 L300,208");
    txtDescs.add(topTxt);
    txtDescs.add(centerTxt1);
    txtDescs.add(centerTxt2);
    sealImageBean.setTxtDescs(txtDescs);
    FileUtils.writeByteArrayToFile(new File("E:\\test1.png"), creatEllipse(sealImageBean));
  }

}