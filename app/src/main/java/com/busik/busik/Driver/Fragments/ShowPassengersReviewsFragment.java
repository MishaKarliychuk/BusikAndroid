package com.busik.busik.Driver.Fragments;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Passanger.Adapters.ReviewsAdapter;
import com.busik.busik.Passanger.ApiResponse.PassangerInfo;
import com.busik.busik.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ShowPassengersReviewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    TextView name;
    ReviewsAdapter reviewsAdapter;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayout back;
    int idPassenger;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_passengers_reviews, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        recyclerView=view.findViewById(R.id.rv_reviews_passanger);
        back=view.findViewById(R.id.ll_back);
        name=view.findViewById(R.id.tv_passanger_name_r);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);
        swipeRefreshLayout.setOnRefreshListener(this);
        recyclerView.setLayoutManager(layoutManager);

        idPassenger=((DriverMainActivity) inflater.getContext()).getPassangerIdReview();


        getPassengerInfo();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

    void getPassengerInfo(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<PassangerInfo> call = getResponse.getPassangerInfo("Token "+token,idPassenger);
        call.enqueue(new Callback<PassangerInfo>() {
            @Override
            public void onResponse(Call<PassangerInfo> call, Response<PassangerInfo> response) {
                if(response.code()==200){
                    name.setText("Відгуки: "+response.body().getFio());
                    if(response.body().getReviews()!=null){
                        reviewsAdapter=new ReviewsAdapter(getContext(),response.body().getReviews());
                        recyclerView.setAdapter(reviewsAdapter);
                    }
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<PassangerInfo> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onRefresh() {
        getPassengerInfo();
    }
}