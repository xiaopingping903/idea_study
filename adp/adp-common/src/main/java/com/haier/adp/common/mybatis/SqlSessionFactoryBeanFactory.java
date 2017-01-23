package com.haier.adp.common.mybatis;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.core.io.Resource;

import javax.sql.DataSource;


/**
 * Mail: xiao@terminus.io <br>
 * Date: 2015-12-25 9:45 AM  <br>
 * Author: xiao
 */
@SuppressWarnings("all")
public class SqlSessionFactoryBeanFactory {


    private static final String DEFAULT_MAPPER_LOCATION_REGREXP = "classpath:/mapper/*Mapper.xml";

    public static SqlSessionFactoryBean newInstance(DataSource dataSource,
                                                    String typeAliasesPackage) {
        return newInstance(dataSource, typeAliasesPackage, DEFAULT_MAPPER_LOCATION_REGREXP);
    }

    public static SqlSessionFactoryBean newInstance(DataSource dataSource,
                                                         String typeAliasesPackage,
                                                         String mapperLocationsRegrexp) {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        org.springframework.beans.BeanWrapper wrapper = new BeanWrapperImpl(sqlSessionFactoryBean);
        Resource[] mapperLocations = (Resource[])((BeanWrapperImpl) wrapper)
                .convertForProperty(mapperLocationsRegrexp, "mapperLocations");
        sqlSessionFactoryBean.setMapperLocations(mapperLocations);
        return sqlSessionFactoryBean;
    }
}
