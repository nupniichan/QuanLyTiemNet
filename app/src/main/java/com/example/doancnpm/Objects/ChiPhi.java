package com.example.doancnpm.Objects;

public class ChiPhi {
private String MaChiPhi, TenChiPhi,MaNV,GhiChu,ThoiGian;
private  int SoTien;


    public ChiPhi(String maChiPhi, String tenChiPhi, String maNV, String ghiChu, String thoiGian, int soTien) {
        MaChiPhi = maChiPhi;
        TenChiPhi = tenChiPhi;
        MaNV = maNV;
        GhiChu = ghiChu;
        ThoiGian = thoiGian;
        SoTien = soTien;
    }
    public  ChiPhi (){

    }

    public String getMaChiPhi() {
        return MaChiPhi;
    }

    public void setMaChiPhi(String maChiPhi) {
        MaChiPhi = maChiPhi;
    }

    public String getTenChiPhi() {
        return TenChiPhi;
    }

    public void setTenChiPhi(String tenChiPhi) {
        TenChiPhi = tenChiPhi;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String maNV) {
        MaNV = maNV;
    }

    public String getGhiChu() {
        return GhiChu;
    }

    public void setGhiChu(String ghiChu) {
        GhiChu = ghiChu;
    }

    public String getThoiGian() {
        return ThoiGian;
    }

    public void setThoiGian(String thoiGian) {
        ThoiGian = thoiGian;
    }

    public int getSoTien() {
        return SoTien;
    }

    public void setSoTien(int soTien) {
        SoTien = soTien;
    }
}
