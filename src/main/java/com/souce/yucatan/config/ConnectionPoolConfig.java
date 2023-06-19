package com.souce.yucatan.config;

import lombok.*;

/**
 * ConnectionPoolConfig
 * @description ConnectionPoolConfig
 * @author lihuanmin
 * @date 2022/7/18 18:30
 * @version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionPoolConfig extends DataSourceConfig{

    /**
     * 最小数量
     */
    protected int minSize = 10;

    /**
     * 最大数量
     */
    protected int maxSize = 200;

    /**
     * 最大的等待时间，5S
     */
    protected long maxWaitMills = 10 * 1000;

    /**
     * 验证查询的语句
     */
    protected String validQuery = "select 1 from dual";

    /**
     * 验证的超时时间，5S
     */
    protected int validTimeOutSeconds = 5;

    /**
     * 获取时是否验证连接url可用性
     */
    protected boolean testOnBorrow = false;

    /**
     * 归还连接时是否验证
     */
    protected boolean testOnReturn = true;

    /**
     * 闲暇时是否验证
     */
    protected boolean testOnIdle = true;

    /**
     * 闲暇时验证的时间间隔,60S
     */
    protected long testOnIdleIntervalSeconds = 60;
}

