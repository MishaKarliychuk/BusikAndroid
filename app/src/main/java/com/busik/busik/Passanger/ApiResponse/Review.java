package com.busik.busik.Passanger.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("driver")
    @Expose
    private String driver;
    @SerializedName("passenger")
    @Expose
    private String passenger;
    @SerializedName("review_text")
    @Expose
    private String reviewText;
    @SerializedName("datetime_created")
    @Expose
    private String datetimeCreated;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getPassenger() {
        return passenger;
    }

    public void setPassenger(String passenger) {
        this.passenger = passenger;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public String getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", driver=" + driver +
                ", passenger=" + passenger +
                ", reviewText='" + reviewText + '\'' +
                ", datetimeCreated='" + datetimeCreated + '\'' +
                '}';
    }
}