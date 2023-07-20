package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Adapters.AcceptedRequestAdapter;
import com.example.splash.Adapters.AllRequestAdapter;
import com.example.splash.R;
import com.example.splash.databinding.ActivityAcceptedRequestBinding;
import com.example.splash.model.AcceptedData;
import com.example.splash.model.RequestData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AcceptedRequestActivity extends AppCompatActivity {
    private DatabaseReference reference;
    private List<AcceptedData> list;
    private AcceptedRequestAdapter adapter;
    private String uniqueId,image,name,surname,location;
    private double latitude,longitude;

    private ActivityAcceptedRequestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAcceptedRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                binding.txtHome.setVisibility(View.GONE);
                binding.inputSearch.setVisibility(View.VISIBLE);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("AcceptedRequest");

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               // filter(editable.toString());
            }
        });

        getData();


    }
 /*   private void filter(String text) {

        List<AcceptedData> filterList = new ArrayList<>();
        for(AcceptedData items : list){
            if(items.getLocation().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }*/
    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();


                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    AcceptedData acceptedData = dataSnapshot.getValue(AcceptedData.class);
                    list.add(acceptedData);


                }
                adapter = new AcceptedRequestAdapter(list, AcceptedRequestActivity.this);
                binding.rcAcceptedRequest.setLayoutManager(new LinearLayoutManager(AcceptedRequestActivity.this));
                binding.rcAcceptedRequest.setAdapter(adapter);
                binding.rcAcceptedRequest.setVisibility(View.VISIBLE);
                binding.pd.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Something went Wrong "+ error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
}
