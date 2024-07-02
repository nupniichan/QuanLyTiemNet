package com.example.doancnpm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.doancnpm.QuanLy.ChuTiemTrangChu;
import com.example.doancnpm.databinding.TrangChuBinding;
import com.example.doancnpm.user.Fragments.DichVuFragment;
import com.example.doancnpm.user.Fragments.MayTinhFragment;
import com.example.doancnpm.user.Fragments.SettingFragment;
import com.example.doancnpm.user.Fragments.TrangChuFragment;

public class TrangChu extends AppCompatActivity {
    TrangChuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TrangChuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolBarTrangChu = findViewById(R.id.ToolBarTrangChu);
        setSupportActionBar(toolBarTrangChu);

        replaceFragment(new TrangChuFragment());

        binding.btmNvbarTrangchu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.btm_nvbar_home_trangchu){
                replaceFragment(new TrangChuFragment());
            }
            else if (itemId == R.id.btm_nvbar_computer_trangchu){
                replaceFragment(new MayTinhFragment());
            }
            else if (itemId == R.id.btm_nvbar_serivce_trangchu){
                replaceFragment(new DichVuFragment());
            }
            else if (itemId == R.id.btm_nvbar_options_trangchu){
                replaceFragment(new SettingFragment());
            }
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_trang_chu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChu_Person) {
            // Toast.makeText(this, "Move to login page", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(getApplicationContext(), ChuTiemTrangChu.class);
            startActivity(i);
        }
        return true;
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.TrangChuFrameLayoutContainter, fragment);
        fragmentTransaction.commit();
    }
}
