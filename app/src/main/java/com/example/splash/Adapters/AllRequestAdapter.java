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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.Driver.AcceptedRequestActivity;
import com.example.splash.Driver.RequestActivity;
import com.example.splash.R;
import com.example.splash.model.RequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.HashMap;
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
        holder.txtClientLocation.setText("Location: "+items.getLocation());

        try {
            Picasso.get().load(items.getImage()).into(holder.imgClientProfile);
        }catch (Exception e){
            e.printStackTrace();
        }




        DatabaseReference mdRef = FirebaseDatabase.getInstance().getReference("RequstData");
        mdRef.child(items.getUniqueId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestData requestData = snapshot.getValue(RequestData.class);
                if(requestData!= null){
                    String status = requestData.getStatus();

                    if (status.equals("Accepted")) {
                        holder.btnAccept.setVisibility(View.GONE);
                        holder.imgTick.setVisibility(View.VISIBLE);
                        holder.cvContainer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, RequestActivity.class);
                                intent.putExtra("uniqueId", items.getUniqueId());
                                intent.putExtra("image", items.getImage());
                                intent.putExtra("name", items.getName());
                                intent.putExtra("surname", items.getSurname());
                                intent.putExtra("location", items.getLocation());
                                intent.putExtra("latitude", items.getLatitude());
                                intent.putExtra("longitude", items.getLongitude());
                                context.startActivity(intent);
                            }
                        });
                    }else{
                        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(context, RequestActivity.class);

                                intent.putExtra("uniqueId", items.getUniqueId());
                                intent.putExtra("image", items.getImage());
                                intent.putExtra("name", items.getName());
                                intent.putExtra("surname", items.getSurname());
                                intent.putExtra("location", items.getLocation());
                                intent.putExtra("latitude", items.getLatitude());
                                intent.putExtra("longitude", items.getLongitude());

                                DatabaseReference dRef = FirebaseDatabase.getInstance()
                                        .getReference("RequstData");

                                String status = "Accepted";

                                HashMap hashMap = new HashMap();
                                hashMap.put("status",status);
                                dRef.child(items.getUniqueId()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        Toast.makeText(context, "Status updated successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "failed"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });

                                DatabaseReference reference = FirebaseDatabase.getInstance()
                                        .getReference("AcceptedRequest");

                                intent.putExtra("uniqueId",items.getUniqueId());

                                RequestData requestData = new RequestData(items.getUniqueId(),items.getImage(),items.getName()
                                        ,items.getSurname(),items.getNumber(), items.getEmail(),items.getLocation(),
                                        items.getStatus(),items.getCarImage(),items.getTittle(),items.getDate(),
                                        items.getPrice(),items.getLatitude(),items.getLongitude());
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

                                context.startActivity(intent);
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /* DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("RequstData");
        databaseReference.child(items.getUniqueId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RequestData requestData = snapshot.getValue(RequestData.class);
                if(requestData==null){
                    context.startActivity(new Intent(context, RequestActivity.class));
                }else{
                    holder.btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {


                            Intent intent = new Intent(context, RequestActivity.class);
                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("AcceptedRequest");

                            intent.putExtra("uniqueId",items.getUniqueId());

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


                           /* DatabaseReference mdRef = FirebaseDatabase.getInstance()
                                    .getReference("RequstData");
                            mdRef.child(items.getUniqueId()).removeValue()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()) {
                                                Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                                                // Intent intent = new Intent(UpdateActivity.this,SignInActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                context.startActivity(intent);
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



                            context.startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AllRequestViewHolder extends RecyclerView.ViewHolder {

        ImageView imgClientProfile,imgTick;
        TextView txtClientUserName,txtClientEmail,txtClientPhone,txtClientLocation;
        Button btnAccept;
        CardView cvContainer;
        public AllRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            imgClientProfile = itemView.findViewById(R.id.imgClientProfile);
            imgTick = itemView.findViewById(R.id.imgTick);
            txtClientUserName = itemView.findViewById(R.id.txtClientUserName);
            txtClientLocation = itemView.findViewById(R.id.txtClientLocation);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            cvContainer = itemView.findViewById(R.id.cvContainer);
        }
    }
}
