package cn.org.bjca.anysign.seal.server.common.metric;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

/**
 * 解析URL对应服务名称
 *
 * @author july_whj
 */
@Data
@Accessors(chain = true)
public class MetricsNamePoint {

  /**
   * 第一级名称
   */
  private String firstName = "firstName";
  /**
   * 第二级名称
   */
  private String secondName;
  /**
   * 第三级名称
   */
  private String thirdName;

  public MetricsNamePoint(String url) {
    this.firstName = MetricsKeyMap.FIRST_KEYMAP.get(this.firstName);
    String[] subUrlArray = url.split("/");
    for (String str : subUrlArray) {
      if (StringUtils.isNotEmpty(MetricsKeyMap.SECOND_KEYMAP.get(str))) {
        secondName = MetricsKeyMap.SECOND_KEYMAP.get(str);
      }
      if (StringUtils.isNotEmpty(MetricsKeyMap.THIRD_KEYMAP.get(str))) {
        thirdName = MetricsKeyMap.THIRD_KEYMAP.get(str);
      }
    }
  }


  public static void main(String[] args) {
    MetricsNamePoint metricsNamePoint = new MetricsNamePoint(
        "http://127.0.0.1:10501/template/v1/docGeneration");
    System.out.println(metricsNamePoint.getFirstName());
    System.out.println(metricsNamePoint.getSecondName());
    System.out.println(metricsNamePoint.getThirdName());
  }

}
