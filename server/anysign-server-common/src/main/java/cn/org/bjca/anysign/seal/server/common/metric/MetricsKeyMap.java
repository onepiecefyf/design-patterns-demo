package cn.org.bjca.anysign.seal.server.common.metric;

import java.util.HashMap;

/**
 * 埋点名称配置类
 *
 * @author july_whj
 */
public class MetricsKeyMap {

  public final static HashMap<String, String> FIRST_KEYMAP = new HashMap<>();
  public final static HashMap<String, String> SECOND_KEYMAP = new HashMap<>();
  public final static HashMap<String, String> THIRD_KEYMAP = new HashMap<>();

  static {
    //一级标题 默认对外服务
    FIRST_KEYMAP.put("firstName", "对外服务");
    //二级标题 服务名称
    SECOND_KEYMAP.put("doc", "文档服务");
    SECOND_KEYMAP.put("template", "模板服务");
    SECOND_KEYMAP.put("seal", "签章服务");
    //三级标题 服务接口
    THIRD_KEYMAP.put("docTransformation", "文档格式转换");
    THIRD_KEYMAP.put("docMerging", "合并PDF文档");
    THIRD_KEYMAP.put("docGeneration", "填充模板");
    THIRD_KEYMAP.put("sealGeneration", "生成印章");
    THIRD_KEYMAP.put("sealVerify", "验证签章");
    THIRD_KEYMAP.put("pdfDigest", "PDF生成摘要");
    THIRD_KEYMAP.put("pdfSignature", "PDF签章");
    THIRD_KEYMAP.put("sealMerging", "PDF文档合章");
  }
}
