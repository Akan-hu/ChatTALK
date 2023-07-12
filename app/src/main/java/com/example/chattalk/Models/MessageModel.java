package com.example.chattalk.Models;

import java.util.Map;

public class MessageModel {
    String uId, message;
  //  Long timeStamp;
   Map<String, String> timestamp;


    public MessageModel(String uId, String message, Map<String, String> timestamp) {
        this.uId = uId;
        this.message = message;
        this.timestamp = timestamp;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }
    public MessageModel(){

    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setTimestamp(Map<String, String> timeStamp) {
        this.timestamp= timestamp;}
    public Map<String, String> getTimestamp() {return timestamp;}
}

  /*  public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }
}

   */
