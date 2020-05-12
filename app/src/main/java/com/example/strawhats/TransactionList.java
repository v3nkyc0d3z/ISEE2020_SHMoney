package com.example.strawhats;

public class TransactionList {
    public String comment;
    public String date;
    public String amount;
    public String action;

    public TransactionList(String comment, String date, String amount, String action) {
        this.comment = comment;
        this.date = date;
        this.amount = amount;
        this.action = action;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
