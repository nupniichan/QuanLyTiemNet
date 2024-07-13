package com.example.doancnpm.QuanLy.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.doancnpm.R;

import java.util.ArrayList;

public class CacChucNangKhacFragment extends Fragment {

    private ListView listViewChuTiem;
    private ArrayList<String> chucNangList;
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cac_chuc_nang_khac, container, false);

        listViewChuTiem = view.findViewById(R.id.listviewChuTiem);

        chucNangList = new ArrayList<>();
        chucNangList.add("Quản lý thông tin cửa tiệm");
        chucNangList.add("Tố cáo từ người dùng");
        chucNangList.add("Báo cáo từ người dùng");
        chucNangList.add("Quản lý thứ hạng khách hàng");
        chucNangList.add("Danh sách dịch vụ chờ xác nhận");
        chucNangList.add("Quản lý chi phí");
        chucNangList.add("Quản lý doanh thu");
        chucNangList.add("Thiết lập chính sách và quy định");

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, chucNangList);
        listViewChuTiem.setAdapter(adapter);

        return view;
    }
}