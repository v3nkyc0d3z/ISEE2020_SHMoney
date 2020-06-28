package com.example.strawhats;



public class CurrencyItem {
    private String mCurrencyName;
    private String mCurrencySymbol;
    private String mCurrencyAbbreviation;
    private Float mCurrencyExchangeEuro;

    public CurrencyItem(String countryName, String CurrencySymbol,String Abbreviation,Float CurrencyExchangeEuro) {
        mCurrencyName = countryName;
        mCurrencySymbol = CurrencySymbol;
        mCurrencyAbbreviation = Abbreviation;
        mCurrencyExchangeEuro = CurrencyExchangeEuro;
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

    public Float getCurrencyExchange(){ return mCurrencyExchangeEuro; }
}
