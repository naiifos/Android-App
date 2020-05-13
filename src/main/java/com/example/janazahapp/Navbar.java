package com.example.janazahapp;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Navbar extends Fragment {
    private ActionBar toolbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_navbar,container,true);

        BottomNavigationView navigationView = (BottomNavigationView) rootView.findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        return rootView;
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.createEvent:
                    Log.d("DEBUG", "vous etes dans create event");
                    Intent intoCreate=new Intent(getActivity() , CreateEvent.class);
                    startActivity(intoCreate);
                    return true;
                case R.id.prayers:
                    Intent intoPrayers=new Intent(getActivity() , PrayersEvents.class);
                    startActivity(intoPrayers);
                    return true;
                case R.id.profile:
                    Intent intoProfile=new Intent(getActivity() , Profile.class);
                    startActivity(intoProfile);
                    return true;
            }
            return false;
        }
    };
}
