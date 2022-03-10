package com.example.note;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditNotes extends AppCompatActivity {
    private EditText editNewTitle;
    private EditText editNewContent;
    private FloatingActionButton editNewNotes;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_notes);
        Toolbar toolbar = findViewById(R.id.toolbarEditNewNotes);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editNewTitle = findViewById(R.id.editNewTitle);
        editNewContent = findViewById(R.id.editNewContent);
        editNewNotes = findViewById(R.id.editNewNotes);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseFirestore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String Title = intent.getStringExtra("title");
        String Context = intent.getStringExtra("context");
        String notId = intent.getStringExtra("noteId");
        editNewTitle.setText(Title.toString().trim());
        editNewContent.setText(Context.toString().trim());
        editNewNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNewTitle.toString().isEmpty() == true) {
                    Toast.makeText(getApplicationContext(), "Không để trống tiêu đề", Toast.LENGTH_SHORT).show();
                } else if (editNewContent.toString().isEmpty() == true) {
                    Toast.makeText(getApplicationContext(), "Không để trống nội dung", Toast.LENGTH_SHORT).show();
                } else {
                    DocumentReference document = firebaseFirestore.collection("notes").document(firebaseUser.getUid()).collection("myNotes").document(notId);
                    Map<String, Object> edit = new HashMap<>();
                    edit.put("title", editNewTitle.getText().toString());
                    edit.put("content", editNewContent.getText().toString());
                    document.set(edit).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(), "Thay đổi thành công", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(EditNotes.this, Notes.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Thay đổi thất bại", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(EditNotes.this, Notes.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}