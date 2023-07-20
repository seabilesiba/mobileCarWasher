package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverFullImageBinding;
import com.example.splash.model.DriverData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DriverFullImageActivity extends AppCompatActivity {

    private ActivityDriverFullImageBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverFullImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DriverData");

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DriverData driverData = snapshot.getValue(DriverData.class);
                        if (driverData!=null){
                            String name = driverData.getDriverName();
                            String surname = driverData.getSurname();
                            String image = driverData.getImage();

                            binding.txtDriverName.setText(surname+" "+name);
                           try{
                                Picasso.get().load(image).into( binding.imgFull);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            /*SubsamplingScaleImageView image1 = (SubsamplingScaleImageView) binding.imgFull;
                            image1.setImage(ImageSource.uri(image));*/

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}