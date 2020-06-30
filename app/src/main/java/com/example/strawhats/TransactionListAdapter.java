package com.example.strawhats;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;

import java.util.ArrayList;
import java.util.List;

public class TransactionListAdapter extends BaseAdapter implements Filterable {
    Context c;
    ArrayList<TransactionList> Original,Temp;
    ArrayList<CurrencyItem> mCurrencyList;
    CustomFilter cs;
    UserDatabaseHelper userDatabaseHelper;


    public TransactionListAdapter(Context context, ArrayList<TransactionList> Objects){
        this.c = context;
        this.Original = Objects;
        this.Temp = Objects;
    }

    @Override
    public int getCount() {
        return Original.size();
    }

    @Override
    public Object getItem(int position) {
        return Original.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.adapter_transaction_list,null);
        row.setPadding(0,30,0,-90);
        TextView tvComment = (TextView) row.findViewById(R.id.lvaComment);
        tvComment.setTextSize(18);
        TextView tvDate = (TextView) row.findViewById(R.id.lvaDate);
        tvDate.setTextSize(14);
        TextView tvAction = (TextView) row.findViewById(R.id.lvaAction);
        tvAction.setTextSize(14);
        TextView tvAmount = (TextView) row.findViewById(R.id.lvaAmount);
        tvAmount.setTextSize(18);
        initCurrencyList();
        String currencySymbol = "";
        Float exchange = 1f;
        for(int i = 0; i<mCurrencyList.size();i++){
            if (Original.get(position).getCurrency().equals(mCurrencyList.get(i).getmCurrencyAbbreviation())){
                currencySymbol = mCurrencyList.get(i).getCurrencySymbol();
                exchange = mCurrencyList.get(i).getCurrencyExchange();
                break;
            }
        }
        String comment = Original.get(position).getComment();
//        tvComment.setText(Original.get(position).getComment());
        tvComment.setText(HtmlCompat.fromHtml(comment,0));
        tvDate.setText(Original.get(position).getDate());
        Float amount = Float.parseFloat(Original.get(position).getAmount());
        tvAmount.setText(currencySymbol+ " " +amount*exchange);
        tvAction.setText(Original.get(position).getAction());

        if(Original.get(position).getAction().equals("you spent")){
            tvComment.setTextColor(ContextCompat.getColor(c, R.color.colorTextRed));
            tvDate.setTextColor(ContextCompat.getColor(c,R.color.colorTextGrey));
            tvAmount.setTextColor(ContextCompat.getColor(c,R.color.colorTextRed));
            tvAction.setTextColor(ContextCompat.getColor(c,R.color.colorTextRed));

        } else{
            tvComment.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
            tvDate.setTextColor(ContextCompat.getColor(c,R.color.colorTextGrey));
            tvAmount.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
            tvAction.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
        }
        ImageView categoryIcon = (ImageView) row.findViewById(R.id.TLCategoryIcon);
        String cat = (String) Original.get(position).getCategory();
        if (cat != null) {
            if (cat.equals("Rental")) {
                categoryIcon.setImageResource(R.drawable.ic_home_black_24dp);
            } else if (cat.equals("Shopping")) {
                categoryIcon.setImageResource(R.drawable.ic_shopping_category);
            } else if (cat.equals("Hospital")) {
                categoryIcon.setImageResource(R.drawable.ic_hospital_category);
            } else if (cat.equals("Wage")) {
                categoryIcon.setImageResource(R.drawable.ic_work_black_24dp);
            } else if (cat.equals("Interest")) {
                categoryIcon.setImageResource(R.drawable.ic_attach_money_black_24dp);
            } else if (cat.equals("Entertainment")) {
                categoryIcon.setImageResource(R.drawable.ic_entertainment_category);
            } else if (cat.equals("Other")) {
                categoryIcon.setImageResource(R.drawable.ic_turned_other_24dp);
            }
        }
        if(Original.get(position).getType().equals("Income")){
            categoryIcon.setColorFilter(ContextCompat.getColor(c,R.color.colorTextGreen));
        } else {
            categoryIcon.setColorFilter(ContextCompat.getColor(c,R.color.colorTextRed));
        }
//        if(Original.get(position).getCategory().equals("Wage")){
//            categoryIcon.setImageResource(R.drawable.ic_work_black_24dp);
//        } else  else if (Original.get(position).getCategory().equals("Interest")){
//            categoryIcon.setImageResource(R.drawable.ic_attach_money_black_24dp);
//        } else if (Original.get(position).getCategory().equals("Other")){
//            categoryIcon.setImageResource(R.drawable.ic_turned_other_24dp);
//        }
        return row;
    }
    private void initCurrencyList() {
        mCurrencyList = new ArrayList<>();
        mCurrencyList.add(new CurrencyItem("Rupee", "\u20B9","INR",84.84f));
        mCurrencyList.add(new CurrencyItem("Pound", "£","GBP",0.90f));
        mCurrencyList.add(new CurrencyItem("Yen", "¥","YEN",120.27f));
        mCurrencyList.add(new CurrencyItem("Dollar", "$","USD",1.12f));
        mCurrencyList.add(new CurrencyItem("Euro", "€","EUR",1f));
    }
    @Override
    public Filter getFilter() {
        if (cs == null){
            cs = new CustomFilter();
        }
        return cs;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0){
                ArrayList<TransactionList> filters = new ArrayList<>();
                constraint = constraint.toString().toUpperCase();
                for (int i = 0 ; i<Temp.size();i++){
                    if (Temp.get(i).getComment().toUpperCase().contains(constraint) && Temp.get(i).getComment().length()!=0){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction(),Temp.get(i).getCurrency());
                        filters.add(record);
                    }
                    if (Temp.get(i).getMode().toUpperCase().equals(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction(),Temp.get(i).getCurrency());
                        filters.add(record);
                    }if (Temp.get(i).getCategory().toUpperCase().equals(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction(),Temp.get(i).getCurrency());
                        filters.add(record);
                    }if (Temp.get(i).getType().toUpperCase().equals(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction(),Temp.get(i).getCurrency());
                        filters.add(record);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            } else{
                results.count = Temp.size();
                results.values = Temp;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Original = (ArrayList<TransactionList>) results.values;
            notifyDataSetChanged();
        }
    }
}
