package com.example.splash.Driver;

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
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import com.example.splash.R;
import com.example.splash.databinding.ActivityDriverHomeBinding;
import com.example.splash.databinding.ActivityDriverLoginBinding;
import com.example.splash.databinding.ActivityHomeBinding;
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
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

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

        CreatepopUpwindow();
        adapter = new ViewAdapter(this);
        // binding.ViewPager.setAdapter(adapter);


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
                    Toast.makeText(DriverHomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.profile) {
                    Toast.makeText(DriverHomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }
                if (item.getItemId() == R.id.logout) {
                    Toast.makeText(DriverHomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DriverHomeActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
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

       /* googleMp.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
               // CheckGps();
                return true;
            }
        });*/


        LatLng latLng = new LatLng(-23.916041, 29.133656);
        MarkerOptions markerOptions = new MarkerOptions();

        markerOptions.title("My position");
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 0);
        googleMap.animateCamera(cameraUpdate);

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


                    Toast.makeText(DriverHomeActivity.this, "Gps is already enabled", Toast.LENGTH_SHORT).show();

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
                Toast.makeText(DriverHomeActivity.this, "Location:"+
                                locationResult.getLastLocation().getLatitude()+": "+
                                locationResult.getLastLocation().getLongitude() ,
                        Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        /*if (item.getItemId() == R.id.noneMap) {
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
        }*/
        return super.onOptionsItemSelected(item);

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
        TextView Skip ,Gotit;
        ImageView clear;
        CircleImageView circleImageView;
        Skip=popUpView.findViewById(R.id.Skip);
        Gotit=popUpView.findViewById(R.id.Gotit);
        circleImageView =popUpView.findViewById(R.id.clear);
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
                return true;
            }
        });

    }
}
