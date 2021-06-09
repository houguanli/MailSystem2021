package com.hou.mail.response;

import ConnectionGetter.conGetter;
import com.hou.mail.bean.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RelationRes {
    private static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int deleteFriendByID(int fromID, int targetID) {
        String preTest = "select * from relation where selfID=? and friendID=?", sql = "delete from relation where selfID=? and friendID=?";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = con.prepareStatement(preTest);
            preparedStatement.setInt(1, fromID);
            preparedStatement.setInt(2, targetID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("already deleted");
                return 0;
            }
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, fromID);
            preparedStatement.setInt(2, targetID);
            int res = preparedStatement.executeUpdate();
            if (res != 1) {
                System.out.println("unToken err on delete relation");
                return 1;
            }
            return 0;
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return -1;
        }
    }

    public static int addNewRelation(int fromID, int targetID) {
        if(fromID == targetID){
            System.out.println("One Cannot add himself!");
            return -1;
        }
        String preTest = "select * from relation where selfID=? and friendID=?", sql = "insert into relation (selfID, friendID) values(?, ?)";
        try {
            PreparedStatement preparedStatement;
            preparedStatement = con.prepareStatement(preTest);
            preparedStatement.setInt(1, fromID);
            preparedStatement.setInt(2, targetID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("already exist relation!");
                return 0;
            }
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, fromID);
            preparedStatement.setInt(2, targetID);
            int res = preparedStatement.executeUpdate();
            if (res != 1) {
                System.out.println("unToken err");
                return 1;
            }
            return 0;
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return -1;
        }
    }
}