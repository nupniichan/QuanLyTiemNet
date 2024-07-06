package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.DichVu_NguoiDung_ViewHolder;

import java.util.List;

public class DichVu_NguoiDung_Adapter extends RecyclerView.Adapter<DichVu_NguoiDung_ViewHolder> {

    private final Context context;
    private List<Service> services;
    private final OnServiceClickListener serviceClickListener;

    public interface OnServiceClickListener {
        void onServiceClick(Service service);
    }

    public DichVu_NguoiDung_Adapter(Context context, List<Service> services, OnServiceClickListener listener) {
        this.context = context;
        this.services = services;
        this.serviceClickListener = listener;
    }

    // Interface callback
    public interface OrderCreationCallback {
        void onOrderCreatedSuccessfully();
    }

    @NonNull
    @Override
    public DichVu_NguoiDung_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DichVu_NguoiDung_ViewHolder(LayoutInflater.from(context).inflate(R.layout.danhsachdichvu_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DichVu_NguoiDung_ViewHolder holder, int position) {
        Service service = services.get(position);

        if (service != null) {
            holder.TenDichVu.setText(service.getServiceName());
            holder.ServiceName.setText(service.getServiceName());
            holder.LoaiDichVu.setText(service.getServiceType());
            holder.MoTa.setText(service.getMoTa());
            String imageUrl = service.getServiceImage();
            Glide.with(context)
                    .load(imageUrl)
                    .into(holder.HinhAnh);

            int price = service.getPrice();
            String formattedPrice = String.format("%,d VND", price);
            holder.Price.setText(formattedPrice);

            holder.ReservationButton.setOnClickListener(v -> {
                if (serviceClickListener != null) {
                    serviceClickListener.onServiceClick(service);
                } else {
                    Toast.makeText(context, "Lỗi: serviceClickListener is null!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return services != null ? services.size() : 0;
    }

    public void setServices(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }
}