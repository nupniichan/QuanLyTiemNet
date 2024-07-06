package com.example.doancnpm.user.Fragments;

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

import com.example.doancnpm.R;
import com.example.doancnpm.user.ChangePasswordActivity;
import com.example.doancnpm.user.DangNhap;
import com.example.doancnpm.user.FeedbackActivity;
import com.example.doancnpm.user.ReportActivity;
import com.google.firebase.auth.FirebaseAuth;

public class SettingFragment extends Fragment {

    private ListView listView;
    private Button btnLogout;

    private static final String PREFS_NAME = "UserSession";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        listView = view.findViewById(R.id.listViewSettings);
        btnLogout = view.findViewById(R.id.txtDX);

        String[] settingsOptions = {"Thông tin tài khoản", "Đổi mật khẩu", "Nạp tiền", "Góp ý", "Tố cáo", "Thông tin phiên bản"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, settingsOptions);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        // Handle Thông tin tài khoản
                        break;
                    case 1:
                        Intent intentChangePassword = new Intent(getActivity(), ChangePasswordActivity.class);
                        startActivity(intentChangePassword);
                        break;
                    case 2:
                        // Handle Nạp tiền
                        break;
                    case 3:
                        Intent intentFeedback = new Intent(getActivity(), FeedbackActivity.class);
                        startActivity(intentFeedback);
                        break;
                    case 4:
                        Intent intentReport = new Intent(getActivity(), ReportActivity.class);
                        startActivity(intentReport);
                        break;
                    case 5:
                        // Handle Thông tin phiên bản
                        break;
                }
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });

        return view;
    }

    private void logoutUser() {
        // Đăng xuất Firebase
        FirebaseAuth.getInstance().signOut();

        // Đăng xuất phiên người dùng
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_IS_LOGGED_IN, false);
        editor.apply();

        // Chuyển hướng đến màn hình đăng nhập
        Intent intent = new Intent(getActivity(), DangNhap.class);
        startActivity(intent);
        getActivity().finish();

        // Hiển thị thông báo
        Toast.makeText(getActivity(), "Đã đăng xuất", Toast.LENGTH_SHORT).show();
    }
}
