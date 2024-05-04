package com.busik.busik.Passanger.Fragments;

import android.annotation.SuppressLint;
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
import com.busik.busik.Passanger.Adapters.PassangerMessagesAdapter;
import com.busik.busik.Passanger.ApiResponse.PassengerMessage;
import com.busik.busik.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessagesPasengerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    List<Integer> list=new ArrayList<>();
    PassangerMessagesAdapter passangerMessagesAdapter;
    RecyclerView recyclerView;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    SwipeRefreshLayout swipeRefreshLayout;
    Call<List<PassengerMessage>> callMessage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_messages_pasenger, container, false);
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

        callMessage= getResponse.getMyMessages("Token "+token);
        callMessage.enqueue(new Callback<List<PassengerMessage>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<PassengerMessage>> call, Response<List<PassengerMessage>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){

                    for(int i=0;i<response.body().size();i++){
                        Date d =formateDate(response.body().get(i).getDateCreated());
                        response.body().get(i).setDateSort(d);
                    }
                    Collections.sort(response.body());
                    Collections.reverse(response.body());
                    passangerMessagesAdapter =new PassangerMessagesAdapter(getActivity(),response.body(),token);

                    recyclerView.setAdapter(passangerMessagesAdapter);
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<PassengerMessage>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callMessage.cancel();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static Date formateDate(String dateStr){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        final ZonedDateTime parsed = ZonedDateTime.parse(dateStr, formatter);
        Date d=Date.from(parsed.toInstant());
        return d;
    }

    @Override
    public void onRefresh() {
        getMessages();
    }
}