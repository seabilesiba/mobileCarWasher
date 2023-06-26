package com.example.splash.Driver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.splash.Adapters.AcceptedRequestAdapter;
import com.example.splash.Adapters.ChatAdapter;
import com.example.splash.R;
import com.example.splash.databinding.ActivityChatBinding;
import com.example.splash.model.ChatData;
import com.example.splash.model.DriverData;
import com.example.splash.model.RequestData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {

    private DatabaseReference reference;
    private List<ChatData> list;
    private ChatAdapter adapter;

    private ActivityChatBinding binding;
    private String image,message,currentDate,time,DateTime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listener();
        //closeKeyboard();

        reference = FirebaseDatabase.getInstance().getReference().child("ChatData");

        binding.inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        getData();


    }
    private void filter(String text) {

        List<ChatData> filterList = new ArrayList<>();
        for(ChatData items : list){
            if(items.getMessage().toLowerCase().contains(text.toLowerCase())){
                filterList.add(items);
            }
        }
        adapter.filterList(filterList);
    }
    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list = new ArrayList<>();


                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    ChatData chatData = dataSnapshot.getValue(ChatData.class);
                    list.add(chatData);


                }
                adapter = new ChatAdapter(list, ChatActivity.this);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this);
                binding.rcMessage.setLayoutManager(linearLayoutManager);
                //linearLayoutManager.getStackFromEnd();
                //linearLayoutManager.setReverseLayout(true);
                adapter.notifyItemRangeInserted(list.size(), list.size()-1);
                binding.rcMessage.scrollToPosition(list.size()-1);

                binding.rcMessage.setAdapter(adapter);
                binding.rcMessage.setVisibility(View.VISIBLE);
                binding.pd.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getApplicationContext(), "Something went Wrong "+ error.getMessage() , Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void listener() {
        binding.layoutSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("ChatData");
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String userID = auth.getCurrentUser().getUid();
                final String key = mRef.push().getKey();

                message = binding.inputMessage.getText().toString().trim();
                Calendar calendar = Calendar.getInstance();
                currentDate = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(calendar.getTime());
                Date dateTime = Calendar.getInstance().getTime();
                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss", Locale.getDefault());

                time = timeFormat.format(dateTime);

                DateTime = currentDate+" at "+time;

                ChatData chatData = new ChatData(message,DateTime,userID,key);


                mRef.child(key).setValue(chatData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            //Toast.makeText(ChatActivity.this, "success", Toast.LENGTH_SHORT).show();
                            binding.inputMessage.setText("");
                            closeKeyboard();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ChatActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });


    }
    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}


