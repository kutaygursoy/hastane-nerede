package com.kutaygursoy.hastanenerede;


import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class NearestHospital extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearesthospital);
        ListView listView = findViewById(R.id.listView);
        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(getApplicationContext(), R.layout.item_layout);

        Parcelable state = listView.onSaveInstanceState();
        listView.setAdapter(itemArrayAdapter);
        listView.onRestoreInstanceState(state);

        InputStream inputStream = getResources().openRawResource(R.raw.database);
        CSVFile csvFile = new CSVFile(inputStream);
        List<String[]> scoreList = csvFile.read();

        Collections.sort(scoreList, new Comparator<String[]>() {
            public int compare(String[] strings, String[] otherStrings) {
                double first = Double.parseDouble(strings[3].replace(',', '.'));
                double second = Double.parseDouble(otherStrings[3].replace(',', '.'));

                if (first >= second) {
                    return 1;
                }
                return -1;
            }
        });

        for (String[] scoreData : scoreList) {
            itemArrayAdapter.add(scoreData);
        }

    }


}
