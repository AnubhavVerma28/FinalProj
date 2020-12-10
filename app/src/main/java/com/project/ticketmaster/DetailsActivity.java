package com.project.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.project.ticketmaster.DataBase.DBAdapter;
import com.project.ticketmaster.DataModel.userdatamodel;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {
TextView txtEventName,txtLocation,txtStartingDate,txtContact,txtPrice;
    DBAdapter obj;
    ArrayList<userdatamodel> dh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dh=new ArrayList<>();

        txtContact=findViewById(R.id.txtContact);
        txtEventName=findViewById(R.id.txtEventName);
        txtLocation=findViewById(R.id.txtLocation);
        txtPrice=findViewById(R.id.txtPrice);
        txtStartingDate=findViewById(R.id.txtDate);
        Intent intent = getIntent();
        String eve=intent.getStringExtra("Name");

        obj = DBAdapter.getDBAdapter(getApplicationContext());
        if (obj.checkDatabase() == false)
            obj.createDatabase(getApplicationContext());
        obj.openDatabase();

        dh=obj.getData(eve);

        txtStartingDate.setText(dh.get(0).getDate());
        txtPrice.setText(dh.get(0).getPrice());
        txtLocation.setText(dh.get(0).getLocation());
        txtEventName.setText(dh.get(0).getEvent());
        txtContact.setText(dh.get(0).getContact());
    }
}