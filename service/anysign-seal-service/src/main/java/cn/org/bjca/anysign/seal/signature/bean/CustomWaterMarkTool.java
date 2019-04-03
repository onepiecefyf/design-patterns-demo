package cn.org.bjca.anysign.seal.signature.bean;


import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.bjca.util.encoders.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/***************************************************************************
 * <pre>水印工具类</pre>
 *
 * @文件名称: CustomWaterMarkTool.java
 * @包 路 径： cn.org.bjca.seal.esspdf.platform.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2014
 * @类描述:
 * @版本: V2.0
 * @创建人： guzhixian
 * @创建时间：2015-8-28 下午3:08:25
 ***************************************************************************/
public class CustomWaterMarkTool {
    /**
     * 水印类型：文字 =1、图片 =2
     */
    public static final int WATERMARK_TYPE_TEXT = 1;
    /**
     * 水印类型：文字 =1、图片 =2
     */
    public static final int WATERMARK_TYPE_IMAGE = 2;
    /**
     * 浮动类型：置上1、置下2
     */
    public static final int FLOATING_TYPE_OVER = 1;
    /**
     * 浮动类型：置上1、置下2
     */
    public static final int FLOATING_TYPE_UNDER = 2;
    /**
     * 浮动类型：置上1、置下2
     */
    public static final int MAX_CONTENT_SIZE = 500;

    private PdfReader reader = null;

    private PdfStamper stamper = null;

    private ByteArrayOutputStream out = null;

    private List<WaterMarkBean> waterMarkList;

    public static boolean isImage(int type) {
        return type == WATERMARK_TYPE_IMAGE;
    }

    public static boolean isUnderContent(int type) {
        return type == FLOATING_TYPE_UNDER;
    }

    /**
     * <p>根据水印模板内容转换水印对象</p>
     *
     * @param wmTemplate
     * @return
     * @throws Exception
     * @Description:
     */
    public static List<WaterMarkBean> parseWaterMarkList(String wmTemplate, String requestContents) throws Exception {
        List<WaterMarkBean> waterMarkList = new ArrayList<WaterMarkBean>();
        Document doc = DocumentHelper.parseText(wmTemplate);
        Element rootElt = doc.getRootElement();
        Iterator<?> iter = rootElt.elementIterator("watermarks");
        if (iter.hasNext()) {
            Element fieldsEle = (Element) iter.next();
            Iterator<?> iterFields = fieldsEle.elementIterator("watermark");
            WaterMarkBean watermark = null;
            String[] tsarr = {};
            if (StringUtils.isNotBlank(requestContents)) {
                tsarr = StringUtils.splitByWholeSeparatorPreserveAllTokens(requestContents, "##");
            }
            while (iterFields.hasNext()) {
                watermark = new WaterMarkBean();
                Element recordEle = (Element) iterFields.next();

                // 设置水印内容
                String content = XMLUtil.getElementText(recordEle, "content");
                String indexNum = XMLUtil.getElementText(recordEle, "indexNum");
                if (StringUtils.isNotBlank(indexNum)) {
                    watermark.setIndexNum(Integer.parseInt(indexNum));
                }
                if (tsarr != null && tsarr.length > watermark.getIndexNum()) {
                    if (StringUtils.isNotBlank(tsarr[watermark.getIndexNum()])) {
                        content = tsarr[watermark.getIndexNum()];
                    }
                }
                if (StringUtils.isBlank(content)) {
                    continue;
                }
                watermark.setContent(content);

                String type = XMLUtil.getElementText(recordEle, "type");
                if (StringUtils.isNotBlank(type)) {
                    watermark.setType(Integer.parseInt(type));
                }
                String fontSize = XMLUtil.getElementText(recordEle, "fontSize");
                if (StringUtils.isNotBlank(fontSize)) {
                    watermark.setFontSize(Integer.parseInt(fontSize));
                }
                String rotation = XMLUtil.getElementText(recordEle, "rotation");
                if (StringUtils.isNotBlank(rotation)) {
                    watermark.setRotation(Integer.parseInt(rotation));
                }
                String alignment = XMLUtil.getElementText(recordEle, "alignment");
                if (StringUtils.isNotBlank(alignment)) {
                    watermark.setAlignment(Integer.parseInt(alignment));
                }
                String floatingType = XMLUtil.getElementText(recordEle, "floatingType");
                if (StringUtils.isNotBlank(floatingType)) {
                    watermark.setFloatingType(Integer.parseInt(floatingType));
                }
                String percent = XMLUtil.getElementText(recordEle, "percent");
                if (StringUtils.isNotBlank(percent)) {
                    watermark.setPercent(Integer.parseInt(percent));
                }
                String positionX = XMLUtil.getElementText(recordEle, "positionX");
                if (StringUtils.isNotBlank(positionX)) {
                    watermark.setPositionX(Integer.parseInt(positionX));
                }
                String positionY = XMLUtil.getElementText(recordEle, "positionY");
                if (StringUtils.isNotBlank(positionY)) {
                    watermark.setPositionY(Integer.parseInt(positionY));
                }
                String fontColor = XMLUtil.getElementText(recordEle, "fontColor");
                if (StringUtils.isNotBlank(fontColor)) {
                    watermark.setFontColor(fontColor);
                }
                String fillOpacity = XMLUtil.getElementText(recordEle, "fillOpacity");
                if (StringUtils.isNotBlank(fillOpacity)) {
                    watermark.setFillOpacity(fillOpacity);
                }
                String strokeOpacity = XMLUtil.getElementText(recordEle, "strokeOpacity");
                if (StringUtils.isNotBlank(strokeOpacity)) {
                    watermark.setStrokeOpacity(Float.parseFloat(strokeOpacity));
                }
                waterMarkList.add(watermark);
            }
        }
        return waterMarkList;
    }

    public CustomWaterMarkTool(byte[] pdfBty) throws Exception {
        reader = new PdfReader(pdfBty);
        out = new ByteArrayOutputStream();
        stamper = new PdfStamper(reader, out);
    }

    /**
     * <p>增加水印配置</p>
     *
     * @param waterMarkList 水印配置列表
     * @Description:
     */
    public void addWaterMarkList(List<WaterMarkBean> waterMarkList) {
        this.waterMarkList = waterMarkList;
    }

    /**
     * <p>产生PDF水印</p>
     *
     * @return
     * @throws Exception
     * @Description:
     */
    public byte[] genPDFWaterMark() throws Exception {
        try {
            // 没有水印配置则返回原pdf
            if (waterMarkList == null || waterMarkList.size() < 1) {
                stamper.close();
                return out.toByteArray();
            }

            int total = reader.getNumberOfPages() + 1;
            PdfContentByte content = null;
            // 设置字体
            BaseFont font = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);

            PdfGState gs = new PdfGState();
            // 循环对每页插入水印
            for (int i = 1; i < total; i++) {
                for (WaterMarkBean wm : waterMarkList) {
                    if (StringUtils.isEmpty(wm.getContent())) {
                        continue;
                    }
                    // 水印在之前文本之下
                    if (isUnderContent(wm.getFloatingType())) {
                        content = stamper.getUnderContent(i);
                    } else {// 水印在之前文本之上
                        content = stamper.getOverContent(i);
                    }
                    // 开始
                    content.saveState();
                    content.beginText();
                    gs = new PdfGState();
                    // 设置透明度
                    gs.setFillOpacity(Float.parseFloat(wm.getFillOpacity()));
                    content.setGState(gs);

                    if (isImage(wm.getType())) {
                        // 图片水印
                        Image image = Image.getInstance(Base64.decode(wm.getContent()));
                        image.setAbsolutePosition(wm.getPositionX(), wm.getPositionY());
//                        // 设置图片的百分比
                        image.scalePercent(wm.getPercent());
                        content.addImage(image);
                    } else {
                        // 文字水印,如果文本超长则截断
                        String wmContent = wm.getContent();
                        if (StringUtils.isNotBlank(wm.getContent()) && wm.getContent().length() > MAX_CONTENT_SIZE) {
                            wmContent = StringUtils.substring(wmContent, 0, MAX_CONTENT_SIZE);
                        }
                        content.setFontAndSize(font, wm.getFontSize());
                        //TODO Compatible 16进制颜色 ->rgb()
//                        content.setColorFill(Color.getColor(wm.getFontColor()).getRGB());
                        content.showTextAligned(wm.getAlignment(), wmContent, wm.getPositionX(), wm.getPositionY(), wm.getRotation());
                    }
                    // 结束
                    content.endText();
                    content.restoreState();
                }
            }

            stamper.close();
            return out.toByteArray();
        } finally {
            IOUtils.closeQuietly(out);
            if (reader != null) {
                reader.close();
            }
        }
    }

}
