package com.busik.busik.Passanger.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.PassengerMessage;
import com.busik.busik.Passanger.Fragments.AddReviewFragment;
import com.busik.busik.Passanger.Fragments.ShowMyTripReqFragment;
import com.busik.busik.R;

import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PassangerMessagesAdapter extends RecyclerView.Adapter<PassangerMessagesAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<PassengerMessage> states;
    String token;

    public PassangerMessagesAdapter(Context context, List<PassengerMessage> states,String token) {
        this.states = states;
        this.token = token;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public PassangerMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_message_passanger, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(PassangerMessagesAdapter.ViewHolder holder, int position) {
        PassengerMessage state = states.get(position);


        if(state.getTypeMessage().equals("move_to_flight")){
            holder.action.setText("До рейсу");
            holder.text.setText(state.getMessageText().toString());
        }
        else if(state.getTypeMessage().equals("leave_review")) {
            holder.action.setText("Залишити відгук");
            holder.text.setText(state.getMessageText().toString());
        }
        else if(state.getTypeMessage().equals("message_from_driver")){
            holder.action.setText("До рейсу");
            holder.text.setText("Повідомлення від водія:\n"+state.getMessageText().toString());
        }

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.getTypeMessage().equals("move_to_flight")){
                    String s=state.getAppData();
                    String[] q=s.split(":");
                    String s1=q[1];
                    s1=s1.replace("}","");

//                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);
//
//                    Call<ResponseBody> call = getResponse.deleteMessagePassanger("Token "+token,state.getId());
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            if(response.code()==204){
//
//                            }
//                            else {
//                                Toast.makeText(inflater.getContext(),"Error",Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//
//                        }
//                    });
                    ((PassangerMainActivity) inflater.getContext()).setIdShowedTrip(Integer.parseInt(s1.trim()));
                    ((PassangerMainActivity) inflater.getContext()).replaceFragment(new ShowMyTripReqFragment());
                }
                else if(state.getTypeMessage().equals("leave_review")) {
                    String s=state.getAppData();
                    String[] q=s.split(",");
                    String[] q1=q[0].split(":");
                    String driverId=q1[1].trim();
                    String[] q2=q[1].split(":");
                    String flightId=q2[1].replace("}","").trim();

                    ((PassangerMainActivity) inflater.getContext()).setIdDriverReview(Integer.parseInt(driverId));
                    ((PassangerMainActivity) inflater.getContext()).setIdMessage(state.getId());
                    ((PassangerMainActivity) inflater.getContext()).setIdFlightReview(Integer.parseInt(flightId));
                    ((PassangerMainActivity) inflater.getContext()).replaceFragment(new AddReviewFragment());
                }
                else if(state.getTypeMessage().equals("message_from_driver")){
                    String s=state.getAppData();
                    String[] q=s.split(":");
                    String s1=q[1];
                    s1=s1.replace("}","");

                    ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                    Call<ResponseBody> call = getResponse.deleteMessagePassanger("Token "+token,state.getId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.code()==204){

                            }
                            else {
                                Toast.makeText(inflater.getContext(),"Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                    ((PassangerMainActivity) inflater.getContext()).setIdShowedTrip(Integer.parseInt(s1.trim()));
                    ((PassangerMainActivity) inflater.getContext()).replaceFragment(new ShowMyTripReqFragment());
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                Call<ResponseBody> call = getResponse.deleteMessagePassanger("Token "+token,state.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if(response.code()==204){
                            states.remove(state);
                            notifyItemRemoved(holder.getAdapterPosition());
                        }
                        else {
                            Toast.makeText(inflater.getContext(),"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });


        SimpleDateFormat output = new SimpleDateFormat("dd.MM HH.mm");
        String formatted=output.format(state.getDateSort());
        holder.date.setText(formatted);


    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text,date;
        ImageView delete;
        Button action;
        ViewHolder(View view){
            super(view);
            text = view.findViewById(R.id.tv_text_message);
            date = view.findViewById(R.id.tv_message_date);
            action = view.findViewById(R.id.btn_action);
            delete = view.findViewById(R.id.iv_delete);
        }
    }
}