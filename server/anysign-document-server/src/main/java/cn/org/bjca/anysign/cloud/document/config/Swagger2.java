package cn.org.bjca.anysign.cloud.document.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LvYong
 **/
@Configuration
@EnableSwagger2
@Slf4j
public class Swagger2 {

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean(SwaggerProperties.class)
    @ConditionalOnProperty(name = "swagger.enabled", matchIfMissing = true)
    public Docket createRestApi(SwaggerProperties swaggerProperties) {
        log.info("init swagger");

        ApiInfo apiInfo = new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .version(swaggerProperties.getVersion())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
//                .contact(new Contact(swaggerProperties.getContact().getName(),
//                                     swaggerProperties.getContact().getUrl(),
//                                     swaggerProperties.getContact().getEmail()))
                .contact("gao")
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .build();

        // base-path处理
        // 当没有配置任何path的时候，解析/**
        if (swaggerProperties.getBasePath().isEmpty()) {
            swaggerProperties.getBasePath().add("/**");
        }
        List<Predicate<String>> basePath = new ArrayList();
        for (String path : swaggerProperties.getBasePath()) {
            basePath.add(PathSelectors.ant(path));
        }

        // exclude-path处理
        List<Predicate<String>> excludePath = new ArrayList();
        for (String path : swaggerProperties.getExcludePath()) {
            excludePath.add(PathSelectors.ant(path));
        }

        Docket docketForBuilder = new Docket(DocumentationType.SWAGGER_2)
//                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo);

        Docket docket = docketForBuilder.select()
                                        .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                                        .apis(Predicates.not(RequestHandlerSelectors
                                                                     .basePackage("org.springframework.boot")))
                                        .apis(
                                                Predicates.not(RequestHandlerSelectors
                                                                       .basePackage("org.springframework.cloud")))
                                        .apis(
                                                Predicates.not(RequestHandlerSelectors.basePackage(
                                                        "org.springframework.data.rest.webmvc")))
                                        .paths(
                                                Predicates.and(
                                                        Predicates.not(Predicates.or(excludePath)),
                                                        Predicates.or(basePath)
                                                )

                                        )
                                        .build();
        return docket;
    }
}
