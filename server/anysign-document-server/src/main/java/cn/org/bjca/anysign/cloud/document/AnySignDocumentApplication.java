package cn.org.bjca.anysign.cloud.document;

import cn.org.bjca.footstone.metrics.client.metrics.loggers.MetricsLoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/***************************************************************************
 * <pre>信手书文档服务应用</pre>
 * @文件名称: AnySignDocumentApplication
 * @包路径: cn.org.bjca.anysign.seal.document
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/13 10:42
 ***************************************************************************/
@SpringBootApplication
@ComponentScan(value = "cn.org.bjca.anysign")
public class AnySignDocumentApplication {

  public static void main(String[] args) {
    SpringApplication.run(AnySignDocumentApplication.class, args);
    MetricsLoggerFactory.init();
  }
}
