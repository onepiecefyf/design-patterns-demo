package com.newcoder.algorithm.config;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.encrypt.api.EncryptColumnRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.EncryptRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.EncryptTableRuleConfiguration;
import org.apache.shardingsphere.encrypt.api.EncryptorRuleConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.EncryptDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 数据源配置
 * @author yafei.feng
 *
 * MapperScan 注解中我们声明了使用数据库1的dao类所在的位置,还声明了 SqlSessionTemplate
 *
 * @Primary 注解来声明这个数据库时默认数据库，不然可能会报错
 *
 */
@Slf4j
@Configuration
@MapperScan(basePackages = "com.newcoder.algorithm.dao", sqlSessionTemplateRef = "dbSqlSessionTemplate")
public class DataSourceConfig {

  /**
   * 声明数据源
   */
  @Bean(name = "dbDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.hikari.db")
  @Primary
  public DataSource buildDataSource() throws SQLException {
    return EncryptDataSourceFactory.createDataSource(DataSourceBuilder.create().build(), getEncryptRuleConfiguration(), new Properties());
  }

  /**
   * 创建sqlSessionFactory
   *
   * 纯手动编写sql 不需要指定xml
   * bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/db1/*.xml"));
   */
  @Bean(name = "dbSqlSessionFactory")
  @Primary
  public SqlSessionFactory buildSqlSessionFactory(@Qualifier("dbDataSource") DataSource dataSource) throws Exception{
    SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    bean.setDataSource(dataSource);
    return bean.getObject();
  }

  /**
   * 配置事务管理
   */
  @Bean(name = "dbTransactionManager")
  @Primary
  public DataSourceTransactionManager buildTransactionManager(@Qualifier("dbDataSource") DataSource dataSource) {
    return new DataSourceTransactionManager(dataSource);
  }

  /**
   *
   * 这个类负责管理MyBatis的SqlSession,调用MyBatis的SQL方法，翻译异常
   * SqlSessionTemplate是线程安全的，可以被多个DAO所共享
   * @param sqlSessionFactory
   * @return
   */
  @Bean(name = "dbSqlSessionTemplate")
  @Primary
  public SqlSessionTemplate buildSqlSessionTemplate(@Qualifier("dbSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
    return new SqlSessionTemplate(sqlSessionFactory);
  }

  private EncryptRuleConfiguration getEncryptRuleConfiguration() {
    Properties props = new Properties();

    //自带aes算法需要
    props.setProperty("aes.key.value", "aeskey");
    EncryptorRuleConfiguration encryptorConfig = new EncryptorRuleConfiguration("AES", props);

    //自定义算法
    //props.setProperty("qb.finance.aes.key.value", aeskey);
    //EncryptorRuleConfiguration encryptorConfig = new EncryptorRuleConfiguration("QB-FINANCE-AES", props);

    EncryptRuleConfiguration encryptRuleConfig = new EncryptRuleConfiguration();
    encryptRuleConfig.getEncryptors().put("aes", encryptorConfig);

    //START: card_info 表的脱敏配置
    {
      EncryptColumnRuleConfiguration columnConfig1 = new EncryptColumnRuleConfiguration("", "name", "", "aes");
      EncryptColumnRuleConfiguration columnConfig2 = new EncryptColumnRuleConfiguration("", "id_no", "", "aes");
      EncryptColumnRuleConfiguration columnConfig3 = new EncryptColumnRuleConfiguration("", "finshell_card_no", "", "aes");
      Map<String, EncryptColumnRuleConfiguration> columnConfigMaps = new HashMap<>();
      columnConfigMaps.put("name", columnConfig1);
      columnConfigMaps.put("id_no", columnConfig2);
      columnConfigMaps.put("finshell_card_no", columnConfig3);
      EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration(columnConfigMaps);
      encryptRuleConfig.getTables().put("card_info", tableConfig);
    }
    //END: card_info 表的脱敏配置

    //START: pay_order 表的脱敏配置
    {
      EncryptColumnRuleConfiguration columnConfig1 = new EncryptColumnRuleConfiguration("", "card_no", "", "aes");
      Map<String, EncryptColumnRuleConfiguration> columnConfigMaps = new HashMap<>();
      columnConfigMaps.put("card_no", columnConfig1);
      EncryptTableRuleConfiguration tableConfig = new EncryptTableRuleConfiguration(columnConfigMaps);
      encryptRuleConfig.getTables().put("pay_order", tableConfig);
    }

    log.info("脱敏配置构建完成:{} ", encryptRuleConfig);
    return encryptRuleConfig;

  }



}
