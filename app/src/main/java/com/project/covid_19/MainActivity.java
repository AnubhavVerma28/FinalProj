package com.project.covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Thread spl = new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        wait(3000);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Intent inte = new Intent(getApplicationContext(), NavCovid.class);
                startActivity(inte);
                finish();
            }
        };
        spl.start();
    }
}