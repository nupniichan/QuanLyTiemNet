package com.example.doancnpm.user.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.doancnpm.R;
import com.example.doancnpm.user.ChangePasswordActivity;
import com.example.doancnpm.user.DangNhap;
import com.example.doancnpm.user.FeedbackActivity;
import com.example.doancnpm.user.ReportActivity;
import com.example.doancnpm.user.TrangChuChuaDangNhap;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class KhacFragment extends Fragment {

    private ListView listView;
    private Button btnLogout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khac, container, false);

        listView = view.findViewById(R.id.listViewSettings);
        btnLogout = view.findViewById(R.id.txtDX);

        String[] settingsOptions = {"Thông tin tài khoản", "Đổi mật khẩu", "Nạp tiền", "Góp ý", "Tố cáo","Hòm thư hỗ trợ",  "Thông tin phiên bản" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, settingsOptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Handle Thông tin tài khoản
                        break;
                    case 1:
                        Intent intentChangePassword = new Intent(getActivity(), ChangePasswordActivity.class);
                        startActivity(intentChangePassword);
                        break;
                    case 2:
                        hienThiDialogNapTien();
                        break;
                    case 3:
                        Intent intentFeedback = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(intentFeedback);
                        break;
                    case 4:
                        Intent intentReport = new Intent(getActivity(), ReportActivity.class);
                        startActivity(intentReport);
                        break;
                    case 5:
                        // Handle Thông tin phiên bản
                        break;

                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getActivity().getApplicationContext(), "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), TrangChuChuaDangNhap.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        CountDownTimer countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) { // 10 phút = 10 * 60 * 1000 milliseconds
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
        CountDownTimer countDownTimer = new CountDownTimer(10 * 60 * 1000, 1000) { // 10 phút = 10 * 60 * 1000 milliseconds
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId);
        usersRef.child("soDuTK").setValue(soTienNap)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Nạp tiền thành công!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

}
