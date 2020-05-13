package com.example.janazahapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyProfileAdapter extends RecyclerView.Adapter<MyHolder> implements Filterable {

    Context c;
    ArrayList<Model> models,filterList;
    CustomerFile filter;


    public MyProfileAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
        this.filterList = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)  {

        View view    = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_card, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int i) {

        MyHolder.name.setText(models.get(i).getName());
        MyHolder.mosque.setText(models.get(i).getMosque());
        MyHolder.prayer.setText(models.get(i).getPrayer());
        MyHolder.description.setText(models.get(i).getDescription());
//        MyHolder.dateEvents.setText(models.get(i).getDescription());

        //MyHolder.participate.setText(models.get(i).getParticipate());

        MyHolder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int position) {

                // on get tout les elements qui se trouvaient dans le card.xml
                String name_ = models.get(position).getName();
                String authorName=models.get(position).getAuthorName();
                String mosque_ =  models.get(position).getMosque();
                String nbParticipants_ =  models.get(position).getNbParticipants();
                String description_ =  models.get(position).getDescription();
                String prayerDay_ =  models.get(position).getPrayer();
                String id=models.get(position).getId();
                String dates = models.get(position).getDate();
                Log.d("profileAdapter", "id: "+ id);
                // il faut ajouter la description mais celle-ci ne se trouvait pas dans le activity_card donc il faut la get a partir de la base de données

                Intent intent  = new  Intent(c, AnotherProfileActivity.class);
                intent.putExtra("nameDead",name_);
                intent.putExtra("authorName",authorName);
                Log.d("adhan", "onItemClickListener: "+authorName);
                intent.putExtra("mosque",mosque_);
                intent.putExtra("description",description_);
                intent.putExtra("nbParticipants",nbParticipants_);
                intent.putExtra("prayerDay",prayerDay_);
                intent.putExtra("id",id);
                intent.putExtra("date",dates);
                c.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @Override
    public Filter getFilter() {

        if(filter == null){
            //i removed filter for the moment cuz it don't work

            //filter = new CustomerFile(filterList,this);
        }
        return filter;
    }
}
