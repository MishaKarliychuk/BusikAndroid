package com.busik.busik.Passanger.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class PassengerMessage implements Comparable<PassengerMessage>{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("message_text")
    @Expose
    private String messageText;
    @SerializedName("type_message")
    @Expose
    private String typeMessage;
    @SerializedName("app_data")
    @Expose
    private String appData;
    @SerializedName("date_created")
    @Expose
    private String dateCreated;

    private Date dateSort;

    public Date getDateSort() {
        return dateSort;
    }

    public void setDateSort(Date dateSort) {
        this.dateSort = dateSort;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTypeMessage() {
        return typeMessage;
    }

    public void setTypeMessage(String typeMessage) {
        this.typeMessage = typeMessage;
    }

    public String getAppData() {
        return appData;
    }

    public void setAppData(String appData) {
        this.appData = appData;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @Override
    public int compareTo(PassengerMessage o) {
        return getDateSort().compareTo(o.getDateSort());
    }

    @Override
    public String toString() {
        return "PassengerMessage{" +
                "id=" + id +
                ", messageText='" + messageText + '\'' +
                ", typeMessage='" + typeMessage + '\'' +
                ", appData='" + appData + '\'' +
                ", dateCreated='" + dateCreated + '\'' +
                ", dateSort='" + dateSort + '\'' +
                '}';
    }
}