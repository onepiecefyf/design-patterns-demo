package cn.org.bjca.anysign.seal.signature.bean;

import cn.org.bjca.anysign.seal.service.bean.enumpackage.RelativePositionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description 坐标定位规则实体
 */
@Data
@Getter
@Setter
public class XYZRuleInfo implements Serializable{
    /**
     * 印章左边界
     */
    private float left;
    /**
     * 印章下边界
     */
    private float bottom;
    /**
     * 印章右边界
     */

    private float right;
    /**
     * 印章上边界
     */
    private float top;

    private RelativePositionType relativePosition = RelativePositionType.CENTER;
    /**
     * 页码
     */
    private int pageNo;
    /**
     * x轴偏移量
     */
    private int rightDeviation;
    /**
     * y轴偏移量
     */
    private int lowerDeviation;
}
