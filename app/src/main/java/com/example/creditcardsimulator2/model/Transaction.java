package com.example.creditcardsimulator2.model;

import androidx.annotation.NonNull;

import java.util.Random;

public class Transaction {
    double distanceFromHome;
    double distanceFromLastTransaction;
    double ratioToMedianPurchasePrice;
    int repeatRetailer;
    int usedChip;
    int usedPinNumber;
    int onlineOrder;

    public Transaction() {
    }

    public Transaction(double distanceFromHome, double distanceFromLastTransaction, double ratioToMedianPurchasePrice, int repeatRetailer, int usedChip, int usedPinNumber, int onlineOrder) {
        this.distanceFromHome = distanceFromHome;
        this.distanceFromLastTransaction = distanceFromLastTransaction;
        this.ratioToMedianPurchasePrice = ratioToMedianPurchasePrice;
        this.repeatRetailer = repeatRetailer;
        this.usedChip = usedChip;
        this.usedPinNumber = usedPinNumber;
        this.onlineOrder = onlineOrder;
    }

    public static Transaction getRandomTransaction(){
        Random random = new Random();
        Transaction transaction = new Transaction();
        transaction.setDistanceFromHome(random.nextDouble() * 1000);
        transaction.setDistanceFromLastTransaction(random.nextDouble() * 1000);
        transaction.setRatioToMedianPurchasePrice(random.nextDouble() * 8);
        transaction.setRepeatRetailer(random.nextInt(2));
        transaction.setUsedChip(random.nextInt(2));
        transaction.setUsedChip(random.nextInt(2));
        transaction.setOnlineOrder(random.nextInt(2));
        return transaction;
    }

    @NonNull
    @Override
    public String toString() {
        return "Transaction{" +
                "\ndistanceFromHome: " + distanceFromHome +
                "\ndistanceFromLastTransaction: " + distanceFromLastTransaction +
                "\nratioToMedianPurchasePrice: " + ratioToMedianPurchasePrice +
                "\nrepeatRetailer: " + repeatRetailer +
                "\nusedChip: " + usedChip +
                "\nusedPinNumber: " + usedPinNumber +
                "\nonlineOrder: " + onlineOrder +
                "\n}";
    }

    public double getDistanceFromHome() {
        return distanceFromHome;
    }

    public void setDistanceFromHome(double distanceFromHome) {
        this.distanceFromHome = distanceFromHome;
    }

    public double getDistanceFromLastTransaction() {
        return distanceFromLastTransaction;
    }

    public void setDistanceFromLastTransaction(double distanceFromLastTransaction) {
        this.distanceFromLastTransaction = distanceFromLastTransaction;
    }

    public double getRatioToMedianPurchasePrice() {
        return ratioToMedianPurchasePrice;
    }

    public void setRatioToMedianPurchasePrice(double ratioToMedianPurchasePrice) {
        this.ratioToMedianPurchasePrice = ratioToMedianPurchasePrice;
    }

    public int getRepeatRetailer() {
        return repeatRetailer;
    }

    public void setRepeatRetailer(int repeatRetailer) {
        this.repeatRetailer = repeatRetailer;
    }

    public int getUsedChip() {
        return usedChip;
    }

    public void setUsedChip(int usedChip) {
        this.usedChip = usedChip;
    }

    public int getUsedPinNumber() {
        return usedPinNumber;
    }

    public void setUsedPinNumber(int usedPinNumber) {
        this.usedPinNumber = usedPinNumber;
    }

    public int getOnlineOrder() {
        return onlineOrder;
    }

    public void setOnlineOrder(int onlineOrder) {
        this.onlineOrder = onlineOrder;
    }
}
