package com.hou;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class conGetter {
    private static Connection con;

    private static void init() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost/data?serverTimezone=UTC";
        //mysql登录名
        String user = "root";
        //mysql登录密码（写自己之前设置的）
        String pass = "hgl@1519";
        con = DriverManager.getConnection(url, user, pass);
    }

    public static Connection getCon() throws SQLException, ClassNotFoundException {
        init();
        return con;
    }
}


