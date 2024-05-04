package com.busik.busik.Passanger.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.busik.busik.IOnBackPressed;
import com.busik.busik.Passanger.Activities.PassangerMainActivity;
import com.busik.busik.R;


public class RecSucFragment extends Fragment {

    Button backMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_rec_suc, container, false);

        backMain=view.findViewById(R.id.cv_back_main);

        backMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((PassangerMainActivity)getActivity()).setAllBuses(null);
                ((PassangerMainActivity)getActivity()).setisAllBuses(false);
                ((PassangerMainActivity)getActivity()).backMain();
            }
        });



        return view;
    }
}