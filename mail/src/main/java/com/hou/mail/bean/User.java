package com.hou.mail.bean;

/**
 * in HEmail system, username means email
 */
public class User {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNickname() {
        return nickname.equals("") ? email : nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User(String email, String nickname, String birthday, int id, int written) {
        this.email = email;
        this.nickname = nickname;
        this.birthday = birthday;
        this.id = id;
        this.written = written;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", birthday=" + birthday +
                ", id=" + id +
                '}';
    }

    private String email, nickname;
    private String birthday;
    private int id, written;

    public int getWritten() {
        return written;
    }

    public void setWritten(int written) {
        this.written = written;
    }
}
