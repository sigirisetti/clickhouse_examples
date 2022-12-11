-- Active database size
SELECT
    table,
    formatReadableSize(size) AS size,
    rows,
    days,
    formatReadableSize(avgDaySize) AS avgDaySize
FROM
(
    SELECT
        table,
        sum(bytes) AS size,
        sum(rows) AS rows,
        min(min_time) AS min_time,
        max(max_time) AS max_time,
        toUInt32((max_time - min_time) / 86400) AS days,
        size / ((max_time - min_time) / 86400) AS avgDaySize
    FROM system.parts
    WHERE active
    GROUP BY table
    ORDER BY rows DESC
);

-- Inactive parts size -- remove active from above sql