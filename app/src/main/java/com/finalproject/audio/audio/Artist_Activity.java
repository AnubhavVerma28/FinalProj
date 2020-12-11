package com.finalproject.audio.audio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.audio.R;
import com.finalproject.audio.databse.DatabaseHelper;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.widget.Toast.LENGTH_SHORT;

public class Artist_Activity extends AppCompatActivity implements
    ArtistList_Adapter.ArtistClickListener {

  RelativeLayout toolLayout;
  Toolbar myToolbar;

  private ProgressBar progressBar;
  private EditText editSearch;
  private ListView listView;

  private final ArrayList<Artist_Model> allList = new ArrayList<>();

  SharedPreferences preferences;
  String titleWord = "";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_audio);

    progressBar = findViewById(R.id.progressBar);
    toolLayout = findViewById(R.id.main);
    listView = findViewById(R.id.album_lists);
    editSearch = findViewById(R.id.album_search);
    Button sButton = findViewById(R.id.search);

    new DatabaseHelper(this);

    editSearch.setText("");

    sButton.setOnClickListener(c -> {
      SharedPreferences.Editor editor = preferences.edit();
      progressBar.setVisibility(View.VISIBLE);
      titleWord = editSearch.getText().toString();
      editor.putString("title", titleWord);
      editor.apply();

      String titleText = null;

      try {
          titleText = URLEncoder.encode(titleWord, "utf-8");
      }catch (UnsupportedEncodingException e) {
          e.printStackTrace();
      }

      new GetDataFromAPI().execute(
          "https://www.theaudiodb.com/api/v1/json/1/searchalbum.php?s=" + titleText);
    });

    preferences = getSharedPreferences("artistLastInput", Context.MODE_PRIVATE);
    String savedtitle = preferences.getString("title", "");
    editSearch.setText(savedtitle);
  }

  @Override public void onBackPressed() {
    super.onBackPressed();
    finish();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater mInflater = getMenuInflater();
    mInflater.inflate(R.menu.menu_common, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    int itemItemId = item.getItemId();

    if (itemItemId == R.id.saved) {
      Toast.makeText(this, "Saved Artist Data", LENGTH_SHORT).show();
      Intent intent = new Intent(this, Album_Saved_Activity.class);
      startActivity(intent);
    }

    if (itemItemId == R.id.exit) {
      showSnackbar(myToolbar);
    }
    return super.onOptionsItemSelected(item);
  }

  private class GetDataFromAPI extends AsyncTask<String, Integer, String> {

    @Override
    protected void onProgressUpdate(Integer... values) {
      super.onProgressUpdate(values);
      progressBar.setProgress(values[0]);
    }

    @Override
    protected String doInBackground(String... strings) {

      HttpURLConnection urlConnection = null;
      String url = strings[0];
      String dataline = null;
      publishProgress(25);

      try {
        URL wordURL = new URL(url);
        urlConnection = (HttpURLConnection) wordURL.openConnection();
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestMethod("GET");
        urlConnection.setDoInput(true);
        urlConnection.connect();

        BufferedReader reader = null;
        publishProgress(25);
        reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
            "UTF-8"), 8);
        dataline = reader.readLine();
        publishProgress(50);
        JSONObject jObject = new JSONObject(dataline);

        JSONArray result = jObject.optJSONArray("album");

        allList.clear();

        for (int i = 0; i < result.length(); i++) {

          JSONObject data = result.optJSONObject(i);
          allList.add(new Artist_Model(data.optString("idAlbum"), data.optString("strAlbum"),
              data.optString("strArtist")));
        }
        publishProgress(75);
      } catch (Exception e) {
        e.printStackTrace();
      }
      return "Finished";
    }

    @Override
    protected void onPostExecute(String s) {
      progressBar.setProgress(100);
      progressBar.setVisibility(View.INVISIBLE);
      setAdapter();
    }
  }

  private void setAdapter() {
    ArtistList_Adapter artistListAdapter = new ArtistList_Adapter(allList, Artist_Activity.this, this);
    listView.setAdapter(artistListAdapter);
  }

  @Override public void OnClick(int position) {
    Intent intent = new Intent(Artist_Activity.this, Album_Details_Activity.class);
    intent.putExtra("albumId", allList.get(position).getAlbumId());
    startActivity(intent);
  }

  public void showSnackbar(Toolbar view) {
    final Snackbar snackbar = Snackbar.make(listView, "Want to exit?", Snackbar.LENGTH_LONG);
    snackbar.setAction("Exit", e -> finish());
    snackbar.show();
  }

}