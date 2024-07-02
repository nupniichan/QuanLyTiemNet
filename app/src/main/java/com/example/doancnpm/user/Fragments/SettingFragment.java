package com.example.doancnpm.user.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doancnpm.R;
import com.example.doancnpm.user.DangNhap;
import com.example.doancnpm.user.TrangChuChuaDangNhap;


public class SettingFragment extends Fragment {
View root;
TextView txtDX;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
    root=inflater.inflate(R.layout.fragment_setting,container,false);
    txtDX = root.findViewById(R.id.txtDX);
    txtDX.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dangxuat();
        }
    });

        return root;
    }

    private void dangxuat() {
        Toast.makeText(getActivity(),"Đẵ đăng xuất",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getActivity(), TrangChuChuaDangNhap.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);             startActivity(intent);
        startActivity(intent);
    }


}