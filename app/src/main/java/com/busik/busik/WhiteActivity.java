package com.busik.busik;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.busik.busik.Driver.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WhiteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_white);
        startActivity(new Intent(WhiteActivity.this, MainActivity.class));

    }
}