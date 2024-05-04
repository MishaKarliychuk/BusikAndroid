package com.busik.busik.Passanger.Models;

import java.util.Calendar;

public class SearchModel {
    private String fromCountry;
    private String toCountry;
    private String fromCity;
    private String toCity;
    private Calendar dateDFrom,dateDTO;
    private Calendar dateTFrom,dateTTO;
    private int humanPriceFrom;
    private int humanPriceTo;
    private int laggagePriceFrom;
    private int laggagePriceTo;

    public SearchModel(String fromCountry, String toCountry, String fromCity, String toCity, Calendar dateDFrom, Calendar dateDTO, Calendar dateTFrom, Calendar dateTTO) {
        this.fromCountry = fromCountry;
        this.toCountry = toCountry;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.dateDFrom = dateDFrom;
        this.dateDTO = dateDTO;
        this.dateTFrom = dateTFrom;
        this.dateTTO = dateTTO;
    }

    public Calendar getDateDFrom() {
        return dateDFrom;
    }

    public void setDateDFrom(Calendar dateDFrom) {
        this.dateDFrom = dateDFrom;
    }

    public Calendar getDateDTO() {
        return dateDTO;
    }

    public void setDateDTO(Calendar dateDTO) {
        this.dateDTO = dateDTO;
    }

    public Calendar getDateTFrom() {
        return dateTFrom;
    }

    public void setDateTFrom(Calendar dateTFrom) {
        this.dateTFrom = dateTFrom;
    }

    public Calendar getDateTTO() {
        return dateTTO;
    }

    public void setDateTTO(Calendar dateTTO) {
        this.dateTTO = dateTTO;
    }

    public String getFromCountry() {
        return fromCountry;
    }

    public void setFromCountry(String fromCountry) {
        this.fromCountry = fromCountry;
    }

    public String getToCountry() {
        return toCountry;
    }

    public void setToCountry(String toCountry) {
        this.toCountry = toCountry;
    }

    public String getFromCity() {
        return fromCity;
    }

    public void setFromCity(String fromCity) {
        this.fromCity = fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public void setToCity(String toCity) {
        this.toCity = toCity;
    }

    public int getHumanPriceFrom() {
        return humanPriceFrom;
    }

    public void setHumanPriceFrom(int humanPriceFrom) {
        this.humanPriceFrom = humanPriceFrom;
    }

    public int getHumanPriceTo() {
        return humanPriceTo;
    }

    public void setHumanPriceTo(int humanPriceTo) {
        this.humanPriceTo = humanPriceTo;
    }

    public int getLaggagePriceFrom() {
        return laggagePriceFrom;
    }

    public void setLaggagePriceFrom(int laggagePriceFrom) {
        this.laggagePriceFrom = laggagePriceFrom;
    }

    public int getLaggagePriceTo() {
        return laggagePriceTo;
    }

    public void setLaggagePriceTo(int laggagePriceTo) {
        this.laggagePriceTo = laggagePriceTo;
    }

    @Override
    public String toString() {
        return "SearchModel{" +
                "fromCountry='" + fromCountry + '\'' +
                ", toCountry='" + toCountry + '\'' +
                ", fromCity='" + fromCity + '\'' +
                ", toCity='" + toCity + '\'' +
                ", dateDFrom=" + dateDFrom +
                ", dateDTO=" + dateDTO +
                ", dateTFrom=" + dateTFrom +
                ", dateTTO=" + dateTTO +
                ", humanPriceFrom=" + humanPriceFrom +
                ", humanPriceTo=" + humanPriceTo +
                ", laggagePriceFrom=" + laggagePriceFrom +
                ", laggagePriceTo=" + laggagePriceTo +
                '}';
    }
}
