package com.example.doancnpm.QuanLy.CacChucNangKhac;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.ChiPhi;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.QuanLyChiPhi_Adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class QuanLyChiPhiFragment extends Fragment {

    private QuanLyChiPhi_Adapter adapter;
    private RecyclerView recyclerViewQuanLyChiPhi;
    QuanLyChiPhiFragment fragment;
    ChiPhi chiPhi;
    FloatingActionButton floatingActionButton;



    public QuanLyChiPhiFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quan_ly_chi_phi, container, false);
        recyclerViewQuanLyChiPhi = view.findViewById(R.id.recycleViewChiPhi);
        recyclerViewQuanLyChiPhi.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<ChiPhi> options =
                new FirebaseRecyclerOptions.Builder<ChiPhi>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("ChiPhi"), ChiPhi.class)
                        .build();

        adapter = new QuanLyChiPhi_Adapter(options);
        recyclerViewQuanLyChiPhi.setAdapter(adapter);








        //thêm
        floatingActionButton=view.findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"thêm chi phí",Toast.LENGTH_LONG).show();
                showAddChiPhiDialog();

            }
        });

        return view;
    }
    private void showAddChiPhiDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Tạo Mới Chi Phí");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_chiphi, null);
        builder.setView(view);

        EditText MaChiPhi = view.findViewById(R.id.edtChiPhiMa);
        EditText TenChiPhi = view.findViewById(R.id.edtChiPhiTen);
        EditText TienChiPhi = view.findViewById(R.id.edtChiPhiSoTien);
        EditText ThoiGianChiPhi = view.findViewById(R.id.edtChiPhiThoiGian);
        EditText GhiChuChiPhi = view.findViewById(R.id.edtChiPhiGhiChu);
        EditText MaNV = view.findViewById(R.id.edtMaNVChiPhi);
        Button btnTaoMoiChiPhi= view.findViewById(R.id.btnTaoMoiChiPhi);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnTaoMoiChiPhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = MaChiPhi.getText().toString();
                String name = TenChiPhi.getText().toString();
                int money = Integer.parseInt(TienChiPhi.getText().toString());
                String time = ThoiGianChiPhi.getText().toString();
                String note = GhiChuChiPhi.getText().toString();
                String idNV= MaNV.getText().toString();

                ChiPhi chiPhi = new ChiPhi(id,name,idNV,note,time,money);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("ChiPhi");

                myRef.child(id).setValue(chiPhi).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "Tạo chi phí mới thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Taọ chi phí mới thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }








//sua




    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}
