package com.example.doancnpm.QuanLy;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.doancnpm.QuanLy.Fragments.QuanLyDichVu;
import com.example.doancnpm.QuanLy.Fragments.QuanLyMayTinh;
import com.example.doancnpm.QuanLy.Fragments.TrangChuQuanLy;
import com.example.doancnpm.R;
import com.example.doancnpm.databinding.ChuTiemTrangChuBinding;
import com.example.doancnpm.user.Fragments.SettingFragment;
import com.example.doancnpm.user.ThongTinCaNhan;

public class ChuTiemTrangChu extends AppCompatActivity {
    ChuTiemTrangChuBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChuTiemTrangChuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new TrangChuQuanLy());

        binding.btmNvbarTrangchuQuanly.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.btm_nvbar_home_trangchu_quanly) {
                replaceFragment(new TrangChuQuanLy());
            } else if (itemId == R.id.btm_nvbar_computer_trangchu_quanly) {
                replaceFragment(new QuanLyMayTinh());
            } else if (itemId == R.id.btm_nvbar_serivce_trangchu_quanly) {
                replaceFragment(new QuanLyDichVu());
            } else if (itemId == R.id.btm_nvbar_options_trangchu_quanly) {
                replaceFragment(new SettingFragment());
            }
            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.TrangChuQuanLyFrameLayoutContainter, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_chutiem_trangchu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChuChuTiem_Person) {
            Intent intent = new Intent(ChuTiemTrangChu.this, ThongTinCaNhan.class);
            startActivity(intent);
            // Xử lý khác nếu cần
            return true;
        }
        if(id==R.id.ToolBarTrangChuChuTiem_QLUser){
            Intent intent = new Intent(ChuTiemTrangChu.this,CapNhatThuHang.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            binding.btmNvbarTrangchuQuanly.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}