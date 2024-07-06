package com.example.doancnpm.user.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;
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
import com.example.doancnpm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TrangChuFragment extends Fragment {

    private TextView textViewUsername, textViewBalance;
    private FirebaseUser currentUser;
    private DatabaseReference usersRef;
    private Button napTienButton;
    private double soDuHienTai;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trang_chu, container, false);

        textViewUsername = view.findViewById(R.id.textView);
        textViewBalance = view.findViewById(R.id.textView2);
        napTienButton = view.findViewById(R.id.btnNapTien);

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
        Button buttonHoanTat = dialogViewQR.findViewById(R.id.buttonHoanTat);
        // TODO: Thay thế bằng hình ảnh mã QR thực tế
        imageViewQR.setImageResource(R.drawable.maqr);

        AlertDialog dialogQR = builderQR.create();
        dialogQR.show();

        buttonHoanTat.setOnClickListener(v -> {
            capNhatGiaoDich(giaoDichId, "Thành công", soTien);
            dialogQR.dismiss();
        });
    }

    private void hienThiDialogChuyenKhoan(double soTien, String giaoDichId) {
        AlertDialog.Builder builderCK = new AlertDialog.Builder(getContext());
        View dialogViewCK = getLayoutInflater().inflate(R.layout.dialog_chuyen_khoan, null);
        builderCK.setView(dialogViewCK);

        TextView textViewThongTinCK = dialogViewCK.findViewById(R.id.textViewThongTinCK);
        TextView textViewThongTinCK1 = dialogViewCK.findViewById(R.id.textViewThongTinCK1);
        TextView textViewThongTinCK2 = dialogViewCK.findViewById(R.id.textViewThongTinCK2);
        TextView textViewSoTienChuyenKhoan = dialogViewCK.findViewById(R.id.textViewThongTinCK3);
        Button buttonHoanTatCK = dialogViewCK.findViewById(R.id.buttonHoanTatCK);

        textViewSoTienChuyenKhoan.setText("Số tiền: " + String.format("%,.0f", soTien) + " VND");

        AlertDialog dialogCK = builderCK.create();
        dialogCK.show();

        buttonHoanTatCK.setOnClickListener(v -> {
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