package com.project.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.project.covid_19.DataBase.DBAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchDataActivity extends AppCompatActivity {
    ListView lstData;
    Button btnSave;
    String country, from, to;
    ProgressDialog pDialog;
    ArrayList<String> countryArr;
    ArrayList<String> provinceArr;
    ArrayList<Integer> caseArr;
    DBAdapter obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);
        lstData = findViewById(R.id.lstShowData);
        btnSave = findViewById(R.id.btnDataSave);
        Intent intent = getIntent();
        country = intent.getStringExtra("Country");
        from = intent.getStringExtra("From");
        to = intent.getStringExtra("To");

        Log.d("To",to);
        Log.d("Form",from);

        pDialog = new ProgressDialog(SearchDataActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(false);

        countryArr=new ArrayList<>();
        provinceArr=new ArrayList<>();
        caseArr=new ArrayList<>();

        new Details().execute();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                obj = DBAdapter.getDBAdapter(getApplicationContext());
                if (obj.checkDatabase() == false)
                    obj.createDatabase(getApplicationContext());
                obj.openDatabase();
                pDialog.show();
                for(int i=0;i<countryArr.size();i++){
                    obj.insertAData(caseArr.get(i),countryArr.get(i),provinceArr.get(i));
                }
                pDialog.dismiss();
                Toast.makeText(getApplicationContext(),"Data Save Successfully",Toast.LENGTH_LONG).show();
            }
        });


    }

    public class Details extends AsyncTask<Object, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.show();
        }

        @Override
        protected ArrayList<String> doInBackground(Object... objects) {
            AndroidNetworking.get(ContentFiles.url +country+ ContentFiles.securl+"?from="+from+"&to="+to)
                    .setPriority(Priority.HIGH)
                    .build().getAsJSONArray(new JSONArrayRequestListener() {
                @Override
                public void onResponse(JSONArray response) {
                    try {
                        if (pDialog.isShowing())
                            pDialog.dismiss();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject ob = response.getJSONObject(i);
                            countryArr.add(ob.getString("Country"));
                            provinceArr.add(ob.getString("Province"));
                            caseArr.add(ob.getInt("Cases"));
                        }
                    } catch (Exception e) {
                        Log.e("Error", e + "");
                    }
                    System.out.println(response);
                    lstData.setAdapter(new CostumAdapter(getApplicationContext(),countryArr,provinceArr,caseArr));
                }

                @Override
                public void onError(ANError anError) {
                    pDialog.dismiss();
                    Log.e("Error", anError + "");
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
        }
    }
}