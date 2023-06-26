package com.example.splash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.Driver.ChatActivity;
import com.example.splash.R;
import com.example.splash.model.RequestData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AcceptedRequestAdapter extends RecyclerView.Adapter<AcceptedRequestAdapter.AcceptedRequestViewHolder> {

    private List<RequestData> list;
    private Context context;

    public AcceptedRequestAdapter(List<RequestData> list, Context context) {
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

    public void filterList(List<RequestData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull AcceptedRequestViewHolder holder, int position) {
        RequestData items = list.get(position);
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
