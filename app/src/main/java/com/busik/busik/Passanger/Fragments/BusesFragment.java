package com.busik.busik.Passanger.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.IOnBackPressed;
import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.Adapters.MyFlightAdapter;
import com.busik.busik.Passanger.Adapters.TripAdapter;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.Passanger.ApiResponse.MyApplication;
import com.busik.busik.Passanger.ApiResponse.MyFlight;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusesFragment extends Fragment implements IOnBackPressed, SwipeRefreshLayout.OnRefreshListener{


    TripAdapter tripAdapter;
    TextView mybuses,allbuses;
    View mb,ab;
    RecyclerView recyclerView;
    ImageView back1,back2;
    LinearLayout search,myBuses,allBuses;

    Button archivedTrips;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    MyFlightAdapter myFlightAdapter;
    LinearLayout llmain,llsearch,llresult;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    LinearLayout mainLayout,cityLayout;
    int curentStep=0;
    int curentStepA=0;

    boolean cityOn=false;

    List<Integer> tripsId=new ArrayList<>();
    List<MyFlight> myFlights=new ArrayList<>();


    EditText pricePFrom,pricePTo,priceLFrom,priceLTo;
    Call<List<Buses>> callAllBuses;
    Call<List<MyApplication>> callId;
    Call<List<MyApplication>> callIdA;

    Call<List<MyFlight>> callMy;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buses, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        recyclerView=view.findViewById(R.id.rv_buses);
        search=view.findViewById(R.id.ll_search);
        myBuses=view.findViewById(R.id.ll_my_buses);
        archivedTrips=view.findViewById(R.id.cv_archived_trips);
        mSwipeRefreshLayout=view.findViewById(R.id.swipe_container);
        allBuses=view.findViewById(R.id.ll_all_buses);
        mybuses=view.findViewById(R.id.tv_my_buses);
        mb=view.findViewById(R.id.v_my_buses);
        allbuses=view.findViewById(R.id.tv_all_buses);
        ab=view.findViewById(R.id.v_all_buses);
//        back1=view.findViewById(R.id.iv_b1);
//        back2=view.findViewById(R.id.iv_b2);
//        llmain=view.findViewById(R.id.ll_main);
//        llsearch=view.findViewById(R.id.ll_search_show);
//        llresult=view.findViewById(R.id.ll_result);
        pricePFrom=view.findViewById(R.id.et_price_p_from);
        priceLFrom=view.findViewById(R.id.et_price_l_from);
        pricePTo=view.findViewById(R.id.et_price_p_to);
        priceLTo=view.findViewById(R.id.et_price_l_to);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        LinearLayoutManager layoutManagerarch
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int pLast=linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(linearLayoutManager.findFirstCompletelyVisibleItemPosition()==0){
                    mSwipeRefreshLayout.setEnabled(true);
                }
                else {
                    mSwipeRefreshLayout.setEnabled(false);
                }

            }


        });

        archivedTrips.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PassangerMainActivity)getActivity()).replaceFragment(new AchivedFlightsFragment());
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((PassangerMainActivity)getActivity()).setIsSearch(true);
//                llmain.setVisibility(View.GONE);
//                llsearch.setVisibility(View.VISIBLE);
                ((PassangerMainActivity)getActivity()).replaceFragment(new SearchV2Fragment());
            }
        });

        myBuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archivedTrips.setVisibility(View.VISIBLE);
                mybuses.setTextColor(Color.BLACK);
                mb.setVisibility(View.VISIBLE);
                allbuses.setTextColor(Color.parseColor("#898989"));
                ab.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                ((PassangerMainActivity)getActivity()).setisAllBuses(false);
//                ((PassangerMainActivity)getActivity()).setAllBuses(null);
                ((PassangerMainActivity)getActivity()).setAllBusesAdapterPos(0);
                getMyBuses();
            }
        });

//        back1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });


//        back2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });

        allBuses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                archivedTrips.setVisibility(View.GONE);
                allbuses.setTextColor(Color.BLACK);
                ab.setVisibility(View.VISIBLE);
                mybuses.setTextColor(Color.parseColor("#898989"));
                mb.setVisibility(View.GONE);
                recyclerView.setAdapter(null);
                getAllBuses();
//                ((PassangerMainActivity)getActivity()).setMyBusesListArchived(null);
//                ((PassangerMainActivity)getActivity()).setMyBusesList(null);
                ((PassangerMainActivity)getActivity()).setisAllBuses(true);
            }
        });

        if(((PassangerMainActivity)getActivity()).isAllBuses()){
            archivedTrips.setVisibility(View.GONE);
            allbuses.setTextColor(Color.BLACK);
            ab.setVisibility(View.VISIBLE);
            mybuses.setTextColor(Color.parseColor("#898989"));
            mb.setVisibility(View.GONE);
            recyclerView.setAdapter(null);
            getAllBuses();
        }
        else {
            getMyBuses();
        }
        mSwipeRefreshLayout.setEnabled(false);

        return view;
    }


    private boolean isRecyclerViewAtTop()   {
        if(recyclerView.getChildCount() == 0)
            return true;
        return recyclerView.getChildAt(0).getTop() == 0;
    }

    void getAllBuses(){
//        if(((PassangerMainActivity)getActivity()).getAllBuses()==null){
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

            callAllBuses= getResponse.getAllBuses("Token "+token);
            callAllBuses.enqueue(new Callback<List<Buses>>() {
                @Override
                public void onResponse(Call<List<Buses>> call, Response<List<Buses>> response) {
                    if(response.code()==200){
                        for(int i=0;i<response.body().size();i++){
                            Date d = null;
                            SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
                            try {
                                d=formatter6.parse(response.body().get(i).getDateDeparture());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            response.body().get(i).setDateSort(d);
                        }

                        Collections.sort(response.body());
                        List<Buses> buses=new ArrayList<>();
                        for(int i=0;i<response.body().size();i++){
                            buses.add(response.body().get(i));
                        }

                        tripAdapter=new TripAdapter(getContext(),buses,token);
//                        ((PassangerMainActivity)getActivity()).setAllBuses(response.body());
                        recyclerView.setAdapter(tripAdapter);
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                }
                @Override
                public void onFailure(Call<List<Buses>> call, Throwable t) {

                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
//        }
//        else {
//
//            for(int i=0;i<((PassangerMainActivity)getActivity()).getAllBuses().size();i++){
//                Date d = null;
//                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
//                try {
//                    d=formatter6.parse(((PassangerMainActivity)getActivity()).getAllBuses().get(i).getDateDeparture());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                ((PassangerMainActivity)getActivity()).getAllBuses().get(i).setDateSort(d);
//            }
//
//            Collections.sort(((PassangerMainActivity)getActivity()).getAllBuses());
//            tripAdapter=new TripAdapter(getContext(),((PassangerMainActivity)getActivity()).getAllBuses(),token);
//            recyclerView.setAdapter(tripAdapter);
//            recyclerView.scrollToPosition(((PassangerMainActivity)getActivity()).getAllBusesAdapterPos());
//        }
    }


    void getMyBuses(){
        myFlights.clear();
//        if(((PassangerMainActivity)getActivity()).getMyBusesList()==null){
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

            callMy = getResponse.getMyFlights("Token "+token);
            callMy.enqueue(new Callback<List<MyFlight>>() {
                @Override
                public void onResponse(Call<List<MyFlight>> call, Response<List<MyFlight>> response) {
                    if(response.code()==200){
                        myFlights=response.body();
                        for(int i=0;i<myFlights.size();i++){
                            Date d = null;
                            SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
                            try {
                                d=formatter6.parse(myFlights.get(i).getDateDeparture());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            myFlights.get(i).setDateSort(d);
                        }

                        Collections.sort(myFlights);


                myFlightAdapter=new MyFlightAdapter(getContext(),myFlights,token,false);

//                        ((PassangerMainActivity)getActivity()).setMyBusesList(response.body());

                        recyclerView.setAdapter(myFlightAdapter);
                    }

                    mSwipeRefreshLayout.setRefreshing(false);
                }
                @Override
                public void onFailure(Call<List<MyFlight>> call, Throwable t) {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
//        }
//        else{
//            for(int i=0;i<((PassangerMainActivity)getActivity()).getMyBusesList().size();i++){
//                Date d = null;
//                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
//                try {
//                    d=formatter6.parse(((PassangerMainActivity)getActivity()).getMyBusesList().get(i).getDateDeparture());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                ((PassangerMainActivity)getActivity()).getMyBusesList().get(i).setDateSort(d);
//            }
//            Collections.sort(((PassangerMainActivity)getActivity()).getMyBusesList());
//            myFlightAdapter=new MyFlightAdapter(getContext(),((PassangerMainActivity)getActivity()).getMyBusesList(),token);
//
//            recyclerView.setAdapter(myFlightAdapter);
//        }
    }

//    void startSet(){
//        if(tripsId.size()>0){
//            if(curentStep<tripsId.size()){
//                setAppId(tripsId.get(curentStep));
//            }
//            else {
//                myFlightAdapter=new MyFlightAdapter(getContext(),myFlights,token,false);
//                curentStep=0;
//                recyclerView.setAdapter(myFlightAdapter);
//            }
//        }
//    }
//
//    void startSetA(){
//        if(tripsIdA.size()>0){
//            if(curentStepA<tripsIdA.size()){
//                setAppIdA(tripsIdA.get(curentStepA));
//            }
//            else {
//                myFlightAdapterArchived=new MyFlightAdapter(getContext(),myFlightsA,token,true);
////                        ((PassangerMainActivity)getActivity()).setMyBusesListArchived(response.body());
//                rvArchived.setAdapter(myFlightAdapterArchived);
//                rvArchived.setVisibility(View.VISIBLE);
//                curentStepA=0;
//            }
//        }
//    }
//
//    void setAppId(int idTrip){
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("flight", String.valueOf(idTrip))
//                .build();
//        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//        callId = getResponse.myApplication("Token "+token,requestBody);
//        callId.enqueue(new Callback<List<MyApplication>>() {
//            @Override
//            public void onResponse(Call<List<MyApplication>> call, Response<List<MyApplication>> response) {
//                if(response.code()==200){
//                    for(int i=0;i<response.body().size();i++){
//                        for(int j=0;j<myFlights.size();j++){
//                            if(myFlights.get(j).getId()==response.body().get(i).getFlight()){
//                                if(myFlights.get(j).getAppId()==0){
//                                    myFlights.get(j).setAppId(response.body().get(i).getId());
//                                    break;
//                                }
//                                else {
//                                }
//                            }
//                        }
//                    }
//                    curentStep++;
//                    startSet();
//                }
//            }
//            @Override
//            public void onFailure(Call<List<MyApplication>> call, Throwable t) {
//
//            }
//        });
//    }
//
//    void setAppIdA(int idTrip){
//
//        RequestBody requestBody = new MultipartBody.Builder()
//                .setType(MultipartBody.FORM)
//                .addFormDataPart("flight", String.valueOf(idTrip))
//                .build();
//        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//        callIdA = getResponse.myApplication("Token "+token,requestBody);
//        callIdA.enqueue(new Callback<List<MyApplication>>() {
//            @Override
//            public void onResponse(Call<List<MyApplication>> call, Response<List<MyApplication>> response) {
//                if(response.code()==200){
//                    for(int i=0;i<response.body().size();i++){
//                        for(int j=0;j<myFlightsA.size();j++){
//                            if(myFlightsA.get(j).getId()==response.body().get(i).getFlight()){
//                                if(myFlightsA.get(j).getAppId()==0){
//                                    myFlightsA.get(j).setAppId(response.body().get(i).getId());
//                                    break;
//                                }
//                                else {
//                                }
//                            }
//                        }
//                    }
//                    curentStepA++;
//                    startSetA();
//                }
//            }
//            @Override
//            public void onFailure(Call<List<MyApplication>> call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onRefresh() {
        if(((PassangerMainActivity)getActivity()).isAllBuses()){
            archivedTrips.setVisibility(View.GONE);
            allbuses.setTextColor(Color.BLACK);
            ab.setVisibility(View.VISIBLE);
            mybuses.setTextColor(Color.parseColor("#898989"));
            mb.setVisibility(View.GONE);
            recyclerView.setAdapter(null);
            getAllBuses();
        }
        else {
            getMyBuses();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(callAllBuses!=null) {
            callAllBuses.cancel();
        }
        if(callMy!=null){
            callMy.cancel();
        }
        if(callId!=null){
            callId.cancel();
        }
        if(callIdA!=null){
            callIdA.cancel();
        }
    }

    @Override
    public boolean onBackPressed() {
        if(((PassangerMainActivity)getActivity()).getIsSearch()){
            if(((PassangerMainActivity)getActivity()).getFilterFound()){
                llresult.setVisibility(View.GONE);

                ((PassangerMainActivity)getActivity()).setFilterFound(false);
                llsearch.setVisibility(View.VISIBLE);
            }
            else if(cityOn){
                cityOn=false;
                mainLayout.setVisibility(View.VISIBLE);
                cityLayout.setVisibility(View.GONE);
            }
            else {
                llsearch.setVisibility(View.GONE);
                ((PassangerMainActivity)getActivity()).setIsSearch(false);
                llmain.setVisibility(View.VISIBLE);
            }
            return true;
        }
        else {
            return false;
        }
    }
}