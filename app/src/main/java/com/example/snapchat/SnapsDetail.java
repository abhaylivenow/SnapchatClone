package com.example.snapchat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class SnapsDetail extends AppCompatActivity {

    ImageView snapImageView;
    TextView messageTxt;

    String imageUrl,message,key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snaps_detail);

        snapImageView = findViewById(R.id.ImageSnap);
        messageTxt = findViewById(R.id.txtMessage);

        Intent intent = getIntent();

        imageUrl = intent.getExtras().getString("imageUrl");
        message = intent.getStringExtra("message");
        key = intent.getStringExtra("key");

        messageTxt.setText(message);

        Picasso.get().load(imageUrl).into(snapImageView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("snaps").child(key).removeValue();
    }
}