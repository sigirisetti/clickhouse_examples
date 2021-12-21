package com.ssk.efx.marketdata.dto;

import com.ssk.efx.base.dto.BaseDto;

public class Trade extends BaseDto {

    protected long ecnTimestamp;
    private String ecn;
    private String ccyPair;
    private double price;

}
