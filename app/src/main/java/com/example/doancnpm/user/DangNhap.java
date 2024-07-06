package com.example.doancnpm.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doancnpm.QuanLy.ChuTiemTrangChu;
import com.example.doancnpm.R;
import com.example.doancnpm.TrangChu;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DangNhap extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private TextView txtSignUp;
    private ProgressBar progressBar;
    private FirebaseAuth firebaseAuth;

    private static final String PREFS_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        progressBar = findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();

        txtSignUp = findViewById(R.id.txtSignUp);
        txtSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DangNhap.this, DangKy.class);
                startActivity(intent);
                finish();
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtUsername = edtUsername.getText().toString();
                String txtPass = edtPassword.getText().toString();
                if (TextUtils.isEmpty(txtUsername)) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập email của bạn", Toast.LENGTH_LONG).show();
                    edtUsername.setError("Yêu cầu nhập email");
                    edtUsername.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtUsername).matches()) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập lại email", Toast.LENGTH_LONG).show();
                    edtUsername.setError("Email không đúng định dạng");
                    edtUsername.requestFocus();
                } else if (TextUtils.isEmpty(txtPass)) {
                    Toast.makeText(DangNhap.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                    edtPassword.setError("Yêu cầu nhập mật khẩu");
                    edtPassword.requestFocus();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(txtUsername, txtPass);
                }
            }
        });

        // Kiểm tra session khi Activity được tạo
        if (isUserLoggedIn()) {
            navigateToTrangChu();
        }
    }

    private void loginUser(String txtUsername, String txtPass) {
        firebaseAuth.signInWithEmailAndPassword(txtUsername, txtPass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công
                            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                            firebaseDatabase.getReference().child("Users")
                                    .child(firebaseAuth.getUid())
                                    .child("userType")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            int userType = snapshot.getValue(Integer.class);
                                            if (userType == 0) {
                                                // Lưu session
                                                saveUserSession();
                                                Intent intent = new Intent(DangNhap.this, TrangChu.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            } else if (userType == 1) {
                                                // Lưu session
                                                saveUserSession();
                                                Intent intent = new Intent(DangNhap.this, ChuTiemTrangChu.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                startActivity(intent);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // Xử lý lỗi
                                            Toast.makeText(DangNhap.this, "Đã xảy ra lỗi", Toast.LENGTH_LONG).show();
                                            progressBar.setVisibility(View.GONE);
                                        }
                                    });

                        } else {
                            // Xử lý đăng nhập thất bại
                            Toast.makeText(DangNhap.this, "Sai thông tin đăng nhập", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


    private void saveUserSession() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.apply();
    }

    private boolean isUserLoggedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    private void navigateToTrangChu() {
        Intent intent = new Intent(DangNhap.this, TrangChu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}