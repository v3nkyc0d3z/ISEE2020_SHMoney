package com.example.strawhats;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.data.Iterator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChartsFragment extends Fragment {

    ArrayList<TransactionList> listData = new ArrayList<>();
    TransactionDatabaseHelper mtransactionDatabaseHelper;
    GraphView graphView;
    LineGraphSeries<DataPoint> series;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts,container,false);

//        LineChart lineChart = view.findViewById(R.id.linechart);
//        RadarChart radarChart = view.findViewById(R.id.radarchart);
        graphView =(GraphView)view.findViewById(R.id.lineGraph);
        mtransactionDatabaseHelper = new TransactionDatabaseHelper(getActivity());
        populateList();
        try {
            series = new LineGraphSeries<>(getDataPoints());
            graphView.addSeries(series);

            graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter(){
                @Override
                public String formatLabel(double value, boolean isValueX) {
                    if (isValueX){
                        return sdf.format(new Date((long)value));
                    } else {
                        return super.formatLabel(value, isValueX);
                    }
                }
            });

        } catch (ParseException e) {
            e.printStackTrace();
        }
//        try {
//            setupLineChart(lineChart);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return view;
    }

    public DataPoint[] getDataPoints() throws ParseException {
        HashMap<Date,Float> lineDataMap = new HashMap<Date,Float>();
        for (int i = 0; i<listData.size();i++){
            TransactionList transaction = listData.get(i);
            String sDate = transaction.getDate();
            Date date = new SimpleDateFormat("yyyy/dd/MM").parse(sDate);
            Float amount = Float.parseFloat(transaction.getAmount());
            if (lineDataMap.containsKey(transaction.getDate())){
                lineDataMap.put(date,(lineDataMap.get(date)+amount));
            }else{
                lineDataMap.put(date,amount);
            }
        }
        DataPoint[] dp = new DataPoint[lineDataMap.size()];
        Map<Date,Float> sortedlineDataMap = new TreeMap<Date,Float>(lineDataMap);
        List<Date> dateList = new ArrayList<Date>(sortedlineDataMap.keySet());
        for (int i = 0;i<dateList.size();i++){
            Date currDate = dateList.get(i);
            Float currAmount = lineDataMap.get(currDate);
            dp[i] = new DataPoint(currDate,currAmount);
        }
        return dp;
    }

    public void populateList(){
        Cursor data = mtransactionDatabaseHelper.getData();
        String content = "";
        String action;
        String amt;
        while(data.moveToNext()){
            int id = data.getInt(0);
            String date = data.getString(1);
            Float amount = data.getFloat(2);
            amt = Float.toString(amount);
            String mode = data.getString(3);
            String category = data.getString(4);
            String comment = data.getString(5);
            String type = data.getString(6);
            if (type.equals("expense")){
                action = "you spent";
            } else {  action = "you got";}
            TransactionList transaction = new TransactionList(id,date,amt,mode,category,comment,type,action);
            listData.add(transaction);
        }
    }
}
