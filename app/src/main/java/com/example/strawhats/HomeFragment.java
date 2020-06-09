package com.example.strawhats;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    String[] categories = {"Income","Expense"};

    Cursor data;
    Float Net = 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        ImageButton AddExpense = (ImageButton) view.findViewById(R.id.imageButtonAddExpense);
        ImageButton AddIncome = (ImageButton) view.findViewById(R.id.imageButtonAddIncome);
        FloatingActionButton FAB = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        TextView totalBalance = (TextView) view.findViewById(R.id.textView7);
        TransactionDatabaseHelper mDatabaseHelper = new TransactionDatabaseHelper(getActivity());
        data = mDatabaseHelper.getData();



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
        totalBalance.setText(Net.toString());
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

            if (type.equals("expense")){
                expense = expense + amount;
                Net = Net - amount;
            } else{
                income = income +amount;
                Net = Net + amount;
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

}
