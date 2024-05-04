package com.busik.busik.Driver.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.ApiResponse.ChangePassword;
import com.busik.busik.Passanger.ApiResponse.Me;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;
import com.busik.busik.R;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DriverSettingsFragment extends Fragment {
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    RelativeLayout country,city;
    TextView cityTv,countryTv;
    LinearLayout back;

    EditText newPass,newPass2;
    Button changePass;
    EditText fio,phone,email;
    Button save;
    boolean isCorect=true;
    private static final int RC_SIGN_IN=9001;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    Button fb,google;
    List<Country> countryList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_settings, container, false);

        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        fio=view.findViewById(R.id.et_fio_settings);
        phone=view.findViewById(R.id.et_phone_settings);
        email=view.findViewById(R.id.et_email_settings);
        city=view.findViewById(R.id.et_city_settings);
        cityTv=view.findViewById(R.id.tv_city);
        back=view.findViewById(R.id.ll_back);
        country=view.findViewById(R.id.et_country_settings);
        countryTv=view.findViewById(R.id.tv_country);
        save=view.findViewById(R.id.btn_save_settings);
        newPass=view.findViewById(R.id.et_new_pass);
        newPass2=view.findViewById(R.id.et_new_pass2);
        changePass=view.findViewById(R.id.btn_change_pass);
        fb=view.findViewById(R.id.cv_fb);
        google=view.findViewById(R.id.cv_google);
        callbackManager = CallbackManager.Factory.create();

        AccessToken accessToken=AccessToken.getCurrentAccessToken();
        if(accessToken!=null&&accessToken.isExpired()==false){

            LoginManager.getInstance().logOut();
        }
        else {


        }
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        AccessToken accessToken= AccessToken.getCurrentAccessToken();
                        GraphRequest request = GraphRequest.newMeRequest(
                                accessToken,
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(
                                            JSONObject object,
                                            GraphResponse response) {
                                        String email = null;
                                        try {
                                            email=object.getString("email");

                                        } catch (JSONException e) {
                                            try {
                                                email=object.getString("id");
                                                email+="@gmail.com";

                                            } catch (JSONException ex) {
                                                ex.printStackTrace();
                                            }
                                            e.printStackTrace();
                                        }


                                        RequestBody requestBody;
                                        requestBody= new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("fb_email", email)
                                                .build();
                                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                                        Call<Me> call = getResponse.updateMeDriver("Token "+token,requestBody);
                                        call.enqueue(new Callback<Me>() {
                                            @Override
                                            public void onResponse(Call<Me> call, Response<Me> response) {

                                                if(response.code()==200){
                                                    Toast.makeText(getActivity(),"Facebook привязали",Toast.LENGTH_SHORT).show();
                                                    fb.setText("Отвязать");
                                                }
                                                else {
                                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Me> call, Throwable t) {


                                            }
                                        });

                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {

                    }

                    @Override
                    public void onError(FacebookException exception) {

                    }
                });

        getMe();
        getCountries();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
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
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorect=true;

                RequestBody requestBody;

                if(email.getText().toString().trim().equals("")){
                    if(fio.getText().toString().trim().length()==0&&isCorect){
                        Toast.makeText(getContext(), "Введите ФИО", Toast.LENGTH_SHORT).show();
                        isCorect=false;
                    }
                    if(cityTv.getText().toString().trim().equals("Місто проживання")){
                        Toast.makeText(getContext(), "Виберіть місто", Toast.LENGTH_SHORT).show();
                        isCorect=false;
                    }
                    requestBody= new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("fio", fio.getText().toString().trim())
                            .addFormDataPart("live_city", cityTv.getText().toString().trim())
                            .addFormDataPart("live_country", countryTv.getText().toString().trim())
                            .build();
                }
                else {
                    if(fio.getText().toString().trim().length()==0&&isCorect){
                        Toast.makeText(getContext(), "Введите ФИО", Toast.LENGTH_SHORT).show();
                        isCorect=false;
                    }
                    if(cityTv.getText().toString().trim().equals("Місто проживання")){
                        Toast.makeText(getContext(), "Виберіть місто", Toast.LENGTH_SHORT).show();
                        isCorect=false;
                    }
                    requestBody= new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("fio", fio.getText().toString().trim())
                            .addFormDataPart("live_city", cityTv.getText().toString().trim())
                            .addFormDataPart("live_country", countryTv.getText().toString().trim())
                            .addFormDataPart("email", email.getText().toString().trim())
                            .build();
                }

                if(isCorect){
                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<Me> call = getResponse.updateMeDriver("Token "+token,requestBody);
                    call.enqueue(new Callback<Me>() {
                        @Override
                        public void onResponse(Call<Me> call, Response<Me> response) {
                            if(response.code()==200){
                                Toast.makeText(getActivity(),"Настройки успешно изменені",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Me> call, Throwable t) {
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Заповніть коректно дані", Toast.LENGTH_SHORT).show();
                }
            }
        });

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isCorect=true;
                if(newPass.getText().toString().trim().length()<8&&isCorect){
                    isCorect=false;
                }
                if(newPass2.getText().toString().trim().length()<8&&isCorect){
                    isCorect=false;
                }
                if(!newPass.getText().toString().trim().equals(newPass2.getText().toString().trim())&&isCorect){
                    isCorect=false;
                }
                if(isCorect){
                    RequestBody requestBody;
                    requestBody= new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("password", newPass.getText().toString().trim())
                            .build();
                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<ChangePassword> call = getResponse.changePassword("Token "+token,requestBody);
                    call.enqueue(new Callback<ChangePassword>() {
                        @Override
                        public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                            if(response.code()==200&&response.body().getStatus()){
                                Toast.makeText(getActivity(),"Пароль успешно изменен",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<ChangePassword> call, Throwable t) {
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(), "Заповніть коректно дані", Toast.LENGTH_SHORT).show();
                }
            }
        });

        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fb.getText().toString().trim().equals("Отвязать")){

                    RequestBody requestBody;
                    requestBody= new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("fb_email", "")
                            .build();
                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<Me> call = getResponse.updateMeDriver("Token "+token,requestBody);
                    call.enqueue(new Callback<Me>() {
                        @Override
                        public void onResponse(Call<Me> call, Response<Me> response) {
                            if(response.code()==200){
                                Toast.makeText(getActivity(),"Facebook отвязали",Toast.LENGTH_SHORT).show();
                                fb.setText("Привязать");
                            }
                            else {
                                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Me> call, Throwable t) {
                        }
                    });
                }
                else if(fb.getText().toString().trim().equals("Привязать")){
                    LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile","email"));
                }
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(google.getText().toString().trim().equals("Отвязать")){

                    RequestBody requestBody;
                    requestBody= new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("gmail_email", "")
                            .build();
                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<Me> call = getResponse.updateMeDriver("Token "+token,requestBody);
                    call.enqueue(new Callback<Me>() {
                        @Override
                        public void onResponse(Call<Me> call, Response<Me> response) {
                            if(response.code()==200){
                                Toast.makeText(getActivity(),"Google отвязали",Toast.LENGTH_SHORT).show();
                                google.setText("Привязать");
                            }
                            else {
                                Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Me> call, Throwable t) {
                        }
                    });
                }
                else if(google.getText().toString().trim().equals("Привязать")){
                    signIn();
                }
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut();

        return view;
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
        else {
            try {
                callbackManager.onActivityResult(requestCode, resultCode, data);
            }
            catch (Exception e){
            }
        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.


            RequestBody requestBody;
            requestBody= new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("gmail_email", account.getEmail())
                    .build();
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<Me> call = getResponse.updateMeDriver("Token "+token,requestBody);
            call.enqueue(new Callback<Me>() {
                @Override
                public void onResponse(Call<Me> call, Response<Me> response) {
                    if(response.code()==200){
                        Toast.makeText(getActivity(),"Google привязали",Toast.LENGTH_SHORT).show();
                        google.setText("Отвязать");
                        mGoogleSignInClient.signOut();
                    }
                    else {
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Me> call, Throwable t) {
                }
            });

            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }

    void getMe(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<Me> call = getResponse.getMeDriver("Token "+token);
        call.enqueue(new Callback<Me>() {
            @Override
            public void onResponse(Call<Me> call, Response<Me> response) {
                if(response.code()==200){
                    fio.setText(response.body().getFio().toString());
                    phone.setText(response.body().getPhone().toString());
                    cityTv.setText(response.body().getLiveCity().toString());
                    countryTv.setText(response.body().getLiveCountry().toString());
                    if(!response.body().getEmail().equals("-"))
                        email.setText(response.body().getEmail().toString());
                    else
                        email.setHint("Email");
                    if(response.body().getFb_email()!=null&&!response.body().getFb_email().equals("")){
                        fb.setText("Отвязать");
                    }
                    else {
                        fb.setText("Привязать");
                    }
                    if(response.body().getGmail_email()!=null&&!response.body().getGmail_email().equals("")){
                        google.setText("Отвязать");
                    }
                    else {
                        google.setText("Привязать");
                    }
                }
                else {
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Me> call, Throwable t) {

            }
        });
    }

    void getCountries(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<List<Country>> call = getResponse.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response.code()==200){
                    countryList=response.body();
                    for(int i=0;i<countryList.size();i++){
                        if(countryList.get(i).getName().equals(countryTv.getText().toString().trim())){
                            getCities(countryList.get(i).getId());
                            break;
                        }
                    }
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
}