package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverProfileBinding;
import com.example.splash.model.DriverData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class DriverProfileActivity extends AppCompatActivity {
    private DatabaseReference mRf;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private String userID;
    private Bitmap bitmap;
    private  final int REQ = 1;
    String downloadUrl;
    private String image,driverName,surname,phone,companyName,regNumber,driverEmail,driverPassword;
    private String image1;


    private ActivityDriverProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mRf = FirebaseDatabase.getInstance().getReference().child("DriverData");
        storageReference = FirebaseStorage.getInstance().getReference("DriverData");
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),DriverHomeActivity.class));
            }
        });
        binding.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DriverFullImageActivity.class));
            }
        });

        getData();

        binding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.pd1.setVisibility(View.VISIBLE);
                binding.btnDelete.setVisibility(View.GONE);

                AlertDialog.Builder builder = new AlertDialog.Builder(DriverProfileActivity.this);
                builder.setMessage("Do you really want to delete your profile?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                        mRf.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(DriverProfileActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(DriverProfileActivity.this, DriverLoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(intent);
                                            binding.pd1.setVisibility(View.GONE);
                                            binding.btnDelete.setVisibility(View.VISIBLE);
                                        }else{
                                            Toast.makeText(DriverProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                            binding.pd1.setVisibility(View.GONE);
                                            binding.btnDelete.setVisibility(View.VISIBLE);
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(DriverProfileActivity.this, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        binding.pd1.setVisibility(View.GONE);
                                        binding.btnDelete.setVisibility(View.VISIBLE);
                                    }
                                });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        binding.pd1.setVisibility(View.GONE);
                        binding.btnDelete.setVisibility(View.VISIBLE);

                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();



            }
        });

        updateProfile();


        binding.imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openGallery();
            }
        });

        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                isValidDetails();
               // loading(false);
                    //uploadImage();

            }
        });
    }

    private void getData() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DriverData");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DriverData driverData = snapshot.getValue(DriverData.class);
                if(driverData != null){
                    String name = driverData.getDriverName();
                    String surname = driverData.getSurname();
                    String phone = driverData.getPhone();
                    String email = driverData.getDriverEmail();
                    String image = driverData.getImage();

                    binding.txtUseName.setText(surname+" "+name);

                    binding.txtPhone.setText(phone);
                    binding.txtEmail.setText(email);
                    binding.txtName.setText(name);
                    binding.txtSurname.setText(surname);
                    try{
                        Picasso.get().load(image).into(binding.imgProfile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.layoutEditPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtPhone.setVisibility(View.GONE);
                binding.edtPhone.setVisibility(View.VISIBLE);
                binding.layoutEditPhone.setVisibility(View.GONE);
            }
        });
        binding.layoutEditEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtEmail.setVisibility(View.GONE);
                binding.edtEmail.setVisibility(View.VISIBLE);
                binding.layoutEditEmail.setVisibility(View.GONE);
            }
        });
        binding.layoutEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtName.setVisibility(View.GONE);
                binding.edtName.setVisibility(View.VISIBLE);
                binding.layoutEditName.setVisibility(View.GONE);
            }
        });
        binding.layoutEditSurname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSurname.setVisibility(View.GONE);
                binding.edtSurname.setVisibility(View.VISIBLE);
                binding.layoutEditSurname.setVisibility(View.GONE);

            }
        });


    }

    private void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
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

            binding.imgProfile.setImageBitmap(bitmap);
        }
    }

    private void isValidDetails() {

        phone = binding.edtPhone.getText().toString().trim();
        driverEmail = binding.edtEmail.getText().toString().trim();
        driverName = binding.edtName.getText().toString().trim();
        surname = binding.edtSurname.getText().toString().trim();

         if (surname.isEmpty()) {
            showMessage("Please enter your surname");
        } else if (driverName.isEmpty()) {
            showMessage("Please enter your name");
        } else if (phone.isEmpty()) {
            showMessage("Please enter your cellphone number");
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            showMessage("Please enter a valid cellphone number");
        } else if (phone.length() != 10 && phone.length() < 10) {
            showMessage("Cellphone number have 10 digits");
        }else if (driverEmail.isEmpty()) {
            showMessage("Please enter email address");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(driverEmail).matches()) {
            showMessage("Please enter a valid email address");
        }else if (bitmap == null) {
            //showMessage("Please select image");
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
        uploadTask.addOnCompleteListener(DriverProfileActivity.this, task -> {
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
                showMessage("Something went wrong");
                loading(false);
            }
        });
    }
    private void UpdateUserProfile(String s) {
        loading(true);
        //startActivity(new Intent(getApplicationContext(), SignInActivity.class));

        HashMap hashMap = new HashMap();

        hashMap.put("phone",phone);
        hashMap.put("driverEmail",driverEmail);
        hashMap.put("driverName",driverName);
        hashMap.put("surname",surname);
        hashMap.put("image",s);

        mRf.child(userID).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                showMessage("Profile updated successfully");
                loading(false);


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("something went wrong");
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
    private void updateProfile() {

        loading(true);
        mRf.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DriverData driverData = snapshot.getValue(DriverData.class) ;
                if(driverData!=null){
                    surname = driverData.getSurname();
                    phone = driverData.getPhone();
                    driverEmail = driverData.getDriverEmail();
                    driverName = driverData.getDriverName();
                    surname = driverData.getSurname();
                    image = driverData.getImage();

                    binding.edtPhone.setText(phone);
                    binding.edtEmail.setText(driverEmail);
                    binding.edtName.setText(driverName);
                    binding.edtSurname.setText(surname);

                    try {
                        Picasso.get().load(image).into(binding.imgProfile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    loading(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}