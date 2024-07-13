package com.example.doancnpm.RecyclerView.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ComputerAdapter extends RecyclerView.Adapter<ComputerAdapter.ComputerViewHolder> {

    private Context context;
    private List<Computer> computerList;

    public ComputerAdapter(Context context, List<Computer> computerList) {
        this.context = context;
        this.computerList = computerList;
    }

    @NonNull
    @Override
    public ComputerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_computer_trangchu, parent, false);
        return new ComputerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComputerViewHolder holder, int position) {
        Computer computer = computerList.get(position);
        holder.textViewComputerName.setText(computer.getName());
        holder.textViewComputerPrice.setText("Giá: " + computer.getPrice() + " VND/giờ");
        Picasso.get()
                .load(computer.getComputerSeatLocation())
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh placeholder khi đang tải
                .error(R.drawable.maytinh) // Hình ảnh hiển thị khi có lỗi
                .into(holder.imageViewComputer);
    }

    @Override
    public int getItemCount() {
        return computerList.size();
    }

    public static class ComputerViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageViewComputer;
        public TextView textViewComputerName;
        public TextView textViewComputerPrice;

        public ComputerViewHolder(View itemView) {
            super(itemView);
            imageViewComputer = itemView.findViewById(R.id.imageViewComputer);
            textViewComputerName = itemView.findViewById(R.id.textViewComputerName);
            textViewComputerPrice = itemView.findViewById(R.id.textViewComputerPrice);
        }
    }
}