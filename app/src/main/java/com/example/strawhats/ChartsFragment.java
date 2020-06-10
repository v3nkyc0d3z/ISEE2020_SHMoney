package com.example.strawhats;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    Integer dateRange = 30;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts,container,false);
        graphView =(GraphView)view.findViewById(R.id.lineGraph);
        mtransactionDatabaseHelper = new TransactionDatabaseHelper(getActivity());


        MultiStateToggleButton button = (MultiStateToggleButton) view.findViewById(R.id.mstb_multi_id);
        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                if (position==0){
                    dateRange = 30;
                } else if (position == 1){
                    dateRange = 60;
                } else {
                    dateRange = 90;
                }
            }
        });

        populateList();
        try {
            series = new LineGraphSeries<>(getDataPoints());
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setDrawBackground(true);
            series.setColor(Color.parseColor("#0e9aa7"));
            series.setThickness(5);

            graphView.addSeries(series);

            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getGridLabelRenderer().setNumHorizontalLabels(listData.size()+1);
            graphView.getGridLabelRenderer().setHumanRounding(false);
            graphView.getGridLabelRenderer().setGridStyle( GridLabelRenderer.GridStyle.NONE );
            graphView.getGridLabelRenderer().setHighlightZeroLines( true );
            graphView.getGridLabelRenderer().setTextSize(25);
            graphView.getGridLabelRenderer().setLabelsSpace(20);


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

//        LineChart lineChart = view.findViewById(R.id.linechart);
//        RadarChart radarChart = view.findViewById(R.id.radarchart);

//########################################################################################################
//        once the activity is initiated an arraylist is loaded with DB Transaction Objects
        //populateList();
//        The getDataPoints method does most of the Data Manipulation part for the plot

//        try {
//            setupLineChart(lineChart);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public DataPoint[] getDataPoints() throws ParseException {
/**There is no order on how the data is stored in the DB
 * The getData method of DBHelper is a 'select *' query that returns all the available data in the DB
 * Dates are stored as Text in the DB so they are parsed as a Date type from string Data
 * A HashMap is like a Dictionary type in Python that has a key value pair
 * Here Dates are keys and the Net amount spent on that day is the value
 * I have not implemented expenses, this piece of code below just add the amount of transaction and stores as a value regardless of "expense " of "income"**/
        HashMap<Date,Float> lineDataMap = new HashMap<Date,Float>();
        for (int i = 0; i<listData.size();i++){
            TransactionList transaction = listData.get(i);
            String sDate = transaction.getDate();
            Date date = new SimpleDateFormat("yyyy/dd/MM").parse(sDate);

            if (date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().isAfter(LocalDate.now().minusDays(dateRange))){
                Float amount = Float.parseFloat(transaction.getAmount());
                String type = transaction.getType();
                if (type.equals("expense")){
                    amount = amount*-1;
                };
                if (lineDataMap.containsKey(transaction.getDate())){
                    if (transaction.getType().equals("Income")) {
                        lineDataMap.put(date, (lineDataMap.get(date) + amount));
                    } else{
                        lineDataMap.put(date, (lineDataMap.get(date) - amount));
                    }
                }else{
                    if(transaction.getType().equals("Income")) {
                        lineDataMap.put(date, amount);
                    } else{
                        lineDataMap.put(date, amount * -1);
                    }
                }
            };

        }
/**      GraphView Graphs have a special Datatype called Datapoint array
 *      It is an array of size (n,2) n-data and 2 - axis
 *      The downfall here is the labels i.e, the dates has to be in ascending order before passing it into graph object
 *      TreeMap takes care of sorting the dates**/
        DataPoint[] dp = new DataPoint[lineDataMap.size()];
        Map<Date,Float> sortedlineDataMap = new TreeMap<Date,Float>(lineDataMap);
        List<Date> dateList = new ArrayList<Date>(sortedlineDataMap.keySet());
        Float netAmount = 0f;
        for (int i = 0;i<dateList.size();i++){
            Date currDate = dateList.get(i);
            Float currAmount = lineDataMap.get(currDate);
            netAmount = netAmount + currAmount;
            dp[i] = new DataPoint(currDate,netAmount);
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
