package com.example.doancnpm.Objects;

public class Computer {
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getLoaiMayTinh() {
        return loaiMayTinh;
    }
    public void setLoaiMayTinh(String loaiMayTinh) {
        this.loaiMayTinh = loaiMayTinh;
    }
    public String getCpu() {
        return cpu;
    }
    public void setCpu(String cpu) {
        this.cpu = cpu;
    }
    public String getGpu() {
        return gpu;
    }
    public void setGpu(String gpu) {
        this.gpu = gpu;
    }
    public String getRam() {
        return ram;
    }
    public void setRam(String ram) {
        this.ram = ram;
    }
    public String getMonitor() {
        return monitor;
    }
    public void setMonitor(String monitor) {
        this.monitor = monitor;
    }
    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getComputerSeatLocation() {
        return computerSeatLocation;
    }

    public void setComputerSeatLocation(String computerSeatLocation) {
        this.computerSeatLocation = computerSeatLocation;
    }

    public String id;
    public String name;
    public String loaiMayTinh;
    public String cpu;
    public String gpu;
    public String ram;
    public String monitor;
    public Integer price;
    public String computerSeatLocation;
    public Computer() {
        // Default constructor required for calls to DataSnapshot.getValue(Computer.class)
    }

    public Computer(String id, String name, String loaiMayTinh, String cpu, String gpu, String ram, String monitor, Integer price, String computerSeatLocatio) {
        this.id = id;
        this.name = name;
        this.loaiMayTinh = loaiMayTinh;
        this.cpu = cpu;
        this.gpu = gpu;
        this.ram = ram;
        this.monitor = monitor;
        this.price = price;
        this.computerSeatLocation = computerSeatLocation;
    }

}
