package com.kutaygursoy.hastanenerede;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class ItemArrayAdapter extends ArrayAdapter<String[]> {

    private static final String TAG = "MyActivity";

    private final List<String[]> scoreList = new ArrayList<>();


    static class ItemViewHolder {
        TextView name;
        TextView score;
    }

    public ItemArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    public void add(String[] object) {
        scoreList.add(object);
        super.add(object);
    }

    @Override
    public int getCount() {
        return this.scoreList.size();
    }

    @Override
    public String[] getItem(int index) {
        return this.scoreList.get(index);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        ItemViewHolder viewHolder;
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.item_layout, parent, false);

            viewHolder = new ItemViewHolder();

            viewHolder.name = row.findViewById(R.id.name);
            viewHolder.score = row.findViewById(R.id.score);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ItemViewHolder) row.getTag();
        }

        String[] stat = getItem(position);

        double newl1 = Double.parseDouble(stat[0]);
        double newl2 = Double.parseDouble(stat[1]);
        String newl3 = stat[2];

        row.setOnClickListener(v -> {
            Log.i(TAG, "LAT: " + newl1);
            Log.i(TAG, "LON: " + newl2);
            String uri = "http://maps.google.com/maps?mode=d&saddr=" + MainActivity.Global.ivar1 + "," + MainActivity.Global.ivar2 + "(" + "Buradan" + ")&daddr=" + newl1 + "," + newl2 + " (" + newl3 + ")";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getContext().startActivity(intent);
        });

        viewHolder.name.setText(stat[2]);
        viewHolder.score.setText(stat[3]);

        return row;


    }

}