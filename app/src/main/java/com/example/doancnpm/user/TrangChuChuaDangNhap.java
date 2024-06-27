package com.example.doancnpm.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.doancnpm.R;

public class TrangChuChuaDangNhap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trangchuchuadangnhap);


        @SuppressLint("WrongViewCast") Button btnLogin = findViewById(R.id.btnDN1);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(TrangChuChuaDangNhap.this, DangNhap.class);
                startActivity(intent);
            }
        });


        Button btnDangKy =findViewById(R.id.btnDK1);
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrangChuChuaDangNhap.this,DangKy.class);
                startActivity(intent);
            }
        });

    }
}