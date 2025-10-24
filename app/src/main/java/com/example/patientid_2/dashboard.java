package com.example.patientid_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.GridView;

import com.example.patientid_2.model.ItemObject;

import java.util.ArrayList;
import java.util.List;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        List<ItemObject> allItems = getAllItemObject();

        CustomAdapter customAdapter = new CustomAdapter(dashboard.this, allItems);
        gridview.setAdapter(customAdapter);
    }

    private List<ItemObject> getAllItemObject() {
        String master="",addcontact="";

        List<ItemObject> items = new ArrayList<>();
        items.add(new ItemObject(R.drawable.patient, "Patient master", "Patient master"));
        items.add(new ItemObject(R.drawable.examination, "Check Up", "Check Up"));
        items.add(new ItemObject(R.drawable.medicalhistory, "History", "History"));

        return items;
    }
}