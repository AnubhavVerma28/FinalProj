package com.project.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SearchActivity extends AppCompatActivity {
    EditText txtCountry;
    CalendarView calFrom, calTo;
    Button btnSearch, btnShow;
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static String From, To;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        txtCountry = findViewById(R.id.txtCountryName);
        calFrom = findViewById(R.id.calFrom);
        calTo = findViewById(R.id.calTo);
        btnSearch = findViewById(R.id.btnSearch);
        btnShow = findViewById(R.id.btnShow);

        calFrom.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {
                month=month+1;
                From = year + "-" + month + "-" + date;
            }
        });

        calTo.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month, int date) {
                month=month+1;
                To = year + "-" + month + "-" + date;
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtCountry.getText().toString().length() > 0) {
                    Intent intent = new Intent(getApplicationContext(), SearchDataActivity.class);
                    intent.putExtra("Country", txtCountry.getText().toString().toUpperCase().trim());
                    intent.putExtra("From", From);
                    intent.putExtra("To", To);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Country Name", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ShowSaveDataActivity.class);
                startActivity(intent);
            }
        });

    }
}