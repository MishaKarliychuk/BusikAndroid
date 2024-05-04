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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.CreateFlight;
import com.busik.busik.Passanger.Models.City;
import com.busik.busik.Passanger.Models.Country;
import com.busik.busik.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddTripFragment extends Fragment implements IOnBackPressed {
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    CardView from,to,date,dateArrive,cTimeDepart,cTimeArrive;
    TextView dateTV,fromTV,toTV,dateTVArrive,tvTimeDepart,tvTimeArrive;
    String token;
    ImageView backCountry;

    boolean cityOn=false;
    String fromCountry="",toCountry="",fromCity="",toCity="";
    Calendar dateAndTime=Calendar.getInstance();
    Calendar dateAndArrive=Calendar.getInstance();
    Calendar timeDepart=Calendar.getInstance();
    Calendar timeArrive=Calendar.getInstance();
    LinearLayout mainLayout,cityLayout;
    List<Country> countryList=new ArrayList<>();
    List<City> cityList=new ArrayList<>();
    TextView direction,countryTV,cityTV;
    LinearLayout back;
    CardView country,city;
    boolean isCorect;
    CheckBox cbPeople,cbLaggage;
    String tDepart="",tArrive="";
    Button con,create;
    EditText pricePerson,maxPerson,priceLaggage,maxLaggeg,description;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_trip, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        token = sp.getString("token", null);

        from=view.findViewById(R.id.cv_from_city);
        to=view.findViewById(R.id.cv_to_city);
        back=view.findViewById(R.id.ll_back);
        date=view.findViewById(R.id.cv_date);
        dateTV=view.findViewById(R.id.tv_date);
        fromTV=view.findViewById(R.id.tv_from);
        backCountry=view.findViewById(R.id.iv_b2);
        description=view.findViewById(R.id.et_description);
        cbLaggage=view.findViewById(R.id.cb_laggage);
        cbPeople=view.findViewById(R.id.cb_people);
        toTV=view.findViewById(R.id.tv_to);
        tvTimeDepart=view.findViewById(R.id.tv_time_depart);
        create=view.findViewById(R.id.btn_create);
        mainLayout=view.findViewById(R.id.ll_main);
        cityLayout=view.findViewById(R.id.ll_choose_city);
        pricePerson=view.findViewById(R.id.et_price_person);
        dateArrive=view.findViewById(R.id.cv_date_arrive);
        dateTVArrive=view.findViewById(R.id.tv_date_arrive);
        maxPerson=view.findViewById(R.id.et_max_person);
        priceLaggage=view.findViewById(R.id.et_price_laggage);
        maxLaggeg=view.findViewById(R.id.et_max_laggage);
        cTimeDepart=view.findViewById(R.id.cv_time_depart);
        cTimeArrive=view.findViewById(R.id.cv_time_arrive);
        tvTimeArrive=view.findViewById(R.id.tv_time_arrive);
        pricePerson.setText("");

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
                cityList.clear();
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

        backCountry.setOnClickListener(new View.OnClickListener() {
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
                cityList.clear();
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
                RequestBody requestBody = null;
                isCorect=true;
                if(fromCountry.equals("")&&isCorect){
                   isCorect=false;
                   Toast.makeText(getContext(),"Виберіть країну відправлення",Toast.LENGTH_SHORT).show();
                }
                if(fromCity.equals("")&&isCorect){
                    isCorect=false;
                    Toast.makeText(getContext(),"Виберіть місто відправлення",Toast.LENGTH_SHORT).show();
                }
                if(toCity.equals("")&&isCorect){
                    isCorect=false;
                    Toast.makeText(getContext(),"Виберіть місто прибуття",Toast.LENGTH_SHORT).show();
                }
                if(toCountry.equals("")&&isCorect){
                    Toast.makeText(getContext(),"Виберіть країну прибуття",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(dateTV.getText().equals("дата виїзду")&&isCorect){
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
                    if((!pricePerson.getText().toString().trim().equals("")&&!cbPeople.isChecked())||(!maxPerson.getText().toString().trim().equals("")&&!cbPeople.isChecked())){
                        Toast.makeText(getContext(),"Підтвердьте прийом пасажирів",Toast.LENGTH_SHORT).show();
                    }
                    else if((!priceLaggage.getText().toString().trim().equals("")&&!cbLaggage.isChecked())||(!maxLaggeg.getText().toString().trim().equals("")&&!cbLaggage.isChecked())){
                        Toast.makeText(getContext(),"Підтвердіть прийом вантажу",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(),"Ви повинні прийняти вантаж або пасажирів",Toast.LENGTH_SHORT).show();
                    }
                    isCorect=false;
                }
                if(cbPeople.isChecked()&pricePerson.getText().toString().trim().equals("")&isCorect){
                    Toast.makeText(getContext(),"Введіть ціну за пасажира",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(cbPeople.isChecked()&maxPerson.getText().toString().trim().equals("")&isCorect){
                    Toast.makeText(getContext(),"Введіть максимальну кількість пасажирів",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(cbLaggage.isChecked()&priceLaggage.getText().toString().trim().equals("")&isCorect){
                    Toast.makeText(getContext(),"Введіть ціну за вантаж",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(cbLaggage.isChecked()&maxLaggeg.getText().toString().trim().equals("")&isCorect){
                    Toast.makeText(getContext(),"Введіть максимальну кількість вантажу",Toast.LENGTH_SHORT).show();
                    isCorect=false;
                }
                if(description.getText().toString().trim().length()>255&isCorect){
                    Toast.makeText(getContext(),"Опис не може бути більшим за 255 символів",Toast.LENGTH_SHORT).show();
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
                    if(cbPeople.isChecked()){
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
                                    .build();
                        }
                    }
                    if(cbLaggage.isChecked()){
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
                                    .build();
                        }
                    }
                    if(cbPeople.isChecked()&&cbLaggage.isChecked()){
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
                                    .build();
                        }
                    }

                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
                    Call<CreateFlight> call = getResponse.createFlight("Token "+token,requestBody);
                    call.enqueue(new Callback<CreateFlight>() {
                        @Override
                        public void onResponse(Call<CreateFlight> call, Response<CreateFlight> response) {
                            if(response.code()==200){
                                Toast.makeText(getContext(),"Створено",Toast.LENGTH_SHORT).show();
                                getActivity().onBackPressed();
                            }
                            else {
                                Toast.makeText(getContext(),"Error",Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<CreateFlight> call, Throwable t) {
                        }
                    });
                }
                else {
//                    Toast.makeText(getContext(),"Введите все данніе",Toast.LENGTH_SHORT).show();
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


    TimePickerDialog.OnTimeSetListener t=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeDepart.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeDepart.set(Calendar.MINUTE, minute);
            setInitialDatetime();
        }
    };

    public void setTime(View v) {
        TimePickerDialog g= new TimePickerDialog(getContext(), t,
                timeDepart.get(Calendar.HOUR_OF_DAY),
                timeDepart.get(Calendar.MINUTE), true);
        g.show();
    }

    TimePickerDialog.OnTimeSetListener tA=new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            timeArrive.set(Calendar.HOUR_OF_DAY, hourOfDay);
            timeArrive.set(Calendar.MINUTE, minute);
            setInitialDatetimeA();
        }
    };

    public void setTimeArrive(View v) {
        TimePickerDialog g1=new TimePickerDialog(getContext(), tA,
                timeArrive.get(Calendar.HOUR_OF_DAY),
                timeArrive.get(Calendar.MINUTE), true);

        g1.show();
    }

    public void setDate(View v) {
        DatePickerDialog e= new DatePickerDialog(getContext(), d,
                dateAndTime.get(Calendar.YEAR),
                dateAndTime.get(Calendar.MONTH),
                dateAndTime.get(Calendar.DAY_OF_MONTH));

        e.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        e.show();
    }

    public void setDateArrive(View v) {
        DatePickerDialog e1= new DatePickerDialog(getContext(), dArrive,
                dateAndArrive.get(Calendar.YEAR),
                dateAndArrive.get(Calendar.MONTH),
                dateAndArrive.get(Calendar.DAY_OF_MONTH));

        e1.getDatePicker().setMinDate(dateAndTime.getTimeInMillis() - 1000);
        e1.show();
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