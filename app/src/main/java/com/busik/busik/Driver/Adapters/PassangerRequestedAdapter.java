package com.busik.busik.Driver.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.busik.busik.Driver.DPassangerRequest;
import com.busik.busik.Passanger.ApiResponse.Passenger;
import com.busik.busik.R;

import java.util.List;

public class PassangerRequestedAdapter extends RecyclerView.Adapter<PassangerRequestedAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Passenger> states;
    DPassangerRequest dPassangerRequest;

    public PassangerRequestedAdapter(Context context, List<Passenger> states, DPassangerRequest dPassangerRequest) {
        this.states = states;
        this.dPassangerRequest = dPassangerRequest;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public PassangerRequestedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_passanger_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PassangerRequestedAdapter.ViewHolder holder, int position) {
        Passenger state = states.get(position);

        holder.name.setText(state.getFio().toString());
        holder.phone.setText(state.getPhone().toString());

//        holder.delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dPassanger.delete(state.getId());
//            }
//        });

        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerRequest.accept(state.getApplication().getId());
            }
        });

        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerRequest.cancel(state.getApplication().getId());
            }
        });

        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerRequest.call(state.getPhone());
            }
        });

        holder.review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dPassangerRequest.review(state.getId());
            }
        });

        holder.countPeople.setText("Людей: "+state.getApplication().getCountPerson());
        holder.countLaggage.setText("Вантажу: "+state.getApplication().getCountKgPackage()+ " кг");

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,phone,countPeople,countLaggage;
        Button call,accept,cancel,review;
        ViewHolder(View view){
            super(view);
            name = view.findViewById(R.id.tv_passanger_name);
            phone = view.findViewById(R.id.tv_passanger_phone);
            call = view.findViewById(R.id.cv_call_to_passanger);
            accept = view.findViewById(R.id.cv_accept_passanger);
            cancel = view.findViewById(R.id.cv_cancel_passanger);
            review = view.findViewById(R.id.cv_reviews_passanger);
            countPeople = view.findViewById(R.id.tv_count_people);
            countLaggage = view.findViewById(R.id.tv_count_laggage);
        }
    }
}