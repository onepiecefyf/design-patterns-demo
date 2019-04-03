package cn.org.bjca.anysign.seal.signature.bean;

import cn.org.bjca.anysign.seal.service.bean.enumpackage.AcrossPageType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description  骑缝章规则实体
 */
@Data
@Getter
@Setter
public class AcrossPage implements Serializable{
    /**
     * 骑缝章模式,"L" :多页左骑缝,"R" :多页右骑缝
     */
    private String  acrossPagePattern;
    /**
     * 	首页宽度,100以内的整形数，为首页印章占整个印章的百分比
     */
    private int startWidth;
    /**
     * 高度比例,10000以内的整形术，为印章高度占真个pdf页面高度的比例，比例为 posCoord/10000
     */
    private int posCoord;

    /**
     * 骑缝章签名方式：单页签名(默认）、多页签名
     */
    private AcrossPageType acrossPageType;

}
