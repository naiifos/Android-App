package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

public class Profile extends AppCompatActivity {

    TextView tvPseudo;
    TextView emptyTv;

    FirebaseAuth mFirebaseAuth;
    ImageView profileImage;
    ImageView backPic;



    //for the event of the connected user
    RecyclerView card;
    MyProfileAdapter myAdapter;
    SharedPreferences preferences;
    ArrayList<Model> models = new ArrayList<>();

    //DATABASE
    DatabaseReference reff;
    DatabaseReference mRef;
    public GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();
        super.onStart();

        mFirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mFireBaseUser = mFirebaseAuth.getCurrentUser();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Profile.this);

        tvPseudo = findViewById(R.id.tvPseudo);
        emptyTv=findViewById(R.id.emptyTv);
        profileImage = findViewById(R.id.pp);
        backPic = findViewById(R.id.backPic);
        tvPseudo.setTextColor(Color.parseColor("#FFFFFF"));
        tvPseudo.setText(account.getDisplayName());

        String photoUrl = account.getPhotoUrl().toString(); //not okay, value == null
        Picasso.with(this).load(photoUrl).into(profileImage);

        //for the events of the connected user
        card = findViewById(R.id.PrayersEvents);
        card.setNestedScrollingEnabled(false);

        getMyList();

    }


    @Override
    public boolean  onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu2,menu);
        /*
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){

            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                myAdapter.getFilter().filter(newText);
                return false;
            }
        });

        */
        return  true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {
                            // ...
                            Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(getApplicationContext(),LoginActivity.class);
                            startActivity(i);
                        }
                    }
            );

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getMyList() {
        //DATABASE

        Intent intent  = getIntent();
        final String id  = intent.getStringExtra("id");
        reff= FirebaseDatabase.getInstance().getReference().child("UserPrayers");
        mRef = FirebaseDatabase.getInstance().getReference().child("Event");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(Profile.this);

                for (final DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    //  comparer le nom du user actuelle qui est connecté avec tout les noms dans Userprayers et si un est egale a true cest bon

                    String valueFirebase = snapshot.getValue().toString();
                    String[] keyValue = valueFirebase.split("=");
                    String username = keyValue[0];
                    String newUsername = "";
                    for(int i = 1; i<username.length(); i++)
                    {
                        newUsername+=username.charAt(i);

                    }
                    if(newUsername.equals(account.getDisplayName()))
                    {
                        Log.d("postid ", "id du post = "+snapshot.getKey().toString()+"nom du user : "+ newUsername + "  current username = " + account.getDisplayName());

                        final String cléPostParticipe = snapshot.getKey();

                        mRef.addValueEventListener(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot_)
                            {
                                for(DataSnapshot snapshots: dataSnapshot_.getChildren()){

                                    if(cléPostParticipe.equals(snapshots.getKey().toString()))
                                    {
                                        Log.d("postid ", "id du post = "+ cléPostParticipe + " id du post firebase = " +snapshots.getKey().toString());


                                        String authorName = snapshots.child("nameDead").getValue().toString();
                                        String realAuthorName=snapshots.child("authorName").getValue().toString();
                                        String prayer = snapshots.child("choosePrayer").getValue().toString();
                                        String description = snapshots.child("descriptionPrayer").getValue().toString();
                                        String nameDead  = snapshots.child("nameDead").getValue().toString();
                                        String mosqueAdress = snapshots.child("chooseMosque").getValue().toString();
                                        String date = snapshots.child("date").getValue().toString();
                                        String id=snapshots.getKey();

                                        // remplissage Arraylist models
                                        Model e = new Model();
                                        e.setName(authorName);
                                        e.setDate(date);
                                        e.setAuthorName(realAuthorName);
                                        e.setMosque("Mosque Adress :  "+mosqueAdress);
                                        e.setPrayer("Prayer : "+prayer);
                                        e.setNbParticipants("Number participants : 0");
                                        e.setDescription(description);
                                        e.setId(id);
                                        models.add(e);


                                    }
                                }
                                if(models.size()==0){
                                    emptyTv.setText("You don't have any event at the moment.");
                                    emptyTv.setPaintFlags(Paint.FAKE_BOLD_TEXT_FLAG);
                                }
                                // verification ArrayList models de remplissage
                                for (Model object: models) {
                                    //   System.out.println(object.getName());
                                    Log.d("Files", " Verification Arraylist models "+ object.getName());

                                }
                                card.setLayoutManager(new LinearLayoutManager(Profile.this));
                                myAdapter = new MyProfileAdapter(Profile.this, models);
                                card.setAdapter(myAdapter);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });

                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        Log.d("Files", "  HUEHUEUEHUEHUEH ");

    }
}
