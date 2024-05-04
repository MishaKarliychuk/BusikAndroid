package com.busik.busik;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.Driver.MainActivity;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegFBActivity extends AppCompatActivity {

    LinearLayout back;
    RelativeLayout pa,dr;
    TextView p,d;
    View vv_p,vv_d;
    CheckBox politica;
    boolean asPassanger=true;
    String google_email;
    String google_name;

    Button reg;
    SharedPreferences sp;
    private static final String MY_SETTINGS = "TOKEN";
    EditText fio,phone,pass,pass2;
    RelativeLayout city,country;
    TextView cityTv,countryTv;
    List<Country> countryList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();
    CountryCodePicker ccp;
    Button haveAcc;
    boolean isCorect=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg_fbactivity);
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        google_email=MyApp.getFb_email();
        google_name=MyApp.getFb_name();

        back=findViewById(R.id.ll_back);
        pa=findViewById(R.id.rl_as_p);
        dr=findViewById(R.id.rl_as_d);
        p=findViewById(R.id.tv_as_passanger);
        d=findViewById(R.id.tv_as_driver);
        vv_d=findViewById(R.id.vv_d);
        vv_p=findViewById(R.id.vv_p);

        fio=findViewById(R.id.et_fio);
        phone=findViewById(R.id.et_phone);
        city=findViewById(R.id.et_city);
        haveAcc=findViewById(R.id.btn_have_acc);
        country=findViewById(R.id.et_country);
        countryTv=findViewById(R.id.tv_country);
        cityTv=findViewById(R.id.tv_city);
        pass=findViewById(R.id.et_pass);
        ccp=findViewById(R.id.ccp);
        pass2=findViewById(R.id.et_pass_two);
        reg=findViewById(R.id.btn_reg);

        fio.setText(google_name);

        politica=findViewById(R.id.cb_politica);


        ccp.registerCarrierNumberEditText(phone);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegFBActivity.this, MainActivity.class));
                finish();
            }
        });
        haveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegFBActivity.this,LogActivity.class));
            }
        });

        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!asPassanger){
                    asPassanger=true;
                    vv_d.setVisibility(View.GONE);
                    d.setTextColor(Color.parseColor("#80000000"));

                    vv_p.setVisibility(View.VISIBLE);
                    p.setTextColor(Color.BLACK);
                }
            }
        });

        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(asPassanger){
                    asPassanger=false;
                    vv_p.setVisibility(View.GONE);
                    p.setTextColor(Color.parseColor("#80000000"));

                    vv_d.setVisibility(View.VISIBLE);
                    d.setTextColor(Color.BLACK);
                }
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorect=true;
                if(fio.getText().toString().trim().length()==0&&isCorect){
                    Toast.makeText(RegFBActivity.this,"Заполните ФИО",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(phone.getText().toString().trim().length()==0&&isCorect){
                    Toast.makeText(RegFBActivity.this,"Заполните телефон",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(countryTv.getText().equals("Країна проживання")&&isCorect){
                    Toast.makeText(RegFBActivity.this,"Виберіть країну",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(cityTv.getText().equals("Місто проживання")&&isCorect){
                    Toast.makeText(RegFBActivity.this, "Виберіть місто", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if((pass.getText().toString().trim().length()==0&&isCorect)||(pass.getText().toString().trim().length()<=8&&isCorect)){
                    Toast.makeText(RegFBActivity.this, "Длина пароля должна біть больше 8 символов", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(!pass.getText().toString().trim().equals(pass2.getText().toString().trim())&&isCorect){
                    Toast.makeText(RegFBActivity.this, "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(!politica.isChecked()&&isCorect){
                    Toast.makeText(RegFBActivity.this, "Примите правила политики", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(isCorect){
                    if(asPassanger){
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("fio", fio.getText().toString().trim())
                                .addFormDataPart("phone", ccp.getFullNumber().toString().trim())
                                .addFormDataPart("live_city", cityTv.getText().toString().trim())
                                .addFormDataPart("live_country", countryTv.getText().toString().trim())
                                .addFormDataPart("password", pass.getText().toString().trim())
                                .addFormDataPart("fb_email", google_email)
                                .build();



                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                        Call<Object> call = getResponse.register(requestBody);
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {
                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(new Gson().toJson(response.body()));
                                    if(jsonObject.getString("auth_token")!=null){
                                        if(response.code()==200){

                                            putToken(jsonObject.getString("auth_token"));

                                            startActivity(new Intent(RegFBActivity.this, MainActivity.class));
                                            finishAffinity();
                                        }
                                    }
                                } catch (JSONException e) {

//                                    if(e.getMessage().equals("No value for auth_token")){
//                                    }
//                                    try {
//                                        if(jsonObject.getString("error")!=null){
//                                            Toast.makeText(RegFBActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
//                                        }
//                                    } catch (JSONException ex) {
//                                        ex.printStackTrace();
//                                    }
                                    if(e.getMessage().equals("No value for auth_token")){
                                    }
                                    Iterator<String> keys= jsonObject.keys();
                                    while (keys.hasNext())
                                    {
                                        String keyValue = (String)keys.next();
                                    }
                                    try {
                                        JSONArray jsonArray=jsonObject.getJSONArray((String) jsonObject.names().get(0));
                                        Toast.makeText(RegFBActivity.this,jsonArray.get(0).toString(),Toast.LENGTH_SHORT).show();
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }


                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }
                    else {

                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("fio", fio.getText().toString().trim())
                                .addFormDataPart("phone", ccp.getFullNumber().toString().trim())
                                .addFormDataPart("live_city", cityTv.getText().toString().trim())
                                .addFormDataPart("live_country", countryTv.getText().toString().trim())
                                .addFormDataPart("password", pass.getText().toString().trim())
                                .addFormDataPart("fb_email", google_email)
                                .build();



                        ApiConfig getResponse =  AppConfig.getRetrofit().create(ApiConfig.class);
                        Call<Object> call = getResponse.registerDriver(requestBody);
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {

                                JSONObject jsonObject = null;
                                try {
                                    jsonObject = new JSONObject(new Gson().toJson(response.body()));

                                    if(jsonObject.getString("auth_token")!=null){
                                        if(response.code()==200){

                                            putToken(jsonObject.getString("auth_token"));

                                            startActivity(new Intent(RegFBActivity.this, MainActivity.class));
                                            finishAffinity();
                                        }
                                    }
                                } catch (JSONException e) {
                                    if(e.getMessage().equals("No value for auth_token")){
                                    }
                                    Iterator<String> keys= jsonObject.keys();
                                    while (keys.hasNext())
                                    {
                                        String keyValue = (String)keys.next();
                                    }
                                    try {
                                        JSONArray jsonArray=jsonObject.getJSONArray((String) jsonObject.names().get(0));
                                        Toast.makeText(RegFBActivity.this,jsonArray.get(0).toString(),Toast.LENGTH_SHORT).show();
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }


                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });
                    }
                }
                else {
                    Toast.makeText(RegFBActivity.this, "Заповніть коректно дані", Toast.LENGTH_SHORT).show();
                }
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b = new AlertDialog.Builder(RegFBActivity.this);
                b.setTitle("Виберіть країну");
                String[] types=new String[countryList.size()];

                for (int i=0;i<countryList.size();i++){
                    types[i]=countryList.get(i).getName();
                }

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        countryTv.setText(types[which]);
                        cityTv.setText("Місто проживання");
                        for(int i=0;i<countryList.size();i++){
                            if(countryList.get(i).getName().equals(types[which])){
                                getCities(countryList.get(i).getId());
                                break;
                            }
                        }
                    }

                });

                b.show();
            }
        });

        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(RegFBActivity.this);
                b.setTitle("Виберіть місто");
                String[] types=new String[cityList.size()];

                for (int i=0;i<cityList.size();i++){
                    types[i]=cityList.get(i).getName();
                }

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        cityTv.setText(types[which]);
                    }

                });

                b.show();
            }
        });


        getCountries();
    }

    void getCountries(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<List<Country>> call = getResponse.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response.code()==200){
                    countryList=response.body();
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {

            }
        });
    }

    void getCities(int cityId){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<List<City>> call = getResponse.getAllCities(cityId);
        call.enqueue(new Callback<List<City>>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<List<City>> call, Response<List<City>> response) {
                if(response.code()==200){
                    cityList=response.body();
                }

            }
            @Override
            public void onFailure(Call<List<City>> call, Throwable t) {

            }
        });
    }
    void putToken(String token){
        SharedPreferences.Editor e = sp.edit();
        e.putString("token", token);
        e.commit();
        saveTypeUser();
    }

    void saveTypeUser(){
        SharedPreferences.Editor e = sp.edit();
        e.putBoolean("asPassanger", asPassanger);
        e.commit();
    }
}