package com.example.splash.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.splash.Fragmants.CarsFragment;
import com.example.splash.Fragmants.DriverFragment;

public class ViewAdapter extends FragmentStateAdapter {
    public ViewAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
       switch (position){
           case 0:
               return new CarsFragment();
           case 1:
               return new DriverFragment();
           default:
               return new CarsFragment();

       }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
