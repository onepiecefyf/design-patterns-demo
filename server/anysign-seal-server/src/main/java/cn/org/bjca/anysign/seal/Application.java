package cn.org.bjca.anysign.seal;

import cn.org.bjca.footstone.metrics.client.metrics.loggers.MetricsLoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author july_whj
 */
@SpringBootApplication
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
    MetricsLoggerFactory.init();
  }

}
