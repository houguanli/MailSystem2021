package com.hou.mail.response;

import ConnectionGetter.conGetter;
import com.hou.mail.bean.Drift;
import com.hou.mail.bean.FileMes;
import com.hou.mail.bean.Mail;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class DriftRes {
    private static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static int getNewestDriftID(){
        String sql = "select max(id) ma from mail where type=3";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
//                 System.out.println(resultSet);
                return resultSet.getInt("ma");
//                resultSet.next();
            } else{
                return -2;
            }
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return -1;
        }
    }

    public static int updateMetaDrift(int type) {
        String pre = "select * from drift where id=0", sql = "update drift set realID=? where id=0";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(pre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {//ok to get meta
                int maxSize = resultSet.getInt("realID");
                maxSize = type == 1 ? maxSize - 1 : maxSize + 1;
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, maxSize);
                int cnt = preparedStatement.executeUpdate();
                if (cnt != 1) {
                    System.out.println("ERR AT update too much or too less " + cnt);
                    return -2;
                }
                return maxSize;
//                resultSet.next();
            } else {//no meta info? impossible!
                System.out.println("NO META INFO!!!");
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("SQL ERR AT UPDATE META DRIFT!");
            e.printStackTrace();
            return -1;
        }
    }

    public static int insertNewDrift(Drift drift) {
        int upRes = updateMetaDrift(2);
        String sql = "insert into drift (id, realID) values(?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,upRes);
            preparedStatement.setInt(2, drift.getRealID());
            if(upRes > 0){
                int res = preparedStatement.executeUpdate();
                if (res != 1) {
                    if (res > 1) {
                        System.out.println("too many update");
                        return 1;
                    } else return 2;
                } else return 0;
            }else {
                System.out.println("META INFO UPDATE FALSE!");
                return -1;
            }
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return -1;
        }
    }

    public static Drift getRandomDrift() {
        String pre = "select * from drift where id=0", sql = "select * from drift where id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(pre);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {//ok to get meta
                int maxSize = resultSet.getInt("realID");
                int ra = (new Random().nextInt() % maxSize + maxSize) % maxSize + 1;
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setInt(1, ra);
                ResultSet res = preparedStatement.executeQuery();
                if (res.next()) {
                    int realID = res.getInt("realID");
                    return new Drift(ra, realID);
                } else {//no meta info? impossible!
                    System.out.println("NO DRIFT IN DATA BASE");
                    return null;
                }
//                resultSet.next();
            } else {//no meta info? impossible!
                throw new SQLException();
            }
        } catch (SQLException e) {
            System.out.println("SQL ERR AT UPDATE META DRIFT!");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * TODO update old drift, deal by manager
     *
     * @param drift: new drift info
     * @return return value expass the update status
     */
    public static int updateDrift(Drift drift) {
        return 0;
    }

}
