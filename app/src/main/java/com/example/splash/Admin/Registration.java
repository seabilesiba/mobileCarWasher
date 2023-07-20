package com.example.splash.Admin;

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

import com.example.splash.Driver.DriverLoginActivity;
import com.example.splash.Driver.DriverRegistrationsActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityRegistrationBinding;
import com.example.splash.model.DriverData;
import com.example.splash.model.OwnerData;
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

public class Registration extends AppCompatActivity {

    private ActivityRegistrationBinding binding;

    private FirebaseAuth auth;
    private FirebaseUser users;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private final int REQ = 1;
    private Bitmap bitmap;
    String downloadUrl;
    private String key, CompanyName, RegNumber, OwnerEmail, OwnerPhone, OwnerPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        users = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("OwnerData");
        storageReference = FirebaseStorage.getInstance().getReference("OwnerData");


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

        binding.btnOwnerRegistration.setOnClickListener(view -> {
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
            binding.btnOwnerRegistration.setVisibility(View.INVISIBLE);
            binding.pd.setVisibility(View.VISIBLE);
        } else {
            binding.btnOwnerRegistration.setVisibility(View.VISIBLE);
            binding.pd.setVisibility(View.INVISIBLE);
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ);
    }

    private void checkPassword() {
        binding.chShowPass.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                binding.edtOwnerPassword.setTransformationMethod(null);
            } else {
                binding.edtOwnerPassword.setTransformationMethod(new PasswordTransformationMethod());
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
            binding.textAddImage.setVisibility(View.GONE);
        }
    }

    private Boolean isValidDetails() {
        CompanyName = binding.edtCompanyName.getText().toString().trim();
        RegNumber = binding.edtRegNumber.getText().toString().trim();
        OwnerEmail = binding.edtOwnerEmail.getText().toString().trim();
        OwnerPhone = binding.edtOwnerPhone.getText().toString().trim();
        OwnerPassword = binding.edtOwnerPassword.getText().toString().trim();



        if (bitmap == null) {
            showMessage("Please select image");
            return false;
        } else if (CompanyName.isEmpty()) {
            showMessage("Please enter company name");
            return false;
        } else if (RegNumber.isEmpty()) {
            showMessage("Please company registration Number");
            return false;
        } else if (OwnerPhone.isEmpty()) {
            showMessage("Please enter your phone number");
            return false;
        } else if (OwnerPhone.length() != 10 && OwnerPhone.length() < 10) {
            showMessage("Cellphone number have 10 digits");
            return false;
        } else if (!Patterns.PHONE.matcher(OwnerPhone).matches()) {
            showMessage("Please enter a valid cellphone number");
            return false;
        }  else if (OwnerEmail.isEmpty()) {
            showMessage("Please enter your Email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(OwnerEmail).matches()) {
            showMessage("Please enter valid  email Email address");
            return false;
        } else if (OwnerPassword.isEmpty()) {
            showMessage("Please enter a password");
            return false;
        } else if (OwnerPassword.length() != 6 && OwnerPassword.length() > 6) {
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
        uploadTask.addOnCompleteListener(Registration.this, task -> {
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
        auth.createUserWithEmailAndPassword(OwnerEmail, OwnerPhone).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                OwnerData ownerData = new OwnerData(FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        downloadUrl, CompanyName, RegNumber, OwnerEmail, OwnerPhone, OwnerPassword);

                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(ownerData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(getApplicationContext(), LogInActivity.class));
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