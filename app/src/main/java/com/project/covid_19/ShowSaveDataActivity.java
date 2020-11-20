package com.project.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.project.covid_19.DataBase.DBAdapter;
import com.project.covid_19.DataModel.userdatamodel;

import java.util.ArrayList;

public class ShowSaveDataActivity extends AppCompatActivity {
    DBAdapter obj;
    ArrayList<userdatamodel> dh;
    ListView listView;
    Button btnDelete;
    ArrayList<String> countryArr;
    ArrayList<String> statesArr;
    ArrayList<Integer> casesArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_save_data);
        listView = findViewById(R.id.lstSaveData);
        btnDelete = findViewById(R.id.btnDelete);
        countryArr=new ArrayList<>();
        statesArr=new ArrayList<>();
        casesArr=new ArrayList<>();

        dh = new ArrayList<>();
        obj = DBAdapter.getDBAdapter(getApplicationContext());
        if (obj.checkDatabase() == false)
            obj.createDatabase(getApplicationContext());
        obj.openDatabase();
        dh=obj.getData();

        for(int i=0;i<dh.size();i++){
            countryArr.add(dh.get(i).getCountry());
            statesArr.add(dh.get(i).getStates());
            casesArr.add(dh.get(i).getCases());
        }
        listView.setAdapter(new CostumAdapter(getApplicationContext(),countryArr,statesArr,casesArr));

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj = DBAdapter.getDBAdapter(getApplicationContext());
                if (obj.checkDatabase() == false)
                    obj.createDatabase(getApplicationContext());
                obj.openDatabase();
                obj.DeleteData();

                Toast.makeText(getApplicationContext(),"Data Delete Successfully",Toast.LENGTH_LONG).show();
                finish();
            }
        });
    }
}