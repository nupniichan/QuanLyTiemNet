package com.example.doancnpm.RecyclerView.QuanLyGroup;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.R;

public class ComputerGroupViewHolder extends RecyclerView.ViewHolder {
    public TextView groupNameTextView;
    public RadioGroup statusRadioGroup;

    public ComputerGroupViewHolder(@NonNull View itemView) {
        super(itemView);
        groupNameTextView = itemView.findViewById(R.id.groupNameTextView);
        statusRadioGroup = itemView.findViewById(R.id.computer_status_radio_group);
    }

    public void bind(ComputerGroup group) {
        groupNameTextView.setText(group.getGroupName());
    }
}