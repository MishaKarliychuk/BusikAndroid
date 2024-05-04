package com.busik.busik.Driver.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.AppConfig;
import com.busik.busik.ApiConfig;
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.MainActivity;
import com.busik.busik.Passanger.ApiResponse.Me;
import com.busik.busik.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyDriverCabinetFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    Button myReviews;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    Button logout,settings,support;
    TextView nameUser,placeUser;
    Call<Me> callMe;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_driver_cabinet, container, false);

        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        myReviews=view.findViewById(R.id.btn_my_reviews);

        nameUser=view.findViewById(R.id.tv_name_user);
        placeUser=view.findViewById(R.id.tv_user_place);
        logout=view.findViewById(R.id.btn_logout);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        settings=view.findViewById(R.id.btn_settings);
        support=view.findViewById(R.id.btn_support);
        swipeRefreshLayout.setOnRefreshListener(this);

        myReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DriverMainActivity)getActivity()).replaceFragment(new MyDriverReviewsFragment());
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DriverMainActivity)getActivity()).replaceFragment(new DriverSettingsFragment());
            }
        });


        getMe();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                Call<ResponseBody> call = getResponse.logoutDriver("Token "+token);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==204){
                            putToken();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                        }
                        else {
                            putToken();
                            startActivity(new Intent(getActivity(), MainActivity.class));
                            getActivity().finish();
                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });

        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto","akwalango@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });

        return view;
    }

    void putToken(){
        SharedPreferences.Editor e = sp.edit();
        e.putString("token", null);
        e.commit();
    }

    void getMe(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        callMe = getResponse.getMeDriver("Token "+token);
        callMe.enqueue(new Callback<Me>() {
            @Override
            public void onResponse(Call<Me> call, Response<Me> response) {
                swipeRefreshLayout.setRefreshing(false);
                if(response.code()==200){
                    nameUser.setText(response.body().getFio());
                    placeUser.setText(response.body().getLiveCity()+", "+response.body().getLiveCountry());
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Me> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callMe!=null){
            callMe.cancel();
        }
    }

    @Override
    public void onRefresh() {
        getMe();
    }
}