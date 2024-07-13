package com.example.doancnpm.QuanLy.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Order;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.OrderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhSachDatDichVuFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;
    private List<Order> orderList;
    private RadioGroup radioGroupFilter;
    private RadioButton radioButtonAll, radioButtonPending;
    private FirebaseUser currentUser;

    public DanhSachDatDichVuFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_sach_dat_dich_vu, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        radioGroupFilter = view.findViewById(R.id.radioGroupFilter);
        radioButtonAll = view.findViewById(R.id.radioButtonAll);
        radioButtonPending = view.findViewById(R.id.radioButtonPending);
        orderList = new ArrayList<>();
        Map<Order, String> orderKeyMap = new HashMap<>();
        currentUser = FirebaseAuth.getInstance().getCurrentUser(); // Lấy người dùng hiện tại

        // Khởi tạo OrderAdapter
        orderAdapter = new OrderAdapter(getContext(), orderList, FirebaseDatabase.getInstance().getReference("Orders"), orderKeyMap);
        recyclerView.setAdapter(orderAdapter);

        loadOrdersFromFirebase(true); // Load all orders by default

        radioGroupFilter.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonAll) {
                loadOrdersFromFirebase(true); // Load all orders
            } else if (checkedId == R.id.radioButtonPending) {
                loadOrdersFromFirebase(false); // Load pending orders
            }
        });

        return view;
    }

    private void loadOrdersFromFirebase(boolean loadAll) {
        Query query = loadAll ? FirebaseDatabase.getInstance().getReference("Orders") :
                FirebaseDatabase.getInstance().getReference("Orders").orderByChild("trangThai").equalTo("Chờ xác nhận");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderList.clear();
                orderAdapter.getOrderKeyMap().clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Order order = snapshot.getValue(Order.class);
                    if (order != null && order.getUserEmail().equals(currentUser.getEmail())) {
                        orderList.add(order);
                        orderAdapter.getOrderKeyMap().put(order, snapshot.getKey());
                    }
                }
                orderAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
