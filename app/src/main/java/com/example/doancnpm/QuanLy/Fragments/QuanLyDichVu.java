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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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

public class QuanLyDichVu extends Fragment implements QuanLyDanhSachDichVuAdapter.OnItemClickListener {
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

        adapter = new QuanLyDanhSachDichVuAdapter(getActivity(), services);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);

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
        EditText moTaEditText = dialogView.findViewById(R.id.MoTaEditTextID);
        selectedImageView = dialogView.findViewById(R.id.selectedImageView);
        Button selectImageButton = dialogView.findViewById(R.id.selectImageButton);
        Button addButton = dialogView.findViewById(R.id.btnThemDichVuID);

        AlertDialog dialog = builder.create();
        dialog.show();

        selectImageButton.setOnClickListener(v -> openImagePicker());

        addButton.setOnClickListener(v -> {
            String id = idEditText.getText().toString().trim();
            String name = nameEditText.getText().toString();
            String mota = moTaEditText.getText().toString();
            String type = typeEditText.getText().toString();
            String price = priceEditText.getText().toString();

            if (selectedBitmap != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imagesFolderRef = storageRef.child("images");
                StorageReference imageRef = imagesFolderRef.child(UUID.randomUUID().toString() + ".jpg");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                            taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(uri -> {
                                String imageUrl = uri.toString();
                                Service newService = new Service(id, name, type, imageUrl, Integer.parseInt(price), mota);
                                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
                                databaseReference.child(id).setValue(newService)
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Toast.makeText(getActivity(), "Dịch vụ đã được thêm thành công", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            } else {
                                                Toast.makeText(getActivity(), "Lỗi khi thêm dịch vụ", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            });
                        })
                        .addOnFailureListener(e -> {
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
                updateRecyclerView(servicesList);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void updateRecyclerView(List<Service> servicesList) {
        if (servicesList != null && !servicesList.isEmpty()) {
            services.clear();
            services.addAll(servicesList);
            adapter.notifyDataSetChanged();
        } else {
            Log.d(TAG, "Service list is empty");
        }
    }
    @Override
    public void onViewClick(int position) {
        Service selectedService = services.get(position);
        showServiceDetailsDialog(selectedService);
    }

    private void showServiceDetailsDialog(Service service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Thông tin dịch vụ");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.service_details_dialog, null);
        builder.setView(view);

        TextView idTextView = view.findViewById(R.id.svdetail_id);
        TextView nameTextView = view.findViewById(R.id.svdetail_name);
        TextView typeTextView = view.findViewById(R.id.svdetail_type);
        TextView priceTextView = view.findViewById(R.id.svdetail_price);
        TextView descriptionTextView = view.findViewById(R.id.svdetail_description);
        ImageView imageView = view.findViewById(R.id.svdetail_image);

        idTextView.setText(service.getId());
        nameTextView.setText(service.getServiceName());
        typeTextView.setText(service.getServiceType());
        priceTextView.setText(String.format("%,d VND", service.getPrice()));
        descriptionTextView.setText(service.getMoTa());

        Glide.with(requireContext())
                .load(service.getServiceImage())
                .into(imageView);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onEditClick(int position) {
        Service selectedService = services.get(position);
        Log.d(TAG, "onEditClick: position = " + position + ", services.size() = " + services.size());
        showEditServiceDialog(selectedService);
    }

    private void showEditServiceDialog(Service service) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Sửa thông tin dịch vụ");

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_service_dialog_layout, null);
        builder.setView(view);

        EditText idEditText = view.findViewById(R.id.IdComputerEditText);
        EditText nameEditText = view.findViewById(R.id.ComputerNameEditTextID);
        EditText typeEditText = view.findViewById(R.id.ComputerTypeEditTextID);
        EditText priceEditText = view.findViewById(R.id.PriceServiceEditTextID);
        EditText moTaEditText = view.findViewById(R.id.MoTaEditTextID);
        selectedImageView = view.findViewById(R.id.selectedImageView);
        Button selectImageButton = view.findViewById(R.id.selectImageButton);
        Button updateButton = view.findViewById(R.id.btnThemDichVuID);
        updateButton.setText("Lưu thay đổi");

        idEditText.setText(service.getId());
        idEditText.setEnabled(false);
        nameEditText.setText(service.getServiceName());
        typeEditText.setText(service.getServiceType());
        priceEditText.setText(String.valueOf(service.getPrice()));
        moTaEditText.setText(service.getMoTa());

        Glide.with(requireContext())
                .load(service.getServiceImage())
                .into(selectedImageView);

        selectImageButton.setOnClickListener(v -> {
            openImagePicker();
        });

        AlertDialog dialog = builder.create();
        dialog.show();

        updateButton.setOnClickListener(v -> {
            String newName = nameEditText.getText().toString();
            String newType = typeEditText.getText().toString();
            String newPriceString = priceEditText.getText().toString();
            String newDescription = moTaEditText.getText().toString();

            if (newName.isEmpty() || newType.isEmpty() || newPriceString.isEmpty()) {
                Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("services");
            DatabaseReference serviceRef = databaseReference.child(service.getId());

            serviceRef.child("serviceName").setValue(newName);
            serviceRef.child("serviceType").setValue(newType);
            serviceRef.child("price").setValue(Integer.parseInt(newPriceString));
            serviceRef.child("moTa").setValue(newDescription);

            if (selectedBitmap != null) {
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference();
                StorageReference imagesFolderRef = storageRef.child("images");
                StorageReference imageRef = imagesFolderRef.child(UUID.randomUUID().toString() + ".jpg");

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                UploadTask uploadTask = imageRef.putBytes(data);

                uploadTask.addOnSuccessListener(taskSnapshot -> {
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String newImageUrl = uri.toString();

                        serviceRef.child("serviceImage").setValue(newImageUrl)
                                .addOnSuccessListener(aVoid -> {
                                    dialog.dismiss();
                                    Toast.makeText(getActivity(), "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getActivity(), "Cập nhật dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                                });
                    });
                }).addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Lỗi khi upload ảnh", Toast.LENGTH_SHORT).show();
                });
            } else {
                dialog.dismiss();
                Toast.makeText(getActivity(), "Cập nhật dịch vụ thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {
        Service serviceToDelete = services.get(position);
        try {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Xóa dịch vụ")
                    .setMessage("Bạn có chắc chắn muốn xóa dịch vụ này?")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        DatabaseReference servicesRef = FirebaseDatabase.getInstance().getReference("services");
                        servicesRef.child(serviceToDelete.getId()).removeValue()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        services.remove(position);
                                        adapter.notifyItemRemoved(position);
                                        Toast.makeText(getContext(), "Xóa dịch vụ thành công", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Xóa dịch vụ thất bại", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } catch (WindowManager.BadTokenException e) {
            Log.e(TAG, "Error showing dialog:", e);
            Toast.makeText(getActivity(), "Unable to delete at this time.", Toast.LENGTH_SHORT).show();
        }
    }
}