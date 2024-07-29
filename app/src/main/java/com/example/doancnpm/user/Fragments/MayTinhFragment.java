package com.example.doancnpm.user.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.MayTinh_NguoiDung_Adapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MayTinhFragment extends Fragment {
    private List<Object> itemList = new ArrayList<>();
    private Map<String, List<Computer>> originalComputerMap = new HashMap<>();
    private RecyclerView recyclerView;
    private MayTinh_NguoiDung_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_may_tinh, container, false);

        recyclerView = rootView.findViewById(R.id.DanhSachMayTinhRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new MayTinh_NguoiDung_Adapter(getContext(), itemList, this::onGroupClick);
        recyclerView.setAdapter(adapter);

        fetchComputersFromFirebase();

        return rootView;
    }

    private void fetchComputersFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("computers");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                itemList.clear();
                originalComputerMap.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Computer computer = snapshot.getValue(Computer.class);
                    if (computer != null) {
                        Log.d("MayTinhFragment", "Fetched computer: " + computer.getName());
                        String groupName = computer.getLoaiMayTinh();
                        if (!originalComputerMap.containsKey(groupName)) {
                            originalComputerMap.put(groupName, new ArrayList<>());
                        }
                        originalComputerMap.get(groupName).add(computer);
                    } else {
                        Log.e("MayTinhFragment", "Fetched computer is null");
                    }
                }

                updateItemList(originalComputerMap);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateItemList(Map<String, List<Computer>> computerMap) {
        itemList.clear();
        for (Map.Entry<String, List<Computer>> entry : computerMap.entrySet()) {
            ComputerGroup group = new ComputerGroup(entry.getKey(), entry.getValue());
            itemList.add(group);
            group.setExpanded(true);
            itemList.addAll(group.getComputers());
        }
    }

    private void onGroupClick(int position) {
        if (position < 0 || position >= itemList.size()) {
            Log.e("MayTinhFragment", "Invalid position: " + position);
            return;
        }

        Object item = itemList.get(position);
        if (!(item instanceof ComputerGroup)) {
            Log.e("MayTinhFragment", "Item at position is not a ComputerGroup: " + position);
            return;
        }

        ComputerGroup group = (ComputerGroup) item;

        group.setExpanded(!group.isExpanded());

        if (group.isExpanded()) {
            showAllComputersInGroup(group);
        } else {
            hideAllComputersInGroup(group);
        }
    }

    private void showAllComputersInGroup(ComputerGroup group) {
        int position = itemList.indexOf(group);
        if (position < 0) return;

        int startIndex = position + 1;
        List<Computer> computers = group.getComputers();
        if (computers != null && !computers.isEmpty()) {
            itemList.addAll(startIndex, computers);
            adapter.notifyItemRangeInserted(startIndex, computers.size());
        } else {
            Log.e("MayTinhFragment", "Computers list is null or empty for group: " + group.getGroupName());
        }
    }

    private void hideAllComputersInGroup(ComputerGroup group) {
        int position = itemList.indexOf(group);
        if (position < 0) return;

        int count = group.getComputers() != null ? group.getComputers().size() : 0;
        if (count > 0) {
            itemList.subList(position + 1, position + 1 + count).clear();
            adapter.notifyItemRangeRemoved(position + 1, count);
        }
    }
}
