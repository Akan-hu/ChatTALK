package com.example.chattalk.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chattalk.ChatDetailActivity;
import com.example.chattalk.Models.Users;
import com.example.chattalk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    ArrayList<Users> users;
    Context context;

    public UsersAdapter(ArrayList<Users> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_row_chats,parent,false);

        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {
        Users userList = users.get(position);
        Picasso.get().load(userList.getUserPic()).placeholder(R.drawable.user).into(holder.imageView);
        holder.nameText.setText(userList.getUserName());

        FirebaseDatabase.getInstance().getReference().child("Chats")
                        .child(FirebaseAuth.getInstance().getUid() + userList.getUserId())
                                .orderByChild("timeStamp")
                                        .limitToLast(1)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        if(snapshot.hasChildren()){
                                                            for (DataSnapshot snapshot1 : snapshot.getChildren()){
                                                                holder.messageText.setText(snapshot1.child("message").getValue().toString());
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, ChatDetailActivity.class);
                i.putExtra("UserId",userList.getUserId());
                i.putExtra("Name",userList.getUserName());
                i.putExtra("ProfilePic",userList.getUserPic());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class UsersViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText,messageText;
        LinearLayout layout;
        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.profile_image);
            nameText = itemView.findViewById(R.id.name_id);
            messageText = itemView.findViewById(R.id.message_id);
            layout = itemView.findViewById(R.id.chat_layout);

        }
    }
}
