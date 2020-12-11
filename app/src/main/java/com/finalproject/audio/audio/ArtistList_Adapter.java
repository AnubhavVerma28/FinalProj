package com.finalproject.audio.audio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.finalproject.audio.R;

import java.util.List;

public class ArtistList_Adapter extends BaseAdapter {

  private final Context context;
  private final ArtistClickListener artistClickListener;
  private final List<Artist_Model> dataList;

  public ArtistList_Adapter(List<Artist_Model> dataList, Context context, ArtistClickListener clickListener) {
    this.context = context;
    artistClickListener = clickListener;
    this.dataList = dataList;
  }

  public interface ArtistClickListener {
    void OnClick(int position);
  }

  @Override
  public int getCount() {
    return dataList.size();
  }

  @Override
  public Object getItem(int position) {
    return dataList.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      convertView = LayoutInflater.from(context).
          inflate(R.layout.artist_list_item, parent, false);
    }

    Artist_Model artistModel = (Artist_Model) getItem(position);

    TextView albumName = convertView.findViewById(R.id.album);

    albumName.setText((position+1)+". "+artistModel.getStrAlbum());
    albumName.setTag(position);

    albumName.setOnClickListener(v -> {
      int pos = (int) v.getTag();
      artistClickListener.OnClick(pos);
    });
    return convertView;
  }
}


