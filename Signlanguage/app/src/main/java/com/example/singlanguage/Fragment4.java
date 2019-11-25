package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

//카테고리 - 사람
public class Fragment4 extends Fragment implements View.OnClickListener{

    public Fragment4() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment4, container, false);

        LinearLayout image_customer = (LinearLayout) view.findViewById(R.id.layout_customer);
        LinearLayout image_woman = (LinearLayout) view.findViewById(R.id.layout_woman);
        LinearLayout image_man = (LinearLayout) view.findViewById(R.id.layout_man);
        LinearLayout image_teacher = (LinearLayout) view.findViewById(R.id.layout_teacher);

        image_customer.setOnClickListener(this);
        image_woman.setOnClickListener(this);
        image_man.setOnClickListener(this);
        image_teacher.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_customer)   intent.putExtra("name", "손님");
        else if(v.getId() == R.id.layout_woman)   intent.putExtra("name", "여자");
        else if(v.getId() == R.id.layout_man)   intent.putExtra("name", "남자");
        else if(v.getId() == R.id.layout_teacher)   intent.putExtra("name", "선생님");

        startActivity(intent);
    }
}

