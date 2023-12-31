package com.example.splash.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Adapters.ViewAdapter;
import com.example.splash.R;
import com.example.splash.databinding.ActivityHomeBinding;
import com.example.splash.model.AddCarData;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {

    private ViewAdapter adapter;
    private ActivityHomeBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new ViewAdapter(this);
        binding.ViewPager.setAdapter(adapter);



       binding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.drawLayout.openDrawer(GravityCompat.START);
            }
        });

       binding.imgSearch.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               binding.inputSearch.setVisibility(View.VISIBLE);
               binding.txtHome.setVisibility(View.GONE);

           }
       });
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // int id = item.getItemId();
                binding.drawLayout.closeDrawer(GravityCompat.START);


                if(item.getItemId() == R.id.home){
                    Toast.makeText(HomeActivity.this, "Home", Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId() == R.id.profile){
                    Toast.makeText(HomeActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                    //startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }
                if(item.getItemId() == R.id.logout){
                    Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId() == R.id.Accepted){
                    //Toast.makeText(HomeActivity.this, "Logout", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), AcceptedClientActivity.class));
                }

                return false;
            }
        });



        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                binding.tabLayout, binding.ViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {


                switch (position){

                    case 0:
                        tab.setText("Cars");
                        tab.setIcon(R.drawable.baseline_directions_car_24);
                        BadgeDrawable badgeDrawable1 = tab.getOrCreateBadge();
                        badgeDrawable1.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(),R.color.error)
                        );
                        badgeDrawable1.setVisible(true);
                        DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("AddCarData");
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String amount ;
                                int count = 0;
                                if(snapshot.exists()){
                                    count = (int) snapshot.getChildrenCount();
                                    badgeDrawable1.setNumber(count);
                                    badgeDrawable1.setMaxCharacterCount(2);

                                }else{
                                    Toast.makeText(HomeActivity.this, "No products in the shop", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        break;
                    case 1:
                        tab.setText("Drivers");
                        tab.setIcon(R.drawable.baseline_person_2_24);
                        BadgeDrawable badgeDrawable = tab.getOrCreateBadge();
                        badgeDrawable.setBackgroundColor(
                                ContextCompat.getColor(getApplicationContext(),R.color.error)
                                );
                        badgeDrawable.setVisible(true);
                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                .getReference("DriverData");
                        reference1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                String amount ;
                                int count = 0;
                                if(snapshot.exists()){
                                    count = (int) snapshot.getChildrenCount();
                                    badgeDrawable.setNumber(count);
                                    badgeDrawable.setMaxCharacterCount(2);

                                }else{
                                    Toast.makeText(HomeActivity.this, "No products in the shop", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        break;
                    default:
                }
            }
        }
        );
        tabLayoutMediator.attach();

       // replaceFragment(new DriverFragment());

    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ViewPager,fragment);
        fragmentTransaction.commit();

    }
}