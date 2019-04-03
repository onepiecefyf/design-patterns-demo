package cn.org.bjca.anysign.seal.signature.bean;


import cn.org.bjca.anysign.seal.service.bean.enumpackage.RelativePositionType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description 关键字定位规则实体
 */
@Data
public class KwRule implements Serializable {

    /**
     * 关键字
     */
    private String kw;

    /**
     * 整体偏移量
     */
    private String kwOffSet;

    /**
     * 签章相对关键字的位置
     */
    private RelativePositionType relativePosition = RelativePositionType.CENTER;
    /**
     * 关键字查找顺序 "0":全签，"1":正序，“2”:倒叙
     */
    private String pageNo;
    /**
     * x轴偏移量
     */
    private int rightDeviation;
    /**
     * y轴偏移量
     */
    private int lowerDeviation;

}
