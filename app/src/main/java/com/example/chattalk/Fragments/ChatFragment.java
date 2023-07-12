package com.example.chattalk.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.chattalk.Adapter.UsersAdapter;
import com.example.chattalk.Models.Users;
import com.example.chattalk.R;
import com.example.chattalk.databinding.FragmentChatBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChatFragment extends Fragment {

    public ChatFragment (){

    }

    FragmentChatBinding chatBinding;
    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chatBinding = FragmentChatBinding.inflate(inflater,container,false);

        UsersAdapter usersAdapter =  new UsersAdapter(list, getContext());
        chatBinding.chatRecycler.setAdapter(usersAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        chatBinding.chatRecycler.setLayoutManager(layoutManager);

        database = FirebaseDatabase.getInstance();
        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                   users.setUserId(dataSnapshot.getKey());
                    list.add(users);
                }
                usersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Fail to get data.", Toast.LENGTH_SHORT).show();


            }
        });
        return chatBinding.getRoot();

    }
}