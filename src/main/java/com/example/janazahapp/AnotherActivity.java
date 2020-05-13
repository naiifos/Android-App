package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AnotherActivity extends AppCompatActivity {
    TextView name_,description_,nbParticipants_,mosque_,prayer_,showComments,eventDate;
    RelativeLayout card;
    Button pariticipate;
    String nbparticipants ;  private FirebaseAuth mAuth;
    String currentUserId;
    private DatabaseReference ParticipateRef;
    Boolean participationChecker  = false;
    int countLike = 0;
    private NotificationManagerCompat notificationManager;

    FirebaseAuth mFirebaseAuth;
    public static boolean filter = false;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference DateEventsRef;
    String authorNameVariable;

    private static final String CHANNEL_ID = App.CHANNEL_ID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
        notificationManager = NotificationManagerCompat.from(this);
        ParticipateRef = FirebaseDatabase.getInstance().getReference().child("UserPrayers");
        DateEventsRef = FirebaseDatabase.getInstance().getReference().child("Event");
        mFirebaseAuth= FirebaseAuth.getInstance();

        final GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(AnotherActivity.this);

        Log.d("debugTag"," current id "+ account.getDisplayName());

        currentUserId=account.getDisplayName();

        ActionBar actionBar = getSupportActionBar();

        card = findViewById(R.id.layout2);
        name_ = findViewById(R.id.nameDead);
        description_ = findViewById(R.id.cardJanazahDescription);
        nbParticipants_ = findViewById(R.id.NbParticipants);
        mosque_ = findViewById(R.id.mosqueLocalisation);
        prayer_ = findViewById(R.id.prayerDay);
        pariticipate = findViewById(R.id.participatePrayer);
        showComments = findViewById(R.id.showComments);
        eventDate = findViewById(R.id.dateEvent);


        Intent intent  = getIntent();
        final String id  = intent.getStringExtra("id");
        final String[] authorName = {intent.getStringExtra("authorname")};
        final String nameDead = intent.getStringExtra("nameDead");
        String mosque = intent.getStringExtra("mosque");
        String prayerDay = intent.getStringExtra("prayerDay");
        final String nbParticipants = intent.getStringExtra("nbParticipants");
        final String description = intent.getStringExtra("description");
        final String date = intent.getStringExtra("date");


        DateEventsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //jarrive pas a get la date de la base de donnée
                authorNameVariable = dataSnapshot.child(id).child("authorName").getValue().toString();
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    String dates = snapshot.child("date").getValue().toString(); // j
                    eventDate.setText("Event Date : " +dates);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ParticipateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Log.d("ATTENTION", " DANS PARTICIPATE REF LE PREMIER PAGE ANOTHERACTIVITY");
                if (dataSnapshot.child(id).hasChild(currentUserId))
                {
                    countLike  = (int) dataSnapshot.child(id).getChildrenCount();
                    nbParticipants_.setText("Number participants : " +countLike);
                    pariticipate.setText("I DON'T GO ");


                    Log.d("nbparticpants", " nbparticpants " + countLike);
                }else{
                    countLike  = (int) dataSnapshot.child(id).getChildrenCount();
                }

                nbParticipants_.setText("Number participants : "+ countLike);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        name_.setText(nameDead);
        nbParticipants_.setText("Number participants : "+nbParticipants);
        mosque_.setText("Mosque Adress : " +mosque);
        description_.setText(description);
        prayer_.setText("Prayer : " +prayerDay);

        pariticipate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //   Log.d("gher","value: "+currentUserId);
                participationChecker = true;

                ParticipateRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d("ATTENTION", " DANS PARTICIPATE REF LE DEUXIEMME    PAGE ANOTHERACTIVITY");
                        if (participationChecker.equals(true))
                        {
                            if (dataSnapshot.child(id).hasChild(currentUserId))
                            {
                                Log.d("messge", "id post = " + id + "  ON EST RENTREEEE");


                                countLike--;
                                ParticipateRef.child(id).child(currentUserId).removeValue();

                                pariticipate.setText("I  GO");
                                participationChecker = false;
                                nbParticipants_.setText("Number participants : " +countLike);
                            } else {

                                Log.d("messge", "id post = " + id + " ON AJOUTE LA LIGNE ");

                                ParticipateRef.child(id).child(currentUserId).setValue(true);

                                pariticipate.setText("I DON'T GO");
                                participationChecker = false;
                                countLike++;
                                nbParticipants_.setText("Number participants : " +countLike);
                            }

                            if(pariticipate.getText().equals("I DON'T GO"))
                            {
                                if(account.getDisplayName().equals(authorNameVariable))
                                {
                                    Log.d("msg", " personne connectée cest la meme que celle qui a crée levenet ");

                                   showNotification(" You participate in your own event ");
                                }else{

                                    showNotification(" Thank you for participant to event of  " + authorNameVariable);

                                    Log.d("msg", "  je participe a l'EVENET ");

                                }
                            }

                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        showComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AnotherActivity.this, CommentsActivity.class);
                intent.putExtra("id",id);
                startActivity(intent);
            }
        });

    }
    public void showNotification(String message) {
        Log.d("ATTENTION", " WE ARE IN TO SHWO NOTIFICATION");
        RemoteViews collapsedView = new RemoteViews(getPackageName(),
                R.layout.notification_collapsed);
        RemoteViews expandedView = new RemoteViews(getPackageName(),
                R.layout.notification_expanded);
        Intent clickIntent = new Intent(this, NotificationReceiver.class);
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(this,
                0, clickIntent, 0);
        collapsedView.setTextViewText(R.id.text_view_collapsed_1, message);
        expandedView.setImageViewResource(R.id.image_view_expanded, R.drawable.lotti2);
        expandedView.setOnClickPendingIntent(R.id.image_view_expanded, clickPendingIntent);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_android)
                .setCustomContentView(collapsedView)
                .setCustomBigContentView(expandedView)
                //.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .build();
        notificationManager.notify(1, notification);
    }
}
