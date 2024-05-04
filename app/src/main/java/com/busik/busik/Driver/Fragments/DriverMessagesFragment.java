package com.busik.busik.Driver.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Driver.Adapters.DriverFlightsAdapter;
import com.busik.busik.Driver.Adapters.DriverMessagesAdapter;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.DriverMessage;
import com.busik.busik.R;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMessagesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    List<Integer> list=new ArrayList<>();
    DriverMessagesAdapter messagesAdapter;
    RecyclerView recyclerView;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    List<DriverFlight> driverFlights=new ArrayList<>();
    Call<List<DriverMessage>> callMessage;
    Call<List<DriverFlight>> callMyFlights;
    Call<List<DriverFlight>> callArchived;
    SwipeRefreshLayout swipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_messages, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        token = sp.getString("token", null);

        recyclerView=view.findViewById(R.id.rv_messages);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        list.add(1);



        getMessages();




        return view;
    }

    void getMessages(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callMessage = getResponse.getDriverMessages("Token "+token);
        callMessage.enqueue(new Callback<List<DriverMessage>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<DriverMessage>> call, Response<List<DriverMessage>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){

                    for(int i=0;i<response.body().size();i++){

                        Date d =formateDate(response.body().get(i).getDateCreated());
                        response.body().get(i).setDateSort(d);
                    }
                    Collections.sort(response.body());
                    Collections.reverse(response.body());
                    messagesAdapter=new DriverMessagesAdapter(getContext(),response.body(),token);

                    recyclerView.setAdapter(messagesAdapter);

                    getMyFlights();
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<DriverMessage>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Date formateDate(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final ZonedDateTime parsed = ZonedDateTime.parse(dateStr, formatter);
        Date d=Date.from(parsed.toInstant());
        return d;
    }

    void getMyFlights(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callMyFlights = getResponse.getDriverFlights("Token "+token);
        callMyFlights.enqueue(new Callback<List<DriverFlight>>() {
            @Override
            public void onResponse(Call<List<DriverFlight>> call, Response<List<DriverFlight>> response) {

                if(response.code()==200){

                    for(DriverFlight d:response.body()){
                        driverFlights.add(d);
                    }
                    messagesAdapter.setDriverFlights(driverFlights);
                    getMyFlightsArchived();

                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<DriverFlight>> call, Throwable t) {

            }
        });
    }

    void getMyFlightsArchived(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callArchived = getResponse.getDriverFlightsArchived("Token "+token);
        callArchived.enqueue(new Callback<List<DriverFlight>>() {
            @Override
            public void onResponse(Call<List<DriverFlight>> call, Response<List<DriverFlight>> response) {

                if(response.code()==200){

                    for(DriverFlight d:response.body()){
                        driverFlights.add(d);
                    }
                    messagesAdapter.setDriverFlights(driverFlights);

                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<DriverFlight>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callMessage!=null){
            callMessage.cancel();
        }
        if(callMyFlights!=null){
            callMyFlights.cancel();
        }
        if(callArchived!=null){
            callArchived.cancel();
        }
    }

    @Override
    public void onRefresh() {
        getMessages();
    }
}