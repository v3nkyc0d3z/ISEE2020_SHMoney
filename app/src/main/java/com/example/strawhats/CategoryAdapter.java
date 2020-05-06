package com.example.strawhats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CategoryAdapter extends ArrayAdapter<CategoryItem> {
    public CategoryAdapter(Context context, ArrayList<CategoryItem> CategoryList){
        super (context,0, CategoryList);
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
    private View initView (int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.category_spinner_row,parent,false
            );
        }
        ImageView imageViewCategory = convertView.findViewById(R.id.image_view_category);
        TextView textViewName = convertView.findViewById(R.id.text_view_category);

        CategoryItem currentItem = getItem(position);
        if (currentItem != null) {
            imageViewCategory.setImageResource(currentItem.getmCategoryImage());
            textViewName.setText(currentItem.getmCategoryName());
        }
        return convertView;
    }
}

