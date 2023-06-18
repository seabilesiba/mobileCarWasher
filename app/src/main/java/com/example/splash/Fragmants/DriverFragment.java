package com.example.splash.Fragmants;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.splash.Adapters.DriverAdapter;
import com.example.splash.R;

import java.util.ArrayList;
import java.util.List;


public class DriverFragment extends Fragment {


    private List<String> list;
    private DriverAdapter adapter;
    private RecyclerView rvDrivers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_driver, container, false);
        // Inflate the layout for this fragment

        rvDrivers = view.findViewById(R.id.rvDrivers);
        list = new ArrayList<>();
        for (int i = 0; i < 10; i++){
            list.add(Integer.toString(i).concat(" item"));
        }
        adapter = new DriverAdapter(list);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvDrivers.setLayoutManager(linearLayoutManager);
        rvDrivers.setAdapter(adapter);
        return view;
    }
}