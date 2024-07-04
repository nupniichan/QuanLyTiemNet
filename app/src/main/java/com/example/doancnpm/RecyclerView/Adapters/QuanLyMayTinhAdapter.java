package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.QuanLyMayTinhViewHolder;
import java.util.List;

public class QuanLyMayTinhAdapter extends RecyclerView.Adapter<QuanLyMayTinhViewHolder> {

    Context context;
    List<Computer> computers;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public QuanLyMayTinhAdapter(Context context, List<Computer> computers) {
        this.context = context;
        this.computers = computers;
    }

    @NonNull
    @Override
    public QuanLyMayTinhViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new QuanLyMayTinhViewHolder(LayoutInflater.from(context).inflate(R.layout.quanly_danhsachmaytinh_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuanLyMayTinhViewHolder holder, int position) {
        holder.TenMayTinh.setText(computers.get(position).getName());
        holder.CPU.setText(computers.get(position).getCpu());
        holder.GPU.setText(computers.get(position).getGpu());
        holder.RAM.setText(computers.get(position).getRam());
        holder.Monitor.setText(computers.get(position).getMonitor());
        holder.Status.setText(computers.get(position).getStatus());
        holder.SeatLocation.setText(computers.get(position).getComputerSeatLocation());

        int price = computers.get(position).getPrice();
        String formattedPrice = String.format("%,d VND", price);
        holder.Price.setText(formattedPrice);

        holder.menuButton.setOnClickListener(view -> showPopupMenu(view, position));
    }

    @Override
    public int getItemCount() {
        return computers.size();
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.computer_item_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onViewClick(position);
                    }
                } else if (id == R.id.menu_edit) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onEditClick(position);
                    }
                } else if (id == R.id.menu_delete) {
                    onItemClickListener.onDeleteClick(position);
                }
                return true;
            }
        });
        popupMenu.show();
    }
}