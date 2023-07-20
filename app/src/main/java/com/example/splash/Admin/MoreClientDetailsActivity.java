package com.example.splash.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.splash.R;
import com.example.splash.databinding.ActivityMoreClientDetailsBinding;
import com.example.splash.model.AcceptedData;
import com.example.splash.model.RequestData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MoreClientDetailsActivity extends AppCompatActivity {

    private ActivityMoreClientDetailsBinding binding;

    private String uniqueId,image,name,surname,number,email,location,status,date;

    private int price;
    private double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMoreClientDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        uniqueId = getIntent().getStringExtra("uniqueId");
        image = getIntent().getStringExtra("image");
        name = getIntent().getStringExtra("name");
        surname = getIntent().getStringExtra("surname");
        number = getIntent().getStringExtra("number");
        email = getIntent().getStringExtra("email");
        location = getIntent().getStringExtra("location");
        email = getIntent().getStringExtra("email");
        date = getIntent().getStringExtra("date");
        price = getIntent().getIntExtra("price",price);

        binding.txtClientName.setText(surname+" "+name);
        binding.txtEmail1.setText(email);
        binding.txtPhone1.setText(number);
        binding.txtCarPrice1.setText(String.valueOf("R"+price+".00"));
        binding.txtClientLocation1.setText(location);
        binding.txtDate1.setText(date);


        DatabaseReference reference = FirebaseDatabase.getInstance()
                .getReference("RequstData");

        reference.child(uniqueId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestData requestData = snapshot.getValue(RequestData.class);
                if(requestData!=null){
                    String carType = requestData.getTittle();
                    String imgCar = requestData.getCarImage();
                    binding.txtCarType1.setText(carType);
                    try{
                        Picasso.get().load(imgCar).into(binding.carImage);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MoreClientDetailsActivity.this, "something went wrong "+
                        error.getDetails(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}