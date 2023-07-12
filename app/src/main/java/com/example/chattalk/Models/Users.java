package com.example.chattalk.Models;

public class Users {
    String userPic;
    String userName;
    String mail;
    String password;
    String userId;
    String lastMessage;

    public Users(String userPic, String userName, String mail, String password, String userId, String lastMessage) {
        this.userPic = userPic;
        this.userName = userName;
        this.mail = mail;
        this.password = password;
        this.userId = userId;
        this.lastMessage = lastMessage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Users(){

    }

    //Constructor for signUp
    public Users(String userName, String mail, String password) {

        this.userName = userName;
        this.mail = mail;
        this.password = password;

    }

    public String getUserPic() {
        return userPic;
    }

    public void setUserPic(String userPic) {
        this.userPic = userPic;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
