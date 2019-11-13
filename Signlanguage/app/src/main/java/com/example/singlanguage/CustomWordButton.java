package com.example.singlanguage;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomWordButton extends LinearLayout {
    ImageView img;
    //ImageView img_press;
    TextView text;
    LinearLayout llayout;

    public CustomWordButton(Context context){
        super(context);
        initView();
    }
    public CustomWordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);

    }

    public CustomWordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }
    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.fragment_fragment1_1, this, false);
        addView(v);

        llayout = (LinearLayout)findViewById(R.id.layout_one);
        img = (ImageView) findViewById(R.id.image_one);
        //img_press = (ImageView) findViewById(R.id.img_press);

        text = (TextView) findViewById(R.id.text);

    }
    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WordButton);

        setTypeArray(typedArray);
    }


    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WordButton, defStyle, 0);
        setTypeArray(typedArray);

    }


    private void setTypeArray(TypedArray typedArray) {  //버튼 설정

        int img_resID = typedArray.getResourceId(R.styleable.WordButton_img, R.drawable.btn_clk_one);   //버튼 기본 이미지
        img.setImageResource(img_resID);

        //int img_press_resID = typedArray.getResourceId(R.styleable.WordButton_img_press, R.drawable.btn_clk_one);   //버튼 눌렀을 때 이미지
        //img_press.setImageResource(img_press_resID);

        String text_string = typedArray.getString(R.styleable.WordButton_text);    //수화 이름
        text.setText(text_string);

        typedArray.recycle();

    }

}
