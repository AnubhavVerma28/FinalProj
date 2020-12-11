package com.finalproject.audio.audio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.finalproject.audio.R;

import java.util.List;

public class Artist_AlbumList_Adapter extends BaseAdapter {

  private final String type;
  private final Context context;
  private final ArtistAlbumClickListener artistClickListener;
  private final List<Artist_Model> dataList;

  public Artist_AlbumList_Adapter(List<Artist_Model> dataList, Context context, ArtistAlbumClickListener clickListener, String type) {
    this.dataList = dataList;
    this.context = context;
    artistClickListener = clickListener;
    this.type = type;
  }

  public interface ArtistAlbumClickListener {
    void OnClick(int position);
  }

  @Override
  public int getCount() {
    return dataList.size();
  }

  @Override
  public Object getItem(int position){
    return dataList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.artist_album_list_item, parent, false);
    }

    Artist_Model artistModel = (Artist_Model) getItem(position);

    LinearLayout allData = convertView.findViewById(R.id.all_data);
    Button btnSave = convertView.findViewById(R.id.save_button);
    TextView albumName = convertView.findViewById(R.id.album_name);
    TextView songName = convertView.findViewById(R.id.song_name);

    if (type.equals("delete")) {
      btnSave.setText("Delete");
    } else {
      btnSave.setText("Save");
    }

    albumName.setText(String.format("Artist Name: %s", artistModel.getStrAlbum()));
    songName.setText(String.format("Track Name: %s", artistModel.getStrTrack()));

    allData.setTag(position);

    allData.setOnClickListener(v -> {

      int pos = (int) v.getTag();

      Intent i = new Intent(Intent.ACTION_VIEW);
      i.setData(Uri.parse("http://www.google.com/search?q=" + dataList.get(pos).getStrArtist()));
      context.startActivity(i);
    });

    btnSave.setTag(position);

    btnSave.setOnClickListener(v -> {
     int pos = (int) v.getTag();
     artistClickListener.OnClick(pos);
    });
    return convertView;
  }
}


