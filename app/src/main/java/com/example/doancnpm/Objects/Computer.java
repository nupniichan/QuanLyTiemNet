package com.example.doancnpm.Objects;

public class Computer {
    public String id;
    public String name;
    public String loaiMayTinh;
    public String cpu;
    public String gpu;
    public String ram;
    public String monitor;
    public String price;
    public String computerSeatLocation;
    public Computer() {
        // Default constructor required for calls to DataSnapshot.getValue(Computer.class)
    }

    public Computer(String id, String name, String loaiMayTinh, String cpu, String gpu, String ram, String monitor, String price, String computerSeatLocatio) {
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
