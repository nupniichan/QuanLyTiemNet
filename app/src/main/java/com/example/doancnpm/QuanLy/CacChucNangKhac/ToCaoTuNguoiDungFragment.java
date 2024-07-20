package com.example.doancnpm.QuanLy.CacChucNangKhac;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doancnpm.Objects.Report;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.ReportAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ToCaoTuNguoiDungFragment extends Fragment {

    private RecyclerView recyclerViewReports;
    private ReportAdapter reportAdapter;
    private ArrayList<Report> reportList;
    private DatabaseReference databaseReference;

    public ToCaoTuNguoiDungFragment() {
        // Required empty public constructor
    }

    public static ToCaoTuNguoiDungFragment newInstance(String param1, String param2) {
        ToCaoTuNguoiDungFragment fragment = new ToCaoTuNguoiDungFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_to_cao_tu_nguoi_dung, container, false);

        recyclerViewReports = view.findViewById(R.id.recyclerViewReports);
        recyclerViewReports.setLayoutManager(new LinearLayoutManager(getContext()));

        reportList = new ArrayList<>();
        reportAdapter = new ReportAdapter(getContext(), reportList);
        recyclerViewReports.setAdapter(reportAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Reports");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reportList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Report report = dataSnapshot.getValue(Report.class);
                    if (report.getStatus() == null || report.getStatus().isEmpty()) {
                        report.setStatus("Chưa giải quyết");
                    }
                    reportList.add(report);
                }
                reportAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });

        return view;
    }
}
