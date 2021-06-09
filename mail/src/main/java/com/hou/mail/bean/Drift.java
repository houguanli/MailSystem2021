package com.hou.mail.bean;

/**
 * TODO: this class calls the same table in the data base
 * the real id calls the true place in the mail table
 * the id=0 st does not contain any drift
 */
public class Drift {
    public Drift(int id, int realID) {
        this.id = id;
        this.realID = realID;
    }

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRealID() {
        return realID;
    }

    public void setRealID(int realID) {
        this.realID = realID;
    }

    private int realID;
}
