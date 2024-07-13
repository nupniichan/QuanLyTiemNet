package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder> {

    private Context context;
    private List<Service> serviceList;

    public ServiceAdapter(Context context, List<Service> serviceList) {
        this.context = context;
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_service_trangchu, parent, false);
        return new ServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceViewHolder holder, int position) {
        Service service = serviceList.get(position);
        holder.textViewServiceName.setText(service.getServiceName());
        holder.textViewServicePrice.setText("Giá: " + service.getPrice() + " VND");

        // Hiển thị hình ảnh từ Firebase Storage sử dụng Picasso
        Picasso.get()
                .load(service.getServiceImage())
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh placeholder khi đang tải
                .error(R.drawable.ic_launcher_foreground) // Hình ảnh hiển thị khi có lỗi
                .into(holder.imageViewService);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewService;
        public TextView textViewServiceName;
        public TextView textViewServicePrice;

        public ServiceViewHolder(View itemView) {
            super(itemView);
            imageViewService = itemView.findViewById(R.id.imageViewService);
            textViewServiceName = itemView.findViewById(R.id.textViewServiceName);
            textViewServicePrice = itemView.findViewById(R.id.textViewServicePrice);
        }
    }
}