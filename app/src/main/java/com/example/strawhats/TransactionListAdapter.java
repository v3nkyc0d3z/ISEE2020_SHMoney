package com.example.strawhats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class TransactionListAdapter extends ArrayAdapter<TransactionList> {
    private static final String Tag = "TransacationListAdapter";
    private Context mContext;
    int mResource;

    public TransactionListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TransactionList> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int id = getItem(position).getId();
        String date = getItem(position).getDate();
        String amount = getItem(position).getAmount();
        String mode = getItem(position).getMode();
        String category = getItem(position).getCategory();
        String comment = getItem(position).getComment();
        String type = getItem(position).getType();
        String action = getItem(position).getAction();


        TransactionList item = new TransactionList(id,date,amount,mode,category,comment,type,action);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource,parent,false);

        TextView tvComment = (TextView) convertView.findViewById(R.id.lvaComment);
        TextView tvDate = (TextView) convertView.findViewById(R.id.lvaDate);
        TextView tvAction = (TextView) convertView.findViewById(R.id.lvaAction);
        TextView tvAmount = (TextView) convertView.findViewById(R.id.lvaAmount);

        tvComment.setText(comment);
        tvDate.setText(date);
        tvAmount.setText(amount);
        tvAction.setText(action);
        if (action.equals("you spent")){
            tvComment.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            tvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
            tvAction.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
        } else{
            tvComment.setTextColor(ContextCompat.getColor(mContext,R.color.green));
            tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.green));
            tvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.green));
            tvAction.setTextColor(ContextCompat.getColor(mContext,R.color.green));
        }
        return convertView;
    }
}
