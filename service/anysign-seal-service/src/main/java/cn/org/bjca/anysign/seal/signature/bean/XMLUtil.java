package cn.org.bjca.anysign.seal.signature.bean;


import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***************************************************************************
 * <pre>XML工具</pre>
 *
 * @文件名称: XMLUtil.java
 * @包 路 径： cn.org.bjca.seal.esspdf.tools.xml
 * @版权所有：北京数字认证股份有限公司 (C) 2015
 *
 * @类描述:
 * @版本: V2.0
 * @创建人： guzhixian
 * @创建时间：2015-8-11 下午4:50:51
 ***************************************************************************/
public class XMLUtil {

  private static Logger logger = LoggerFactory.getLogger(XMLUtil.class); // 日志

  /**
   * <p>获取xml节点数据</p>
   *
   * @Description:
   */
  public static String getElementText(Element element, String elementName) {
    if (StringUtils.isNotBlank(element.elementTextTrim(elementName))) {
      return element.elementTextTrim(elementName).trim();
    }
    return null;
  }


}
