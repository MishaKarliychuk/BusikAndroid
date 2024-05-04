package com.busik.busik.Passanger.Fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.CancelMyReq;
import com.busik.busik.Passanger.ApiResponse.Driver;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.ApiResponse.MyApplication;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TripDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    Button add;
    LinearLayout driver;
    int tripId;

    TextView driverName,cityDepart,cityArrive,timeDepart,timeArrive,dateDepart,status,inTravel,priceP,priceL,driverRating,details;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    LinearLayout back;
    String token;
    int appId;
    Button cancelReq;
    SwipeRefreshLayout mSwipeRefreshLayout;
    Driver dr;
    FullDetail f;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_trip_detail, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        mSwipeRefreshLayout=view.findViewById(R.id.swipe_container);
        add=view.findViewById(R.id.cv_add);
        driverName=view.findViewById(R.id.tv_driver_name);
        driver=view.findViewById(R.id.ll_driver);
        cityDepart=view.findViewById(R.id.tv_city_depert);
        cityArrive=view.findViewById(R.id.tv_city_arrive);
        timeArrive=view.findViewById(R.id.tv_time_arrive);
        details=view.findViewById(R.id.tv_details);
        timeDepart=view.findViewById(R.id.tv_time_depart);
        dateDepart=view.findViewById(R.id.tv_date_depart);
        status=view.findViewById(R.id.tv_status);
        inTravel=view.findViewById(R.id.tv_in_travel);
        back=view.findViewById(R.id.ll_back);
        priceP=view.findViewById(R.id.tv_price_passange);
        priceL=view.findViewById(R.id.tv_price_laggage);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        driverRating=view.findViewById(R.id.tv_driver_rating);
        cancelReq=view.findViewById(R.id.cv_cancel_req);

        tripId=((PassangerMainActivity)getActivity()).gettripDetailId();






        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PassangerMainActivity)getActivity()).setCurentTrip(f);
                ((PassangerMainActivity)getActivity()).replaceFragment(new AddTropRequestFragment());
            }
        });

        cancelReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("");  // заголовок
                builder.setMessage("Ви бажаєте скасувати заявку?"); // сообщение
                builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                        Call<CancelMyReq> call = getResponse.cancelMyReq("Token "+token,appId);
                        call.enqueue(new Callback<CancelMyReq>() {
                            @Override
                            public void onResponse(Call<CancelMyReq> call, Response<CancelMyReq> response) {
                                if(response.code()==200){
                                    if(response.body().getStatus()){
                                        cancelReq.setVisibility(View.GONE);
                                    }
                                    else {
                                        Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<CancelMyReq> call, Throwable t) {

                            }
                        });
                    }
                });
                builder.setNegativeButton("Ні", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                builder.setCancelable(true);
                builder.show();
            }
        });


        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(dr!=null){
                    ((PassangerMainActivity)getActivity()).setDriver(dr);
                    ((PassangerMainActivity)getActivity()).replaceFragment(new DriverDetailsFragment());
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


    @Override
    public void onRefresh() {
        getTripDetail();
    }


    void getReq(){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("flight", String.valueOf(tripId))
                .build();
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<List<MyApplication>> call = getResponse.myApplication("Token "+token,requestBody);
        call.enqueue(new Callback<List<MyApplication>>() {
            @Override
            public void onResponse(Call<List<MyApplication>> call, Response<List<MyApplication>> response) {
                if(response.code()==200){
                    boolean canSend=true;
                    if(response.body().size()>0){
                        for(int i=0;i<response.body().size();i++){
//                            if(response.body().get(i).getStatus().equals("cancelled")||response.body().get(i).getStatus().equals("removed")){
                            if(response.body().get(i).getStatus().equals("waiting")||response.body().get(i).getStatus().equals("approved")){
                                canSend=false;
                                appId=response.body().get(i).getId();
                                break;

                            }
                        }
                    }
                    else {
                        canSend=true;
                    }
                    if(canSend){
                        add.setVisibility(View.VISIBLE);
                        cancelReq.setVisibility(View.GONE);
                    }
                    else {
                        cancelReq.setVisibility(View.VISIBLE);
                        add.setVisibility(View.GONE);
                    }
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<MyApplication>> call, Throwable t) {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
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

                    getReq();
                    dr=response.body().getDriver();
                    cityDepart.setText(response.body().getCityFrom()+", "+response.body().getCountryFrom());
                    cityArrive.setText(response.body().getCityTo()+", "+response.body().getCountryTo());

                    driverName.setText("Перевізник "+response.body().getDriver().getFio());
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
                    dateDepart.setText(dayM+" "+Month+ " "+c1.get(Calendar.YEAR));



                    try {
                        d=formatter6.parse(response.body().getDateArrival());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    c1=Calendar.getInstance();
                    c1.setTime(d);
                    dw=c1.get(Calendar.DAY_OF_WEEK);
                    m=c1.get(Calendar.MONTH);
                    dM=c1.get(Calendar.DAY_OF_MONTH);
                    if(dM<10){
                        dayM="0"+dM;
                    }
                    else {
                        dayM=dM+"";
                    }
                    m++;
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


                    if(m<10){
                        MMMM="0"+m;
                    }
                    else {
                        MMMM=m+"";
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

                    details.setText(response.body().getDetails());

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
                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FullDetail> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}