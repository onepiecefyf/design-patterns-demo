package cn.org.bjca.anysign.seal.server.common.environment;

import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 获取服务名称
 *
 * @author july_whj
 */
@Component
public class ServerName implements EnvironmentAware {

    private Environment environment;

    RelaxedPropertyResolver springPropertyResolver = null;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        springPropertyResolver = new RelaxedPropertyResolver(environment, "spring.application.");
    }

    /**
     * 获取服务名称
     *
     * @return 服务名称
     */
    public String getServerName() {
        return springPropertyResolver.getProperty("name");
    }
}
