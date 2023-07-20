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
import com.example.splash.databinding.ChatItemsBinding;
import com.example.splash.databinding.TextItemsBinding;
import com.example.splash.model.AcceptedData;
import com.example.splash.model.ChatData;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatData> list;
    private Context context;

    public static final int VIEW_TYPE_SEND = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    private String senderId;
    private String driverId;

    public ChatAdapter(List<ChatData> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SEND) {
            return new sendMessageViewHolder(
                    ChatItemsBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        } else {
            return new sendMessageViewHolder.ReceivedMessageViewHolder(
                    TextItemsBinding.inflate(
                            LayoutInflater.from(parent.getContext()),
                            parent,
                            false
                    )
            );
        }
    }
    public void filterList(List<ChatData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position)== VIEW_TYPE_SEND){
            ((sendMessageViewHolder) holder).setData(list.get(position));

        }else{
            ((sendMessageViewHolder.ReceivedMessageViewHolder) holder).setData(list.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    @Override
    public int getItemViewType(int position) {

        ChatData items = list.get(position);




        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("ChatData");
        DatabaseReference dRef = FirebaseDatabase.getInstance().getReference().child("Accepted");


        reference.child(items.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ChatData chatData = snapshot.getValue(ChatData.class);
                if(chatData!=null){
                    senderId = chatData.getClientId();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
       /* reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    ChatData chatData = dataSnapshot.getValue(ChatData.class);
                    if(chatData != null){
                        senderId = chatData.getClientId();
                        Toast.makeText(context, senderId, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/

        if (items.getClientId().equals(senderId)) {
            return VIEW_TYPE_SEND;
        }else{
            return VIEW_TYPE_RECEIVED;
        }
    }
    static class sendMessageViewHolder extends RecyclerView.ViewHolder{

        private final ChatItemsBinding binding;

        sendMessageViewHolder(ChatItemsBinding chatItemsBinding){
            super(chatItemsBinding.getRoot());
            binding = chatItemsBinding;
        }
        void setData(ChatData list){
            binding.textMessage.setText(list.getMessage());
            binding.textDateTime.setText(list.getDate());

        }

        static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder{

            private final TextItemsBinding binding;

            ReceivedMessageViewHolder(TextItemsBinding textItemsBinding){
                super(textItemsBinding.getRoot());
                binding = textItemsBinding;
            }

            void setData(ChatData list){
                binding.textMessage.setText(list.getMessage());
                binding.textDateTime.setText(list.getDate());

            }
        }
    }
}

/*public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatAdapterViewHolder> {

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
       // if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_items,
                    parent, false);
            return new ChatAdapterViewHolder(view);
       // } else {
          //  View view = LayoutInflater.from(context).inflate(R.layout.text_items,
                    //parent, false);
           // return new ChatAdapterViewHolder(view);
       // }
    }
    public void filterList(List<ChatData> filterList){
        list = filterList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ChatAdapterViewHolder holder, int position) {
       // if(getItemViewType(position)== 1){


            ChatData item = list.get(position);
             holder.textMessage.setText(item.getMessage());
             holder.textDateTime.setText(item.getDate());






       // }else{
            //ChatData item = list.get(position);
            //holder.textMessage.setText(item.getMessage());
            // holder.textDateTime.setText(item.getDate());
           // Toast.makeText(context, "Sender", Toast.LENGTH_SHORT).show();
       // }

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
}*/
