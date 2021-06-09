package com.hou.mail.response;

import ConnectionGetter.conGetter;
import com.hou.mail.bean.Mail;

import java.sql.*;
import java.util.ArrayList;

public class MailRes {
    private static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

//    public static ArrayList<Mail> getMailBySQL(String sql){}


    static ArrayList<Mail> getLimitedMailByTOID(int userId, int st) {
        String sql = "select * from mail where toID=? and status=" + st;
        return getMail(userId, sql);
    }

    static ArrayList<Mail> getLimitedMailByUserID(int userId, int st) {
        String sql = "select * from mail where fromID=? and status=" + st;
        return getMail(userId, sql);
    }

    static ArrayList<Mail> getUnLimitedMailByUserID(int userId) {
        String sql = "select * from mail where fromID=? and type!=-1";
        return getMail(userId, sql);
    }

    private static ArrayList<Mail> getMail(int userId, String sql) {
        ArrayList<Mail> res = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
//                System.out.println("add new mail!");
                String message = resultSet.getString("message"), title = resultSet.getString("title");
                int fromID = resultSet.getInt("fromID"), toID = resultSet.getInt("toID"),
                        status = resultSet.getInt("status"), type = resultSet.getInt("type"), mailID = resultSet.getInt("id");
                res.add(new Mail(fromID, toID, status, mailID, message, title, type));
//                resultSet.next();
            }
            return res;
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<Mail> getDraftMail(int userId) {
        return getLimitedMailByUserID(userId, 1);
    }

    public static ArrayList<Mail> getSentMail(int userId) {
        return getUnLimitedMailByUserID(userId);
    }

    public static ArrayList<Mail> getReceiveMail(int userId) {
        ArrayList<Mail> r1 = getLimitedMailByTOID(userId, 2), r2 = getLimitedMailByTOID(userId, 3);
        if (r1 == null || r2 == null) {
            System.out.println("SQL ERR AT GET MAIL");
            return null;
        } else {
            r2.addAll(r1);
            return r2;
        }
    }

    public static ArrayList<Mail> getDeletedMailByUserID(int userId) {
        return getLimitedMailByUserID(userId, 4);
    }

    public static ArrayList<Mail> getDeletedMailByToID(int userId) {
        return getLimitedMailByTOID(userId, 4);
    }

    public static Mail getMailByID(int mailID) {
        String sql = "select * from mail where id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, mailID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("No such email in database");
                throw new SQLException();
            }
            String message = resultSet.getString("message"), title = resultSet.getString("title");
            int fromID = resultSet.getInt("fromID"), toID = resultSet.getInt("toID")
                    , status = resultSet.getInt("status"), type = resultSet.getInt("type");
            return new Mail(fromID, toID, status, mailID, message, title, type);
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return null;
        }
    }

    public static int updateMailByID(int id, int status) {
        String sql = "update mail set status=? where id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, status);
            preparedStatement.setInt(2, id);
            int cnt = preparedStatement.executeUpdate();
            if (cnt == 1) {
                return 0;
            } else {
                System.out.println("upgrade err: no upgrade or too many");
                return 1;
            }
        } catch (SQLException throwables) {
            System.out.println("sql ERR");
            throwables.printStackTrace();
            return -1;
        }
    }

    public static int updateMailChangeMailToReadByID(int id) {
        return updateMailByID(id, 3);
    }

    public static int updateMailChangeMailToDeletedByID(int id) {
        return updateMailByID(id, 4);
    }

    public static int insertNewMail(Mail mail) {
        String sql = "insert into mail (fromID, toID, message, status, type, title) values(?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, mail.getFromId());
            preparedStatement.setInt(2, mail.getToId());
            preparedStatement.setString(3, mail.getMessage());
            preparedStatement.setInt(4, mail.getStatus());
            preparedStatement.setInt(5, mail.getType());
            preparedStatement.setString(6, mail.getTitle());
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
//        String preTest = "select username from user where username=?";

//        String sql =
//        return -1;
    }

    /**
     *
     * @param mail update everything expect id
     */
    public static int updateOldEmail(Mail mail){
        String sql = "update mail set fromID=?, toID=?, status=?, type=?, title=?, message=?  where id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, mail.getFromId());
            preparedStatement.setInt(2, mail.getToId());
            preparedStatement.setInt(3, mail.getStatus());
            preparedStatement.setInt(4, mail.getType());
            preparedStatement.setString(5, mail.getTitle());
            preparedStatement.setString(6, mail.getMessage());
            preparedStatement.setInt(7, mail.getId());
//            preparedStatement.setInt(2, id);
            int cnt = preparedStatement.executeUpdate();
            if (cnt == 1) {
                return 0;
            } else {
                System.out.println("upgrade err: no update or too many");
                return 1;
            }
        } catch (SQLException throwables) {
            System.out.println("sql ERR");
            throwables.printStackTrace();
            return -1;
        }
    }
}
