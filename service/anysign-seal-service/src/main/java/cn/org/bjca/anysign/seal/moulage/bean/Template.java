package cn.org.bjca.anysign.seal.moulage.bean;

import cn.org.bjca.anysign.seal.service.bean.base.CommonRequestParam;
import java.io.Serializable;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

/***************************************************************************
 * <pre>模板请求对象</pre>
 *
 * @author july_whj
 * @文件名称: Template.class
 * @包 路   径：  cn.org.bjca.anysign.seal.convert.moulage.bean
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/29 18:30
 ***************************************************************************/
public class Template extends CommonRequestParam implements Serializable {

  /**
   * 模板编号
   */
  @Setter
  @Getter
  private String tempId;
  /**
   * 附文信息 integer 为附文顺序
   */
  @Setter
  @Getter
  private Map<String, String> rider;

}