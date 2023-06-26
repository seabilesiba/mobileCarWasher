package com.example.splash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.Driver.AcceptedRequestActivity;
import com.example.splash.R;
import com.example.splash.model.RequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

public class AllRequestAdapter extends RecyclerView.Adapter<AllRequestAdapter.AllRequestViewHolder> {
    private List<RequestData> list;
    private Context context;

    public AllRequestAdapter(List<RequestData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AllRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.all_request_items,
                parent, false);
        return new AllRequestViewHolder(view);
    }
    public void filterList(List<RequestData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull AllRequestViewHolder holder, int position) {
        RequestData items = list.get(position);
        holder.txtClientUserName.setText("Client: "+items.getSurname()+" "+items.getName());
        //holder.txtClientEmail.setText(items.getEmail());
        //holder.txtClientPhone.setText(items.getPhone());
        holder.txtClientLocation.setText("Location: "+items.getLocation());

        try {
            Picasso.get().load(items.getImage()).into(holder.imgClientProfile);
        }catch (Exception e){
            e.printStackTrace();
        }


        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AcceptedRequestActivity.class);
                DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("AcceptedRequest");

                RequestData requestData = new RequestData(items.getUniqueId(),items.getImage(),items.getName()
                        ,items.getSurname(),items.getLocation(),items.getLatitude(),items.getLongitude());
                reference.child(items.getUniqueId()).setValue(requestData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(context, "successful", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
                //if accepted remove client in a list

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AllRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView imgClientProfile;
        TextView txtClientUserName,txtClientEmail,txtClientPhone,txtClientLocation;
        Button btnAccept;
        public AllRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            imgClientProfile = itemView.findViewById(R.id.imgClientProfile);
            txtClientUserName = itemView.findViewById(R.id.txtClientUserName);
            //txtClientEmail = itemView.findViewById(R.id.txtClientEmail);
            //txtClientPhone = itemView.findViewById(R.id.txtClientPhone);
            txtClientLocation = itemView.findViewById(R.id.txtClientLocation);
            btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }
}
