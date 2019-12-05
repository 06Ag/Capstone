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
        LinearLayout image_slave = (LinearLayout) view.findViewById(R.id.layout_slave);
        LinearLayout image_ggun = (LinearLayout) view.findViewById(R.id.layout_ggun);
        LinearLayout image_champion = (LinearLayout) view.findViewById(R.id.layout_champion);
        LinearLayout image_gentleman = (LinearLayout) view.findViewById(R.id.layout_gentleman);
        LinearLayout image_kid = (LinearLayout) view.findViewById(R.id.layout_kid);
        LinearLayout image_orphan = (LinearLayout) view.findViewById(R.id.layout_orphan);
        LinearLayout image_you = (LinearLayout) view.findViewById(R.id.layout_you);
        LinearLayout image_wife = (LinearLayout) view.findViewById(R.id.layout_wife);

        image_customer.setOnClickListener(this);
        image_woman.setOnClickListener(this);
        image_man.setOnClickListener(this);
        image_teacher.setOnClickListener(this);
        image_slave.setOnClickListener(this);
        image_ggun.setOnClickListener(this);
        image_champion.setOnClickListener(this);
        image_gentleman.setOnClickListener(this);
        image_kid.setOnClickListener(this);
        image_orphan.setOnClickListener(this);
        image_you.setOnClickListener(this);
        image_wife.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_customer)   intent.putExtra("name", "손님");
        else if(v.getId() == R.id.layout_woman)   intent.putExtra("name", "여자");
        else if(v.getId() == R.id.layout_man)   intent.putExtra("name", "남자");
        else if(v.getId() == R.id.layout_teacher)   intent.putExtra("name", "선생님");
        else if(v.getId() == R.id.layout_slave)   intent.putExtra("name", "노예");
        else if(v.getId() == R.id.layout_ggun)   intent.putExtra("name", "꾼");
        else if(v.getId() == R.id.layout_champion)   intent.putExtra("name", "챔피언");
        else if(v.getId() == R.id.layout_gentleman)   intent.putExtra("name", "신사");
        else if(v.getId() == R.id.layout_kid)   intent.putExtra("name", "꼬마");
        else if(v.getId() == R.id.layout_orphan)   intent.putExtra("name", "고아");
        else if(v.getId() == R.id.layout_you)   intent.putExtra("name", "녀석");
        else if(v.getId() == R.id.layout_wife)   intent.putExtra("name", "아내");
        startActivity(intent);
    }
}

