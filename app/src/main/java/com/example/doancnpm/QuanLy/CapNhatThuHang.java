package com.example.doancnpm.QuanLy;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Users;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.ThongTin_NguoiDung_Adapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CapNhatThuHang extends AppCompatActivity {
    private RecyclerView recyclerView;
    DatabaseReference databaseReference;
    private ThongTin_NguoiDung_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cap_nhat_thu_hang);


        recyclerView = findViewById(R.id.rv);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), Users.class)
                        .build();



        adapter = new ThongTin_NguoiDung_Adapter(options);
        recyclerView.setAdapter(adapter);







    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}