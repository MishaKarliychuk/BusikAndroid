package com.busik.busik.Driver.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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
import com.busik.busik.Passanger.Adapters.ReviewsAdapter;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.PassangerInfo;
import com.busik.busik.Passanger.ApiResponse.ReviewAboutPassanger;
import com.busik.busik.R;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LeftReviewAboutPassangerFragment extends Fragment {
    TextView driverName,tripDetail;
    EditText reviewText;
    Button send;
    int idDriver;

    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    LinearLayout back;
    DriverFlight driverFlight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_left_review_about_passanger, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        idDriver= ((DriverMainActivity) inflater.getContext()).getPassangerIdReview();
        driverFlight= ((DriverMainActivity) inflater.getContext()).getReviewIdFlight();



        driverName=view.findViewById(R.id.tv_name_driver);
        tripDetail=view.findViewById(R.id.tv_name_trip);
        back=view.findViewById(R.id.ll_back);
        reviewText=view.findViewById(R.id.et_review_text);
        send=view.findViewById(R.id.cv_send_review);
        tripDetail.setText("До поїздки "+driverFlight.getCityFrom()+" -> "+driverFlight.getCityTo());


        getPassengerInfo();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("passenger",String.valueOf(idDriver))
                        .addFormDataPart("review_text", reviewText.getText().toString().trim())
                        .build();
                Call<ReviewAboutPassanger> call = getResponse.leftReviweAboutPassanger("Token "+token,requestBody);
                call.enqueue(new Callback<ReviewAboutPassanger>() {
                    @Override
                    public void onResponse(Call<ReviewAboutPassanger> call, Response<ReviewAboutPassanger> response) {
                        if(response.code()==200){
                            deleteMessage();
                        }
                        else {
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ReviewAboutPassanger> call, Throwable t) {
                    }
                });
            }
        });


        return view;
    }

    void deleteMessage(){

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<ResponseBody> call = getResponse.deleteMessageDriver("Token "+token,((DriverMainActivity)getActivity()).getIdMessage());
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

    void getPassengerInfo(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<PassangerInfo> call = getResponse.getPassangerInfo("Token "+token,idDriver);
        call.enqueue(new Callback<PassangerInfo>() {
            @Override
            public void onResponse(Call<PassangerInfo> call, Response<PassangerInfo> response) {
                if(response.code()==200){
                    driverName.setText("Залишити відгук "+response.body().getFio());
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<PassangerInfo> call, Throwable t) {
            }
        });
    }

}