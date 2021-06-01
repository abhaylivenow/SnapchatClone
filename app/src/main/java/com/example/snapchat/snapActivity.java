package com.example.snapchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class snapActivity extends AppCompatActivity {

    Button btnOpenGallery, btnChooseUser;
    ImageView snapImage;
    EditText edtMessage;
    public static final int PICK_IMAGE = 1;
    Uri imageUri;
    StorageReference mStorageRef;
    UUID imageName = UUID.randomUUID();
    StorageTask mUploadTask;
    String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snap);

        btnOpenGallery = findViewById(R.id.btnOpenGallery);
        btnChooseUser = findViewById(R.id.btnSelectUser);
        snapImage = findViewById(R.id.snapImageView);
        edtMessage = findViewById(R.id.edtMessage);

        btnOpenGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        btnChooseUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(snapActivity.this,ChooseUser.class);

                intent.putExtra("imageName",imageName.toString());
                intent.putExtra("imageUrl",downloadUrl);
                intent.putExtra("message",edtMessage.getText().toString());

                startActivity(intent);
            }
        });
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data!=null && data.getData() != null){
            imageUri = data.getData();
            snapImage.setImageURI(imageUri);

            uploadImage();
        }
    }
    private void uploadImage(){
        if(imageUri!=null){
            mStorageRef = FirebaseStorage.getInstance().getReference().child("image").child(imageName+".jpg");
            mUploadTask = mStorageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(snapActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();

                    mStorageRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            downloadUrl = task.getResult().toString();
                            Log.i("download url",downloadUrl);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(snapActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}