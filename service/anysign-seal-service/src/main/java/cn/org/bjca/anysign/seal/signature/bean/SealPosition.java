package cn.org.bjca.anysign.seal.signature.bean;

import cn.org.bjca.anysign.seal.service.bean.enumpackage.SealPositionType;
import java.io.Serializable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zjgao
 * @create 2018/9/13.
 * @description
 */
@Data
@Getter
@Setter
public class SealPosition implements Serializable {

  /**
   * 定位规则，参考签章定位类型枚举
   */
  private SealPositionType positionType;

  /**
   * 坐标定位规则实体
   */
  private XYZRuleInfo xyzRuleInfo;
  /**
   * 关键字定位规则实体
   */
  private KwRule kwRule;
  /**
   * 骑缝章规则实体
   */
  private AcrossPage acrossPage;
}
