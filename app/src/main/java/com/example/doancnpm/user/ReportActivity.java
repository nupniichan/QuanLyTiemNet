package com.example.doancnpm.user;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doancnpm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class ReportActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;

    private EditText edtName, edtPhone, edtEmail, edtReportDate, edtContent;
    private CheckBox checkBoxConfirm;
    private Button btnSubmitReport, btnSelectImage;
    private ImageView imageViewSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        edtName = findViewById(R.id.edtName); // User can now enter their name manually
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtReportDate = findViewById(R.id.edtReportDate);
        edtContent = findViewById(R.id.edtContent);
        checkBoxConfirm = findViewById(R.id.checkBoxConfirm);
        btnSubmitReport = findViewById(R.id.btnSubmitReport);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imageViewSelected = findViewById(R.id.imageViewSelected);
        ImageButton buttonBack = findViewById(R.id.buttonBack);

        // Set up back button click listener
        buttonBack.setOnClickListener(v -> finish()); // Go back to the previous screen

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            edtEmail.setText(user.getEmail());

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String phone = snapshot.child("phone").getValue(String.class);
                        edtPhone.setText(phone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(ReportActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Calendar calendar = Calendar.getInstance();
        edtReportDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));

        btnSelectImage.setOnClickListener(v -> openFileChooser());

        btnSubmitReport.setOnClickListener(v -> {
            if (checkBoxConfirm.isChecked()) {
                submitReport();
            } else {
                Toast.makeText(ReportActivity.this, "Please confirm the accuracy of your report.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imageViewSelected.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUri).into(imageViewSelected);
        }
    }

    private void submitReport() {
        String name = edtName.getText().toString();
        String phone = edtPhone.getText().toString();
        String email = edtEmail.getText().toString();
        String reportDate = edtReportDate.getText().toString();
        String content = edtContent.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference reportsRef = FirebaseDatabase.getInstance().getReference("Reports");
        String reportId = reportsRef.push().getKey();

        Report report = new Report(reportId, name, phone, email, reportDate, content, imageUri != null ? imageUri.toString() : null);

        // If an image is selected, upload it first
        if (imageUri != null) {
            StorageReference fileReference = FirebaseStorage.getInstance().getReference("ReportImages").child(reportId);
            fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                report.imageUrl = uri.toString();
                reportsRef.child(reportId).setValue(report)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ReportActivity.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(ReportActivity.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
                            }
                        });
            })).addOnFailureListener(e -> Toast.makeText(ReportActivity.this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } else {
            // No image, just submit the report
            reportsRef.child(reportId).setValue(report)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(ReportActivity.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ReportActivity.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    public static class Report {
        public String reportId;
        public String name;
        public String phone;
        public String email;
        public String reportDate;
        public String content;
        public String imageUrl;

        public Report() {
        }

        public Report(String reportId, String name, String phone, String email, String reportDate, String content, String imageUrl) {
            this.reportId = reportId;
            this.name = name;
            this.phone = phone;
            this.email = email;
            this.reportDate = reportDate;
            this.content = content;
            this.imageUrl = imageUrl;
        }
    }
}
