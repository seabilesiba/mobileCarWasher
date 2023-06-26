package com.example.splash.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splash.R;
import com.example.splash.model.ChatData;
import com.example.splash.model.DriverData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterViewHolder> {

    private List<ChatData> list;
    private Context context;

    public ChatAdapter(List<ChatData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatAdapter.ChatAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // View view = LayoutInflater.from(context).inflate(R.layout.chat_items,
        // parent, false);
        if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_items,
                    parent, false);
            return new ChatAdapterViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.text_items,
                    parent, false);
            return new ChatAdapterViewHolder(view);
        }
    }
    public void filterList(List<ChatData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatAdapterViewHolder holder, int position) {
        if(getItemViewType(position)== 1){

            ChatData item = list.get(position);
            holder.textMessage.setText(item.getMessage());
            holder.textDateTime.setText(item.getDate());

        }else{
            //ChatData item = list.get(position);
            //holder.textMessage.setText(item.getMessage());
            // holder.textDateTime.setText(item.getDate());
            Toast.makeText(context, "Sender", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        ChatData items = list.get(position);
        if (items.equals(items.getKey())) {
            return 1;
        }else{
            return 2;
        }
    }

    public class ChatAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView textMessage,textDateTime;
        ImageView imageProfile;
        public ChatAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            textMessage = itemView.findViewById(R.id.textMessage);
            textDateTime = itemView.findViewById(R.id.textDateTime);
            imageProfile = itemView.findViewById(R.id.imageProfile);
        }
    }
}
