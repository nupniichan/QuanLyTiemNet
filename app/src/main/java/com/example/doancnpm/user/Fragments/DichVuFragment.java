package com.example.doancnpm.user.Fragments;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.DanhSachDichVuAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DichVuFragment extends Fragment {
    private List<Service> services = new ArrayList<>();
    private RecyclerView recyclerView;
    private DanhSachDichVuAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dich_vu, container, false);

        recyclerView = rootView.findViewById(R.id.DanhSachDichVuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new DanhSachDichVuAdapter(getActivity().getApplicationContext(), services);
        recyclerView.setAdapter(adapter);

        fetchServicesFromFirebase();

        return rootView;
    }
    private void fetchServicesFromFirebase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference servicesRef = database.getReference("services");

        servicesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Service> servicesList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Service service = snapshot.getValue(Service.class);
                    if (service != null) {
                        String imageUrl = service.getServiceImage();
                        if (imageUrl != null && !imageUrl.isEmpty()) {
                            servicesList.add(service);
                        }
                    }
                }
                updateRecyclerView(servicesList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateRecyclerView(List<Service> servicesList) {
        adapter.setServices(servicesList);
        adapter.notifyDataSetChanged();
    }
}
