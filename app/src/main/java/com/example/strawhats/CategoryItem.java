package com.example.strawhats;

public class CategoryItem {
    private String mCategoryName;
    private int mCategoryImage;

    public CategoryItem(String mCategoryName, int mCategoryImage) {
        this.mCategoryName = mCategoryName;
        this.mCategoryImage = mCategoryImage;
    }

    public String getmCategoryName() {
        return mCategoryName;
    }

    public int getmCategoryImage() {
        return mCategoryImage;
    }
}
