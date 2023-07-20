package com.example.splash.Admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Driver.DriverProfileActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityUpdateCarBinding;
import com.example.splash.model.AddCarData;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

public class UpdateCarActivity extends AppCompatActivity {

    private DatabaseReference mRf;
    private StorageReference storageReference;
    private ActivityUpdateCarBinding binding;
    private String image,key,title,numberP,edtTitle,edtNumPlate;
    private Bitmap bitmap;
    private  final int REQ = 1;
    String downloadUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUpdateCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRf = FirebaseDatabase.getInstance().getReference("AddCarData");
        storageReference = FirebaseStorage.getInstance().getReference("AddCarData");

        image = getIntent().getStringExtra("image");
        key = getIntent().getStringExtra("key");
        title = getIntent().getStringExtra("title");
        numberP = getIntent().getStringExtra("numberP");

       binding.edtTitle.setText(title);
       binding.edtNumPlate.setText(numberP);

       try{
           Picasso.get().load(image).into(binding.carImage);
       }catch (Exception e){
           e.printStackTrace();
       }


       binding.carImage.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               openGallery();
           }
       });

       binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               isValidDetails();
           }
       });

    }
    private void openGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent,REQ);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK){
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            }catch (Exception exception){
                exception.printStackTrace();
            }

            binding.carImage.setImageBitmap(bitmap);
        }
    }
    private void isValidDetails() {

        edtTitle = binding.edtTitle.getText().toString().trim();
        edtNumPlate = binding.edtNumPlate.getText().toString().trim();


        if (edtTitle.isEmpty()) {
            Toast.makeText(this, "Please enter car name", Toast.LENGTH_SHORT).show();
        } else if (edtNumPlate.isEmpty()) {
            Toast.makeText(this, "Please enter car number plate", Toast.LENGTH_SHORT).show();
        }else if (bitmap == null) {
            UpdateUserProfile(image);

        }else {
            uploadImage();
        }
    }
    private void uploadImage(){

        loading(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50,baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalimg+"jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(UpdateCarActivity.this, task -> {
            if(task.isSuccessful()){
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = String.valueOf(uri);
                            UpdateUserProfile(downloadUrl);
                            loading(false);

                        });
                    }
                });
            }else {
                Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                loading(false);
            }
        });
    }
    private void UpdateUserProfile(String s) {
        loading(true);
        
        HashMap hashMap = new HashMap();
        
        hashMap.put("title",edtTitle);
        hashMap.put("numberP",numberP);
        hashMap.put("image",s);

        mRf.child(key).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(UpdateCarActivity.this, "Car information updated successfully",
                        Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                loading(false);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UpdateCarActivity.this, "Something went wrong: "
                        +e.getMessage(), Toast.LENGTH_SHORT).show();
                loading(false);
            }
        });



    }



    private void loading(Boolean isLoading){
        if(isLoading){
            binding.btnUpdate.setVisibility(View.INVISIBLE);
            binding.pd.setVisibility(View.VISIBLE);
        }else{
            binding.btnUpdate.setVisibility(View.VISIBLE);
            binding.pd.setVisibility(View.INVISIBLE);
        }
    }
}