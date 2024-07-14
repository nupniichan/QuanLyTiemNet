package com.example.doancnpm.user;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.util.HashMap;
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

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextFeedback = findViewById(R.id.editTextFeedback);
        textViewPhone = findViewById(R.id.textViewPhone);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewDate = findViewById(R.id.textViewDate);
        checkBox = findViewById(R.id.checkBox);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        ImageButton buttonBack = findViewById(R.id.buttonBack);

        // Set up Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Set up back button click listener
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to the previous screen
            }
        });

        // Set user data if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            textViewEmail.setText(currentUser.getEmail());

            mDatabase.child("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String phone = snapshot.child("sdt").getValue(String.class);
                        textViewPhone.setText(phone);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(FeedbackActivity.this, "Error loading user data", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Set current date
        String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        textViewDate.setText(currentDate);

        // Set submit button click listener
        buttonSubmit.setOnClickListener(view -> {
            String name = editTextName.getText().toString();
            String feedback = editTextFeedback.getText().toString();
            boolean isChecked = checkBox.isChecked();

            if (name.isEmpty() || feedback.isEmpty()) {
                Toast.makeText(FeedbackActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Prepare data for Firebase
                DatabaseReference feedbackRef = mDatabase.child("Feedbacks").push(); // Create a unique key for each feedback
                HashMap<String, Object> feedbackData = new HashMap<>();
                feedbackData.put("name", name);
                feedbackData.put("email", textViewEmail.getText().toString());
                feedbackData.put("phone", textViewPhone.getText().toString());
                feedbackData.put("date", textViewDate.getText().toString());
                feedbackData.put("content", feedback);
                feedbackData.put("status", isChecked ? "Hoped for improvement" : "No specific hope");

                // Submit data to Firebase
                feedbackRef.setValue(feedbackData).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(FeedbackActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FeedbackActivity.this, "Failed to submit feedback: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
