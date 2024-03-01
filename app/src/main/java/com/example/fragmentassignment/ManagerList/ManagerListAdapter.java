package com.example.fragmentassignment.ManagerList;

import static android.media.browse.MediaBrowser.*;
import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.media.browse.MediaBrowser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fragmentassignment.R;
import com.example.fragmentassignment.fragments_and_dialog.AddItemDialog;
import com.example.fragmentassignment.fragments_and_dialog.Fragment2;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ManagerListAdapter extends RecyclerView.Adapter<ManagerListAdapter.MyViewHolder>{
    private ArrayList<ManagerListModel> dataset;
    String uid;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef ; // Adjust the reference path as per your database structure


    public ManagerListAdapter(ArrayList<ManagerListModel> dataset, String uid) {
        this.dataset = dataset;
        this.uid = uid;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView itemName;
        TextView itemAmount;
        Button editButton;
        Button deleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.itemName);
            itemAmount = itemView.findViewById(R.id.itemAmount);
            editButton = itemView.findViewById(R.id.editItemButton);
            deleteButton = itemView.findViewById(R.id.deleteItemButton);

        }
    }

    @NonNull
    @Override
    public ManagerListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerListAdapter.MyViewHolder holder, int position) {
        TextView itemName = holder.itemName;
        TextView itemAmount = holder.itemAmount;
        Button editButton = holder.editButton;
        Button deleteButton = holder.deleteButton;

        itemName.setText(dataset.get(position).getItemName());
        itemAmount.setText(dataset.get(position).getItemAmount());
        myRef = database.getReference("users").child(uid).child("shoppingList");
//        updateDatabase();

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentPosition = holder.getAdapterPosition();
                AddItemDialog addItemDialog = new AddItemDialog(v.getContext(),itemName.getText().toString()
                        ,itemAmount.getText().toString(),ManagerListAdapter.this,currentPosition);
                addItemDialog.show();
                updateDatabase();
                notifyDataSetChanged();
//
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int currentPosition = holder.getAdapterPosition();
                dataset.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, dataset.size());
                updateDatabase();
            }
        });


    }


    @Override
    public int getItemCount() {
        return dataset.size();
    }


    public void updateAdapterWithData(String name, String amount, int position) {

       dataset.get(position).setItemName(name);
       dataset.get(position).setItemAmount(amount);
        notifyDataSetChanged();
    }

    public void updateDatabase(){


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing data in the database node (optional)
                myRef.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Iterate over the dataset and add each item to the database
                            for (ManagerListModel item : dataset) {
                                // Prepare data to be written to the database
                                String itemName = item.getItemName();
                                String itemAmount = item.getItemAmount();
                                String itemId = String.valueOf(dataset.indexOf(item)); // Generate a unique key for each item
                                myRef.child(itemId).child("itemName").setValue(itemName);
                                myRef.child(itemId).child("itemAmount").setValue(itemAmount);
                                // Write other item details as needed
                            }
                        } else {
                            // Handle error
                            Log.e("Firebase", "Failed to clear data: " + task.getException().getMessage());
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                throw error.toException();
            }
        });
    }
}
