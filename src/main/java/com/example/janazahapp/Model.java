package com.example.janazahapp;

import java.util.Comparator;
import android.graphics.Color;
import android.widget.Button;
import android.widget.RelativeLayout;

public class Model {

    private String name;
    private String mosque;
    private String participate;
    private String prayer;
    private String NbParticipants;
    private String description;
    private String id;
    private String authorName;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private Button btn;
    private RelativeLayout card;


    public RelativeLayout getCard() {
        return card;
    }

    public void setCard(RelativeLayout card) {
        this.card = card;
    }

    public Button getBtn() {
        return btn;
    }

    public String getMosque() {
        return mosque;
    }

    public void setMosque(String mosque) {
        this.mosque = mosque;
    }
    public void setBtn(Button btn) {
        this.btn = btn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNbParticipants() {
        return NbParticipants;
    }

    public void setNbParticipants(String nbParticipants) {
        NbParticipants = nbParticipants;
    }

    public String getPrayer() {
        return prayer;
    }

    public void setPrayer(String prayer) {
        this.prayer = prayer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getParticipate() {
        return participate;
    }

    public void setParticipate(String participate) {
        this.participate = participate;
    }
/*
    public static final Comparator<Model> By_TITLE_ASCENDING = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            return o1.getMosque().compareTo(o2.getMosque());
        }
    };
    public static final Comparator<Model> By_TITLE_DESCENDING = new Comparator<Model>() {
        @Override
        public int compare(Model o1, Model o2) {
            return o2.getMosque().compareTo(o1.getMosque());
        }
    };

*/

}
