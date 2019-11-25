package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

//카테고리 - 기타
public class Fragment6 extends Fragment implements View.OnClickListener {

    public Fragment6() {
            // Required empty public constructor
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment6, container, false);
        LinearLayout image_itch = (LinearLayout) view.findViewById(R.id.layout_itch);
        LinearLayout image_presentation = (LinearLayout) view.findViewById(R.id.layout_presentation);
        LinearLayout image_read = (LinearLayout) view.findViewById(R.id.layout_read);
        LinearLayout image_discuss = (LinearLayout) view.findViewById(R.id.layout_discuss);
        LinearLayout image_north = (LinearLayout) view.findViewById(R.id.layout_north);
        LinearLayout image_south = (LinearLayout) view.findViewById(R.id.layout_south);

        image_itch.setOnClickListener(this);
        image_presentation.setOnClickListener(this);
        image_read.setOnClickListener(this);
        image_discuss.setOnClickListener(this);
        image_north.setOnClickListener(this);
        image_south.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_itch)   intent.putExtra("name", "가렵다");
        else if(v.getId() == R.id.layout_presentation)   intent.putExtra("name", "발표하다");
        else if(v.getId() == R.id.layout_read)   intent.putExtra("name", "읽다");
        else if(v.getId() == R.id.layout_discuss)   intent.putExtra("name", "상의하다");
        else if(v.getId() == R.id.layout_north)   intent.putExtra("name", "북쪽");
        else if(v.getId() == R.id.layout_south)   intent.putExtra("name", "남쪽");

        startActivity(intent);
    }
}

