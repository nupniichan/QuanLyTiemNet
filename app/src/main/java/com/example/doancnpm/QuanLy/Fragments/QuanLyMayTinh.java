package com.example.doancnpm.QuanLy.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.QuanLyGroup.ComputerListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuanLyMayTinh extends Fragment {

    private RecyclerView recyclerView;
    private ComputerListAdapter adapter;
    private List<Object> itemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_may_tinh, container, false);
        Toolbar toolBarTrangChu = view.findViewById(R.id.ToolbarChuTiemQuanLyMayTinhToolBar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolBarTrangChu);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.QuanLyMayTinhRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ComputerListAdapter(itemList, this);
        recyclerView.setAdapter(adapter);

        fetchComputersFromFirebase();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_chutiem_quanlymaytinh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChuChuTiem_MayTinh_Add) {
            Toast.makeText(getActivity(), "Thêm máy tính", Toast.LENGTH_SHORT).show();
            showAddComputerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddComputerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thêm máy tính");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_computer_dialog_layout, null);
        builder.setView(view);

        EditText idEditText = view.findViewById(R.id.IdComputerEditText);
        EditText nameEditText = view.findViewById(R.id.ComputerNameEditTextID);
        EditText loaiMayTinhEditText = view.findViewById(R.id.ComputerTypeEditTextID);
        EditText cpuEditText = view.findViewById(R.id.CPUEditTextID);
        EditText gpuEditText = view.findViewById(R.id.GPUEditTextID);
        EditText ramEditText = view.findViewById(R.id.RAMEditTextID);
        EditText monitorEditText = view.findViewById(R.id.MonitorEditTextID);
        EditText priceEditText = view.findViewById(R.id.PriceEditTextID);
        EditText seatLocationEditText = view.findViewById(R.id.SeatLocationEditTextID);
        EditText statusEditText = view.findViewById(R.id.computerStatusEditTextID);
        Button addButton = view.findViewById(R.id.btnThemMayTInhID);
        AlertDialog dialog = builder.create();
        dialog.show();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String loaiMayTinh = loaiMayTinhEditText.getText().toString();
                String cpu = cpuEditText.getText().toString();
                String gpu = gpuEditText.getText().toString();
                String ram = ramEditText.getText().toString();
                String monitor = monitorEditText.getText().toString();
                String priceString = priceEditText.getText().toString();
                Integer price = Integer.parseInt(priceString);
                String seatLocation = seatLocationEditText.getText().toString();
                String status = statusEditText.getText().toString();
                Computer computer = new Computer(id, name, loaiMayTinh, cpu, gpu, ram, monitor, price, seatLocation, status);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("computers");

                myRef.child(id).setValue(computer).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Thêm máy tính thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Thêm máy tính thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
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


    public void showComputerDetailsDialog(Computer computer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông tin máy tính");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.computer_details_dialog, null);
        builder.setView(view);

        // Lấy các TextView từ layout chi tiết máy tính
        TextView idTextView = view.findViewById(R.id.detail_id);
        TextView nameTextView = view.findViewById(R.id.detail_name);
        TextView typeTextView = view.findViewById(R.id.detail_type);
        TextView cpuTextView = view.findViewById(R.id.detail_cpu);
        TextView gpuTextView = view.findViewById(R.id.detail_gpu);
        TextView ramTextView = view.findViewById(R.id.detail_ram);
        TextView monitorTextView = view.findViewById(R.id.detail_monitor);
        TextView priceTextView = view.findViewById(R.id.detail_price);
        TextView seatTextView = view.findViewById(R.id.detail_seat);

        // Đổ dữ liệu máy tính vào các TextView
        idTextView.setText(computer.getId());
        nameTextView.setText(computer.getName());
        typeTextView.setText(computer.getLoaiMayTinh());
        cpuTextView.setText(computer.getCpu());
        gpuTextView.setText(computer.getGpu());
        ramTextView.setText(computer.getRam());
        monitorTextView.setText(computer.getMonitor());
        priceTextView.setText(String.format("%,d VND", computer.getPrice()));
        seatTextView.setText(computer.getComputerSeatLocation());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showEditComputerDialog(Computer computer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sửa thông tin máy tính");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_computer_dialog_layout, null);
        builder.setView(view);

        EditText idEditText = view.findViewById(R.id.IdComputerEditText);
        EditText nameEditText = view.findViewById(R.id.ComputerNameEditTextID);
        EditText loaiMayTinhEditText = view.findViewById(R.id.ComputerTypeEditTextID);
        EditText cpuEditText = view.findViewById(R.id.CPUEditTextID);
        EditText gpuEditText = view.findViewById(R.id.GPUEditTextID);
        EditText ramEditText = view.findViewById(R.id.RAMEditTextID);
        EditText monitorEditText = view.findViewById(R.id.MonitorEditTextID);
        EditText priceEditText = view.findViewById(R.id.PriceEditTextID);
        EditText seatLocationEditText = view.findViewById(R.id.SeatLocationEditTextID);
        EditText statusEditText = view.findViewById(R.id.computerStatusEditTextID);
        Button updateButton = view.findViewById(R.id.btnThemMayTInhID);
        updateButton.setText("Lưu thay đổi");

        // Đổ dữ liệu của máy tính vào các trường EditText
        idEditText.setText(computer.getId());
        idEditText.setEnabled(false);
        nameEditText.setText(computer.getName());
        loaiMayTinhEditText.setText(computer.getLoaiMayTinh());
        cpuEditText.setText(computer.getCpu());
        gpuEditText.setText(computer.getGpu());
        ramEditText.setText(computer.getRam());
        monitorEditText.setText(computer.getMonitor());
        priceEditText.setText(String.valueOf(computer.getPrice()));
        statusEditText.setText(computer.getStatus());
        seatLocationEditText.setText(computer.getComputerSeatLocation());

        AlertDialog dialog = builder.create();
        dialog.show();

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newId = idEditText.getText().toString();
                String newName = nameEditText.getText().toString();
                String newLoaiMayTinh = loaiMayTinhEditText.getText().toString();
                String newCpu = cpuEditText.getText().toString();
                String newGpu = gpuEditText.getText().toString();
                String newRam = ramEditText.getText().toString();
                String newMonitor = monitorEditText.getText().toString();
                String status = statusEditText.getText().toString();
                int newPrice = Integer.parseInt(priceEditText.getText().toString());
                String newSeatLocation = seatLocationEditText.getText().toString();

                // Tạo một đối tượng Computer mới với thông tin đã cập nhật
                Computer updatedComputer = new Computer(newId, newName, newLoaiMayTinh, newCpu, newGpu, newRam, newMonitor, newPrice, newSeatLocation, status);
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("computers");
                myRef.child(computer.getId()).setValue(updatedComputer)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // Cập nhật thành công, đóng dialog
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Cập nhật máy tính thành công", Toast.LENGTH_SHORT).show();
                                } else {
                                    // Xử lý lỗi
                                    Toast.makeText(getActivity(), "Cập nhật máy tính thất bại", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    public void deleteComputer(int position) {
        Object item = itemList.get(position);
        if (item instanceof Computer) {
            Computer computer = (Computer) item;
            // Sử dụng requireActivity() để lấy Context của Activity
            Activity activity = requireActivity();

            activity.runOnUiThread(() -> {
                if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Xác nhận xóa")
                            .setMessage("Bạn có chắc chắn muốn xóa " + computer.getName() + "?")
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                // Xóa dữ liệu trên Firebase
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                DatabaseReference myRef = database.getReference("computers").child(computer.getId());
                                myRef.removeValue()
                                        .addOnSuccessListener(aVoid -> {
                                            // Xóa item khỏi danh sách trong Fragment
                                            itemList.remove(position);

                                            // Cập nhật UI thông qua Adapter
                                            adapter.notifyItemRemoved(position);

                                            // Hiển thị Toast
                                            Toast.makeText(activity, "Xóa " + computer.getName() + " thành công", Toast.LENGTH_SHORT).show();
                                        })
                                        .addOnFailureListener(e -> {
                                            // Xử lý lỗi
                                            Toast.makeText(activity, "Xóa " + computer.getName() + " thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        });
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
    }
}