package com.example.zzy4f5da2.mysqlconntest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by teacher on 2018/9/5.
 */

public class SQLMan {

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_USER = "root";
    public static final String MYSQL_PWD = "zzy2osc1314520";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName(MYSQL_DRIVER);
    Connection connection = DriverManager.getConnection("jdbc:mysql://cdb-bwb1p755.gz.tencentcdb.com:10032/system_db?characterEncoding=utf-8"
            ,MYSQL_USER,MYSQL_PWD);
    return connection;
    }
}
