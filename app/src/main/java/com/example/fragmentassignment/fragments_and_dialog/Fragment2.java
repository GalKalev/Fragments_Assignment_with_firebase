package com.example.fragmentassignment.fragments_and_dialog;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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


import com.example.fragmentassignment.ManagerList.ManagerListAdapter;
import com.example.fragmentassignment.ManagerList.ManagerListData;
import com.example.fragmentassignment.ManagerList.ManagerListModel;
import com.example.fragmentassignment.R;
import com.example.fragmentassignment.activities.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment {
    //    AddItemDialog addItemDialog;
//    AddItemDialog2 addItemDialog2;
    ManagerListAdapter adapter;
    ArrayList<ManagerListModel> dataset;
    RecyclerView shoppingListRv;

    DatabaseReference myRef;
    FirebaseDatabase database;
    String uid = null;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment2.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_2, container, false);

        MainActivity mainActivity = (MainActivity) getActivity();
        Bundle getNameBundle = getArguments();
        String name = "";
        try {
            if (getNameBundle == null) {
                // Both bundles are not empty
                throw new Exception("Bundle error");
            } else {
                // getNameBundleSignUp is not empty, getEmailBundleLogin is either null or empty
                name = getNameBundle.getString("name");
                uid = getNameBundle.getString("uid");
                dataset = (ArrayList<ManagerListModel>) getNameBundle.getSerializable("dataset");
            }

            mainActivity = (MainActivity) getActivity();
            if (mainActivity != null) {
                mainActivity.updateHeaderText("Hello " + name + "! Happy Shopping :) ", "VISIBLE");
            }
        } catch (Exception e) {
            Toast.makeText(getContext(), "Bundle error", Toast.LENGTH_SHORT).show();
        }

        TextView emptyListText = view.findViewById(R.id.emptyListText);
        Button addItem = view.findViewById(R.id.addItemButton);


        shoppingListRv = view.findViewById(R.id.shoppingList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        shoppingListRv.setLayoutManager(linearLayoutManager);

        shoppingListRv.setItemAnimator(new DefaultItemAnimator());

//        dataset = new ArrayList<ManagerListModel>();
        Log.d("list data", "frg2");

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users").child(uid).child("shoppingList");

        if (dataset == null) {
            dataset = new ArrayList<>();
            for (int i = 0; i < ManagerListData.itemNameArray.size(); i++) {
                dataset.add(new ManagerListModel(
                        ManagerListData.itemNameArray.get(i),
                        ManagerListData.itemAmountArray.get(i),
                        ManagerListData._id.get(i)
                ));
            }
        }


        adapter = new ManagerListAdapter(dataset, uid);
        shoppingListRv.setAdapter(adapter);


        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddItemDialog addItemDialog = new AddItemDialog(v.getContext(), Fragment2.this);

                addItemDialog.show();
            }
        });


        Bundle fromDialog = getArguments();
        if (fromDialog != null) {
            String[] data = fromDialog.getStringArray("updateAdapter");
            if (data != null) {
                String itemName = data[0];
                String itemAmount = data[1];
                dataset.add(new ManagerListModel(itemName, itemAmount, ManagerListData._id.size()));
                adapter.notifyItemInserted(dataset.size() - 1); // Notify adapter of item insertion
            }
        }


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        shoppingListRv.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallBack);
        itemTouchHelper.attachToRecyclerView(shoppingListRv);


        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallBack = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            Log.d("change positions", "change");
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(dataset, fromPosition, toPosition);
            updateDatabase();
            shoppingListRv.getAdapter().notifyItemMoved(fromPosition, toPosition);


            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    public void updateListAdapter(){
        Log.d("list data","updateListAdapter");
        if (adapter != null) {
            Log.d("list data","adapter not null");
            adapter.notifyDataSetChanged();
        }
    }
    public void updateAdapterWithData(String name, String amount) {
        String itemName = name;
        String itemAmount = amount;

        dataset.add(new ManagerListModel(itemName, itemAmount, ManagerListData._id.size()));
        adapter.notifyItemInserted(dataset.size() - 1);
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
                            if (task.isSuccessful()) {

                                for (ManagerListModel item : dataset) {

                                    String itemName = item.getItemName();
                                    String itemAmount = item.getItemAmount();


                                    String itemId = String.valueOf(dataset.indexOf(item));
                                    myRef.child(itemId).child("itemName").setValue(itemName);
                                    myRef.child(itemId).child("itemAmount").setValue(itemAmount);
                                }
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