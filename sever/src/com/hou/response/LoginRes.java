package com.hou.response;

import com.hou.conGetter;
import com.hou.hashPakage.DigestUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginRes {
    static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static int login(String username, String password) {
        String sql = "select password, salt from user where username=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("no such username");
                return 1;
            }
            String dbPassword = resultSet.getString("password"), dbSalt = resultSet.getString("salt");
            System.out.println(dbPassword + '\n' + dbSalt);
            String ans = DigestUtil.shaDigest(password + dbSalt);
            System.out.println(ans);
            if (ans.equals(dbPassword)) {
                return 0;
            } else {
                System.out.println("wrong password");
                return 1;
            }
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
        }
        return -1;
    }
}
