package com.busik.busik;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.Models.City;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    static int tripDetailId=0;

    static FullDetail curentTrip;
    static String google_email;
    static String google_name;
    static String fb_email;
    static boolean goToBus=false;
    static String fb_name;

    private static final String MY_SETTINGS = "TOKEN";
    boolean isCorect=true;
    SharedPreferences sp;
    static boolean fromGuest=false;

    private static boolean recB=false;
    @Override
    public void onCreate() {
        super.onCreate();
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);



    }

    public static boolean isGoToBus() {
        return goToBus;
    }

    public static void setGoToBus(boolean goToBus) {
        MyApp.goToBus = goToBus;
    }

    public static String getGoogle_email() {
        return google_email;
    }

    public static String getFb_email() {
        return fb_email;
    }

    public static void setFb_email(String fb_email) {
        MyApp.fb_email = fb_email;
    }

    public static String getFb_name() {
        return fb_name;
    }

    public static void setFb_name(String fb_name) {
        MyApp.fb_name = fb_name;
    }

    public static void setGoogle_email(String google_email) {
        MyApp.google_email = google_email;
    }

    public static String getGoogle_name() {
        return google_name;
    }

    public static void setGoogle_name(String google_name) {
        MyApp.google_name = google_name;
    }

    public static FullDetail getCurentTrip(){
        return curentTrip;
    }

    public static void setCurentTrip(FullDetail f){
        curentTrip=f;
    }



    public static int gettripDetailId(){
        return tripDetailId;
    }

    public static void setTripDetailId(int id){
        tripDetailId=id;
    }

    public static boolean isFromGuest() {
        return fromGuest;
    }

    public static void setFromGuest(boolean f) {
        fromGuest = f;
    }
}
