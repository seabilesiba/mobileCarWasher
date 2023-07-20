package com.example.splash.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.Admin.UpdateCarActivity;
import com.example.splash.Driver.DriverLoginActivity;
import com.example.splash.Driver.DriverProfileActivity;
import com.example.splash.R;
import com.example.splash.model.AddCarData;
import com.example.splash.model.RequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {

    private List<AddCarData> list;

    private Context context;

    public CarAdapter(List<AddCarData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cars_items,
                parent, false);
        return new CarViewHolder(view);
    }
    public void filterList(List<AddCarData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {

        AddCarData items = list.get(position);
        holder.txtCarName.setText("Car Name: "+items.getTitle());
        holder.txtNumPlate.setText("Number Plate: "+items.getNumberP());

        try{
            Picasso.get().load(items.getImage()).into(holder.carImage);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.txtUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateCarActivity.class);
                intent.putExtra("key", items.getKey());
                intent.putExtra("image", items.getImage());
                intent.putExtra("title", items.getTitle());
                intent.putExtra("numberP", items.getNumberP());
                context.startActivity(intent);
            }
        });
        holder.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Do you really want to delete your profile?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DatabaseReference mRf = FirebaseDatabase.getInstance()
                                .getReference("AddCarData");

                        mRf.child(items.getKey()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()) {
                                            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(context, DriverLoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                           // context.startActivity(intent);
                                        }else{
                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(context, "Something went wrong "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {



                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CarViewHolder extends RecyclerView.ViewHolder {

        TextView txtCarName,txtNumPlate,txtDelete,txtUpdate;
        ImageView carImage;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);

            txtNumPlate = itemView.findViewById(R.id.txtNumPlate);
            txtCarName = itemView.findViewById(R.id.txtCarName);
            carImage = itemView.findViewById(R.id.carImage);
            txtDelete = itemView.findViewById(R.id.txtDelete);
            txtUpdate = itemView.findViewById(R.id.txtUpdate);
        }
    }
}
