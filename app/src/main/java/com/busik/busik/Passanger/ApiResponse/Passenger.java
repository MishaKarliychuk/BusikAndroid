package com.busik.busik.Passanger.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Passenger implements Comparable<Passenger>{

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
    @SerializedName("live_city")
    @Expose
    private String liveCity;
    @SerializedName("live_country")
    @Expose
    private String liveCountry;
    @SerializedName("rating")
    @Expose
    private Integer rating;
    @SerializedName("application")
    @Expose
    private Application application;


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

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    @Override
    public String toString() {
        return "Passenger{" +
                "id=" + id +
                ", fio='" + fio + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", liveCity='" + liveCity + '\'' +
                ", liveCountry='" + liveCountry + '\'' +
                ", rating=" + rating +
                ", application=" + application +
                '}';
    }

    @Override
    public int compareTo(Passenger o) {
        return getDateSort().compareTo(o.getDateSort());
    }
}