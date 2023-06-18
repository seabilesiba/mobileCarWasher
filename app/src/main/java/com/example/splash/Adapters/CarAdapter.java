package com.example.splash.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.R;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<String> list;

    private Context context;

    public CarAdapter(List<String> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_items
                ,parent,false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        holder.txtCount.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {

        TextView txtCount;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCount = itemView.findViewById(R.id.txtCount);
        }
    }
}
