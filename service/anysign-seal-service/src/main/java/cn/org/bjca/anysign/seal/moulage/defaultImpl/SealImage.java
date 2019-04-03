
package cn.org.bjca.anysign.seal.moulage.defaultImpl;

import cn.org.bjca.anysign.seal.moulage.bean.ShapeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/***************************************************************************
 * <pre>svg生成工具类</pre>
 *
 * @文件名称: SealImage.class
 * @包 路   径：  cn.org.bjca.anysign.commponents.seal
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/5/18 10:51
 ***************************************************************************/
public class SealImage {

    /**
     * 五角星
     */
    @Deprecated
    protected static String FIVE_POINTED_STAR = "<svg x=\"" + "%s " + "\" y=\" " + "%s" + " \">\n"
            + "    <polygon points=\"40,0 16,72 76,24 4,24 64,72 40,0\" style=\"fill:red;\" transform=\"scale(0.333,0.333)\"/>\n"
            + "    </svg>\n";
    /**
     * four parameter: width height shape text
     */
    @Deprecated
    private static String template = "<svg contentScriptType=\"text/ecmascript\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" xmlns=\"http://www.w3.org/2000/svg\"\n"
            + "     baseProfile=\"full\" zoomAndPan=\"magnify\" contentStyleType=\"text/css\" preserveAspectRatio=\"xMidYMid meet\" width=\"" + "%s" + "px\" height=\"" + "%s" + "px\"\n"
            + "     version=\"1.0\">" + " %s%s " + "</svg>";

    /**
     * 生成 文本
     *
     * @param text
     * @param color  字体颜色，表示方式 #ff0000 or rgb(255,0,0)
     * @param size   字体大小
     * @param family 字体名称 如公章刻章字体
     * @param weight 字体粗细  normal,bold
     * @param path   文本起始位置及路径
     * @return
     */

    private String generateText(String text, String color, int size, String family, String weight, String path) {
        String id = UUID.randomUUID().toString();
        String template = "<defs><path id=\"" + id + "\" d=\"" + path + "\"/></defs><text style=\"fill:" + color + ";\" font-size=\"" + size
                + "\" font-family=\"" + family + "\" font-weight=\"" + weight + "\"><textPath xlink:href=\"#" + id + "\" text-anchor=\"middle\"  startOffset=\"50%\"  lengthAdjust=\"spacing\">"
                + text + "</textPath></text>";
        return template;

    }

    /**
     * 生成形状
     *
     * @param shape       签章形状
     * @param color       形状颜色
     * @param stroke      形状粗度
     * @param x           形状基点 x坐标
     * @param y           形状基点 y坐标
     * @param shapeWidth  形状宽度
     * @param shapeHeight 形状高度
     * @return
     */
    private String generateShape(int shape, String color, int stroke, float x, float y, float shapeWidth, float shapeHeight) {
        String shapeString = null;
        if (ShapeEnum.circle.ordinal() == shape) {
            shapeString = "<circle  stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" cx=\"" + x + "\" cy=\"" + y + "\" r=\"" + shapeWidth + "\" stroke=\"" + color + "\"\n"
                    + "     stroke-width=\"" + stroke + "\"/>";
        } else if (ShapeEnum.rectangle.ordinal() == shape) {
            shapeString = "<rect stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" x=\"" + x + "\" y=\"" + y + "\" width=\"" + shapeWidth + "\"  height=\"" + shapeHeight + "\" stroke=\"" + color + "\"\n"
                    + "     stroke-width=\"" + stroke + "\"/>";
        } else if (ShapeEnum.ellipse.ordinal() == shape) {
            shapeString = "<ellipse stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" cx=\"" + x + "\" cy=\"" + y + "\" rx=\"" + shapeWidth + "\"  ry=\"" + shapeHeight + "\" stroke=\"" + color + "\"\n"
                    + "     stroke-width=\"" + stroke + "\"/>";
        }
        return shapeString;
    }

    /**
     * 获取参数构造形状svg
     *
     * @param sealWidth
     * @param sealHeight
     * @param textSvgDescs
     * @param shapeSvgDesc
     * @return
     * @throws Exception
     */
    private String genSvgParam(float sealWidth, float sealHeight, List<Map<String, Object>> textSvgDescs,
                               Map<String, Object> shapeSvgDesc) throws Exception {
        //构造文本svg
        StringBuffer textSvgBuffer = new StringBuffer();
        for (Map<String, Object> textSvgDesc : textSvgDescs) {
            String text = (String) textSvgDesc.get("text");
            String fontColor = (String) textSvgDesc.get("color");
            int fontSize = (Integer) textSvgDesc.get("size");
            String fontFamily = (String) textSvgDesc.get("family");
            String fontWeight = (String) textSvgDesc.get("weight");
            String path = (String) textSvgDesc.get("path");
            String textSvn = generateText(text, fontColor, fontSize, fontFamily, fontWeight, path);
            textSvgBuffer.append(textSvn);
        }
        return textSvgBuffer.toString();
    }

    /**
     * 无五角星生成公章
     *
     * @param sealWidth
     * @param sealHeight
     * @param textSvgDescs
     * @param shapeSvgDesc
     * @return
     * @throws Exception
     */
    private String genSvg(float sealWidth, float sealHeight, List<Map<String, Object>> textSvgDescs, Map<String, Object> shapeSvgDesc) throws Exception {
        String textSvgBuffer = this.genSvgParam(sealWidth, sealHeight, textSvgDescs, shapeSvgDesc);
        int shape = (Integer) shapeSvgDesc.get("shape");
        String color = (String) shapeSvgDesc.get("color");
        int stroke = (Integer) shapeSvgDesc.get("stroke");
        float x = (Float) shapeSvgDesc.get("x");
        float y = (Float) shapeSvgDesc.get("y");
        float shapeWidth = (Float) shapeSvgDesc.get("shapeWidth");
        float shapeHeight = (Float) shapeSvgDesc.get("shapeHeight");
        String shapeSvg = generateShape(shape, color, stroke, x, y, shapeWidth, shapeHeight);
        //合成最终svg
        return String.format(template, sealWidth, sealHeight, shapeSvg, textSvgBuffer);
    }


    /**
     * 生成五角星
     *
     * @param sealWidth
     * @param sealHeight
     * @param textSvgDescs
     * @param shapeSvgDesc
     * @return
     * @throws Exception
     */
    private String genSvg(float sealWidth, float sealHeight, List<Map<String, Object>> textSvgDescs, Map<String, Object> shapeSvgDesc, float xStar, float yStar) throws Exception {
        String textSvgBuffer = this.genSvgParam(sealWidth, sealHeight, textSvgDescs, shapeSvgDesc);
        int shape = (Integer) shapeSvgDesc.get("shape");
        String color = (String) shapeSvgDesc.get("color");
        int stroke = (Integer) shapeSvgDesc.get("stroke");
        float x = (Float) shapeSvgDesc.get("x");
        float y = (Float) shapeSvgDesc.get("y");
        float shapeWidth = (Float) shapeSvgDesc.get("shapeWidth");
        float shapeHeight = (Float) shapeSvgDesc.get("shapeHeight");
        String shapeSvg = generateShape(shape, color, stroke, x, y, shapeWidth, shapeHeight);
        textSvgBuffer += String.format(FIVE_POINTED_STAR, xStar, yStar);
        //合成最终svg
        return String.format(template, sealWidth, sealHeight, shapeSvg, textSvgBuffer);
    }

    /**
     * 无五角星
     *
     * @param outputStream
     * @param sealWidth
     * @param sealHeight
     * @param textSvgDescs
     * @param shapeSvgDesc
     * @throws Exception
     */
    @Deprecated
    public void createSeal(OutputStream outputStream, float sealWidth, float sealHeight, List<Map<String, Object>> textSvgDescs, Map<String, Object> shapeSvgDesc) throws Exception {
        String svgStr = genSvg(sealWidth, sealHeight, textSvgDescs, shapeSvgDesc);
        creatImage(outputStream, svgStr);
    }

    /**
     * 有五角星
     *
     * @param outputStream
     * @param sealWidth
     * @param sealHeight
     * @param textSvgDescs
     * @param shapeSvgDesc
     * @param xStar        五角星x坐标
     * @param yStar        五角星y坐标
     * @throws Exception
     */
    @Deprecated
    public void createSeal(OutputStream outputStream, float sealWidth, float sealHeight, List<Map<String, Object>> textSvgDescs, Map<String, Object> shapeSvgDesc, float xStar, float yStar) throws Exception {
        String svgStr = genSvg(sealWidth, sealHeight, textSvgDescs, shapeSvgDesc, xStar, yStar);
        creatImage(outputStream, svgStr);
    }

    /**
     * 将svg生成图片流
     *
     * @param outputStream
     * @param svgStr
     * @throws Exception
     */
    public void creatImage(OutputStream outputStream, String svgStr) throws Exception {
        ByteArrayInputStream bais = null;
        try {
            bais = new ByteArrayInputStream(svgStr.getBytes("UTF-8"));
            TranscoderInput input_svg_image = new TranscoderInput(bais);
            TranscoderOutput output_png_image = new TranscoderOutput(outputStream);
            PNGTranscoder my_converter = new PNGTranscoder();
            my_converter.transcode(input_svg_image, output_png_image);
            outputStream.flush();
        } catch (Exception e) {
            throw new Exception("产生印章失败。", e);
        } finally {
            IOUtils.closeQuietly(bais);
        }
    }

    /**
     * @param shape
     * @param color
     * @param stroke
     * @param x
     * @param y
     * @param shapeWidth
     * @param shapeHeight
     * @return
     */
    @Deprecated
    public static Map<String, Object> buildShape(int shape, String color, int stroke, float x, float y, float shapeWidth, float shapeHeight) {
        Map<String, Object> desc = new HashMap<String, Object>(16);
        desc.put("shape", shape);
        desc.put("color", color);
        desc.put("stroke", stroke);
        desc.put("x", x);
        desc.put("y", y);
        desc.put("shapeWidth", shapeWidth);
        desc.put("shapeHeight", shapeHeight);
        return desc;
    }

    /**
     * 构建文件信息
     *
     * @param text
     * @param color
     * @param size
     * @param family
     * @param weight
     * @param path
     * @return
     */
    @Deprecated
    public static Map<String, Object> buildText(String text, String color, int size, String family, String weight, String path) {
        Map<String, Object> desc = new HashMap<String, Object>(16);
        desc.put("text", text);
        desc.put("color", color);
        desc.put("size", size);
        desc.put("family", family);
        desc.put("weight", weight);
        desc.put("path", path);
        return desc;
    }


}
