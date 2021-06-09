package com.hou.mail.response;

import ConnectionGetter.conGetter;
import com.hou.mail.bean.User;

import java.sql.*;
import java.util.ArrayList;

/**
 * this system make username=email
 */
public class UserRes {
    private final static String defaultNickname = "no_nickname";
    private static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String getNicknameByEmail(String email) {
        User user = getUserByEmail(email);
        assert user != null;
        return user.getNickname().equals("") ? defaultNickname : user.getNickname();
    }

    public static User getUserByEmail(String email) {
        String preTest = "select * from user where username=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(preTest);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("No such emailAddress in database");
                throw new SQLException();
            }
            String nickname = resultSet.getString("nickname");
            int id = resultSet.getInt("id"), written = resultSet.getInt("written");
            String metaBirthday = resultSet.getString("birthday");
//            String[] split = metaBirthday.split("-");
//            int year = Integer.parseInt(split[0]), month = Integer.parseInt(split[1]), day = Integer.parseInt(split[2]);
//            Calendar calendar = Calendar.getInstance();
//            calendar.set(year, month, day);
//            Date birthday = calendar.getTime();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            simpleDateFormat.format(birthday);
            return new User(email, nickname, metaBirthday, id, written);
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return null;
        }
    }

    public static User getUserByID(int id) {
        String preTest = "select * from user where id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(preTest);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                System.out.println("No such emailAddress in database");
                return null;
//                throw new SQLException();
            }
            String nickname = resultSet.getString("nickname");
            int written = resultSet.getInt("written");
            String metaBirthday = resultSet.getString("birthday"), email = resultSet.getString("username");
            return new User(email, nickname, metaBirthday, id, written);
        } catch (SQLException e) {
            System.out.println("SQL ERR");
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User> getFriendsByUserID(int id) {
        String sql = "select * from relation where selfID=?";
        ArrayList<User> res = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int friID = resultSet.getInt("friendID");
                try {
                    res.add(getUserByID(friID));
                } catch (NullPointerException e) {
                    System.out.println("NULL USer AT ID " + friID);
                }
            }
            return res;
        } catch (SQLException throwables) {
            System.out.println("sql ERR");
            throwables.printStackTrace();
            return null;
        }
    }

    public static ArrayList<User> findMailAddQZ(String add) {
        String sql = "select * from user where username like '" + add + "%'";
        return findMailAddByStr(sql);
    }

    public static ArrayList<User> findMailAddDeep(String add) {
        String sql = "select * from user where username like %" + add + "%";
        return findMailAddByStr(sql);
    }

    public static ArrayList<User> findMailAddByStr(String sql) {
        ArrayList<User> res = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String nickname = resultSet.getString("nickname");
                int written = resultSet.getInt("written"), id = resultSet.getInt("id");
                String metaBirthday = resultSet.getString("birthday"), email = resultSet.getString("username");
                res.add(new User(email, nickname, metaBirthday, id, written));
            }
            if (res.size() == 0) {
                System.out.println("No such emailAddress like in database");
            }
            return res;
        } catch (SQLException throwables) {
            System.out.println("sql ERR");
            throwables.printStackTrace();
            return null;
        }
    }

    public static int updateUser(User user) {
        String sql = "update user set nickname=?, birthday=? where id=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, user.getNickname());
            preparedStatement.setString(2, user.getBirthday());
            preparedStatement.setInt(3, user.getId());
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
    public static int updateUserMail(String old, String ne) {
        String sql = "update user set username=? where username=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, ne);
            preparedStatement.setString(2, old);
            int cnt = preparedStatement.executeUpdate();
            if (cnt == 1) {
                return 0;
            } else {
                System.out.println("upgrade err: no upgrade or too many, num " + cnt);
                return 1;
            }
        } catch (SQLException throwables) {
            System.out.println("sql ERR");
            throwables.printStackTrace();
            return -1;
        }
    }
}
