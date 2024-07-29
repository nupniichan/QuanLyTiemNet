package com.example.doancnpm.RecyclerView.QuanLyGroup;

import android.os.Handler;
import android.os.Looper;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComputerListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_GROUP = 0;
    private static final int VIEW_TYPE_COMPUTER = 1;

    private List<Object> itemList;
    private QuanLyMayTinh fragment;

    // Biến để theo dõi trạng thái lọc của từng nhóm
    private Map<String, List<Computer>> filteredComputersMap = new HashMap<>();
    private Map<String, Integer> selectedFilterMap = new HashMap<>();

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

                // Đặt RadioButton mặc định là giá trị được lưu trong selectedFilterMap
                Integer selectedFilter = selectedFilterMap.get(group.getGroupName());
                if (selectedFilter == null || selectedFilter == R.id.all_computers_radio) {
                    ((ComputerGroupViewHolder) holder).statusRadioGroup.check(R.id.all_computers_radio);
                } else if (selectedFilter == R.id.available_computers_radio) {
                    ((ComputerGroupViewHolder) holder).statusRadioGroup.check(R.id.available_computers_radio);
                }

                // Xử lý đóng/mở nhóm
                ((ComputerGroupViewHolder) holder).itemView.setOnClickListener(v -> {
                    int pos = holder.getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        ComputerGroup grp = (ComputerGroup) itemList.get(pos);
                        grp.setExpanded(!grp.isExpanded());
                        notifyItemChanged(pos);
                        if (grp.isExpanded()) {
                            int nextPosition = pos + 1;
                            List<Computer> computersToShow = filteredComputersMap.containsKey(grp.getGroupName())
                                    ? filteredComputersMap.get(grp.getGroupName())
                                    : grp.getComputers();
                            itemList.addAll(nextPosition, computersToShow);
                            notifyItemRangeInserted(nextPosition, computersToShow.size());
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
                    selectedFilterMap.put(group.getGroupName(), checkedId);
                    if (checkedId == R.id.available_computers_radio) {
                        // Lọc theo trạng thái máy
                        filterComputersByStatus(group.getGroupName(), "Con trong");
                    } else if (checkedId == R.id.all_computers_radio) {
                        // Hiển thị tất cả máy tính trong nhóm
                        showAllComputersInGroup(group.getGroupName());
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

    private void filterComputersByStatus(String groupName, String status) {
        ComputerGroup group = null;
        for (Object item : itemList) {
            if (item instanceof ComputerGroup && ((ComputerGroup) item).getGroupName().equals(groupName)) {
                group = (ComputerGroup) item;
                break;
            }
        }
        if (group != null) {
            List<Computer> filteredComputers = new ArrayList<>();
            for (Computer computer : group.getComputers()) {
                if (computer.getStatus().equals(status)) {
                    filteredComputers.add(computer);
                }
            }
            filteredComputersMap.put(groupName, filteredComputers);
            updateGroupComputers(group, filteredComputers);
        }
    }

    private void showAllComputersInGroup(String groupName) {
        ComputerGroup group = null;
        for (Object item : itemList) {
            if (item instanceof ComputerGroup && ((ComputerGroup) item).getGroupName().equals(groupName)) {
                group = (ComputerGroup) item;
                break;
            }
        }
        if (group != null) {
            int groupPosition = itemList.indexOf(group);
            if (groupPosition != -1) {
                // Xóa các phần tử hiện tại trong nhóm
                int nextPosition = groupPosition + 1;
                while (nextPosition < itemList.size() && itemList.get(nextPosition) instanceof Computer) {
                    itemList.remove(nextPosition);
                }

                // Thêm lại tất cả các máy tính trong nhóm
                itemList.addAll(nextPosition, group.getComputers());

                // Sử dụng Handler để trì hoãn notifyDataSetChanged()
                new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
            }
        }
    }

    private void updateGroupComputers(ComputerGroup group, List<Computer> computersToShow) {
        int groupPosition = itemList.indexOf(group);
        int nextPosition = groupPosition + 1;
        while (nextPosition < itemList.size() && itemList.get(nextPosition) instanceof Computer) {
            itemList.remove(nextPosition);
        }
        itemList.addAll(nextPosition, computersToShow);

        // Sử dụng Handler để trì hoãn notifyDataSetChanged()
        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());
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
