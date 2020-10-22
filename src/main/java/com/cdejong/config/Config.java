package com.cdejong.config;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Config {
    private String prefix;
    private String activity;
    private List<String> owners;
    @SerializedName("bot_channels")
    private List<String> botChannels;

    public Config(String prefix, String activity, List<String> owners, List<String> botChannels) {
        this.prefix = prefix;
        this.activity = activity;
        this.owners = owners;
        this.botChannels = botChannels;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public List<String> getOwners() {
        return owners;
    }

    public void setOwners(List<String> owners) {
        this.owners = owners;
    }

    public List<String> getBotChannels() {
        return botChannels;
    }

    public void setBotChannels(List<String> botChannels) {
        this.botChannels = botChannels;
    }
}
