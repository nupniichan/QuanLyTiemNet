package com.example.doancnpm.QuanLy.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.ComputerAdapter;
import com.example.doancnpm.RecyclerView.Adapters.ServiceAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TrangChuQuanLy extends Fragment {
    // RecyclerView cho máy tính
    private RecyclerView recyclerViewComputers;
    private ComputerAdapter computerAdapter;
    private List<Computer> computerList;

    // RecyclerView cho dịch vụ
    private RecyclerView recyclerViewServices;
    private ServiceAdapter serviceAdapter;
    private List<Service> serviceList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu_quan_ly, container, false);
        Toolbar toolBarTrangChu = view.findViewById(R.id.ToolbarChuTiemQuanLyTrangChuToolBar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolBarTrangChu);
        // Khởi tạo RecyclerView cho máy tính
        recyclerViewComputers = view.findViewById(R.id.recyclerViewCacLoaiMay);
        recyclerViewComputers.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        computerList = new ArrayList<>();
        computerAdapter = new ComputerAdapter(getContext(), computerList);
        recyclerViewComputers.setAdapter(computerAdapter);

        // Khởi tạo RecyclerView cho dịch vụ
        recyclerViewServices = view.findViewById(R.id.recyclerViewCacLoaiDichVu);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        serviceList = new ArrayList<>();
        serviceAdapter = new ServiceAdapter(getContext(), serviceList);
        recyclerViewServices.setAdapter(serviceAdapter);

        DatabaseReference computersRef = FirebaseDatabase.getInstance().getReference("computers");
        computersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                computerList.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot computerSnapshot : dataSnapshot.getChildren()) {
                    Computer computer = computerSnapshot.getValue(Computer.class);
                    computerList.add(computer);
                }

                // Xáo trộn danh sách ngẫu nhiên
                Collections.shuffle(computerList);

                // Lấy tối đa 5 phần tử
                if (computerList.size() > 5) {
                    computerList = computerList.subList(0, 5);
                }

                computerAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Lấy dữ liệu dịch vụ từ Firebase
        DatabaseReference servicesRef = FirebaseDatabase.getInstance().getReference("services");
        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                serviceList.clear(); // Xóa dữ liệu cũ
                for (DataSnapshot serviceSnapshot : dataSnapshot.getChildren()) {
                    Service service = serviceSnapshot.getValue(Service.class);
                    serviceList.add(service);
                }

                // Xáo trộn danh sách ngẫu nhiên
                Collections.shuffle(serviceList);

                // Lấy tối đa 5 phần tử
                if (serviceList.size() > 5) {
                    serviceList = serviceList.subList(0, 5);
                }

                serviceAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
      
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChuChuTiem_Person) {
            Toast.makeText(getActivity(), "Quản lý thông tin", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.ToolBarTrangChuChuTiem_Notification) {
            Toast.makeText(getActivity(), "Thông báo", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}