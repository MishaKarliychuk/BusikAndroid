package com.busik.busik.Passanger.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.Adapters.FilterTripAdapter;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.Passanger.Models.SearchModel;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ResultSearchFragment extends Fragment implements IOnBackPressed,SwipeRefreshLayout.OnRefreshListener {

    FilterTripAdapter tripAdapter1;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    String token;
    RecyclerView recyclerViewResult;
    LinearLayout search1;
    TextView searchFilter,countFount;
    SearchModel searchModel;
    CardView cr;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_search, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);




        recyclerViewResult=view.findViewById(R.id.rv_found_trip);
        search1=view.findViewById(R.id.ll_search1);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        searchFilter=view.findViewById(R.id.tv_search_filter);
        cr=view.findViewById(R.id.cv_r);
        countFount=view.findViewById(R.id.tv_result_found);
        swipeRefreshLayout.setOnRefreshListener(this);

        LinearLayoutManager layoutManager1
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerViewResult.setLayoutManager(layoutManager1);

        result();

        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }


    void result(){
        ((PassangerMainActivity)getActivity()).setFilterFound(true);
        searchModel= ((PassangerMainActivity)getActivity()).getSearchModel();
        searchFilter.setText("");

//        if(((PassangerMainActivity) getActivity()).getResultSearchList()==null){
            getBusesFilter();
//        }
//        else {
//            for(int i=0;i<((PassangerMainActivity) getActivity()).getResultSearchList().size();i++){
//                Date d = null;
//                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
//                try {
//                    d=formatter6.parse(((PassangerMainActivity) getActivity()).getResultSearchList().get(i).getDateDeparture());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                ((PassangerMainActivity) getActivity()).getResultSearchList().get(i).setDateSort(d);
//            }
//
//            Collections.sort(((PassangerMainActivity) getActivity()).getResultSearchList());
//            tripAdapter1=new FilterTripAdapter(getContext(),((PassangerMainActivity) getActivity()).getResultSearchList(),token);
//            countFount.setText("Найдено "+((PassangerMainActivity) getActivity()).getResultSearchList().size()+" доступніх рейсов");
//            recyclerViewResult.scrollToPosition(((PassangerMainActivity) getActivity()).getResultSearchAdapterPos());
//
//            recyclerViewResult.setAdapter(tripAdapter1);
//        }


//        if(searchModel.getFromCountry()==null&&searchModel.getFromCity()==null&&searchModel.getToCountry()==null&&searchModel.getToCity()==null&&searchModel.getHumanPriceFrom()==0&&searchModel.getHumanPriceTo()==0&&searchModel.getLaggagePriceFrom()==0&&searchModel.getLaggagePriceTo()==0&&searchModel.getDateDFrom()==null) {
//            searchFilter.setText("");
//        }else {
//
////            int d=searchModel.getDate().get(Calendar.DAY_OF_WEEK);
////
////            String day="";
////            if(d==1){
////                day="Нд";
////            }
////            else if(d==2){
////                day="Пн";
////            }
////            else if(d==3){
////                day="Вт";
////            }
////            else if(d==4){
////                day="Ср";
////            }
////            else if(d==5){
////                day="Чт";
////            }
////            else if(d==6){
////                day="Пт";
////            }
////            else if(d==7){
////                day="Сб";
////            }
////
////            int m=searchModel.getDate().get(Calendar.MONTH);
////            m++;
////            searchFilter.setText(searchModel.getFromCity()+", "+searchModel.getFromCountry()+" -> "+searchModel.getToCity()+", "+searchModel.getToCountry()+"\n"+
////                    day+","+searchModel.getDate().get(Calendar.DAY_OF_MONTH)+"."+m);
//        }





        search1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }




    void getBusesFilter(){
        if(     searchModel.getFromCountry()==null&&searchModel.getFromCity()==null&&
                searchModel.getToCountry()==null&&searchModel.getToCity()==null&&
                searchModel.getHumanPriceFrom()==0&&searchModel.getHumanPriceTo()==0&&
                searchModel.getLaggagePriceFrom()==0&&searchModel.getLaggagePriceTo()==0&&
                searchModel.getDateDFrom()==null&&searchModel.getDateDTO()==null&&
                searchModel.getDateTFrom()==null&&searchModel.getDateTTO()==null){

//            MultipartBody.Builder M=new MultipartBody.Builder();
//            M.setType(MultipartBody.FORM);
//            Calendar c = searchModel.getDate();
//            int y=c.get(Calendar.YEAR);
//            int m=c.get(Calendar.MONTH);
//            m++;
//            int d=c.get(Calendar.DAY_OF_MONTH);
//            String year=y+"";
//            String month;
//            String day;
//            if(m<10){
//                month="0"+m;
//            }
//            else {
//                month=m+"";
//            }
//            if(d<10){
//                day="0"+d;
//            }
//            else {
//                day=d+"";
//            }
//
//            M.addFormDataPart("date_departure", year+"-"+month+"-"+day);
//            RequestBody requestBody= M.build();
//
//            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//            Call<List<Buses>> call = getResponse.getAllBusesFilter("Token "+token,requestBody);
//            call.enqueue(new Callback<List<Buses>>() {
//                @Override
//                public void onResponse(Call<List<Buses>> call, Response<List<Buses>> response) {
//                    if(response.code()==200){
//
//                        tripAdapter1=new TripAdapter(getContext(),response.body(),token);
//                        countFount.setText("Найдено "+response.body().size()+" доступніх рейсов");
//
//                        recyclerViewResult.setAdapter(tripAdapter1);
//                    }
//                }
//                @Override
//                public void onFailure(Call<List<Buses>> call, Throwable t) {
//
//                }
//            });


            cr.setVisibility(View.GONE);
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

            Call<List<Buses>> call = getResponse.getAllBuses("Token "+token);
            call.enqueue(new Callback<List<Buses>>() {
                @Override
                public void onResponse(Call<List<Buses>> call, Response<List<Buses>> response) {
                    swipeRefreshLayout.setRefreshing(false);
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

                        tripAdapter1=new FilterTripAdapter(getContext(),response.body(),token);

                        countFount.setText("Знайдено "+response.body().size()+" доступних рейсів");
                        recyclerViewResult.setAdapter(tripAdapter1);
                        ((PassangerMainActivity) getActivity()).setResultSearchList(response.body());
                    }
                }
                @Override
                public void onFailure(Call<List<Buses>> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        else {
            MultipartBody.Builder M=new MultipartBody.Builder();
            M.setType(MultipartBody.FORM);
            if(searchModel.getFromCountry()!=null&&!searchModel.getFromCountry().equals("Звідки")){
                M.addFormDataPart("country_from", searchModel.getFromCountry());
                if(searchModel.getFromCity()!=null&&!searchModel.getFromCity().equals("Звідки")){
                    M.addFormDataPart("city_from", searchModel.getFromCity());
                    searchFilter.setText(searchModel.getFromCity()+", "+searchModel.getFromCountry());
                }
                else {
                    searchFilter.setText(searchModel.getFromCountry());
                }
            }
            if(searchModel.getToCountry()!=null&&!searchModel.getToCountry().equals("Звідки")){
                M.addFormDataPart("country_to", searchModel.getToCountry());
                if(searchModel.getToCity()!=null&&!searchModel.getToCity().equals("Куди")){
                    M.addFormDataPart("city_to", searchModel.getToCity());
                    searchFilter.setText(searchFilter.getText()+"\n"+searchModel.getToCity()+", "+searchModel.getToCountry());
                }
                else {
                    searchFilter.setText(searchModel.getToCountry());
                }
            }
            if(searchModel.getHumanPriceFrom()>0){
                M.addFormDataPart("price_1_person__gte", String.valueOf(searchModel.getHumanPriceFrom()));
                searchFilter.setText(searchFilter.getText()+"\n"+"Ціна за пасажира от: "+searchModel.getHumanPriceFrom());
            }
            if(searchModel.getHumanPriceTo()>0){
                M.addFormDataPart("price_1_person__lte", String.valueOf(searchModel.getHumanPriceTo()));
                searchFilter.setText(searchFilter.getText()+"\n"+"Ціна за пасажира до: "+searchModel.getHumanPriceTo());
            }
            if(searchModel.getLaggagePriceFrom()>0){
                M.addFormDataPart("price_1_kg_package__gte", String.valueOf(searchModel.getLaggagePriceFrom()));
                searchFilter.setText(searchFilter.getText()+"\n"+"Ціна за груз от: "+searchModel.getLaggagePriceFrom());
            }
            if(searchModel.getLaggagePriceTo()>0){
                M.addFormDataPart("price_1_kg_package__lte", String.valueOf(searchModel.getLaggagePriceTo()));
                searchFilter.setText(searchFilter.getText()+"\n"+"Ціна за груз до: "+searchModel.getLaggagePriceTo());
            }

            String departD="";
            if(searchModel.getDateDFrom()!=null){
                Calendar c = searchModel.getDateDFrom();
                int y=c.get(Calendar.YEAR);
                int m=c.get(Calendar.MONTH);
                m++;
                int d=c.get(Calendar.DAY_OF_MONTH);
                String year=y+"";
                String month;
                String day;
                if(m<10){
                    month="0"+m;
                }
                else {
                    month=m+"";
                }
                if(d<10){
                    day="0"+d;
                }
                else {
                    day=d+"";
                }
                M.addFormDataPart("date_departure__gte", year+"-"+month+"-"+day);
                departD="дата відправлення: "+year+"-"+month+"-"+day;
            }
            if(searchModel.getDateDTO()!=null){
                if(searchModel.getDateDFrom().equals(searchModel.getDateDTO())){
                    Calendar c = searchModel.getDateDTO();
                    int y=c.get(Calendar.YEAR);
                    int m=c.get(Calendar.MONTH);
                    m++;
                    int d=c.get(Calendar.DAY_OF_MONTH);
                    String year=y+"";
                    String month;
                    String day;
                    if(m<10){
                        month="0"+m;
                    }
                    else {
                        month=m+"";
                    }
                    if(d<10){
                        day="0"+d;
                    }
                    else {
                        day=d+"";
                    }
                    M.addFormDataPart("date_departure__lte", year+"-"+month+"-"+day+" 23:59:59");
                }
                else {
                    Calendar c = searchModel.getDateDTO();
                    int y=c.get(Calendar.YEAR);
                    int m=c.get(Calendar.MONTH);
                    m++;
                    int d=c.get(Calendar.DAY_OF_MONTH);
                    String year=y+"";
                    String month;
                    String day;
                    if(m<10){
                        month="0"+m;
                    }
                    else {
                        month=m+"";
                    }
                    if(d<10){
                        day="0"+d;
                    }
                    else {
                        day=d+"";
                    }
                    M.addFormDataPart("date_departure__lte", year+"-"+month+"-"+day+" 23:59:59");
                    departD=departD+" до "+year+"-"+month+"-"+day;
                }
            }
            searchFilter.setText(searchFilter.getText()+"\n"+departD);
            String arrivalD="";

            if(searchModel.getDateTFrom()!=null){
                Calendar c = searchModel.getDateTFrom();
                int y=c.get(Calendar.YEAR);
                int m=c.get(Calendar.MONTH);
                m++;
                int d=c.get(Calendar.DAY_OF_MONTH);
                String year=y+"";
                String month;
                String day;
                if(m<10){
                    month="0"+m;
                }
                else {
                    month=m+"";
                }
                if(d<10){
                    day="0"+d;
                }
                else {
                    day=d+"";
                }
                M.addFormDataPart("date_arrival__gte", year+"-"+month+"-"+day);
                arrivalD="Дата прибиття: "+year+"-"+month+"-"+day;
            }
            if(searchModel.getDateTTO()!=null){
                if(searchModel.getDateTFrom().equals(searchModel.getDateTTO())){
                    Calendar c = searchModel.getDateTTO();
                    int y=c.get(Calendar.YEAR);
                    int m=c.get(Calendar.MONTH);
                    m++;
                    int d=c.get(Calendar.DAY_OF_MONTH);
                    String year=y+"";
                    String month;
                    String day;
                    if(m<10){
                        month="0"+m;
                    }
                    else {
                        month=m+"";
                    }
                    if(d<10){
                        day="0"+d;
                    }
                    else {
                        day=d+"";
                    }
                    M.addFormDataPart("date_departure__lte", year+"-"+month+"-"+day+" 23:59:59");
                }
                else {
                    Calendar c = searchModel.getDateTTO();
                    int y=c.get(Calendar.YEAR);
                    int m=c.get(Calendar.MONTH);
                    m++;
                    int d=c.get(Calendar.DAY_OF_MONTH);
                    String year=y+"";
                    String month;
                    String day;
                    if(m<10){
                        month="0"+m;
                    }
                    else {
                        month=m+"";
                    }
                    if(d<10){
                        day="0"+d;
                    }
                    else {
                        day=d+"";
                    }
                    M.addFormDataPart("date_arrival__lte", year+"-"+month+"-"+day+" 23:59:59");
                    arrivalD=arrivalD+" до "+year+"-"+month+"-"+day;
                }
            }
            searchFilter.setText(searchFilter.getText()+"\n"+arrivalD);
            RequestBody requestBody= M.build();

            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

            Call<List<Buses>> call = getResponse.getAllBusesFilter("Token "+token,requestBody);
            call.enqueue(new Callback<List<Buses>>() {
                @Override
                public void onResponse(Call<List<Buses>> call, Response<List<Buses>> response) {
                    swipeRefreshLayout.setRefreshing(false);
                    if(response.code()==200){

                        tripAdapter1=new FilterTripAdapter(getContext(),response.body(),token);
                        countFount.setText("Знайдено "+response.body().size()+" доступних рейсів");

                        recyclerViewResult.setAdapter(tripAdapter1);
                    }
                }
                @Override
                public void onFailure(Call<List<Buses>> call, Throwable t) {

                    swipeRefreshLayout.setRefreshing(false);
                }
            });
            searchFilter.setText(searchFilter.getText().toString().trim());
        }

    }

    @Override
    public boolean onBackPressed() {
        ((PassangerMainActivity)getActivity()).setResultSearchList(null);
        return false;
    }

    @Override
    public void onRefresh() {
        result();
    }
}