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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.CancelMyReq;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.ApiResponse.MyApplication;
import com.busik.busik.Passanger.ApiResponse.UpdateFlight;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowMyTripReqFragment extends Fragment implements IOnBackPressed ,SwipeRefreshLayout.OnRefreshListener{
    TextView cityDepart,cityArrive,timeDepart,timeArrive,dateDepart,status,priceP,priceL,description,reqStatus,countPeople,countLaggage;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    LinearLayout back;
    String tripStatus;
    String token;
    int idF;
    RelativeLayout driver;
    TextView driverName;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    int idReq;
    Button cancel,makeReq;
    FullDetail f;
    int appId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_my_trip_req, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        idF= ((PassangerMainActivity) inflater.getContext()).getIdShowedTrip();
        appId= ((PassangerMainActivity) inflater.getContext()).getIdAppShowed();

        cityDepart=view.findViewById(R.id.tv_city_depert);
        cityArrive=view.findViewById(R.id.tv_city_arrive);
        timeArrive=view.findViewById(R.id.tv_time_arrive);
        mSwipeRefreshLayout=view.findViewById(R.id.swipe_container);
        timeDepart=view.findViewById(R.id.tv_time_depart);
        dateDepart=view.findViewById(R.id.tv_date_depart);
        status=view.findViewById(R.id.tv_status);
        back=view.findViewById(R.id.ll_back);
        priceP=view.findViewById(R.id.tv_price_passange);
        priceL=view.findViewById(R.id.tv_price_laggage);
        description=view.findViewById(R.id.tv_description);
        cancel=view.findViewById(R.id.cv_passangers);
        makeReq=view.findViewById(R.id.cv_make_req);
        reqStatus=view.findViewById(R.id.tv_req_status);
        countPeople=view.findViewById(R.id.tv_count_people);
        driver=view.findViewById(R.id.rl_driver);
        driverName=view.findViewById(R.id.tv_driver_name);
        countLaggage=view.findViewById(R.id.tv_count_laggage);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        cancel.setVisibility(View.GONE);
        makeReq.setVisibility(View.GONE);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PassangerMainActivity)getActivity()).setisAllBuses(true);
                getActivity().onBackPressed();
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
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
                                        reqStatus.setText("Статус заявки: Отменен");
                                        cancel.setVisibility(View.GONE);
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

        makeReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PassangerMainActivity)getActivity()).setCurentTrip(f);
                ((PassangerMainActivity)getActivity()).replaceFragment(new AddTropRequestFragment());
            }
        });

        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(f.getDriver()!=null){
                    ((PassangerMainActivity)getActivity()).setDriver(f.getDriver());
                    ((PassangerMainActivity)getActivity()).replaceFragment(new DriverDetailsFragment());
                }
            }
        });

        getTripDetail();


        return view;
    }

    void getReq(){
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("flight", String.valueOf(idF))
                .build();
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<List<MyApplication>> call = getResponse.myApplication("Token "+token,requestBody);
        call.enqueue(new Callback<List<MyApplication>>() {
            @Override
            public void onResponse(Call<List<MyApplication>> call, Response<List<MyApplication>> response) {
                if(response.code()==200){
                    for(int i=0;i<response.body().size();i++){
                        if(appId==0){
                            if(response.body().get(i).getStatus().equals("approved")){
                                if(tripStatus.equals("waiting")){
                                    if(response.body().get(i).getStatus().equals("waiting")){
                                        cancel.setVisibility(View.VISIBLE);
                                        makeReq.setVisibility(View.GONE);
                                    }
                                    if(response.body().get(i).getStatus().equals("approved")){
                                        cancel.setVisibility(View.VISIBLE);
                                        makeReq.setVisibility(View.GONE);
                                    }
                                    if(response.body().get(i).getStatus().equals("cancelled")){
                                        makeReq.setVisibility(View.VISIBLE);
                                        cancel.setVisibility(View.GONE);
                                    }
                                    if(response.body().get(i).getStatus().equals("removed")){
                                        makeReq.setVisibility(View.VISIBLE);
                                        cancel.setVisibility(View.GONE);
                                    }
                                }
                                else {
                                    makeReq.setVisibility(View.GONE);
                                    cancel.setVisibility(View.GONE);

                                }
                                switch (response.body().get(i).getStatus()){
                                    case("waiting"):{
                                        reqStatus.setText("Статус заявки: Ждет одобрения");
                                        break;
                                    }
                                    case("approved"):{
                                        reqStatus.setText("Статус заявки: Одобрен");
                                        break;
                                    }
                                    case("cancelled"):{
                                        reqStatus.setText("Статус заявки: Отменен");
                                        break;
                                    }
                                    case("removed"):{
                                        reqStatus.setText("Статус заявки: Удален");
                                        break;
                                    }
                                }
                                countPeople.setText("Людей: "+response.body().get(i).getCountPerson());
                                countLaggage.setText("Вантажу: "+response.body().get(i).getCountKgPackage()+" кг");
                                break;
                            }
                        }
                        else if(response.body().get(i).getId()==appId){
                            idReq=response.body().get(i).getId();
                            if(tripStatus.equals("waiting")){
                                if(response.body().get(i).getStatus().equals("waiting")){
                                    cancel.setVisibility(View.VISIBLE);
                                    makeReq.setVisibility(View.GONE);
                                }
                                if(response.body().get(i).getStatus().equals("approved")){
                                    cancel.setVisibility(View.VISIBLE);
                                    makeReq.setVisibility(View.GONE);
                                }
                                if(response.body().get(i).getStatus().equals("cancelled")){
                                    makeReq.setVisibility(View.VISIBLE);
                                    cancel.setVisibility(View.GONE);
                                }
                                if(response.body().get(i).getStatus().equals("removed")){
                                    makeReq.setVisibility(View.VISIBLE);
                                    cancel.setVisibility(View.GONE);
                                }
                            }
                            switch (response.body().get(i).getStatus()){
                                case("waiting"):{
                                    reqStatus.setText("Статус заявки: Ждет одобрения");
                                    break;
                                }
                                case("approved"):{
                                    reqStatus.setText("Статус заявки: Одобрен");
                                    break;
                                }
                                case("cancelled"):{
                                    reqStatus.setText("Статус заявки: Отменен");
                                    break;
                                }
                                case("removed"):{
                                    reqStatus.setText("Статус заявки: Удален");
                                    break;
                                }
                            }
                            countPeople.setText("Людей: "+response.body().get(i).getCountPerson());
                            countLaggage.setText("Вантажу: "+response.body().get(i).getCountKgPackage()+" кг");
                        }
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


    @Override
    public void onRefresh() {
        getTripDetail();
    }


    void getTripDetail(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<FullDetail> call = getResponse.getFullDetail("Token "+token,idF);
        call.enqueue(new Callback<FullDetail>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<FullDetail> call, Response<FullDetail> response) {
                if(response.code()==200){
                    f=response.body();
                    driverName.setText("Перевізник "+f.getDriver().getFio());


                    cityDepart.setText(response.body().getCityFrom()+", "+response.body().getCountryFrom());
                    cityArrive.setText(response.body().getCityTo()+", "+response.body().getCountryTo());

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
                    description.setText(response.body().getDetails());


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
                    if(m<10){
                        MMMM="0"+m;
                    }
                    else {
                        MMMM=m+"";
                    }
                    dM=c1.get(Calendar.DAY_OF_MONTH);
                    if(dM<10){
                        dayM="0"+dM;
                    }
                    else {
                        dayM=dM+"";
                    }
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

                    tripStatus=response.body().getStatus();
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
                    getReq();
                }
            }
            @Override
            public void onFailure(Call<FullDetail> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onBackPressed() {
        ((PassangerMainActivity)getActivity()).setisAllBuses(true);
        return false;
    }
}