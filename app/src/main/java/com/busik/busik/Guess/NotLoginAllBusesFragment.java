package com.busik.busik.Guess;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.busik.busik.ApiConfig;
import com.busik.busik.AppConfig;
import com.busik.busik.Guess.Adapters.GuestTripAdapter;
import com.busik.busik.Passanger.ApiResponse.Buses;
import com.busik.busik.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NotLoginAllBusesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    RecyclerView recyclerView;
    private static final String MY_SETTINGS = "TOKEN";
    SharedPreferences sp;
    ImageView close;
    TextView log;
    RelativeLayout header;
    LinearLayout search;
    SwipeRefreshLayout swipeRefreshLayout;

    GuestTripAdapter tripAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_login_all_buses, container, false);
        sp = getActivity().getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);

        recyclerView=view.findViewById(R.id.rv_buses);
        close=view.findViewById(R.id.iv_close);
        search=view.findViewById(R.id.ll_search);
        header=view.findViewById(R.id.rl_header);
        swipeRefreshLayout=view.findViewById(R.id.swipe_container);
        log=view.findViewById(R.id.tv_log);
        swipeRefreshLayout.setOnRefreshListener(this);


        LinearLayoutManager layoutManager
                = new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(layoutManager);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ((GuessMainActivity)getActivity()).replaceFragment(new GuestSeacthFragment());
            }
        });

        getAllBuses();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                header.setVisibility(View.GONE);
            }
        });

        foo();

        return view;
    }

    private void foo() {
        SpannableString link = makeLinkSpan("авторизуватися", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuessMainActivity)getActivity()).setBus(false);
                ((GuessMainActivity)getActivity()).replaceFragment(new GuestAuthFragment());
            }
        });

        // We need a TextView instance.

        // Set the TextView's text
        log.setText("Ви увійшли як гість, щоб отримати доступ до всіх функцій необхідно ");

        // Append the link we created above using a function defined below.
        log.append(link);

        // Append a period (this will not be a link).
        log.append(".");

        // This line makes the link clickable!
        makeLinksFocusable(log);
    }

    private SpannableString makeLinkSpan(CharSequence text, View.OnClickListener listener) {
        SpannableString link = new SpannableString(text);
        link.setSpan(new ClickableString(listener), 0, text.length(),
                SpannableString.SPAN_INCLUSIVE_EXCLUSIVE);
        link.setSpan(new ForegroundColorSpan(Color.parseColor("#3D3D3D")), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return link;
    }

    private void makeLinksFocusable(TextView tv) {
        MovementMethod m = tv.getMovementMethod();
        if ((m == null) || !(m instanceof LinkMovementMethod)) {
            if (tv.getLinksClickable()) {
                tv.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }
    }

    @Override
    public void onRefresh() {
        getAllBuses();
    }


    private static class ClickableString extends ClickableSpan {
        private View.OnClickListener mListener;
        public ClickableString(View.OnClickListener listener) {
            mListener = listener;
        }
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }
    }

    void getAllBuses(){
//        if(((GuessMainActivity)getActivity()).getAllBuses()==null){
            ApiConfig getResponse = AppConfig.getRetrofit().create(ApiConfig.class);

            Call<List<Buses>> call = getResponse.getAllBuses(getActivity().getResources().getString(R.string.guess_token));
            call.enqueue(new Callback<List<Buses>>() {
                @Override
                public void onResponse(Call<List<Buses>> call, Response<List<Buses>> response) {
                    if(response.code()==200){
                        for(int i=0;i<response.body().size();i++){
                            Date d = null;
                            SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
                            try {
                                d=formatter6.parse(response.body().get(i).getDateDeparture());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            response.body().get(i).setDateSort(d);
                        }

                        Collections.sort(response.body());

                        tripAdapter=new GuestTripAdapter(getContext(),response.body(),getActivity().getResources().getString(R.string.guess_token));
//                        ((GuessMainActivity)getActivity()).setAllBuses(response.body());
                        recyclerView.setAdapter(tripAdapter);
                    }
                    else {
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
                @Override
                public void onFailure(Call<List<Buses>> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);

                }
            });
//        }
//        else {
//
//            for(int i=0;i<((GuessMainActivity)getActivity()).getAllBuses().size();i++){
//                Date d = null;
//                SimpleDateFormat formatter6=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                SimpleDateFormat output1 = new SimpleDateFormat("dd.MM");
//                try {
//                    d=formatter6.parse(((GuessMainActivity)getActivity()).getAllBuses().get(i).getDateDeparture());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                ((GuessMainActivity)getActivity()).getAllBuses().get(i).setDateSort(d);
//            }
//
//            Collections.sort(((GuessMainActivity)getActivity()).getAllBuses());
//            tripAdapter=new GuestTripAdapter(getContext(),((GuessMainActivity)getActivity()).getAllBuses(),getActivity().getResources().getString(R.string.guess_token));
//            recyclerView.setAdapter(tripAdapter);
//            recyclerView.scrollToPosition(((GuessMainActivity)getActivity()).getAllBusesAdapterPos());
//        }
    }
}