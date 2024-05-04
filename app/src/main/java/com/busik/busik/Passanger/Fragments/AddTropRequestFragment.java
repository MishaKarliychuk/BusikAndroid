package com.busik.busik.Passanger.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.ApplyFlight;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTropRequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    double priceHuman=0;
    double priceLaggage=0;
    int humanCount=0;
    int laggageCount=0;
    Button plusHuman,minusHuman;
    Button plusLaggage,minusLaggage;
    TextView humans;
    EditText laggage;
    TextView inResult;
    TextView predictPrice,predictLaggage;
    LinearLayout back;
    Button add;
    private static final String MY_SETTINGS = "TOKEN";
    int tripId;
    SharedPreferences sp;
    SwipeRefreshLayout mSwipeRefreshLayout;
    String token;

    FullDetail fullDetail;
    TextView cityDepart,cityArrive,timeDepart,timeArrive,dateDepart,driverName,driverRating;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_trop_request, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        tripId=((PassangerMainActivity)getActivity()).gettripDetailId();

        cityDepart=view.findViewById(R.id.tv_city_depart);
        cityArrive=view.findViewById(R.id.tv_city_arrive);
        back=view.findViewById(R.id.ll_back);
        timeDepart=view.findViewById(R.id.tv_time_depart);
        timeArrive=view.findViewById(R.id.tv_time_arrive);
        dateDepart=view.findViewById(R.id.tv_date_depart);
        driverName=view.findViewById(R.id.tv_driver_name);
        driverRating=view.findViewById(R.id.tv_driver_rating);
        mSwipeRefreshLayout=view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);

//        SimpleDateFormat output = new SimpleDateFormat("dd.MM HH.mm");
//        SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
//
//        Date d = null;
//        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//        try {
//            d=formatter6.parse(fullDetail.getDateDeparture());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        String formatted=output.format(d);
//
//        String f=output1.format(d);
//
//        timeDepart.setText(formatted);
//        dateDepart.setText(f);
//
//
//        try {
//            d=formatter6.parse(fullDetail.getDateArrival());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        formatted=output.format(d);
//
//        timeArrive.setText(formatted);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        plusHuman=view.findViewById(R.id.cv_plus_human);
        minusHuman=view.findViewById(R.id.cv_minus_human);
        plusLaggage=view.findViewById(R.id.cv_plus_laggage);
        minusLaggage=view.findViewById(R.id.cv_minus_laggage);
        humans=view.findViewById(R.id.tv_humans);
        laggage=view.findViewById(R.id.tv_laggage);
        inResult=view.findViewById(R.id.tv_inRes);
        add=view.findViewById(R.id.cv_add);
        predictPrice=view.findViewById(R.id.tv_predictPrice);
        predictLaggage=view.findViewById(R.id.tv_predictLaggage);

        plusHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(humanCount<99){
                    humanCount++;
                    humans.setText(humanCount+"");
                    predictPrice.setText(humanCount+" человек: €"+humanCount*priceHuman);
                    double rP=(humanCount*priceHuman)+(laggageCount*priceLaggage);
                    inResult.setText("Всього:  €"+rP);
                }
            }
        });


        minusHuman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(humanCount>0){
                    humanCount--;
                    humans.setText(humanCount+"");
                    predictPrice.setText(humanCount+" человек: €"+humanCount*priceHuman);
                    double rP=(humanCount*priceHuman)+(laggageCount*priceLaggage);
                    inResult.setText("Всього:  €"+rP);
                }
            }
        });

        plusLaggage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    laggageCount++;
                    laggage.setText(laggageCount+"");
                    predictLaggage.setText(laggageCount+" груза: €"+laggageCount*priceLaggage);
                    double rP=(humanCount*priceHuman)+(laggageCount*priceLaggage);
                    inResult.setText("Всього:  €"+rP);
            }
        });

        laggage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!editable.toString().equals("")){
                    laggageCount=Integer.parseInt(editable.toString());
                }
                else {
                    laggageCount=0;
                }
                predictLaggage.setText(laggageCount+" груза: €"+laggageCount*priceLaggage);
            }
        });

        minusLaggage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(laggageCount>0){
                    laggageCount--;
                    laggage.setText(laggageCount+"");
                    double rP=(humanCount*priceHuman)+(laggageCount*priceLaggage);
                    inResult.setText("Всього:  €"+rP);
                    predictLaggage.setText(laggageCount+" груза: €"+laggageCount*priceLaggage);
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(humanCount>0||laggageCount>0){
                    if(humanCount<=fullDetail.getMaxCountPerson()&&laggageCount<=fullDetail.getMaxKgPackage()){
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("flight_id", String.valueOf(fullDetail.getId()))
                                .addFormDataPart("count_person", String.valueOf(humanCount))
                                .addFormDataPart("count_kg_package", String.valueOf(laggageCount))
                                .build();

                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                        Call<ApplyFlight> call = getResponse.applyFlight("Token "+token,requestBody);
                        call.enqueue(new Callback<ApplyFlight>() {
                            @Override
                            public void onResponse(Call<ApplyFlight> call, Response<ApplyFlight> response) {
                                if(response.code()==200){
                                    if(response.body().getStatus()){
                                        ((PassangerMainActivity)getActivity()).replaceFragment(new RecSucFragment());
                                    }
                                    else {
                                        Toast.makeText(getActivity(), response.body().getError(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    Toast.makeText(getActivity(), "Помилка сервера: "+response.code(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<ApplyFlight> call, Throwable t) {

                            }
                        });
                    }
                    if(humanCount>fullDetail.getMaxCountPerson()){
                        Toast.makeText(getContext(),"Максимальное количество человек: "+fullDetail.getMaxCountPerson(),Toast.LENGTH_SHORT).show();
                    }
                    if(laggageCount>fullDetail.getMaxKgPackage()){
                        Toast.makeText(getContext(),"Максимальное количество груза: "+fullDetail.getMaxKgPackage(),Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getContext(),   "Заполните заявку", Toast.LENGTH_SHORT).show();
                }
            }
        });

        getTripDetail();

        return view;
    }


    void getTripDetail(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<FullDetail> call = getResponse.getFullDetail("Token "+token,tripId);
        call.enqueue(new Callback<FullDetail>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<FullDetail> call, Response<FullDetail> response) {
                if(response.code()==200){
                    fullDetail=response.body();

                    if(fullDetail.getMaxCountPerson()>0){
                        plusHuman.setClickable(true);
                        minusHuman.setClickable(true);
                    }
                    else {
                        plusHuman.setClickable(false);
                        minusHuman.setClickable(false);
                    }
                    if(fullDetail.getMaxKgPackage()>0){
                        plusLaggage.setClickable(true);
                        minusLaggage.setClickable(true);
                        laggage.setEnabled(true);
                    }
                    else {
                        plusLaggage.setClickable(false);
                        minusLaggage.setClickable(false);
                        laggage.setEnabled(false);
                    }

                    driverName.setText(fullDetail.getDriver().getFio());
                    driverRating.setText(fullDetail.getDriver().getRating().toString());

                    cityDepart.setText(fullDetail.getCityFrom()+", "+fullDetail.getCountryFrom());
                    cityArrive.setText(fullDetail.getCityTo()+", "+fullDetail.getCountryTo());


                    SimpleDateFormat output = new SimpleDateFormat("dd.MM HH.mm");
                    SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");

                    Date d = null;
                    SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        d=formatter6.parse(fullDetail.getDateDeparture());
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

                    String f=output1.format(d);

                    int dM=c1.get(Calendar.DAY_OF_MONTH);
                    String dayM="";
                    if(dM<0){
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
                    timeDepart.setText(dayWeek+" "+dH+":"+dM1);
                    dateDepart.setText(dM+" "+Month+" "+(d.getYear()+1900));



                    try {
                        d=formatter6.parse(fullDetail.getDateArrival());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    c1=Calendar.getInstance();
                    c1.setTime(d);
                    dw=c1.get(Calendar.DAY_OF_WEEK);
                    m=c1.get(Calendar.MONTH);
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

                    timeArrive.setText(dayWeek+" "+dH+":"+dM1);
                    if(fullDetail.getPrice1Person()!=null){
                        priceHuman=fullDetail.getPrice1Person();
                    }
                    else {
                        priceHuman=0;
                    }
                    if(fullDetail.getPrice1KgPackage()!=null){
                        priceLaggage=fullDetail.getPrice1KgPackage();
                    }
                    else {
                        priceLaggage=0;
                    }



                    humans.setText(humanCount+"");
                    laggage.setText(laggageCount+"");
                    predictPrice.setText(humanCount+" человек: €"+humanCount*priceHuman);
                    predictLaggage.setText(laggageCount+" груза: €"+laggageCount*priceLaggage);
                    double rP=(humanCount*priceHuman)+(laggageCount*priceLaggage);
                    inResult.setText("Всього:  €"+rP);


                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FullDetail> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getTripDetail();
    }
}