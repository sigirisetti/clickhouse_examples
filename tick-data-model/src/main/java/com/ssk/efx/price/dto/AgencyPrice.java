package com.ssk.efx.price.dto;

import com.ssk.efx.base.dto.BaseDto;

public class AgencyPrice extends BaseDto {

    protected String stream;
    protected double[] spreads;
    private double[] bid;
    private double[] ask;
    private int[] level;
}
