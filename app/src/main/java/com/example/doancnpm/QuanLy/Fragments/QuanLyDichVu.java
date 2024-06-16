package com.example.doancnpm.QuanLy.Fragments;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doancnpm.Objects.Service;
import com.example.doancnpm.R;
import com.example.doancnpm.RecyclerView.Adapters.QuanLyDanhSachDichVuAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class QuanLyDichVu extends Fragment {
    private List<Service> services = new ArrayList<>();
    private RecyclerView recyclerView;
    private QuanLyDanhSachDichVuAdapter adapter;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView selectedImageView;
    private Bitmap selectedBitmap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_quan_ly_dich_vu, container, false);
        Toolbar toolBarTrangChu = view.findViewById(R.id.ToolbarChuTiemQuanLyDichVuToolBar);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolBarTrangChu);
        setHasOptionsMenu(true);

        recyclerView = view.findViewById(R.id.QuanLyDichVuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new QuanLyDanhSachDichVuAdapter(getActivity().getApplicationContext(), services);
        recyclerView.setAdapter(adapter);

        fetchServicesFromFirebase();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_chutiem_quanlydichvu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.ToolBarTrangChuChuTiem_DichVu_Add) {
            Toast.makeText(getActivity(), "Thêm dịch vụ", Toast.LENGTH_SHORT).show();
            showAddServiceDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAddServiceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.add_service_dialog_layout, null);
        builder.setView(dialogView);

        EditText idEditText = dialogView.findViewById(R.id.IdComputerEditText);
        EditText nameEditText = dialogView.findViewById(R.id.ComputerNameEditTextID);
        EditText typeEditText = dialogView.findViewById(R.id.ComputerTypeEditTextID);
        EditText priceEditText = dialogView.findViewById(R.id.PriceServiceEditTextID);
        selectedImageView = dialogView.findViewById(R.id.selectedImageView);
        Button selectImageButton = dialogView.findViewById(R.id.selectImageButton);
        Button addButton = dialogView.findViewById(R.id.btnThemDichVuID);

        // Tạo dialog và hiển thị ngay khi tạo builder
        AlertDialog dialog = builder.create();
        dialog.show();

        selectImageButton.setOnClickListener(v -> openImagePicker());

        addButton.setOnClickListener(v -> {
            String id = idEditText.getText().toString().trim();
            String name = nameEditText.getText().toString().trim();
            String type = typeEditText.getText().toString().trim();
            String price = priceEditText.getText().toString().trim();

            if (selectedBitmap != null) {
                // Tạo reference cho Firebase Storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imagesFolderRef = storageRef.child("images"); // Tạo thư mục "images"
                StorageReference imageRef = imagesFolderRef.child(UUID.randomUUID().toString() + ".jpg");

                // Chuyển đổi ảnh sang mảng byte
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                // Tạo task lưu ảnh lên Storage
                UploadTask uploadTask = imageRef.putBytes(data);

                // Lắng nghe sự kiện upload hoàn thành
                uploadTask.addOnSuccessListener(taskSnapshot -> {
                            // Lấy đường dẫn đến ảnh đã upload
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                                // Lưu đường dẫn ảnh vào Firebase Realtime Database
                                String imageUrl = uri.toString();
                                Service newService = new Service(id, name, type, imageUrl, Integer.parseInt(price));
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
                                databaseReference.child(id).setValue(newService)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Dịch vụ đã được thêm thành công", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss(); // Dismiss dialog after successful save
                                            } else {
                                                Toast.makeText(getActivity(), "Lỗi khi thêm dịch vụ", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            });
                        })
                        .addOnFailureListener(e -> {
                            // Xử lý lỗi khi upload
                            Toast.makeText(getActivity(), "Lỗi khi upload ảnh", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getActivity(), "Vui lòng chọn hình ảnh", Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                selectedBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), imageUri);
                selectedImageView.setImageBitmap(selectedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
                // Cập nhật RecyclerView với danh sách dịch vụ mới
                updateRecyclerView(servicesList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Xử lý lỗi nếu có
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateRecyclerView(List<Service> servicesList) {
        // Cập nhật RecyclerView adapter với danh sách dịch vụ mới
        adapter.setServices(servicesList);
        adapter.notifyDataSetChanged();
    }
}