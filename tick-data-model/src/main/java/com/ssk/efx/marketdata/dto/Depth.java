package com.ssk.efx.marketdata.dto;

import com.ssk.efx.base.dto.BaseDto;

public class Depth extends BaseDto {

    protected long ecnTimestamp;
    private String ecn;
    private String ccyPair;
    private String tenor;
    private double[] bid;
    private double[] ask;
    private int[] level;

    public long getEcnTimestamp() {
        return ecnTimestamp;
    }

    public void setEcnTimestamp(long ecnTimestamp) {
        this.ecnTimestamp = ecnTimestamp;
    }

    public String getEcn() {
        return ecn;
    }

    public void setEcn(String ecn) {
        this.ecn = ecn;
    }

    public String getCcyPair() {
        return ccyPair;
    }

    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    public String getTenor() {
        return tenor;
    }

    public void setTenor(String tenor) {
        this.tenor = tenor;
    }

    public double[] getBid() {
        return bid;
    }

    public void setBid(double[] bid) {
        this.bid = bid;
    }

    public double[] getAsk() {
        return ask;
    }

    public void setAsk(double[] ask) {
        this.ask = ask;
    }

    public int[] getLevel() {
        return level;
    }

    public void setLevel(int[] level) {
        this.level = level;
    }
}
