package com.example.strawhats;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.tooltip.Tooltip;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    String[] categories = {"Income","Expense"};

    Cursor data;
    Float Net = 0f;
    UserDatabaseHelper userDatabaseHelper;
    ArrayList<CurrencyItem> mCurrencyList;
    CurrencyItem DefaultCurrency;
    ProgressBar ThresholdPB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        userDatabaseHelper = new UserDatabaseHelper(getActivity());

        initCurrencyList();
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
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ThresholdPB = (ProgressBar) view.findViewById(R.id.pbThreshold);
        ImageButton AddExpense = (ImageButton) view.findViewById(R.id.imageButtonAddExpense);
        ImageButton AddIncome = (ImageButton) view.findViewById(R.id.imageButtonAddIncome);
        FloatingActionButton FAB = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        TextView totalBalance = (TextView) view.findViewById(R.id.textView7);
        TransactionDatabaseHelper mDatabaseHelper = new TransactionDatabaseHelper(getActivity());
        data = mDatabaseHelper.getData();
        final ImageButton Help = (ImageButton) view.findViewById(R.id.homescreenhelp);
        Help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tooltip tooltip = new Tooltip.Builder(Help).setText("This is the homescreen \nThe donut chart represents your net transaction value \nTap the centre of the donut chart to view the transaction history \nClick on this message to make it disappear ")
                                    .setTextColor(Color.WHITE).setGravity(Gravity.BOTTOM)
                                    .setCornerRadius(8f)
                                    .setDismissOnClick(true)
                                    .show();
            }
        });



        PieChart pieChart = view.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);



        FAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),TransactionSummary.class));
            }
        });
        AddExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), TransactionForm.class));
            }
        });
        AddIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),IncomeForm.class));
            }
        });
        setupPieChart(pieChart);
        totalBalance.setText(DefaultCurrency.getCurrencySymbol() + Net.toString());
        if (Net < 0) {
            FAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorTextRed)));
        } else{
            FAB.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.colorTextGreen)));
        }
        return view;
    }


    public void setupPieChart(PieChart pieChart){
        pieChart.setUsePercentValues(false);
        List<PieEntry> value = new ArrayList<>();
        Float income = 0f;
        Float expense = 0f;
        while(data.moveToNext()){
            String type = data.getString(6);
            float amount = data.getFloat(2);
            float exchange = amount*DefaultCurrency.getCurrencyExchange();
            if (type.equals("Expense")){
                expense = expense + exchange;
                ThresholdPB.setProgress(25);
                Net = Net - exchange;
            } else{
                income = income + exchange;
                Net = Net + exchange;
            }
        }
        value.add(new PieEntry(income,"inc"));
        value.add(new PieEntry(expense,"exp"));


        PieDataSet pieDataSet = new PieDataSet(value,"Proportions");

        PieData pieData = new PieData(pieDataSet);
        pieData.setValueTextSize(13f);
        pieChart.setData(pieData);
        pieChart.setHoleColor(Color.parseColor("#1f4068"));
        pieChart.setHoleRadius(70);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.animateY(1000, Easing.EasingOption.EaseInOutCubic);

        final int[] MY_COLORS = {Color.rgb(12,166,139), Color.rgb(212,64,34)};
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
    }
}
