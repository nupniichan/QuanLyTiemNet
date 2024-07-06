package com.example.doancnpm.RecyclerView.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.DichVu_QuanLy_ViewHolder;

import java.util.List;

public class DichVu_QuanLy_Adapter extends RecyclerView.Adapter<DichVu_QuanLy_ViewHolder> {

    private static final String TAG = "ServiceAdapter";
    private Activity activityContext;
    private List<Service> services;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public DichVu_QuanLy_Adapter(Activity activityContext, List<Service> services) {
        this.activityContext = activityContext;
        this.services = services;
    }

    @NonNull
    @Override
    public DichVu_QuanLy_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DichVu_QuanLy_ViewHolder(LayoutInflater.from(activityContext).inflate(R.layout.quanly_danhsachdichvu_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DichVu_QuanLy_ViewHolder holder, int position) {
        Service service = services.get(position);

        holder.TenDichVu.setText(service.getServiceName());
        holder.ServiceName.setText(service.getServiceName());
        holder.LoaiDichVu.setText(service.getServiceType());
        holder.MoTa.setText(service.getMoTa());

        Glide.with(activityContext)
                .load(service.getServiceImage())
                .into(holder.HinhAnh);

        int price = service.getPrice();
        String formattedPrice = String.format("%,d VND", price);
        holder.Price.setText(formattedPrice);

        holder.menuButton.setOnClickListener(view -> {
            showPopupMenu(view, position);
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public void setServices(List<Service> services) {
        this.services = services;
        notifyDataSetChanged();
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(activityContext, view);
        popupMenu.inflate(R.menu.computer_item_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_view){
                    if (onItemClickListener != null) {
                        onItemClickListener.onViewClick(position);
                    }
                }
                if (id == R.id.menu_edit){
                    if (onItemClickListener != null) {
                        onItemClickListener.onEditClick(position);
                    }
                }
                if (id == R.id.menu_delete){
                    onItemClickListener.onDeleteClick(position);
                }
                return true;
            }
        });
        popupMenu.show();
    }
}