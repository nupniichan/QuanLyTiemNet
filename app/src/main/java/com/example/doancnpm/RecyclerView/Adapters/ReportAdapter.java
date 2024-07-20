package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doancnpm.Objects.Report;
import com.example.doancnpm.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private Context context;
    private ArrayList<Report> reportList;

    public ReportAdapter(Context context, ArrayList<Report> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.get(position);
        holder.textViewName.setText(report.getName());
        holder.textViewEmail.setText(report.getEmail());
        holder.textViewPhone.setText(report.getPhone());
        holder.textViewContent.setText(report.getContent());
        holder.status.setText(report.getStatus());
        Glide.with(context).load(report.getImageUrl()).into(holder.imageViewReport);

        holder.btnDaGiaiQuyet.setOnClickListener(v -> {
            DatabaseReference reportRef = FirebaseDatabase.getInstance().getReference("Reports").child(report.getReportId());
            reportRef.child("status").setValue("Đã giải quyết")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(context, "Trạng thái đã được cập nhật", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(context, "Cập nhật trạng thái thất bại", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewEmail, textViewPhone, textViewContent, status;
        ImageView imageViewReport;
        Button btnDaGiaiQuyet;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewEmail = itemView.findViewById(R.id.textViewEmail);
            textViewPhone = itemView.findViewById(R.id.textViewPhone);
            textViewContent = itemView.findViewById(R.id.textViewContent);
            status = itemView.findViewById(R.id.textViewReportStatus);
            imageViewReport = itemView.findViewById(R.id.imageViewReport);
            btnDaGiaiQuyet = itemView.findViewById(R.id.btnDaGiaiQuyet);
        }
    }
}
