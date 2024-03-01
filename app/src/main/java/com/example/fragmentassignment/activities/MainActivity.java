package com.example.fragmentassignment.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.fragmentassignment.R;
import com.example.fragmentassignment.fragments_and_dialog.AddItemDialog2;
import com.example.fragmentassignment.fragments_and_dialog.Fragment2;
import com.example.fragmentassignment.userInfo.UserInfo;

public class MainActivity extends AppCompatActivity {
    private TextView header;
    private Fragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        header = (TextView) findViewById(R.id.mainHeader);

//         userNameHeader = (TextView) findViewById(R.id.userNameHeader);
//         userNameLayout = (LinearLayout) findViewById(R.id.userNameLayout);
//         cartBtn = (ImageButton) findViewById(R.id.cartButton);
//
//        UserInfo user1 = new UserInfo("Lass","1234","0500000000");
//        if(!user1.checkUserOnSignIn()){
//            Log.d("user1 check user","false");
//        }else{
//            Log.d("user1 check user","true");
//        }
//        user1.printingAllUsersInfo();
//        UserInfo user2 = new UserInfo("Gal","7465","0520000000");
//        if(!user2.checkUserOnSignIn()){
//            Log.d("user2 check user","false");
//        }else{
//            Log.d("user2 check user","true");
//        }
//        UserInfo user3 = new UserInfo("1","1","1");
//        if(!user3.checkUserOnSignIn()){
//            Log.d("user3 check user","false");
//        }else{
//            Log.d("user3 check user","true");
//        }

//        fragment2 = new Fragment2();
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(R.id.fragment2, fragment2); // Use the ID of the container where Fragment2 should be added
//        transaction.commit();

//        user2.printingAllUsersInfo();

//        cartBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                DialogCart dialogCart = new DialogCart();
//                dialogCart.show(getSupportFragmentManager(),"DialogCart");
//            }
//        });

    }

//    public void sendInput(String[] input) {
//        // Implement what to do with the received input data
//        // For example, you can pass it to Fragment2
////       fragment2 = (Fragment2) getSupportFragmentManager().findFragmentById(R.id.fragment2);
////        if (fragment2 != null) {
////            fragment2.updateAdapterWithData(input);
////        }
//    }

    public void updateHeaderText(String text, String visibility){
        header.setVisibility(visibility.equals("VISIBLE") ? View.VISIBLE : View.GONE);
        header.setText(text);
    }
//    public void (String text){
//        header.setVisibility(View.GONE);
//        userNameLayout.setVisibility(View.VISIBLE);
//        userNameHeader.setText(text);
//    }



}