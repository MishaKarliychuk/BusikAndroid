package com.busik.busik.Driver;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Guess.GuessMainActivity;
import com.busik.busik.LogActivity;
import com.busik.busik.MyApp;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.R;
import com.busik.busik.RegActivity;
import com.busik.busik.RegFBActivity;
import com.busik.busik.RegGoogleActivity;
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
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    Button log,reg;
    TextView guess;

    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    private static final int RC_SIGN_IN=9001;
    GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    Button regGoogle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        String token = sp.getString("token", null);
        boolean asPassanger=sp.getBoolean("asPassanger",false);


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
                                        String fio=null;
                                        try {
                                            fio=object.getString("name");
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
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
                                        MyApp.setFb_email(email);
                                        MyApp.setFb_name(fio);
                                        startActivity(new Intent(MainActivity.this, RegFBActivity.class));
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

        Button loginButton=findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile","email"));
            }
        });
        if(token!=null){
            if(asPassanger){
                startActivity(new Intent(MainActivity.this, PassangerMainActivity.class));
                finish();
            }
            else {
                startActivity(new Intent(MainActivity.this, DriverMainActivity.class));
                finish();
            }
        }
        else {
        }

        log=findViewById(R.id.btn_login);
        reg=findViewById(R.id.btn_reg);
        regGoogle=findViewById(R.id.reg_google);
        guess=findViewById(R.id.tv_as_guess);

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, LogActivity.class));
            }
        });

        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegActivity.class));
            }
        });

        guess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, GuessMainActivity.class));
                finish();
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut();
        SignInButton signInButton=findViewById(R.id.sign_in_button);
//
//        signInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                    signIn();
//            }
//        });

        regGoogle.setOnClickListener(new View.OnClickListener() {
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
            MyApp.setGoogle_email(account.getEmail());
            MyApp.setGoogle_name(account.getDisplayName());
            startActivity(new Intent(MainActivity.this, RegGoogleActivity.class));
            //updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            //updateUI(null);
        }
    }
}