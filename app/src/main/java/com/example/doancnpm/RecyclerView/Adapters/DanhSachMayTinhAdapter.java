package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.DanhSachMayTinhViewHolder;

import java.text.DecimalFormat;
import java.util.List;

public class DanhSachMayTinhAdapter extends RecyclerView.Adapter<DanhSachMayTinhViewHolder> {

    Context context;
    List<Computer> computers;

    public DanhSachMayTinhAdapter(Context context,List<Computer> computers){
        this.context = context;
        this.computers = computers;
    }
    @NonNull
    @Override
    public DanhSachMayTinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DanhSachMayTinhViewHolder(LayoutInflater.from(context).inflate(R.layout.danhsachmaytinh_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachMayTinhViewHolder holder, int position) {
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
