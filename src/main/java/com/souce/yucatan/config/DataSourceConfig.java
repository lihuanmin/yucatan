package com.souce.yucatan.config;

import lombok.Data;

/**
 * DataSourceConfig
 * @description DataSourceConfig
 * @author lihuanmin
 * @date 2022/7/18 18:28
 * @version 1.0
 */
@Data
public class DataSourceConfig {

    /**
     * 驱动类
     */
    protected String driverClass;

    /**
     * 连接地址
     */
    protected String jdbcUrl;

    /**
     * 用户名
     */
    protected String user;

    /**
     * 密码
     */
    protected String password;
}

