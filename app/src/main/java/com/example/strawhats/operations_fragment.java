package com.example.strawhats;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class operations_fragment extends Fragment {

    String[] categories = {"Income","Expense"};
    int[] amounts = {1200, 800};


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_operations,container,false);
        ImageButton AddExpense = (ImageButton) view.findViewById(R.id.imageButtonAddExpense);
        ImageButton AddIncome = (ImageButton) view.findViewById(R.id.imageButtonAddIncome);
        FloatingActionButton FAB = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        TextView totalBalance = (TextView) view.findViewById(R.id.textView7);


        PieChart pieChart = view.findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        totalBalance.setText("$1234");

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
        return view;
    }


    public void setupPieChart(PieChart pieChart){
        List<PieEntry> value = new ArrayList<>();
        value.add(new PieEntry(40f, "Jan"));
        value.add(new PieEntry(60f, "Feb"));

        PieDataSet pieDataSet = new PieDataSet(value,"Month");

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.setHoleRadius(70);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

    }
}
