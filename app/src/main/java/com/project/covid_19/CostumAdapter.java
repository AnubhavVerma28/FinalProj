package com.project.covid_19;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class CostumAdapter extends BaseAdapter {

    private static LayoutInflater inflater = null;

    ArrayList<String> country;
    ArrayList<String> state;
    ArrayList<Integer> caseArr;
    Context context;

    public CostumAdapter(Context incomeFragment, ArrayList<String> country, ArrayList<String> state, ArrayList<Integer> caseArr) {
        context = incomeFragment;
        this.country = country;
        this.state=state;
        this.caseArr=caseArr;
        inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return country.size();
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
        holder.txtCountry = (TextView) convertView.findViewById(R.id.txtCountry);
        holder.txtState=convertView.findViewById(R.id.txtState);
        holder.txtCase=convertView.findViewById(R.id.txtCase);
        holder.txtCountry.setText(country.get(position));
        holder.txtCase.setText(caseArr.get(position)+"");
        holder.txtState.setText(state.get(position));

        return convertView;
    }

    class Holder {
        TextView txtCountry, txtCase,txtState;

    }
}
