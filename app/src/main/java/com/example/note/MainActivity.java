package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText emailLogIn, passWordLogIn;
    private Button buttonLogIn, buttonCreate;
    private TextView textViewForgot;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        emailLogIn = findViewById(R.id.dangnhapEmail);
        passWordLogIn = findViewById(R.id.dangnhapmatkhau);
        buttonLogIn = findViewById(R.id.buttonLogin);
        buttonCreate = findViewById(R.id.buttonCreate);
        textViewForgot = findViewById(R.id.textViewForgot);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            Intent i = new Intent(MainActivity.this, Notes.class);
            startActivity(i);
        }
        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, SignUp.class);
                startActivity(i);
            }
        });
        textViewForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ForgotPassWord.class);
                startActivity(i);
            }
        });
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = emailLogIn.getText().toString().trim();
                String s2 = passWordLogIn.getText().toString().trim();
                if (s1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Yêu cầu nhập địa chỉ email", Toast.LENGTH_SHORT).show();
                } else if (s2.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Yêu cầu nhập mật khẩu", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(s1, s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                checkEmailVerfication();
                            } else {
                                Toast.makeText(getApplicationContext(), "Tài khoản chưa được đăng kí", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkEmailVerfication() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser.isEmailVerified()) {
            Toast.makeText(getApplicationContext(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            finish();
            Intent i = new Intent(MainActivity.this, Notes.class);
            startActivity(i);
        } else {
            Toast.makeText(getApplicationContext(), "Cần xác minh email trước", Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}