package com.busik.busik;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogActivity extends AppCompatActivity {

    LinearLayout back;
    RelativeLayout pa,dr;
    TextView p,d;
    Button login;
    View vv_p,vv_d;
    boolean asPassanger=true;
    EditText phone,pass;
    private static final String MY_SETTINGS = "TOKEN";
    boolean isCorect=true;
    SharedPreferences sp;
    private static final int RC_SIGN_IN=9001;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    Button createAcc;
    CountryCodePicker ccp;

    Button fbLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        back=findViewById(R.id.ll_back);
        pa=findViewById(R.id.rl_as_p);
        dr=findViewById(R.id.rl_as_d);
        p=findViewById(R.id.tv_as_passanger);
        d=findViewById(R.id.tv_as_driver);
        login=findViewById(R.id.btn_login);
        vv_d=findViewById(R.id.vv_d);
        vv_p=findViewById(R.id.vv_p);
        phone=findViewById(R.id.et_phone_login);
        pass=findViewById(R.id.et_pass_login);
        fbLogin=findViewById(R.id.fb_login);
        ccp=findViewById(R.id.ccp);
        createAcc=findViewById(R.id.btn_create_acc);


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

                                                                        startActivity(new Intent(LogActivity.this, PassangerMainActivity.class));
                                                                        finishAffinity();
                                                                    }
                                                                    else {
                                                                        Toast.makeText(LogActivity.this,"Ві зарегистрировані как водитель",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                                else {
                                                                    if(jsonObject.getString("type_user").equals("driver")){
                                                                        putToken(jsonObject.getString("auth_token"));

                                                                        startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
                                                                        finishAffinity();
                                                                    }
                                                                    else {
                                                                        Toast.makeText(LogActivity.this,"Ви зареєстровані як пасажир",Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    } catch (JSONException e) {
                                                        if(e.getMessage().equals("No value for auth_token")){
                                                        }
                                                        try {
                                                            if(jsonObject.getString("error")!=null){
                                                                Toast.makeText(LogActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
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
                                                else {
                                                    Toast.makeText(LogActivity.this,"Error",Toast.LENGTH_SHORT).show();
                                                }
//                                                if(response.code()==200){
////                                                    putToken(response.body().getAuthToken());
//                                                    if(asPassanger){
//                                                        startActivity(new Intent(LogActivity.this, PassangerMainActivity.class));
//                                                        finishAffinity();
//                                                    }
//                                                    else {
//                                                        startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
//                                                        finishAffinity();
//                                                    }
//                                                }
//                                                else {
//                                                    Toast.makeText(LogActivity.this,"Error",Toast.LENGTH_SHORT).show();
//                                                }

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


        fbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(LogActivity.this, Arrays.asList("public_profile","email"));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LogActivity.this,RegActivity.class));
            }
        });

        pa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    asPassanger=true;
                    vv_d.setVisibility(View.GONE);
                    d.setTextColor(Color.parseColor("#80000000"));

                    vv_p.setVisibility(View.VISIBLE);
                    p.setTextColor(Color.BLACK);

            }
        });

        dr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    asPassanger=false;
                    vv_p.setVisibility(View.GONE);
                    p.setTextColor(Color.parseColor("#80000000"));

                    vv_d.setVisibility(View.VISIBLE);
                    d.setTextColor(Color.BLACK);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isCorect=true;
                if(phone.getText().toString().trim().length()==0&&isCorect){
                    Toast.makeText(LogActivity.this, "Введіть телефон", Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if((pass.getText().toString().trim().length()==0&&isCorect)||(pass.getText().toString().trim().length()<=8&&isCorect)){
                    Toast.makeText(LogActivity.this, "Длина пароля должна біть больше 8 символов.", Toast.LENGTH_SHORT).show();
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

                                                startActivity(new Intent(LogActivity.this, PassangerMainActivity.class));
                                                finishAffinity();
                                            }
                                            else {
                                                Toast.makeText(LogActivity.this,"Ві зарегистрировані как водитель",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    if(e.getMessage().equals("No value for auth_token")){
                                    }
                                    try {
                                        if(jsonObject.getString("error")!=null){
                                            Toast.makeText(LogActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
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
                            }

                            @Override
                            public void onFailure(Call<Object> call, Throwable t) {

                            }
                        });

                    }
                    else {
                        RequestBody requestBody = new MultipartBody.Builder()
                                .setType(MultipartBody.FORM)
                                .addFormDataPart("phone", ccp.getFullNumber().toString().trim())
                                .addFormDataPart("password", pass.getText().toString().trim())
                                .build();



                        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                        Call<Object> call = getResponse.loginDriver(requestBody);
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
                                            if(jsonObject.getString("type_user").equals("driver")){
                                                putToken(jsonObject.getString("auth_token"));

                                                startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
                                                finishAffinity();
                                            }
                                            else {
                                                Toast.makeText(LogActivity.this,"Ви зареєстровані як пасажир",Toast.LENGTH_SHORT).show();
                                            }
//                                            putToken(jsonObject.getString("auth_token"));
//
//                                            startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
//                                            finishAffinity();
                                        }
                                    }
                                } catch (JSONException e) {
                                    if(e.getMessage().equals("No value for auth_token")){
                                    }
                                    try {
                                        if(jsonObject.getString("error")!=null){
                                            Toast.makeText(LogActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
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
//                                        Toast.makeText(LogActivity.this,jsonArray.get(0).toString(),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(LogActivity.this, "Заповніть коректно дані", Toast.LENGTH_SHORT).show();
                }
            }
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        Button signInButton=findViewById(R.id.sign_in_button);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
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

                                        startActivity(new Intent(LogActivity.this, PassangerMainActivity.class));
                                        finishAffinity();
                                        }
                                        else {
                                            Toast.makeText(LogActivity.this,"Ві зарегистрировані как водитель",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        if(jsonObject.getString("type_user").equals("driver")){
                                            putToken(jsonObject.getString("auth_token"));

                                            startActivity(new Intent(LogActivity.this, DriverMainActivity.class));
                                            finishAffinity();
                                        }
                                        else {
                                            Toast.makeText(LogActivity.this,"Ви зареєстровані як пасажир",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            if(e.getMessage().equals("No value for auth_token")){
                            }
                            try {
                                if(jsonObject.getString("error")!=null){
                                    Toast.makeText(LogActivity.this,jsonObject.getString("error"),Toast.LENGTH_SHORT).show();
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
                    else {
                        Toast.makeText(LogActivity.this,"Error",Toast.LENGTH_SHORT).show();
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