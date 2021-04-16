package com.thanhnguyen.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EditPassword extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText etOldPassword,etNewPassword,etConfirmNewPassword;
    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        etOldPassword = findViewById(R.id.tietPassword);
        etNewPassword = findViewById(R.id.tietPasswordNewPass);
        etConfirmNewPassword = findViewById(R.id.tietPasswordConfirmNewPass);
        Button btnSavePassword = findViewById(R.id.btnChangePassword);
        ImageView btnBack = (ImageView) findViewById(R.id.imageViewEditPassword);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        final String email = user.getEmail();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditPassword.this, HomeActivity.class);
                startActivity(i);
            }
        });

        btnSavePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String oldPassword = etOldPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                String confirmNewPassword = etConfirmNewPassword.getText().toString();

                if (newPassword.equals(confirmNewPassword)) {
                    onChangePassword(oldPassword, newPassword);
                } else {
                    Toast.makeText(EditPassword.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                }
            }
        });
    };
    public void onChangePassword(String oldPassword, final String newPassword) {

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        String currentEmail = user.getEmail();
        AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, oldPassword);

        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(EditPassword.this, "Password was changed successfully", Toast.LENGTH_LONG).show();
                                                redirectToHome(user);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(EditPassword.this, "Authentication failed, wrong password?", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void redirectToHome(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(EditPassword.this, HomeActivity.class);
            startActivity(i);// Redirect to home page
        }
    }
}