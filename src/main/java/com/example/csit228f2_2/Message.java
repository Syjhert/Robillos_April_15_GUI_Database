package com.example.csit228f2_2;

public class Message {
    private int messageID;
    private int userID;
    private String content;
    private String dateTimeSent;
    public Message(int messageID, int userID, String content, String dateTimeSent){
        this.messageID = messageID;
        this.userID = userID;
        this.content = content;
        this.dateTimeSent = dateTimeSent;
    }
    public Message(int userID, String content, String dateTimeSent){    //for inserting
        messageID = -1;
        this.userID = userID;
        this.content = content;
        this.dateTimeSent = dateTimeSent;
    }

    public int getMessageID() {
        return messageID;
    }

    public int getUserID() {
        return userID;
    }

    public String getContent() {
        return content;
    }

    public String getDateTimeSent() {
        return dateTimeSent;
    }
}
