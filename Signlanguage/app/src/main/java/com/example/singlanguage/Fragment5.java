package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//카테고리 - 음식
public class Fragment5 extends Fragment implements View.OnClickListener{

    public Fragment5() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment5, container, false);

        LinearLayout image_hardtack = (LinearLayout) view.findViewById(R.id.layout_hardtack);
        LinearLayout image_meat = (LinearLayout) view.findViewById(R.id.layout_meat);
        LinearLayout image_ricdough = (LinearLayout) view.findViewById(R.id.layout_ricedough);
        LinearLayout image_fruit = (LinearLayout) view.findViewById(R.id.layout_fruit);

        image_hardtack.setOnClickListener(this);
        image_meat.setOnClickListener(this);
        image_ricdough.setOnClickListener(this);
        image_fruit.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_hardtack)   intent.putExtra("name", "건빵");
        else if(v.getId() == R.id.layout_meat)   intent.putExtra("name", "고기");
        else if(v.getId() == R.id.layout_ricedough)   intent.putExtra("name", "떡");
        else if(v.getId() == R.id.layout_fruit)   intent.putExtra("name", "열매");

        startActivity(intent);
    }
}

