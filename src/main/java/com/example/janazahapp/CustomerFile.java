package com.example.janazahapp;

import android.util.Log;
import android.widget.Filter;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomerFile extends Filter {
    ArrayList<Model> filterList;
    MyAdapter adapter;

    public CustomerFile(ArrayList<Model> filterList, MyAdapter adapter)
    {
        this.filterList = filterList;
        this.adapter = adapter;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();
        if (constraint != null && constraint.length() > 0) {

            constraint = constraint.toString().toUpperCase();
            ArrayList<Model> filterModels = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {

                //   Log.d("Files", " mosque recherchée : " +filterList.get(i).getMosque() );
                if (filterList.get(i).getMosque().toUpperCase().contains(constraint))
                {
                    Log.d("Files", " mosque recherchée : " +filterList.get(i).getMosque() );
                    filterModels.add(filterList.get(i));
                }
            }

            results.count = filterModels.size();
            results.values = filterModels;


        }

        else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results)
    {
        adapter.models = (ArrayList<Model>) results.values;
        adapter.notifyDataSetChanged();
    }
}
