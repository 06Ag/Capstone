package com.example.singlanguage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class Fragment1_1 extends Fragment {

    public Fragment1_1() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_fragment1_1, container, false);
            Button bt_one = (Button)view.findViewById(R.id.button_one);
            Button bt_two = (Button)view.findViewById(R.id.button_two);

        /*bt_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //카테고리학습 페이지로 이동
                Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                //학습할 단어 이름 넘기기
                intent.putExtra("name", "1");
                startActivity(intent);
            }
        });*/

        return view;
    }
}

