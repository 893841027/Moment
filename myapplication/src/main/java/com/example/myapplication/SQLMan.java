package com.example.myapplication;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by teacher on 2018/9/5.
 */

public class SQLMan {

    public static final String MYSQL_DRIVER = "com.mysql.jdbc.Driver";
    public static final String MYSQL_USER = "root";
    public static final String MYSQL_PWD = "y6lNmSmAEzDxrKZ51DozHg== ";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
    Class.forName(MYSQL_DRIVER);
    Connection connection = DriverManager.getConnection("jdbc:mysql://cdb-bwb1p755.gz.tencentcdb.com:10032/system_db"
            ,MYSQL_USER,new Encrypt().decryptPassword(MYSQL_PWD));
    return connection;
    }
}
