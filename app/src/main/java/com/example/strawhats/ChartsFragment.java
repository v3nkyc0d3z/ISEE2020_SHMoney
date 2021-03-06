package com.example.strawhats;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.tooltip.Tooltip;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    Cursor data;
    Float Net = 0f;
    Integer dateRange = 60;
    private MultiStateToggleButton button;
    ArrayList<CurrencyItem> mCurrencyList;
    UserDatabaseHelper userDatabaseHelper;
    CurrencyItem DefaultCurrency;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts,container,false);
//---------------------------------Balance Over Time----------------------------------------------------------
//------------------------------------------------------------------------------------------------------------
        userDatabaseHelper = new UserDatabaseHelper(getActivity());
        initCurrencyList();
        TransactionDatabaseHelper mDatabaseHelper = new TransactionDatabaseHelper(getActivity());
        data = mDatabaseHelper.getData();
        PieChart pieChart = view.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        setupPieChart(pieChart);
        button = (MultiStateToggleButton) view.findViewById(R.id.mstb_multi_id);

        graphView =(GraphView)view.findViewById(R.id.lineGraph);
        mtransactionDatabaseHelper = new TransactionDatabaseHelper(getActivity());
        init();
//---------------------------------Threshold progress--------------------------------------------------------
//-----------------------------------------------------------------------------------------------------------
        return view;
    }
    private void create_line_graph(){
        populateList();
//        The getDataPoints method does most of the Data Manipulation part for the plot
        try {
            graphView.removeAllSeries();
            series = new LineGraphSeries<>(getDataPoints());
            series.setDrawDataPoints(true);
            series.setDataPointsRadius(10);
            series.setDrawBackground(true);
            series.setColor(Color.parseColor("#0e9aa7"));
            series.setThickness(5);

            graphView.addSeries(series);

            graphView.getViewport().setMinX(series.getLowestValueX());
            graphView.getViewport().setMaxX(series.getHighestValueX());
            graphView.getViewport().setMinY(series.getLowestValueY());
            graphView.getViewport().setMaxY(series.getHighestValueY());
            graphView.getViewport().setXAxisBoundsManual(true);
            graphView.getViewport().setYAxisBoundsManual(true);

            graphView.getGridLabelRenderer().setNumHorizontalLabels(5);
            graphView.getGridLabelRenderer().setNumVerticalLabels(3);
            graphView.getGridLabelRenderer().setHumanRounding(true);
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
    }
    private void init(){
        //declare the xySeries Object

        button.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.d("Chart", "Position: " + position);
                if (position==0){
                    dateRange = 30;
                } else if (position==1){
                    dateRange = 60;
                } else if (position==2){
                    dateRange = 90;
                }
                else dateRange=10000;
                Log.d("Chart", "DateRange: " + dateRange);
                init();
            }
        });

        create_line_graph();

    }

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
            Float amount = Float.parseFloat(transaction.getAmount());
            Calendar calendar = Calendar.getInstance(); // this would default to now
            calendar.add(Calendar.DAY_OF_MONTH, -dateRange);

            if (date.after(calendar.getTime())){
//                String type = transaction.getType();
//                if (type.equals("Expense")){
//                    amount = amount*-1;
//                }
                if (lineDataMap.containsKey(date)){
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
            }
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
            amount = amount*DefaultCurrency.getCurrencyExchange();
            amt = Float.toString(amount);
            String mode = data.getString(3);
            String category = data.getString(4);
            String comment = data.getString(5);
            String type = data.getString(6);
            String currency = data.getString(7);
            if (type.equals("expense")){
                action = "you spent";
            } else {  action = "you got";}
            TransactionList transaction = new TransactionList(id,date,amt,mode,category,comment,type,action,currency);
            listData.add(transaction);
        }

   }

    public void setupPieChart(PieChart pieChart){
        pieChart.setUsePercentValues(false);
        List<PieEntry> value = new ArrayList<>();
        Float shopping = 0f;
        Float entertainment = 0f;
        Float rental = 0f;
        Float hospital = 0f;
        while(data.moveToNext()){
            String type = data.getString(6);
            String category = data.getString(4);
            float amount = data.getFloat(2);
            amount = amount*DefaultCurrency.getCurrencyExchange();
            if (type != null && category != null){
                if (type.equals("Expense")){
                    if (category.equals("Shopping")){
                        shopping = shopping + amount;
                    } else if (category.equals("Entertainment")){
                        entertainment = entertainment + amount;
                    } else if (category.equals("Rental")){
                        rental = rental + amount;
                    } else {
                        hospital = hospital + amount;
                    }
                }
            }

        }
        if (shopping > 0){
            value.add(new PieEntry(shopping,"Shopping"));
        }
        if (entertainment > 0){
            value.add(new PieEntry(entertainment,"Entertainment"));
        }
        if (rental > 0){
            value.add(new PieEntry(rental,"Rental"));
        }
        if (hospital > 0){
            value.add(new PieEntry(hospital,"Hospital"));
        }



        PieDataSet pieDataSet = new PieDataSet(value,"");

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(13f);
        pieData.setValueTextColor(Color.WHITE);


        pieChart.setData(pieData);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        pieChart.setDrawSliceText(false);

        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        final int[] MY_COLORS = ColorTemplate.COLORFUL_COLORS;
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for(int c: MY_COLORS) colors.add(c);
        pieDataSet.setColors(colors);

    }
    private void initCurrencyList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR",84.84f));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP",0.90f));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN",120.27f));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD",1.12f));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR",1f));
        String currencyPreference="";
        Cursor userData = userDatabaseHelper.getData();
        while (userData.moveToNext()){
            currencyPreference = userData.getString(6);

        }
        for (CurrencyItem currency: mCurrencyList){
            if (currencyPreference.equals(currency.getmCurrencyAbbreviation())){
                DefaultCurrency = currency;
            }
        }
    }
}
