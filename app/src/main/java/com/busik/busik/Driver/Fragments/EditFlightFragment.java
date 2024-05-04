package com.busik.busik.Driver.Fragments;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.UpdateFlight;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;
import com.busik.busik.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditFlightFragment extends Fragment implements IOnBackPressed {
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    CardView from,to,date,dateArrive,cTimeDepart,cTimeArrive;
    TextView dateTV,fromTV,toTV,dateTVArrive,tvTimeDepart,tvTimeArrive;
    String token;

    boolean cityOn=false;
    String fromCountry="",toCountry="",fromCity="",toCity="";
    Calendar dateAndTime=Calendar.getInstance();
    Calendar dateAndArrive=Calendar.getInstance();
    Calendar timeDepart=Calendar.getInstance();
    Calendar timeArrive=Calendar.getInstance();
    String tDepart="",tArrive="";
    LinearLayout mainLayout,cityLayout;
    List<Country> countryList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();
    TextView direction,countryTV,cityTV;
    CardView country,city;
    TextView cE;
    boolean isCorect;
    CheckBox cbPeople,cbLaggage;
    String oldStatus;
    Button con,create;
    Spinner spinner;
    EditText pricePerson,maxPerson,priceLaggage,maxLaggeg,description;
    LinearLayout back;
    ImageView b2;
    boolean canEdit=true;

    DriverFlight driverFlight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_flight, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        init(view);
        back=view.findViewById(R.id.ll_back);
        b2=view.findViewById(R.id.iv_b2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        driverFlight= ((DriverMainActivity) inflater.getContext()).getShowFlight();

        for(int i=0;i<driverFlight.getPassengers().size();i++){
            if(driverFlight.getPassengers().get(i).getApplication().getStatus().equals("approved")){
                canEdit=false;
                break;
            }
        }
        setInfo();

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

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
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

        dateArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateArrive(getView());
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
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
                RequestBody requestBody = null;
                isCorect=true;
                if(fromCountry.equals("")&&isCorect){
                    isCorect=false;
                }
                if(fromCity.equals("")&&isCorect){
                    isCorect=false;
                }
                if(toCity.equals("")&&isCorect){
                    isCorect=false;
                }
                if(toCountry.equals("")&&isCorect){
                    isCorect=false;
                }
                if(dateTV.getText().equals("Дата виїзду")&&isCorect){
                    isCorect=false;
                    Toast.makeText(getContext(),"Виберіть дату виїзду",Toast.LENGTH_SHORT).show();
                }
                if(dateTVArrive.getText().equals("Дата приїзду")&&isCorect){
                    Toast.makeText(getContext(),"Виберіть дату приїзду",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(tvTimeDepart.getText().equals("Час виїзду")&&isCorect){
                    Toast.makeText(getContext(),"Виберіть час виїзду",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(tvTimeArrive.getText().equals("Час приїзду")&&isCorect){
                    Toast.makeText(getContext(),"Виберіть час приїзду",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(!cbLaggage.isChecked()&&!cbPeople.isChecked()&&isCorect){
                    isCorect=false;
                }
                if(cbPeople.isChecked()&pricePerson.getText().toString().trim().equals("")&isCorect){
                    isCorect=false;
                }
                if(description.getText().toString().trim().length()>255&isCorect){
                    Toast.makeText(getContext(),"Опис не може бути більшим за 255 символів",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(cbLaggage.isChecked()&priceLaggage.getText().toString().trim().equals("")&isCorect){
                    isCorect=false;
                }
                if(cbLaggage.isChecked()&maxLaggeg.getText().toString().trim().equals("")&isCorect){
                    isCorect=false;
                }
                if ((dateAndArrive.get(Calendar.MONTH)<dateAndTime.get(Calendar.MONTH)&isCorect)|(dateAndArrive.get(Calendar.DAY_OF_MONTH)<dateAndTime.get(Calendar.DAY_OF_MONTH)&dateAndArrive.get(Calendar.MONTH)<=dateAndTime.get(Calendar.MONTH)&isCorect)&isCorect){
                    Toast.makeText(getContext(),"Дата прибуття має бути пізніше дати відправлення",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(dateAndTime.get(Calendar.DAY_OF_MONTH)==dateAndArrive.get(Calendar.DAY_OF_MONTH)&&dateAndTime.get(Calendar.MONTH)==dateAndArrive.get(Calendar.MONTH)&&isCorect){
                    if(timeArrive.get(Calendar.HOUR_OF_DAY)<timeDepart.get(Calendar.HOUR_OF_DAY)&&isCorect){
                        Toast.makeText(getContext(),"Час прибуття не може бути меншим від часу відправлення",Toast.LENGTH_SHORT).show();
                        isCorect=false;
                    }
                    else if(timeArrive.get(Calendar.HOUR_OF_DAY)==timeDepart.get(Calendar.HOUR_OF_DAY)&&isCorect){
                        if(timeArrive.get(Calendar.MINUTE)==timeDepart.get(Calendar.MINUTE)&&isCorect){
                            Toast.makeText(getContext(),"Час прибуття не може бути однаковим",Toast.LENGTH_SHORT).show();
                            isCorect=false;
                        }
                    }
                }
                if(isCorect){
                    boolean isNewStatus=false;
                    String fStatus="";
                    if(!oldStatus.equals(spinner.getSelectedItem())){
                        isNewStatus=true;

                    }
                    switch (spinner.getSelectedItem().toString()){
                        case("Завершено"):{
                            fStatus="completed";
                            oldStatus="Завершено";
                            break;
                        }
                        case("Не відбувся"):{
                            fStatus="cancelled";
                            oldStatus="Не відбувся";
                            break;
                        }
                        case ("В дорозі"):{
                            fStatus="on_the_way";
                            oldStatus="В дорозі";
                            break;
                        }
                        case ("В очікуванні"):{
                            fStatus="waiting";
                            oldStatus="В очікуванні";
                            break;
                        }
                    }
                    String dateDepart;
                    int yD= dateAndTime.get(Calendar.YEAR);
                    int mD=dateAndTime.get(Calendar.MONTH);
                    mD+=1;
                    int dD=dateAndTime.get(Calendar.DAY_OF_MONTH);
                    String mDD;
                    if(mD<10){
                        mDD="0"+mD;
                    }
                    else {
                        mDD=String.valueOf(mD);
                    }
                    String dDD;
                    if(dD<10){
                        dDD="0"+dD;
                    }
                    else {
                        dDD=String.valueOf(dD);
                    }
                    dateDepart=yD+"-"+mDD+"-"+dDD;

                    String dateArrive;
                    int yA=dateAndArrive.get(Calendar.YEAR);
                    int mA=dateAndArrive.get(Calendar.MONTH);
                    mA++;
                    int dA=dateAndArrive.get(Calendar.DAY_OF_MONTH);
                    String mAA;
                    if(mA<10){
                        mAA="0"+mA;
                    }
                    else {
                        mAA=String.valueOf(mA);
                    }
                    String dAA;
                    if(dA<10){
                        dAA="0"+dA;
                    }
                    else {
                        dAA=String.valueOf(dA);
                    }
                    dateArrive=yA+"-"+mAA+"-"+dAA;

                    if(cbPeople.isChecked()&&cbLaggage.isChecked()){
                        if(isNewStatus){
                            if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                if(description.getText().toString().trim().equals("")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {

                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                            }
                            else {
                                if(description.getText().toString().trim().equals("")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                                else {

                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                        }
                        else {
                            if(description.getText().toString().trim().equals("")){
                                if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                            else {

                                if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                        }
                    }
                    else if(cbPeople.isChecked()){
                        if(isNewStatus){
                            if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                if(description.getText().toString().equals("")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                            }
                            else {
                                if(description.getText().toString().equals("")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                        }
                        else {
                            if(description.getText().toString().equals("")){
                                if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                            else {
                                if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person",pricePerson.getText().toString().trim())
                                            .addFormDataPart("max_count_person",maxPerson.getText().toString().trim())
                                            .addFormDataPart("price_1_kg_package","")
                                            .addFormDataPart("max_kg_package","0")
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                        }
                    }
                    else if(cbLaggage.isChecked()){
                        if(isNewStatus){
                            if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                if(description.getText().toString().trim().equals("")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                            }
                            else {
                                if(description.getText().toString().trim().equals("")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("status",fStatus)
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                        }
                        else {
                            if(description.getText().toString().trim().equals("")){
                                if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                            else {
                                if(fStatus.equals("completed")||fStatus.equals("cancelled")){
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("archived","true")
                                            .build();
                                }
                                else {
                                    requestBody= new MultipartBody.Builder()
                                            .setType(MultipartBody.FORM)
                                            .addFormDataPart("country_from",fromCountry)
                                            .addFormDataPart("city_from", fromCity)
                                            .addFormDataPart("city_to", toCity)
                                            .addFormDataPart("country_to", toCountry)
                                            .addFormDataPart("date_departure",dateDepart+" "+tDepart)
                                            .addFormDataPart("date_arrival",dateArrive+" "+tArrive)
                                            .addFormDataPart("price_1_kg_package",priceLaggage.getText().toString().trim())
                                            .addFormDataPart("max_kg_package",maxLaggeg.getText().toString().trim())
                                            .addFormDataPart("details",description.getText().toString().trim())
                                            .addFormDataPart("price_1_person","")
                                            .addFormDataPart("max_count_person","0")
                                            .addFormDataPart("archived","false")
                                            .build();
                                }
                            }
                        }
                    }

                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<UpdateFlight> call = getResponse.updateFlight("Token "+token,requestBody,driverFlight.getId());
                    call.enqueue(new Callback<UpdateFlight>() {
                        @Override
                        public void onResponse(Call<UpdateFlight> call, Response<UpdateFlight> response) {
                            if(response.code()==200){
                                Toast.makeText(getContext(),"Рейс изменен",Toast.LENGTH_SHORT).show();
                                driverFlight.setCityFrom(response.body().getCityFrom());
                                driverFlight.setCountryFrom(response.body().getCountryFrom());
                                driverFlight.setCityTo(response.body().getCityTo());
                                driverFlight.setCountryTo(response.body().getCountryTo());
                                driverFlight.setDateDeparture(response.body().getDateDeparture());
                                driverFlight.setDateArrival(response.body().getDateArrival());
                                driverFlight.setDetails(response.body().getDetails());
                                driverFlight.setPrice1Person(response.body().getPrice1Person());
                                driverFlight.setMaxCountPerson(response.body().getMaxCountPerson());
                                driverFlight.setPrice1KgPackage(response.body().getPrice1KgPackage());
                                driverFlight.setMaxKgPackage(response.body().getMaxKgPackage());
                                driverFlight.setStatus(response.body().getStatus());
                                ((DriverMainActivity) inflater.getContext()).setShowFlight(driverFlight);

//                                getActivity().onBackPressed();
                            }
                            else {
                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<UpdateFlight> call, Throwable t) {
                        }
                    });
                }
                else {
                    Toast.makeText(getContext(),"Введите все данніе",Toast.LENGTH_SHORT).show();
                }
            }
        });

        cTimeDepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTime(getView());
            }
        });


        cTimeArrive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setTimeArrive(getView());
            }
        });

        getCountries();




        return view;
    }

    void init(View view){
        token = sp.getString("token", null);

        from=view.findViewById(R.id.cv_from_city);
        to=view.findViewById(R.id.cv_to_city);
        date=view.findViewById(R.id.cv_date);
        dateTV=view.findViewById(R.id.tv_date);
        fromTV=view.findViewById(R.id.tv_from);
        tvTimeDepart=view.findViewById(R.id.tv_time_depart);
        cTimeDepart=view.findViewById(R.id.cv_time_depart);
        cTimeArrive=view.findViewById(R.id.cv_time_arrive);
        tvTimeArrive=view.findViewById(R.id.tv_time_arrive);
        cE=view.findViewById(R.id.tv_can_edit);
        description=view.findViewById(R.id.et_description);
        cbLaggage=view.findViewById(R.id.cb_laggage);
        cbPeople=view.findViewById(R.id.cb_people);
        toTV=view.findViewById(R.id.tv_to);
        create=view.findViewById(R.id.btn_create);
        mainLayout=view.findViewById(R.id.ll_main);
        cityLayout=view.findViewById(R.id.ll_choose_city);
        pricePerson=view.findViewById(R.id.et_price_person);
        dateArrive=view.findViewById(R.id.cv_date_arrive);
        dateTVArrive=view.findViewById(R.id.tv_date_arrive);
        maxPerson=view.findViewById(R.id.et_max_person);
        priceLaggage=view.findViewById(R.id.et_price_laggage);
        spinner=view.findViewById(R.id.spinner_status);
        maxLaggeg=view.findViewById(R.id.et_max_laggage);
        pricePerson.setText("");

        direction=view.findViewById(R.id.tv_direction);
        country=view.findViewById(R.id.cv_country);
        countryTV=view.findViewById(R.id.tv_country);
        city=view.findViewById(R.id.cv_city);
        cityTV=view.findViewById(R.id.tv_city);
        con=view.findViewById(R.id.btn_con_city);
    }

    void setInfo(){
        if(!canEdit){
            cE.setVisibility(View.VISIBLE);
            cbPeople.setClickable(false);
            cbLaggage.setClickable(false);
            pricePerson.setEnabled(false);
            maxPerson.setEnabled(false);
            priceLaggage.setEnabled(false);
            maxLaggeg.setEnabled(false);
        }
        fromTV.setText(driverFlight.getCityFrom());
        fromCountry=driverFlight.getCountryFrom();
        fromCity=driverFlight.getCityFrom();

        toTV.setText(driverFlight.getCityTo());
        toCountry=driverFlight.getCountryTo();
        toCity=driverFlight.getCityTo();

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatter.parse(driverFlight.getDateDeparture());
            dateAndTime.setTime(date);
            timeDepart.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setInitialDateTime();
        setInitialDatetime();

        try {
            date = formatter.parse(driverFlight.getDateArrival());
            dateAndArrive.setTime(date);
            timeArrive.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setInitialDateTimeArrive();
        setInitialDatetimeA();

        if (!driverFlight.getDetails().equals("-")){
            description.setText(driverFlight.getDetails());
        }

        if (driverFlight.getMaxCountPerson()>0){
            cbPeople.setChecked(true);
        }

        if(cbPeople.isChecked()){
            pricePerson.setText(driverFlight.getPrice1Person().intValue()+"");
            maxPerson.setText(driverFlight.getMaxCountPerson().intValue()+"");
        }

        if(driverFlight.getMaxKgPackage()>0){
            cbLaggage.setChecked(true);
        }

        if(cbLaggage.isChecked()){
            priceLaggage.setText(driverFlight.getPrice1KgPackage()+"");
            maxLaggeg.setText(driverFlight.getMaxKgPackage().intValue()+"");
        }

        switch (driverFlight.getStatus()){
            case("waiting"):{
                spinner.setSelection(3);
                oldStatus="В очікуванні";
//                status.setText("В очікуванні");
                break;
            }
            case("on_the_way"):{
                spinner.setSelection(2);
                oldStatus="В дорозі";
//                status.setText("В дорозі");
                break;
            }
            case ("completed"):{
                spinner.setSelection(0);
                oldStatus="Завершено";
//                status.setText("Завершено");
                break;
            }
            case ("cancelled"):{
                spinner.setSelection(1);
                oldStatus="Не відбувся";
//                status.setText("Не відбувся");
                break;
            }
        }
    }

    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeDepart.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeDepart.set(Calendar.MINUTE, minute);
            setInitialDatetime();
        }
    };

    public void setTime(View v) {
        new TimePickerDialog(getContext(), t,
                timeDepart.get(Calendar.HOUR_OF_DAY),
                timeDepart.get(Calendar.MINUTE), true)
                .show();
    }

    TimePickerDialog.OnTimeSetListener tA=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeArrive.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeArrive.set(Calendar.MINUTE, minute);
            setInitialDatetimeA();
        }
    };

    public void setTimeArrive(View v) {
        new TimePickerDialog(getContext(), tA,
                timeArrive.get(Calendar.HOUR_OF_DAY),
                timeArrive.get(Calendar.MINUTE), true)
                .show();
    }

    private void setInitialDatetimeA() {
        String m="";
        String h="";
        if(timeArrive.get(Calendar.HOUR_OF_DAY)<10){
            h="0"+timeArrive.get(Calendar.HOUR_OF_DAY);
        }
        else {
            h=timeArrive.get(Calendar.HOUR_OF_DAY)+"";
        }
        if(timeArrive.get(Calendar.MINUTE)<10){
            m="0"+timeArrive.get(Calendar.MINUTE);
        }
        else {
            m=timeArrive.get(Calendar.MINUTE)+"";
        }
        tvTimeArrive.setText(h+":"+m+"");
        tArrive=h+":"+m;
    }



    private void setInitialDatetime() {
        String m="";
        String h="";
        if(timeDepart.get(Calendar.HOUR_OF_DAY)<10){
            h="0"+timeDepart.get(Calendar.HOUR_OF_DAY);
        }
        else {
            h=timeDepart.get(Calendar.HOUR_OF_DAY)+"";
        }
        if(timeDepart.get(Calendar.MINUTE)<10){
            m="0"+timeDepart.get(Calendar.MINUTE);
        }
        else {
            m=timeDepart.get(Calendar.MINUTE)+"";
        }
        tvTimeDepart.setText(h+":"+m+"");
        tDepart=h+":"+m;
    }

    public void setDate(View v) {
        DatePickerDialog e =new DatePickerDialog(getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));
        e.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        e.show();
    }

    public void setDateArrive(View v) {
        DatePickerDialog e =new DatePickerDialog(getContext(), dArrive,
                dateAndArrive.get(Calendar.YEAR),
                dateAndArrive.get(Calendar.MONTH),
                dateAndArrive.get(Calendar.DAY_OF_MONTH));
        e.getDatePicker().setMinDate(dateAndTime.getTimeInMillis() - 1000);
        e.show();
    }

    DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime.set(Calendar.YEAR, year);
            dateAndTime.set(Calendar.MONTH, monthOfYear);
            dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTime();
        }
    };

    DatePickerDialog.OnDateSetListener dArrive=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndArrive.set(Calendar.YEAR, year);
            dateAndArrive.set(Calendar.MONTH, monthOfYear);
            dateAndArrive.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setInitialDateTimeArrive();
        }
    };


    private void setInitialDateTime() {
        dateTV.setText(DateUtils.formatDateTime(getContext(),
                dateAndTime.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
    }

    private void setInitialDateTimeArrive() {
        dateTVArrive.setText(DateUtils.formatDateTime(getContext(),
                dateAndArrive.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR));
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
                    if(!fromCity.equals("")){
                        for(int i=0;i<cityList.size();i++){
                            if(cityList.get(i).getName().equals(fromCity)){
                                cityList.remove(i);
                                break;
                            }
                        }
                    }
//                    if(!toCity.equals("")){
//                        for(int i=0;i<cityList.size();i++){
//                            if(cityList.get(i).getName().equals(toCity)){
//                                cityList.remove(i);
//                                break;
//                            }
//                        }
//                    }
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
            return false;
        }
    }
}