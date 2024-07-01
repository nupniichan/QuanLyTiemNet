package com.example.doancnpm.RecyclerView.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.QuanLyDanhSachDichVuViewHolder;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class QuanLyDanhSachDichVuAdapter extends RecyclerView.Adapter<QuanLyDanhSachDichVuViewHolder> {

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

    public QuanLyDanhSachDichVuAdapter(Activity activityContext, List<Service> services) {
        this.activityContext = activityContext;
        this.services = services;
    }

    @NonNull
    @Override
    public QuanLyDanhSachDichVuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyDanhSachDichVuViewHolder(LayoutInflater.from(activityContext).inflate(R.layout.quanly_danhsachdichvu_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyDanhSachDichVuViewHolder holder, int position) {
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