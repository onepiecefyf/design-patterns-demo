package cn.org.bjca.anysign.seal.image.bean;


import cn.org.bjca.anysign.seal.service.bean.enumpackage.DocType;

import java.io.Serializable;
import java.util.List;

/***************************************************************************
 * <pre>印模实体类</pre>
 *
 * @文件名称: Moulage.class
 * @包 路   径：  cn.org.bjca.anysign.seal.image.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/12 14:45
 ***************************************************************************/
public class Moulage implements Serializable {

    /**
     * 印章文件类型，详见文件类型枚举(*)
     */
    private DocType fileType;
    /**
     * 印章形状(*)
     */
    private MoulageShape sealshape;
    /**
     * 印章宽度(*)
     */
    private float sealWidth;
    /**
     * 印章高度(*)
     */
    private float sealHeight;
    /**
     * 印章边框厚度(*)
     */
    private float borderWeight;
    /**
     * 印章颜色(*)
     */
    private String sealColor;
    /**
     * 印章字体(*)
     */
    private MoulageFontFamily fontFamily;
    /**
     * 中心图标(*)
     */
    private String center;
    /**
     * 附文信息
     */
    private List<RidersBean> riders;


    public DocType getFileType() {
        return fileType;
    }

    public void setFileType(DocType fileType) {
        this.fileType = fileType;
    }

    public MoulageShape getSealshape() {
        return sealshape;
    }

    public void setSealshape(MoulageShape sealshape) {
        this.sealshape = sealshape;
    }

    public float getSealWidth() {
        return sealWidth;
    }

    public void setSealWidth(float sealWidth) {
        this.sealWidth = sealWidth;
    }

    public float getSealHeight() {
        return sealHeight;
    }

    public void setSealHeight(float sealHeight) {
        this.sealHeight = sealHeight;
    }

    public float getBorderWeight() {
        return borderWeight;
    }

    public void setBorderWeight(float borderWeight) {
        this.borderWeight = borderWeight;
    }

    public String getSealColor() {
        return sealColor;
    }

    public void setSealColor(String sealColor) {
        this.sealColor = sealColor;
    }

    public MoulageFontFamily getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(MoulageFontFamily fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public List<RidersBean> getRiders() {
        return riders;
    }

    public void setRiders(List<RidersBean> riders) {
        this.riders = riders;
    }
}