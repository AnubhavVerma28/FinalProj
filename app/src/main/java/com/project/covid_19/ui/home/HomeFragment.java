package com.project.covid_19.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.covid_19.R;
import com.project.covid_19.SearchDataActivity;
import com.project.covid_19.ShowSaveDataActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    EditText txtCountry;
    CalendarView calFrom, calTo;
    Button btnSearch, btnShow;
    final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static String From, To;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        txtCountry = root.findViewById(R.id.txtCountryName);
        calFrom = root.findViewById(R.id.calFrom);
        calTo = root.findViewById(R.id.calTo);
        btnSearch = root.findViewById(R.id.btnSearch);
        btnShow = root.findViewById(R.id.btnShow);

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
                    Intent intent = new Intent(getActivity().getApplicationContext(), SearchDataActivity.class);
                    intent.putExtra("Country", txtCountry.getText().toString().toUpperCase().trim());
                    intent.putExtra("From", From);
                    intent.putExtra("To", To);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Enter Country Name", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(), ShowSaveDataActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}