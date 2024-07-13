package com.example.doancnpm.user.Fragments;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.DichVu_NguoiDung_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DichVuFragment extends Fragment {
    private List<Service> services = new ArrayList<>();
    private RecyclerView recyclerView;
    private DichVu_NguoiDung_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_dich_vu, container, false);

        recyclerView = rootView.findViewById(R.id.DanhSachDichVuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DichVu_NguoiDung_Adapter(getContext(), services, this::showOrderDialog);
        recyclerView.setAdapter(adapter);

        fetchServicesFromFirebase();

        return rootView;
    }

    private void fetchServicesFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference servicesRef = database.getReference("services");

        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Service> servicesList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        String imageUrl = service.getServiceImage();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            servicesList.add(service);
                        }
                    }
                }
                updateRecyclerView(servicesList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateRecyclerView(List<Service> servicesList) {
        adapter.setServices(servicesList);
        adapter.notifyDataSetChanged();
    }

    private void showOrderDialog(Service service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_order_service, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        EditText edtSoGhe = dialogView.findViewById(R.id.edtSoGhe);
        EditText edtSoLuong = dialogView.findViewById(R.id.edtSoLuong);
        TextView txtTongTien = dialogView.findViewById(R.id.txtTongTien);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnThanhToan = dialogView.findViewById(R.id.btnThanhToan);

        // Lấy serviceName và serviceType từ Service object
        String serviceName = service.getServiceName();
        String serviceType = service.getServiceType();

        // Hiển thị serviceName và serviceType trên dialog
        TextView txtServiceName = dialogView.findViewById(R.id.txtServiceName);
        TextView txtServiceType = dialogView.findViewById(R.id.txtServiceType);

        if (txtServiceName != null) {
            txtServiceName.setText("Dịch vụ: " + serviceName);
        }

        if (txtServiceType != null) {
            txtServiceType.setText("Loại: " + serviceType);
        }

        int price = service.getPrice();

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPrice(edtSoLuong, txtTongTien, price);
            }
        };

        edtSoLuong.addTextChangedListener(textWatcher);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnThanhToan.setOnClickListener(v -> {
            String soGheString = edtSoGhe.getText().toString();
            String soLuongString = edtSoLuong.getText().toString();

            if (soGheString.isEmpty()) {
                edtSoGhe.setError("Vui lòng nhập số ghế!");
                return;
            }
            if (soLuongString.isEmpty()) {
                edtSoLuong.setError("Vui lòng nhập số lượng!");
                return;
            }

            String soGhe = soGheString;
            int soLuong = Integer.parseInt(soLuongString);
            int tongTien = price * soLuong;

            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(getContext(), "Vui lòng đăng nhập để đặt dịch vụ!", Toast.LENGTH_SHORT).show();
                return;
            }
            String userId = currentUser.getUid();

            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    if (userSnapshot.exists() &&
                            userSnapshot.hasChild("soDuTK") &&
                            userSnapshot.hasChild("email") &&
                            userSnapshot.child("soDuTK").getValue(Integer.class) >= tongTien) {

                        int newBalance = userSnapshot.child("soDuTK").getValue(Integer.class) - tongTien;
                        String userEmail = userSnapshot.child("email").getValue(String.class);

                        usersRef.child(userId).child("soDuTK").setValue(newBalance)
                                .addOnSuccessListener(aVoid ->
                                        createOrderForService(service, soGhe, soLuong, tongTien, userId, userEmail, serviceName, serviceType, dialog::dismiss))
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi khi trừ tiền: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getContext(), "Số dư không đủ!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getContext(), "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void updateTotalPrice(EditText edtSoLuong, TextView txtTongTien, int price) {
        String soLuongString = edtSoLuong.getText().toString();
        int soLuong = 0;
        if (!soLuongString.isEmpty()) {
            soLuong = Integer.parseInt(soLuongString);
        }
        int tongTien = price * soLuong;

        // Định dạng tiền tệ Việt Nam
        DecimalFormat formatter = new DecimalFormat("#,###");
        String tongTienFormatted = formatter.format(tongTien);

        txtTongTien.setText("Tổng tiền: " + tongTienFormatted + " VND");
    }

    private void createOrderForService(Service service, String soGhe, int soLuong, int tongTien, String userId, String userEmail, String serviceName, String serviceType, DichVu_NguoiDung_Adapter.OrderCreationCallback callback) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        String orderId = ordersRef.push().getKey();

        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("userEmail", userEmail);
        orderData.put("trangThai", "Chờ xác nhận");
        orderData.put("soTien", tongTien);
        orderData.put("serviceName", serviceName);
        orderData.put("serviceType", serviceType);
        orderData.put("soLuong", soLuong);
        orderData.put("soGhe", soGhe);
        orderData.put("loaiDonHang", "dichvu");  // Set order type to "dịch vụ"

        ordersRef.child(orderId).setValue(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Đặt dịch vụ thành công!", Toast.LENGTH_SHORT).show();
                    callback.onOrderCreatedSuccessfully();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Lỗi khi tạo đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

}