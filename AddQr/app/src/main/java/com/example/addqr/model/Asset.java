package com.example.addqr.model;

public class Asset {

    private int id;
    private String qrCode;
    private String name;
    private String description;
    private String status;
    private String lastLocation;
    private long creationTimestamp;

    public Asset() {
    }

    public Asset(String qrCode, String name, String description, String status, String lastLocation, long creationTimestamp) {
        this.qrCode = qrCode;
        this.name = name;
        this.description = description;
        this.status = status;
        this.lastLocation = lastLocation;
        this.creationTimestamp = creationTimestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }

    public long getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(long creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }
}