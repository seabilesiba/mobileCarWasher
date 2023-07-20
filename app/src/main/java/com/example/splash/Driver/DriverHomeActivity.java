package com.example.splash.Driver;

import static com.example.splash.Constants.TOPIC;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.splash.Adapters.ViewAdapter;
import com.example.splash.Admin.HomeActivity;
import com.example.splash.ApiUtilities;
import com.example.splash.ControlActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverHomeBinding;
import com.example.splash.databinding.ActivityDriverLoginBinding;
import com.example.splash.databinding.ActivityHomeBinding;
import com.example.splash.model.DriverData;
import com.example.splash.model.NotificationData;
import com.example.splash.model.PushNotification;
import com.example.splash.model.RequestData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DriverHomeActivity extends AppCompatActivity implements OnMapReadyCallback {

    boolean isPermissionGranted;
    GoogleMap googleMp;
    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationProviderClient;

    private ViewAdapter adapter;
    private ActivityDriverHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDriverHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        navHeader();
        //CreatepopUpwindow();
        adapter = new ViewAdapter(this);
        // binding.ViewPager.setAdapter(adapter);

        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);

        //PushNotification notification = new PushNotification(new NotificationData("Hello","Check your requests"), TOPIC);
        sendNotification();


        binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawLayout.openDrawer(GravityCompat.START);
            }
        });

        /*binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.inputSearch.setVisibility(View.VISIBLE);
                binding.txtHome.setVisibility(View.GONE);

            }
        });*/

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // int id = item.getItemId();
                binding.drawLayout.closeDrawer(GravityCompat.START);

                if (item.getItemId() == R.id.home) {
                    startActivity(new Intent(getApplicationContext(),DriverHomeActivity.class));
                }
                if (item.getItemId() == R.id.profile) {
                    startActivity(new Intent(getApplicationContext(),DriverProfileActivity.class));
                }
                if (item.getItemId() == R.id.logout) {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(DriverHomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), ControlActivity.class));
                }
                if (item.getItemId() == R.id.AllRequest) {
                    startActivity(new Intent(getApplicationContext(), AllRequestActivity.class));
                }


                return false;
            }
        });


        checkPermission();
        if (isPermissionGranted) {
            if (checkGooglePlayService()) {
                SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
                getSupportFragmentManager().beginTransaction().add(R.id.Container, supportMapFragment).commit();
                supportMapFragment.getMapAsync(this);
                if (isPermissionGranted) {
                     CheckGps();
                }

            } else {
                Toast.makeText(this, "Google Play services is not Available", Toast.LENGTH_SHORT).show();
            }
        }

        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.inputSearch.setVisibility(View.VISIBLE);
                // binding.txtHome.setVisibility(View.GONE);
                String location = binding.inputSearch.getText().toString();
                if (location == null) {
                    Toast.makeText(DriverHomeActivity.this, "Please enter the location", Toast.LENGTH_SHORT).show();
                } else {
                    Geocoder geocoder = new Geocoder(DriverHomeActivity.this, Locale.getDefault());
                    try {
                        List<Address> listAddress = geocoder.getFromLocationName(location, 1);
                        if (listAddress.size() > 0) {
                            LatLng latLng = new LatLng(listAddress.get(0).getLatitude(), listAddress.get(0).getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.title("My search position");
                            markerOptions.position(latLng);
                            googleMp.addMarker(markerOptions);
                            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                            googleMp.animateCamera(cameraUpdate);

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    private boolean checkGooglePlayService() {

        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int result = googleApiAvailability.isGooglePlayServicesAvailable(this);
        if (result == ConnectionResult.SUCCESS) {
            return true;

        } else if (googleApiAvailability.isUserResolvableError(result)) {

            Dialog dialog = googleApiAvailability.getErrorDialog(this, result, 201, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    Toast.makeText(DriverHomeActivity.this, "User Cancelled Dialog", Toast.LENGTH_SHORT).show();
                }
            });
            dialog.show();

        }

        return false;
    }

    private void checkPermission() {
        Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
            @Override
            public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                isPermissionGranted = true;
               // Toast.makeText(DriverHomeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), "");
                intent.setData(uri);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMp = googleMap;
        googleMp.getUiSettings().setMyLocationButtonEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        googleMp.setMyLocationEnabled(true);

        googleMp.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                CheckGps();
                return true;
            }
        });







        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RequstData");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    RequestData requestData = dataSnapshot.getValue(RequestData.class);
                    double latitude,longitude;
                    latitude = requestData.getLatitude();
                    longitude = requestData.getLongitude();

                    LatLng latLng = new LatLng(latitude, longitude);
                    MarkerOptions markerOptions = new MarkerOptions();
                    
                    markerOptions.title("Client Location \n" );
                    markerOptions.position(latLng);
                    googleMap.addMarker(markerOptions);

                    //Toast.makeText(DriverHomeActivity.this, markerOptions.toString(), Toast.LENGTH_SHORT).show();


                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                    googleMap.animateCamera(cameraUpdate);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

    private void CheckGps() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //locationRequest.setInterval(5000);
        //locationRequest.setFastestInterval(3000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        Task<LocationSettingsResponse> locationSettingsRequestTask;
        locationSettingsRequestTask = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        locationSettingsRequestTask.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    GteCurrentUpdate();


                   // Toast.makeText(DriverHomeActivity.this, "Gps is already enabled", Toast.LENGTH_SHORT).show();

                } catch (ApiException e) {
                    if (e.getStatusCode() == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                        ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                        try {
                            resolvableApiException.startResolutionForResult(DriverHomeActivity.this, 101);
                        } catch (IntentSender.SendIntentException sendIntentException) {
                            sendIntentException.printStackTrace();
                        }
                    }
                    if (e.getStatusCode() == LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE) {
                        Toast.makeText(DriverHomeActivity.this, "Gps is unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }

    private void GteCurrentUpdate() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(DriverHomeActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);


                LatLng latLng = new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.title("Current Location " );
                markerOptions.position(latLng);
                googleMp.addMarker(markerOptions);

                //Toast.makeText(DriverHomeActivity.this, markerOptions.toString(), Toast.LENGTH_SHORT).show();


                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                googleMp.animateCamera(cameraUpdate);

            }
        }, Looper.getMainLooper());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Now Gps is enabled", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Gps Cancelled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu_one) {
        getMenuInflater().inflate(R.menu.menu_one, menu_one);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.noneMap) {
            googleMp.setMapType(GoogleMap.MAP_TYPE_NONE);
        }
        if(item.getItemId() == R.id.normalMap){
            googleMp.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if(item.getItemId() == R.id.satelliteMap){
            googleMp.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if(item.getItemId() == R.id.mapHybridMap){
            googleMp.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
        if(item.getItemId() == R.id.mapTerrain){
            googleMp.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        return super.onOptionsItemSelected(item);

    }
    private void navHeader(){

        View headerView = binding.navigationView.getHeaderView(0);

        ImageView imageProfile = headerView.findViewById(R.id.imageProfile);
        TextView txtName = headerView.findViewById(R.id.txtName);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("DriverData");
        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DriverData driverData = snapshot.getValue(DriverData.class);
                if(driverData!=null){
                    String image1 = driverData.getImage();
                    String name = driverData.getDriverName();
                    String surname = driverData.getSurname();

                    try{
                        Picasso.get().load(image1).into(imageProfile);
                        //Toast.makeText(DriverHomeActivity.this, image1, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    txtName.setText(surname+" "+name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    @SuppressLint("MissingInflatedId")
    private void CreatepopUpwindow() {
        LayoutInflater inflater= (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View popUpView=inflater.inflate(R.layout.driver_popup_item,null);

        int width= ViewGroup.LayoutParams.MATCH_PARENT;
        int height=ViewGroup.LayoutParams.MATCH_PARENT;
        boolean focusable=true;
        PopupWindow popupWindow=new PopupWindow(popUpView,width,height,focusable);
        binding.Container.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(binding.Container, Gravity.BOTTOM,0,0);

            }
        });



        TextView Skip ,Gotit,userName,location;
        ImageView clear,profile;
        CircleImageView circleImageView;
        Skip=popUpView.findViewById(R.id.Skip);
        Gotit=popUpView.findViewById(R.id.Gotit);
        userName =popUpView.findViewById(R.id.txtUsername);
        location =popUpView.findViewById(R.id.txtLocaton);
        profile =popUpView.findViewById(R.id.imgClient);
        circleImageView =popUpView.findViewById(R.id.clear);


        //Toast.makeText(this, "Lesiba", Toast.LENGTH_SHORT).show();


        RequestData requestData = new RequestData();

        String name = requestData.getLocation();

        Toast.makeText(this, name, Toast.LENGTH_SHORT).show();

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("RequstData");
        //FirebaseAuth auth = FirebaseAuth.getInstance();

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    RequestData requestData1 = dataSnapshot.getValue(RequestData.class);

                    String clientName,clientSurname,clientImage,clientLocation;
                    clientName = requestData1.getName();
                    clientSurname = requestData1.getSurname();
                    clientLocation = requestData1.getLocation();
                    clientImage = requestData1.getImage();

                    userName.setText(clientSurname+" "+clientName);
                    location.setText("Location: "+clientLocation);

                    try {
                        Picasso.get().load(clientImage).into(profile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DriverHomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
       /* mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestData requestData = snapshot.getValue(RequestData.class);
                String clientName,clientSurname,clientImage,clientLocation;
                if(requestData != null){
                    clientName = requestData.getName();
                    clientSurname = requestData.getSurname();
                    clientLocation = requestData.getLocation();
                    clientImage = requestData.getImage();

                    userName.setText(clientSurname+" "+clientName);
                    location.setText("Location: "+clientLocation);
                    Toast.makeText(getApplicationContext(), "Lesiba", Toast.LENGTH_SHORT).show();

                    try {
                        Picasso.get().load(clientImage).into(profile);
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    Toast.makeText(DriverHomeActivity.this, "Empty 3", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(DriverHomeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });*/


        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        Gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // write code anything you want

            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        // and if you want to close popup when touch Screen
        popUpView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                //popupWindow.dismiss();
                //Toast.makeText(DriverHomeActivity.this, "Lesiba", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

    }
    private void sendNotification() {
        PushNotification notification = null;
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful()) {

                    Toast.makeText(DriverHomeActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DriverHomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(DriverHomeActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
