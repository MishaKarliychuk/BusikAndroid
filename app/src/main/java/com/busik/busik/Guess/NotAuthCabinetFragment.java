package com.busik.busik.Guess;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.busik.busik.R;

public class NotAuthCabinetFragment extends Fragment {

    TextView log;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_auth_cabinet, container, false);


        log=view.findViewById(R.id.tv1);


        foo();


        return view;
    }

    private void foo() {
        SpannableString link = makeLinkSpan("Авторизуйтесь", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((GuessMainActivity)getActivity()).setBus(false);
                ((GuessMainActivity)getActivity()).replaceFragment(new GuestAuthFragment());
            }
        });

        // We need a TextView instance.

        // Set the TextView's text
        log.append(link);

        // Append the link we created above using a function defined below.
        log.append(" щоб переглядати кабінет");

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
}