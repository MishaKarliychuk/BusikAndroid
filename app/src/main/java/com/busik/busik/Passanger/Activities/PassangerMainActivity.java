package com.busik.busik.Passanger.Activities;

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
import com.busik.busik.MyApp;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.Passanger.ApiResponse.Driver;
import com.busik.busik.Passanger.ApiResponse.FullDetail;
import com.busik.busik.Passanger.ApiResponse.MyFlight;
import com.busik.busik.Passanger.Fragments.BusesFragment;
import com.busik.busik.Passanger.Fragments.CabinetPassangerFragment;
import com.busik.busik.Passanger.Fragments.MessagesPasengerFragment;
import com.busik.busik.Passanger.Fragments.TripDetailFragment;
import com.busik.busik.Passanger.Models.SearchModel;
import com.busik.busik.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class PassangerMainActivity extends AppCompatActivity {

    static PassangerMainActivity instance;
    SearchModel searchModel=null;

    int tripDetailId=0;
    Driver driver;
    FullDetail curentTrip;
    boolean isSearch=false;
    boolean isCity=false;
    boolean filterFound=false;

    //ліст і вибраний рейс у пошуку
    List<Buses> resultSearchList=null;
    int resultSearchAdapterPos=0;

    List<MyFlight> myBusesList=null;
    int myFlightAdapterPos=0;

    List<MyFlight> myBusesListArchived=null;
    int myFlightAdapterPosArchived=0;

    List<Buses> allBuses=null;
    int allBusesAdapterPos=0;
    int idShowedTrip;
    int idAppShowed;

    int idDriverReview;
    int idFlightReview;

    int idMessage;
    boolean isAllBuses=true;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance=this;
        setContentView(R.layout.activity_passanger_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if(MyApp.isFromGuest()&&MyApp.isGoToBus()){
            isAllBuses=true;
        }
        replaceFragment(new BusesFragment());
        if(MyApp.isFromGuest()&&MyApp.isGoToBus()){
            MyApp.setFromGuest(false);
            tripDetailId=MyApp.gettripDetailId();
            allBusesAdapterPos = 0;
            replaceFragment(new TripDetailFragment());
        }
        else {
            MyApp.setFromGuest(false);
        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BusesFragment()).commit();

    }

    public int getIdAppShowed() {
        return idAppShowed;
    }

    public void setIdAppShowed(int idAppShowed) {
        this.idAppShowed = idAppShowed;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public int getIdDriverReview() {
        return idDriverReview;
    }

    public void setIdDriverReview(int idDriverReview) {
        this.idDriverReview = idDriverReview;
    }

    public int getIdFlightReview() {
        return idFlightReview;
    }

    public void setIdFlightReview(int idFlightReview) {
        this.idFlightReview = idFlightReview;
    }

    public int getIdShowedTrip() {
        return idShowedTrip;
    }

    public void setIdShowedTrip(int idShowedTrip) {
        this.idShowedTrip = idShowedTrip;
    }

    public boolean isAllBuses() {
        return isAllBuses;
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

    public List<MyFlight> getMyBusesListArchived() {
        return myBusesListArchived;
    }

    public void setMyBusesListArchived(List<MyFlight> myBusesListArchived) {
        this.myBusesListArchived = myBusesListArchived;
    }

    public int getMyFlightAdapterPosArchived() {
        return myFlightAdapterPosArchived;
    }

    public void setMyFlightAdapterPosArchived(int myFlightAdapterPosArchived) {
        this.myFlightAdapterPosArchived = myFlightAdapterPosArchived;
    }

    public int getMyFlightAdapterPos() {
        return myFlightAdapterPos;
    }

    public void setMyFlightAdapterPos(int myFlightAdapterPos) {
        this.myFlightAdapterPos = myFlightAdapterPos;
    }

    public List<MyFlight> getMyBusesList() {
        return myBusesList;
    }

    public void setMyBusesList(List<MyFlight> myBusesList) {
        this.myBusesList = myBusesList;
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

    public boolean isCity() {
        return isCity;
    }

    public void setCity(boolean city) {
        isCity = city;
    }

    public boolean getIsSearch(){
        return isSearch;
    }

    public void setIsSearch(boolean b){
        isSearch=b;
    }

    public boolean getFilterFound(){
        return filterFound;
    }

    public void setFilterFound(boolean b){
        filterFound=b;
    }

    public FullDetail getCurentTrip(){
        return curentTrip;
    }

    public void setCurentTrip(FullDetail f){
        curentTrip=f;
    }

    public int gettripDetailId(){
        return tripDetailId;
    }

    public void setTripDetailId(int id){
        tripDetailId=id;
    }

    public Driver getCurentDriver(){
        return driver;
    }

    public void setDriver(Driver d){
        driver=d;
    }

    public static PassangerMainActivity getInstance() {
        return instance;
    }

    public void setSearchModel(SearchModel s){
        searchModel=s;
    }

    public SearchModel getSearchModel(){
        return searchModel;
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

    public void backMain(){
        clearBackStack(getSupportFragmentManager());
        replaceFragment(new BusesFragment());
//        Fragment selectedFragment = new BusesFragment();
//        isSearch=false;
//        filterFound=false;
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.fragment_container, selectedFragment)
//                .commit();
//        FragmentManager fm = getSupportFragmentManager();
//        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
//            fm.popBackStack();
//        }
//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new BusesFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.algorithm:
                    selectedFragment = new BusesFragment();
                    isSearch=false;
                    filterFound=false;
                    setAllBuses(null);
                    setisAllBuses(true);
                    break;
                case R.id.course:
                    selectedFragment = new MessagesPasengerFragment();
                    isSearch=false;
                    filterFound=false;
                    break;
                case R.id.profile:
                    selectedFragment = new CabinetPassangerFragment();
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