package com.busik.busik.Passanger.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.ApiResponse.GetDriver;
import com.busik.busik.Passanger.ApiResponse.ReviewAboutDriver;
import com.busik.busik.Passanger.ApiResponse.ReviewAboutPassanger;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddReviewFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    TextView driverName,tripDetail;
    EditText reviewText;
    Button send;
    SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout bakc;
    int idDriver;

    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    int idFLight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_review, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        idDriver= ((PassangerMainActivity) inflater.getContext()).getIdDriverReview();
        idFLight= ((PassangerMainActivity) inflater.getContext()).getIdFlightReview();


        driverName=view.findViewById(R.id.tv_name_driver);
        tripDetail=view.findViewById(R.id.tv_name_trip);
        reviewText=view.findViewById(R.id.et_review_text);
        mSwipeRefreshLayout=view.findViewById(R.id.swipe_container);
        send=view.findViewById(R.id.cv_send_review);
        bakc=view.findViewById(R.id.ll_back);
        mSwipeRefreshLayout.setOnRefreshListener(this);


        bakc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        getDriver();
        getTripDetail();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("driver",String.valueOf(idDriver))
                        .addFormDataPart("review_text", reviewText.getText().toString().trim())
                        .build();
                Call<ReviewAboutDriver> call = getResponse.leftReviewAboutDriver("Token "+token,requestBody);
                call.enqueue(new Callback<ReviewAboutDriver>() {
                    @Override
                    public void onResponse(Call<ReviewAboutDriver> call, Response<ReviewAboutDriver> response) {
                        if(response.code()==200){
                            deleteMessage();
                        }
                        else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewAboutDriver> call, Throwable t) {

                    }
                });
            }
        });


        return view;
    }

    void deleteMessage(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<ResponseBody> call = getResponse.deleteMessagePassanger("Token "+token,((PassangerMainActivity)getActivity()).getIdMessage());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code()==204){
                    Toast.makeText(getActivity(),"Відгук залишено",Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


    void getTripDetail(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<FullDetail> call = getResponse.getFullDetail("Token "+token,idFLight);
        call.enqueue(new Callback<FullDetail>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<FullDetail> call, Response<FullDetail> response) {
                if(response.code()==200){
                    tripDetail.setText("До поїздки "+response.body().getCityFrom()+" -> "+response.body().getCityTo());
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<FullDetail> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    void getDriver(){

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<GetDriver> call = getResponse.getDriverInfo("Token "+token,idDriver);
        call.enqueue(new Callback<GetDriver>() {
            @Override
            public void onResponse(Call<GetDriver> call, Response<GetDriver> response) {
                if(response.code()==200){
                    driverName.setText("Залишити відгук "+response.body().getFio());
                }
                else{
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<GetDriver> call, Throwable t) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        getDriver();
        getTripDetail();
    }
}