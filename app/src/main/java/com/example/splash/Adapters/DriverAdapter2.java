package com.example.splash.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.R;
import com.example.splash.model.AddCarData;
import com.example.splash.model.DriverData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DriverAdapter2 extends RecyclerView.Adapter<DriverAdapter2.DriversViewHolder> {

    private List<DriverData> list;
    private Context context;

    public DriverAdapter2(List<DriverData> list, Context context) {
        this.list = list;
        this.context = this.context;
    }

    @NonNull
    @Override
    public DriversViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.driver_item,parent,false);
        return new DriversViewHolder(view);
    }
    public void filterList(List<DriverData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull DriversViewHolder holder, int position) {
        DriverData items = list.get(position);

        holder.txtDriveName.setText("Driver: "+items.getSurname()+" "+items.getDriverName());
        holder.txtDrivePhone.setText("Phone: "+items.getPhone());
        holder.txtDriveEmail.setText("Email: "+items.getDriverEmail());

        try {
            Picasso.get().load(items.getImage()).into(holder.imgDriverProfile);
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class DriversViewHolder extends RecyclerView.ViewHolder {

       ImageView imgDriverProfile;
       TextView txtDriveName,txtDrivePhone,txtDriveEmail;
        public DriversViewHolder(@NonNull View itemView) {
            super(itemView);

            imgDriverProfile = itemView.findViewById(R.id.imgDriverProfile);
            txtDriveName = itemView.findViewById(R.id.txtDriveName);
            txtDrivePhone = itemView.findViewById(R.id.txtDrivePhone);
            txtDriveEmail = itemView.findViewById(R.id.txtDriveEmail);

        }

    }
}
