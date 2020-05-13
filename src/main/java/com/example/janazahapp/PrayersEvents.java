package com.example.janazahapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;

import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class PrayersEvents extends AppCompatActivity {
    RecyclerView card;
    MyAdapter myAdapter;
    SharedPreferences preferences;
    ArrayList<Model> models = new ArrayList<>();

    //DATABASE
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pryer);
        //Toast.makeText(getApplicationContext()," page prayers " ,Toast.LENGTH_SHORT).show();

        card = findViewById(R.id.PrayersEvents);
        preferences = this.getSharedPreferences("My_Pref", MODE_PRIVATE);
        getMyList();

        //DATABASE



    }

    @Override
    public boolean  onCreateOptionsMenu(Menu menu){

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
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
        return  true;
    }
    private void getMyList() {
        //DATABASE
        reff= FirebaseDatabase.getInstance().getReference().child("Event");

        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {


                    // extraction db
                    String authorName = snapshot.child("authorName").getValue().toString();
                    String prayer = snapshot.child("choosePrayer").getValue().toString();
                    String description = snapshot.child("descriptionPrayer").getValue().toString();
                    String nameDead  = snapshot.child("nameDead").getValue().toString();
                    String mosqueAdress = snapshot.child("chooseMosque").getValue().toString();
                    String dateEvent = snapshot.child("date").getValue().toString();
                    String id=snapshot.getKey();
                    Log.d("Files", " données de Firebase " + authorName  + " " + prayer + " " + description + " " + nameDead +" "+mosqueAdress );


                    // remplissage Arraylist models
                    Model e = new Model();
                    e.setName(nameDead);
                    e.setAuthorName(nameDead);
                    e.setMosque(mosqueAdress);
                    e.setPrayer(prayer);
                    e.setNbParticipants("0");
                    e.setDescription(description);
                    e.setDate(dateEvent);
                    e.setId(id);
                    Log.d("prayersEvents", "id: "+ id);

                    models.add(e);

                }

                // verification ArrayList models de remplissage
                for (Model object: models) {
                 //   System.out.println(object.getName());
                    Log.d("Files", " Verification Arraylist models "+ object.getName());

                }

                card.setLayoutManager(new LinearLayoutManager(PrayersEvents.this));
                myAdapter = new MyAdapter(PrayersEvents.this, models);
                card.setAdapter(myAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
/*
    private void sortDailog() {

        String[] options = {"Ascending", "Descending"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Sort by");
        builder.setIcon(R.drawable.ic_action_sort); // add one icon

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Sort", "ascending");
                    editor.apply();
                    getMyList();
                }

                if (which == 1) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("Sort", "descending");
                    editor.apply();
                    getMyList();

                }
            }
        });

        builder.create().show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.sorting) {
            sortDailog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

 */
}
/*
        String pr_ = "Prayer : ";
        String nb_ = "People : 100";

        Model e = new Model();
        e.setName("Ilyass");
        e.setMosque("Jadid");
        e.setPrayer(pr_ + " Dohr ");
        e.setNbParticipants(nb_ );
        e.setDescription("////////////////////  ");
        models.add(e);

*/