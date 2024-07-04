package com.example.doancnpm.RecyclerView.ViewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;

public class QuanLyMayTinhViewHolder extends RecyclerView.ViewHolder {
    public TextView TenMayTinh;
    public TextView CPU;
    public TextView GPU;
    public TextView RAM;
    public TextView Monitor;
    public TextView Price;
    public TextView Status;
    public TextView SeatLocation;
    public ImageButton menuButton;

    public QuanLyMayTinhViewHolder(@NonNull View itemView) {
        super(itemView);
        TenMayTinh = itemView.findViewById(R.id.TenMayTinhTextView);
        CPU = itemView.findViewById(R.id.CPUTextView);
        GPU = itemView.findViewById(R.id.GPUTextView);
        RAM = itemView.findViewById(R.id.RAMTextView);
        Monitor = itemView.findViewById(R.id.MonitorTextView);
        Price = itemView.findViewById(R.id.PriceTextView);
        Status = itemView.findViewById(R.id.TinhTrangMayTextView);
        SeatLocation = itemView.findViewById(R.id.GheMayTextView);
        menuButton = itemView.findViewById(R.id.QuanLyMayTinhMenuButton);
    }
    public void bind(Computer computer) {
        TenMayTinh.setText(computer.getName());
        CPU.setText(computer.getCpu());
        GPU.setText(computer.getGpu());
        RAM.setText(computer.getRam());
        Monitor.setText(computer.getMonitor());
        Status.setText(computer.getStatus());
        SeatLocation.setText(computer.getComputerSeatLocation());

        int price = computer.getPrice();
        String formattedPrice = String.format("%,d VND", price);
        Price.setText(formattedPrice);
    }
}