package com.example.doancnpm.user.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.Order;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.OrderAdapterKH;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DanhSachDichVuDaDat extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapterKH orderAdapterKH;
    private List<Order> orderList;
    private DatabaseReference databaseReferenceOrder, databaseReferenceComputer, databaseReferenceService;
    private FirebaseUser currentUser;
    private Map<Order, String> orderKeyMap = new HashMap<>();

    public DanhSachDichVuDaDat() {
        // Required empty public constructor
    }

    public static DanhSachDichVuDaDat newInstance() {
        return new DanhSachDichVuDaDat();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_danh_sach_dich_vu_da_dat, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewDVDaDat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        orderAdapterKH = new OrderAdapterKH(getContext(), orderList, databaseReferenceOrder, orderKeyMap);
        recyclerView.setAdapter(orderAdapterKH);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReferenceOrder = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReferenceComputer = FirebaseDatabase.getInstance().getReference("Computer");
        databaseReferenceService = FirebaseDatabase.getInstance().getReference("Service");

        if (currentUser != null) {
            loadOrdersFromFirebase();
        } else {
            Toast.makeText(getContext(), "Vui lòng đăng nhập để xem danh sách dịch vụ", Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private void loadOrdersFromFirebase() {
        databaseReferenceOrder.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshotOrder) {
                orderList.clear();
                Log.d("FirebaseData", "Number of orders: " + dataSnapshotOrder.getChildrenCount());
                for (DataSnapshot snapshotOrder : dataSnapshotOrder.getChildren()) {
                    Order order = snapshotOrder.getValue(Order.class);

                    if (order != null) {
                        Log.d("FirebaseData", "Order email: " + order.getUserEmail());
                        if (order.getUserEmail().equals(currentUser.getEmail())) {
                            orderKeyMap.put(order, snapshotOrder.getKey());
                            Log.d("FirebaseData", "Order added: " + order.getLoaiDonHang());

                            orderList.add(order); // Ensure this is outside the conditional block if not specific to type
                        }
                    }
                }
                if (orderList.isEmpty()) {
                    Log.d("FirebaseData", "No orders matched or list is empty");
                } else {
                    orderAdapterKH.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseData", "Error loading data: " + databaseError.getMessage());
            }
        });
    }
}