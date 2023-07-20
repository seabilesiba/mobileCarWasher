package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriverLoginActivity extends AppCompatActivity {

    private ActivityDriverLoginBinding binding;
    private FirebaseAuth auth;
    private String email, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        setListeners();
        checkPassword();


       /* binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });*/
    }

    private void setListeners() {
        binding.Signup.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),DriverRegistrationsActivity.class)));
        binding.forgotPassword.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(),DriverResetPasswordActivity.class)));


       binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // startActivity(new Intent(getApplicationContext(),DriverHomeActivity.class));
                if(isValidDetails()){
                    sigIn();
                }
            }
        });
    }
    private void checkPassword() {
        binding.chShowPass.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.DriverPassword.setTransformationMethod(null);
            }else{
                binding.DriverPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }
    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    private void loading(Boolean isLoading){
        if(isLoading){
            binding.logBtn.setVisibility(View.INVISIBLE);
            binding.pd.setVisibility(View.VISIBLE);
        }else{
            binding.logBtn.setVisibility(View.VISIBLE);
            binding.pd.setVisibility(View.INVISIBLE);
        }
    }
    private Boolean isValidDetails(){
        email = binding.DriverEmail.getText().toString().trim();
        pass = binding.DriverPassword.getText().toString().trim();
        if(email.isEmpty()){
            showMessage("Please enter email address");
            return false;
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            showMessage("Please enter a valid email address");
            return false;
        }else if(pass.isEmpty()){
            showMessage("Please enter password");
            return false;
        }else if((pass.length() != 6 && pass.length() < 6)){
            showMessage("Password has at least 6 characters ");
            return false;
        }else{
            return true;
        }

    }
    private void sigIn(){
        loading(true);
        //startActivity(new Intent(SignInActivity.this, HomeActivity.class));
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(DriverLoginActivity.this, DriverHomeActivity.class));
                    loading(false);
                }else{
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
