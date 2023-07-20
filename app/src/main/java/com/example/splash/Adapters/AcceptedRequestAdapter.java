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

import com.example.splash.Driver.ChatActivity;
import com.example.splash.R;
import com.example.splash.model.AcceptedData;
import com.example.splash.model.ChatData;
import com.example.splash.model.ConversationData;
import com.example.splash.model.RequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.AcceptedRequestViewHolder> {

    private List<AcceptedData> list;
    private Context context;

    public AcceptedRequestAdapter(List<AcceptedData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AcceptedRequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accepte_requst_items,
                parent, false);
        return new AcceptedRequestViewHolder(view);
    }

    public void filterList(List<AcceptedData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull AcceptedRequestViewHolder holder, int position) {
        AcceptedData items = list.get(position);
        holder.txtClientUserName.setText("Client: "+items.getSurname()+ " "+items.getName());
        holder.txtClientLocation.setText("Location: "+items.getLocation());

        try{
            Picasso.get().load(items.getImage()).into(holder.imgClientProfile);
        }catch (Exception e){
            e.printStackTrace();
        }

        holder.imgChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uniqueId",items.getUniqueId());
                intent.putExtra("image",items.getImage());
                intent.putExtra("name",items.getName());
                intent.putExtra("surname",items.getSurname());
                intent.putExtra("location",items.getLocation());
                intent.putExtra("latitude",items.getLatitude());
                intent.putExtra("longitude",items.getLongitude());

               /* DatabaseReference reference = FirebaseDatabase.getInstance()
                        .getReference("ChatData");

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                            ChatData chatData = dataSnapshot.getValue(ChatData.class);
                            if(chatData!= null){
                                String driverId = chatData.getDriverId();
                                String clientId = chatData.getClientId();
                                String message = chatData.getMessage();
                                String date = chatData.getDate();
                                String key = chatData.getKey();
                                DatabaseReference dRef = FirebaseDatabase.getInstance()
                                        .getReference("ConversationData");
                                final String conversationId = reference.push().getKey();
                                ConversationData conversationData = new ConversationData(conversationId,
                                        driverId,clientId,message,date);
                                dRef.child(conversationId).child(key).setValue(conversationData)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                        Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                    }
                });*/

                context.startActivity(intent);
            }
        });
        holder.txtVieLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AcceptedRequestViewHolder extends RecyclerView.ViewHolder {
        ImageView imgClientProfile,imgCall,imgChat;
        TextView txtClientUserName,txtVieLocation,txtClientPhone,txtClientLocation;
       // Button btnAccept;
        public AcceptedRequestViewHolder(@NonNull View itemView) {
            super(itemView);

            imgClientProfile = itemView.findViewById(R.id.imgClientProfile);
            txtClientUserName = itemView.findViewById(R.id.txtClientUserName);
            imgCall = itemView.findViewById(R.id.imgCall);
            imgChat = itemView.findViewById(R.id.imgChat);
            txtClientLocation = itemView.findViewById(R.id.txtClientLocation);
            txtVieLocation = itemView.findViewById(R.id.txtVieLocation);
            //btnAccept = itemView.findViewById(R.id.btnAccept);
        }
    }
}
