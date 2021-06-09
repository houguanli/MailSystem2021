package com.hou.mail.response;

//import org.springframework.web.multipart.MultipartFile;

import ConnectionGetter.conGetter;
import com.hou.mail.bean.FileMes;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FileRes {
    private static Connection con;

    static {
        try {
            con = conGetter.getCon();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //upload a file and store the path to server
    private static final String uploadBase = System.getProperty("user.dir") + "\\files\\upload\\";
    private static final String downloadBase = System.getProperty("user.dir") + "\\files\\download\\";

    public static uploadInfo uploadMultiPartFile(MultipartFile file) {
        File res = new File(uploadBase + file.getOriginalFilename());
        String sql = "insert into file (hash, path, size, name) values(?, ?, ?, ?)";

        if (!res.exists()) res.mkdir();
        try {
            file.transferTo(res);
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            String hash = getFileHash(res), path = res.getAbsolutePath(), name = res.getName(), size = res.length() + "";
            preparedStatement.setString(1, hash);
            preparedStatement.setString(2, path);
            preparedStatement.setString(3, size);
            preparedStatement.setString(4, name);
            int ans = preparedStatement.executeUpdate();
            if (ans != 1) {
                System.out.println("ERR ON UPDATE");
                return new uploadInfo(null, -1);
            } else return new uploadInfo(new FileMes(hash, name, size, path), 0);
//            System.out.println("OK");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            System.out.println("傻逼服务器出错了！文件你妈的没传上！");
            return null;
        }
    }

    public static int uploadFile(byte[] file, String filePath, String fileName) {
        try {
            File file1 = new File(filePath + fileName);
            if (!file1.exists()) {
                boolean ans = file1.mkdirs();
                if (!ans) {
                    System.out.println("fail to make a file!");
                    return 1;
                }
            }
            FileOutputStream out = new FileOutputStream(filePath + fileName);
            out.write(file);
            out.flush();
            out.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    //download a file and store the path to server
    public static FileMes getFileMesByHash(String hash) {
        String sql = "select * from file where hash=?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, hash);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
//                System.out.println(resultSet.getString("name"));
                String size = resultSet.getString("size"), path = resultSet.getString("path"),
                        name = resultSet.getString("name");
                int id = resultSet.getInt("id");
                FileMes res = new FileMes(hash, name, size, path);
                res.setId(id);
                return res;
//                return resultSet.getString("name");
            } else {
                System.out.println("NO such file");
                return null;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public static int downloadFileInDefaultPath(String fileName, File src) {
        File target = new File(downloadBase + fileName);
        return downloadFile(src, target);
    }

    public static int downloadFile(File src, File target) {
        FileInputStream fileInputStream;
        BufferedInputStream bufferedInputStream;
        try {
            fileInputStream = new FileInputStream(src);
            bufferedInputStream = new BufferedInputStream(fileInputStream);
            FileOutputStream outputStream = new FileOutputStream(target, true);
            if (target.exists()) {
                if (FileRes.compareFile(src, target)) {
                    System.out.println("already ok");
                    return 0;
                } else {
                    System.out.println("replacing existed target");
                }
            } else target.createNewFile();
            byte[] buf = new byte[fileInputStream.available()];
            fileInputStream.read(buf);
            outputStream.write(buf);
            System.out.println("download complete!");
            bufferedInputStream.close();
            fileInputStream.close();
            return 0;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static boolean compareFile(File f1, File f2) {
        String s1 = getFileHash(f1), s2 = getFileHash(f2);
        System.out.println("get hash\n" +
                f1 + ": " + s1 + '\n' +
                f2 + ": " + s2);
        return s1.equals(s2);
    }

    public static String getFileHash(File file, String type) {
        if (!file.isFile()) {
            System.out.println("no such file");
            return null;
        }
        MessageDigest messageDigest;
        FileInputStream in = null;
        byte[] buffer = new byte[8192];
        int res;
        try {
            messageDigest = MessageDigest.getInstance(type);
            in = new FileInputStream(file);
            while ((res = in.read(buffer)) != -1) {
                messageDigest.update(buffer, 0, res);
            }
            return new BigInteger(1, messageDigest.digest()).toString(16);
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert in != null;
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getFileHash(File file) {
        return getFileHash(file, "MD5");
    }

    public static class uploadInfo {

        public FileMes fileMes;
        public int status;
        public String hash;

        public uploadInfo(FileMes fileMes, int status) {
            this.fileMes = fileMes;
            this.status = status;
            hash = fileMes == null ? null : fileMes.getHash();
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public FileMes getFileMes() {
            return fileMes;
        }

        public void setFileMes(FileMes fileMes) {
            this.fileMes = fileMes;
        }
    }
}