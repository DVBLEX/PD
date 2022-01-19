package com.pad.server.anpr.jsonentities;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TimeoutJson {

    private int     btDowntimeSecondsLimit;
    private int     btUptimeSecondsLimit;
    private boolean isIISServerEnabled;

    public int getBtDowntimeSecondsLimit() {
        return btDowntimeSecondsLimit;
    }

    public void setBtDowntimeSecondsLimit(int btDowntimeSecondsLimit) {
        this.btDowntimeSecondsLimit = btDowntimeSecondsLimit;
    }

    public int getBtUptimeSecondsLimit() {
        return btUptimeSecondsLimit;
    }

    public void setBtUptimeSecondsLimit(int btUptimeSecondsLimit) {
        this.btUptimeSecondsLimit = btUptimeSecondsLimit;
    }

    public boolean getIsIISServerEnabled() {
        return isIISServerEnabled;
    }

    public void setIsIISServerEnabled(boolean isIISServerEnabled) {
        this.isIISServerEnabled = isIISServerEnabled;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("TimeoutJson [btDowntimeSecondsLimit=");
        builder.append(btDowntimeSecondsLimit);
        builder.append(", btUptimeSecondsLimit=");
        builder.append(btUptimeSecondsLimit);
        builder.append(", isIISServerEnabled=");
        builder.append(isIISServerEnabled);
        builder.append("]");
        return builder.toString();
    }

}
