package com.hou.mail.bean;

public class FileMes {
    private String hash, name, size, path;

    public FileMes(String hash, String name, String size, String path) {
        this.hash = hash;
        this.name = name;
        this.size = size;
        this.path = path;
    }

    private int id;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
