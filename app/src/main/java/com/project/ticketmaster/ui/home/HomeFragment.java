package com.project.ticketmaster.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.project.ticketmaster.CostumAdapter;
import com.project.ticketmaster.DataBase.DBAdapter;
import com.project.ticketmaster.DataModel.userdatamodel;
import com.project.ticketmaster.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    EditText txtSearchName;
    Button btnSearch;
    ListView lstResult;
    DBAdapter obj;
    ArrayList<userdatamodel> dh;
    ArrayList<String> data;
    ArrayList<String> daArr;
    String status;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        txtSearchName=root.findViewById(R.id.txtCityName);
        btnSearch = root.findViewById(R.id.btnSearch);
        lstResult = root.findViewById(R.id.list_item);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("app", Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        status = sharedPreferences.getString("status", "");
        if (status.equals("")) {
        } else {
            obj = DBAdapter.getDBAdapter(getActivity().getApplicationContext());
            if (obj.checkDatabase() == false)
                obj.createDatabase(getActivity().getApplicationContext());
            obj.openDatabase();
            dh = new ArrayList<>();
            dh = obj.getEvent(status, "Online");

            daArr = new ArrayList<>();
            for (int k = 0; k < dh.size(); k++) {
                daArr.add(dh.get(k).getEvent());
            }
            lstResult.setAdapter(new CostumAdapter(getActivity().getApplicationContext(), daArr));
        }


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(txtSearchName.getText().toString().length()>0) {
                    obj = DBAdapter.getDBAdapter(getActivity().getApplicationContext());
                    if (obj.checkDatabase() == false)
                        obj.createDatabase(getActivity().getApplicationContext());
                    obj.openDatabase();
                    dh = new ArrayList<>();
                    dh = obj.getEvent(txtSearchName.getText().toString().trim(), "Online");

                    daArr = new ArrayList<>();
                    for (int k = 0; k < dh.size(); k++) {
                        daArr.add(dh.get(k).getEvent());
                    }
                    lstResult.setAdapter(new CostumAdapter(getActivity().getApplicationContext(), daArr));
                    editor.putString("status", txtSearchName.getText().toString().trim());
                    editor.apply();
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please Enter Text For Search",Toast.LENGTH_LONG).show();
                }
            }
        });


        return root;
    }
}