package com.example.dev.kmv_mobile.models;

public class Message {
    String msg;
    String sender;
    String time;

    public Message(String msg, String sender, String time) {
        this.msg = msg;
        this.sender = sender;
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Message(String msg, String time) {
        this.msg = msg;
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
