package com.example.doancnpm.user.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.doancnpm.R;
import com.example.doancnpm.user.ChangePasswordActivity;
import com.example.doancnpm.user.DangNhap;
import com.example.doancnpm.user.FeedbackActivity;
import com.example.doancnpm.user.ReportActivity;
import com.google.firebase.auth.FirebaseAuth;

public class KhacFragment extends Fragment {

    private ListView listView;
    private Button btnLogout;

    private static final String PREFS_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_khac, container, false);

        listView = view.findViewById(R.id.listViewSettings);
        btnLogout = view.findViewById(R.id.txtDX);

        String[] settingsOptions = {"Thông tin tài khoản", "Đổi mật khẩu", "Nạp tiền", "Danh sách dịch vụ đã đặt", "Góp ý", "Tố cáo", "Thông tin phiên bản"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, settingsOptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleListItemClick(position);
            }
        });

        btnLogout.setOnClickListener(v -> logoutUser());

        return view;
    }

    private void handleListItemClick(int position) {
        switch (position) {
            case 0:
                // Handle account information
                break;
            case 1:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case 2:
                // Handle deposit money
                break;
            case 3:
                replaceFragment(new DanhSachDichVuDaDat());
                break;
            case 4:
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
                break;
            case 5:
                startActivity(new Intent(getActivity(), ReportActivity.class));
                break;
            case 6:
                // Handle version information
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager(); // Ensures you're using the Activity's FragmentManager
        if (fragmentManager != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.TrangChuFrameLayoutContainter, fragment); // Corrected to use your FrameLayout container ID
            fragmentTransaction.addToBackStack(null); // Optional, adds transaction to the back stack
            fragmentTransaction.commit();
        }
    }

    private void logoutUser() {
        FirebaseAuth.getInstance().signOut();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();

        startActivity(new Intent(getActivity(), DangNhap.class));
        getActivity().finish();

        Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }
}
