package com.project.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.project.ticketmaster.DataBase.DBAdapter;
import com.project.ticketmaster.DataModel.userdatamodel;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String status;
    DBAdapter obj;
    ProgressDialog pDialog;
    ArrayList<userdatamodel> dh = new ArrayList<userdatamodel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InsertDataBaseData insertDataBaseData=new InsertDataBaseData();

        ArrayList<String> eventName = new ArrayList<>();
        ArrayList<String> eventLocation = new ArrayList<>();
        ArrayList<String> eventDate = new ArrayList<>();
        ArrayList<String> eventContact = new ArrayList<>();
        ArrayList<String> eventPrice = new ArrayList<>();
        ArrayList<String> eventCity = new ArrayList<>();

        eventName=insertDataBaseData.eventName;
        eventLocation=insertDataBaseData.eventLocation;
        eventDate=insertDataBaseData.eventDate;
        eventContact=insertDataBaseData.eventContact;
        eventPrice=insertDataBaseData.eventPrice;
        eventCity=insertDataBaseData.eventCity;

        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);


        SharedPreferences sharedPreferences = getSharedPreferences("app", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        status = sharedPreferences.getString("status", "");
        if (status.equals("hello")) {
        } else {
            obj = DBAdapter.getDBAdapter(getApplicationContext());
            if (obj.checkDatabase() == false)
                obj.createDatabase(getApplicationContext());
            obj.openDatabase();

            for (int i = 0; i < eventCity.size(); i++) {
                obj.insertEvent(eventName.get(i), eventLocation.get(i), eventDate.get(i), eventContact.get(i), eventPrice.get(i), eventCity.get(i));
            }
            editor.putString("status", "hello");
            editor.apply();
        }

pDialog.show();
        Thread spl=new Thread(){
            public void run(){
                try{
                    synchronized (this) {
                        wait(5000);
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }
                Intent inte=new Intent(getApplicationContext(),MainActivity2.class);
                startActivity(inte);
                finish();
                pDialog.cancel();
            }
        };
        spl.start();
    }
}