package com.busik.busik.Driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.busik.busik.Driver.DPassangerAccepted;
import com.busik.busik.Passanger.ApiResponse.Passenger;
import com.busik.busik.R;

import java.util.List;

public class PassangerAcceptedAdapter extends RecyclerView.Adapter<PassangerAcceptedAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Passenger> states;
    DPassangerAccepted dPassangerAccepted;

    public PassangerAcceptedAdapter(Context context, List<Passenger> states,DPassangerAccepted dPassangerAccepted) {
        this.states = states;
        this.dPassangerAccepted = dPassangerAccepted;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public PassangerAcceptedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_passanger_accepted, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PassangerAcceptedAdapter.ViewHolder holder, int position) {
        Passenger state = states.get(position);

        holder.name.setText(state.getFio().toString());
        holder.phone.setText(state.getPhone().toString());

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dPassanger.delete(state.getId());
//            }
//        });


        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerAccepted.call(state.getPhone());
            }
        });

        holder.countPeople.setText("Людей: "+state.getApplication().getCountPerson());
        holder.countLaggage.setText("Вантажу: "+state.getApplication().getCountKgPackage()+ " кг");

        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerAccepted.reviews(state.getId());
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerAccepted.delete(state.getApplication().getId(),state.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,phone,countPeople,countLaggage;
        Button call,delete,review;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.tv_passanger_name);
            phone = view.findViewById(R.id.tv_passanger_phone);
            call = view.findViewById(R.id.cv_call_to_passanger);
            delete = view.findViewById(R.id.cv_delete_passanger);
            review = view.findViewById(R.id.cv_reviews_passanger);
            countPeople = view.findViewById(R.id.tv_count_people);
            countLaggage = view.findViewById(R.id.tv_count_laggage);
        }
    }
}