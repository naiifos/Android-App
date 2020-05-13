package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class CreateEvent extends AppCompatActivity {
    private ActionBar toolbar;

    private EditText nameDead;
    private Button chooseMosque;
    private Spinner choosePrayer;
    private EditText descriptionPrayer;
    private TextView dateEvent;
    private Button submitEvent;
    String address="";
    String mosqueName;

    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //DATABASE VARIABLES
    DatabaseReference reff;
    Event event;


    ArrayAdapter<CharSequence> adapter;
    public static final int REQUEST_CODE = 1;
    //when i use the following code it doesn't work
    // DON'T FORGET TO CHECK LATER WHY
    /*
    LatLng latLng= new LatLng(12,12);
    latLng = getIntent().getExtras().getParcelable("latLng");
    */
    //but the following code works.
    Intent intent=getIntent();
   //Bundle extras=getIntent().getExtras();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        mFirebaseAuth= FirebaseAuth.getInstance();

        GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(this);
        Log.d("tamr","value:"+account.getDisplayName());
        nameDead =findViewById(R.id.nameDead);
        chooseMosque=findViewById(R.id.chooseMosque);
        choosePrayer = findViewById(R.id.choosePrayer);
        descriptionPrayer = findViewById(R.id.janazahDescription);
        dateEvent = findViewById(R.id.dateEvent);

        submitEvent = findViewById(R.id.submitEvent);

        chooseMosque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*ChooseMosquePopup chooseMosquePopup=new ChooseMosquePopup(createEvent.this);
                chooseMosquePopup.setTitle("choose a mosque to pray in");
                chooseMosquePopup.build();*/
                Intent intent = new Intent(CreateEvent.this, SelectMosque.class);
                startActivityForResult(intent , REQUEST_CODE);
            }
        });

        ArrayAdapter<CharSequence> adapter;
        Spinner spinner = (Spinner) findViewById(R.id.choosePrayer);
        adapter = ArrayAdapter.createFromResource(this, R.array.prayers_, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        final String prayerChoosed = spinner.getSelectedItem().toString();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        submitEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name=nameDead.getText().toString();
                String prayer = choosePrayer.getSelectedItem().toString();
                String description=descriptionPrayer.getText().toString();
                String date = dateEvent.getText().toString();
                FirebaseUser mFireBaseUser=mFirebaseAuth.getCurrentUser();

                GoogleSignInAccount account=GoogleSignIn.getLastSignedInAccount(CreateEvent.this);
                if(account !=null){
                    Log.d("gher","value: "+account.getDisplayName());
                }

                Log.d("testuser","value: "+mFireBaseUser);
                String authorName=account.getDisplayName();
                if(!name.isEmpty() && !prayer.isEmpty() && !description.isEmpty()){
                    //HERE WE WILL MANAGE WHAT IS RELATED TO THE DATABASE

                    event=new Event();
                    reff= FirebaseDatabase.getInstance().getReference().child("Event");

                    event.setNameDead(name);
                    event.setChoosePrayer(prayer);
                    event.setChooseMosque(address);
                    event.setDescriptionPrayer(description);
                    event.setAuthorName(authorName);
                    event.setParticipants(0);
                    event.setMosqueName(mosqueName);
                    event.setDate(date);

                    reff.push().setValue(event);
                    //reff.child("event1").setValue(event);
                    Toast.makeText(CreateEvent.this,"Event creation successful: "+ mosqueName,Toast.LENGTH_SHORT).show();
                }
                if(name.isEmpty()){
                    nameDead.setError("Please enter a name");
                    nameDead.requestFocus();
                }
                if(prayer.isEmpty()){
                    TextView errorText = (TextView)choosePrayer.getSelectedView();
                    errorText.setError("anything here, just to add the icon");
                    errorText.setTextColor(Color.RED);//just to highlight that this is an error
                    errorText.setText("Choose a prayer");//changes the selected item text to this
                    choosePrayer.requestFocus();
                }if(description.isEmpty()){
                    descriptionPrayer.setError("Please enter a description");
                    descriptionPrayer.requestFocus();
                }
                if(address.isEmpty()){
                    chooseMosque.setBackgroundColor(Color.RED);
                    chooseMosque.requestFocus();
                    new CountDownTimer(5000, 50) {

                        @Override
                        public void onTick(long arg0) {
                            // TODO Auto-generated method stub

                        }
                        @Override
                        public void onFinish() {
                            chooseMosque.setBackgroundColor(Color.WHITE);
                        }
                    }.start();
                }if(date.isEmpty()){
                    dateEvent.setError("Please enter a date");
                    dateEvent.requestFocus();

                }
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("alz","on rentre");
        if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK){
            /////////IT ALWAYS RETURN DEFAULT VALUE
            //the solution was to use the variable data instead of getIntent()
            LatLng latLng =data.getExtras().getParcelable("latLng");
            Geocoder geocoder= new Geocoder(this, Locale.getDefault());
            try {

                List<Address> addresses;
                addresses=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                String address_=addresses.get(0).getFeatureName().toString();
                address=address_;
                mosqueName=addresses.get(0).getFeatureName();
                Log.d("tamr","value: "+mosqueName);

                /*String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                */

            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }
}
