package com.project.ticketmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.project.ticketmaster.DataBase.DBAdapter;
import com.project.ticketmaster.DataModel.userdatamodel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    EditText txtSearchName;
    Button btnSearch;
    ListView lstResult;
    DBAdapter obj;
    ArrayList<userdatamodel> dh;
    ArrayList<String> data;
    ArrayList<String> daArr;
    String status;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtSearchName = findViewById(R.id.txtCityName);
        btnSearch = findViewById(R.id.btnSearch);
        lstResult = findViewById(R.id.list_item);



        SharedPreferences sharedPreferences = getSharedPreferences("app", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        status = sharedPreferences.getString("status", "");
        if (status.equals("")) {
        } else {
            obj = DBAdapter.getDBAdapter(getApplicationContext());
            if (obj.checkDatabase() == false)
                obj.createDatabase(getApplicationContext());
            obj.openDatabase();
            dh = new ArrayList<>();
            dh = obj.getEvent(status, "Online");

            daArr = new ArrayList<>();
            for (int k = 0; k < dh.size(); k++) {
                daArr.add(dh.get(k).getEvent());
            }
            lstResult.setAdapter(new CostumAdapter(getApplicationContext(), daArr));
        }



        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSearchName.getText().toString().length()>0) {
                    obj = DBAdapter.getDBAdapter(getApplicationContext());
                    if (obj.checkDatabase() == false)
                        obj.createDatabase(getApplicationContext());
                    obj.openDatabase();
                    dh = new ArrayList<>();
                    dh = obj.getEvent(txtSearchName.getText().toString().trim(), "Online");

                    daArr = new ArrayList<>();
                    for (int k = 0; k < dh.size(); k++) {
                        daArr.add(dh.get(k).getEvent());
                    }
                    lstResult.setAdapter(new CostumAdapter(getApplicationContext(), daArr));
                    editor.putString("status", txtSearchName.getText().toString().trim());
                    editor.apply();
                }else{
                    Toast.makeText(getApplicationContext(),"Please Enter Text For Search",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}