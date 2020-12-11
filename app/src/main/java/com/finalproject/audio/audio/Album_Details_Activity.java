package com.finalproject.audio.audio;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.audio.R;
import com.finalproject.audio.databse.DatabaseHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Album_Details_Activity extends AppCompatActivity implements
    Artist_AlbumList_Adapter.ArtistAlbumClickListener {

  private ListView listView;
  private DatabaseHelper databaseHelper;
  private ProgressBar progressBar;

  private final ArrayList<Artist_Model> allList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album_details);

    databaseHelper = new DatabaseHelper(this);

    listView = findViewById(R.id.album_list);
    progressBar = findViewById(R.id.progressBar);

    String albumId = getIntent().getStringExtra("albumId");

    new GetDataFromAPI().execute(
        "https://theaudiodb.com/api/v1/json/1/track.php?m=" + albumId);
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

          JSONArray result = jObject.optJSONArray("track");

          allList.clear();

          for (int i = 0; i < result.length(); i++) {

            JSONObject artistdata = result.optJSONObject(i);
            allList.add(new Artist_Model(1, artistdata.optString("idTrack"), artistdata.optString("strAlbum"),
                artistdata.optString("strArtist"), artistdata.optString("strTrack"),
                artistdata.optString("intTotalListeners"),
                artistdata.optString("intTotalPlays")));
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
    Artist_AlbumList_Adapter artistListAdapter =
        new Artist_AlbumList_Adapter(allList, Album_Details_Activity.this, this, "save");
    listView.setAdapter(artistListAdapter);
  }

  @Override public void OnClick(int position) {
    addData(allList.get(position));
  }

  public void addData(Artist_Model artistModel) {
    if (databaseHelper.checkIfArtistDataExist(artistModel) > 0) {
      Toast.makeText(Album_Details_Activity.this, "Already Saved", Toast.LENGTH_SHORT).show();
    } else {
      long id =
          databaseHelper.insertArtistData(artistModel.getStrTrack(), artistModel.getStrAlbum(),
              artistModel.getStrArtist(), artistModel.getTotalListeners(),
              artistModel.getTotalPlays(),artistModel.getTrackId());
      artistModel.setId(id);

      Toast.makeText(Album_Details_Activity.this, "Save Successfully", Toast.LENGTH_SHORT).show();
    }
  }
}