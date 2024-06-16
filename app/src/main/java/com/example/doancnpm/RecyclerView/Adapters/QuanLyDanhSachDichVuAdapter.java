package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.QuanLyDanhSachDichVuViewHolder;

import java.util.List;

public class QuanLyDanhSachDichVuAdapter extends RecyclerView.Adapter<QuanLyDanhSachDichVuViewHolder> {

    Context context;
    List<Service> service;

    public QuanLyDanhSachDichVuAdapter(Context context, List<Service> service){
        this.context = context;
        this.service = service;
    }

    @NonNull
    @Override
    public QuanLyDanhSachDichVuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyDanhSachDichVuViewHolder(LayoutInflater.from(context).inflate(R.layout.quanly_danhsachdichvu_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyDanhSachDichVuViewHolder holder, int position) {
        holder.TenDichVu.setText(service.get(position).getServiceName());
        holder.ServiceName.setText(service.get(position).getServiceName());
        holder.LoaiDichVu.setText(service.get(position).getServiceType());
        holder.MoTa.setText(service.get(position).getMoTa());
        String imageUrl = service.get(position).getServiceImage();

        Glide.with(context)
                .load(imageUrl)
                .into(holder.HinhAnh);

        int price = service.get(position).getPrice();
        String formattedPrice = String.format("%,d VND", price);
        holder.Price.setText(formattedPrice);
    }

    @Override
    public int getItemCount() {
        return service.size();
    }

    public void setServices(List<Service> services) {
        this.service = services;
        notifyDataSetChanged();
    }
}