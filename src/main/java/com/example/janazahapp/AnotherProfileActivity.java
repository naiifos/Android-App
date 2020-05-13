package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

public class AnotherProfileActivity extends AppCompatActivity{
    TextView name_,description_,nbParticipants_,mosque_,prayer_,dateEvent,showComments;
    RelativeLayout card;
    Button pariticipate;
    private DatabaseReference ParticipateRef;

    //DATABASE
    DatabaseReference reff;
    DatabaseReference reff1;
    boolean mine=false;//to see if event belong to the connected user or not

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tonpr", "onCreate: ");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.another_profile_activity);
        final GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(AnotherProfileActivity.this);
        ActionBar actionBar = getSupportActionBar();

        card = findViewById(R.id.layout2);
        name_ = findViewById(R.id.nameDead);
        description_ = findViewById(R.id.cardJanazahDescription);
        nbParticipants_ = findViewById(R.id.NbParticipants);
        mosque_ = findViewById(R.id.mosqueLocalisation);
        prayer_ = findViewById(R.id.prayerDay);
        pariticipate = findViewById(R.id.participatePrayer);
        dateEvent = findViewById(R.id.dateEvent);
        showComments = findViewById(R.id.showComments);

        Intent intent  = getIntent();

        String nameDead = intent.getStringExtra("nameDead");
        final String authorName = intent.getStringExtra("authorName");
        String mosque = intent.getStringExtra("mosque");
        String prayerDay = intent.getStringExtra("prayerDay");
        String nbParticipants = intent.getStringExtra("nbParticipants");
        String description = intent.getStringExtra("description");
        String dates = intent.getStringExtra("date");
        final String clickedId=intent.getStringExtra("id");

        ParticipateRef = FirebaseDatabase.getInstance().getReference().child("UserPrayers");
        dateEvent.setText("Date Event : " +dates);
        ParticipateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int countLike= 0;
                if (dataSnapshot.child(clickedId).hasChild(account.getDisplayName()))
                {
                    countLike  = (int) dataSnapshot.child(clickedId).getChildrenCount();

                }else{


                }
                nbParticipants_.setText("Number participants : "+ countLike);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        showComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AnotherProfileActivity.this, CommentsActivity.class);
                intent.putExtra("id",clickedId);
                startActivity(intent);
            }
        });

        Log.d("anotherProfile", "id: "+ clickedId);
        // android:background="#afeeee"
        Log.d("adhan", "onCreate: "+authorName);


        if(account.getDisplayName().equals(authorName)){//means that le user clicked on its own event
            pariticipate.setText("DELETE THIS EVENT");
            mine=true;
        }
        else{
            pariticipate.setText("I DON'T GO");
            mine =false;
        }

        name_.setText(nameDead);
        nbParticipants_.setText(nbParticipants);
        mosque_.setText(mosque);
        description_.setText(description);
        prayer_.setText(prayerDay);
        reff= FirebaseDatabase.getInstance().getReference().child("Event");


        final boolean finalMine = mine;
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.d("moundax","value: ");
                if(which==DialogInterface.BUTTON_POSITIVE){
                    reff1= FirebaseDatabase.getInstance().getReference();

                    if(mine ==true){
                        reff.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    Log.d("dedans", "clickedid: "+clickedId+ " date = " + snapshot.child("date").getValue().toString());
                                    // extraction db
                                    String id = snapshot.getKey();
                                    Log.d("dedans", "clickedid: "+clickedId+" /id:"+id);
                                    Log.d("ATTENTION", " DANS REFF  PAGE ANOTHER PROFILE ACTIVITY");
                                    if(clickedId.equals(id)){
                                        Log.d("mhm", "on rentr dans l id: "+clickedId);
                                        snapshot.getRef().removeValue();

                                        reff1.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    // extraction db
                                                    String id = snapshot.getKey();
                                                    Log.d("dedans", "clickedid: "+clickedId+" /id:"+id);

                                                    if(clickedId.equals(id)){
                                                        snapshot.getRef().removeValue();
                                                        Toast.makeText(AnotherProfileActivity.this,"Event deleted successfully", Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });

                                        Toast.makeText(AnotherProfileActivity.this,"Event deleted successfully", Toast.LENGTH_SHORT).show();

                                    }

                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                    else{
                        reff1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                Log.d("ATTENTION", " DANS REFF11  PAGE ANOTHER PROFILE ACTIVITY");
                                for (DataSnapshot snapshot : dataSnapshot.child("UserPrayers").getChildren()) {
                                    // extraction db
                                    String id = snapshot.getKey();

                                    Log.d("dedans", "clickedid: "+clickedId+" /id:"+id);

                                    if(clickedId.equals(id)){

                                    //    ParticipateRef.child.child(currentUserId).removeValue();


                                        Log.d("ATTENTION", " DANS REFF11 CONDITION IF   PAGE ANOTHER PROFILE ACTIVITY");
                                        snapshot.child(account.getDisplayName()).getRef().removeValue();
                                        Log.d("mhm"," on rentr dans l id: " +snapshot.child(authorName));

                                        Toast.makeText(AnotherProfileActivity.this, "You are no longer part of this event.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }


                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }

                    Intent intent = new Intent(AnotherProfileActivity.this, Profile.class);
                    startActivity(intent);
                }
                else if(which==DialogInterface.BUTTON_NEGATIVE) {

                }
            }
        };



        pariticipate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AnotherProfileActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });






    }
}
