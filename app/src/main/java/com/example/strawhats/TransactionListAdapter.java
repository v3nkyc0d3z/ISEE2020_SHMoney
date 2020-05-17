package com.example.strawhats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class TransactionListAdapter extends BaseAdapter implements Filterable {
    Context c;
    ArrayList<TransactionList> Original,Temp;
    CustomFilter cs;


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

        TextView tvComment = (TextView) row.findViewById(R.id.lvaComment);
        TextView tvDate = (TextView) row.findViewById(R.id.lvaDate);
        TextView tvAction = (TextView) row.findViewById(R.id.lvaAction);
        TextView tvAmount = (TextView) row.findViewById(R.id.lvaAmount);

        tvComment.setText(Original.get(position).getComment());
        tvDate.setText(Original.get(position).getDate());
        tvAmount.setText(Original.get(position).getAmount());
        tvAction.setText(Original.get(position).getAction());

        if(Original.get(position).getAction().equals("you spent")){
            tvComment.setTextColor(ContextCompat.getColor(c,R.color.colorTextRed));
            tvDate.setTextColor(ContextCompat.getColor(c,R.color.colorTextRed));
            tvAmount.setTextColor(ContextCompat.getColor(c,R.color.colorTextRed));
            tvAction.setTextColor(ContextCompat.getColor(c,R.color.colorTextRed));

        } else{
            tvComment.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
            tvDate.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
            tvAmount.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
            tvAction.setTextColor(ContextCompat.getColor(c,R.color.colorTextGreen));
        }
        return row;
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
                    if (Temp.get(i).getComment().toUpperCase().contains(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction());
                        filters.add(record);
                    }
                    if (Temp.get(i).getMode().toUpperCase().equals(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction());
                        filters.add(record);
                    }if (Temp.get(i).getCategory().toUpperCase().equals(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction());
                        filters.add(record);
                    }if (Temp.get(i).getType().toUpperCase().equals(constraint)){
                        TransactionList record = new TransactionList(Temp.get(i).getId(),Temp.get(i).getDate(),Temp.get(i).getAmount(),
                                Temp.get(i).getMode(),Temp.get(i).getCategory(),Temp.get(i).getComment(),Temp.get(i).getType(),Temp.get(i).getAction());
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

//public class TransactionListAdapter extends ArrayAdapter<TransactionList> {
//    private static final String Tag = "TransacationListAdapter";
//    private Context mContext;
//    int mResource;
//
//    public TransactionListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TransactionList> objects) {
//        super(context, resource, objects);
//        this.mContext = context;
//        this.mResource = resource;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        int id = getItem(position).getId();
//        String date = getItem(position).getDate();
//        String amount = getItem(position).getAmount();
//        String mode = getItem(position).getMode();
//        String category = getItem(position).getCategory();
//        String comment = getItem(position).getComment();
//        String type = getItem(position).getType();
//        String action = getItem(position).getAction();
//
//
//        TransactionList item = new TransactionList(id,date,amount,mode,category,comment,type,action);
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        convertView = inflater.inflate(mResource,parent,false);
//
//        TextView tvComment = (TextView) convertView.findViewById(R.id.lvaComment);
//        TextView tvDate = (TextView) convertView.findViewById(R.id.lvaDate);
//        TextView tvAction = (TextView) convertView.findViewById(R.id.lvaAction);
//        TextView tvAmount = (TextView) convertView.findViewById(R.id.lvaAmount);
//
//        tvComment.setText(comment);
//        tvDate.setText(date);
//        tvAmount.setText(amount);
//        tvAction.setText(action);
//        if (action.equals("you spent")){
//            tvComment.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
//            tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
//            tvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
//            tvAction.setTextColor(ContextCompat.getColor(mContext,R.color.colorPrimary));
//        } else{
//            tvComment.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//            tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//            tvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//            tvAction.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//        }
//        return convertView;
//    }
//}


//package com.example.strawhats;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.Filterable;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.core.content.ContextCompat;
//
//import java.nio.file.DirectoryStream;
//import java.util.ArrayList;
//import java.util.List;
//
//public class TransactionListAdapter extends ArrayAdapter<TransactionList> implements Filterable {
//    private static final String Tag = "TransacationListAdapter";
//    private Context mContext;
//    int mResource;
//    public ArrayList<TransactionList> mObjects;
////    TransactionFilter cs;
//
//    public TransactionListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<TransactionList> objects) {
//        super(context, resource, objects);
//        this.mContext = context;
//        this.mResource = resource;
//        this.mObjects = objects;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        int id = getItem(position).getId();
//        String date = getItem(position).getDate();
//        String amount = getItem(position).getAmount();
//        String mode = getItem(position).getMode();
//        String category = getItem(position).getCategory();
//        String comment = getItem(position).getComment();
//        String type = getItem(position).getType();
//        String action = getItem(position).getAction();
//
//
//        TransactionList item = new TransactionList(id,date,amount,mode,category,comment,type,action);
//
//        LayoutInflater inflater = LayoutInflater.from(mContext);
//        convertView = inflater.inflate(mResource,parent,false);
//
//        TextView tvComment = (TextView) convertView.findViewById(R.id.lvaComment);
//        TextView tvDate = (TextView) convertView.findViewById(R.id.lvaDate);
//        TextView tvAction = (TextView) convertView.findViewById(R.id.lvaAction);
//        TextView tvAmount = (TextView) convertView.findViewById(R.id.lvaAmount);
//
//        tvComment.setText(comment);
//        tvDate.setText(date);
//        tvAmount.setText(amount);
//        tvAction.setText(action);
//        if (action.equals("you spent")){
//            tvComment.setTextColor(ContextCompat.getColor(mContext,R.color.colorTextRed));
//            tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.colorTextRed));
//            tvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.colorTextRed));
//            tvAction.setTextColor(ContextCompat.getColor(mContext,R.color.colorTextRed));
//        } else{
//            tvComment.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//            tvDate.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//            tvAmount.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//            tvAction.setTextColor(ContextCompat.getColor(mContext,R.color.green));
//        }
//        return convertView;
//    }
////
////    public void update(ArrayList<TransactionList> result){
////        mObjects = new ArrayList<>();
////        mObjects.addAll(result);
////        notifyDataSetChanged();
////    }
//
//
//
//    Filter myFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//            ArrayList<TransactionList> tempList = new ArrayList<TransactionList>();
//            if (constraint != null &&  constraint.length()>0){
//                int length = mObjects.size();
//                int i = 0;
//                constraint = constraint.toString().toUpperCase();
//                while(i<length){
//                    TransactionList item = mObjects.get(i);
//                    if(item.getComment().toUpperCase().contains(constraint)){
//                        tempList.add(item);
//                    }
//                    i++;
//                }
//                results.values = tempList;
//                results.count = tempList.size();
//            }
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            mObjects = (ArrayList<TransactionList>)results.values;
//            if (results.count > 0){
//                notifyDataSetChanged();
//            }else{
//                notifyDataSetInvalidated();
//            }
//        }
//    };
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        return myFilter;
//    }

//    class TransactionFilter extends Filter{
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//
//            if (constraint != null &&  constraint.length()>0){
//                constraint = constraint.toString().toUpperCase();
//                ArrayList<TransactionList> filters = new ArrayList<>();
//
//                for (int i = 0;i<tempmObjects.size();i++) {
//                    if (tempmObjects.get(i).getComment().toUpperCase().contains(constraint)) {
//                        TransactionList record = new TransactionList(tempmObjects.get(i).getId(), tempmObjects.get(i).getDate(),
//                                tempmObjects.get(i).getAmount(), tempmObjects.get(i).getMode(), tempmObjects.get(i).getCategory(),
//                                tempmObjects.get(i).getComment(), tempmObjects.get(i).getType(), tempmObjects.get(i).getAction());
//                        filters.add(record);
//                    }
//                }
//                results.count = filters.size();
//                results.values = filters;
//            }else {
//                results.count = tempmObjects.size();
//                results.values = tempmObjects;
//            }
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            mObjects = (ArrayList<TransactionList>) results.values;
//            notifyDataSetChanged();
//        }
//    }

//}
