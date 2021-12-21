package com.ssk.efx.base.dto;

import java.sql.Date;
import java.sql.Time;

public abstract class BaseDto {

    protected long timestamp;
    protected Date date;
    protected Time time;
    protected long triggerTimestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public long getTriggerTimestamp() {
        return triggerTimestamp;
    }

    public void setTriggerTimestamp(long triggerTimestamp) {
        this.triggerTimestamp = triggerTimestamp;
    }
}
