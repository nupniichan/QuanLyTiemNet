package com.example.doancnpm.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doancnpm.Objects.Users;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.MayTinh_QuanLy_Adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import androidx.appcompat.widget.Toolbar;

public class ThongTinCaNhan extends AppCompatActivity {
    private TextView txtWelcome,txtFullname,txtEmail,txtDoB,txtGender,txtPhone,txtCCCD,txtAddress;
    private ProgressBar progressBar;

    private  String fullName,Email,DoB,Gender,Phone,CCCD,Address;
    private ImageView imageView;
    private FirebaseAuth firebaseAuth;
    Context context;
    private MayTinh_QuanLy_Adapter.OnItemClickListener onItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_tin_ca_nhan);
        Toolbar toolBarTrangTTCN = findViewById(R.id.ToolBarTrangTTCN);

        setSupportActionBar(toolBarTrangTTCN);


        txtWelcome=findViewById(R.id.txtWelcom);
        txtFullname=findViewById(R.id.txtFullname);
        txtEmail=findViewById(R.id.txtEmail);
        txtDoB=findViewById(R.id.txtDoB);
        txtGender=findViewById(R.id.txtGender);
        txtPhone=findViewById(R.id.txtPhone);
        txtCCCD=findViewById(R.id.txtCCCD);
        txtAddress=findViewById(R.id.txtAddress);
        progressBar=findViewById(R.id.progressBar);
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser==null){
            Toast.makeText(ThongTinCaNhan.this,"Đã xảy ra lỗi!!",Toast.LENGTH_LONG).show();

        }
        else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }



    }

    private void showUserProfile(FirebaseUser firebaseUser) {

        String UserID =firebaseUser.getUid();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.child(UserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users readUser = snapshot.getValue(Users.class);
                if(readUser!=null){
                    fullName= firebaseUser.getDisplayName();
                    Email = firebaseUser.getEmail();
                    DoB = readUser.getNgaySinh();
                    Gender = readUser.getGioiTinh();
                    Phone = readUser.getSDT();
                    CCCD = readUser.getCCCD();
                    Address = readUser.getDiaChi();
                    txtWelcome.setText("Welcome "+fullName);
                    txtFullname.setText(fullName);
                    txtEmail.setText(Email);
                    txtPhone.setText(Phone);
                    txtAddress.setText(Address);
                    txtCCCD.setText(CCCD);
                    txtDoB.setText(DoB);
                    txtGender.setText(Gender);

                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ThongTinCaNhan.this,"đã xảy ra lỗi",Toast.LENGTH_LONG).show();

            }
        });


    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater mnu = getMenuInflater();
        mnu.inflate(R.menu.menu_ttcn,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.menu_editTTCN) {
            Intent intent = new Intent(ThongTinCaNhan.this, CapNhatThongTinCN.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }
}