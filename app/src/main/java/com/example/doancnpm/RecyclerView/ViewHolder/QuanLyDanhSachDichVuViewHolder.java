package com.example.doancnpm.RecyclerView.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.R;

public class QuanLyDanhSachDichVuViewHolder extends RecyclerView.ViewHolder {
    public TextView TenDichVu;
    public TextView ServiceName;
    public TextView LoaiDichVu;
    public ImageView HinhAnh;
    public TextView Price;
    public Button ReservationButton;
    public ImageButton menuButton;
    public QuanLyDanhSachDichVuViewHolder(@NonNull View itemView) {
        super(itemView);
        TenDichVu = itemView.findViewById(R.id.TenDichVuTextView);
        ServiceName = itemView.findViewById(R.id.ServiceName);
        LoaiDichVu = itemView.findViewById(R.id.SerivceTypeID);
        HinhAnh = itemView.findViewById(R.id.ServiceImageID);
        Price = itemView.findViewById(R.id.PriceTextView);
        menuButton = itemView.findViewById(R.id.QuanLyDichVuButton);
    }
}
