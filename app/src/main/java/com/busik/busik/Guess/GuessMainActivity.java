package com.busik.busik.Guess;

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

import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.Passanger.ApiResponse.Driver;
import com.busik.busik.Passanger.Models.SearchModel;
import com.busik.busik.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class GuessMainActivity extends AppCompatActivity {
    static GuessMainActivity instance;

    boolean doubleBackToExitPressedOnce = false;
    List<Buses> allBuses=null;
    int allBusesAdapterPos=0;
    int idShowedTrip;
    Driver driver;
    boolean isBus=false;
    SearchModel searchModel=null;
    List<Buses> resultSearchList=null;
    int resultSearchAdapterPos=0;
    boolean filterFound=false;
    int tripDetailId=0;

    boolean isAllBuses=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_guess_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        replaceFragment(new NotLoginAllBusesFragment());
    }

    public boolean isBus() {
        return isBus;
    }

    public void setBus(boolean bus) {
        isBus = bus;
    }

    public int getResultSearchAdapterPos() {
        return resultSearchAdapterPos;
    }

    public void setResultSearchAdapterPos(int resultSearchAdapterPos) {
        this.resultSearchAdapterPos = resultSearchAdapterPos;
    }

    public List<Buses> getResultSearchList() {
        return resultSearchList;
    }

    public void setResultSearchList(List<Buses> resultSearchList) {
        this.resultSearchList = resultSearchList;
    }

    public boolean getFilterFound(){
        return filterFound;
    }

    public void setFilterFound(boolean b){
        filterFound=b;
    }

    public void setSearchModel(SearchModel s){
        searchModel=s;
    }

    public SearchModel getSearchModel(){
        return searchModel;
    }

    public Driver getCurentDriver(){
        return driver;
    }

    public void setDriver(Driver d){
        driver=d;
    }

    public int gettripDetailId(){
        return tripDetailId;
    }

    public void setTripDetailId(int id){
        tripDetailId=id;
    }
    public void setisAllBuses(boolean allBuses) {
        isAllBuses = allBuses;
    }

    public List<Buses> getAllBuses() {
        return allBuses;
    }

    public void setAllBuses(List<Buses> allBuses) {
        this.allBuses = allBuses;
    }

    public int getAllBusesAdapterPos() {
        return allBusesAdapterPos;
    }

    public void setAllBusesAdapterPos(int allBusesAdapterPos) {
        this.allBusesAdapterPos = allBusesAdapterPos;
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
                    selectedFragment = new NotLoginAllBusesFragment();
                    break;
                case R.id.course:
                    selectedFragment = new NotAuthMessagesFragment();
                    break;
                case R.id.profile:
                    selectedFragment = new NotAuthCabinetFragment();
                    break;
            }

            clearBackStack(getSupportFragmentManager());
            replaceFragment(selectedFragment);

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