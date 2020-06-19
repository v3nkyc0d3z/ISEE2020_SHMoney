package com.example.strawhats;



public class CurrencyItem {
    private String mCurrencyName;
    private String mCurrencySymbol;
    private String mCurrencyAbbreviation;

    public CurrencyItem(String countryName, String CurrencySymbol,String Abbreviation) {
        mCurrencyName = countryName;
        mCurrencySymbol = CurrencySymbol;
        mCurrencyAbbreviation = Abbreviation;
    }

    public String getCurrencyName() {
        return mCurrencyName;
    }

    public String getCurrencySymbol() {
        return mCurrencySymbol;
    }

    public String getmCurrencyAbbreviation() {
        return mCurrencyAbbreviation;
    }
}
