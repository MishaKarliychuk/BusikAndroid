package com.busik.busik.Passanger.ApiResponse;


import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Buses implements Comparable<Buses>{

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("driver")
    @Expose
    private Integer driver;
    @SerializedName("city_from")
    @Expose
    private String cityFrom;
    @SerializedName("country_from")
    @Expose
    private String countryFrom;
    @SerializedName("city_to")
    @Expose
    private String cityTo;
    @SerializedName("country_to")
    @Expose
    private String countryTo;
    @SerializedName("date_departure")
    @Expose
    private String dateDeparture;
    @SerializedName("date_arrival")
    @Expose
    private String dateArrival;
    @SerializedName("price_1_kg_package")
    @Expose
    private Double price1KgPackage;
    @SerializedName("price_1_person")
    @Expose
    private Double price1Person;
    @SerializedName("details")
    @Expose
    private String details;
    @SerializedName("passengers")
    @Expose
    private List<Integer> passengers = null;
    @SerializedName("archived")
    @Expose
    private Boolean archived;
    @SerializedName("status")
    @Expose
    private String status;

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

    public Integer getDriver() {
        return driver;
    }

    public void setDriver(Integer driver) {
        this.driver = driver;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCountryFrom() {
        return countryFrom;
    }

    public void setCountryFrom(String countryFrom) {
        this.countryFrom = countryFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getCountryTo() {
        return countryTo;
    }

    public void setCountryTo(String countryTo) {
        this.countryTo = countryTo;
    }

    public String getDateDeparture() {
        return dateDeparture;
    }

    public void setDateDeparture(String dateDeparture) {
        this.dateDeparture = dateDeparture;
    }

    public String getDateArrival() {
        return dateArrival;
    }

    public void setDateArrival(String dateArrival) {
        this.dateArrival = dateArrival;
    }

    public Double getPrice1KgPackage() {
        return price1KgPackage;
    }

    public void setPrice1KgPackage(Double price1KgPackage) {
        this.price1KgPackage = price1KgPackage;
    }

    public Double getPrice1Person() {
        return price1Person;
    }

    public void setPrice1Person(Double price1Person) {
        this.price1Person = price1Person;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<Integer> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Integer> passengers) {
        this.passengers = passengers;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int compareTo(Buses o) {
        return getDateSort().compareTo(o.getDateSort());
    }

    @Override
    public String toString() {
        return "Buses{" +
                "id=" + id +
                ", driver=" + driver +
                ", cityFrom='" + cityFrom + '\'' +
                ", countryFrom='" + countryFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", countryTo='" + countryTo + '\'' +
                ", dateDeparture='" + dateDeparture + '\'' +
                ", dateArrival='" + dateArrival + '\'' +
                ", price1KgPackage=" + price1KgPackage +
                ", price1Person=" + price1Person +
                ", details='" + details + '\'' +
                ", passengers=" + passengers +
                ", archived=" + archived +
                ", status='" + status + '\'' +
                ", dateSort=" + dateSort +
                '}';
    }
}