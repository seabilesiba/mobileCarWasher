package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.splash.databinding.ActivityDriverRegistrationsBinding;
import com.example.splash.model.DriverData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class DriverRegistrationsActivity extends AppCompatActivity {

    private ActivityDriverRegistrationsBinding binding;
    private FirebaseAuth auth;
    private FirebaseUser users;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl;
    private String driverName, surname, phone, companyName, regNumber, driverEmail, driverPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverRegistrationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        auth = FirebaseAuth.getInstance();
        users = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("DriverData");
        storageReference = FirebaseStorage.getInstance().getReference("DriverData");


       /* binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/
        setListener();
        checkPassword();

    }

    private void setListener() {

        binding.btnRegister.setOnClickListener(view -> {
            if (isValidDetails()) {
                uploadImage();
            }


        });

        binding.imgProfile.setOnClickListener(view -> {
            openGallery();

        });

    }


    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.btnRegister.setVisibility(View.INVISIBLE);
            binding.pb.setVisibility(View.VISIBLE);
        } else {
            binding.btnRegister.setVisibility(View.VISIBLE);
            binding.pb.setVisibility(View.INVISIBLE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ);
    }

    private void checkPassword() {
        binding.chShowPass.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.DriverPassword.setTransformationMethod(null);
            } else {
                binding.DriverPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            binding.imgProfile.setImageBitmap(bitmap);
        }
    }

    private Boolean isValidDetails() {
        driverName = binding.DriverName.getText().toString().trim();
        surname = binding.DriverSurname.getText().toString().trim();
        phone = binding.DriverPhone.getText().toString().trim();
        companyName = binding.companyName.getText().toString().trim();
        driverEmail = binding.DriverEmail.getText().toString().trim();
        regNumber = binding.regNumber.getText().toString().trim();
        driverPassword = binding.DriverPassword.getText().toString().trim();


        if (bitmap == null) {
            showMessage("Please select image");
            return false;
        } else if (driverName.isEmpty()) {
            showMessage("Please enter your name");
            return false;
        } else if (surname.isEmpty()) {
            showMessage("Please enter your surname");
            return false;
        } else if (phone.isEmpty()) {
            showMessage("Please enter your phone number");
            return false;
        } else if (phone.length() != 10 && phone.length() < 10) {
            showMessage("Cellphone number have 10 digits");
            return false;
        } else if (!Patterns.PHONE.matcher(phone).matches()) {
            showMessage("Please enter a valid cellphone number");
            return false;
        } else if (companyName.isEmpty()) {
            showMessage("Please enter company name");
            return false;
        } else if (regNumber.isEmpty()) {
            showMessage("Please enter company registration number");
            return false;
        } else if (driverEmail.isEmpty()) {
            showMessage("Please enter your Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(driverEmail).matches()) {
            showMessage("Please enter valid  email Email address");
            return false;
        } else if (driverPassword.isEmpty()) {
            showMessage("Please enter a password");
            return false;
        } else if (driverPassword.length() != 6 && driverPassword.length() > 6) {
            showMessage("Password at least have 6 characters");
            return false;
        } else {
            return true;
        }
    }

    private void uploadImage() {

        loading(true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] finalimg = baos.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child(finalimg + "jpg");
        final UploadTask uploadTask = filePath.putBytes(finalimg);
        uploadTask.addOnCompleteListener(DriverRegistrationsActivity.this, task -> {
            if (task.isSuccessful()) {
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(uri -> {
                            downloadUrl = String.valueOf(uri);
                            signUp();
                            loading(false);

                        });
                    }
                });
            } else {
                showMessage("wrong");
                loading(false);
            }
        });
    }

    private void signUp() {
        loading(true);
        auth.createUserWithEmailAndPassword(driverEmail, driverPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                DriverData driverData = new DriverData(downloadUrl, driverName, surname, phone, companyName,
                        regNumber, driverEmail, driverPassword);

                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(driverData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), DriverLoginActivity.class));
                            loading(false);
                        } else {
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

        });
    }
}