package com.example.snapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChooseUser extends AppCompatActivity {

    ListView userList;
    ArrayList<String> emailList;
    ArrayList<String> uidList;
    ArrayAdapter arrayAdapter;
    DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_user);

        userList = findViewById(R.id.userList);
        emailList = new ArrayList<>();
        uidList = new ArrayList<>();

        arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,emailList);
        userList.setAdapter(arrayAdapter);

        mRef = FirebaseDatabase.getInstance().getReference();
        mRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                emailList.clear();
                uidList.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    String email = postSnapshot.child("email").getValue().toString();
                    String uid = postSnapshot.getKey();
                    uidList.add(uid);
                    emailList.add(email);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = getIntent();

                String url = intent.getExtras().getString("imageUrl");
                String imageName = intent.getExtras().getString("imageName");
                String message = intent.getExtras().getString("message");

                String from = FirebaseAuth.getInstance().getCurrentUser().getEmail();

                HashMap<String,String> map = new HashMap<>();

                map.put("url",url);
                map.put("from",from);
                map.put("imageName",imageName);
                map.put("message",message);

                FirebaseDatabase.getInstance().getReference().child("user").child(uidList.get(position)).child("snaps").push().setValue(map);
            }
        });
    }
}