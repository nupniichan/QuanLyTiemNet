package com.example.doancnpm.user.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.ComputerAdapter;
import com.example.doancnpm.RecyclerView.Adapters.ServiceAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TrangChuFragment extends Fragment {

    private TextView textViewUsername, textViewBalance;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef;
    private Button napTienButton;
    private double soDuHienTai;
    private CountDownTimer countDownTimer;
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
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        textViewUsername = view.findViewById(R.id.textView);
        textViewBalance = view.findViewById(R.id.textView2);
        napTienButton = view.findViewById(R.id.btnNapTien);

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

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            usersRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());

            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String username = dataSnapshot.child("email").getValue(String.class);
                        soDuHienTai = dataSnapshot.child("soDuTK").getValue(Double.class);

                        int index = username.indexOf('@');
                        if (index != -1) {
                            username = username.substring(0, index);
                        }

                        textViewUsername.setText("Tên tài khoản: " + username);
                        textViewBalance.setText("Số dư: " + String.format("%,.0f", soDuHienTai) + " VND");
                    } else {
                        textViewUsername.setText("Tên tài khoản: Không tìm thấy");
                        textViewBalance.setText("Số dư: Không tìm thấy");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Lỗi: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

            // Lấy dữ liệu máy tính từ Firebase
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
        } else {
            textViewUsername.setText("Tên tài khoản: Vui lòng đăng nhập");
            textViewBalance.setText("Số dư: Vui lòng đăng nhập");
        }

        napTienButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hienThiDialogNapTien();
            }
        });

        return view;
    }

    private void hienThiDialogNapTien() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_nap_tien, null);
        builder.setView(dialogView);

        EditText editTextSoTien = dialogView.findViewById(R.id.editTextSoTien);
        RadioGroup radioGroupPhuongThuc = dialogView.findViewById(R.id.radioGroupPhuongThuc);
        RadioButton radioButtonQR = dialogView.findViewById(R.id.radioButtonQR);
        RadioButton radioButtonChuyenKhoan = dialogView.findViewById(R.id.radioButtonChuyenKhoan);
        Button buttonXacNhan = dialogView.findViewById(R.id.buttonXacNhan);
        Button buttonHuy = dialogView.findViewById(R.id.buttonHuy);

        AlertDialog dialog = builder.create();

        buttonXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String soTienString = editTextSoTien.getText().toString().trim();
                if (soTienString.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
                    return;
                }

                int checkedRadioButtonId = radioGroupPhuongThuc.getCheckedRadioButtonId();
                if (checkedRadioButtonId == -1) {
                    Toast.makeText(getContext(), "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
                    return;
                }

                String phuongThuc = "";
                if (checkedRadioButtonId == R.id.radioButtonQR) {
                    phuongThuc = "Quét mã QR";
                } else if (checkedRadioButtonId == R.id.radioButtonChuyenKhoan) {
                    phuongThuc = "Chuyển khoản";
                }

                double soTien = Double.parseDouble(soTienString);
                xuLyGiaoDich(phuongThuc, soTien, dialog);
            }
        });

        buttonHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void xuLyGiaoDich(String phuongThuc, double soTien, AlertDialog previousDialog) {
        String userId = currentUser.getUid();
        DatabaseReference lichSuGiaoDichRef = FirebaseDatabase.getInstance().getReference("LichSuGiaoDich").child(userId);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String ngayNap = dateFormat.format(new Date());
        String gioNap = timeFormat.format(new Date());

        String giaoDichId = lichSuGiaoDichRef.push().getKey();

        Map<String, Object> giaoDichData = new HashMap<>();
        giaoDichData.put("ngayNap", ngayNap);
        giaoDichData.put("gioNap", gioNap);
        giaoDichData.put("soTien", soTien);
        giaoDichData.put("phuongThuc", phuongThuc);
        giaoDichData.put("trangThai", "Chờ thanh toán");

        lichSuGiaoDichRef.child(giaoDichId).setValue(giaoDichData)
                .addOnSuccessListener(aVoid -> {
                    previousDialog.dismiss();
                    if (phuongThuc.equals("Quét mã QR")) {
                        hienThiDialogQuetQR(soTien, giaoDichId);
                    } else {
                        hienThiDialogChuyenKhoan(soTien, giaoDichId);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void hienThiDialogQuetQR(double soTien, String giaoDichId) {
        AlertDialog.Builder builderQR = new AlertDialog.Builder(getContext());
        View dialogViewQR = getLayoutInflater().inflate(R.layout.dialog_quet_qr, null);
        builderQR.setView(dialogViewQR);

        ImageView imageViewQR = dialogViewQR.findViewById(R.id.imageViewQR);
        TextView textViewThoiGianConLaiQR = dialogViewQR.findViewById(R.id.textViewThoiGianConLaiQR);
        Button buttonHoanTat = dialogViewQR.findViewById(R.id.buttonHoanTat);

        // TODO: Thay thế bằng hình ảnh mã QR thực tế
        imageViewQR.setImageResource(R.drawable.maqr);

        AlertDialog dialogQR = builderQR.create();
        dialogQR.show();

        // Bắt đầu đếm ngược
        countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) { // 10 phút = 10 * 60 * 1000 milliseconds

            public void onTick(long millisUntilFinished) {
                String thoiGian = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                textViewThoiGianConLaiQR.setText("Thời gian còn lại: " + thoiGian);
            }

            public void onFinish() {
                capNhatGiaoDich(giaoDichId, "Thất bại", soTien);
                dialogQR.dismiss();
                Toast.makeText(getContext(), "Giao dịch hết hạn!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        buttonHoanTat.setOnClickListener(v -> {
            countDownTimer.cancel(); // Hủy đếm ngược
            capNhatGiaoDich(giaoDichId, "Thành công", soTien);
            dialogQR.dismiss();
        });
    }


    private void hienThiDialogChuyenKhoan(double soTien, String giaoDichId) {
        AlertDialog.Builder builderCK = new AlertDialog.Builder(getContext());
        View dialogViewCK = getLayoutInflater().inflate(R.layout.dialog_chuyen_khoan, null);
        builderCK.setView(dialogViewCK);

        TextView textViewThongTinCK = dialogViewCK.findViewById(R.id.textViewAccountNumber);
        TextView textViewThongTinCK1 = dialogViewCK.findViewById(R.id.textViewBankName);
        TextView textViewThongTinCK2 = dialogViewCK.findViewById(R.id.textViewAccountName);
        TextView textViewSoTienChuyenKhoan = dialogViewCK.findViewById(R.id.textViewAmount);
        TextView textViewThoiGianConLaiCK = dialogViewCK.findViewById(R.id.textViewThoiGianConLaiCK);
        Button buttonHoanTatCK = dialogViewCK.findViewById(R.id.buttonHoanTatCK);

        textViewSoTienChuyenKhoan.setText("Số tiền: " + String.format("%,.0f", soTien) + " VND");

        AlertDialog dialogCK = builderCK.create();
        dialogCK.show();

        // Bắt đầu đếm ngược
        countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) { // 10 phút = 10 * 60 * 1000 milliseconds

            public void onTick(long millisUntilFinished) {
                String thoiGian = String.format(Locale.getDefault(), "%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) % 60,
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) % 60);
                textViewThoiGianConLaiCK.setText("Thời gian còn lại: " + thoiGian);
            }

            public void onFinish() {
                capNhatGiaoDich(giaoDichId, "Thất bại", soTien);
                dialogCK.dismiss();
                Toast.makeText(getContext(), "Giao dịch hết hạn!", Toast.LENGTH_SHORT).show();
            }
        }.start();

        buttonHoanTatCK.setOnClickListener(v -> {
            countDownTimer.cancel(); // Hủy đếm ngược
            capNhatGiaoDich(giaoDichId, "Thành công", soTien);
            dialogCK.dismiss();
        });
    }


    private void capNhatGiaoDich(String giaoDichId, String trangThaiMoi, double soTien) {
        String userId = currentUser.getUid();
        DatabaseReference giaoDichRef = FirebaseDatabase.getInstance().getReference("LichSuGiaoDich")
                .child(userId).child(giaoDichId).child("trangThai");

        giaoDichRef.setValue(trangThaiMoi)
                .addOnSuccessListener(aVoid -> {
                    if (trangThaiMoi.equals("Thành công")) {
                        capNhatSoDu(soTien);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void capNhatSoDu(double soTienNap) {
        soDuHienTai += soTienNap;
        usersRef.child("soDuTK").setValue(soDuHienTai)
                .addOnSuccessListener(aVoid -> {
                    textViewBalance.setText("Số dư: " + String.format("%,.0f", soDuHienTai) + " VND");
                    Toast.makeText(getContext(), "Nạp tiền thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}