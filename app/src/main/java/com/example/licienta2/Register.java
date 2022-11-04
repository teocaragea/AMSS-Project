package com.example.licienta2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    EditText etName, etPhone, etMail, etPass, etRePass, etNrMat;
    TextView tvLogin;
    FirebaseAuth fAuth;
    Button btnRegister;
    DatabaseReference mDatabase;
    String[] facultati ={"Facultatea de Matematica si Informatica", "Facultatea de Drept", "Facultatea de Biologie", "Facultatea de Chimie", "Facultatea de istorie", "Facultatea de Geografie"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapter;
    String facultate = "def";
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etMail = findViewById(R.id.etMail);
        etPass = findViewById(R.id.etPass);
        etRePass = findViewById(R.id.etRePass);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);
        etNrMat = findViewById(R.id.etNrMat);
        mLoginFormView = findViewById(R.id.linea);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvLoad.setVisibility(View.GONE);
        autoCompleteTextView = findViewById(R.id.auto_complete_text);
        adapter = new ArrayAdapter<String>(this, R.layout.facultate, facultati);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                facultate = parent.getItemAtPosition(position).toString();
            }
        });
        fAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();


        if (fAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email, phone, pass, repass, nrMat;
                email = etMail.getText().toString().trim();
                name = etName.getText().toString().trim();
                phone = etPhone.getText().toString().trim();
                pass = etPass.getText().toString().trim();
                repass = etRePass.getText().toString().trim();
                nrMat = etNrMat.getText().toString().trim();


                if (TextUtils.isEmpty(email)) {
                    etMail.setError("Email is required.");
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    etPass.setError("Password is required");
                    return;
                }
                if (!pass.equals(repass)) {
                    etRePass.setError("Passwords do not match.");
                    return;
                }
                if (pass.length() < 6) {
                    etPass.setError("Password must be greater than 6 characters.");
                    return;
                }
                if (phone.length() != 13) {
                    etPhone.setError("CNP has exactly 13 numbers.");
                    return;
                }
                if (facultate.equals("def")) {
                    autoCompleteTextView.setError("Facultatea trebuie introdusa");
                    return;
                }
                showProgress(true);
                User user = new User(name, email, phone, false, nrMat, facultate, 0,0, false);
                mDatabase.child("Users").child(phone).setValue(user);
                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        showProgress(false);
                        if (task.isSuccessful()) {
                            Toast.makeText(Register.this, "User successfully register", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            i.putExtra("mail", phone);
                            startActivity(i);
                        } else {
                            Toast.makeText(Register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        private void showProgress(final boolean show){
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });

                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                mProgressView.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });

                tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                tvLoad.animate().setDuration(shortAnimTime).alpha(
                        show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                    }
                });
            } else {
                // The ViewPropertyAnimator APIs are not available, so simply show
                // and hide the relevant UI components.
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
}