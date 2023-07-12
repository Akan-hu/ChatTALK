package com.example.chattalk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.chattalk.Adapter.FragmentAdapter;
import com.example.chattalk.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        binding.viewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.setting:
                Intent intent1 = new Intent(MainActivity.this,Settings.class);
                startActivity(intent1);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent2 = new Intent(MainActivity.this,Login.class);
                startActivity(intent2);
                finish();
                break;
            case R.id.group_chat:
                Intent intent3 = new
                        Intent(MainActivity.this,GroupChatActivity.class);
                startActivity(intent3);

        }
        return super.onOptionsItemSelected(item);
    }
}
