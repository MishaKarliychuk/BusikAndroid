package com.busik.busik.Passanger.Fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;
import com.busik.busik.Passanger.Models.SearchModel;
import com.busik.busik.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchV2Fragment extends Fragment implements IOnBackPressed {
    LinearLayout mainLayout,cityLayout;

    //mainLayout
    CardView from,to,date,dateTo;
    TextView dateTV,dateTVTo,fromTV,toTV;
    Button Search;


    //cityLayout
    TextView direction,countryTV,cityTV;
    CardView country,city;
    Button con;
    String fromCountry,toCountry,fromCity,toCity;


    boolean cityOn=false;
    Calendar dateAndTimeDFrom=null;
    Calendar dateAndTimeDTO=null;
    ImageView back,back1;
    Calendar dateAndTimeTFrom=null;
    Calendar dateAndTimeTTO=null;

    EditText pricePFrom,pricePTo,priceLFrom,priceLTo;
    List<Country> countryList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_v2, container, false);
        mainLayout=view.findViewById(R.id.ll_main_search);
        cityLayout=view.findViewById(R.id.ll_choose_city);

        from=view.findViewById(R.id.cv_from_city);
        to=view.findViewById(R.id.cv_to_city);
        date=view.findViewById(R.id.cv_date);
        dateTV=view.findViewById(R.id.tv_date);
        dateTo=view.findViewById(R.id.cv_date_to);
        dateTVTo=view.findViewById(R.id.tv_date_to);
        fromTV=view.findViewById(R.id.tv_from);
        toTV=view.findViewById(R.id.tv_to);
        back=view.findViewById(R.id.iv_b1);
        back1=view.findViewById(R.id.iv_b2);
        Search=view.findViewById(R.id.btn_search);

        direction=view.findViewById(R.id.tv_direction);
        country=view.findViewById(R.id.cv_country);
        countryTV=view.findViewById(R.id.tv_country);
        city=view.findViewById(R.id.cv_city);
        cityTV=view.findViewById(R.id.tv_city);
        con=view.findViewById(R.id.btn_con_city);
        pricePFrom=view.findViewById(R.id.et_price_p_from);
        priceLFrom=view.findViewById(R.id.et_price_l_from);
        pricePTo=view.findViewById(R.id.et_price_p_to);
        priceLTo=view.findViewById(R.id.et_price_l_to);

        fromCity=null;
        fromCountry=null;
        toCity=null;
        toCountry=null;
        dateAndTimeDFrom=null;
        dateAndTimeDTO=null;
        dateAndTimeTTO=null;
        dateAndTimeTFrom=null;
//        SearchModel sm = ((PassangerMainActivity)getActivity()).getSearchModel();
//        if(sm==null){
//        }
//        else {
//            if(sm.getFromCountry()==null&&sm.getFromCity()==null&&sm.getToCountry()==null&&sm.getToCity()==null&&sm.getDateDFrom()==null&&sm.getDateDTO()==null&&
//            sm.getDateTFrom()==null&&sm.getDateTTO()==null&&sm.getHumanPriceFrom()==0&&sm.getHumanPriceTo()==0&&sm.getLaggagePriceFrom()==0&&sm.getLaggagePriceTo()==0){
//            }
//            else {
//                if(sm.getFromCity()!=null){
//                    fromTV.setText(sm.getFromCity());
//                }
//                if(sm.getToCity()!=null){
//                    toTV.setText(sm.getToCity());
//                }
//                if(sm.getDateDFrom()!=null){
//                    String departD="";
//
//                    Calendar c = sm.getDateDFrom();
//                    int y=c.get(Calendar.YEAR);
//                    int m=c.get(Calendar.MONTH);
//                    m++;
//                    int d=c.get(Calendar.DAY_OF_MONTH);
//                    String year=y+"";
//                    String month;
//                    String day;
//                    if(m<10){
//                        month="0"+m;
//                    }
//                    else {
//                        month=m+"";
//                    }
//                    if(d<10){
//                        day="0"+d;
//                    }
//                    else {
//                        day=d+"";
//                    }
//                    departD=year+"-"+month+"-"+day;
//                    if(!sm.getDateDFrom().equals(sm.getDateDTO())){
//                        Calendar c1 = sm.getDateDTO();
//                        int y1=c1.get(Calendar.YEAR);
//                        int m1=c1.get(Calendar.MONTH);
//                        m1++;
//                        int d1=c1.get(Calendar.DAY_OF_MONTH);
//                        String year1=y1+"";
//                        String month1;
//                        String day1;
//                        if(m1<10){
//                            month1="0"+m1;
//                        }
//                        else {
//                            month1=m1+"";
//                        }
//                        if(d1<10){
//                            day1="0"+d1;
//                        }
//                        else {
//                            day1=d1+"";
//                        }
//                        departD=departD+" - "+year1+"-"+month1+"-"+day1;
//                    }
//                    dateTV.setText(departD);
//                }
//                if(sm.getDateTFrom()!=null){
//                    String departD="";
//
//                    Calendar c = sm.getDateTFrom();
//                    int y=c.get(Calendar.YEAR);
//                    int m=c.get(Calendar.MONTH);
//                    m++;
//                    int d=c.get(Calendar.DAY_OF_MONTH);
//                    String year=y+"";
//                    String month;
//                    String day;
//                    if(m<10){
//                        month="0"+m;
//                    }
//                    else {
//                        month=m+"";
//                    }
//                    if(d<10){
//                        day="0"+d;
//                    }
//                    else {
//                        day=d+"";
//                    }
//                    departD=year+"-"+month+"-"+day;
//                    if(!sm.getDateTFrom().equals(sm.getDateTTO())){
//                        Calendar c1 = sm.getDateTTO();
//                        int y1=c1.get(Calendar.YEAR);
//                        int m1=c1.get(Calendar.MONTH);
//                        m1++;
//                        int d1=c1.get(Calendar.DAY_OF_MONTH);
//                        String year1=y1+"";
//                        String month1;
//                        String day1;
//                        if(m1<10){
//                            month1="0"+m1;
//                        }
//                        else {
//                            month1=m1+"";
//                        }
//                        if(d1<10){
//                            day1="0"+d1;
//                        }
//                        else {
//                            day1=d1+"";
//                        }
//                        departD=departD+" - "+year1+"-"+month1+"-"+day1;
//                    }
//                    dateTVTo.setText(departD);
//                }
//                if(sm.getHumanPriceFrom()>0){
//                    pricePFrom.setText(sm.getHumanPriceFrom()+"");
//                }
//                if(sm.getHumanPriceTo()>0){
//                    pricePTo.setText(sm.getHumanPriceTo()+"");
//                }
//                if(sm.getLaggagePriceFrom()>0){
//                    priceLFrom.setText(sm.getLaggagePriceFrom()+"");
//                }
//                if(sm.getLaggagePriceTo()>0){
//                    priceLTo.setText(sm.getLaggagePriceTo()+"");
//                }
//            }
//        }


        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityOn=true;
                mainLayout.setVisibility(View.GONE);
                cityLayout.setVisibility(View.VISIBLE);
                direction.setText("Звідки");
                countryTV.setText("Виберіть країну");
                cityTV.setText("Населений пункт");
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityOn=true;
                mainLayout.setVisibility(View.GONE);
                cityLayout.setVisibility(View.VISIBLE);
                direction.setText("Куди");
                countryTV.setText("Виберіть країну");
                cityTV.setText("Населений пункт");
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setDate(getView());
                MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

                // now define the properties of the
                // materialDateBuilder
                materialDateBuilder.setTitleText("SELECT A DATE");

                // now create the instance of the material date
                // picker
                final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                // now handle the positive button click from the
                // material design date picker
                materialDatePicker.addOnPositiveButtonClickListener(
                        new MaterialPickerOnPositiveButtonClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                Pair<Long,Long> l= (Pair<Long, Long>) selection;
                                Long f=l.first;
                                Long s=l.second;
//                                dateTV.setText("" + materialDatePicker.getHeaderText());
                                dateAndTimeDFrom=Calendar.getInstance();
                                dateAndTimeDFrom.setTime(new Date(f));
                                dateAndTimeDTO=Calendar.getInstance();
                                dateAndTimeDTO.setTime(new Date(s));
                                if(dateAndTimeDFrom.equals(dateAndTimeDTO)){
                                }
                                else {
                                }


                                String departD="";

                                Calendar c = dateAndTimeDFrom;
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
                                departD=year+"-"+month+"-"+day;


                                if(!dateAndTimeDFrom.equals(dateAndTimeDTO)){
                                    Calendar c1 = dateAndTimeDTO;
                                    int y1=c1.get(Calendar.YEAR);
                                    int m1=c1.get(Calendar.MONTH);
                                    m1++;
                                    int d1=c1.get(Calendar.DAY_OF_MONTH);
                                    String year1=y1+"";
                                    String month1;
                                    String day1;
                                    if(m1<10){
                                        month1="0"+m1;
                                    }
                                    else {
                                        month1=m1+"";
                                    }
                                    if(d1<10){
                                        day1="0"+d1;
                                    }
                                    else {
                                        day1=d1+"";
                                    }
                                    departD=departD+" - "+year1+"-"+month1+"-"+day1;
                                }
                                dateTV.setText(departD);
                            }
                        });
            }
        });

        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                setDate(getView());
                MaterialDatePicker.Builder<Pair<Long, Long>> materialDateBuilder = MaterialDatePicker.Builder.dateRangePicker();

                // now define the properties of the
                // materialDateBuilder
                materialDateBuilder.setTitleText("SELECT A DATE");

                // now create the instance of the material date
                // picker
                final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

                materialDatePicker.show(getParentFragmentManager(), "MATERIAL_DATE_PICKER");
                // now handle the positive button click from the
                // material design date picker
                materialDatePicker.addOnPositiveButtonClickListener(
                        new MaterialPickerOnPositiveButtonClickListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onPositiveButtonClick(Object selection) {
                                Pair<Long,Long> l= (Pair<Long, Long>) selection;
                                Long f=l.first;
                                Long s=l.second;
//                                dateTVTo.setText("" + materialDatePicker.getHeaderText());
                                dateAndTimeTFrom=Calendar.getInstance();
                                dateAndTimeTFrom.setTime(new Date(f));
                                dateAndTimeTTO=Calendar.getInstance();
                                dateAndTimeTTO.setTime(new Date(s));
                                if(dateAndTimeTFrom.equals(dateAndTimeTTO)){
                                }
                                else {
                                }



                                String departD="";

                                Calendar c = dateAndTimeTFrom;
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
                                departD=year+"-"+month+"-"+day;


                                if(!dateAndTimeTFrom.equals(dateAndTimeTTO)){
                                    Calendar c1 = dateAndTimeTTO;
                                    int y1=c1.get(Calendar.YEAR);
                                    int m1=c1.get(Calendar.MONTH);
                                    m1++;
                                    int d1=c1.get(Calendar.DAY_OF_MONTH);
                                    String year1=y1+"";
                                    String month1;
                                    String day1;
                                    if(m1<10){
                                        month1="0"+m1;
                                    }
                                    else {
                                        month1=m1+"";
                                    }
                                    if(d1<10){
                                        day1="0"+d1;
                                    }
                                    else {
                                        day1=d1+"";
                                    }
                                    departD=departD+" - "+year1+"-"+month1+"-"+day1;
                                }
                                dateTVTo.setText(departD);
                            }
                        });
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Країна");
                String[] types=new String[countryList.size()];

                for (int i=0;i<countryList.size();i++){
                    types[i]=countryList.get(i).getName();
                }

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        countryTV.setText(types[which]);
                        getCities(countryList.get(which).getId());
                        cityTV.setText("Населений пункт");
                        if(direction.getText().equals("Звідки")){
                            fromCountry=types[which];
                        }
                        else {
                            toCountry=types[which];
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
                b.setTitle("Місто");
                String[] types = new String[cityList.size()];

                for(int i=0;i<cityList.size();i++){
                    types[i]=cityList.get(i).getName();
                }

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        cityTV.setText(types[which]);
                        if(direction.getText().equals("Звідки")){
                            fromCity=types[which];
                        }
                        else {
                            toCity=types[which];
                        }
                    }

                });


                if(cityList.size()>0)
                    b.show();
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!cityTV.getText().equals("Населений пункт")){
                    if(direction.getText().equals("Звідки")){
                        fromTV.setText(cityTV.getText());
                    }
                    else {
                        toTV.setText(cityTV.getText());
                    }
                    countryTV.setText("Виберіть країну");
                    cityTV.setText("Населений пункт");

                    cityOn=false;
                    mainLayout.setVisibility(View.VISIBLE);
                    cityLayout.setVisibility(View.GONE);
                }
                else {
                    if(direction.getText().equals("Звідки")){
                        fromTV.setText("Звідки");
                    }
                    else {
                        toTV.setText("Куди");
                    }
                    countryTV.setText("Виберіть країну");
                    cityTV.setText("Населений пункт");

                    cityOn=false;
                    mainLayout.setVisibility(View.VISIBLE);
                    cityLayout.setVisibility(View.GONE);
                }
                cityList.clear();
                if(direction.getText().equals("Звідки")){
                    if(fromCity!=null){
                        fromTV.setText(fromCity+", "+fromCountry);
                    }
                    else {
                        fromTV.setText(fromCountry);
                    }
                }
                else {
                    if(toCity!=null){
                        toTV.setText(toCity+", "+toCountry);
                    }
                    else {
                        toTV.setText(toCountry);
                    }
                }
            }
        });


        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!fromTV.getText().equals("Звідки")&&!toTV.getText().equals("Куди")){
                boolean isCorect=true;
                try {
                    if(Integer.parseInt(pricePFrom.getText().toString().trim())>0&isCorect){
                        if(Integer.parseInt(pricePTo.getText().toString().trim())<=Integer.parseInt(pricePFrom.getText().toString().trim())){
                            isCorect=false;
                            Toast.makeText(getContext(),"Ціна до должан біть больше чем от",Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception e){

                }
                try {
                    if(Integer.parseInt(priceLFrom.getText().toString().trim())>0&isCorect){
                        if(Integer.parseInt(priceLTo.getText().toString().trim())<=Integer.parseInt(priceLFrom.getText().toString().trim())){
                            isCorect=false;
                            Toast.makeText(getContext(),"Ціна до должан біть больше чем от",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){

                }

                if(isCorect){
                    SearchModel searchModel=new SearchModel(fromCountry,toCountry,fromCity,toCity,dateAndTimeDFrom,dateAndTimeDTO,dateAndTimeTFrom,dateAndTimeTTO);
                    if(!pricePFrom.getText().toString().trim().equals("")&&Integer.parseInt(pricePFrom.getText().toString().trim())>=0){
                        searchModel.setHumanPriceFrom(Integer.parseInt(pricePFrom.getText().toString().trim()));
                    }
                    else {
                        searchModel.setHumanPriceFrom(0);
                    }
                    if(!pricePTo.getText().toString().trim().equals("")&&Integer.parseInt(pricePTo.getText().toString().trim())>=0){
                        searchModel.setHumanPriceTo(Integer.parseInt(pricePTo.getText().toString().trim()));
                    }
                    else {
                        searchModel.setHumanPriceTo(0);
                    }
                    if(!priceLFrom.getText().toString().trim().equals("")&&Integer.parseInt(priceLFrom.getText().toString().trim())>=0){
                        searchModel.setLaggagePriceFrom(Integer.parseInt(priceLFrom.getText().toString().trim()));
                    }
                    else{
                        searchModel.setLaggagePriceFrom(0);
                    }
                    if(!priceLTo.getText().toString().trim().equals("")&&Integer.parseInt(priceLTo.getText().toString().trim())>=0){
                        searchModel.setLaggagePriceTo(Integer.parseInt(priceLTo.getText().toString().trim()));
                    }
                    else {
                        searchModel.setLaggagePriceTo(0);
                    }
                    ((PassangerMainActivity)getActivity()).setSearchModel(searchModel);

//                llsearch.setVisibility(View.GONE);
//                llresult.setVisibility(View.VISIBLE);
//                result();
//                }
                    ((PassangerMainActivity)getActivity()).replaceFragment(new ResultSearchFragment());
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((PassangerMainActivity)getActivity()).setSearchModel(null);
                getActivity().onBackPressed();
            }
        });


        back1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((PassangerMainActivity)getActivity()).setSearchModel(null);
                getActivity().onBackPressed();
            }
        });

        getCountries();


        return view;
    }

    void getCountries(){
        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
        Call<List<Country>> call = getResponse.getAllCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if(response.code()==200){
                    countryList=response.body();
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

    @Override
    public boolean onBackPressed() {
        if(cityOn){
            cityOn=false;
            mainLayout.setVisibility(View.VISIBLE);
            cityLayout.setVisibility(View.GONE);
            return true;
        }
        else {
            ((PassangerMainActivity)getActivity()).setSearchModel(null);
            return false;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        pricePFrom.setText("");
        pricePTo.setText("");
        priceLFrom.setText("");
        priceLTo.setText("");
    }
}