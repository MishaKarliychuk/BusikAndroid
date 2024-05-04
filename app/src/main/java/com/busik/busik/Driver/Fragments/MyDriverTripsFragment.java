package com.busik.busik.Driver.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.Adapters.DriverFlightsAdapter;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyDriverTripsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    LinearLayout archived,actived;
    Button addTrip;
    boolean isActiveted=true;
    DriverFlightsAdapter driverFlightsAdapter;
    RecyclerView recyclerView;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    SwipeRefreshLayout swipeRefreshLayout;
    String token;
    View Act,Arh;
    TextView act,arh;

    Call<List<DriverFlight>> callFlights;
    Call<List<DriverFlight>> callFlightsA;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_driver_trips, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        token = sp.getString("token", null);


        actived=view.findViewById(R.id.cv_active_trips);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        act=view.findViewById(R.id.tv_act);
        arh=view.findViewById(R.id.tv_arh);
        Act=view.findViewById(R.id.v_all_buses);
        Arh=view.findViewById(R.id.v_my_buses);
        archived=view.findViewById(R.id.cv_archived_trips);
        addTrip=view.findViewById(R.id.cv_add_trip);
        recyclerView=view.findViewById(R.id.rv_driver_trips_);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        actived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isActiveted){
                    isActiveted=true;
                    getMyFlights();
                    arh.setTextColor(Color.parseColor("#6B6B6B"));
                    act.setTextColor(Color.parseColor("#000000"));
                    Act.setVisibility(View.VISIBLE);
                    Arh.setVisibility(View.GONE);
                    addTrip.setVisibility(View.VISIBLE);
                    ((DriverMainActivity)getActivity()).setArchived(false);
                }
            }
        });

        archived.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isActiveted){
                    ((DriverMainActivity)getActivity()).setArchived(true);
                    isActiveted=false;
                    getMyFlightsArchived();
                    act.setTextColor(Color.parseColor("#6B6B6B"));
                    Act.setVisibility(View.GONE);
                    arh.setTextColor(Color.parseColor("#000000"));
                    Arh.setVisibility(View.VISIBLE);
                    addTrip.setVisibility(View.GONE);
                }
            }
        });

        if(((DriverMainActivity)getActivity()).isArchived()){
            ((DriverMainActivity)getActivity()).setArchived(true);
            isActiveted=false;
            getMyFlightsArchived();
            act.setTextColor(Color.parseColor("#6B6B6B"));
            Act.setVisibility(View.GONE);
            arh.setTextColor(Color.parseColor("#000000"));
            Arh.setVisibility(View.VISIBLE);
            addTrip.setVisibility(View.GONE);
        }
        else {
            isActiveted=true;
            getMyFlights();
            arh.setTextColor(Color.parseColor("#6B6B6B"));
            act.setTextColor(Color.parseColor("#000000"));
            Act.setVisibility(View.VISIBLE);
            Arh.setVisibility(View.GONE);
            addTrip.setVisibility(View.VISIBLE);
            ((DriverMainActivity)getActivity()).setArchived(false);
        }

        addTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DriverMainActivity)getActivity()).replaceFragment(new AddTripFragment());
            }
        });

        return view;
    }

    void getMyFlights(){

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callFlights = getResponse.getDriverFlights("Token "+token);
        callFlights.enqueue(new Callback<List<DriverFlight>>() {
            @Override
            public void onResponse(Call<List<DriverFlight>> call, Response<List<DriverFlight>> response) {
                swipeRefreshLayout.setRefreshing(false);
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

                    driverFlightsAdapter=new DriverFlightsAdapter(getContext(),response.body(),false);

                    recyclerView.setAdapter(driverFlightsAdapter);
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<DriverFlight>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    void getMyFlightsArchived(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callFlightsA = getResponse.getDriverFlightsArchived("Token "+token);
        callFlightsA.enqueue(new Callback<List<DriverFlight>>() {
            @Override
            public void onResponse(Call<List<DriverFlight>> call, Response<List<DriverFlight>> response) {
                swipeRefreshLayout.setRefreshing(false);
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
                    Collections.reverse(response.body());
                    driverFlightsAdapter=new DriverFlightsAdapter(getContext(),response.body(),true);

                    recyclerView.setAdapter(driverFlightsAdapter);
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<DriverFlight>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callFlights!=null){
            callFlights.cancel();
        }
        if(callFlightsA!=null){
            callFlightsA.cancel();
        }
    }

    @Override
    public void onRefresh() {
        if(!isActiveted){
            getMyFlightsArchived();
        }
        else {
            getMyFlights();
        }
    }
}