package com.example.janazahapp;

public class Event {
    private String authorName;
    private String nameDead;
    private String chooseMosque;
    private String mosqueName;
    private String choosePrayer;
    private String descriptionPrayer;
    private String date;

    private int participants;

    public Event() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMosqueName() {
        return mosqueName;
    }

    public void setMosqueName(String mosqueName) {
        this.mosqueName = mosqueName;
    }

    public int getParticipants() {
        return participants;
    }

    public void setParticipants(int participants) {
        this.participants = participants;
    }
    public void setNameDead(String nameDead) {
        this.nameDead = nameDead;
    }
    public void setAuthorName(String setAuthorName) {
        this.authorName = setAuthorName;
    }

    public void setChooseMosque(String chooseMosque) {
        this.chooseMosque = chooseMosque;
    }

    public void setChoosePrayer(String choosePrayer) {
        this.choosePrayer = choosePrayer;
    }

    public void setDescriptionPrayer(String descriptionPrayer) {
        this.descriptionPrayer = descriptionPrayer;
    }

    public String getNameDead() {
        return nameDead;
    }
    public String getAuthorName() {
        return authorName;
    }

    public String getChooseMosque() {
        return chooseMosque;
    }

    public String getChoosePrayer() {
        return choosePrayer;
    }

    public String getDescriptionPrayer() {
        return descriptionPrayer;
    }
}
