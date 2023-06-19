package com.souce.yucatan.connection;



import com.souce.yucatan.datasource.PoolDataSource;

import java.sql.Connection;

public interface IPooledConnection extends Connection {

    boolean isBusy();

    void setBusy(boolean busy);

    Connection getConnection();

    void setConnection(Connection connection);

    void setDataSource(final PoolDataSource dataSource);
}
