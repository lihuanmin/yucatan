package com.souce.yucatan.datasource;

import com.souce.yucatan.connection.IPooledConnection;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * DataSource 是Javax中的一个接口，用于获取一个Connection
 * 从DataSource获取的连接 是连接池中的连接  这个是新的一个规范概念
 */
public interface PoolDataSource extends DataSource {

    /**
     * 获取连接
     * @return
     * @throws SQLException
     */
    @Override
    IPooledConnection getConnection() throws SQLException;
    /**
     * 归还连接
     *
     * @param pooledConnection 连接池
     */
    void returnConnection(IPooledConnection pooledConnection);
}
