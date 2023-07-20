package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.splash.R;
import com.example.splash.databinding.ActivityRequestBinding;
import com.example.splash.model.AcceptedData;
import com.example.splash.model.RequestData;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private FirebaseAuth auth;
    private String surname,name,location,image,uniqueId,image1,title;
    private int price;

    //FusedLocationProviderClient fusedLocationProviderClient;

    private ActivityRequestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference().child("RequstData");
        auth = FirebaseAuth.getInstance();

        getData();

        binding.txtVieLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLastLocation();
            }
        });

    }
    private void getData(){
        uniqueId = getIntent().getStringExtra("uniqueId");
        reference.child(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestData requestData = snapshot.getValue(RequestData.class);
                if(requestData!= null){
                    surname = requestData.getSurname();
                    name = requestData.getName();
                    location = requestData.getLocation();
                    image = requestData.getImage();
                    image1 = requestData.getCarImage();
                    title = requestData.getTittle();
                    price = requestData.getPrice();

                    binding.username1.setText(surname+" "+name);
                    binding.location1.setText(location);
                    binding.title1.setText(title);
                    binding.price1.setText(String.valueOf("R"+price+".00"));

                    try{
                        Picasso.get().load(image).into(binding.clientImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    try{
                        Picasso.get().load(image1).into(binding.carImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getLastLocation() {


        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("RequstData");


        reference.child(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestData requestData = snapshot.getValue(RequestData.class);
                if (requestData != null) {

                    double latitude, longitude;
                    latitude = requestData.getLatitude();
                    longitude = requestData.getLongitude();

                    String latLang = latitude + "," + longitude;

                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("google.navigation:q=" + latLang + "&model=1"));

                    intent.setPackage("com.google.android.apps.maps");
                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivity(intent);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}