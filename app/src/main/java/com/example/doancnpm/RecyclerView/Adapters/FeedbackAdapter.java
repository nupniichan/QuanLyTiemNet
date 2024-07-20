package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Feedback;
import com.example.doancnpm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private Context context;
    private ArrayList<Feedback> feedbackList;

    public FeedbackAdapter(Context context, ArrayList<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = feedbackList.get(position);
        holder.textViewName.setText(feedback.getName());
        holder.textViewEmail.setText(feedback.getEmail());
        holder.textViewPhone.setText(feedback.getPhone());
        holder.textViewContent.setText(feedback.getContent());
        holder.textViewStatus.setText(feedback.getStatus());

        holder.btnUpdateStatus.setOnClickListener(v -> {
            DatabaseReference feedbackRef = FirebaseDatabase.getInstance().getReference("Feedbacks").child(feedback.getFeedbackId());
            feedbackRef.child("status").setValue("Addressed")
                    .addOnSuccessListener(aVoid -> {
                        holder.textViewStatus.setText("Addressed");
                        Toast.makeText(context, "Status updated", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail, textViewPhone, textViewContent, textViewStatus;
        Button btnUpdateStatus;

        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            btnUpdateStatus = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }
}
