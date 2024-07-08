package com.example.doancnpm.RecyclerView.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.R;

public class MayTinh_NguoiDung_ViewHolder extends RecyclerView.ViewHolder {
    public TextView TenMayTinh;
    public TextView CPU;
    public TextView GPU;
    public TextView RAM;
    public TextView Monitor;
    public TextView Price;
    public TextView Status;
    public TextView SeatLocation;
    public Button ReservationButton;
    public MayTinh_NguoiDung_ViewHolder(@NonNull View itemView) {
        super(itemView);
        TenMayTinh = itemView.findViewById(R.id.TenMayTinhTextView);
        CPU = itemView.findViewById(R.id.CPUTextView);
        GPU = itemView.findViewById(R.id.GPUTextView);
        RAM = itemView.findViewById(R.id.RAMTextView);
        Monitor = itemView.findViewById(R.id.MonitorTextView);
        Price = itemView.findViewById(R.id.PriceTextView);
        ReservationButton = itemView.findViewById(R.id.btnReservation);
        Status = itemView.findViewById(R.id.TinhTrangMayTextView);
        SeatLocation = itemView.findViewById(R.id.GheMayTextView);
    }
}
