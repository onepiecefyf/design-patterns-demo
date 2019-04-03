package cn.org.bjca.anysign.seal.moulage.defaultImpl;


import cn.org.bjca.anysign.seal.moulage.bean.SealImageBean;
import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/***************************************************************************
 * <pre>圆章</pre>
 *
 * @文件名称: SealImageUtils.class
 * @包 路   径：  cn.org.bjca.anysign.commponents.seal
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/5/18 10:51
 ***************************************************************************/
@Slf4j
public class SealImageCircle extends SealImage {
    /**
     * 取小宽度
     *
     * @param width
     * @param height
     * @return
     */
    private static float min(float width, float height) {
        return (width >= height) ? height : width;
    }

    /**
     * 计算圆形半径
     *
     * @param width       画布宽度
     * @param height      画布高度
     * @param strokeWidth 线条宽度
     * @return
     */
    private static float circle_R(float width, float height, float strokeWidth) {
        float a = min(width, height);
        a = a * 0.5f;
        return a - strokeWidth;
    }

    /**
     * 获取圆心坐标
     *
     * @param width  画布宽度
     * @param height 画布高度
     * @return
     */
    private static float[] circleO(float width, float height) {
        float[] intArray = new float[2];
        intArray[0] = width * 0.5f;
        intArray[1] = height * 0.5f;
        return intArray;
    }

    /**
     * @param width
     * @param height
     * @param strokeWidth
     * @return
     */
    private static String path(float width, float height, float strokeWidth) {
        float x0, x1, y, a0, a1, r, y_, a_ = 0;
        StringBuffer stringBuffer = new StringBuffer();
        r = circle_R(width, height, strokeWidth);
        x0 = (r * 20 / 100) - strokeWidth;
        x1 = r * 2;
        y = r;
        y_ = r * 0.25f;
        a0 = r * 0.5f;
        a1 = r * 0.5f;
        a_ = r * 0.25f;
        stringBuffer.append("M");
        stringBuffer.append(x0);
        stringBuffer.append(",");
        stringBuffer.append(y + y_ - 2 * strokeWidth);
        stringBuffer.append(" A");
        stringBuffer.append(a0 - 2 * strokeWidth);
        stringBuffer.append(",");
        stringBuffer.append(a1);
        stringBuffer.append(" 0 1,1,");
        stringBuffer.append(x1 - x0);
        stringBuffer.append(",");
        stringBuffer.append(y + y_ - 2 * strokeWidth);
        return stringBuffer.toString();
    }

    /**
     * 创建圆形公章
     *
     * @param sealImageBean
     * @return
     */
    public static byte[] creatCircle(SealImageBean sealImageBean) {
        byte[] imageByte = null;
        if (null == sealImageBean) {
            log.error("SealImageBean cannot null !");
            return null;
        }
        SealImage ei = new SealImage();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        float width = sealImageBean.getCanvasWidth();
        float height = sealImageBean.getCanvasHeight();
        if (0 == width || 0 == height) {
            log.error("The canvas width or height cannot be zero !");
            return null;
        }
        float r = circle_R(width, height, sealImageBean.getBorderWeight());
        float[] o = circleO(width, height);
        Map<String, Object> shapeDec = buildShape(sealImageBean.getShape().ordinal(), sealImageBean.getBorderColor(), sealImageBean.getBorderWeight(), o[0], o[1], r, 0);
        try {
            if (sealImageBean.isStar()) {
                ei.createSeal(bao, width, height, sealImageBean.getTxtDescs(), shapeDec, o[0] - 14, o[1] - 14);
            } else {
                ei.createSeal(bao, width, height, sealImageBean.getTxtDescs(), shapeDec);
            }
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
        sealImageBean.setStar(true);
        sealImageBean.setShape(ShapeEnum.circle);
        sealImageBean.setBorderWeight(3);
        sealImageBean.setCanvasWidth(119);
        sealImageBean.setCanvasHeight(119);
        sealImageBean.setBorderColor("#ff0000");
        List<Map<String, Object>> txtDescs = new LinkedList<Map<String, Object>>();
        Map<String, Object> topTxt = buildText("北京数字认证股份有限公司", "#ff0000", 12, "公章刻章字体", "bold", "M29,88 A41,41 0 1,1,90,88");
//        Map<String, Object> topTxt = buildText(name, "#ff0000", 25, "公章刻章字体", "bold", path);
//        Map<String, Object> centerTxt1 = buildText("12346789", "#ff0000", 22, "公章刻章字体", "bold", "M20,170 L277,170");
        Map<String, Object> centerTxt2 = buildText("发票专用章", "#ff0000", 12, "公章刻章字体", "bold", "M21,92 L100,92");
        txtDescs.add(topTxt);
//        txtDescs.add(centerTxt1);
        txtDescs.add(centerTxt2);
        sealImageBean.setTxtDescs(txtDescs);
        FileUtils.writeByteArrayToFile(new File("E:\\test222.png"), creatCircle(sealImageBean));
    }
}