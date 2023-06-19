package com.lee.dbpool.common;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Demo
 * @description demo
 * @author lihuanmin
 * @date 2022/6/14 18:00
 * @version 1.0
 */
public class Demo {

    public static void main(String[] args) throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "lihuanminlee");

        PreparedStatement preparedStatement = connection.prepareStatement("select * from t_user");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("id：" + resultSet.getString("id") + "，username：" + resultSet.getString("username"));
        }
    }

    @Test
    public void method () throws SQLException {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("lihuanminlee");
        dataSource.setPort(3306);
        dataSource.setDatabaseName("demo");
        dataSource.setServerName("localhost");

        Connection connection = dataSource.getConnection();

        PreparedStatement preparedStatement = connection.prepareStatement("select * from t_user");

        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            System.out.println("id：" + resultSet.getString("id") + "，username：" + resultSet.getString("username"));
        }

    }
}

