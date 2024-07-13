package com.example.doancnpm.RecyclerView.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Computer;
import com.example.doancnpm.Objects.ComputerGroup;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.ViewHolder.MayTinh_NguoiDung_ViewHolder;
import com.example.doancnpm.user.UserGroup.ComputerUserGroupViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MayTinh_NguoiDung_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_COMPUTER = 0;
    private static final int VIEW_TYPE_GROUP = 1;

    private Context context;
    private List<Object> items;
    private OnGroupClickListener groupClickListener;

    // Firebase
    private FirebaseUser currentUser;
    private DatabaseReference databaseReference;

    public interface OnGroupClickListener {
        void onGroupClick(int position);
    }
    public interface OrderCreationCallback {
        void onOrderCreatedSuccessfully();
    }
    public MayTinh_NguoiDung_Adapter(Context context, List<Object> items, OnGroupClickListener listener) {
        this.context = context;
        this.items = items;
        this.groupClickListener = listener;

        // Firebase initialization
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public int getItemViewType(int position) {
        if (items.get(position) instanceof Computer) {
            return VIEW_TYPE_COMPUTER;
        } else if (items.get(position) instanceof ComputerGroup) {
            return VIEW_TYPE_GROUP;
        } else {
            throw new IllegalArgumentException("Invalid item type");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_COMPUTER) {
            return new MayTinh_NguoiDung_ViewHolder(LayoutInflater.from(context).inflate(R.layout.danhsachmaytinh_view, parent, false));
        } else {
            return new ComputerUserGroupViewHolder(LayoutInflater.from(context).inflate(R.layout.computer_user_group_layout, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MayTinh_NguoiDung_ViewHolder) {
            // Bind data for Computer items
            Computer computer = (Computer) items.get(position);
            ((MayTinh_NguoiDung_ViewHolder) holder).TenMayTinh.setText(computer.getName());
            ((MayTinh_NguoiDung_ViewHolder) holder).CPU.setText(computer.getCpu());
            ((MayTinh_NguoiDung_ViewHolder) holder).GPU.setText(computer.getGpu());
            ((MayTinh_NguoiDung_ViewHolder) holder).RAM.setText(computer.getRam());
            ((MayTinh_NguoiDung_ViewHolder) holder).Monitor.setText(computer.getMonitor());
            ((MayTinh_NguoiDung_ViewHolder) holder).SeatLocation.setText(computer.getComputerSeatLocation());
            ((MayTinh_NguoiDung_ViewHolder) holder).Status.setText(computer.getStatus());
            int price = computer.getPrice();
            String formattedPrice = String.format("%,d VND", price);
            ((MayTinh_NguoiDung_ViewHolder) holder).Price.setText(formattedPrice);

            // Hiển thị dialog khi nhấn button "Đặt chỗ"
            ((MayTinh_NguoiDung_ViewHolder) holder).ReservationButton.setOnClickListener(view -> {
                showReservationDialog(computer);
            });

        } else if (holder instanceof ComputerUserGroupViewHolder) {
            ComputerGroup group = (ComputerGroup) items.get(position);
            ((ComputerUserGroupViewHolder) holder).groupNameTextView.setText(group.getGroupName());

            RadioGroup radioGroup = ((ComputerUserGroupViewHolder) holder).statusRadioGroup;
            radioGroup.setOnCheckedChangeListener(null);
            radioGroup.check(R.id.all_computers_radio);

            radioGroup.setOnCheckedChangeListener((viewGroup, checkedId) -> {
                if (checkedId == R.id.available_computers_radio) {
                    filterComputersByStatus(position, "CON TRONG");
                } else if (checkedId == R.id.all_computers_radio) {
                    showAllComputersInGroup(position);
                }
            });

            holder.itemView.setOnClickListener(v -> {
                int currentPosition = holder.getAdapterPosition();
                if (currentPosition != RecyclerView.NO_POSITION) {
                    groupClickListener.onGroupClick(currentPosition);
                }
            });
        }
    }

    private void showReservationDialog(Computer computer) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_computer_reservation, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.show();

        LinearLayout dateContainer = dialogView.findViewById(R.id.dateContainer);
        TextView txtSelectedDate = dialogView.findViewById(R.id.txtSelectedDate);
        DatePicker datePicker = dialogView.findViewById(R.id.datePicker);

        LinearLayout timeContainer = dialogView.findViewById(R.id.timeContainer);
        TextView txtSelectedTime = dialogView.findViewById(R.id.txtSelectedTime);
        TimePicker timePicker = dialogView.findViewById(R.id.timePicker);
        EditText edtPlayingTime = dialogView.findViewById(R.id.edtPlayingTime);
        TextView txtTotalPrice = dialogView.findViewById(R.id.txtTotalPrice);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);

        // Lấy thông tin loại máy và ghế máy từ Computer object
        String loaiMay = computer.getLoaiMayTinh();
        String gheMay = computer.getComputerSeatLocation();

        // Hiển thị loại máy và ghế máy trên dialog
        TextView txtLoaiMay = dialogView.findViewById(R.id.txtLoaiMay);
        TextView txtGheMay = dialogView.findViewById(R.id.txtGheMay);

        if (txtLoaiMay != null) {
            txtLoaiMay.setText("Loại máy: " + loaiMay);
        }

        if (txtGheMay != null) {
            txtGheMay.setText("Ghế máy: " + gheMay);
        }

        int pricePerHour = computer.getPrice();

        edtPlayingTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Không cần thiết
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Không cần thiết
            }

            @Override
            public void afterTextChanged(Editable s) {
                String playingTimeString = s.toString();
                int playingTime = 0;
                if (!playingTimeString.isEmpty()) {
                    playingTime = Integer.parseInt(playingTimeString);
                }
                int totalPrice = pricePerHour * playingTime;
                txtTotalPrice.setText("Tổng tiền: " + totalPrice + " VND");
            }
        });

        dateContainer.setOnClickListener(v -> datePicker.setVisibility(datePicker.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

        datePicker.setOnDateChangedListener((view, year, monthOfYear, dayOfMonth) -> {
            txtSelectedDate.setText(String.format(Locale.getDefault(), "%02d/%02d/%d", dayOfMonth, monthOfYear + 1, year));
            datePicker.setVisibility(View.GONE);
        });

        timeContainer.setOnClickListener(v -> timePicker.setVisibility(timePicker.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                txtSelectedTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                timePicker.setVisibility(View.GONE);
            });
        } else {
            timePicker.setOnTimeChangedListener((view, hourOfDay, minute) -> {
                if (txtSelectedTime.getText().toString().equals("Chọn giờ")) {
                    Calendar currentTime = Calendar.getInstance();
                    hourOfDay = currentTime.get(Calendar.HOUR_OF_DAY);
                    minute = currentTime.get(Calendar.MINUTE);
                }
                txtSelectedTime.setText(String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute));
                timePicker.setVisibility(View.GONE);
            });
        }

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(context, "Vui lòng đăng nhập để đặt chỗ!", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        btnConfirm.setOnClickListener(v -> {
            String selectedDate = txtSelectedDate.getText().toString();
            String selectedTime = txtSelectedTime.getText().toString();
            String playingTimeString = edtPlayingTime.getText().toString();

            if (selectedDate.equals("Chọn ngày")) {
                Toast.makeText(context, "Vui lòng chọn ngày!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedTime.equals("Chọn giờ")) {
                Toast.makeText(context, "Vui lòng chọn giờ!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (playingTimeString.isEmpty()) {
                edtPlayingTime.setError("Vui lòng nhập số giờ chơi!");
                return;
            }

            int playingTime = Integer.parseInt(playingTimeString);
            int totalPrice = pricePerHour * playingTime;

            usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                    if (userSnapshot.exists() &&
                            userSnapshot.hasChild("soDuTK") &&
                            userSnapshot.hasChild("email") &&
                            userSnapshot.child("soDuTK").getValue(Integer.class) >= totalPrice) {

                        int newBalance = userSnapshot.child("soDuTK").getValue(Integer.class) - totalPrice;
                        String userEmail = userSnapshot.child("email").getValue(String.class);

                        usersRef.child(userId).child("soDuTK").setValue(newBalance)
                                .addOnSuccessListener(aVoid -> createOrder(computer, selectedDate, selectedTime, playingTime, totalPrice, userId, userEmail, loaiMay, gheMay, dialog::dismiss))
                                .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi trừ tiền: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(context, "Số dư không đủ!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, "Lỗi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
    }

    private void createOrder(Computer computer, String selectedDate, String selectedTime, int playingTime, int totalPrice, String userId, String userEmail, String loaiMay, String gheMay, OrderCreationCallback callback) {
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("Orders");
        String orderId = ordersRef.push().getKey();

        HashMap<String, Object> orderData = new HashMap<>();
        orderData.put("userEmail", userEmail);
        orderData.put("trangThai", "Chờ xác nhận");
        orderData.put("soTien", totalPrice);
        orderData.put("soGioChoi", playingTime);
        orderData.put("ngayChoi", selectedDate + " " + selectedTime);
        orderData.put("loaiMay", loaiMay);
        orderData.put("gheMay", gheMay);
        orderData.put("loaiDonHang", "maytinh");  // Set order type to "máy tính"

        ordersRef.child(orderId).setValue(orderData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Đặt chỗ thành công!", Toast.LENGTH_SHORT).show();
                    callback.onOrderCreatedSuccessfully();
                })
                .addOnFailureListener(e -> Toast.makeText(context, "Lỗi khi tạo đơn hàng: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void filterComputersByStatus(int groupPosition, String status) {
        ComputerGroup group = (ComputerGroup) items.get(groupPosition);
        List<Computer> filteredComputers = new ArrayList<>();
        for (Computer computer : group.getComputers()) {
            if (computer.getStatus().equals(status)) {
                filteredComputers.add(computer);
            }
        }

        int startIndex = groupPosition + 1;
        int oldCount = getItemCount() - startIndex;
        items.subList(startIndex, startIndex + oldCount).clear();
        notifyItemRangeRemoved(startIndex, oldCount);

        items.addAll(startIndex, filteredComputers);
        notifyItemRangeInserted(startIndex, filteredComputers.size());
    }

    private void showAllComputersInGroup(int groupPosition) {
        ComputerGroup group = (ComputerGroup) items.get(groupPosition);

        int startIndex = groupPosition + 1;
        int currentCount = getItemCount() - startIndex;
        items.subList(startIndex, startIndex + currentCount).clear();
        items.addAll(startIndex, group.getComputers());
        notifyItemRangeChanged(startIndex, getItemCount() - startIndex);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}