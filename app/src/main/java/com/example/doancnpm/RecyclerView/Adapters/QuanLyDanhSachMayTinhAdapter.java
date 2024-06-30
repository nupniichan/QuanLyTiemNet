package com.example.doancnpm.RecyclerView.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.QuanLyDanhSachMayTinhViewHolder;

import java.util.List;

public class QuanLyDanhSachMayTinhAdapter extends RecyclerView.Adapter<QuanLyDanhSachMayTinhViewHolder> {

    Context context;
    List<Computer> computers;
    // Thêm interface để xử lý sự kiện click vào nút xem, sửa, xóa
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onViewClick(int position);

        void onEditClick(int position);

        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public QuanLyDanhSachMayTinhAdapter(Context context, List<Computer> computers) {
        this.context = context;
        this.computers = computers;
    }

    @NonNull
    @Override
    public QuanLyDanhSachMayTinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyDanhSachMayTinhViewHolder(LayoutInflater.from(context).inflate(R.layout.quanly_danhsachmaytinh_view, parent, false));
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

        // Xử lý sự kiện click vào nút menu
        holder.menuButton.setOnClickListener(view -> {
            showPopupMenu(view, position);
        });
    }

    @Override
    public int getItemCount() {
        return computers.size();
    }

    // Hiển thị popup menu
    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
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
                    new AlertDialog.Builder(context)
                            .setTitle("Xóa máy tính")
                            .setMessage("Bạn có chắc chắn muốn xóa máy tính này?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // Xử lý xóa máy tính ở đây
                                    if (onItemClickListener != null) {
                                        onItemClickListener.onDeleteClick(position);
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                return true;
            }
        });
        popupMenu.show();
    }
}