package com.example.doancnpm.QuanLy.CacChucNangKhac;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doancnpm.Objects.Feedback;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.FeedbackAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FeedbackFragment extends Fragment {

    private RecyclerView recyclerViewFeedbacks;
    private FeedbackAdapter feedbackAdapter;
    private ArrayList<Feedback> feedbackList;
    private DatabaseReference databaseReference;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bao_cao_tu_nguoi_dung, container, false);

        recyclerViewFeedbacks = view.findViewById(R.id.recyclerViewFeedbacks);
        recyclerViewFeedbacks.setLayoutManager(new LinearLayoutManager(getContext()));

        feedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(getContext(), feedbackList);
        recyclerViewFeedbacks.setAdapter(feedbackAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Feedbacks");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Feedback feedback = dataSnapshot.getValue(Feedback.class);
                    if (feedback.getStatus() == null || feedback.getStatus().isEmpty()) {
                        feedback.setStatus("Hoped for improvement");
                    }
                    feedbackList.add(feedback);
                }
                feedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle possible errors.
            }
        });

        return view;
    }
}
