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
import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.MayTinh_QuanLy_ViewHolder;
import java.util.List;

public class MayTinh_QuanLy_Adapter extends RecyclerView.Adapter<MayTinh_QuanLy_ViewHolder> {

    Context context;
    List<Object> itemList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onViewClick(int position);
        void onEditClick(int position);
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MayTinh_QuanLy_Adapter(Context context, List<Object> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public MayTinh_QuanLy_ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MayTinh_QuanLy_ViewHolder(LayoutInflater.from(context).inflate(R.layout.quanly_danhsachmaytinh_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MayTinh_QuanLy_ViewHolder holder, int position) {
        Object item = itemList.get(position);
        if (item instanceof Computer) {
            Computer computer = (Computer) item;
            holder.TenMayTinh.setText(computer.getName());
            holder.CPU.setText(computer.getCpu());
            holder.GPU.setText(computer.getGpu());
            holder.RAM.setText(computer.getRam());
            holder.Monitor.setText(computer.getMonitor());
            holder.Status.setText(computer.getStatus());
            holder.SeatLocation.setText(computer.getComputerSeatLocation());

            int price = computer.getPrice();
            String formattedPrice = String.format("%,d VND", price);
            holder.Price.setText(formattedPrice);

            holder.menuButton.setOnClickListener(view -> showPopupMenu(view, position));
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
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

    public void filterList(List<Object> filteredList) {
        itemList = filteredList;
        notifyDataSetChanged();
    }
}
