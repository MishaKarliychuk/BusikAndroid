package com.busik.busik.Passanger.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.busik.busik.Passanger.ApiResponse.Review;
import com.busik.busik.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ReviewsAdapter  extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<Review> states;

    public ReviewsAdapter(Context context, List<Review> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ReviewsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(ReviewsAdapter.ViewHolder holder, int position) {
        Review state = states.get(position);

        holder.name.setText(state.getDriver().toString());
        holder.text.setText(state.getReviewText().toString());
//
//        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy");
//        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
//
//// since no built-in format, we provides pattern directly.
//        @SuppressLint({"NewApi", "LocalSuppress"}) DateFormat df = new SimpleDateFormat(pattern);
//
//
//        Date d = null;
//
//        @SuppressLint({"NewApi", "LocalSuppress"}) Instant ins = Instant.parse(state.getDatetimeCreated());
//        d = Date.from(ins);
//
//        String formatted = output.format(d);
//        holder.date.setText(formatted);

        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        SimpleDateFormat output = new SimpleDateFormat("dd.MM.yyyy");
        String pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX";
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Calendar calendar = new GregorianCalendar();
        Date date = null;
        try {
            date = formatter.parse(state.getDatetimeCreated());
        } catch (ParseException e) {
            e.printStackTrace();
        }

// since no built-in format, we provides pattern directly.
//        @SuppressLint({"NewApi", "LocalSuppress"}) DateFormat df = new SimpleDateFormat(pattern);
//
//
//        Date d = null;
//
//        @SuppressLint({"NewApi", "LocalSuppress"}) Instant ins = Instant.parse(state.getDatetimeCreated());
//        d = Date.from(ins);

        String formatted = output.format(date);
        holder.date.setText(formatted);

    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text, name,date;
        ViewHolder(View view){
            super(view);
            text = view.findViewById(R.id.tv_text_review);
            name = view.findViewById(R.id.tv_author_review);
            date = view.findViewById(R.id.tv_date_review);
        }
    }
}