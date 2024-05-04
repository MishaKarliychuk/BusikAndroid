package com.busik.busik.Guess;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.MyApp;
import com.busik.busik.Passanger.ApiResponse.Driver;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GuestTripDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    Button add;
    LinearLayout driver;
    int tripId;

    TextView driverName,cityDepart,cityArrive,timeDepart,timeArrive,dateDepart,status,inTravel,priceP,priceL,driverRating;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    LinearLayout back;
    SwipeRefreshLayout swipeRefreshLayout;
    String token;
    Driver dr;
    FullDetail f;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_trip_detail, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = getActivity().getResources().getString(R.string.guess_token);

        add=view.findViewById(R.id.cv_add);
        driverName=view.findViewById(R.id.tv_driver_name);
        driver=view.findViewById(R.id.ll_driver);
        cityDepart=view.findViewById(R.id.tv_city_depert);
        cityArrive=view.findViewById(R.id.tv_city_arrive);
        timeArrive=view.findViewById(R.id.tv_time_arrive);
        timeDepart=view.findViewById(R.id.tv_time_depart);
        dateDepart=view.findViewById(R.id.tv_date_depart);
        status=view.findViewById(R.id.tv_status);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        inTravel=view.findViewById(R.id.tv_in_travel);
        back=view.findViewById(R.id.ll_back);
        priceP=view.findViewById(R.id.tv_price_passange);
        priceL=view.findViewById(R.id.tv_price_laggage);
        driverRating=view.findViewById(R.id.tv_driver_rating);
        swipeRefreshLayout.setOnRefreshListener(this);

        tripId=((GuessMainActivity)getActivity()).gettripDetailId();

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.setCurentTrip(f);
                ((GuessMainActivity)getActivity()).setBus(true);
                ((GuessMainActivity)getActivity()).replaceFragment(new GuestAuthFragment());
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dr!=null){
                    ((GuessMainActivity)getActivity()).setDriver(dr);
                    ((GuessMainActivity)getActivity()).replaceFragment(new GuestDriverDetailsFragment());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        getTripDetail();




        return view;
    }

    void findDifference(Date d1, Date d2)
    {
        long difference_In_Time
                = d2.getTime() - d1.getTime();

        // Calucalte time difference in seconds,
        // minutes, hours, years, and days
        long difference_In_Seconds
                = TimeUnit.MILLISECONDS
                .toSeconds(difference_In_Time)
                % 60;

        long difference_In_Minutes
                = TimeUnit
                .MILLISECONDS
                .toMinutes(difference_In_Time)
                % 60;

        long difference_In_Hours
                = TimeUnit
                .MILLISECONDS
                .toHours(difference_In_Time)
                % 24;

        long difference_In_Days
                = TimeUnit
                .MILLISECONDS
                .toDays(difference_In_Time)
                % 365;

        long difference_In_Years
                = TimeUnit
                .MILLISECONDS
                .toDays(difference_In_Time)
                / 365l;

        // Print the date difference in
        // years, in days, in hours, in
        // minutes, and in seconds

        // Print result

        if(difference_In_Days>0){
            inTravel.setText("В дорозі "+difference_In_Days+" день "+difference_In_Hours+" годин");
        }
        else {
            inTravel.setText("В дорозі "+difference_In_Hours+" годин");
        }
    }


    void getTripDetail(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<FullDetail> call = getResponse.getFullDetail("Token "+token,tripId);
        call.enqueue(new Callback<FullDetail>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<FullDetail> call, Response<FullDetail> response) {
                if(response.code()==200){
                    f=response.body();

                    dr=response.body().getDriver();
                    cityDepart.setText(response.body().getCityFrom()+", "+response.body().getCountryFrom());
                    cityArrive.setText(response.body().getCityTo()+", "+response.body().getCountryTo());

                    driverName.setText(response.body().getDriver().getFio());
                    driverRating.setText(response.body().getDriver().getRating().toString());

                    SimpleDateFormat output = new SimpleDateFormat("dd.MM HH.mm");
                    SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");

                    Date d = null;
                    SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        d=formatter6.parse(response.body().getDateDeparture());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date d1=d;

                    String formatted=output.format(d);
                    Calendar c1=Calendar.getInstance();
                    c1.setTime(d);
                    int dw=c1.get(Calendar.DAY_OF_WEEK);
                    int m=c1.get(Calendar.MONTH);
                    m++;
                    String dayWeek="";
                    switch (dw){
                        case (2):{
                            dayWeek="Пн";
                            break;
                        }
                        case (3):{
                            dayWeek="Вт";
                            break;
                        }
                        case (4):{
                            dayWeek="Ср";
                            break;
                        }
                        case (5):{
                            dayWeek="Чт";
                            break;
                        }
                        case (6):{
                            dayWeek="Пт";
                            break;
                        }
                        case (7):{
                            dayWeek="Сб";
                            break;
                        }
                        case (1):{
                            dayWeek="Вс";
                            break;
                        }
                    }


                    String Month="";
                    switch (m){
                        case (1):{
                            Month="Января";
                            break;
                        }
                        case (2):{
                            Month="Февраля";
                            break;
                        }
                        case (3):{
                            Month="Марта";
                            break;
                        }
                        case (4):{
                            Month="Апреля";
                            break;
                        }
                        case (5):{
                            Month="Май";
                            break;
                        }
                        case (6):{
                            Month="Июнь";
                            break;
                        }
                        case (7):{
                            Month="Июль";
                            break;
                        }
                        case (8):{
                            Month="Августа";
                            break;
                        }
                        case (9):{
                            Month="Сентября";
                            break;
                        }
                        case (10):{
                            Month="Октября";
                            break;
                        }
                        case (11):{
                            Month="Ноября";
                            break;
                        }
                        case (12):{
                            Month="Декаберя";
                            break;
                        }
                    }

                    String MMMM="";
                    if(m<10){
                        MMMM="0"+m;
                    }
                    else {
                        MMMM=m+"";
                    }
                    String f=output1.format(d);

                    int dM=c1.get(Calendar.DAY_OF_MONTH);
                    String dayM="";
                    if(dM<10){
                        dayM="0"+dM;
                    }
                    else {
                        dayM=dM+"";
                    }
                    int dh=d.getHours();
                    String dH="";
                    if(dh<10){
                        dH="0"+dh;
                    }
                    else {
                        dH=dh+"";
                    }
                    int dm=d.getMinutes();
                    String dM1="";
                    if(dm<10){
                        dM1="0"+dm;
                    }
                    else {
                        dM1=dm+"";
                    }
                    timeDepart.setText(dayM+"."+MMMM+" "+dayWeek+" "+dH+":"+dM1);
                    dateDepart.setText(dayM+" "+Month+" "+(d.getYear()+1900));



                    try {
                        d=formatter6.parse(response.body().getDateArrival());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    c1=Calendar.getInstance();
                    c1.setTime(d);
                    dw=c1.get(Calendar.DAY_OF_WEEK);
                    m=c1.get(Calendar.MONTH);
                    m++;
                    dM=c1.get(Calendar.DAY_OF_MONTH);
                    dayWeek="";
                    switch (dw){
                        case (2):{
                            dayWeek="Пн";
                            break;
                        }
                        case (3):{
                            dayWeek="Вт";
                            break;
                        }
                        case (4):{
                            dayWeek="Ср";
                            break;
                        }
                        case (5):{
                            dayWeek="Чт";
                            break;
                        }
                        case (6):{
                            dayWeek="Пт";
                            break;
                        }
                        case (7):{
                            dayWeek="Сб";
                            break;
                        }
                        case (1):{
                            dayWeek="Вс";
                            break;
                        }
                    }
                    MMMM="";
                    if(m<10){
                        MMMM="0"+m;
                    }
                    else {
                        MMMM=m+"";
                    }

                    if(dM<10){
                        dayM="0"+dM;
                    }
                    else {
                        dayM=dM+"";
                    }
                    dh=d.getHours();
                    dH="";
                    if(dh<10){
                        dH="0"+dh;
                    }
                    else {
                        dH=dh+"";
                    }
                    dm=d.getMinutes();
                    dM1="";
                    if(dm<10){
                        dM1="0"+dm;
                    }
                    else {
                        dM1=dm+"";
                    }

                    Date d2=d;

                    timeArrive.setText(dayM+"."+MMMM+" "+dayWeek+" "+dH+":"+dM1);

                    switch (response.body().getStatus()){
                        case("waiting"):{
                            status.setText("В очікуванні");
                            break;
                        }
                        case("on_the_way"):{
                            status.setText("В дорозі");
                            break;
                        }
                        case ("completed"):{
                            status.setText("Завершено");
                            break;
                        }
                        case ("cancelled"):{
                            status.setText("Не відбувся");
                            break;
                        }
                    }

                    findDifference(d1,d2);

                    if(response.body().getPrice1Person()==null){
                        priceP.setText("0€");
                    }
                    else {
                        priceP.setText("€"+response.body().getPrice1Person());
                    }
                    if(response.body().getPrice1KgPackage()==null){

                        priceL.setText("0€");
                    }
                    else {
                        priceL.setText("€"+response.body().getPrice1KgPackage());
                    }

                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FullDetail> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getTripDetail();
    }
}