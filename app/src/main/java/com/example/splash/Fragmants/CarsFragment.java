package com.example.splash.Fragmants;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.splash.Adapters.AllRequestAdapter;
import com.example.splash.Adapters.CarAdapter;
import com.example.splash.Adapters.DriverAdapter;
import com.example.splash.Admin.AddCarActivity;
import com.example.splash.Driver.AllRequestActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityAllRequestBinding;
import com.example.splash.model.AddCarData;
import com.example.splash.model.RequestData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class CarsFragment extends Fragment {

    private DatabaseReference reference;
    private List<AddCarData> list;
    private CarAdapter adapter;

    private RecyclerView rvCars;
    private ProgressBar pd;
    private EditText inputSearch;
    private FloatingActionButton fb;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_cars, container, false);

        View view = inflater.inflate(R.layout.fragment_cars, container, false);
        // Inflate the layout for this fragment

        rvCars = view.findViewById(R.id.rvCars);
        pd = view.findViewById(R.id.pd);
        fb = view.findViewById(R.id.fabCars);
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
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddCarActivity.class));
            }
        });
        return view;
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
                adapter = new CarAdapter(list, getContext());
                rvCars.setLayoutManager(new LinearLayoutManager(getContext()));
                rvCars.setAdapter(adapter);
                rvCars.setVisibility(View.VISIBLE);
                pd.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), "Something went Wrong "+ error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
}