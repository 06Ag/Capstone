package com.example.singlanguage;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//카테고리 - 사물
public class Fragment3 extends Fragment implements View.OnClickListener{

    public Fragment3() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment3, container, false);

        LinearLayout image_computer = (LinearLayout) view.findViewById(R.id.layout_computer);
        LinearLayout image_school = (LinearLayout) view.findViewById(R.id.layout_school);
        LinearLayout image_house = (LinearLayout) view.findViewById(R.id.layout_house);
        LinearLayout image_powder = (LinearLayout) view.findViewById(R.id.layout_powder);

        image_computer.setOnClickListener(this);
        image_school.setOnClickListener(this);
        image_house.setOnClickListener(this);
        image_powder.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_computer)   intent.putExtra("name", "컴퓨터");
        else if(v.getId() == R.id.layout_school)   intent.putExtra("name", "학교");
        else if(v.getId() == R.id.layout_house)   intent.putExtra("name", "집");
        else if(v.getId() == R.id.layout_powder)   intent.putExtra("name", "가루");

        startActivity(intent);
    }
}
