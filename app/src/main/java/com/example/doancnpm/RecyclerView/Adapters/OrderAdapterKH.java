package com.example.doancnpm.RecyclerView.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Order;
import com.example.doancnpm.R;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderAdapterKH extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Order> orderList;
    private DatabaseReference databaseReference;
    private Map<Order, String> orderKeyMap;
    private static final int VIEW_TYPE_COMPUTER = 1;
    private static final int VIEW_TYPE_SERVICE = 2;

    public OrderAdapterKH(Context context, List<Order> orderList, DatabaseReference databaseReference, Map<Order, String> orderKeyMap) {
        this.context = context;
        this.orderList = orderList;
        this.databaseReference = databaseReference;
        this.orderKeyMap = orderKeyMap;
    }

    @Override
    public int getItemViewType(int position) {
        Order order = orderList.get(position);
        if ("maytinh".equals(order.getLoaiDonHang())) {  // Check if the order is for a computer
            return VIEW_TYPE_COMPUTER;
        } else if ("dichvu".equals(order.getLoaiDonHang())) {  // Check if the order is for a service
            return VIEW_TYPE_SERVICE;
        } else {
            return super.getItemViewType(position);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COMPUTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.order_item_computer_kh, parent, false);
            return new ComputerViewHolder(view);
        } else if (viewType == VIEW_TYPE_SERVICE) {
            View view = LayoutInflater.from(context).inflate(R.layout.order_item_service_kh, parent, false);
            return new ServiceViewHolder(view);
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        Order order = orderList.get(position);

        if (order == null) return; // Adds a null check for robustness

        String orderKey = orderKeyMap.get(order);

        switch (viewType) {
            case VIEW_TYPE_COMPUTER:
                ComputerViewHolder computerViewHolder = (ComputerViewHolder) holder;
                bindComputerViewHolder(computerViewHolder, order);
                break;
            case VIEW_TYPE_SERVICE:
                ServiceViewHolder serviceViewHolder = (ServiceViewHolder) holder;
                bindServiceViewHolder(serviceViewHolder, order);
                break;
        }
    }

    private void bindComputerViewHolder(ComputerViewHolder holder, Order order) {
        holder.tvGheMay.setText("Ghế máy: " + order.getGheMay());
        holder.tvLoaiMay.setText("Loại máy: " + order.getLoaiMay());
        holder.tvNgayChoi.setText("Ngày chơi: " + order.getNgayChoi());
        holder.tvSoGioChoi.setText("Số giờ chơi: " + order.getSoGioChoi());
        holder.tvSoTien.setText("Số tiền: " + order.getSoTien());
        holder.tvTrangThai.setText("Trạng thái: " + order.getTrangThai());
        holder.tvUserEmail.setText("Email người đặt: " + order.getUserEmail());
    }

    private void bindServiceViewHolder(ServiceViewHolder holder, Order order) {
        holder.tvServiceName.setText("Tên dịch vụ: " + order.getServiceName());
        holder.tvServiceType.setText("Loại dịch vụ: " + order.getServiceType());
        holder.tvSoLuong.setText("Số lượng: " + order.getSoLuong());
        holder.tvSoGhe.setText("Số ghế: " + order.getSoGhe());
        holder.tvSoTien.setText("Số tiền: " + order.getSoTien());
        holder.tvTrangThai.setText("Trạng thái: " + order.getTrangThai());
        holder.tvUserEmail.setText("Email người đặt: " + order.getUserEmail());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public void setOrderList(List<Order> orderList, Map<Order, String> orderKeyMap) {
        this.orderList = orderList;
        this.orderKeyMap = orderKeyMap;
        notifyDataSetChanged();
    }

    public static class ComputerViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGheMay, tvLoaiMay, tvNgayChoi, tvSoGioChoi, tvSoTien, tvTrangThai, tvUserEmail;

        public ComputerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGheMay = itemView.findViewById(R.id.kh_tvGheMay);
            tvLoaiMay = itemView.findViewById(R.id.kh_tvLoaiMay);
            tvNgayChoi = itemView.findViewById(R.id.kh_tvNgayChoi);
            tvSoGioChoi = itemView.findViewById(R.id.kh_tvSoGioChoi);
            tvSoTien = itemView.findViewById(R.id.kh_tvSoTien);
            tvTrangThai = itemView.findViewById(R.id.kh_tvTrangThai);
            tvUserEmail = itemView.findViewById(R.id.kh_tvemailnguoidat);
        }
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public TextView tvServiceName, tvServiceType, tvSoLuong, tvSoGhe, tvSoTien, tvTrangThai, tvUserEmail;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.kh_tvServiceName);
            tvServiceType = itemView.findViewById(R.id.kh_tvServiceType);
            tvSoLuong = itemView.findViewById(R.id.kh_tvSoLuong);
            tvSoGhe = itemView.findViewById(R.id.kh_tvSoGhe);
            tvSoTien = itemView.findViewById(R.id.kh_tvSoTien);
            tvTrangThai = itemView.findViewById(R.id.kh_tvTrangThai);
            tvUserEmail = itemView.findViewById(R.id.kh_tvemailnguoidat);
        }
    }
}
