package com.ssk.efx.marketdata.dao;

import com.ssk.efx.common.Constants;
import com.ssk.efx.marketdata.dto.Depth;
import ru.yandex.clickhouse.ClickHouseArray;
import ru.yandex.clickhouse.ClickHouseConnection;
import ru.yandex.clickhouse.ClickHouseDataSource;
import ru.yandex.clickhouse.domain.ClickHouseDataType;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static com.ssk.efx.common.Constants.INSERT_BATCH_SIZE;

public class DepthDaoImpl implements DepthDao {

    private static final String INSERT_DEPTH_DATA = String.format("INSERT INTO %s.depth (" +
            "  timestamp," +
            "  date," +
            "  triggerTimestamp," +
            "  ecnTimestamp," +
            "  ecn," +
            "  ccyPair," +
            "  tenor," +
            "  bid," +
            "  ask," +
            "  levels" +
            ") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Constants.SCHEMA);


    private static final String SELECT_DEPTH_DATA = String.format("SELECT * FROM %s.depth " +
            "WHERE ccyPair = ? AND ecn = ? AND date = ? ORDER BY timestamp ", Constants.SCHEMA);

    private final ClickHouseDataSource dataSource;

    public DepthDaoImpl(ClickHouseDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Depth depth) throws SQLException {
        try (ClickHouseConnection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_DEPTH_DATA)) {
            int i = 0;
            insertDepth(depth, ps, i);
            ps.executeUpdate();
        }
    }

    private void insertDepth(Depth depth, PreparedStatement ps, int i) throws SQLException {
        ps.setLong(++i, depth.getTimestamp());
        ps.setDate(++i, depth.getDate());
        ps.setLong(++i, depth.getTriggerTimestamp());
        ps.setLong(++i, depth.getEcnTimestamp());
        ps.setString(++i, depth.getEcn());
        ps.setString(++i, depth.getCcyPair());
        ps.setString(++i, depth.getTenor());
        ps.setArray(++i, new ClickHouseArray(ClickHouseDataType.Decimal64, depth.getBid()));
        ps.setArray(++i, new ClickHouseArray(ClickHouseDataType.Decimal64, depth.getAsk()));
        ps.setArray(++i, new ClickHouseArray(ClickHouseDataType.Decimal32, depth.getLevel()));
    }

    @Override
    public void insert(List<Depth> depths) throws SQLException {
        try (ClickHouseConnection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_DEPTH_DATA)) {

            for (int i = 0; i < depths.size(); i++) {
                int idx = 0;
                Depth depth = depths.get(i);
                insertDepth(depth, ps, idx);
                ps.addBatch();
                if (i != 0 && i % INSERT_BATCH_SIZE == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch();
        }
    }

    @Override
    public List<Depth> select(String ccyPair, String ecn, long date, long startTime, long endTime) throws SQLException {
        List<Depth> records = new ArrayList<>();
        try (ClickHouseConnection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_DEPTH_DATA)) {
            ps.setString(1, ccyPair);
            ps.setString(2, ecn);
            ps.setLong(3, date);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Depth depth = new Depth();
                    depth.setTimestamp(rs.getLong("timestamp"));
                    depth.setDate(rs.getDate("date"));
                    depth.setTime(rs.getTime("time"));
                    depth.setTimestamp(rs.getLong("triggerTimestamp"));
                    depth.setEcnTimestamp(rs.getLong("ecnTimestamp"));
                    depth.setEcn(rs.getString("ecn"));
                    depth.setCcyPair(rs.getString("ccyPair"));
                    depth.setTenor(rs.getString("tenor"));
                    depth.setBid((double[]) rs.getArray("bid").getArray());
                    depth.setBid((double[]) rs.getArray("ask").getArray());
                    depth.setBid((double[]) rs.getArray("levels").getArray());
                    records.add(depth);
                }
            }
        }
        return records;
    }
}
