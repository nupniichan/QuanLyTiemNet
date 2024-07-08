package com.example.doancnpm.Objects;

public class Users {


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCCCD() {
        return CCCD;
    }

    public void setCCCD(String CCCD) {
        this.CCCD = CCCD;
    }

    public String getGioiTinh() {
        return GioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        GioiTinh = gioiTinh;
    }

    public String getDiaChi() {
        return DiaChi;
    }

    public void setDiaChi(String diaChi) {
        DiaChi = diaChi;
    }

    public String getNgaySinh() {
        return NgaySinh;
    }

    public void setNgaySinh(String ngaySinh) {
        NgaySinh = ngaySinh;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getMatKhau() {
        return MatKhau;
    }

    public void setMatKhau(String matKhau) {
        MatKhau = matKhau;
    }

    public double getSoDuTK() {
        return SoDuTK;
    }

    public void setSoDuTK(double soDuTK) {
        SoDuTK = soDuTK;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    private String Email;
    private String CCCD;
    private String GioiTinh;
    private String DiaChi;
    private String NgaySinh;
    private String SDT;
    private String MatKhau;

    public String getThuHang() {
        return ThuHang;
    }

    public void setThuHang(String thuHang) {
        ThuHang = thuHang;
    }

    private String ThuHang;
    private double SoDuTK;
    private int userType;

    public Users(String Email, String NgaySinh, String GioiTinh, String SDT, String MatKhau, String CCCD, String DiaChi, int userType, Double SoDuTK, String ThuHang) {
        this.Email=Email;
        this.NgaySinh = NgaySinh;
        this.GioiTinh = GioiTinh;
        this.SDT = SDT;
        this.MatKhau=MatKhau;
        this.CCCD = CCCD;
        this.DiaChi = DiaChi;
        this.SoDuTK = SoDuTK;
        this.userType=userType;
        this.ThuHang = ThuHang;
    }
    public  Users(){

    }
}
