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

public class DriverAdapter extends RecyclerView.Adapter<DriverAdapter.DriversViewHolder> {

    private List<String> list;
    private Context context;

    public DriverAdapter(List<String> list) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DriversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_items,parent,false);
        return new DriversViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DriversViewHolder holder, int position) {

        holder.txtCount.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DriversViewHolder extends RecyclerView.ViewHolder {

        TextView txtCount;
        public DriversViewHolder(@NonNull View itemView) {
            super(itemView);

            txtCount = itemView.findViewById(R.id.txtCount);
        }

    }
}
