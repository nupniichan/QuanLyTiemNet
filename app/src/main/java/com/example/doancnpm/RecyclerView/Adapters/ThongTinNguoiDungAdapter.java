package com.example.doancnpm.RecyclerView.Adapters;

public class ThongTinNguoiDungAdapter {


    public  String Email, CCCD,GioiTinh,DiaChi,NgaySinh,SDT,MatKhau;
    public int userType;

    public ThongTinNguoiDungAdapter( String Email,String NgaySinh, String GioiTinh, String SDT,String MatKhau, String CCCD, String DiaChi, int userType) {
        this.Email=Email;
        this.NgaySinh = NgaySinh;
        this.GioiTinh = GioiTinh;
        this.SDT = SDT;
        this.MatKhau=MatKhau;
        this.CCCD = CCCD;
        this.DiaChi = DiaChi;
        this.userType=userType;
    }
}
