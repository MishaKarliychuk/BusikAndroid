package com.busik.busik.Passanger.Fragments;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;
import com.busik.busik.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class SearchFragment extends Fragment implements IOnBackPressed {

    LinearLayout mainLayout,cityLayout;

    //mainLayout
    CardView from,to,date;
    TextView dateTV,fromTV,toTV;
    Button search;


    //cityLayout
    TextView direction,countryTV,cityTV;
    CardView country,city;
    Button con;
    String fromCountry,toCountry,fromCity,toCity;


    boolean cityOn=false;
    Calendar dateAndTime=Calendar.getInstance();
    List<Country> countryList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_search, container, false);

//        List<City> c1=new ArrayList<>();
//        c1.add(new City("Lviv"));
//        c1.add(new City("Ivano-Frankivsk"));
//
//        List<City> c2=new ArrayList<>();
//        c2.add(new City("Gdansk"));
//        c2.add(new City("Krakow"));
//
//        countryList.add(new Country("Ukraine",c1));
//        countryList.add(new Country("Poland",c2));

        mainLayout=view.findViewById(R.id.ll_main_search);
        cityLayout=view.findViewById(R.id.ll_choose_city);

        from=view.findViewById(R.id.cv_from_city);
        to=view.findViewById(R.id.cv_to_city);
        date=view.findViewById(R.id.cv_date);
        dateTV=view.findViewById(R.id.tv_date);
        fromTV=view.findViewById(R.id.tv_from);
        toTV=view.findViewById(R.id.tv_to);
        search=view.findViewById(R.id.btn_search);

        direction=view.findViewById(R.id.tv_direction);
        country=view.findViewById(R.id.cv_country);
        countryTV=view.findViewById(R.id.tv_country);
        city=view.findViewById(R.id.cv_city);
        cityTV=view.findViewById(R.id.tv_city);
        con=view.findViewById(R.id.btn_con_city);

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
                setDate(getView());
            }
        });

        country.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder b = new AlertDialog.Builder(getContext());
                b.setTitle("Country");
                String[] types=new String[countryList.size()];

                for (int i=0;i<countryList.size();i++){
//                    types[i]=countryList.get(i).getNameCountry();
                }

                b.setItems(types, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                        countryTV.setText(types[which]);
//                        cityList=countryList.get(which).getCityList();
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
                b.setTitle("City");
                String[] types = new String[cityList.size()];

                for(int i=0;i<cityList.size();i++){
//                    types[i]=cityList.get(i).getNameCity();
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
            }
        });


//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                SearchModel searchModel=new SearchModel(fromCountry,toCountry,fromCity,toCity,dateAndTime);
//                ((PassangerMainActivity)getActivity()).setSearchModel(searchModel);
//                ((PassangerMainActivity)getActivity()).replaceFragment(new SearchTripFragment());
//            }
//        });



        return view;
    }


    public void setDate(View v) {
        new DatePickerDialog(getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH))
                .show();


    }


    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };


    private void setInitialDateTime() {
        dateTV.setText(DateUtils.formatDateTime(getContext(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }


    @Override
    public boolean onBackPressed() {
        if (cityOn) {
            cityOn=false;
            mainLayout.setVisibility(View.VISIBLE);
            cityLayout.setVisibility(View.GONE);
            return true;
        } else {
            return false;
        }
    }
}