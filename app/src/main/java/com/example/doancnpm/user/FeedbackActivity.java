package com.example.doancnpm.user;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doancnpm.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editTextName, editTextFeedback;
    private TextView textViewPhone, textViewEmail, textViewDate;
    private CheckBox checkBox;
    private Button buttonSubmit;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        editTextName = findViewById(R.id.editTextName);
        editTextFeedback = findViewById(R.id.editTextFeedback);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewDate = findViewById(R.id.textViewDate);
        checkBox = findViewById(R.id.checkBox);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textViewEmail.setText(currentUser.getEmail());

            mDatabase.child("users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String phone = snapshot.child("phone").getValue(String.class);
                        textViewPhone.setText(phone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FeedbackActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        textViewDate.setText(currentDate);

        buttonSubmit.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String feedback = editTextFeedback.getText().toString();
            boolean isChecked = checkBox.isChecked();

            if (name.isEmpty() || feedback.isEmpty()) {
                Toast.makeText(FeedbackActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Handle feedback submission
                Toast.makeText(FeedbackActivity.this, "Feedback submitted", Toast.LENGTH_SHORT).show();
            }
        });
    }
}