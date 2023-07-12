package com.example.chattalk;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.chattalk.Models.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Settings extends AppCompatActivity {

    CircleImageView imageView;
    TextView nameText,saveText,cancelText,editText;
    RelativeLayout layout;
    EditText et;
    FirebaseDatabase database;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        imageView = findViewById(R.id.profile_image_setting);
        nameText = findViewById(R.id.user_name);
        saveText = findViewById(R.id.save_bio);
        cancelText = findViewById(R.id.cancel_bio);
        editText = findViewById(R.id.edit_profile);
        et = findViewById(R.id.bio);
        layout = findViewById(R.id.bio_layout);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = auth.getCurrentUser();


        // Assuming you have the user ID of the currently logged-in user
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

// Assuming you have a Realtime Database instance and a "users" node
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("userName").getValue(String.class);
                    String profilePicUrl = dataSnapshot.child("userPic").getValue(String.class);

                    // Load the profile picture using Glide or Picasso
                    // For example, using Glide:
                    Picasso.get().load(profilePicUrl).placeholder(R.drawable.user).into(imageView);


                    // Set the name to the appropriate TextView
                    nameText.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Log.d(TAG, "Error getting user data: ", databaseError.toException());
            }
        });

    }
}