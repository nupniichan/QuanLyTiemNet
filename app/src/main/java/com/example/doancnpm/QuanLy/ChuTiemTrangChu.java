package com.example.doancnpm.QuanLy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.doancnpm.R;

public class ChuTiemTrangChu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_chu);

        Toolbar toolBarTrangChu = findViewById(R.id.ToolbarChuTiemTrangChuToolBar);
        setSupportActionBar(toolBarTrangChu);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_chutiem,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChuChuTiem_Person){
            Toast.makeText(this, "Move to login page", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
}