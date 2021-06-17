package com.yj.platform.dbproxy.mybatis;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.type.EnumTypeHandler;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * mybatis-plus配置
 *
 * @author 杨旭平
 * @date 2021/6/17 10:04
 */
@Component
@MapperScan("com.yj.**.mapper")
public class MybatisPlusConfig {

    private Logger LOGGER = LoggerFactory.getLogger(MybatisPlusConfig.class);

    @Autowired
    private DruidDataSource druidDataSource;

    @Bean(name = "sqlSessionFactory")
    public MybatisSqlSessionFactoryBean sqlSessionFactory() {
        MybatisSqlSessionFactoryBean mybatisSqlSessionFactoryBean = new MybatisSqlSessionFactoryBean();
        mybatisSqlSessionFactoryBean.setDataSource(druidDataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Set<Resource> result = new LinkedHashSet<Resource>(16);
        try {
            result.addAll(Arrays.asList(resolver.getResources("classpath:mappers/*.xml")));
            //result.addAll(Arrays.asList(resolver.getResources("classpath*:config/mapper/*/*.xml")));
            //result.addAll(Arrays.asList(resolver.getResources("classpath*:config/mapper/*.xml")));
        } catch (Exception e){
            LOGGER.warn("获取【classpath:mapper/*.xml】资源错误!");
        }
        mybatisSqlSessionFactoryBean.setMapperLocations(result.toArray(new Resource[0]));
        MybatisConfiguration mybatisConfiguration = new MybatisConfiguration();
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);


        mybatisConfiguration.setLogImpl(org.apache.ibatis.logging.log4j2.Log4j2Impl.class);
        //乐观锁配置
        mybatisConfiguration.setDefaultEnumTypeHandler(org.apache.ibatis.type.BaseTypeHandler.class);
        mybatisConfiguration.setMapUnderscoreToCamelCase(true);

        //设置枚举类型映射用 name
        mybatisConfiguration.setDefaultEnumTypeHandler(EnumTypeHandler.class);

        mybatisSqlSessionFactoryBean.setConfiguration(mybatisConfiguration);

        GlobalConfig globalConfig = new GlobalConfig();

        //配置DbConfig
        GlobalConfig.DbConfig dbConfig = new GlobalConfig.DbConfig();
        // 主键策略配置
        dbConfig.setIdType(IdType.AUTO);
        globalConfig.setDbConfig(dbConfig);
        globalConfig.setMetaObjectHandler(new MetaObjectHandlerConfig());

        mybatisSqlSessionFactoryBean.setGlobalConfig(globalConfig);

        return mybatisSqlSessionFactoryBean;
    }

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        return interceptor;
    }

}
