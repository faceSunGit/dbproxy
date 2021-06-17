package com.yj.platform.dbproxy.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 数据源配置
 *
 * @author 杨旭平
 * @date 2021/6/17 9:23
 */
@EnableTransactionManagement
@Configuration
@Primary
public class DruidDataSourceConfig {

    private Logger LOGGER = LoggerFactory.getLogger(DruidDataSourceConfig.class);

    public DruidDataSourceConfig() {
    }

    @Bean(name = "springDruidDataSource")
    @ConfigurationProperties("spring.datasource")
    public DruidDataSource druidDataSource(){
        LOGGER.info("======> create druid data source");
        return new DruidDataSource();
    }
}
