package com.kutaygursoy.hastanenerede;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class CSVFile {

    InputStream inputStream;

    public CSVFile(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<String[]> read() {
        List<String[]> resultList = new ArrayList<String[]>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] row = csvLine.split(";");

                double newl1 = Double.parseDouble(row[0]);
                double newl2 = Double.parseDouble(row[1]);


                double mesafe2 = MainActivity.distance(newl1, MainActivity.Global.ivar1, newl2, MainActivity.Global.ivar2, 0, 0);

                String[] row2 = new String[4];
                row2[0] = row[0];
                row2[1] = row[1];
                row2[2] = row[2];
                row2[3] = String.format("%.3f", mesafe2 / 1000);

                resultList.add(row2);
            }
        } catch (IOException ex) {
            throw new RuntimeException("CSV Dosya okuma hatası: " + ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException("Yazım Hatası: " + e);
            }
        }

        return resultList;
    }
}