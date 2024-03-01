package com.example.fragmentassignment.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fragmentassignment.R;
import com.example.fragmentassignment.fragments_and_dialog.Fragment2;

public class MainActivity extends AppCompatActivity {
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header = (TextView) findViewById(R.id.mainHeader);

    }

    public void updateHeaderText(String text, String visibility){
        header.setVisibility(visibility.equals("VISIBLE") ? View.VISIBLE : View.GONE);
        header.setText(text);
    }



}