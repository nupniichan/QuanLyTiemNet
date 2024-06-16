package com.example.doancnpm.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.doancnpm.R;

public class TrangChuDaDangNhap extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu_da_dang_nhap);

        UserSession userSession = new UserSession(this);
        if (!userSession.isUserLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        Toolbar toolBarTrangChu = findViewById(R.id.ToolBarTrangChuDaDangNhap);
        setSupportActionBar(toolBarTrangChu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_trangchu_dadangnhap, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChu_DaDangNhap_PersonLogined) {
            Toast.makeText(this, "Move to user profile management page", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.ToolBarTrangChu_DaDangNhap_Logout) {
            Toast.makeText(this, "Show confirm dialog logout", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.ToolBarTrangChu_DaDangNhap_Settings) {
            Toast.makeText(this, "Move to settings page", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}
