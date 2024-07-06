package com.example.doancnpm.user.Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.ComputerGroup; // Make sure you have this class
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
    private List<Object> itemList = new ArrayList<>(); // Changed to List<Object>
    private RecyclerView recyclerView;
    private MayTinh_NguoiDung_Adapter adapter; // You might need to adjust this adapter

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

                Map<String, List<Computer>> computerMap = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Computer computer = snapshot.getValue(Computer.class);
                    String groupName = computer.getLoaiMayTinh();
                    if (!computerMap.containsKey(groupName)) {
                        computerMap.put(groupName, new ArrayList<>());
                    }
                    computerMap.get(groupName).add(computer);
                }

                for (Map.Entry<String, List<Computer>> entry : computerMap.entrySet()) {
                    ComputerGroup group = new ComputerGroup(entry.getKey(), entry.getValue());
                    itemList.add(group);
                    group.setExpanded(true);
                    itemList.addAll(group.getComputers());
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý lỗi
                Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void onGroupClick(int position) {
        // 1. Get the clicked group
        Object item = itemList.get(position);
        if (!(item instanceof ComputerGroup)) return; // Return if not a group
        ComputerGroup group = (ComputerGroup) item;

        // 2. Toggle the expansion state
        group.setExpanded(!group.isExpanded());

        // 3. Update the adapter
        if (group.isExpanded()) {
            // Expand: Add computers of this group below the group header
            itemList.addAll(position + 1, group.getComputers());
            adapter.notifyItemRangeInserted(position + 1, group.getComputers().size());
        } else {
            // Collapse: Remove computers of this group
            int count = group.getComputers().size();
            for (int i = 0; i < count; i++) {
                itemList.remove(position + 1);
            }
            adapter.notifyItemRangeRemoved(position + 1, count);
        }
    }
}