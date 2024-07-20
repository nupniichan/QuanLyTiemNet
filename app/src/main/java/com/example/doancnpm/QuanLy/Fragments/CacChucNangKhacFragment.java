package com.example.doancnpm.QuanLy.Fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.doancnpm.QuanLy.CacChucNangKhac.FeedbackFragment;
import com.example.doancnpm.QuanLy.CacChucNangKhac.QuanLyChiPhiFragment;
import com.example.doancnpm.QuanLy.CacChucNangKhac.QuanLyDoanhThuFragment;
import com.example.doancnpm.QuanLy.CacChucNangKhac.QuanLyThongTinCuaTiemFragment;
import com.example.doancnpm.QuanLy.CacChucNangKhac.ThietLapChinhSachFragment;
import com.example.doancnpm.R;
import com.example.doancnpm.QuanLy.CacChucNangKhac.ToCaoTuNguoiDungFragment;

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
        chucNangList.add("Góp ý từ người dùng");
        chucNangList.add("Quản lý thứ hạng khách hàng");
        chucNangList.add("Danh sách dịch vụ chờ xác nhận");
        chucNangList.add("Quản lý chi phí");
        chucNangList.add("Quản lý doanh thu");
        chucNangList.add("Thiết lập chính sách và quy định");

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, chucNangList);
        listViewChuTiem.setAdapter(adapter);

        listViewChuTiem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment selectedFragment = null;

                switch (position) {
                    case 0:
                        selectedFragment = new QuanLyThongTinCuaTiemFragment();
                        break;
                    case 1:
                        selectedFragment = new ToCaoTuNguoiDungFragment();
                        break;
                    case 2:
                        selectedFragment = new FeedbackFragment();
                        break;
                    case 3:
                        selectedFragment = new CapNhatThuHang();
                        break;
                    case 4:
                        selectedFragment = new DanhSachDatDichVuFragment();
                        break;
                    case 5:
                        selectedFragment = new QuanLyChiPhiFragment();
                        break;
                    case 6:
                        selectedFragment = new QuanLyDoanhThuFragment();
                        break;
                    case 7:
                        selectedFragment = new ThietLapChinhSachFragment();
                        break;
                }

                if (selectedFragment != null) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.TrangChuQuanLyFrameLayoutContainter, selectedFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
        });

        return view;
    }
}
