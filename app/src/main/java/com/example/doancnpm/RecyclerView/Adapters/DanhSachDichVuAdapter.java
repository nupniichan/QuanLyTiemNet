package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.DanhSachDichVuViewHolder;
import java.util.List;

public class DanhSachDichVuAdapter extends RecyclerView.Adapter<DanhSachDichVuViewHolder> {

    private final Context context;
    private List<Service> services;

    public DanhSachDichVuAdapter(Context context, List<Service> services){
        this.context = context;
        this.services = services;
    }

    @NonNull
    @Override
    public DanhSachDichVuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DanhSachDichVuViewHolder(LayoutInflater.from(context).inflate(R.layout.danhsachdichvu_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DanhSachDichVuViewHolder holder, int position) {
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
