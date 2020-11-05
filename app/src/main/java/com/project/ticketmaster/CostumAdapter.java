package com.project.ticketmaster;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CostumAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    ArrayList<String> event;
    ArrayList<String> amountType;
    ArrayList<String> dec;
    Context context;

    public CostumAdapter(Context incomeFragment, ArrayList<String> event) {
        context = incomeFragment;
        this.event = event;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return event.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.incomeviewlist, null);
        Holder holder = new Holder();
        holder.tDec = (TextView) convertView.findViewById(R.id.txtLstDec);
        holder.tDec.setText(event.get(position));

        holder.tDec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent abc=new Intent(context,DetailsActivity.class);
                abc.putExtra("Name",holder.tDec.getText());
                abc.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(abc);
                Toast.makeText(context,holder.tDec.getText(),Toast.LENGTH_LONG).show();
            }
        });

        return convertView;
    }

    class Holder {
        TextView tAmount, tAmountType, tDec;

    }
}
