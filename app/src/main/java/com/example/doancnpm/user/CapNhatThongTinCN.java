package com.example.doancnpm.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.ThongTinNguoiDungAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CapNhatThongTinCN extends AppCompatActivity {
    private EditText edtUpdateName,edtUpdateDoB,edtUpdatePhone,edtUpdateSDT,edtUpdateCCCD,edtUpdateAddress;
    private RadioGroup RadiogroupGender;
    private RadioButton radioButtonGenderSelected;
    private  String fullName,DoB,Phone,Gender,CCCD,Address;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    DatePickerDialog picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_thong_tin_cn);

        progressBar = findViewById(R.id.progressBar);
        edtUpdateName = findViewById(R.id.edtUpdate_profile_name);
        edtUpdateDoB = findViewById(R.id.edtUpdate_profile_DoB);
        edtUpdatePhone = findViewById(R.id.edtUpdate_profile_Phone);
        edtUpdateSDT = findViewById(R.id.edtUpdate_profile_Phone);
        edtUpdateCCCD = findViewById(R.id.edtUpdate_profile_CCCD);
        edtUpdateAddress = findViewById(R.id.edtUpdate_profile_Address);


        RadiogroupGender = findViewById(R.id.radio_group_update_gender);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        showProfile(firebaseUser);




        //update profile
        Button btn_update_profile=findViewById(R.id.btn_update_profile);
        btn_update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateProfile(firebaseUser);

            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser) {
        int selectedGenderID= RadiogroupGender.getCheckedRadioButtonId();
        radioButtonGenderSelected =findViewById(selectedGenderID);
        String mobileRegex = "[0][0-9]{9}";
        Matcher mobileMatcher;
        Pattern mobilePattern= Pattern.compile(mobileRegex);
        mobileMatcher = mobilePattern.matcher(Phone);


        //kiem tra du lieu

        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(this,"vui lòng nhập đầy đủ họ và tên !!",Toast.LENGTH_LONG).show();
            edtUpdateName.setError("không được bỏ trống ");
            return;



        }

        else  if(TextUtils.isEmpty(Phone)){
            Toast.makeText(this,"vui lòng nhập số điện thoại  !!",Toast.LENGTH_LONG).show();
            edtUpdatePhone.setError("không được bỏ trống ");
            return;
        }else  if(Phone.length()!=10){
            Toast.makeText(this,"vui lòng nhập số điện thoại  !!",Toast.LENGTH_LONG).show();
            edtUpdatePhone.setError("số điện thoại phải đủ 10 số ");
            return;
        }
        else if(!mobileMatcher.find()){
            Toast.makeText(this,"vui lòng nhập số điện thoại !!",Toast.LENGTH_LONG).show();
            edtUpdatePhone.setError("vui lòng xem lại số điện thoại");
            return;
        }



        else  if(TextUtils.isEmpty(DoB)){
            Toast.makeText(this,"vui lòng nhập ngày sinh !!",Toast.LENGTH_LONG).show();
            edtUpdateDoB.setError("không được bỏ trống ");
            return;
        }


        else  if(RadiogroupGender.getCheckedRadioButtonId()==-1){
            Toast.makeText(this,"vui lòng chọn giới tính của bạn !!",Toast.LENGTH_LONG).show();

            return;
        }else {
            Gender=radioButtonGenderSelected.getText().toString();
            fullName=edtUpdateName.getText().toString();
            DoB=edtUpdateDoB.getText().toString();
            Phone=edtUpdatePhone.getText().toString();
            CCCD=edtUpdateCCCD.getText().toString();
            Address=edtUpdateAddress.getText().toString();

            ThongTinNguoiDungAdapter readWriteUserDetail = new ThongTinNguoiDungAdapter(firebaseUser.getEmail(),DoB,Gender,Phone,"",CCCD,Address,0);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Register Users");

            String IDuser = firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);
            reference.child(IDuser).setValue(readWriteUserDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if(task.isSuccessful()){
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(fullName).build();
                        firebaseUser.updateProfile(userProfileChangeRequest);

                        Toast.makeText(CapNhatThongTinCN.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CapNhatThongTinCN.this,ThongTinCaNhan.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }
                    else {

                        try {
                            throw task.getException();

                        }catch (Exception e){
                            Toast.makeText(CapNhatThongTinCN.this,e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                    progressBar.setVisibility(View.GONE);

                }
            });



            progressBar.setVisibility(View.VISIBLE);



        }

    }





    private void showProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();


        //lấy đối tượng từ trong firebase tên là Register

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Registered Users");

        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ThongTinNguoiDungAdapter readWriteUserDetail = snapshot.getValue(ThongTinNguoiDungAdapter.class);
                if(readWriteUserDetail!=null){
                    fullName= firebaseUser.getDisplayName();

                    DoB = readWriteUserDetail.NgaySinh;
                    Gender = readWriteUserDetail.GioiTinh;
                    Phone = readWriteUserDetail.SDT;
                    CCCD = readWriteUserDetail.CCCD;
                    Address = readWriteUserDetail.DiaChi;



                    //hiện thị giới tính thông qua RadioGroup
                    if(Gender.equals("Nam")){
                        radioButtonGenderSelected=findViewById(R.id.radio_btn_update_male);

                    }else {
                        radioButtonGenderSelected=findViewById(R.id.radio_btn_update_female);
                    }
                    radioButtonGenderSelected.setChecked(true);


                }else {
                    Toast.makeText(CapNhatThongTinCN.this, "Đã xảy ra lỗi !", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CapNhatThongTinCN.this, "Đã xảy ra lỗi !", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });

    }

}
