package com.newcoder.algorithm.config;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
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

/**
 * 数据源配置
 * @author yafei.feng
 *
 * MapperScan 注解中我们声明了使用数据库1的dao类所在的位置,还声明了 SqlSessionTemplate
 *
 * @Primary 注解来声明这个数据库时默认数据库，不然可能会报错
 *
 */
@Configuration
@MapperScan(basePackages = "com.newcoder.algorithm.dao", sqlSessionTemplateRef = "dbSqlSessionTemplate")
public class DataSourceConfig {

  /**
   * 声明数据源
   */
  @Bean(name = "dbDataSource")
  @ConfigurationProperties(prefix = "spring.datasource.hikari.db")
  @Primary
  public DataSource buildDataSource() {
    return DataSourceBuilder.create().build();
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



}
