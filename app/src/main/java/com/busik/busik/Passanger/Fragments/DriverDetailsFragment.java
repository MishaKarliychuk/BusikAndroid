package com.busik.busik.Passanger.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
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
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.Adapters.DriverRAdapter;
import com.busik.busik.Passanger.ApiResponse.Driver;
import com.busik.busik.Passanger.ApiResponse.GetDriver;
import com.busik.busik.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverDetailsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    DriverRAdapter reviewsAdapter;
    List<Integer> list=new ArrayList<>();
    RecyclerView recyclerView;
    TextView driverName,driverCity,driverRating;
    Driver driver;
    LinearLayout back;
    Button call;
    SwipeRefreshLayout swipeRefreshLayout;

    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_driver_details, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        recyclerView=view.findViewById(R.id.rv_driver_reviews);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        call=view.findViewById(R.id.cv_call_driver);
        swipeRefreshLayout.setOnRefreshListener(this);


        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);


        driver=((PassangerMainActivity)getActivity()).getCurentDriver();

        driverName=view.findViewById(R.id.tv_driver_name);
        back=view.findViewById(R.id.ll_back);
        driverCity=view.findViewById(R.id.tv_driver_city);
        driverRating=view.findViewById(R.id.tv_driver_rating);

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:+" + driver.getPhone()));
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    grantPermission();

                    return;
                }
                startActivity(intent);
            }
        });

//        reviewsAdapter=new ReviewsAdapter(getContext(),list);

//        recyclerView.setAdapter(reviewsAdapter);


        getDriver();




        return view;
    }

    void getDriver(){

        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<GetDriver> call = getResponse.getDriverInfo("Token "+token,driver.getId());
        call.enqueue(new Callback<GetDriver>() {
            @Override
            public void onResponse(Call<GetDriver> call, Response<GetDriver> response) {
                if(response.code()==200){
                    driver.setPhone(response.body().getPhone());

                    reviewsAdapter=new DriverRAdapter(getContext(),response.body().getReviews());

                    recyclerView.setAdapter(reviewsAdapter);
                    driverName.setText(response.body().getFio());

                    back.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getActivity().onBackPressed();
                        }
                    });

                    driverCity.setText(response.body().getLiveCity()+", "+response.body().getLiveCountry());

                    driverRating.setText(response.body().getRating().toString());
                }
                else{
                }
                swipeRefreshLayout.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<GetDriver> call, Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }


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
        getDriver();
    }
}