package com.finalproject.audio.audio;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.finalproject.audio.R;
import com.finalproject.audio.databse.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class Album_Saved_Activity extends AppCompatActivity implements
    Artist_AlbumList_Adapter.ArtistAlbumClickListener {

  private ListView listView;
  private Artist_AlbumList_Adapter artistAlbumListAdapter;
  private DatabaseHelper databaseHelper;

  private List<Artist_Model> allList = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_album_details);

    databaseHelper = new DatabaseHelper(this);

    listView = findViewById(R.id.album_list);

    allList = databaseHelper.getAllArtistData();
    setAdapter();
  }

  private void setAdapter() {
    artistAlbumListAdapter =
        new Artist_AlbumList_Adapter(allList, Album_Saved_Activity.this, this, "delete");
    listView.setAdapter(artistAlbumListAdapter);
  }

  @Override public void OnClick(int position) {
    deleteData(allList.get(position), position);
  }

  public void deleteData(Artist_Model artistModel, int position) {

    databaseHelper.deleteArtistEntry(artistModel.getId());
    allList.remove(position);
    artistAlbumListAdapter.notifyDataSetChanged();

    if (allList.size() == 0) {
      Toast.makeText(this, "Favorite list is empty!!!", Toast.LENGTH_SHORT).show();
    }
  }
}