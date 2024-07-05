package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.DanhSachMayTinhViewHolder;
import com.example.doancnpm.user.UserGroup.ComputerUserGroupViewHolder;

import java.util.ArrayList;
import java.util.List;

public class MayTinh_NguoiDung_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_COMPUTER = 0;
    private static final int VIEW_TYPE_GROUP = 1;

    private Context context;
    private List<Object> items;
    private OnGroupClickListener groupClickListener;
    public interface OnGroupClickListener {
        void onGroupClick(int position);
    }
    public MayTinh_NguoiDung_Adapter(Context context, List<Object> items, OnGroupClickListener listener) {
        this.context = context;
        this.items = items;
        this.groupClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Computer) {
            return VIEW_TYPE_COMPUTER;
        } else if (items.get(position) instanceof ComputerGroup) {
            return VIEW_TYPE_GROUP;
        } else {
            throw new IllegalArgumentException("Invalid item type");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COMPUTER) {
            return new DanhSachMayTinhViewHolder(LayoutInflater.from(context).inflate(R.layout.danhsachmaytinh_view, parent, false));
        } else {
            return new ComputerUserGroupViewHolder(LayoutInflater.from(context).inflate(R.layout.computer_user_group_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DanhSachMayTinhViewHolder) {
            // Bind data for Computer items
            Computer computer = (Computer) items.get(position);
            ((DanhSachMayTinhViewHolder) holder).TenMayTinh.setText(computer.getName());
            ((DanhSachMayTinhViewHolder) holder).CPU.setText(computer.getCpu());
            ((DanhSachMayTinhViewHolder) holder).GPU.setText(computer.getGpu());
            ((DanhSachMayTinhViewHolder) holder).RAM.setText(computer.getRam());
            ((DanhSachMayTinhViewHolder) holder).Monitor.setText(computer.getMonitor());
            int price = computer.getPrice();
            String formattedPrice = String.format("%,d VND", price);
            ((DanhSachMayTinhViewHolder) holder).Price.setText(formattedPrice);

        } else if (holder instanceof ComputerUserGroupViewHolder) {
            ComputerGroup group = (ComputerGroup) items.get(position);
            ((ComputerUserGroupViewHolder) holder).groupNameTextView.setText(group.getGroupName());

            RadioGroup radioGroup = ((ComputerUserGroupViewHolder) holder).statusRadioGroup;
            radioGroup.setOnCheckedChangeListener(null);

            radioGroup.check(R.id.all_computers_radio);

            // Use a different variable name in the lambda (e.g., 'viewGroup'):
            radioGroup.setOnCheckedChangeListener((viewGroup, checkedId) -> {
                if (checkedId == R.id.available_computers_radio) {
                    // Loc theo tinh trang may
                    filterComputersByStatus(position, "CON TRONG");
                } else if (checkedId == R.id.all_computers_radio) {
                    showAllComputersInGroup(position);
                }
            });
        }
    }
    private void filterComputersByStatus(int groupPosition, String status) {
        ComputerGroup group = (ComputerGroup) items.get(groupPosition);
        List<Computer> filteredComputers = new ArrayList<>();
        for (Computer computer : group.getComputers()) {
            if (computer.getStatus().equals(status)) {
                filteredComputers.add(computer);
            }
        }

        // Remove old computers
        int startIndex = groupPosition + 1;
        int oldCount = getItemCount() - startIndex;
        items.subList(startIndex, startIndex + oldCount).clear();
        notifyItemRangeRemoved(startIndex, oldCount);

        // Add new computers
        items.addAll(startIndex, filteredComputers);
        notifyItemRangeInserted(startIndex, filteredComputers.size());
    }

    private void showAllComputersInGroup(int groupPosition) {
        ComputerGroup group = (ComputerGroup) items.get(groupPosition);

        // Update the itemList and notify the adapter
        int startIndex = groupPosition + 1;
        int currentCount = getItemCount() - startIndex;
        items.subList(startIndex, startIndex + currentCount).clear();
        items.addAll(startIndex, group.getComputers());
        notifyItemRangeChanged(startIndex, getItemCount() - startIndex);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
}