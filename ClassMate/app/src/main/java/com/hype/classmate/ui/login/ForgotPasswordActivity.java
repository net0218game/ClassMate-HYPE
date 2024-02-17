package com.hype.classmate.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.hype.classmate.R;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText emailInput;
    Button loginButton;

    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        emailInput = findViewById(R.id.loginEmailInput);
        loginButton = findViewById(R.id.loginButton);
        status = findViewById(R.id.emailStatus);
        status.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!emailInput.getText().toString().equals("")) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(emailInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            status.setVisibility(View.VISIBLE);
                            status.setTextColor(Color.parseColor("#008000"));
                            status.setText("Email successfully sent to " + emailInput.getText().toString());
                            //Toast.makeText(ForgotPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            status.setVisibility(View.VISIBLE);
                            status.setTextColor(Color.parseColor("#FF0000"));
                            status.setText("Failed to send email to " + emailInput.getText().toString());
                            //Toast.makeText(ForgotPasswordActivity.this, "Failed to send email", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}