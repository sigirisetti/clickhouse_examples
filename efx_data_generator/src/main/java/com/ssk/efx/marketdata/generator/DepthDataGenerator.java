package com.ssk.efx.marketdata.generator;

import com.ssk.db.clickhouse.ClickhouseDBParams;
import com.ssk.db.clickhouse.ClickhouseDatasourceInitializer;
import com.ssk.db.clickhouse.NanoClock;
import com.ssk.efx.clickhouse.dao.ClickhouseDaoConfiguration;
import com.ssk.efx.marketdata.dao.DepthDao;
import com.ssk.efx.marketdata.dto.Depth;
import com.ssk.efx.price.PriceData;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

@SpringBootApplication(scanBasePackages = {"com.ssk.efx", "com.ssk.db"})
public class DepthDataGenerator {

    private static final String[] ECN = new String[]{
            "JPM", //"UBS", "SCB", "UBS", "NOMURA", "BARC"
    };

    private static final String[] G10_PAIRS = new String[]{
            "EURUSD",
            /*
            "AUDUSD",
            "USDCAD",
            "USDCHF",
            "USDDKK",
            "NZDUSD",
            "USDJPY",
            "USDNOK",
            "GBPUSD",
            "USDSEK"
             */
    };

    private static final String[] DIRECT_PAIRS = new String[]{
            "AUDCAD",
            "AUDCHF",
            "AUDJPY",
            "AUDNZD",
            "AUDUSD",
            "BGNRON",
            "CADCHF",
            "CADJPY",
            "CHFBGN",
            "CHFJPY",
            "CHFRON",
            "CHFTRY",
            "EURAUD",
            "EURCAD",
            "EURCHF",
            "EURCZK",
            "EURDKK",
            "EURGBP",
            "EURHKD",
            "EURHUF",
            "EURILS",
            "EURJPY",
            "EURMXN",
            "EURNOK",
            "EURNZD",
            "EURPLN",
            "EURRON",
            "EURRUB",
            "EURSEK",
            "EURSGD",
            "EURTRY",
            "EURUSD",
            "EURZAR",
            "GBPAUD",
            "GBPBGN",
            "GBPCAD",
            "GBPCHF",
            "GBPCZK",
            "GBPDKK",
            "GBPHKD",
            "GBPHUF",
            "GBPJPY",
            "GBPNOK",
            "GBPNZD",
            "GBPPLN",
            "GBPRON",
            "GBPSEK",
            "GBPSGD",
            "GBPTRY",
            "GBPUSD",
            "GBPZAR",
            "HKDJPY",
            "NZDCAD",
            "NZDCHF",
            "NZDJPY",
            "NZDUSD",
            "SGDHKD",
            "SGDJPY",
            "TRYBGN",
            "TRYJPY",
            "TRYRON",
            "USDBGN",
            "USDCAD",
            "USDCHF",
            "USDCZK",
            "USDDKK",
            "USDHKD",
            "USDHUF",
            "USDILS",
            "USDJPY",
            "USDMXN",
            "USDNOK",
            "USDPLN",
            "USDRON",
            "USDRUB",
            "USDSEK",
            "USDSGD",
            "USDTRY",
            "USDZAR"
    };

    private static List<PriceData> loadPriceData() throws IOException {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL url = cl.getResource("prices.csv");
        List<String> list = Files.readAllLines(new File(url.getFile()).toPath(), Charset.defaultCharset());
        List<PriceData> prices = new ArrayList<>();
        for (String line : list) {
            String[] s = line.split(",");
            PriceData priceData = new PriceData();
            priceData.setCcyPair(s[0].replace("/", ""));
            priceData.setBid(new Double(s[1]));
            priceData.setAsk(new Double(s[2]));
            prices.add(priceData);
        }
        return prices;
    }

    public static void main(String[] args) throws IOException, SQLException {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.register(ClickhouseDBParams.class);
        ctx.register(ClickhouseDatasourceInitializer.class);
        ctx.register(ClickhouseDaoConfiguration.class);
        ctx.refresh();
        DepthDao depthDao = ctx.getBean(DepthDao.class);

        List<PriceData> initialPrices = loadPriceData();
        Random random = new Random();
        NanoClock nanoClock = new NanoClock();
        Map<String, PriceData> priceMap = initialPrices.stream().collect(Collectors.toMap(PriceData::getCcyPair, Function.identity()));

        int[] levels = new int[]{1000_000, 3000_000, 5000_000, 10_000_000};
        double[] bidSpread = new double[]{0.1, 0.2, 0.3, 0.4};
        double[] askSpread = new double[]{0.11, 0.21, 0.32, 0.41};

        int batch = 1000;
        int samplePerCcypair = 1_000_00;

        List<Depth> depths = new ArrayList<>(batch);

        long date = System.currentTimeMillis();
        long time = System.currentTimeMillis();
        long tsInMicros = System.currentTimeMillis() * 1_000;

        int count = 0;
        for (int i = 0; i < samplePerCcypair; i++) {
            for (String ecn : ECN) {
                for (String ccyPair : G10_PAIRS) {
                    //System.out.println("i# " + i + ", ecn : " + ecn + ", ccyPair: " + ccyPair);
                    double rand = random.nextDouble();
                    PriceData priceData = priceMap.get(ccyPair);
                    double bid = priceData.getBid();
                    double ask = priceData.getAsk();
                    double mid = (bid + ask) * 0.5;
                    double rdn = random.nextDouble();
                    double a = mid * 0.9;
                    double b = mid * 1.1;
                    double s = mid * 0.1;
                    double newMid = a + (b - a) * random.nextDouble();
                    com.ssk.efx.marketdata.dto.Depth depth = new Depth();
                    //depth.setTimestamp(nanoClock.getEpochNano());
                    tsInMicros += (long) (300_000 * random.nextDouble());
                    time += (long) (300 * random.nextDouble());
                    depth.setTimestamp(tsInMicros);
                    depth.setDate(new Date(date));
                    depth.setTime(new Time(time));
                    depth.setTriggerTimestamp((long)  (tsInMicros - 100_000 * random.nextDouble()));
                    depth.setEcnTimestamp((long) (tsInMicros - 1000_000 * random.nextDouble()));
                    depth.setEcn(ecn);
                    depth.setCcyPair(ccyPair);
                    depth.setTenor("SP");
                    depth.setBid(new double[]{newMid - bidSpread[0], newMid - bidSpread[1], newMid - bidSpread[2], newMid - bidSpread[3],});
                    depth.setAsk(new double[]{newMid + askSpread[0], newMid + askSpread[1], newMid + askSpread[2], newMid + askSpread[3],});
                    depth.setLevel(levels);

                    depths.add(depth);
                    count++;

                    if (count != 0 && count % batch == 0) {
                        depthDao.insert(depths);
                        depths.clear();
                        System.out.println("inserted = " + count);
                    }
                }
            }
        }
        if (!depths.isEmpty()) {
            depthDao.insert(depths);
        }
        System.out.println("insert completed");
    }
}
