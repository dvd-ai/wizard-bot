package com.learngerman.wizardbot.channel;

public class Channel {

    private Long channelId;
    private boolean isForReport;
    private boolean isIgnoredForCurrencyOperations;

    public Channel() {
    }

    public Channel(Long channelId, boolean isForReport, boolean isIgnoredForCurrencyOperations) {
        this.channelId = channelId;
        this.isForReport = isForReport;
        this.isIgnoredForCurrencyOperations = isIgnoredForCurrencyOperations;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public boolean isForReport() {
        return isForReport;
    }

    public void setForReport(boolean forReport) {
        isForReport = forReport;
    }

    public boolean isIgnoredForCurrencyOperations() {
        return isIgnoredForCurrencyOperations;
    }

    public void setIgnoredForCurrencyOperations(boolean ignoredForCurrencyOperations) {
        isIgnoredForCurrencyOperations = ignoredForCurrencyOperations;
    }
}
