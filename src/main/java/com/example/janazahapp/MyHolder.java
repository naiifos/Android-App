package com.example.janazahapp;


import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    static TextView nbPartip;
    static TextView name;
    static TextView mosque;
    static TextView prayer;
    static TextView description;
    static TextView dateEvents;
    static RelativeLayout layout_;
    static ItemClickListener itemClickListener;
    // static Button participate;

    public MyHolder(@NonNull View itemView) {

        super(itemView);

        this.prayer = itemView.findViewById(R.id.prayerDay);
        this.name = itemView.findViewById(R.id.nameDead);
        this.description = itemView.findViewById(R.id.cardJanazahDescription);
        this.mosque = itemView.findViewById(R.id.mosqueLocalisation);
        this.nbPartip = itemView.findViewById(R.id.NbParticipants);
        this.dateEvents = itemView.findViewById(R.id.dateEvent);

        itemView.setOnClickListener(this);
        //  this.participate = itemView.findViewById(R.id.participate_);
    }

    @Override
    public void onClick(View v) {

        this.itemClickListener.onItemClickListener(v,getLayoutPosition());
    }

    public static void setItemClickListener(ItemClickListener ic) {
        itemClickListener = ic;
    }
}
