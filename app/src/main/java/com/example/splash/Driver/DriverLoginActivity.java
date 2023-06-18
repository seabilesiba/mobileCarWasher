package com.example.splash.Driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverLoginBinding;

public class DriverLoginActivity extends AppCompatActivity {

    private ActivityDriverLoginBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DriverHomeActivity.class));
            }
        });


    }
}