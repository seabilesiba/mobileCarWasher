package com.example.splash.Fragmants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.splash.Adapters.CarAdapter;
import com.example.splash.Adapters.DriverAdapter;
import com.example.splash.R;

import java.util.ArrayList;
import java.util.List;


public class CarsFragment extends Fragment {

     private List<String> list;
     private CarAdapter adapter;
     private RecyclerView rvCars;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_cars, container, false);

        View view = inflater.inflate(R.layout.fragment_cars, container, false);
        // Inflate the layout for this fragment

        rvCars = view.findViewById(R.id.rvCars);
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            list.add(Integer.toString(i).concat(" item"));
        }
        adapter = new CarAdapter(list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvCars.setLayoutManager(linearLayoutManager);
        rvCars.setAdapter(adapter);
        return view;
    }
}