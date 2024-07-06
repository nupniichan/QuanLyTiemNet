package com.example.doancnpm.RecyclerView.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doancnpm.R;

public class DichVu_NguoiDung_ViewHolder extends RecyclerView.ViewHolder {
    public TextView TenDichVu;
    public TextView ServiceName;
    public TextView LoaiDichVu;
    public ImageView HinhAnh;
    public TextView Price;
    public Button ReservationButton;
    public TextView MoTa;
    public ImageButton menuButton;

    public DichVu_NguoiDung_ViewHolder(@NonNull View itemView) {
        super(itemView);
        TenDichVu = itemView.findViewById(R.id.TenDichVuTextView);
        ServiceName = itemView.findViewById(R.id.MoTaTextView);
        LoaiDichVu = itemView.findViewById(R.id.SerivceTypeID);
        HinhAnh = itemView.findViewById(R.id.ServiceImageID);
        MoTa = itemView.findViewById(R.id.MoTaTextView);
        Price = itemView.findViewById(R.id.PriceTextView);
        ReservationButton = itemView.findViewById(R.id.btnReservation);
        menuButton = itemView.findViewById(R.id.QuanLyDichVuButton);
    }
}
