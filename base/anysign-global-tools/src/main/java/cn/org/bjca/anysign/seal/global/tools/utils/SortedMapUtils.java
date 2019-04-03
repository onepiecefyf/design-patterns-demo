package cn.org.bjca.anysign.seal.global.tools.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/***************************************************************************
 * <pre>ASCII码从小到大排序</pre>
 *
 * @author july_whj
 * @文件名称: SortedMapUtils.class
 * @包 路   径：  cn.org.bjca.anysign.seal.global.tools.utils
 * @版权所有：北京数字认证股份有限公司 (C) 2018
 * @类描述:
 * @版本: V2.0
 * @创建人： july_whj
 * @创建时间：2018/9/20 13:35
 ***************************************************************************/
public class SortedMapUtils {

  /**
   * 排序参数
   */
  private SortedMap<String, String> parameters;

  public SortedMapUtils() {

  }

  /**
   * 构造
   *
   * @param parameters SortedMap待排序参数
   */
  public SortedMapUtils(SortedMap<String, String> parameters) {
    this.parameters = parameters;
  }

  /**
   * 构造
   *
   * @param parameters HashMap待排序参数
   */
  public SortedMapUtils(Map<String, String> parameters) {
    this.parameters = new TreeMap<String, String>(parameters);
  }


  /**
   * 返回排序后参数
   */
  public String setSortedMap(SortedMap<String, String> parameters) {
    this.parameters = parameters;
    return toString();
  }


  /**
   * 重新toString方法，返回排序后参数
   */
  @Override
  public String toString() {
    StringBuffer sb = new StringBuffer();
    //所有参与传参的参数按照accsii排序（升序）
    Set es = parameters.entrySet();
    Iterator it = es.iterator();
    while (it.hasNext()) {
      Map.Entry entry = (Map.Entry) it.next();
      String k = (String) entry.getKey();
      Object v = entry.getValue();
      //空值不传递，不参与签名组串
      if (null != v && !"".equals(v)) {
        sb.append(k).append("=").append(v).append("&");
      }
    }
    String result = sb.toString().substring(0, sb.toString().length() - 1);
    //排序后的字符串
    return result;
  }


}