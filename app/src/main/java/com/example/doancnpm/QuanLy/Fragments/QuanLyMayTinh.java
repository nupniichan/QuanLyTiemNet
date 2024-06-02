package com.example.doancnpm.QuanLy.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class QuanLyMayTinh extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_may_tinh, container, false);
        Toolbar toolBarTrangChu = view.findViewById(R.id.ToolbarChuTiemQuanLyMayTinhToolBar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolBarTrangChu);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_chutiem_quanlymaytinh, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChuChuTiem_MayTinh_Add) {
            Toast.makeText(getActivity(), "Thêm máy tính", Toast.LENGTH_SHORT).show();
            showAddComputerDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void showAddComputerDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thêm máy tính");

        // Inflate layout của form và đặt làm nội dung của AlertDialog
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_computer_dialog_layout, null);
        builder.setView(view);

        // Khai báo EditText cho mỗi trường thông tin
        EditText idEditText = view.findViewById(R.id.IdComputerEditText);
        EditText nameEditText = view.findViewById(R.id.ComputerNameEditTextID);
        EditText loaiMayTinhEditText = view.findViewById(R.id.ComputerTypeEditTextID);
        EditText cpuEditText = view.findViewById(R.id.CPUEditTextID);
        EditText gpuEditText = view.findViewById(R.id.GPUEditTextID);
        EditText ramEditText = view.findViewById(R.id.RAMEditTextID);
        EditText monitorEditText = view.findViewById(R.id.MonitorEditTextID);
        EditText priceEditText = view.findViewById(R.id.PriceEditTextID);
        EditText seatLocationEditText = view.findViewById(R.id.SeatLocationEditTextID);
        Button addButton = view.findViewById(R.id.btnThemMayTInhID);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = idEditText.getText().toString();
                String name = nameEditText.getText().toString();
                String loaiMayTinh = loaiMayTinhEditText.getText().toString();
                String cpu = cpuEditText.getText().toString();
                String gpu = gpuEditText.getText().toString();
                String ram = ramEditText.getText().toString();
                String monitor = monitorEditText.getText().toString();
                String price = priceEditText.getText().toString();
                String seatLocation = seatLocationEditText.getText().toString();
                Computer computer = new Computer(id, name, loaiMayTinh, cpu, gpu, ram, monitor, price, seatLocation);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("computers");

                myRef.child(id).setValue(computer);
            }
        });
        // Tạo và hiển thị AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
