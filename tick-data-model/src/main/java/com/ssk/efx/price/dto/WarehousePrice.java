package com.ssk.efx.price.dto;

import com.ssk.efx.base.dto.BaseDto;

public class WarehousePrice extends BaseDto {

    protected String stream;
    protected double mid;
    private double[] bid;
    private double[] ask;
    private double[] spreads;
    private int[] level;
}
