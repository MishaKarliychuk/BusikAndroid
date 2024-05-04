package com.busik.busik.Passanger.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.Adapters.MyFlightAdapter;
import com.busik.busik.Passanger.ApiResponse.MyFlight;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AchivedFlightsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView rvArchived;
    TextView archived;
    MyFlightAdapter myFlightAdapterArchived;

    Call<List<MyFlight>> callArchived;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    List<Integer> tripsIdA=new ArrayList<>();
    SwipeRefreshLayout mSwipeRefreshLayout;
    String token;
    List<MyFlight> myFlightsA=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_achived_flights, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);


        rvArchived=view.findViewById(R.id.rv_arhived_my);
        mSwipeRefreshLayout=view.findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        archived=view.findViewById(R.id.tv_archived);
        LinearLayoutManager layoutManagerarch
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        rvArchived.setHasFixedSize(true);
        rvArchived.setLayoutManager(layoutManagerarch);

        getArchived();

        return view;
    }


    void getArchived(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callArchived = getResponse.getMyFlightsArchived("Token "+token);
        callArchived.enqueue(new Callback<List<MyFlight>>() {
            @Override
            public void onResponse(Call<List<MyFlight>> call, Response<List<MyFlight>> response) {
                if(response.code()==200){

                    myFlightsA=response.body();
                    if(myFlightsA.size()==0){
                        archived.setVisibility(View.GONE);
                    }
                    for(int i=0;i<myFlightsA.size();i++){
                        Date d = null;
                        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
                        try {
                            d=formatter6.parse(myFlightsA.get(i).getDateDeparture());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        myFlightsA.get(i).setDateSort(d);
                    }

                    Collections.sort(myFlightsA);
                    for(MyFlight m:myFlightsA){
                        if(!tripsIdA.contains(m.getId())){
                            tripsIdA.add(m.getId());
                        }
                    }
                    myFlightAdapterArchived=new MyFlightAdapter(getContext(),myFlightsA,token,true);
                    rvArchived.setAdapter(myFlightAdapterArchived);
                    rvArchived.setVisibility(View.VISIBLE);
                }

                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<List<MyFlight>> call, Throwable t) {

                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {

        getArchived();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callArchived!=null){
            callArchived.cancel();
        }
    }
}