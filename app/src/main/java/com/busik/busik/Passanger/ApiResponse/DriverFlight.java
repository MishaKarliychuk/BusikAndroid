package com.busik.busik.Passanger.ApiResponse;



import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverFlight implements Comparable<DriverFlight>{

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
    @SerializedName("max_kg_package")
    @Expose
    private Integer maxKgPackage;
    @SerializedName("price_1_kg_package")
    @Expose
    private Object price1KgPackage;
    @SerializedName("max_count_person")
    @Expose
    private Integer maxCountPerson;
    @SerializedName("price_1_person")
    @Expose
    private Double price1Person;
    @SerializedName("passengers")
    @Expose
    private List<Passenger> passengers = null;
    @SerializedName("details")
    @Expose
    private String details;
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

    public Integer getMaxKgPackage() {
        return maxKgPackage;
    }

    public void setMaxKgPackage(Integer maxKgPackage) {
        this.maxKgPackage = maxKgPackage;
    }

    public Object getPrice1KgPackage() {
        return price1KgPackage;
    }

    public void setPrice1KgPackage(Object price1KgPackage) {
        this.price1KgPackage = price1KgPackage;
    }

    public Integer getMaxCountPerson() {
        return maxCountPerson;
    }

    public void setMaxCountPerson(Integer maxCountPerson) {
        this.maxCountPerson = maxCountPerson;
    }

    public Double getPrice1Person() {
        return price1Person;
    }

    public void setPrice1Person(Double price1Person) {
        this.price1Person = price1Person;
    }

    public List<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<Passenger> passengers) {
        this.passengers = passengers;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
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
    public int compareTo(DriverFlight o) {
        return getDateSort().compareTo(o.getDateSort());
    }


    @Override
    public String toString() {
        return "DriverFlight{" +
                "id=" + id +
                ", driver=" + driver +
                ", cityFrom='" + cityFrom + '\'' +
                ", countryFrom='" + countryFrom + '\'' +
                ", cityTo='" + cityTo + '\'' +
                ", countryTo='" + countryTo + '\'' +
                ", dateDeparture='" + dateDeparture + '\'' +
                ", dateArrival='" + dateArrival + '\'' +
                ", maxKgPackage=" + maxKgPackage +
                ", price1KgPackage=" + price1KgPackage +
                ", maxCountPerson=" + maxCountPerson +
                ", price1Person=" + price1Person +
                ", passengers=" + passengers +
                ", details='" + details + '\'' +
                ", archived=" + archived +
                ", status='" + status + '\'' +
                ", dateSort=" + dateSort +
                '}';
    }
}