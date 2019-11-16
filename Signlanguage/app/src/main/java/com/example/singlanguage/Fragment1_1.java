package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class Fragment1_1 extends Fragment implements View.OnClickListener {

    public Fragment1_1() {
            // Required empty public constructor
        }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1_1, container, false);
        LinearLayout image1 = (LinearLayout) view.findViewById(R.id.layout_one);
        LinearLayout image2 = (LinearLayout) view.findViewById(R.id.layout_two);
        LinearLayout image3 = (LinearLayout) view.findViewById(R.id.layout_three);
        LinearLayout image4 = (LinearLayout) view.findViewById(R.id.layout_four);
        LinearLayout image5 = (LinearLayout) view.findViewById(R.id.layout_five);
        LinearLayout image6 = (LinearLayout) view.findViewById(R.id.layout_six);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);
        //Button bt_one = (Button)view.findViewById(R.id.button_one);
        //bt_two = (Button)view.findViewById(R.id.button_two);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_one)   intent.putExtra("name", "1");
        else if(v.getId() == R.id.layout_two)   intent.putExtra("name", "2");
        else if(v.getId() == R.id.layout_three)   intent.putExtra("name", "3");
        else if(v.getId() == R.id.layout_four)   intent.putExtra("name", "4");
        else if(v.getId() == R.id.layout_five)   intent.putExtra("name", "5");
        else if(v.getId() == R.id.layout_six)   intent.putExtra("name", "6");

        startActivity(intent);
    }
}

