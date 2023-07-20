package com.example.splash.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.splash.Adapters.AcceptedClientAdapter;
import com.example.splash.Adapters.AllRequestAdapter;
import com.example.splash.Driver.AllRequestActivity;
import com.example.splash.R;
import com.example.splash.databinding.ActivityAcceptedClientBinding;
import com.example.splash.model.AcceptedData;
import com.example.splash.model.RequestData;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AcceptedClientActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private List<AcceptedData> list;
    private AcceptedClientAdapter adapter;
    private ActivityAcceptedClientBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityAcceptedClientBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference("AcceptedRequest");

        getData();
    }
    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                int amount = 0;
                int count = 0;
                if(snapshot.exists()){
                    count = (int) snapshot.getChildrenCount();

                }else{
                    Toast.makeText(AcceptedClientActivity.this, "No products in the shop", Toast.LENGTH_SHORT).show();
                }


                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    AcceptedData acceptedData = dataSnapshot.getValue(AcceptedData.class);
                    list.add(acceptedData);
                    amount+=acceptedData.getPrice();


                    binding.txtAmount.setText("Total amount is: R"+String.valueOf(amount)+".00"
                            +"\n with "+ String.valueOf(count)+ " client Requested ");


                }
                adapter = new AcceptedClientAdapter(list, AcceptedClientActivity.this);
                binding.rcAcceptedClientRequest.setLayoutManager(new LinearLayoutManager(AcceptedClientActivity.this));
                binding.rcAcceptedClientRequest.setAdapter(adapter);
                binding.rcAcceptedClientRequest.setVisibility(View.VISIBLE);
                binding.pd.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Something went Wrong "+ error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }
}