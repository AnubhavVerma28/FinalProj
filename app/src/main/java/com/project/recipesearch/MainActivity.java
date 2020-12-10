package com.project.recipesearch;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.android.finalproject.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rp_activity_main);
        Thread spl = new Thread() {
            public void run() {
                try {
                    synchronized (this) {
                        wait(5000);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Intent inte = new Intent(getApplicationContext(), MainActivity2.class);
                startActivity(inte);
                finish();
            }
        };
        spl.start();
    }
}