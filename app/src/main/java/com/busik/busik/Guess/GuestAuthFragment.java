package com.busik.busik.Guess;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.MainActivity;
import com.busik.busik.LogActivity;
import com.busik.busik.MyApp;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.Login;
import com.busik.busik.R;
import com.busik.busik.RegActivity;
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
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GuestAuthFragment extends Fragment {
    LinearLayout back;
    Button login;
    boolean asPassanger=true;
    EditText phone,pass;
    private static final String MY_SETTINGS = "TOKEN";
    boolean isCorect=true;
    CallbackManager callbackManager;
    SharedPreferences sp;
    Button logFB,logGoogle;
    private static final int RC_SIGN_IN=9001;
    GoogleSignInClient mGoogleSignInClient;
    CountryCodePicker ccp;
    Button createNewAcc;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_guest_auth, container, false);

        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        back=view.findViewById(R.id.ll_back);
        login=view.findViewById(R.id.btn_login);
        phone=view.findViewById(R.id.et_phone_login);
        pass=view.findViewById(R.id.et_pass_login);
        logFB=view.findViewById(R.id.log_fb);
        logGoogle=view.findViewById(R.id.log_google);
        createNewAcc=view.findViewById(R.id.btn_create_new_acc);
        ccp=view.findViewById(R.id.ccp);


        ccp.registerCarrierNumberEditText(phone);
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


                                        RequestBody requestBody = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("fb_email", email)
                                                .build();


                                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                                        Call<Object> call = getResponse.loginFacebook(requestBody);
                                        call.enqueue(new Callback<Object>() {
                                            @Override
                                            public void onResponse(Call<Object> call, Response<Object> response) {

                                                if(response.code()==200){

                                                    Object body=null;
                                                    String s="";
                                                    if(response.code()==200){
                                                        body=response.body();
                                                    }

                                                    JSONObject jsonObject = null;
                                                    if(response.code()==200){
                                                        try {
                                                            jsonObject= new JSONObject(new Gson().toJson(body));
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                    try {
                                                        if(jsonObject.getString("auth_token")!=null){
                                                            if(response.code()==200){
                                                                if(asPassanger){
                                                                    if(jsonObject.getString("type_user").equals("passenger")){
                                                                        putToken(jsonObject.getString("auth_token"));

                                                                        startActivity(new Intent(getActivity(), PassangerMainActivity.class));
                                                                        getActivity().finish();
                                                                    }
                                                                    else {
                                                                        Toast.makeText(getContext(),"Ві зарегистрировані как водитель",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                else {
                                                                    if(jsonObject.getString("type_user").equals("driver")){
                                                                        putToken(jsonObject.getString("auth_token"));

                                                                        startActivity(new Intent(getActivity(), DriverMainActivity.class));
                                                                        getActivity().finish();
                                                                    }
                                                                    else {
                                                                        Toast.makeText(getContext(),"Ви зареєстровані як пасажир",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        if(e.getMessage().equals("No value for auth_token")){
                                                        }
                                                        try {
                                                            if(jsonObject.getString("error")!=null){
                                                                Toast.makeText(getContext(),jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (JSONException ex) {
                                                            ex.printStackTrace();
                                                        }
//                                    Iterator<String> keys= jsonObject.keys();
//                                    while (keys.hasNext())
//                                    {
//                                        String keyValue = (String)keys.next();
//                                    }
//                                    try {
//                                        JSONArray jsonArray=jsonObject.getJSONArray((String) jsonObject.names().get(0));
//                                        Toast.makeText(LogActivity.this,jsonArray.get(0).toString(),Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException ex) {
//                                        ex.printStackTrace();
//                                    }


                                                        e.printStackTrace();
                                                    }
                                                    //
//                        putToken(response.body().getAuthToken());
//                        if(asPassanger){
//                            startActivity(new Intent(LogActivity.this, PassangerMainActivity.class));
//                            finishAffinity();
//                        }
//                        else {
//                            startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
//                            finishAffinity();
//                        }
                                                }
//                                                if(response.code()==200){
////                                                    putToken(response.body().getAuthToken());
//                                                    if(asPassanger){
////                                                        putToken(response.body().getAuthToken());
//                                                        MyApp.setFromGuest(true);
//                                                        if(((GuessMainActivity)getActivity()).isBus()){
//                                                            MyApp.setGoToBus(true);
//                                                        }
//                                                        startActivity(new Intent(getActivity(), MainActivity.class));
//                                                        getActivity().finish();
//                                                    }
//                                                    else {
//                                                        startActivity(new Intent(getActivity(), DriverMainActivity.class));
//                                                        getActivity().finish();
//                                                    }
//                                                }
                                                else {
                                                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                                                }

                                            }

                                            @Override
                                            public void onFailure(Call<Object> call, Throwable t) {

                                            }
                                        });


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,link,email");
                        request.setParameters(parameters);
                        request.executeAsync();

                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onCancel() {

                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        LoginManager.getInstance().logOut();
                    }

                });


        logFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(getActivity(), Arrays.asList("public_profile","email"));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorect=true;
                if(phone.getText().toString().trim().length()==0&&isCorect){
                    Toast.makeText(getContext(), "Введіть телефон", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if((pass.getText().toString().trim().length()==0&&isCorect)||(pass.getText().toString().trim().length()<=8&&isCorect)){
                    Toast.makeText(getContext(), "Длина пароля должна біть больше 8 символов.", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(isCorect){
                    if(asPassanger){
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("phone", ccp.getFullNumber().toString().trim())
                                .addFormDataPart("password", pass.getText().toString().trim())
                                .build();


                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                        Call<Object> call = getResponse.login(requestBody);
                        call.enqueue(new Callback<Object>() {
                            @Override
                            public void onResponse(Call<Object> call, Response<Object> response) {

                                Object body=null;
                                String s="";
                                if(response.code()==200){
                                    body=response.body();
                                }
                                if(response.code()!=200){
                                    try {
                                        s=response.errorBody().string();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    body=response.errorBody();
                                }

                                JSONObject jsonObject = null;
                                if(response.code()==200){
                                    try {
                                        jsonObject= new JSONObject(new Gson().toJson(body));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    try {
                                        jsonObject= new JSONObject(s);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                try {
                                    if(jsonObject.getString("auth_token")!=null){
                                        if(response.code()==200){
                                            if(jsonObject.getString("type_user").equals("passenger")){
                                                putToken(jsonObject.getString("auth_token"));

                                                MyApp.setFromGuest(true);
                                                if(((GuessMainActivity)getActivity()).isBus()){
                                                    MyApp.setGoToBus(true);
                                                }
                                                startActivity(new Intent(getActivity(), MainActivity.class));
                                                getActivity().finish();
                                            }
                                            else {
                                                Toast.makeText(getActivity(),"Ві зарегистрировані как водитель",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    if(e.getMessage().equals("No value for auth_token")){
                                    }
                                    try {
                                        if(jsonObject.getString("error")!=null){
                                            Toast.makeText(getActivity(),jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException ex) {
                                        ex.printStackTrace();
                                    }
//                                    if(e.getMessage().equals("No value for auth_token")){
//                                    }
//                                    Iterator<String> keys= jsonObject.keys();
//                                    while (keys.hasNext())
//                                    {
//                                        String keyValue = (String)keys.next();
//                                    }
//                                    try {
//                                        JSONArray jsonArray=jsonObject.getJSONArray((String) jsonObject.names().get(0));
//                                        Toast.makeText(getContext(),jsonArray.get(0).toString(),Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException ex) {
//                                        ex.printStackTrace();
//                                    }
//
//
//                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });

                    }
                }
                else{
                    Toast.makeText(getContext(), "Заповніть коректно дані", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),RegActivity.class));
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);
        mGoogleSignInClient.signOut();

        logGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        return view;
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

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    //отримання результату з гугла
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("gmail_email", account.getEmail())
                    .build();


            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
            Call<Object> call = getResponse.loginGoogle(requestBody);
            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {

                    if(response.code()==200){

                        Object body=null;
                        String s="";
                        if(response.code()==200){
                            body=response.body();
                        }

                        JSONObject jsonObject = null;
                        if(response.code()==200){
                            try {
                                jsonObject= new JSONObject(new Gson().toJson(body));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        try {
                            if(jsonObject.getString("auth_token")!=null){
                                if(response.code()==200){
                                    if(asPassanger){
                                        if(jsonObject.getString("type_user").equals("passenger")){
                                            putToken(jsonObject.getString("auth_token"));

                                            startActivity(new Intent(getActivity(), PassangerMainActivity.class));
                                            getActivity().finish();
                                        }
                                        else {
                                            Toast.makeText(getContext(),"Ві зарегистрировані как водитель",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        if(jsonObject.getString("type_user").equals("driver")){
                                            putToken(jsonObject.getString("auth_token"));

                                            startActivity(new Intent(getActivity(), DriverMainActivity.class));
                                            getActivity().finish();
                                        }
                                        else {
                                            Toast.makeText(getContext(),"Ви зареєстровані як пасажир",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            if(e.getMessage().equals("No value for auth_token")){
                            }
                            try {
                                if(jsonObject.getString("error")!=null){
                                    Toast.makeText(getContext(),jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException ex) {
                                ex.printStackTrace();
                            }
//                                    Iterator<String> keys= jsonObject.keys();
//                                    while (keys.hasNext())
//                                    {
//                                        String keyValue = (String)keys.next();
//                                    }
//                                    try {
//                                        JSONArray jsonArray=jsonObject.getJSONArray((String) jsonObject.names().get(0));
//                                        Toast.makeText(LogActivity.this,jsonArray.get(0).toString(),Toast.LENGTH_SHORT).show();
//                                    } catch (JSONException ex) {
//                                        ex.printStackTrace();
//                                    }


                            e.printStackTrace();
                        }
                        //
//                        putToken(response.body().getAuthToken());
//                        if(asPassanger){
//                            startActivity(new Intent(LogActivity.this, PassangerMainActivity.class));
//                            finishAffinity();
//                        }
//                        else {
//                            startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
//                            finishAffinity();
//                        }
                    }
//                    if(response.code()==200){
////                        putToken(response.body().getAuthToken());
//                        if(asPassanger){
////                            putToken(response.body().getAuthToken());
//                            MyApp.setFromGuest(true);
//                            if(((GuessMainActivity)getActivity()).isBus()){
//                                MyApp.setGoToBus(true);
//                            }
//                            startActivity(new Intent(getActivity(), MainActivity.class));
//                            getActivity().finish();
//                        }
//                        else {
//                            startActivity(new Intent(getActivity(), DriverMainActivity.class));
//                            getActivity().finish();
//                        }
//                    }
                    else {
                        Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {

                }
            });
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
        mGoogleSignInClient.signOut();
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
}