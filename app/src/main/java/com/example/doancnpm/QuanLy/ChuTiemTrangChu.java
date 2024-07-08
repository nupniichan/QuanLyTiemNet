package com.example.doancnpm.QuanLy;

import android.os.Bundle;
import android.view.View;

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
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            binding.btmNvbarTrangchuQuanly.setVisibility(View.VISIBLE);
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}