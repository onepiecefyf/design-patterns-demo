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
 * <pre>长，方章</pre>
 *
 * @文件名称: SealImageRectangle.class
 * @包 路   径：  cn.org.bjca.anysign.commponents.seal
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/5/18 15:12
 ***************************************************************************/
@Slf4j
public class SealImageRectangle extends SealImage {

    /**
     * 图形中心
     *
     * @param width
     * @param height
     * @return
     */
    private static float[] rectangleO(float width, float height, float borderWeight) {
        float[] floats = new float[2];
        floats[0] = borderWeight;
        floats[1] = borderWeight;
        return floats;
    }

    /**
     * 计算图形长宽
     *
     * @param width
     * @param height
     * @param borderWeight
     * @return
     */
    private static float[] rectangleR(float width, float height, float borderWeight) {
        float[] floats = new float[2];
        floats[0] = width - 2 * borderWeight;
        floats[1] = height - 2 * borderWeight;
        return floats;
    }

    /**
     * 获取长章Path
     *
     * @param width        印模宽度
     * @param siez         字体大小
     * @param borderWeight 边框厚度
     * @return
     */
    public static String getPath(float width, float siez, float borderWeight) {
        StringBuffer stringBuffer = new StringBuffer();
        float[] floats = new float[2];
        float[] floate = new float[2];
        floats[0] = borderWeight * 2;
        floats[1] = 2 * borderWeight + siez;
        floate[0] = width - floats[0];
        floate[1] = floats[1];
        stringBuffer.append("M").append(floats[0]).append(",").append(floats[1]);
        stringBuffer.append(" ").append(floate[0]).append(",").append(floate[1]);
        return stringBuffer.toString();
    }

    /**
     * 生成长、正章
     *
     * @param sealImageBean
     * @return
     */
    public static byte[] creatRectangle(SealImageBean sealImageBean) {
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
        float[] o = rectangleO(width, height, borderWeight);
        float[] r = rectangleR(width, height, borderWeight);
        if (0 == width || 0 == height) {
            log.error("The canvas width and height cannot be zero !");
            return null;
        }
        Map<String, Object> shapeDec = buildShape(sealImageBean.getShape().ordinal(), sealImageBean.getBorderColor(), sealImageBean.getBorderWeight(), o[0], o[1], r[0], r[1]);
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
        sealImageBean.setShape(ShapeEnum.rectangle);
        sealImageBean.setBorderColor("#ff0000");
        sealImageBean.setBorderWeight(5);
        sealImageBean.setCanvasWidth(100);
        sealImageBean.setCanvasHeight(50);
        List<Map<String, Object>> txtDescs = new LinkedList<Map<String, Object>>();
//        Map<String, Object> topTxt = buildText("北京数字认证股份有限公司", "#ff0000", 25, "公章刻章字体", "bold", "M110,212 A128,88 0 1,1,288,212");
////        Map<String, Object> topTxt = buildText(name, "#ff0000", 25, "公章刻章字体", "bold", path);
//        Map<String, Object> centerTxt1 = buildText("12346789", "#ff0000", 22, "公章刻章字体", "bold", "M102,160 L300,160");
        Map<String, Object> centerTxt2 = buildText("徐旭铭", "#ff0000", 25, "公章刻章字体", "bold", getPath(100, 25, 5));
//        txtDescs.add(topTxt);
//        txtDescs.add(centerTxt1);
        txtDescs.add(centerTxt2);
        sealImageBean.setTxtDescs(txtDescs);
        FileUtils.writeByteArrayToFile(new File("E:\\test\\test1.png"), creatRectangle(sealImageBean));
    }

}