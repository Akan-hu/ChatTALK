package com.example.chattalk;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.chattalk.Models.Users;
import com.example.chattalk.databinding.ActivityLoginBinding;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(Login.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("We're logging you to your account");

        //Google Sign in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);

        binding.newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,SignUp.class);
                startActivity(intent);
                finish();
            }
        });

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.logEmail.getText().toString().isEmpty() && binding.logPassword.getText().toString().isEmpty()){
                    Toast.makeText(Login.this, "Please fill empty fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    loginUser();
                }
            }
        });
        binding.googleBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    int RC_SIGN_IN = 87;
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        //result returned from launching the intent from mGoogleSignInClient.getSignInIntent(....)
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{

               //Google sign in was successful, authenticate with firebase
               GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG","firebaseAuthWithGoogle"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            }
            catch (ApiException e){
                Log.w(TAG,"Google sing in failed",e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(idToken,null);
            auth.signInWithCredential(authCredential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithCredential:success");
                                FirebaseUser user = auth.getCurrentUser();

                                //Saving user google information in Firebase like name,id and profile picture
                                Users users = new Users();
                                users.setUserId(user.getUid());
                                users.setUserName(user.getDisplayName());
                                users.setUserPic(user.getPhotoUrl().toString());
                                database.getReference().child("Users").child(user.getUid()).setValue(users);

                                Intent i = new Intent(Login.this,MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithCredential:failure", task.getException());

                            }
                        }
                    });

    }

    private void loginUser() {
        progressDialog.show();
        auth.signInWithEmailAndPassword(binding.logEmail.getText().toString(),binding.logPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(Login.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}