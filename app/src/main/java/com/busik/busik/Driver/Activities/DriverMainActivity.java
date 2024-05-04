package com.busik.busik.Driver.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;

import com.busik.busik.Driver.Fragments.DriverMessagesFragment;
import com.busik.busik.Driver.Fragments.MyDriverCabinetFragment;
import com.busik.busik.Driver.Fragments.MyDriverTripsFragment;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.ApiResponse.Driver;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.Models.SearchModel;
import com.busik.busik.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DriverMainActivity extends AppCompatActivity {
    static DriverMainActivity instance;
    SearchModel searchModel;

    int tripDetailId=0;
    Driver driver;
    FullDetail curentTrip;
    boolean doubleBackToExitPressedOnce = false;

    boolean isSearch=false;
    boolean isCity=false;
    DriverFlight showFlight;
    boolean filterFound=false;
    int passengerIdReview;
    boolean goBack;
    int passangerIdReview;
    DriverFlight reviewIdFlight;
    int idMessage;
    boolean isArchived=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_driver_main);


        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        replaceFragment(new MyDriverTripsFragment());
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyDriverTripsFragment()).commit();
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setArchived(boolean archived) {
        isArchived = archived;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public int getPassangerIdReview() {
        return passangerIdReview;
    }

    public void setPassangerIdReview(int p) {
        passangerIdReview = p;
    }

    public DriverFlight getReviewIdFlight() {
        return reviewIdFlight;
    }

    public void setReviewIdFlight(DriverFlight reviewIdFlight) {
        this.reviewIdFlight = reviewIdFlight;
    }

    public boolean isGoBack() {
        return goBack;
    }

    public void setGoBack(boolean goBack) {
        this.goBack = goBack;
    }

    public int getPassengerIdReview() {
        return passengerIdReview;
    }

    public void setPassengerIdReview(int passengerIdReview) {
        this.passengerIdReview = passengerIdReview;
    }

    public void setShowFlight(DriverFlight d){
        showFlight=d;
    }

    public DriverFlight getShowFlight() {
        return showFlight;
    }

    public void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.algorithm:
                    selectedFragment = new MyDriverTripsFragment();
                    isSearch=false;
                    filterFound=false;
                    break;
                case R.id.course:
                    selectedFragment = new DriverMessagesFragment();
                    isSearch=false;
                    filterFound=false;
                    break;
                case R.id.profile:
                    selectedFragment = new MyDriverCabinetFragment();
                    isSearch=false;
                    filterFound=false;
                    break;
            }
            clearBackStack(getSupportFragmentManager());
            replaceFragment(selectedFragment);
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .replace(R.id.fragment_container, selectedFragment)
//                    .commit();
            return true;
        }
    };

    private void clearBackStack(FragmentManager fragmentManager) {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            FragmentManager.BackStackEntry entry = fragmentManager.getBackStackEntryAt(0);
            fragmentManager.popBackStack(entry.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            if(getSupportFragmentManager().getBackStackEntryCount()>1){
                super.onBackPressed();
            }
            else if(getSupportFragmentManager().getBackStackEntryCount()==1){
                if (doubleBackToExitPressedOnce) {
                    super.onBackPressed();
                    System.exit(0);
                    return;
                }
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Натисніть ще раз, щоб вийти", Toast.LENGTH_SHORT).show();

                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce=false;
                    }
                }, 2000);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}