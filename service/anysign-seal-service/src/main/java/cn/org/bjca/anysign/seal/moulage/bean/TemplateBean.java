package cn.org.bjca.anysign.seal.moulage.bean;

import cn.org.bjca.anysign.seal.global.tools.utils.SerialCloneable;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/***************************************************************************
 * <pre>椭圆模板对象</pre>
 *
 * @author july_whj
 * @文件名称: TemplateBean.cls
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 16:41
 ***************************************************************************/
public class TemplateBean extends SerialCloneable {

    /**
     * 模板ID
     */
    @Setter
    @Getter
    private String tempId;
    /**
     * 画布宽
     */
    @Setter
    @Getter
    private int width;
    /**
     * 画布高
     */
    @Setter
    @Getter
    private int height;
    /**
     * 中心X
     */
    @Setter
    @Getter
    private int centerX;
    /**
     * 中心Y
     */
    @Setter
    @Getter
    private int centerY;
    /**
     * 长半轴
     */
    @Setter
    @Getter
    private int rx;
    /**
     * 短半轴
     */
    @Setter
    @Getter
    private int ry;
    /**
     * 颜色
     */
    @Setter
    @Getter
    private String color;
    /**
     * 边框厚度
     */
    @Setter
    @Getter
    private int stroke;
    /**
     * 附文信息
     */
    @Setter
    @Getter
    private List<RiderBean> riderBeans;
    /**
     * 印模形状
     */
    @Setter
    @Getter
    private ShapeEnum shapeEnum;
    /**
     * 是否有五角星
     */
    @Setter
    @Getter
    private Boolean isStar = false;
    /**
     * 五角星SVG
     */
    @Setter
    @Getter
    private String starValue;
}