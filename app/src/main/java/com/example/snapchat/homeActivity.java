package com.example.snapchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class homeActivity extends AppCompatActivity {

    ListView snapsListView;
    ArrayList<String> snapsList;
    ArrayList<DataSnapshot> snapShotList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        snapsListView = findViewById(R.id.snapList);
        snapsList = new ArrayList<>();
        snapShotList = new ArrayList<>();

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,snapsList);
        snapsListView.setAdapter(arrayAdapter);

        FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth.getInstance().getUid()).child("snaps").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapsList.clear();
                for(DataSnapshot postSnapshot : snapshot.getChildren()){
                    String email = postSnapshot.child("from").getValue().toString();
                    snapsList.add(email);
                    snapShotList.add(postSnapshot);
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        snapsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataSnapshot snapshot = snapShotList.get(position);
                Intent intent = new Intent(homeActivity.this,SnapsDetail.class);
                intent.putExtra("imageUrl", snapshot.child("url").getValue().toString());
                intent.putExtra("message", snapshot.child("message").getValue().toString());
                intent.putExtra("key" , snapshot.getKey());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.snapmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.takeSnap){
            Intent intent = new Intent(homeActivity.this,snapActivity.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.logout){
            Intent intent = new Intent(homeActivity.this,MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}