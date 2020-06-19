package com.example.strawhats;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;


public class CurrencyAdapter extends ArrayAdapter<CurrencyItem> {

    public CurrencyAdapter(Context context, ArrayList<CurrencyItem> countryList) {
        super(context, 0, countryList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.currency_spinner_row, parent, false
            );
        }

        TextView CurrencySign = convertView.findViewById(R.id.currency_sign);
        TextView CurrencyName = convertView.findViewById(R.id.text_view_name);

        CurrencyItem currentItem = getItem(position);

        if (currentItem != null) {
            CurrencySign.setText(currentItem.getCurrencySymbol());
            CurrencyName.setText(currentItem.getCurrencyName());
        }

        return convertView;
    }
}