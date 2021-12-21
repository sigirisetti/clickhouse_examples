package com.ssk.efx.clickhouse.dao;

import com.ssk.efx.marketdata.dao.DepthDao;
import com.ssk.efx.marketdata.dao.DepthDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.yandex.clickhouse.ClickHouseDataSource;

@Configuration
public class ClickhouseDaoConfiguration {

    @Bean
    public DepthDao getDepthDao(ClickHouseDataSource dataSource) {
        return new DepthDaoImpl(dataSource);
    }

}
