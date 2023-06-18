package com.example.splash;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.splash.Driver.DriverLoginActivity;
import com.example.splash.Admin.LogInActivity;
import com.example.splash.databinding.ActivityControlBinding;

public class ControlActivity extends AppCompatActivity {

    private ActivityControlBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityControlBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.driverBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), DriverLoginActivity.class)));
        binding.ownerBtn.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), LogInActivity.class)));
    }
}