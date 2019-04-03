package cn.org.bjca.anysign.seal.moulage.bean;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

/***************************************************************************
 * <pre>附文信息</pre>
 *
 * @author july_whj
 * @文件名称: RiderBean.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 16:48
 ***************************************************************************/
public class RiderBean implements Serializable {

  /**
   * 附文(*)
   */
  @Setter
  @Getter
  private String appendixContent;
  /**
   * 弧度(*)
   */
  @Setter
  @Getter
  private String radian;
  /**
   * 加粗(*)
   */
  @Setter
  @Getter
  private String fontWeight;
  /**
   * 字体大小(*)
   */
  @Setter
  @Getter
  private float fontSize;
  /**
   * 字体
   */
  @Setter
  @Getter
  private String fontFamily;
  /**
   * 颜色
   */
  @Setter
  @Getter
  private String color;

  /**
   * 顺序
   */
  @Setter
  @Getter
  private String order;
}