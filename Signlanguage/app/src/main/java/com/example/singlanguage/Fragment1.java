package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class Fragment1 extends Fragment implements View.OnClickListener {

    public Fragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment1, container, false);
        LinearLayout image0 = (LinearLayout) view.findViewById(R.id.layout_zero);
        LinearLayout image1 = (LinearLayout) view.findViewById(R.id.layout_one);
        LinearLayout image2 = (LinearLayout) view.findViewById(R.id.layout_two);
        LinearLayout image3 = (LinearLayout) view.findViewById(R.id.layout_three);
        LinearLayout image4 = (LinearLayout) view.findViewById(R.id.layout_four);
        LinearLayout image5 = (LinearLayout) view.findViewById(R.id.layout_five);
        LinearLayout image6 = (LinearLayout) view.findViewById(R.id.layout_six);
        LinearLayout image7 = (LinearLayout) view.findViewById(R.id.layout_seven);
        LinearLayout image8 = (LinearLayout) view.findViewById(R.id.layout_eight);
        LinearLayout image9 = (LinearLayout) view.findViewById(R.id.layout_nine);
        LinearLayout image10 = (LinearLayout) view.findViewById(R.id.layout_ten);
        image0.setOnClickListener(this);
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        image3.setOnClickListener(this);
        image4.setOnClickListener(this);
        image5.setOnClickListener(this);
        image6.setOnClickListener(this);
        image7.setOnClickListener(this);
        image8.setOnClickListener(this);
        image9.setOnClickListener(this);
        image10.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_zero)   intent.putExtra("name", "0");
        else if(v.getId() == R.id.layout_one)   intent.putExtra("name", "1");
        else if(v.getId() == R.id.layout_two)   intent.putExtra("name", "2");
        else if(v.getId() == R.id.layout_three)   intent.putExtra("name", "3");
        else if(v.getId() == R.id.layout_four)   intent.putExtra("name", "4");
        else if(v.getId() == R.id.layout_five)   intent.putExtra("name", "5");
        else if(v.getId() == R.id.layout_six)   intent.putExtra("name", "6");
        else if(v.getId() == R.id.layout_seven)   intent.putExtra("name", "7");
        else if(v.getId() == R.id.layout_eight)   intent.putExtra("name", "8");
        else if(v.getId() == R.id.layout_nine)   intent.putExtra("name", "9");
        else if(v.getId() == R.id.layout_ten)   intent.putExtra("name", "10");

        startActivity(intent);
    }
}

