package com.example.strawhats;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChartsFragment extends Fragment {

    Cursor data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts,container,false);

        LineChart lineChart = view.findViewById(R.id.linechart);
        RadarChart radarChart = view.findViewById(R.id.radarchart);

        TransactionDatabaseHelper mDatabaseHelper = new TransactionDatabaseHelper(getActivity());
        data = mDatabaseHelper.getData();

        try {
            setupLineChart(lineChart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void setupLineChart(LineChart lineChart) throws ParseException {
        ArrayList<Entry> values = new ArrayList<>();

        while(data.moveToNext()){
            Float transactionDate = data.getFloat(1);
            Float amount = data.getFloat(2);
            values.add(new Entry(transactionDate, amount));
        }

        LineDataSet set = new LineDataSet(values, "Line DataSet");
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        set.setValueTextColor(Color.BLACK);
        set.setValueTextSize(18f);
        LineData d = new LineData();
        d.addDataSet(set);
        lineChart.setData(d);
        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);


    }
}
