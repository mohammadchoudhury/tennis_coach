package com.example.mohammad.tenniscoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    TextInputLayout mEmailEditTextLayout, mPasswordEditTextLayout;
    EditText mEmailEditText, mPasswordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        mEmailEditTextLayout = findViewById(R.id.etl_email);
        mEmailEditText = findViewById(R.id.et_email);
        mPasswordEditTextLayout = findViewById(R.id.etl_password);
        mPasswordEditText = findViewById(R.id.et_password);

        findViewById(R.id.tv_forgot_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                String emailReset = ((EditText) findViewById(R.id.et_email)).getText().toString();
                mAuth.sendPasswordResetEmail(emailReset)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Snackbar.make(v, getString(R.string.text_reset_sent), Snackbar.LENGTH_SHORT).show();
                                } else {
                                    Snackbar.make(v, "ERROR: " + task.getException(), Snackbar.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isValidForm()) {
                    String email = mEmailEditText.getText().toString().trim();
                    String password = mPasswordEditText.getText().toString();
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        finish();
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    } else {
                                        if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                            Snackbar.make(v, getString(R.string.error_user_does_not_exist), Snackbar.LENGTH_SHORT).show();
                                        } else if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                            Snackbar.make(v, getString(R.string.error_password_incorrect), Snackbar.LENGTH_SHORT).show();
                                        } else {
                                            Snackbar.make(v, "Error: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (null != currentUser) {
            finish();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }
    }

    private boolean isValidForm() {
        boolean valid = true;
        String password = mPasswordEditText.getText().toString();
        if (password.isEmpty()) {
            mPasswordEditTextLayout.setError(getString(R.string.error_password_empty));
            valid = false;
        } else {
            mPasswordEditTextLayout.setErrorEnabled(false);
        }
        return valid;
    }


}
