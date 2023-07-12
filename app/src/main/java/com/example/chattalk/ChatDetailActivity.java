package com.example.chattalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattalk.Adapter.ChatAdapter;
import com.example.chattalk.Models.MessageModel;
import com.example.chattalk.databinding.ActivityChatDetailBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetailActivity extends AppCompatActivity {
    ActivityChatDetailBinding chatDetailBinding;
    FirebaseDatabase database;
    String Name,Pic,receiveId;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chatDetailBinding = ActivityChatDetailBinding.inflate(getLayoutInflater());
        setContentView(chatDetailBinding.getRoot());
        getSupportActionBar().hide();

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        final String senderId = auth.getUid();
        receiveId = getIntent().getStringExtra("UserId");
        Name = getIntent().getStringExtra("Name");
        Pic = getIntent().getStringExtra("ProfilePic");

        chatDetailBinding.detailName.setText(Name);
        Picasso.get().load(Pic).placeholder(R.drawable.user).into(chatDetailBinding.profileImageDetail);

        chatDetailBinding.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        final ArrayList<MessageModel> messageModels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModels,ChatDetailActivity.this);
        chatDetailBinding.chatsRecyclerView.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatDetailActivity.this);
        chatDetailBinding.chatsRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + receiveId;
        final String receiverRoom = receiveId + senderId;

        database.getReference().child("Chats").child(senderRoom)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //This method used because we don't want that same data come again and again from database
                                //Which is already loaded only new data show in adapter
                                messageModels.clear();
                               for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                   MessageModel model = snapshot1.getValue(MessageModel.class);
                                   messageModels.add(model);
                               }
                               chatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


            chatDetailBinding.sendFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String msg = chatDetailBinding.sendMsgEt.getText().toString();
                    final MessageModel model = new MessageModel(senderId,msg);

                  //  model.setTimestamp(ServerValue.TIMESTAMP);
                    model.setTimestamp(ServerValue.TIMESTAMP);
                    chatDetailBinding.sendMsgEt.setText("");
                    database.getReference().child("Chats").child(senderRoom).push()
                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.getReference().child("Chats").child(receiverRoom).push()
                                            .setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                }
                                            });
                                }
                            });
                }
            });
        }


}