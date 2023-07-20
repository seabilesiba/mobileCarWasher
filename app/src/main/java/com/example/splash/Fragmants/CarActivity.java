package com.example.splash.Fragmants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.splash.Adapters.CarAdapter;
import com.example.splash.Admin.AddCarActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityCarBinding;
import com.example.splash.model.AddCarData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CarActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private List<AddCarData> list;
    private CarAdapter adapter;


    private ActivityCarBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // inputSearch = view.findViewById(R.id.inputSearch);
       /* list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            list.add(Integer.toString(i).concat(" item"));
        }
        adapter = new CarAdapter(list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCars.setLayoutManager(linearLayoutManager);
        rvCars.setAdapter(adapter);*/
        /*inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });*/
        getData();
        binding.fabCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddCarActivity.class));
            }
        });

    }
    private void filter(String text) {

        List<AddCarData> filterList = new ArrayList<>();
        for(AddCarData items : list){
            if(items.getTitle().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }
    private void getData() {

        reference = FirebaseDatabase.getInstance().getReference("AddCarData");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();


                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    AddCarData addCarData = dataSnapshot.getValue(AddCarData.class);
                    list.add(addCarData);


                }
                adapter = new CarAdapter(list, getApplicationContext());
                binding.rvCars.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                binding.rvCars.setAdapter(adapter);
                binding.rvCars.setVisibility(View.VISIBLE);
                binding.pd.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Something went Wrong "+ error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
}