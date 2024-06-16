package com.example.doancnpm.Objects;

// No need to import Utils or Bitmap here

public class Service {
    private String id;
    private String ServiceName;
    private String ServiceType;
    private String ServiceImage; // Now stores the URL
    private int price;

    public Service() {
        // Default constructor required for calls to DataSnapshot.getValue(Service.class)
    }

    public Service(String id, String ServiceName, String ServiceType, String ServiceImage, int price) {
        this.id = id;
        this.ServiceName = ServiceName;
        this.ServiceType = ServiceType;
        this.ServiceImage = ServiceImage;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String ServiceName) {
        this.ServiceName = ServiceName;
    }

    public String getServiceType() {
        return ServiceType;
    }

    public void setServiceType(String ServiceType) {
        this.ServiceType = ServiceType;
    }

    public void setServiceImage(String ServiceImage) {
        this.ServiceImage = ServiceImage;
    }

    public String getServiceImage() {
        return ServiceImage;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}