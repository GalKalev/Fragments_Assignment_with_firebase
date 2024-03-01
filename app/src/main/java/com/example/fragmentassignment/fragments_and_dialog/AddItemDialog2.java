package com.example.fragmentassignment.fragments_and_dialog;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.fragmentassignment.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddItemDialog2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddItemDialog2 extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddItemDialog2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddItemDialog2.
     */
    // TODO: Rename and change types and number of parameters
    public static AddItemDialog2 newInstance(String param1, String param2) {
        AddItemDialog2 fragment = new AddItemDialog2();
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

    public interface OnInputListener{
        void sendInput(String[] input);
    }
    public  OnInputListener mOnInputListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_item_dialog2, container, false);

        EditText enterItemName = view.findViewById(R.id.enterItemName);
        EditText enterItemAmount = view.findViewById(R.id.enterItemAmount);
        Button confirmAddedItem = view.findViewById(R.id.confirmItemButton);
        Spinner spinner = view.findViewById(R.id.measurementList);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.spinner_amount_type, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        confirmAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if enterItemName and enterItemAmount are not null
                if (enterItemName != null && enterItemAmount != null) {
                    String itemName = enterItemName.getText().toString();
                    String itemAmount = enterItemAmount.getText().toString();

                    // Perform null check on itemName and itemAmount
                    if (itemName != null && itemAmount != null) {
                        String[] toFrg2 = {itemName, itemAmount};
                        mOnInputListener.sendInput(toFrg2);
//                        Bundle updateManagerListInFrg2 = new Bundle();
//                        updateManagerListInFrg2.putStringArray("updateAdapter", toFrg2);
                        getDialog().dismiss();
                    } else {
                        Log.e("AddItemDialog", "Item name or amount is null.");
                    }
                } else {
                    Log.e("AddItemDialog", "EditText fields are not initialized properly.");
                }
            }
        });


        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try{
            mOnInputListener = (OnInputListener)getActivity();
        }catch (ClassCastException e){
            Log.d("AddItemDialog", "not good");
        }
    }
}