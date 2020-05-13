package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileEvent extends AppCompatActivity {
    TextView name_,description_,nbParticipants_,mosque_,prayer_;
    RelativeLayout card;
    Button pariticipate;

    //DATABASE
    DatabaseReference reff;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_event);


        ActionBar actionBar = getSupportActionBar();

        card = findViewById(R.id.layout2);
        name_ = findViewById(R.id.nameDead);
        description_ = findViewById(R.id.cardJanazahDescription);
        nbParticipants_ = findViewById(R.id.NbParticipants);
        mosque_ = findViewById(R.id.mosqueLocalisation);
        prayer_ = findViewById(R.id.prayerDay);
        pariticipate = findViewById(R.id.participatePrayer);

        //for the supression
        final String id= getIntent().getIdentifier();
        Log.d("idvalue","id: "+id);

        Intent intent  = getIntent();

        String nameDead = intent.getStringExtra("nameDead");
        String mosque = intent.getStringExtra("mosque");
        String prayerDay = intent.getStringExtra("prayerDay");
        String nbParticipants = intent.getStringExtra("nbParticipants");
        String description = intent.getStringExtra("description");

        // android:background="#afeeee"

        name_.setText(nameDead);
        nbParticipants_.setText(nbParticipants);
        mosque_.setText(mosque);
        description_.setText(description);
        prayer_.setText(prayerDay);




    }
}
