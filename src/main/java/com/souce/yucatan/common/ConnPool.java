package com.lee.dbpool.common;

import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.LinkedList;
import java.util.Properties;

/**
 * ConnPool
 * @description 连接池
 * @author lihuanmin
 * @date 2022/7/11 8:43
 * @version 1.0
 */
public class ConnPool {
    private static LinkedList<Connection> connPool = new LinkedList<>();

    static {
        InputStream in = ConnPool.class.getClassLoader().getResourceAsStream("db.properties");
        Properties prop = new Properties();
        try {
            prop.load(in);
            String driver =  prop.getProperty("driver");
            String url = prop.getProperty("url");
            String user = prop.getProperty("user");
            String password = prop.getProperty("password");
            int initSize = 10;
            Class.forName("org.postgresql.Driver");
            for (int i = 0; i < initSize; i++) {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/db_pool", "lee", "tusc@6789#JKL");
                connPool.add(conn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws Exception {
        if (connPool.size() > 0) {
            final Connection conn = connPool.removeFirst();
            return (Connection) Proxy.newProxyInstance(ConnPool.class.getClassLoader(), conn.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    if (!"close".equals(method.getName())) {
                        return method.invoke(conn, args);
                    } else {
                        connPool.add(conn);
                        return null;
                    }
                }
            });
        } else {
            throw new RuntimeException("数据库繁忙");
        }
    }
}

