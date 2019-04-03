package cn.org.bjca.anysign.cloud.template;

import cn.org.bjca.footstone.metrics.client.metrics.loggers.MetricsLoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/***************************************************************************
 * <pre>信手书模板应用</pre>
 * @文件名称: AnySignTemplateApplication
 * @包路径: cn.org.bjca.anysign.seal.template
 * @版权所有:北京数字认证股份有限公司 (C) 2018
 *
 * @类描述:
 * @版本: V2.0
 * @创建人: wanghp
 * @创建时间: 2018/9/11 13:28
 ***************************************************************************/
@SpringBootApplication
@ComponentScan(value = "cn.org.bjca.anysign")
public class AnySignTemplateApplication {

    public static void main(String[] args) {
        SpringApplication.run(AnySignTemplateApplication.class, args);
        MetricsLoggerFactory.init();
    }
}
