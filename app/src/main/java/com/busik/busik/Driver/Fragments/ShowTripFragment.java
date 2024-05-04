package com.busik.busik.Driver.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.Adapters.DriverFlightsAdapter;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.ReviewAboutPassanger;
import com.busik.busik.Passanger.ApiResponse.SendMessage;
import com.busik.busik.Passanger.ApiResponse.UpdateFlight;
import com.busik.busik.R;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowTripFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    TextView cityDepart,cityArrive,timeDepart,timeArrive,dateDepart,status,priceP,priceL,description;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    LinearLayout back;
    String token;
    TextView goBack;
    Call<List<DriverFlight>> callFlights;
    DriverFlight driverFlight;
    SwipeRefreshLayout swipeRefreshLayout;
    Calendar ccc=Calendar.getInstance();

    Button passangers,cancel,change,end,sendMessage,goPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_trip, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        driverFlight= ((DriverMainActivity) inflater.getContext()).getShowFlight();

        cityDepart=view.findViewById(R.id.tv_city_depert);
        cityArrive=view.findViewById(R.id.tv_city_arrive);
        sendMessage=view.findViewById(R.id.cv_send_message);
        timeArrive=view.findViewById(R.id.tv_time_arrive);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        timeDepart=view.findViewById(R.id.tv_time_depart);
        goBack=view.findViewById(R.id.tv_go_back);
        dateDepart=view.findViewById(R.id.tv_date_depart);
        status=view.findViewById(R.id.tv_status);
        back=view.findViewById(R.id.ll_back);
        priceP=view.findViewById(R.id.tv_price_passange);
        goPass=view.findViewById(R.id.cv_go_passangers);
        priceL=view.findViewById(R.id.tv_price_laggage);
        description=view.findViewById(R.id.tv_description);
        passangers=view.findViewById(R.id.cv_passangers);
        cancel=view.findViewById(R.id.cv_cancel);
        change=view.findViewById(R.id.cv_change);
        end=view.findViewById(R.id.cv_end);
        swipeRefreshLayout.setOnRefreshListener(this);


        if(((DriverMainActivity) inflater.getContext()).isGoBack()){
            goBack.setText("Назад");
            ((DriverMainActivity)getActivity()).setGoBack(false);
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

            Call<ResponseBody> call = getResponse.deleteMessageDriver("Token "+token,((DriverMainActivity)getActivity()).getIdMessage());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.code()==204){

                    }
                    else {

                        Toast.makeText(inflater.getContext(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });




        passangers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DriverMainActivity)getActivity()).replaceFragment(new ShowPassangersToFlightFragment());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("");  // заголовок
                builder.setMessage("Ви бажаєте скасувати рейс?"); // сообщение
                builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RequestBody requestBody= new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("status","cancelled")
                                .addFormDataPart("archived","True")
                                .build();
                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                        Call<UpdateFlight> call = getResponse.updateFlight("Token "+token,requestBody,driverFlight.getId());
                        call.enqueue(new Callback<UpdateFlight>() {
                            @Override
                            public void onResponse(Call<UpdateFlight> call, Response<UpdateFlight> response) {
                                if(response.code()==200){
                                    Toast.makeText(getContext(),"Рейс отменен",Toast.LENGTH_SHORT).show();
                                    getActivity().onBackPressed();
                                }
                                else {
                                    Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<UpdateFlight> call, Throwable t) {
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
                //
            }
        });

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DriverMainActivity)getActivity()).replaceFragment(new EditFlightFragment());
            }
        });

        end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date d = ccc.getTime();

                Date ar = null;
                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    ar=formatter6.parse(driverFlight.getDateArrival());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(d.getMonth()>=ar.getMonth()&&d.getDay()>=ar.getDay()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("");  // заголовок
                    builder.setMessage("Ви хочете завершити рейс?"); // сообщение
                    builder.setPositiveButton("Так", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            RequestBody requestBody= new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("status","completed")
                                    .addFormDataPart("archived","True")
                                    .build();
                            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                            Call<UpdateFlight> call = getResponse.updateFlight("Token "+token,requestBody,driverFlight.getId());
                            call.enqueue(new Callback<UpdateFlight>() {
                                @Override
                                public void onResponse(Call<UpdateFlight> call, Response<UpdateFlight> response) {
                                    if(response.code()==200){
                                        Toast.makeText(getContext(),"Рейс Завершено",Toast.LENGTH_SHORT).show();
                                        getActivity().onBackPressed();
                                    }
                                    else {
                                        Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                                    }

                                }

                                @Override
                                public void onFailure(Call<UpdateFlight> call, Throwable t) {
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
                else {
                    Toast.makeText(getContext(),"Ще не можливо завершити рейс",Toast.LENGTH_SHORT).show();
                }

            }
        });

        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup viewGroup = view.findViewById(android.R.id.content);

                boolean isApproved=false;
                for(int i=0;i<driverFlight.getPassengers().size();i++){
                    if(driverFlight.getPassengers().get(i).getApplication().getStatus().equals("approved")){
                        isApproved=true;
                        break;
                    }
                }
                //then we will inflate the custom alert dialog xml that we created
                View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_send_message, viewGroup, false);


                EditText textMessage=dialogView.findViewById(R.id.et_message_text);
                Button send=dialogView.findViewById(R.id.cv_send);


                //Now we need an AlertDialog.Builder object
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                //setting the view of the builder to our custom view that we already inflated
                builder.setView(dialogView);

                //finally creating the alert dialog and displaying it
                AlertDialog alertDialog = builder.create();
                if(isApproved){
                    alertDialog.show();
                }
                else {
                    Toast.makeText(getContext(),"Немає підтверджених пасажирів",Toast.LENGTH_SHORT).show();
                }

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.hide();

                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("flight",driverFlight.getId().toString())
                                .addFormDataPart("message_text", textMessage.getText().toString().trim())
                                .addFormDataPart("type_message", "message_from_driver")
                                .addFormDataPart("app_data", "{\"flight_id\": "+driverFlight.getId().toString()+"}")
                                .build();
                        Call<SendMessage> call = getResponse.sendMessage("Token "+token,requestBody);
                        call.enqueue(new Callback<SendMessage>() {
                            @Override
                            public void onResponse(Call<SendMessage> call, Response<SendMessage> response) {
                                if(response.code()==200){
                                    Toast.makeText(getContext(),"Сообщение отправлено",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<SendMessage> call, Throwable t) {

                            }
                        });
                    }
                });
            }
        });

        goPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((DriverMainActivity)getActivity()).replaceFragment(new JustPassengersFragment());
            }
        });

        getMyFlights();
        return view;
    }


    void getMyFlights(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callFlights = getResponse.getDriverFlights("Token "+token);
        callFlights.enqueue(new Callback<List<DriverFlight>>() {
            @Override
            public void onResponse(Call<List<DriverFlight>> call, Response<List<DriverFlight>> response) {
//                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){

                    for(int i=0;i<response.body().size();i++){
                        Date d = null;
                        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
                        try {
                            d=formatter6.parse(response.body().get(i).getDateDeparture());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        response.body().get(i).setDateSort(d);
                    }
                    Collections.sort(response.body());

                    for(DriverFlight d:response.body()){
                        if(d.getId()==driverFlight.getId()){
                            driverFlight=d;
                        }
                    }

                    ((DriverMainActivity) getActivity()).setShowFlight(driverFlight);
                    cityDepart.setText(driverFlight.getCityFrom()+", "+driverFlight.getCountryFrom());
                    cityArrive.setText(driverFlight.getCityTo()+", "+driverFlight.getCountryTo());

                    SimpleDateFormat output = new SimpleDateFormat("dd.MM HH.mm");
                    SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm");
                    SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");

                    Date d = null;
                    SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        d=formatter6.parse(driverFlight.getDateDeparture());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String formatted=outputTime.format(d);

                    String f=output1.format(d);

                    Calendar c1=Calendar.getInstance();
                    c1.setTime(d);
                    int dOfWeek=c1.get(Calendar.DAY_OF_WEEK);
                    int dOfMonth=c1.get(Calendar.DAY_OF_MONTH);
                    int month=c1.get(Calendar.MONTH);
                    month++;
                    String dayWeek = "";
                    switch (dOfWeek){
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
                    switch (month){
                        case (1):{
                            Month="Январь";
                            break;
                        }
                        case (2):{
                            Month="Февраль";
                            break;
                        }
                        case (3):{
                            Month="Март";
                            break;
                        }
                        case (4):{
                            Month="Апрель";
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
                            Month="Август";
                            break;
                        }
                        case (9):{
                            Month="Сентябрь";
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
                            Month="Декабрь";
                            break;
                        }
                    }
                    String MMMM="";
                    if(month<10){
                        MMMM="0"+month;
                    }
                    else {
                        MMMM=month+"";
                    }
                    String DDDD="";
                    if(dOfMonth<10){
                        DDDD="0"+dOfMonth;
                    }
                    else {
                        DDDD=dOfMonth+"";
                    }

                    timeDepart.setText(DDDD+"."+MMMM+" "+dayWeek+" "+formatted);
                    dateDepart.setText(DDDD+" "+Month+ " "+c1.get(Calendar.YEAR));

                    try {
                        d=formatter6.parse(driverFlight.getDateArrival());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    c1.setTime(d);
                    dOfWeek=c1.get(Calendar.DAY_OF_WEEK);
                    dOfMonth=c1.get(Calendar.DAY_OF_MONTH);
                    month=c1.get(Calendar.MONTH);
                    month++;
                    if(month<10){
                        MMMM="0"+month;
                    }
                    else {
                        MMMM=month+"";
                    }
                    if(dOfMonth<10){
                        DDDD="0"+dOfMonth;
                    }
                    else {
                        DDDD=dOfMonth+"";
                    }

                    formatted=outputTime.format(d);
                    switch (dOfWeek){
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

                    timeArrive.setText(DDDD+"."+MMMM+" "+dayWeek+" "+formatted);

                    switch (driverFlight.getStatus()){
                        case("waiting"):{
                            passangers.setVisibility(View.VISIBLE);
                            end.setVisibility(View.VISIBLE);
                            sendMessage.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            change.setVisibility(View.VISIBLE);
                            goPass.setVisibility(View.GONE);
                            status.setText("В очікуванні");
                            break;
                        }
                        case("on_the_way"):{
                            passangers.setVisibility(View.VISIBLE);
                            end.setVisibility(View.VISIBLE);
                            sendMessage.setVisibility(View.VISIBLE);
                            cancel.setVisibility(View.VISIBLE);
                            change.setVisibility(View.VISIBLE);
                            goPass.setVisibility(View.GONE);
                            status.setText("В дорозі");
                            break;
                        }
                        case ("completed"):{
                            passangers.setVisibility(View.GONE);
                            end.setVisibility(View.GONE);
                            sendMessage.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                            change.setVisibility(View.GONE);
                            goPass.setVisibility(View.VISIBLE);
                            status.setText("Завершено");
                            break;
                        }
                        case ("cancelled"):{
                            passangers.setVisibility(View.GONE);
                            end.setVisibility(View.GONE);
                            sendMessage.setVisibility(View.GONE);
                            cancel.setVisibility(View.GONE);
                            change.setVisibility(View.GONE);
                            goPass.setVisibility(View.VISIBLE);
                            status.setText("Не відбувся");
                            break;
                        }
                    }

                    if(driverFlight.getPrice1KgPackage()==null){
                        priceL.setText("0");
                    }
                    else {
                        priceL.setText("€"+driverFlight.getPrice1KgPackage());
                    }
                    if(driverFlight.getPrice1Person()==null){
                        priceP.setText("0");
                    }
                    else {
                        priceP.setText("€"+driverFlight.getPrice1Person());
                    }

                    description.setText(driverFlight.getDetails());
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<DriverFlight>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getMyFlights();
    }
}