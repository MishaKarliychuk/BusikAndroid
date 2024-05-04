package com.busik.busik.Driver.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.Fragments.ShowTripFragment;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DriverFlightsAdapter  extends RecyclerView.Adapter<DriverFlightsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<DriverFlight> states;
    Date curentDate;
    Calendar calCurent;
    boolean isArchived;

    public DriverFlightsAdapter(Context context, List<DriverFlight> states,boolean isArchived) {
        this.states = states;
        this.isArchived = isArchived;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public DriverFlightsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_driver_trip, parent, false);
        return new DriverFlightsAdapter.ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(DriverFlightsAdapter.ViewHolder holder, int position) {
        DriverFlight state = states.get(position);
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
//        Date d = null;
//        SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
//        try {
//            d=formatter6.parse(state.getDateDeparture());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        calCurent = Calendar.getInstance();
//        Calendar calD = Calendar.getInstance();
//        calD.setTime(d);
//        if(curentDate==null){
//            curentDate=d;
//            calCurent.setTime(curentDate);
//            String f=output1.format(d);
//            holder.tripsDate.setText(f);
//            holder.tripsDate.setVisibility(View.VISIBLE);
//        }
//        else if(calCurent.get(Calendar.MONTH)!=calD.get(Calendar.MONTH)||calCurent.get(Calendar.DAY_OF_MONTH)!=calD.get(Calendar.DAY_OF_MONTH)){
//            curentDate=d;
//            calCurent.setTime(curentDate);
//            String f=output1.format(d);
//            holder.tripsDate.setText(f);
//            holder.tripsDate.setVisibility(View.VISIBLE);
//        }
//        else {
//            holder.tripsDate.setVisibility(View.GONE);
//        }


        holder.departCity.setText(state.getCityFrom()+", "+state.getCountryFrom());
        holder.arriveCity.setText(state.getCityTo()+", "+state.getCountryTo());


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
//
//
//
//        holder.timeArrive.setText(tArrive);


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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!isArchived){
                    ((DriverMainActivity) inflater.getContext()).setShowFlight(state);
                    ((DriverMainActivity) inflater.getContext()).replaceFragment(new ShowTripFragment());
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView departCity,arriveCity,timeDepart,timeArrive,passangerPrice,laggagePrice,tripsDate,status;
        ViewHolder(View view){
            super(view);
            departCity = view.findViewById(R.id.tv_depart_city);
            arriveCity = view.findViewById(R.id.tv_arrive_city);
            timeDepart = view.findViewById(R.id.tv_time_depart);
            timeArrive = view.findViewById(R.id.tv_time_arrive);
            passangerPrice = view.findViewById(R.id.tv_passanger_price);
            laggagePrice = view.findViewById(R.id.tv_laggage_price);
            tripsDate = view.findViewById(R.id.tv_date_trips);
            status = view.findViewById(R.id.tv_status_flight);

        }
    }
}