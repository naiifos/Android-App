package com.example.janazahapp;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

public class ChooseMosquePopup extends Dialog {
    private String title1;
    private TextView titleView;
    //constructor
    public ChooseMosquePopup(Activity activity){
        super(activity,R.style.Theme_AppCompat_DayNight_NoActionBar);
        setContentView(R.layout.acivity_maps);
        this.title1="Choose a mosque to pray in";
        this.titleView=findViewById(R.id.title);
    }
    public void setTitle(String title) {this.title1=title;}
    public void build(){
        show();
        this.titleView.setText(title1);
    }
}
