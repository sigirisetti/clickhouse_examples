package com.ssk.efx.price;

import java.util.Objects;

public class PriceData {

    private String ccyPair;
    private double bid;
    private double ask;

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PriceData priceData = (PriceData) o;
        return Double.compare(priceData.bid, bid) == 0 && Double.compare(priceData.ask, ask) == 0 && Objects.equals(ccyPair, priceData.ccyPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ccyPair, bid, ask);
    }

    @Override
    public String toString() {
        return "PriceData{" +
                "ccyPair='" + ccyPair + '\'' +
                ", bid=" + bid +
                ", ask=" + ask +
                '}';
    }
}
