package com.example.fragmentassignment.fragments_and_dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fragmentassignment.ManagerList.ManagerListAdapter;
import com.example.fragmentassignment.R;

import java.util.Arrays;
import java.util.List;

public class AddItemDialog {

    private EditText enterItemName;
    private EditText enterItemAmount;
    private Button confirmAddedItem;
    private Button cancelAddedItem;
    private Spinner spinner;
    private Dialog dialog;
    private Fragment2 fragment2;
    private ManagerListAdapter listAdapter;
    String TAG = "aaa";




    public AddItemDialog(Context context, Fragment2 fragment2) {
        this.fragment2 = fragment2;
        setUpDialog(context);
        confirmAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"confirm click");
                String itemName = getEnterItemName().getText().toString();
                String itemAmount = getEnterItemAmount().getText().toString();
                Object measurment = getSpinner().getSelectedItem();
                if(itemName.equals("") || itemAmount.equals("") || measurment.equals("Select a measurment")) {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    fragment2.updateAdapterWithData(itemName, itemAmount + " " + measurment);
                    fragment2.updateDatabase();
                    dismiss();
                }
                Log.d(TAG, "m: " + measurment);

            }
        });

        cancelAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public AddItemDialog(Context context, String name, String amount, ManagerListAdapter listAdapter,int position) {
        setUpDialog(context);
        this.listAdapter = listAdapter;
        enterItemName.setText(name);
        String[] splitAmountString = amount.split(" ");
        enterItemAmount.setText(splitAmountString[0]);
        if(splitAmountString[1].equals("kg")){
            spinner.setSelection(1);
        }else if(splitAmountString[1].equals("item(s)")){
            spinner.setSelection(2);
        }
        confirmAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"confirm click");
                String itemName = getEnterItemName().getText().toString();
                String itemAmount = getEnterItemAmount().getText().toString();
                Object measurment = getSpinner().getSelectedItem();
                if(itemName.equals("") || itemAmount.equals("") || measurment.equals("Select a measurment")){
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }else{
                    listAdapter.updateAdapterWithData(itemName, itemAmount + " " + measurment, position);
                    listAdapter.updateDatabase();
                    listAdapter.notifyDataSetChanged();
                    dismiss();
                }

            }
        });
        cancelAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setUpDialog(Context context){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.add_item);
        enterItemName = dialog.findViewById(R.id.enterItemName);
        enterItemAmount = dialog.findViewById(R.id.enterItemAmount);
        confirmAddedItem = dialog.findViewById(R.id.confirmItemButton);
        cancelAddedItem = dialog.findViewById(R.id.cancelAddItemButton);
        spinner = dialog.findViewById(R.id.measurementList);
        setupSpinner(context);

    }


    private void setupSpinner(Context context) {
        List<String> measurements = Arrays.asList("Select a measurment","kg", "item(s)");
        ArrayAdapter<String> measurementListSpinnerAdapter = new ArrayAdapter<>(context, R.layout.spinner_list, measurements);
        measurementListSpinnerAdapter.setDropDownViewResource(R.layout.spinner_list);
        spinner.setAdapter(measurementListSpinnerAdapter);
    }

    private void setupConfirmButton() {

        confirmAddedItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment2.updateAdapterWithData(getEnterItemName().getText().toString(),
                        getEnterItemAmount().getText().toString() + " " + getSpinner().getSelectedItem().toString());
                dismiss();
            }
        });
    }


    public EditText getEnterItemName() {
        return enterItemName;
    }

    public void setEnterItemName(EditText enterItemName) {
        this.enterItemName = enterItemName;
    }

    public EditText getEnterItemAmount() {
        return enterItemAmount;
    }

    public void setEnterItemAmount(EditText enterItemAmount) {
        this.enterItemAmount = enterItemAmount;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public void show() {
        dialog.show();
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
