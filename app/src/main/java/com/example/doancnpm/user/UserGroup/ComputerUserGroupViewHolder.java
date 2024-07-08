package com.example.doancnpm.user.UserGroup;

import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.R;

public class ComputerUserGroupViewHolder extends RecyclerView.ViewHolder {
    public TextView groupNameTextView;
    public RadioGroup statusRadioGroup;

    public ComputerUserGroupViewHolder(View itemView) {
        super(itemView);
        groupNameTextView = itemView.findViewById(R.id.may_tinh_user_group);
        statusRadioGroup = itemView.findViewById(R.id.computer_status_radio_group);
    }
}