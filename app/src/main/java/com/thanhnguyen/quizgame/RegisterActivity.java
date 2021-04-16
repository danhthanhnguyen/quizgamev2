package com.thanhnguyen.quizgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        ImageView btnBack = ((ImageView) findViewById(R.id.imageView));
        Button btnRegister = ((Button) findViewById(R.id.btnRegister));
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText email = ((EditText)findViewById(R.id.tieEmail));
                EditText password = ((EditText)findViewById(R.id.tiePassword));
                if (!isEmpty(email) && !isEmpty(password)) {
                    if (isEmail(email)) {
                        createAccount(email.getText().toString(), password.getText().toString());
                    } else {
                        email.setError("Enter valid email");
                    }
                } else {
                    if (isEmpty(email)) {
                        email.setError("Email is required");
                    } else {
                        password.setError("Password is required");
                    }
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean isEmpty(EditText et) {
        CharSequence editText = et.getText().toString();
        return TextUtils.isEmpty(editText);
    }

    private boolean isEmail(EditText e) {
        CharSequence email = e.getText().toString();
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        redirectToHome(currentUser);
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FIREBASE", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            redirectToHome(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FIREBASE", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "This account already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void redirectToHome(FirebaseUser user) {
        if (user != null) {
            Intent i = new Intent(RegisterActivity.this, HomeActivity.class);
            startActivity(i);// Redirect to home page
        }
    }
}


