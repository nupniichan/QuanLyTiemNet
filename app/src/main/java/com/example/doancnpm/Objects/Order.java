package com.example.doancnpm.Objects;

import java.util.HashMap;

public class Order {
    private String trangThai;
    private String userEmail;
    private int soTien;
    private Computer computer;
    private Service service;
    private String loaiDonHang;  // "máy tính" or "dịch vụ"

    // Fields for all orders
    private String ngayChoi;
    private int soGioChoi;  // Relevant if it's a computer order

    // Fields for computer orders
    private String gheMay;
    private String loaiMay;

    // Fields for service orders
    private String serviceName;
    private String serviceType;
    private int soLuong;
    private String soGhe;

    private HashMap<String, Object> orderDetails;

    public Order() {
        orderDetails = new HashMap<>();
    }

    public Order(String trangThai, String userEmail, int soTien, HashMap<String, Object> orderDetails, String loaiDonHang) {
        this.trangThai = trangThai;
        this.userEmail = userEmail;
        this.soTien = soTien;
        this.orderDetails = orderDetails;
        this.loaiDonHang = loaiDonHang;
    }
    // Getters and setters
    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public int getSoTien() {
        return soTien;
    }

    public void setSoTien(int soTien) {
        this.soTien = soTien;
    }

    public String getLoaiDonHang() {
        return loaiDonHang;
    }

    public void setLoaiDonHang(String loaiDonHang) {
        this.loaiDonHang = loaiDonHang;
    }

    public String getNgayChoi() {
        return ngayChoi;
    }

    public void setNgayChoi(String ngayChoi) {
        this.ngayChoi = ngayChoi;
    }

    public int getSoGioChoi() {
        return soGioChoi;
    }

    public void setSoGioChoi(int soGioChoi) {
        this.soGioChoi = soGioChoi;
    }

    public String getGheMay() {
        return gheMay;
    }

    public void setGheMay(String gheMay) {
        this.gheMay = gheMay;
    }

    public String getLoaiMay() {
        return loaiMay;
    }

    public void setLoaiMay(String loaiMay) {
        this.loaiMay = loaiMay;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public String getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }
    // Method to set the Computer object
    public void setComputer(Computer computer) {
        this.computer = computer;
    }

    // Method to get the Computer object
    public Computer getComputer() {
        return computer;
    }

    // Method to set the Service object
    public void setService(Service service) {
        this.service = service;
    }

    // Method to get the Service object
    public Service getService() {
        return service;
    }
}