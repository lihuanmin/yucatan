package com.souce.yucatan.datasource;


import com.souce.yucatan.config.ConnectionPoolConfig;
import com.souce.yucatan.connection.IPooledConnection;
import com.souce.yucatan.connection.PooledConnection;
import lombok.Data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 整体思路就是   把原生的Connection 进行一次封装  加上一些属性，包括是否繁忙 等属性
 * 然后获取连接的时候，加一些动态扩容 等待等操作
 */
@Data
public class DefaultDataSource extends AbstractDataSource implements PoolDataSource {

    private ConnectionPoolConfig connectionPoolConfig;


    private List<IPooledConnection> pooledConnectionList;


    public void init() throws ClassNotFoundException {
        if (this.connectionPoolConfig == null) {
            throw new RuntimeException("数据源配置缺失");
        }
        Class.forName("com.mysql.cj.jdbc.Driver");

        initDataBaseConnectionPool();
    }

    /**
     * 初始化数据库连接池
     */
    private void initDataBaseConnectionPool() {
        final int minSize = this.connectionPoolConfig.getMinSize();
        pooledConnectionList = new ArrayList<>(minSize);
        for (int i = 0; i < minSize; i++) {
            pooledConnectionList.add(createDataBaseConnectionPool());
        }
    }


    private IPooledConnection createDataBaseConnectionPool () {
        Connection connection = createConnection();

        IPooledConnection pooledConnection = new PooledConnection();
        pooledConnection.setBusy(false);
        pooledConnection.setConnection(connection);
        pooledConnection.setDataSource(this);
        return pooledConnection;
    }


    private Connection createConnection ()  {
        try {
            return DriverManager.getConnection(this.connectionPoolConfig.getJdbcUrl(), this.connectionPoolConfig.getUser(), this.connectionPoolConfig.getPassword());
        } catch (SQLException e) {
            throw new RuntimeException("创建连接异常");
        }

    }

    @Override
    public synchronized void returnConnection(IPooledConnection IPooledConnection)  {
        if (connectionPoolConfig.isTestOnReturn()) {
            checkValid(IPooledConnection);
        }
        IPooledConnection.setBusy(false);
        // 通知其它的线程
        notifyAll();
    }

    private void checkValid(IPooledConnection pooledConnection) {
        if (this.connectionPoolConfig.getValidQuery() != null) {
            // 这个是真正的连接
            try {
                Connection connection = pooledConnection.getConnection();
                if (!connection.isValid(this.connectionPoolConfig.getValidTimeOutSeconds())) {
                    Connection newConnection = createConnection();
                    pooledConnection.setConnection(newConnection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("校验sql为空，跳过校验");
        }
    }

    @Override
    public IPooledConnection getConnection() throws SQLException {
        IPooledConnection connection = getFreeConnectionFromPool();
        if (connection != null) {
            return connection;
        }
        if (this.pooledConnectionList.size() > this.connectionPoolConfig.getMaxSize()) {
            if (this.connectionPoolConfig.getMaxWaitMills() <= 0) {
                throw new RuntimeException("从连接池获取连接失败");
            }
            // 开始等待空闲连接出现
        } else {
            // 扩容，而且只扩容一个
            IPooledConnection pooledConnection = createDataBaseConnectionPool();
            pooledConnection.setBusy(true);
            this.pooledConnectionList.add(pooledConnection);
            return pooledConnection;
        }
        return null;
    }

    private IPooledConnection getFreeConnectionFromPool() {
        for (IPooledConnection connection : this.pooledConnectionList) {
            if (!connection.isBusy()) {
                connection.setBusy(true);
                return connection;
            }
        }
        return null;
    }
}
