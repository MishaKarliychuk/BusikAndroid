package com.busik.busik.Guess.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Guess.GuessMainActivity;
import com.busik.busik.Guess.GuestTripDetailFragment;
import com.busik.busik.MyApp;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.Passanger.ApiResponse.GetDriver;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestTripAdapter  extends RecyclerView.Adapter<GuestTripAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Buses> states;
    Date curentDate;
    Calendar calCurent;
    String token;

    public GuestTripAdapter(Context context, List<Buses> states, String token) {
        this.states = states;
        this.token = token;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public GuestTripAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_trip, parent, false);


        return new GuestTripAdapter.ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(GuestTripAdapter.ViewHolder holder, int position) {
        Buses state = states.get(position);
        Date d = null;
        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
        try {
            d=formatter6.parse(state.getDateDeparture());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calCurent = Calendar.getInstance();
        Calendar calD = Calendar.getInstance();
        calD.setTime(d);
        if(curentDate==null){
            curentDate=d;
            calCurent.setTime(curentDate);
            String f=output1.format(d);
            holder.tripsDate.setText(f);
            holder.tripsDate.setVisibility(View.VISIBLE);
        }
        else{
            Calendar ccc=Calendar.getInstance();
            ccc.setTime(curentDate);
            if(ccc.get(Calendar.MONTH)!=calD.get(Calendar.MONTH)||ccc.get(Calendar.DAY_OF_MONTH)!=calD.get(Calendar.DAY_OF_MONTH)){
                curentDate=d;
                calCurent.setTime(curentDate);
                String f=output1.format(d);
                holder.tripsDate.setText(f);
                holder.tripsDate.setVisibility(View.VISIBLE);
            }
            else {
                holder.tripsDate.setVisibility(View.GONE);
            }
        }


        holder.departCity.setText(state.getCityFrom()+", "+state.getCountryFrom());
        holder.arriveCity.setText(state.getCityTo()+", "+state.getCountryTo());
//        holder.nameDriver.setText(state.getDriver().toString());


//        @SuppressLint({"NewApi", "LocalSuppress"})
//        LocalDateTime datetime = LocalDateTime.parse(state.getDateDeparture(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        @SuppressLint({"NewApi", "LocalSuppress"}) String tDepart = datetime.format(DateTimeFormatter.ofPattern("dd.MM"));
//
//
//        holder.timeDepart.setText(tDepart);
//
//
//
//        datetime = LocalDateTime.parse(state.getDateArrival(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//        @SuppressLint({"NewApi", "LocalSuppress"}) String tArrive = datetime.format(DateTimeFormatter.ofPattern("dd.MM"));


        @SuppressLint({"NewApi", "LocalSuppress"})
        LocalDateTime datetime = LocalDateTime.parse(state.getDateDeparture(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        @SuppressLint({"NewApi", "LocalSuppress"}) String tDepart = datetime.format(DateTimeFormatter.ofPattern("dd.MM"));

        String dayWeek = "";
        if(datetime.getDayOfWeek()== DayOfWeek.MONDAY){
            dayWeek="Пн";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.TUESDAY){
            dayWeek="Вт";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.WEDNESDAY){
            dayWeek="Ср";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.THURSDAY){
            dayWeek="Чт";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.FRIDAY){
            dayWeek="Пт";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.SATURDAY){
            dayWeek="Сб";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.SUNDAY){
            dayWeek="Вс";
        }
        int h=datetime.getHour();
        int m=datetime.getMinute();
        String hour="";
        String minut="";
        if(h<10){
            hour="0"+h;
        }
        else {
            hour=""+h;
        }
        if(m<10){
            minut="0"+m;
        }
        else {
            minut=m+"";
        }

        holder.timeDepart.setText(dayWeek+" "+hour+":"+minut);



        datetime = LocalDateTime.parse(state.getDateArrival(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        @SuppressLint({"NewApi", "LocalSuppress"}) String tArrive = datetime.format(DateTimeFormatter.ofPattern("dd.MM"));


        dayWeek = "";
        if(datetime.getDayOfWeek()==DayOfWeek.MONDAY){
            dayWeek="Пн";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.TUESDAY){
            dayWeek="Вт";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.WEDNESDAY){
            dayWeek="Ср";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.THURSDAY){
            dayWeek="Чт";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.FRIDAY){
            dayWeek="Пт";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.SATURDAY){
            dayWeek="Сб";
        }
        if(datetime.getDayOfWeek()==DayOfWeek.SUNDAY){
            dayWeek="Вс";
        }
        h=datetime.getHour();
        m=datetime.getMinute();
        hour="";
        minut="";
        if(h<10){
            hour="0"+h;
        }
        else {
            hour=""+h;
        }
        if(m<10){
            minut="0"+m;
        }
        else {
            minut=m+"";
        }

        holder.timeArrive.setText(dayWeek+" "+hour+":"+minut);



//        holder.timeArrive.setText(tArrive);

        if(state.getPrice1KgPackage()==null){
            holder.laggagePrice.setText("€0");
        }
        else {
            holder.laggagePrice.setText("€"+state.getPrice1KgPackage());
        }
        if(state.getPrice1Person()==null){
            holder.passangerPrice.setText("€0");
        }
        else {
            holder.passangerPrice.setText("€"+state.getPrice1Person());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyApp.setTripDetailId(state.getId());
                ((GuessMainActivity) inflater.getContext()).setTripDetailId(state.getId());
                ((GuessMainActivity) inflater.getContext()).setAllBusesAdapterPos(holder.getAdapterPosition());
                ((GuessMainActivity) inflater.getContext()).replaceFragment(new GuestTripDetailFragment());
            }
        });




        ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

        Call<GetDriver> call = getResponse.getDriverInfo("Token "+token,state.getDriver());
        call.enqueue(new Callback<GetDriver>() {
            @Override
            public void onResponse(Call<GetDriver> call, Response<GetDriver> response) {
                if(response.code()==200){
                    holder.nameDriver.setText(response.body().getFio());
                    holder.rate.setText(response.body().getRating()+"");
                }
                else{
                }
            }
            @Override
            public void onFailure(Call<GetDriver> call, Throwable t) {
            }
        });
        switch (state.getStatus()){
            case("waiting"):{
                holder.status.setText("В очікуванні");
                break;
            }
            case("on_the_way"):{
                holder.status.setText("В дорозі");
                break;
            }
            case ("completed"):{
                holder.status.setText("Завершено");
                break;
            }
            case ("cancelled"):{
                holder.status.setText("Не відбувся");
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameDriver,status,departCity,arriveCity,timeDepart,timeArrive,passangerPrice,laggagePrice,tripsDate,rate;
        ViewHolder(View view){
            super(view);
            nameDriver = view.findViewById(R.id.tv_name_driver);
            departCity = view.findViewById(R.id.tv_depart_city);
            arriveCity = view.findViewById(R.id.tv_arrive_city);
            timeDepart = view.findViewById(R.id.tv_time_depart);
            timeArrive = view.findViewById(R.id.tv_time_arrive);
            status = view.findViewById(R.id.tv_status_flight);
            rate = view.findViewById(R.id.tv_driver_rate);
            passangerPrice = view.findViewById(R.id.tv_passanger_price);
            laggagePrice = view.findViewById(R.id.tv_laggage_price);
            tripsDate = view.findViewById(R.id.tv_date_trips);
        }
    }
}