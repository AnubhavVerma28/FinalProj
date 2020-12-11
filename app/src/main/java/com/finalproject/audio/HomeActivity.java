package com.finalproject.audio;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.finalproject.audio.audio.Artist_Activity;

public class HomeActivity extends AppCompatActivity {

  Button btnAudio;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    btnAudio = findViewById(R.id.audio_app_button);

    btnAudio.setOnClickListener(n -> {
      Intent intent = new Intent(HomeActivity.this, Artist_Activity.class);
      startActivity(intent);
    });
  }
}
