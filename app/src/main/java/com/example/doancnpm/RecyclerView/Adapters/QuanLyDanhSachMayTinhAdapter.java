package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.QuanLyDanhSachMayTinhViewHolder;

import java.util.List;

public class QuanLyDanhSachMayTinhAdapter extends RecyclerView.Adapter<QuanLyDanhSachMayTinhViewHolder> {

    Context context;
    List<Computer> computers;

    public QuanLyDanhSachMayTinhAdapter(Context context, List<Computer> computers){
        this.context = context;
        this.computers = computers;
    }
    @NonNull
    @Override
    public QuanLyDanhSachMayTinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyDanhSachMayTinhViewHolder(LayoutInflater.from(context).inflate(R.layout.quanly_danhsachmaytinh_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyDanhSachMayTinhViewHolder holder, int position) {
        holder.TenMayTinh.setText(computers.get(position).getName());
        holder.CPU.setText(computers.get(position).getCpu());
        holder.GPU.setText(computers.get(position).getGpu());
        holder.RAM.setText(computers.get(position).getRam());
        holder.Monitor.setText(computers.get(position).getMonitor());

        int price = computers.get(position).getPrice();
        String formattedPrice = String.format("%,d VND", price);
        holder.Price.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return computers.size();
    }
}
