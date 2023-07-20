package com.example.splash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.Admin.MoreClientDetailsActivity;
import com.example.splash.R;
import com.example.splash.model.AcceptedData;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AcceptedClientAdapter extends RecyclerView.Adapter<AcceptedClientAdapter.AcceptedClientViewHolder> {

    private List<AcceptedData> list;
    private Context context;

    public AcceptedClientAdapter(List<AcceptedData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AcceptedClientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.accepted_client_total_items,
                parent,false);
        return new AcceptedClientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AcceptedClientViewHolder holder, int position) {

        AcceptedData items = list.get(position);
        holder.txtClientName.setText("Client: "+items.getName());
        holder.txtClientEmail.setText("Email: "+items.getEmail());
        holder.txtDate.setText("Date: "+items.getDate());
        try{
            Picasso.get().load(items.getImage()).into(holder.imgClientProfile);
        }catch(Exception e){
            e.printStackTrace();
        }

        holder.btnViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MoreClientDetailsActivity.class);
                intent.putExtra("uniqueId", items.getUniqueId());
                intent.putExtra("image", items.getImage());
                intent.putExtra("name", items.getName());
                intent.putExtra("surname", items.getSurname());
                intent.putExtra("number", items.getNumber());
                intent.putExtra("email", items.getEmail());
                intent.putExtra("status", items.getStatus());
                intent.putExtra("date", items.getDate());
                intent.putExtra("price", items.getPrice());
                intent.putExtra("location", items.getLocation());
                intent.putExtra("longitude", items.getLongitude());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class AcceptedClientViewHolder extends RecyclerView.ViewHolder {

        ImageView imgClientProfile;
        TextView txtClientName,txtClientEmail,txtDate;
        Button btnViewMore;
        public AcceptedClientViewHolder(@NonNull View itemView) {
            super(itemView);
            imgClientProfile = itemView.findViewById(R.id.imgClientProfile);
            txtClientName = itemView.findViewById(R.id.txtClientName);
            txtClientEmail = itemView.findViewById(R.id.txtClientEmail);
            txtDate = itemView.findViewById(R.id.txtDate);
            btnViewMore = itemView.findViewById(R.id.btnViewMore);
        }
    }
}
