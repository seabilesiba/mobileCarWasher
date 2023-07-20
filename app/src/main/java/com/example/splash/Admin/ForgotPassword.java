package com.example.splash.Admin;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Driver.DriverLoginActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverResetPasswordBinding;
import com.example.splash.databinding.ActivityForgotPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private FirebaseAuth auth;
    private String email;

    private ActivityForgotPasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /*binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        auth = FirebaseAuth.getInstance();
        setListeners();

    }

    private void setListeners() {
        binding.signIn.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(), DriverLoginActivity.class)));
        binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isValidDetails()){
                    forgetPass();
                }
            }
        });
    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.logBtn.setVisibility(View.INVISIBLE);
            binding.pb.setVisibility(View.VISIBLE);
        }else{
            binding.logBtn.setVisibility(View.VISIBLE);
            binding.pb.setVisibility(View.INVISIBLE);
        }
    }
    private Boolean isValidDetails(){
        email = binding.email.getText().toString().trim();

        if(email.isEmpty()){
            showMessage("Please enter email address");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showMessage("Please enter a valid email address");
            return false;
        }else{
            return true;
        }

    }
    private void forgetPass(){
        loading(true);
        //startActivity(new Intent(ForgetPassword.this, HomeActivity.class));

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    showMessage("please check your email to reset password");
                    startActivity(new Intent(getApplicationContext(), DriverLoginActivity.class));
                    loading(false);
                }else {
                    showMessage("Something went wrong");
                    loading(false);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage(e.getMessage());
                loading(false);
            }
        });

    }
}