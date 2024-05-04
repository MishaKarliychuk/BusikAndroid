package com.busik.busik.Passanger.ApiResponse;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Application {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("flight")
    @Expose
    private Integer flight;
    @SerializedName("passenger")
    @Expose
    private Integer passenger;
    @SerializedName("count_kg_package")
    @Expose
    private Integer countKgPackage;
    @SerializedName("count_person")
    @Expose
    private Integer countPerson;
    @SerializedName("datetime_created")
    @Expose
    private String datetimeCreated;
    @SerializedName("status")
    @Expose
    private String status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFlight() {
        return flight;
    }

    public void setFlight(Integer flight) {
        this.flight = flight;
    }

    public Integer getPassenger() {
        return passenger;
    }

    public void setPassenger(Integer passenger) {
        this.passenger = passenger;
    }

    public Integer getCountKgPackage() {
        return countKgPackage;
    }

    public void setCountKgPackage(Integer countKgPackage) {
        this.countKgPackage = countKgPackage;
    }

    public Integer getCountPerson() {
        return countPerson;
    }

    public void setCountPerson(Integer countPerson) {
        this.countPerson = countPerson;
    }

    public String getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(String datetimeCreated) {
        this.datetimeCreated = datetimeCreated;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Application{" +
                "id=" + id +
                ", flight=" + flight +
                ", passenger=" + passenger +
                ", countKgPackage=" + countKgPackage +
                ", countPerson=" + countPerson +
                ", datetimeCreated='" + datetimeCreated + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}