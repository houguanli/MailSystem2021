package com.hou.mail.bean;


public class Mail {
    private int fromId, toId, status, id, type;
    private String message, title;
    private boolean init = false;
    public int getFromId() {
        return fromId;
    }

    public void setFromId(int fromId) {
        this.fromId = fromId;
    }

    public int getToId() {
        return toId;
    }

    public void setToId(int toId) {
        this.toId = toId;
    }
    /**
     * the status of the email means it is
     * until be cleared, it will stay at database
     *
     * deleted 4 cannot change message any more, depend on database
     * been read, 3 only can be deleted
     * sent 2 only be del / rec , not change message
     * draft 1 not send, can change toID/message if need, need to be written in database
     * 0 default status, when first be created
     */
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * the value of the type means
     * 0 default when first created
     * -1 just a draft
     * 1 is a person mail
     * 2 is a group mail
     * 3 is a unkonwn sender mail //piao liu ping
     */
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    public Mail(int fromId, int toId, int status, int id, String message, String title, int type) {
        this.fromId = fromId;
        this.toId = toId;
        this.status = status;
        this.id = id;
        this.message = message;
        this.type = type;
        this.title = title;
    }
//    public Mail() {
//    }
    @Override
    public String toString() {
        return "Mail{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", status=" + status +
                ", id=" + id +
                ", title =" + title +
                 ", message='" + message + '\'' +
                '}';
    }

    public boolean isInit() {
        return init;
    }

    public void setInit(boolean init) {
        this.init = init;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
