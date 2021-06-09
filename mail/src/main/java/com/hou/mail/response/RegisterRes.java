package com.hou.mail.response;


import ConnectionGetter.conGetter;
import com.hou.mail.hashPakage.DigestUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class RegisterRes {
    static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int register(String username, String password) {
        String sql = "insert into user (username, password, salt) values(?, ?, ?)";
        String preTest = "select username from user where username=?";
        String[] ans = codeHashing(password);
        try {
            PreparedStatement preparedStatement = con.prepareStatement(preTest);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("already exist user!");
                return 2;
            }
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, ans[0]);//pass hash
            preparedStatement.setString(3, ans[1]);//salt hash
            int res = preparedStatement.executeUpdate();
            if (res == 1) {
                return 0;
            } else if (res > 1) {
                System.out.println("too many update");
                return 1;
            } else return 2;
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return -1;
        }
    }

    private static String[] codeHashing(String org) {
        String salt = UUID.randomUUID().toString();
        org = DigestUtil.shaDigest(org + salt);
        System.out.println(salt + '\n' + org);
        return new String[]{org, salt};
    }
}
