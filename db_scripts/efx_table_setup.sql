CREATE TABLE efx.depth (
  `timestamp` DateTime64(6),
  `date` Date,
  `triggerTimestamp` DateTime64(6),
  `ecnTimestamp` DateTime64(6),
  `ecn` LowCardinality(String),
  `ccyPair` FixedString(6),
  `tenor` LowCardinality(String),
  `spot` Array(Float64),
  `bid` Array(Float64),
  `ask` Array(Float64),
  `levels` Array(UInt32)
) ENGINE = MergeTree() PARTITION BY toYYYYMM(timestamp)
ORDER BY
  (
    gccMurmurHash(ccyPair),
    timestamp,
    date
  ) SAMPLE BY gccMurmurHash(ccyPair) SETTINGS index_granularity = 8192;

