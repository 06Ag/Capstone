package com.example.singlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

//카테고리 - 자모음
public class Fragment2 extends Fragment implements View.OnClickListener {

    public Fragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment2, container, false);
        LinearLayout image_giyeok = (LinearLayout) view.findViewById(R.id.layout_giyeok);
        LinearLayout image_nieun = (LinearLayout) view.findViewById(R.id.layout_nieun);
        LinearLayout image_digeut = (LinearLayout) view.findViewById(R.id.layout_digeut);
        LinearLayout image_rieul = (LinearLayout) view.findViewById(R.id.layout_rieul);
        LinearLayout image_mieum = (LinearLayout) view.findViewById(R.id.layout_mieum);
        LinearLayout image_bieup = (LinearLayout) view.findViewById(R.id.layout_bieup);
        LinearLayout image_siot = (LinearLayout) view.findViewById(R.id.layout_siot);
        LinearLayout image_ieung = (LinearLayout) view.findViewById(R.id.layout_ieung);
        LinearLayout image_jieut = (LinearLayout) view.findViewById(R.id.layout_jieut);
        LinearLayout image_chieut = (LinearLayout) view.findViewById(R.id.layout_chieut);
        LinearLayout image_kieuk = (LinearLayout) view.findViewById(R.id.layout_kieuk);
        LinearLayout image_tieut = (LinearLayout) view.findViewById(R.id.layout_tieut);
        LinearLayout image_pieup = (LinearLayout) view.findViewById(R.id.layout_pieup);
        LinearLayout image_hieut = (LinearLayout) view.findViewById(R.id.layout_hieut);
        LinearLayout image_a = (LinearLayout) view.findViewById(R.id.layout_a);
        LinearLayout image_ya = (LinearLayout) view.findViewById(R.id.layout_ya);
        LinearLayout image_eo = (LinearLayout) view.findViewById(R.id.layout_eo);
        LinearLayout image_yeo = (LinearLayout) view.findViewById(R.id.layout_yeo);
        LinearLayout image_o = (LinearLayout) view.findViewById(R.id.layout_o);
        LinearLayout image_yo = (LinearLayout) view.findViewById(R.id.layout_yo);
        LinearLayout image_u = (LinearLayout) view.findViewById(R.id.layout_u);
        LinearLayout image_yu = (LinearLayout) view.findViewById(R.id.layout_yu);
        LinearLayout image_eu = (LinearLayout) view.findViewById(R.id.layout_eu);
        LinearLayout image_i = (LinearLayout) view.findViewById(R.id.layout_i);
        LinearLayout image_ae = (LinearLayout) view.findViewById(R.id.layout_ae);
        LinearLayout image_yae = (LinearLayout) view.findViewById(R.id.layout_yae);
        LinearLayout image_e = (LinearLayout) view.findViewById(R.id.layout_e);
        LinearLayout image_ye = (LinearLayout) view.findViewById(R.id.layout_ye);
        LinearLayout image_ui = (LinearLayout) view.findViewById(R.id.layout_ui);
        LinearLayout image_oe = (LinearLayout) view.findViewById(R.id.layout_oe);
        LinearLayout image_wi = (LinearLayout) view.findViewById(R.id.layout_wi);

        image_giyeok.setOnClickListener(this);
        image_nieun.setOnClickListener(this);
        image_digeut.setOnClickListener(this);
        image_rieul.setOnClickListener(this);
        image_mieum.setOnClickListener(this);
        image_bieup.setOnClickListener(this);
        image_siot.setOnClickListener(this);
        image_ieung.setOnClickListener(this);
        image_jieut.setOnClickListener(this);
        image_chieut.setOnClickListener(this);
        image_kieuk.setOnClickListener(this);
        image_tieut.setOnClickListener(this);
        image_pieup.setOnClickListener(this);
        image_hieut.setOnClickListener(this);
        image_a.setOnClickListener(this);
        image_ya.setOnClickListener(this);
        image_eo.setOnClickListener(this);
        image_yeo.setOnClickListener(this);
        image_o.setOnClickListener(this);
        image_yo.setOnClickListener(this);
        image_u.setOnClickListener(this);
        image_yu.setOnClickListener(this);
        image_eu.setOnClickListener(this);
        image_i.setOnClickListener(this);
        image_ae.setOnClickListener(this);
        image_yae.setOnClickListener(this);
        image_e.setOnClickListener(this);
        image_ye.setOnClickListener(this);
        image_ui.setOnClickListener(this);
        image_oe.setOnClickListener(this);
        image_wi.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), CategoryLearning_study_info.class);

        if (v.getId() == R.id.layout_giyeok)   intent.putExtra("name", "ㄱ [기역]");
        else if(v.getId() == R.id.layout_nieun)   intent.putExtra("name", "ㄴ [니은]");
        else if(v.getId() == R.id.layout_digeut)   intent.putExtra("name", "ㄷ [디귿]");
        else if(v.getId() == R.id.layout_rieul)   intent.putExtra("name", "ㄹ [리을]");
        else if(v.getId() == R.id.layout_mieum)   intent.putExtra("name", "ㅁ [미음]");
        else if(v.getId() == R.id.layout_bieup)   intent.putExtra("name", "ㅂ [비읍]");
        else if(v.getId() == R.id.layout_siot)   intent.putExtra("name", "ㅅ [시옷]");
        else if(v.getId() == R.id.layout_ieung)   intent.putExtra("name", "ㅇ [이응]");
        else if(v.getId() == R.id.layout_jieut)   intent.putExtra("name", "ㅈ [지읒]");
        else if(v.getId() == R.id.layout_chieut)   intent.putExtra("name", "ㅊ [치읓]");
        else if(v.getId() == R.id.layout_kieuk)   intent.putExtra("name", "ㅋ [키읔]");
        else if(v.getId() == R.id.layout_tieut)   intent.putExtra("name", "ㅌ [티읕]");
        else if(v.getId() == R.id.layout_pieup)   intent.putExtra("name", "ㅍ [피읖]");
        else if(v.getId() == R.id.layout_hieut)   intent.putExtra("name", "ㅎ [히읗]");
        else if(v.getId() == R.id.layout_a)   intent.putExtra("name", "ㅏ [아]");
        else if(v.getId() == R.id.layout_ya)   intent.putExtra("name", "ㅑ [야]");
        else if(v.getId() == R.id.layout_eo)   intent.putExtra("name", "ㅓ [어]");
        else if(v.getId() == R.id.layout_yeo)   intent.putExtra("name", "ㅕ [여]");
        else if(v.getId() == R.id.layout_o)   intent.putExtra("name", "ㅗ [오]");
        else if(v.getId() == R.id.layout_yo)   intent.putExtra("name", "ㅛ [요]");
        else if(v.getId() == R.id.layout_u)   intent.putExtra("name", "ㅜ [우]");
        else if(v.getId() == R.id.layout_yu)   intent.putExtra("name", "ㅠ [유]");
        else if(v.getId() == R.id.layout_eu)   intent.putExtra("name", "ㅡ [으]");
        else if(v.getId() == R.id.layout_i)   intent.putExtra("name", "ㅣ [이]");
        else if(v.getId() == R.id.layout_ae)   intent.putExtra("name", "ㅐ [애]");
        else if(v.getId() == R.id.layout_yae)   intent.putExtra("name", "ㅒ [얘]");
        else if(v.getId() == R.id.layout_e)   intent.putExtra("name", "ㅔ [에]");
        else if(v.getId() == R.id.layout_ye)   intent.putExtra("name", "ㅖ [예]");
        else if(v.getId() == R.id.layout_ui)   intent.putExtra("name", "ㅢ [의]");
        else if(v.getId() == R.id.layout_oe)   intent.putExtra("name", "ㅚ [외]");
        else if(v.getId() == R.id.layout_wi)   intent.putExtra("name", "ㅟ [위]");

        startActivity(intent);
    }
}
