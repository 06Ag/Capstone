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

    /*public CustomWordButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateViews(context, attrs);
    }

    public CustomWordButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateViews(context, attrs);
    }

    void inflateViews(Context context, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.fragment_fragment1_1, this);

        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.WordButton);
            selectedId = array.getInteger(0, 0);

            int img_resID = typedArray.getResourceId(R.styleable.WordButton_img, R.drawable.btn_clk_one);   //버튼 기본 이미지
            img.setImageResource(img_resID);

            String text_string = typedArray.getString(R.styleable.WordButton_text);    //수화 이름
            text.setText(text_string);

            array.recycle();
        }
    }*/
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
        text = (TextView) findViewById(R.id.txt_one);

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

        String text_string = typedArray.getString(R.styleable.WordButton_text);    //수화 이름
        text.setText(text_string);

        typedArray.recycle();

    }

    void setImg(int img_resID){
        img.setImageResource(img_resID);
    }
    void setText(String text_string){
        text.setText(text_string);
    }

}
