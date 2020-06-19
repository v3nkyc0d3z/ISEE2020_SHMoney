package com.example.strawhats;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionList implements Parcelable {
//    id,date,amt,mode,category,comment,type,action
    public int id;
    public String date;
    public String amount;
    public String mode;
    public String category;
    public String comment;
    public String type;
    public String action;
    public String currency;

    public TransactionList(int id, String date, String amount, String mode, String category, String comment, String type, String action,String currency) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.mode = mode;
        this.category = category;
        this.comment = comment;
        this.type = type;
        this.action = action;
        this.currency = currency;
    }

    protected TransactionList(Parcel in) {
        id = in.readInt();
        date = in.readString();
        amount = in.readString();
        mode = in.readString();
        category = in.readString();
        comment = in.readString();
        type = in.readString();
        action = in.readString();
        currency = in.readString();
    }

    public static final Creator<TransactionList> CREATOR = new Creator<TransactionList>() {
        @Override
        public TransactionList createFromParcel(Parcel in) {
            return new TransactionList(in);
        }

        @Override
        public TransactionList[] newArray(int size) {
            return new TransactionList[size];
        }
    };

    @Override
    public String toString() {
        return "TransactionList{" +
                "id=" + id +
                ", date='" + date + '\'' +
                ", amount='" + amount + '\'' +
                ", mode='" + mode + '\'' +
                ", category='" + category + '\'' +
                ", comment='" + comment + '\'' +
                ", type='" + type + '\'' +
                ", action='" + action + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getCurrency(){return currency;}

    public void setCurrency(String currency){this.currency =  currency;}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(date);
        dest.writeString(amount);
        dest.writeString(mode);
        dest.writeString(category);
        dest.writeString(comment);
        dest.writeString(type);
        dest.writeString(action);
        dest.writeString(currency);
    }
}
