package com.busik.busik.Driver.Adapters;

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
import com.busik.busik.Driver.Activities.DriverMainActivity;
import com.busik.busik.Driver.Fragments.LeftReviewAboutPassangerFragment;
import com.busik.busik.Driver.Fragments.ShowTripFragment;
import com.busik.busik.Passanger.ApiResponse.DriverFlight;
import com.busik.busik.Passanger.ApiResponse.DriverMessage;
import com.busik.busik.R;

import java.text.SimpleDateFormat;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverMessagesAdapter  extends RecyclerView.Adapter<DriverMessagesAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<DriverMessage> states;
    List<DriverFlight> driverFlights;
    String token;

    public DriverMessagesAdapter(Context context, List<DriverMessage> states,String token) {
        this.states = states;
        this.token = token;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public DriverMessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_message, parent, false);
        return new DriverMessagesAdapter.ViewHolder(view);
    }

    public void setDriverFlights(List<DriverFlight> driverFlights) {
        this.driverFlights = driverFlights;
    }


    @Override
    public void onBindViewHolder(DriverMessagesAdapter.ViewHolder holder, int position) {
        DriverMessage state = states.get(position);


        holder.text.setText(state.getMessageText().toString());

        if(state.getTypeMessage().equals("move_to_flight")){
            holder.action.setText("До рейсу");
        }
        else if(state.getTypeMessage().equals("leave_review")) {
            holder.action.setText("Залишити відгук");
        }

        holder.action.setVisibility(View.VISIBLE);

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(state.getTypeMessage().equals("move_to_flight")){
                    String s=state.getAppData();

                    String[] q=s.split(":");
                    String s1=q[1];
                    s1=s1.replace("}","");
                    for (DriverFlight d:driverFlights){
                        if(d.getId().equals(Integer.parseInt(s1.trim()))){

                            ((DriverMainActivity) inflater.getContext()).setShowFlight(d);
                            ((DriverMainActivity) inflater.getContext()).setIdMessage(state.getId());
                            ((DriverMainActivity) inflater.getContext()).setGoBack(true);
                            ((DriverMainActivity) inflater.getContext()).replaceFragment(new ShowTripFragment());
                            break;
                        }
                    }
//                    ((PassangerMainActivity) inflater.getContext()).setIdShowedTrip(Integer.parseInt(s1.trim()));
//                    ((PassangerMainActivity) inflater.getContext()).replaceFragment(new ShowMyTripReqFragment());
                }
                else if(state.getTypeMessage().equals("leave_review")) {
                    String s=state.getAppData();
                    String[] q=s.split(",");
                    String[] q1=q[0].split(":");
                    String idPassanger=q1[1].trim();
                    String[] q2=q[1].split(":");
                    String idFlight=q2[1].trim().replace("}","").trim();

                    for (DriverFlight d:driverFlights){
                        if(d.getId().equals(Integer.parseInt(idFlight))){
                            ((DriverMainActivity) inflater.getContext()).setReviewIdFlight(d);
                        }
                    }
                    ((DriverMainActivity) inflater.getContext()).setPassangerIdReview(Integer.parseInt(idPassanger));
                    ((DriverMainActivity) inflater.getContext()).setIdMessage(state.getId());
                    ((DriverMainActivity) inflater.getContext()).replaceFragment(new LeftReviewAboutPassangerFragment());
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

                Call<ResponseBody> call = getResponse.deleteMessageDriver("Token "+token,state.getId());
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
            delete = view.findViewById(R.id.iv_d_message);
        }
    }
}