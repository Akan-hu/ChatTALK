package com.example.chattalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Toast;

import com.example.chattalk.Adapter.ChatAdapter;
import com.example.chattalk.Models.MessageModel;
import com.example.chattalk.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class GroupChatActivity extends AppCompatActivity {

    ActivityGroupChatBinding groupChatBinding;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupChatBinding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(groupChatBinding.getRoot());

        Objects.requireNonNull(getSupportActionBar()).hide();
        groupChatBinding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        final String senderId = FirebaseAuth.getInstance().getUid();
        groupChatBinding.detailName.setText("Friends Group");
        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ChatAdapter adapter = new ChatAdapter(messageModels,GroupChatActivity.this);
        groupChatBinding.chatsRecyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(GroupChatActivity.this);
        groupChatBinding.chatsRecyclerView.setLayoutManager(layoutManager);
        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModels.clear();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    MessageModel model = dataSnapshot.getValue(MessageModel.class);
                                    messageModels.add(model);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        groupChatBinding.sendFabGroupChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = groupChatBinding.grouChatEt.getText().toString();
                final MessageModel model = new MessageModel(senderId,message);


               // model.setTimeStamp(new Date().getTime());
             //   model.setTimeStamp(ServerValue.TIMESTAMP);
                groupChatBinding.grouChatEt.setText("");
                database.getReference().child("Group Chat").push().setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
            }
        });

    }
}