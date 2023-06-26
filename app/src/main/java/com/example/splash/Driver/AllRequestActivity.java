package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Adapters.AllRequestAdapter;
import com.example.splash.R;
import com.example.splash.databinding.ActivityAllRequestBinding;
import com.example.splash.model.RequestData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRequestActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private List<RequestData> list;
    private AllRequestAdapter adapter;
    private ActivityAllRequestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllRequestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtHome.setVisibility(View.GONE);
                binding.inputSearch.setVisibility(View.VISIBLE);
            }
        });

        reference = FirebaseDatabase.getInstance().getReference().child("RequstData");

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
   /* private void filter(String text) {

        List<RequestData> filterList = new ArrayList<>();
        for(RequestData items : list){
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

                    RequestData requestData = dataSnapshot.getValue(RequestData.class);
                    list.add(requestData);


                }
                adapter = new AllRequestAdapter(list, AllRequestActivity.this);
                binding.rcAllRequest.setLayoutManager(new LinearLayoutManager(AllRequestActivity.this));
                binding.rcAllRequest.setAdapter(adapter);
                binding.rcAllRequest.setVisibility(View.VISIBLE);
                binding.pd.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Something went Wrong "+ error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
}
