package com.example.doancnpm.RecyclerView.QuanLyGroup;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.QuanLy.Fragments.QuanLyMayTinh;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.MayTinh_QuanLy_ViewHolder;

import java.util.ArrayList;
import java.util.List;

public class ComputerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_GROUP = 0;
    private static final int VIEW_TYPE_COMPUTER = 1;

    private List<Object> itemList;
    private QuanLyMayTinh fragment;

    public ComputerListAdapter(List<Object> itemList, QuanLyMayTinh fragment) {
        this.itemList = itemList;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        if (itemList.get(position) instanceof ComputerGroup) {
            return VIEW_TYPE_GROUP;
        } else if (itemList.get(position) instanceof Computer) {
            return VIEW_TYPE_COMPUTER;
        } else {
            throw new IllegalArgumentException("Invalid item type");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                View groupView = inflater.inflate(R.layout.computer_group_item, parent, false);
                return new ComputerGroupViewHolder(groupView);
            case VIEW_TYPE_COMPUTER:
                View computerView = inflater.inflate(R.layout.quanly_danhsachmaytinh_view, parent, false);
                return new MayTinh_QuanLy_ViewHolder(computerView);
            default:
                throw new IllegalArgumentException("Invalid view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case VIEW_TYPE_GROUP:
                ComputerGroup group = (ComputerGroup) itemList.get(position);
                ((ComputerGroupViewHolder) holder).bind(group);

                // Xử lý đóng/mở nhóm
                ((ComputerGroupViewHolder) holder).itemView.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        ComputerGroup grp = (ComputerGroup) itemList.get(pos);
                        grp.setExpanded(!grp.isExpanded());
                        notifyItemChanged(pos);
                        if (grp.isExpanded()) {
                            int nextPosition = pos + 1;
                            itemList.addAll(nextPosition, grp.getComputers());
                            notifyItemRangeInserted(nextPosition, grp.getComputers().size());
                        } else {
                            int nextPosition = pos + 1;
                            int itemCount = grp.getComputers().size();
                            itemList.subList(nextPosition, nextPosition + itemCount).clear();
                            notifyItemRangeRemoved(nextPosition, itemCount);
                        }
                    }
                });

                // Xử lý sự kiện khi chọn trạng thái
                RadioGroup radioGroup = ((ComputerGroupViewHolder) holder).statusRadioGroup;
                radioGroup.setOnCheckedChangeListener((viewGroup, checkedId) -> {
                    if (checkedId == R.id.available_computers_radio) {
                        // Lọc theo trạng thái máy
                        filterComputersByStatus(position, "Con trong");
                    } else if (checkedId == R.id.all_computers_radio) {
                        // Hiển thị tất cả máy tính trong nhóm
                        showAllComputersInGroup(position);
                    }
                });
                break;

            case VIEW_TYPE_COMPUTER:
                Computer computer = (Computer) itemList.get(position);

                // Kiểm tra null trước khi bind
                if (computer != null) {
                    ((MayTinh_QuanLy_ViewHolder) holder).bind(computer);
                } else {
                    // Xử lý trường hợp computer là null, ví dụ: log lỗi
                    Log.e("ComputerListAdapter", "Computer at position " + position + " is null!");
                }

                ((MayTinh_QuanLy_ViewHolder) holder).menuButton.setOnClickListener(view -> showPopupMenu(view, position));
                break;
        }
    }
    private void filterComputersByStatus(int groupPosition, String status) {
        ComputerGroup group = (ComputerGroup) itemList.get(groupPosition);
        List<Computer> filteredComputers = new ArrayList<>();
        for (Computer computer : group.getComputers()) {
            if (computer.getStatus().equals(status)) {
                filteredComputers.add(computer);
            }
        }
        // Thay đổi ở đây
        itemList = new ArrayList<>(itemList.subList(0, groupPosition + 1)); // giữ lại phần tử group
        itemList.addAll(filteredComputers);
        notifyDataSetChanged();
    }

    private void showAllComputersInGroup(int groupPosition) {
        ComputerGroup group = (ComputerGroup) itemList.get(groupPosition);
        // Thay đổi ở đây
        itemList = new ArrayList<>(itemList.subList(0, groupPosition + 1));
        itemList.addAll(group.getComputers());
        notifyDataSetChanged();
    }

    // Phương thức cập nhật danh sách máy tính trong nhóm
    private void updateComputersInGroup(int groupPosition, List<Computer> newComputers) {
        int startIndex = groupPosition + 1;
        int oldCount = getItemCount() - startIndex;
        itemList.subList(startIndex, startIndex + oldCount).clear();
        itemList.addAll(startIndex, newComputers);
        notifyItemRangeChanged(startIndex, getItemCount() - startIndex);
    }

    private void showPopupMenu(View view, int position) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.computer_item_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.menu_view) {
                    Computer computer = (Computer) itemList.get(position);
                    fragment.showComputerDetailsDialog(computer);
                } else if (id == R.id.menu_edit) {
                    Computer computer = (Computer) itemList.get(position);
                    fragment.showEditComputerDialog(computer);
                } else if (id == R.id.menu_delete) {
                    fragment.deleteComputer(position);
                }
                return true;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}