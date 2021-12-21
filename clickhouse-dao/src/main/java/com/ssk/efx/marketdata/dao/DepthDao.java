package com.ssk.efx.marketdata.dao;

import com.ssk.efx.marketdata.dto.Depth;

import java.sql.SQLException;
import java.util.List;

public interface DepthDao {
    void insert(Depth depth) throws SQLException;
    void insert(List<Depth> depths) throws SQLException;
    List<Depth> select(String ccyPair, String ecn, long date, long startTime, long endTime) throws SQLException;
}
