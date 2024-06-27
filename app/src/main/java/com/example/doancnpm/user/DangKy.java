package com.example.doancnpm.user;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doancnpm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class DangKy extends AppCompatActivity {

    private EditText edtHovaTen,edtEmai,edtNgaySinh,edtSDT,edtMatKhau,edtXNMatKhau,edtDiaChi,edtCCCD;
    private ProgressBar progressBar;
    private RadioGroup radioGroupGender;
    private RadioButton radioButtonGenderSelected;
    private  static  final String TAG="DangKyActivity";
    Button btnDK;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);


        Toast.makeText(DangKy.this,"Bạn có thể đăng ký ngay bây giờ",Toast.LENGTH_LONG).show();
       progressBar = findViewById(R.id.progressBar);
        edtHovaTen = findViewById(R.id.edtHovaTen);
        edtEmai=findViewById(R.id.edtEmail);
        edtNgaySinh=findViewById(R.id.edtNgaySinh);
        edtSDT=findViewById(R.id.edtSDT);
        edtMatKhau=findViewById(R.id.edtMatKhau);
        edtXNMatKhau=findViewById(R.id.edtXNMatKhau);
        edtDiaChi=findViewById(R.id.edtDiaChi);
        edtCCCD=findViewById(R.id.edtCCCD);
        radioGroupGender=findViewById(R.id.radio_group_btn);
        radioGroupGender.clearCheck();
        btnDK = findViewById(R.id.btnDK2);
        //xử lý điều kiện
        btnDK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selecGenderID=radioGroupGender.getCheckedRadioButtonId();
                radioButtonGenderSelected = findViewById(selecGenderID);
                String txtHovaTen,txtEmail,txtNgaySinh,txtSDT,txtMatKhau,txtXNMatKhau,txtDiaChi,txtCCCD,txtGioiTinh;
                txtHovaTen = edtHovaTen.getText().toString();
                txtEmail = edtEmai.getText().toString();
                txtNgaySinh=edtNgaySinh.getText().toString();
                txtSDT=edtSDT.getText().toString();
                txtMatKhau=edtMatKhau.getText().toString();
                txtXNMatKhau=edtXNMatKhau.getText().toString();
                txtDiaChi=edtDiaChi.getText().toString();
                txtCCCD=edtCCCD.getText().toString();


                if(TextUtils.isEmpty(txtHovaTen)){
                    Toast.makeText(DangKy.this,"Vui lòng nhập họ và tên của bạn ",Toast.LENGTH_LONG).show();
                    edtHovaTen.setError("Yêu cầu nhập họ và tên !");
                    edtHovaTen.requestFocus();
                } else if(TextUtils.isEmpty(txtEmail)){
                    Toast.makeText(DangKy.this,"Vui lòng nhập email của bạn ",Toast.LENGTH_LONG).show();
                    edtEmai.setError("Yêu cầu nhập email !");
                    edtEmai.requestFocus();
                }else if(TextUtils.isEmpty(txtNgaySinh)) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập ngày sinh của bạn ", Toast.LENGTH_LONG).show();
                    edtNgaySinh.setError("Yêu cầu nhập ngày sinh của bạn !");
                    edtNgaySinh.requestFocus();

                }else if(!Patterns.EMAIL_ADDRESS.matcher(txtEmail).matches()){
                    Toast.makeText(DangKy.this, "Vui lòng nhập lại email của bạn ", Toast.LENGTH_LONG).show();
                    edtNgaySinh.setError("Email không hợp lệ !");
                    edtNgaySinh.requestFocus();
                }
                else if(TextUtils.isEmpty(txtSDT)) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập số điện thoại của bạn ", Toast.LENGTH_LONG).show();
                    edtSDT.setError("Yêu cầu nhập số điện thoại của bạn !");
                    edtSDT.requestFocus();

                }else if(txtSDT.length()!=10){
                    Toast.makeText(DangKy.this,"Vui lòng nhập lại số điện thoại ",Toast.LENGTH_LONG).show();
                    edtSDT.setError("Số điện thoại không hợp lệ");
                    edtSDT.requestFocus();
                }

                else if(radioGroupGender.getCheckedRadioButtonId()==-1){
                    Toast.makeText(DangKy.this,"Vui lòng chọn giới tính của bạn",Toast.LENGTH_LONG).show();
                    radioButtonGenderSelected.setError("Yêu cầu chọn giới tính");
                    radioButtonGenderSelected.requestFocus();

                } else if(TextUtils.isEmpty(txtMatKhau)) {
                    Toast.makeText(DangKy.this, "Vui lòng nhập mật khẩu", Toast.LENGTH_LONG).show();
                    edtMatKhau.setError("Yêu cầu nhập mật khẩu");
                    edtMatKhau.requestFocus();

                }else  if(txtMatKhau.length()<6||!txtMatKhau.matches(".*[A-Z]*.")||!txtMatKhau.matches(".*[a-z]*.")){
                    Toast.makeText(DangKy.this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_LONG).show();
                    edtMatKhau.setError("Mật khẩu phải có chứa chữ in hoa và nhiều hơn 6 kí tự");
                    edtMatKhau.requestFocus();
                } else  if(TextUtils.isEmpty(txtXNMatKhau)){
                    Toast.makeText(DangKy.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_LONG).show();
                    edtXNMatKhau.setError("Yêu cầu nhập lại mật khẩu");
                    edtXNMatKhau.requestFocus();
                }else  if(!txtMatKhau.equals(txtXNMatKhau)){
                    Toast.makeText(DangKy.this,"Vui lòng nhập nhập cùng một mật khẩu",Toast.LENGTH_LONG).show();
                    edtXNMatKhau.setError("Yêu cầu nhập lại xác nhận mật khẩu");
                    edtXNMatKhau.requestFocus();
                    edtMatKhau.clearComposingText();
                    edtXNMatKhau.clearComposingText();


                }else if(TextUtils.isEmpty(txtDiaChi)){
                    Toast.makeText(DangKy.this,"Vui lòng nhập địa chỉ",Toast.LENGTH_LONG).show();
                    edtDiaChi.setError("Yêu cầu nhập địa chỉ");
                    edtDiaChi.requestFocus();

                }else if(TextUtils.isEmpty(txtCCCD)){
                    Toast.makeText(DangKy.this,"Vui lòng nhập CCCD",Toast.LENGTH_LONG).show();
                    edtCCCD.setError("Yêu cầu nhập CCCD");
                    edtCCCD.requestFocus();
                }else if(txtCCCD.length()!=12){
                    Toast.makeText(DangKy.this,"Vui lòng nhập lại mật khẩu",Toast.LENGTH_LONG).show();
                    edtCCCD.setError("Thông tin CCCD không hợp lệ");
                    edtCCCD.requestFocus();

                }else {
                    txtGioiTinh=radioButtonGenderSelected.getText().toString();
                    progressBar.setVisibility(View.VISIBLE);
                    DangKyUser(txtHovaTen,txtEmail,txtNgaySinh,txtSDT,txtMatKhau,txtXNMatKhau,txtDiaChi,txtCCCD,txtGioiTinh);
                }



            }
        });
    }
    //Đăng ký tài khoản
    private void DangKyUser(String txtHovaTen, String txtEmail, String txtNgaySinh, String txtSDT, String txtMatKhau, String txtXNMatKhau, String txtDiaChi, String txtCCCD, String txtGioiTinh) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(txtEmail,txtMatKhau).addOnCompleteListener(DangKy.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Toast.makeText(DangKy.this,"Đăng ký thành công",Toast.LENGTH_LONG).show();
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                firebaseUser.sendEmailVerification();

//                //chuyển sang trang chủ
//                Intent intent = new Intent(DangKy.this, TrangChuDaDangNhap.class);
//                //xóa section
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                finish();

            }else {
                try {
                    throw task.getException();

                }catch (FirebaseAuthWeakPasswordException e){
                    edtMatKhau.setError("mật khẩu của bạn rất yếu, vui lòng kết hợp với chữ viết hoa và số");
                    edtMatKhau.requestFocus();

                }catch (FirebaseAuthInvalidCredentialsException e){
                    edtEmai.setError("Email của bạn không hợp lệ hoặc đã được sử dụng, vui lòng nhập lại !");
                    edtEmai.requestFocus();
                }catch (FirebaseAuthUserCollisionException e){
                    edtEmai.setError("Email của bạn đã được đăng ký, vui lòng xài tài khoản khác");
                    edtEmai.requestFocus();
                }catch (Exception e ){
                    Log.e(TAG,e.getMessage());
                    Toast.makeText(DangKy.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
            }
        });


    }
}