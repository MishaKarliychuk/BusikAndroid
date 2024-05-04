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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.Adapters.ReviewsAdapter;
import com.busik.busik.Passanger.ApiResponse.Review;
import com.busik.busik.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyReviewsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    ReviewsAdapter reviewsAdapter;
    List<Integer> list=new ArrayList<>();
    RecyclerView recyclerView;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    LinearLayout back;
    SwipeRefreshLayout swipeRefreshLayout;
    TextView countR;
    Call<List<Review>> callReview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  =inflater.inflate(R.layout.fragment_my_reviews, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        token = sp.getString("token", null);

        recyclerView=view.findViewById(R.id.rv_my_reviews);
        countR=view.findViewById(R.id.tv_count_reviews);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        back=view.findViewById(R.id.ll_back);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        getReviews();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        return view;
    }

    void getReviews(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callReview = getResponse.getMyReviews("Token "+token);
        callReview.enqueue(new Callback<List<Review>>() {
            @Override
            public void onResponse(Call<List<Review>> call, Response<List<Review>> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){

                    reviewsAdapter=new ReviewsAdapter(getContext(),response.body());

                    recyclerView.setAdapter(reviewsAdapter);
                        countR.setText("У вас "+response.body().size()+" відгуків");
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Review>> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        callReview.cancel();
    }

    @Override
    public void onRefresh() {
        getReviews();
    }
}