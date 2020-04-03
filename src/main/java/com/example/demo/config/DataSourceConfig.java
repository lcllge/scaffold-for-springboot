package com.example.demo.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author lcllge
 */

/**
 * @version V1.0.0
 * @ClassName: {@link DataSourceConfig}
 * @Description: 数据源配置
 * @author: 兰州
 * @date: 2019/7/16 9:22
 * @Copyright: @2019 All rights reserved.
 */
@Configuration
public class DataSourceConfig {

    /**
     * mybatis Plus分页配置
     *
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }

}
