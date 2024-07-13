package com.example.doancnpm.RecyclerView.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
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

import java.util.List;
import java.util.Map;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<Order> orderList;
    private DatabaseReference databaseReference;
    private Map<Order, String> orderKeyMap;
    private static final int VIEW_TYPE_COMPUTER = 1;
    private static final int VIEW_TYPE_SERVICE = 2;

    public OrderAdapter(Context context, List<Order> orderList, DatabaseReference databaseReference, Map<Order, String> orderKeyMap) {
        this.context = context;
        this.orderList = orderList;
        this.databaseReference = databaseReference;
        this.orderKeyMap = orderKeyMap;
    }

    @Override
    public int getItemViewType(int position) {
        Order order = orderList.get(position);
        if ("maytinh".equals(order.getLoaiDonHang())) {
            return VIEW_TYPE_COMPUTER;
        } else if ("dichvu".equals(order.getLoaiDonHang())) {
            return VIEW_TYPE_SERVICE;
        } else {
            Log.e("OrderAdapter", "Unrecognized order type: " + order.getLoaiDonHang());
            return -1;  // Continue to use -1 for unrecognized types to facilitate debugging.
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == VIEW_TYPE_COMPUTER) {
            View view = inflater.inflate(R.layout.item_order_computer, parent, false);
            return new ComputerViewHolder(view);
        } else if (viewType == VIEW_TYPE_SERVICE) {
            View view = inflater.inflate(R.layout.item_order_service, parent, false);
            return new ServiceViewHolder(view);
        } else {
            Log.e("OrderAdapter", "Creating default view holder for unexpected type: " + viewType);
            View view = inflater.inflate(R.layout.unexpected_order_type, parent, false);
            return new RecyclerView.ViewHolder(view) {};
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        String orderKey = orderKeyMap.get(order);
        if (holder instanceof ComputerViewHolder) {
            populateComputerViewHolder((ComputerViewHolder) holder, order);
        } else if (holder instanceof ServiceViewHolder) {
            populateServiceViewHolder((ServiceViewHolder) holder, order);
        }
    }

    private void populateComputerViewHolder(ComputerViewHolder holder, Order order) {
        holder.tvGheMay.setText(order.getGheMay() != null ? order.getGheMay() : "N/A");
        holder.tvLoaiMay.setText(order.getLoaiMay() != null ? order.getLoaiMay() : "N/A");
        holder.tvNgayChoi.setText(order.getNgayChoi() != null ? order.getNgayChoi() : "N/A");
        holder.tvSoGioChoi.setText(String.valueOf(order.getSoGioChoi()));
        holder.tvSoTien.setText(String.valueOf(order.getSoTien()));
        holder.tvTrangThai.setText(order.getTrangThai() != null ? order.getTrangThai() : "N/A");
        holder.tvOrderEmailNguoiDat.setText(order.getUserEmail() != null ? order.getUserEmail() : "N/A");

        Log.d("OrderAdapter", "ComputerViewHolder - tvGheMay: " + order.getGheMay());
        Log.d("OrderAdapter", "ComputerViewHolder - tvLoaiMay: " + order.getLoaiMay());
        Log.d("OrderAdapter", "ComputerViewHolder - tvNgayChoi: " + order.getNgayChoi());
        Log.d("OrderAdapter", "ComputerViewHolder - tvSoGioChoi: " + order.getSoGioChoi());
        Log.d("OrderAdapter", "ComputerViewHolder - tvSoTien: " + order.getSoTien());
        Log.d("OrderAdapter", "ComputerViewHolder - tvTrangThai: " + order.getTrangThai());
        Log.d("OrderAdapter", "ComputerViewHolder - tvOrderEmailNguoiDat: " + order.getUserEmail());

        holder.xacNhan.setOnClickListener(v -> confirmOrder(holder, order));
    }

    private void populateServiceViewHolder(ServiceViewHolder holder, Order order) {
        holder.tvServiceName.setText("Tên dịch vụ: " + order.getServiceName() != null ? order.getServiceName() : "N/A");
        holder.tvServiceType.setText("Loại dịch vụ: " + order.getServiceType() != null ? order.getServiceType() : "N/A");
        holder.tvSoLuong.setText(String.valueOf(order.getSoLuong()));
        holder.tvSoGhe.setText(order.getSoGhe() != null ? order.getSoGhe() : "N/A");
        holder.tvSoTien.setText(String.valueOf(order.getSoTien()));
        holder.tvTrangThai.setText(order.getTrangThai() != null ? order.getTrangThai() : "N/A");
        holder.tvOrderEmailNguoiDat.setText(order.getUserEmail() != null ? order.getUserEmail() : "N/A");

        Log.d("OrderAdapter", "ServiceViewHolder - tvServiceName: " + order.getServiceName());
        Log.d("OrderAdapter", "ServiceViewHolder - tvServiceType: " + order.getServiceType());
        Log.d("OrderAdapter", "ServiceViewHolder - tvSoLuong: " + order.getSoLuong());
        Log.d("OrderAdapter", "ServiceViewHolder - tvSoGhe: " + order.getSoGhe());
        Log.d("OrderAdapter", "ServiceViewHolder - tvSoTien: " + order.getSoTien());
        Log.d("OrderAdapter", "ServiceViewHolder - tvTrangThai: " + order.getTrangThai());
        Log.d("OrderAdapter", "ServiceViewHolder - tvOrderEmailNguoiDat: " + order.getUserEmail());

        holder.xacNhan.setOnClickListener(v -> confirmOrder(holder, order));
    }

    private void confirmOrder(RecyclerView.ViewHolder holder, Order order) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn xác nhận đơn hàng này?")
                .setPositiveButton("Có", (dialog, which) -> {
                    int currentPosition = holder.getAdapterPosition();
                    if (currentPosition != RecyclerView.NO_POSITION) {
                        String orderKey = orderKeyMap.get(order);
                        databaseReference.child(orderKey).child("trangThai").setValue("Đã xác nhận");
                        order.setTrangThai("Đã xác nhận");
                        notifyItemChanged(currentPosition);
                    }
                })
                .setNegativeButton("Không", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public Map<Order, String> getOrderKeyMap() {
        return orderKeyMap;
    }

    public static class ComputerViewHolder extends RecyclerView.ViewHolder {
        public TextView tvGheMay, tvLoaiMay, tvNgayChoi, tvSoGioChoi, tvSoTien, tvTrangThai, tvOrderEmailNguoiDat;
        public Button xacNhan;

        public ComputerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvGheMay = itemView.findViewById(R.id.tvGheMay);
            tvLoaiMay = itemView.findViewById(R.id.tvLoaiMay);
            tvNgayChoi = itemView.findViewById(R.id.tvNgayChoi);
            tvSoGioChoi = itemView.findViewById(R.id.tvSoGioChoi);
            tvSoTien = itemView.findViewById(R.id.tvSoTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvOrderEmailNguoiDat = itemView.findViewById(R.id.tvOrderEmailNguoiDat);
            xacNhan = itemView.findViewById(R.id.btnXacNhanDichVu);
        }
    }

    public static class ServiceViewHolder extends RecyclerView.ViewHolder {
        public TextView tvServiceName, tvServiceType, tvSoLuong, tvSoGhe, tvSoTien, tvTrangThai, tvOrderEmailNguoiDat;
        public Button xacNhan;

        public ServiceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvServiceType = itemView.findViewById(R.id.tvServiceType);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuong);
            tvSoGhe = itemView.findViewById(R.id.tvSoGhe);
            tvSoTien = itemView.findViewById(R.id.tvSoTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            tvOrderEmailNguoiDat = itemView.findViewById(R.id.tvOrderEmailNguoiDat);
            xacNhan = itemView.findViewById(R.id.btnXacNhanDichVu);
        }
    }
}
