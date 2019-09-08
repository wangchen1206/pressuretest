package com.hp.roam.model;

import java.time.LocalDateTime;

public class OnlineRate {
    private Integer id;

    private String deviceId;

    private Double onlineRate;

    private LocalDateTime createDate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId == null ? null : deviceId.trim();
    }

    public Double getOnlineRate() {
        return onlineRate;
    }

    public void setOnlineRate(Double onlineRate) {
        this.onlineRate = onlineRate;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
}