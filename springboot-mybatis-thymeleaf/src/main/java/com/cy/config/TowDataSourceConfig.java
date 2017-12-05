package com.cy.config;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import javax.sql.DataSource;


/**
 * Created by cy
 * 2017/12/1 10:39
 */
@Configuration
@MapperScan(basePackages = "com.cy.daos", sqlSessionTemplateRef = "towSqlSessionTemplate")
public class TowDataSourceConfig {

    @Bean(name = "towDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.tow")
    public DataSource setDataSource() {
        return DruidDataSourceBuilder.create().build();
    }

    @Bean(name = "towTransactionManager")
    public DataSourceTransactionManager testTransactionManager(@Qualifier("towDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "towSqlSessionFactory")
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("towDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
       // bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "towSqlSessionTemplate")
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("towSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
