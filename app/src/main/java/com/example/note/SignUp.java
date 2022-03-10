package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {
    private TextInputEditText email, passWord1, passWord2;
    private Button buttonSignUp;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        email = findViewById(R.id.taoemail);
        passWord1 = findViewById(R.id.taomatkhau);
        passWord2 = findViewById(R.id.nhaplaimatkhau);
        buttonSignUp = findViewById(R.id.buttonSignup);
        firebaseAuth = FirebaseAuth.getInstance();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String s1 = email.getText().toString().trim();
                String s2 = passWord1.getText().toString().trim();
                String s3 = passWord2.getText().toString().trim();
                if (s1.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Không được để trống Email", Toast.LENGTH_SHORT).show();
                } else if (s2.isEmpty() || s3.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Không được để trống mật khẩu", Toast.LENGTH_SHORT).show();
                } else if (s2.compareTo(s3) != 0) {
                    Toast.makeText(getApplicationContext(), "Mật khẩu không trùng khớp", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.createUserWithEmailAndPassword(s1, s2).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Tạo tài khoản thành công", Toast.LENGTH_SHORT).show();
                                sendEmailVertification();
                            } else {
                                Toast.makeText(getApplicationContext(), "Tạo tài khoản thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private void sendEmailVertification() {
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(getApplicationContext(), "Đã gửi xác nhận Email thành công", Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    Intent i = new Intent(SignUp.this, MainActivity.class);
                    startActivity(i);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Không gửi được xác nhận Email", Toast.LENGTH_SHORT).show();
        }
    }
}