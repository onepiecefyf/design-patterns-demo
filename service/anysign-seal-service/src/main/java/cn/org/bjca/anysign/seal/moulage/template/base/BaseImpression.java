package cn.org.bjca.anysign.seal.moulage.template.base;

import cn.org.bjca.anysign.seal.moulage.defaultImpl.SealImage;
import cn.org.bjca.anysign.seal.moulage.bean.RiderBean;
import cn.org.bjca.anysign.seal.moulage.bean.TemplateBean;
import cn.org.bjca.anysign.seal.global.tools.constant.StatusConstants;
import cn.org.bjca.anysign.seal.global.tools.exception.BaseRuntimeException;
import cn.org.bjca.anysign.seal.global.tools.utils.Assert;
import cn.org.bjca.anysign.seal.moulage.ICreateImage;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/***************************************************************************
 * <pre>印模生成工具</pre>
 *
 * @author july_whj
 * @文件名称: BaseImpression.cls
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 13:50
 ***************************************************************************/
@Slf4j
public abstract class BaseImpression extends SealImage implements ICreateImage {

    /**
     * 默认dpi
     */
    public static final int DPI = 600;

    /**
     * four parameter: width height shape text
     */
    private static String template = "<svg contentScriptType=\"text/ecmascript\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" " +
            "xmlns=\"http://www.w3.org/2000/svg\"\n" + "baseProfile=\"full\" zoomAndPan=\"magnify\" " +
            "contentStyleType=\"text/css\" preserveAspectRatio=\"xMidYMid meet\" " +
            "width=\"" + "%s" + "px\" height=\"" + "%s" + "px\"\n" + "version=\"1.0\">" + " %s%s " + "</svg>";
    /**
     * 构建形状
     */
    private static String shapeString = "<ellipse stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" " +
            "cx=\"%s\" cy=\"%s\" rx=\"%s\"  ry=\"%s\" stroke=\"%s\" stroke-width=\"%s\"/>";
    /**
     * 方章
     */
    private static String shapeRectangleString = "<rect stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" " +
            "x=\"%s\" y=\"%s\" width=\"%s\" height=\"%s\" stroke=\"%s\"\n stroke-width=\"%s\"/>";

    /**
     * 圆章
     */
    private static String shapeCircleleString = "<circle stroke-dashoffset=\"3\" display=\"inline\" fill=\"none\" " +
            "cx=\"%s\" cy=\"%s\" r=\"%s\" stroke=\"%s\"\nstroke-width=\"%s\"/>";


    /**
     * 构建文本路径
     */
    private static String PATH = "<defs><path id=\"%s\" d=\"%s\"/></defs>\n%s";
    /**
     * 构建文本样式
     */
    private static String textStyle = "<text style=\"fill:%s;\" font-size=\"%s\" font-family=\"%s\" font-weight=\"%s\">\n%s</text>";

    /**
     * 构建字体对象
     *
     * @param color      颜色
     * @param fontSize   字体大小
     * @param fontFamily 字体样式
     * @param fontWeight 字体加粗
     * @param value      印模附文
     * @param path       印模路径
     * @return 印模信息
     */
    private String getText(String color, int fontSize, String fontFamily, String fontWeight, String value, String path) {
        String id = UUID.randomUUID().toString();
        String textValue = generateText(id, value);
        String textSt = String.format(textStyle, color, fontSize, fontFamily, fontWeight, textValue);
        return String.format(PATH, id, path, textSt);
    }

    /**
     * 构建文本内容
     *
     * @param id   pathId
     * @param text 印模内容
     * @return 印模内容
     */
    private String generateText(String id, String text) {
        String template = "<textPath xlink:href=\"#" + id + "\" font-stretch=\"condensed\" text-anchor=\"middle\" " +
                " startOffset=\"50%\"  lengthAdjust=\"spacing\">\n" + text + "\n</textPath>\n";
        return template;
    }

    /**
     * 构建椭圆
     *
     * @param x      中心x坐标
     * @param y      中心y坐标
     * @param rx     x半轴
     * @param ry     y半轴
     * @param color  颜色
     * @param stroke 厚度
     * @return String
     */
    private String getShapeString(int x, int y, int rx, int ry, String color, int stroke) {
        return String.format(shapeString, x, y, rx, ry, color, stroke);
    }

    /**
     * 构建长章
     *
     * @param x      属性定义矩形的左侧位置
     * @param y      属性定义矩形的顶端位置
     * @param width  宽度
     * @param height 高度
     * @param color  颜色
     * @param stroke 厚度
     * @return String
     */
    private String shapeRectangleString(int x, int y, int width, int height, String color, int stroke) {
        return String.format(shapeRectangleString, x, y, width, height, color, stroke);
    }

    /**
     * 圆章
     *
     * @param x      中心x
     * @param y      中心y
     * @param r      半径
     * @param color  颜色
     * @param stroke 边框厚度
     * @return String
     */
    private String getShapeCircleleString(int x, int y, int r, String color, int stroke) {
        return String.format(shapeCircleleString, x, y, r, color, stroke);
    }

    /**
     * 构建svg
     *
     * @param width  画布宽度
     * @param height 画布高度
     * @param shape  形状
     * @param value  附文内容
     * @return svg
     */
    private String getTemplateEllipse(int width, int height, String shape, String value) {
        return String.format(template, width, height, shape, value);
    }

    @Override
    public void verification(TemplateBean templateBean) {
        Assert.notNull(templateBean.getTempId(), StatusConstants.IMAGE_ERROR);
    }

    /**
     * 获取印模内容
     *
     * @param templateBean 模板对象
     * @return 印模内容
     */
    private String getTexts(TemplateBean templateBean) {
        String value;
        StringBuffer stringBuffer = new StringBuffer();
        for (RiderBean riderBean : templateBean.getRiderBeans()) {
            stringBuffer.append(this.getText(riderBean.getColor(), (int) riderBean.getFontSize(), riderBean.getFontFamily(),
                    riderBean.getFontWeight(), riderBean.getAppendixContent(), riderBean.getRadian()));
        }
        value = stringBuffer.toString();
        if (templateBean.getIsStar()) {
            value += templateBean.getStarValue();
        }
        return value;
    }

    /**
     * 获取 svg
     *
     * @param templateBean 模板对象
     * @return
     */
    protected String getSvg(TemplateBean templateBean) {
        String shape;
        switch (templateBean.getShapeEnum()) {
            case rectangle:
            case square:
                shape = shapeRectangleString(templateBean.getStroke() / 2, templateBean.getStroke() / 2,
                        templateBean.getWidth() - templateBean.getStroke(),
                        templateBean.getHeight() - templateBean.getStroke(),
                        templateBean.getColor(), templateBean.getStroke());
                break;
            case circle:
                shape = getShapeCircleleString(templateBean.getCenterX(), templateBean.getCenterY(),
                        templateBean.getRx(), templateBean.getColor(), templateBean.getStroke());
                break;
            case ellipse:
                shape = getShapeString(templateBean.getCenterX(), templateBean.getCenterY(), templateBean.getRx(),
                        templateBean.getRy(), templateBean.getColor(), templateBean.getStroke());
                break;
            default:
                throw new BaseRuntimeException(StatusConstants.SEALSHAPE_ERROR, null, "生成印章");
        }
        String text = getTexts(templateBean);
        return getTemplateEllipse(templateBean.getWidth(), templateBean.getHeight(), shape, text);
    }

}