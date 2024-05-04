package com.busik.busik.Passanger.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Me {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("fio")
    @Expose
    private String fio;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("gmail_email")
    @Expose
    private String gmail_email;
    @SerializedName("fb_email")
    @Expose
    private String fb_email;
    @SerializedName("live_city")
    @Expose
    private String liveCity;
    @SerializedName("live_country")
    @Expose
    private String liveCountry;
    @SerializedName("rating")
    @Expose
    private Integer rating;

    public String getGmail_email() {
        return gmail_email;
    }

    public void setGmail_email(String gmail_email) {
        this.gmail_email = gmail_email;
    }

    public String getFb_email() {
        return fb_email;
    }

    public void setFb_email(String fb_email) {
        this.fb_email = fb_email;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLiveCity() {
        return liveCity;
    }

    public void setLiveCity(String liveCity) {
        this.liveCity = liveCity;
    }

    public String getLiveCountry() {
        return liveCountry;
    }

    public void setLiveCountry(String liveCountry) {
        this.liveCountry = liveCountry;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Me{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", gmail_email='" + gmail_email + '\'' +
                ", fb_email='" + fb_email + '\'' +
                ", liveCity='" + liveCity + '\'' +
                ", liveCountry='" + liveCountry + '\'' +
                ", rating=" + rating +
                '}';
    }
}