package com.busik.busik.Driver.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.Adapters.PassangerAcceptedAdapter;
import com.busik.busik.Driver.Adapters.PassangerRequestedAdapter;
import com.busik.busik.Driver.DPassangerAccepted;
import com.busik.busik.Driver.DPassangerRequest;
import com.busik.busik.Passanger.ApiResponse.AcceptPassanger;
import com.busik.busik.Passanger.ApiResponse.CancelPassanger;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.Passenger;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowPassangersToFlightFragment extends Fragment  implements SwipeRefreshLayout.OnRefreshListener{

    PassangerRequestedAdapter passangerRequestedAdapter;
    PassangerAcceptedAdapter passangerAcceptedAdapter;
    RecyclerView recyclerViewRequest,recyclerViewAccepted;
    TextView coutnPassangersRequested,status,countPassangerAccepted,dateDepart;
    DriverFlight driverFlight;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    DPassangerRequest dPassangerRequest;
    TextView ap,al;
    DPassangerAccepted dPassangerAccepted;
    LinearLayout back;
    List<Passenger> passangerInfoListReq=new ArrayList<>();
    List<Passenger> passangerInfoListAccept=new ArrayList<>();
    Call<List<DriverFlight>> callFlights;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_passangers_to_flight, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        driverFlight= ((DriverMainActivity) inflater.getContext()).getShowFlight();

        coutnPassangersRequested=view.findViewById(R.id.tv_passangers_requested);
        back=view.findViewById(R.id.ll_back);
        countPassangerAccepted=view.findViewById(R.id.tv_passenger_accepted);
        ap=view.findViewById(R.id.tv_a_p);
        dateDepart=view.findViewById(R.id.tv_date_depart);
        al=view.findViewById(R.id.tv_a_l);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        status=view.findViewById(R.id.tv_status);
        recyclerViewRequest=view.findViewById(R.id.rv_passangers_request);
        recyclerViewAccepted=view.findViewById(R.id.rv_passangers_accepted);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewRequest.setLayoutManager(layoutManager);
        recyclerViewAccepted.setLayoutManager(layoutManager1);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        dPassangerAccepted=new DPassangerAccepted() {
            @Override
            public void delete(int id,int pId) {
//                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("flight_id", String.valueOf(driverFlight.getId()))
//                        .addFormDataPart("passenger_id", String.valueOf(id))
//                        .build();
//                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//                Call<DeletePassanger> call = getResponse.deletePassangerFromFlight("Token "+token,requestBody);
//                call.enqueue(new Callback<DeletePassanger>() {
//                    @Override
//                    public void onResponse(Call<DeletePassanger> call, Response<DeletePassanger> response) {

//                        if(response.code()==200){
//                            if(response.body().getStatus()){
//                                for(int i=0;i<passangerInfoListAccept.size();i++){
//                                    if(passangerInfoListAccept.get(i).getId()==id){
//                                        passangerInfoListAccept.remove(i);
//                                        passangerAcceptedAdapter.notifyItemRemoved(i);
//                                        for(int j=0;j<driverFlight.getPassengers().size();j++){
//                                            if(driverFlight.getPassengers().get(j).getId()==id){
//                                                driverFlight.getPassengers().remove(j);
//                                                ((DriverMainActivity) inflater.getContext()).setShowFlight(driverFlight);
//                                            }
//                                        }
//                                        countPassangerAccepted.setText("Підтверджені ("+passangerInfoListAccept.size()+")");
//                                        countAccepted();
//                                    }
//                                }
//                            }
//                            else {
//                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else {
//                            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<DeletePassanger> call, Throwable t) {
//                        Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                    }
//                });
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                Call<CancelPassanger> call = getResponse.cancelPassanger("Token "+token,id);
                call.enqueue(new Callback<CancelPassanger>() {
                    @Override
                    public void onResponse(Call<CancelPassanger> call, Response<CancelPassanger> response) {


                        if(response.code()==200){
                            if(response.body().getStatus()){
                                for(int i=0;i<passangerInfoListAccept.size();i++){
                                    if(passangerInfoListAccept.get(i).getId()==pId){
                                        passangerInfoListAccept.remove(i);
                                        passangerAcceptedAdapter.notifyItemRemoved(i);
                                        for(int j=0;j<driverFlight.getPassengers().size();j++){
                                            if(driverFlight.getPassengers().get(j).getId()==pId){
                                                driverFlight.getPassengers().remove(j);
                                                ((DriverMainActivity) inflater.getContext()).setShowFlight(driverFlight);
                                            }
                                        }
                                        countPassangerAccepted.setText("Підтверджені ("+passangerInfoListAccept.size()+")");
                                        countAccepted();
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getContext(),"Eror",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Eror",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<CancelPassanger> call, Throwable t) {

                    }
                });
            }

            @Override
            public void reviews(int id) {

                ((DriverMainActivity) inflater.getContext()).setPassangerIdReview(id);
                ((DriverMainActivity) inflater.getContext()).replaceFragment(new ShowPassengersReviewsFragment());
            }

            @Override
            public void call(String phone) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    grantPermission();

                    return;
                }
                startActivity(intent);
            }
        };


        dPassangerRequest =new DPassangerRequest() {
//            @Override
//            public void delete(int id) {
//                RequestBody requestBody = new MultipartBody.Builder()
//                        .setType(MultipartBody.FORM)
//                        .addFormDataPart("flight_id", String.valueOf(driverFlight.getId()))
//                        .addFormDataPart("passenger_id", String.valueOf(id))
//                        .build();
//                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//                Call<DeletePassanger> call = getResponse.deletePassangerFromFlight("Token "+token,requestBody);
//                call.enqueue(new Callback<DeletePassanger>() {
//                    @Override
//                    public void onResponse(Call<DeletePassanger> call, Response<DeletePassanger> response) {

//                        if(response.code()==200){
//                            if(response.body().getStatus()){
//                                for(int i=0;i<passangerInfoList.size();i++){
//                                    if(passangerInfoList.get(i).getId()==id){
//                                        passangerInfoList.remove(i);
//                                        passangerRequestedAdapter.notifyItemRemoved(i);
//                                        for(int j=0;j<driverFlight.getPassengers().size();j++){
//                                            if(driverFlight.getPassengers().get(j).getId()==id){
//                                                driverFlight.getPassengers().remove(j);
//                                                ((DriverMainActivity) inflater.getContext()).setShowFlight(driverFlight);
//                                            }
//                                        }
//                                        coutnPassangersRequested.setText("Підтверджені ("+driverFlight.getPassengers().size()+")");
//                                    }
//                                }
//                            }
//                            else {
//                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        else {
//                            Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                    @Override
//                    public void onFailure(Call<DeletePassanger> call, Throwable t) {
//                        Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
            @Override
            public void accept(int id) {

                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                Call<AcceptPassanger> call = getResponse.acceptPassanger("Token "+token,id);
                call.enqueue(new Callback<AcceptPassanger>() {
                    @Override
                    public void onResponse(Call<AcceptPassanger> call, Response<AcceptPassanger> response) {

                        if(response.code()==200){
                            if(response.body().getStatus()){
                                for(int i=0;i<passangerInfoListReq.size();i++){
                                    if(passangerInfoListReq.get(i).getApplication().getId()==id){
                                        Passenger p=passangerInfoListReq.get(i);
                                        passangerInfoListAccept.add(p);
                                        passangerInfoListReq.remove(p);
                                        passangerAcceptedAdapter.notifyItemInserted(passangerInfoListAccept.size()-1);
                                        passangerRequestedAdapter.notifyItemRemoved(i);
                                        coutnPassangersRequested.setText("Запити ("+passangerInfoListReq.size()+")");
                                        countPassangerAccepted.setText("Підтверджені ("+passangerInfoListAccept.size()+")");

                                        for(int j=0;j<driverFlight.getPassengers().size();j++){
                                            if(driverFlight.getPassengers().get(j).getId()==p.getId()){
                                                driverFlight.getPassengers().get(j).getApplication().setStatus("approved");
                                                ((DriverMainActivity) inflater.getContext()).setShowFlight(driverFlight);
                                                break;
                                            }
                                        }
                                        countAccepted();
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getContext(),"Eror",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Eror",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<AcceptPassanger> call, Throwable t) {

                    }
                });
            }

            @Override
            public void cancel(int id) {

                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                Call<CancelPassanger> call = getResponse.cancelPassanger("Token "+token,id);
                call.enqueue(new Callback<CancelPassanger>() {
                    @Override
                    public void onResponse(Call<CancelPassanger> call, Response<CancelPassanger> response) {

                        if(response.code()==200){
                            if(response.body().getStatus()){
                                for(int i=0;i<passangerInfoListReq.size();i++){
                                    if(passangerInfoListReq.get(i).getApplication().getId()==id){
                                        Passenger p=passangerInfoListReq.get(i);
                                        passangerInfoListReq.remove(p);
                                        passangerRequestedAdapter.notifyItemRemoved(i);
                                        coutnPassangersRequested.setText("Запити ("+passangerInfoListReq.size()+")");
                                        for(int j=0;j<driverFlight.getPassengers().size();j++){
                                            if(driverFlight.getPassengers().get(j).getId()==p.getId()){
                                                driverFlight.getPassengers().get(j).getApplication().setStatus("cancelled");
                                                ((DriverMainActivity) inflater.getContext()).setShowFlight(driverFlight);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                Toast.makeText(getContext(),"Eror",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(getContext(),"Eror",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<CancelPassanger> call, Throwable t) {

                    }
                });
            }

            @Override
            public void call(String phone) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + phone));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    grantPermission();

                    return;
                }
                startActivity(intent);
            }

            @Override
            public void review(int id) {
                ((DriverMainActivity) inflater.getContext()).setPassangerIdReview(id);
                ((DriverMainActivity) inflater.getContext()).replaceFragment(new ShowPassengersReviewsFragment());
            }
        };

        getMyFlights();

//        getPassangers();
        return view;
    }

    void countAccepted(){
        int p=0;
        int l=0;
        for(int i=0;i<passangerInfoListAccept.size();i++){
            p+=passangerInfoListAccept.get(i).getApplication().getCountPerson();
            l+=passangerInfoListAccept.get(i).getApplication().getCountKgPackage();
        }
        ap.setText("Підтверджено місць: "+p+" людей з "+driverFlight.getMaxCountPerson());
        al.setText("Підтверджено вантажу: "+l+" кг з "+driverFlight.getMaxKgPackage());
    }


    void getMyFlights(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callFlights = getResponse.getDriverFlights("Token "+token);
        callFlights.enqueue(new Callback<List<DriverFlight>>() {
            @Override
            public void onResponse(Call<List<DriverFlight>> call, Response<List<DriverFlight>> response) {
//                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200) {

                    for (int i = 0; i < response.body().size(); i++) {
                        Date d = null;
                        SimpleDateFormat formatter6 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
                        try {
                            d = formatter6.parse(response.body().get(i).getDateDeparture());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        response.body().get(i).setDateSort(d);
                    }
                    Collections.sort(response.body());

                    for (DriverFlight d : response.body()) {
                        if (d.getId() == driverFlight.getId()) {
                            driverFlight = d;
                        }
                    }

                    switch (driverFlight.getStatus()){
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


                    passangerInfoListAccept.clear();
                    passangerInfoListReq.clear();
                    for(int i=0;i<driverFlight.getPassengers().size();i++){
                        if(driverFlight.getPassengers().get(i).getApplication().getStatus().equals("waiting")){
                            passangerInfoListReq.add(driverFlight.getPassengers().get(i));
                        }
                        else if(driverFlight.getPassengers().get(i).getApplication().getStatus().equals("approved")){
                            passangerInfoListAccept.add(driverFlight.getPassengers().get(i));
                        }
                    }
                    countAccepted();
                    for(int i=0;i<passangerInfoListReq.size();i++){

                        Date d = null;
                        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        try {
                            d=formatter6.parse(passangerInfoListReq.get(i).getApplication().getDatetimeCreated());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        passangerInfoListReq.get(i).setDateSort(d);
                    }

                    Collections.sort(passangerInfoListReq);
                    Collections.reverse(passangerInfoListReq);

                    Date d = null;
                    SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        d=formatter6.parse(driverFlight.getDateDeparture());
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    Calendar c1=Calendar.getInstance();
                    c1.setTime(d);
                    int dOfWeek=c1.get(Calendar.DAY_OF_WEEK);
                    int dOfMonth=c1.get(Calendar.DAY_OF_MONTH);
                    int month=c1.get(Calendar.MONTH);
                    String DDDD="";
                    if (dOfMonth < 10) {
                        DDDD="0"+dOfMonth;
                    }
                    else {
                        DDDD=dOfMonth+"";
                    }
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

                    dateDepart.setText(dayWeek+" "+DDDD+" "+Month+ " "+c1.get(Calendar.YEAR));

                    passangerRequestedAdapter = new PassangerRequestedAdapter(getContext(),passangerInfoListReq, dPassangerRequest);
                    coutnPassangersRequested.setText("Запити ("+passangerInfoListReq.size()+")");
                    recyclerViewRequest.setAdapter(passangerRequestedAdapter);

                    passangerAcceptedAdapter=new PassangerAcceptedAdapter(getContext(),passangerInfoListAccept,dPassangerAccepted);
                    countPassangerAccepted.setText("Підтверджені ("+passangerInfoListAccept.size()+")");
                    recyclerViewAccepted.setAdapter(passangerAcceptedAdapter);
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<DriverFlight>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

//    void getPassangers(){
//        for(int i=0;i<driverFlight.getPassengers().size();i++){
//            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//            Call<PassangerInfo> call = getResponse.getPassangerInfo("Token "+token,driverFlight.getPassengers().get(i).getId());
//            call.enqueue(new Callback<PassangerInfo>() {
//                @Override
//                public void onResponse(Call<PassangerInfo> call, Response<PassangerInfo> response) {
//                    if(response.code()==200){
//                        passangerInfoList.add(response.body());
//
//                        passangerAcceptedAdapter = new PassangerAcceptedAdapter(getContext(),passangerInfoList,dPassanger);
//                        recyclerView.setAdapter(passangerAcceptedAdapter);
//                    }
//                }
//                @Override
//                public void onFailure(Call<PassangerInfo> call, Throwable t) {
//                }
//            });
//        }
//    }

    private void grantPermission() {

        // show your dialog box to ask for permission
        new AlertDialog.Builder(getContext())
                .setTitle("Call Permission Required")
                .setMessage("This App needs Call permission, to function properly")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT)
                                .show();
                    }
                })
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //here permission will be given
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 3); // 3 is requestCode and can be any number
                    }
                })
                .create()
                .show();
    }

    @Override
    public void onRefresh() {
        getMyFlights();
    }
}