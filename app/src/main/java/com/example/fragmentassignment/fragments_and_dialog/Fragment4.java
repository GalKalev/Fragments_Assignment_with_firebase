package com.example.fragmentassignment.fragments_and_dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentassignment.ManagerList.ManagerListModel;
import com.example.fragmentassignment.R;
import com.example.fragmentassignment.activities.MainActivity;
import com.example.fragmentassignment.userInfo.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment4#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment4 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment4() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment4.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment4 newInstance(String param1, String param2) {
        Fragment4 fragment = new Fragment4();
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

    private FirebaseAuth mAuth;
    String TAG = "frg4";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_4, container, false);

        mAuth = FirebaseAuth.getInstance();
        MainActivity mainActivity = (MainActivity) getActivity();

        if(mainActivity != null){
            mainActivity.updateHeaderText("WELCOME BACK :)","VISIBLE");
        }

        EditText emailFrg4 = view.findViewById(R.id.emailInputLogIn);
        EditText passwordFrg4 = view.findViewById(R.id.passwordInputLogIn);

        TextView warning = view.findViewById(R.id.inputWarningLogIn);

        Button goBtnFrg3 = (Button) view.findViewById(R.id.submitBtnLogIn);

        goBtnFrg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailFrg4.getText().toString().trim();
                String password = passwordFrg4.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty() ){
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("!Fill All Fields!");
                }else
                {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        warning.setVisibility(View.GONE);
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users").child(uid);

                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()) {
                                                    UserInfo userInfo = snapshot.getValue(UserInfo.class);
                                                    if (userInfo != null) {
                                                        String name = userInfo.getUserName();
                                                        ArrayList<ManagerListModel> dataset = new ArrayList<ManagerListModel>();

                                                        for (DataSnapshot itemSnapshot : snapshot.child("shoppingList").getChildren()) {
                                                            // Iterate through each child node under "shoppingList"
                                                            String itemName = itemSnapshot.child("itemName").getValue(String.class);
                                                            String itemAmount = itemSnapshot.child("itemAmount").getValue(String.class);

                                                            dataset.add(new ManagerListModel(itemName,itemAmount, dataset.size()));

                                                        }

                                                        Log.d(TAG, "User name: " + name);
                                                        Bundle bundle = new Bundle();
                                                        bundle.putString("name", name);
                                                        bundle.putString("uid", uid);
                                                        bundle.putSerializable("dataset",dataset);
                                                        Navigation.findNavController(view).navigate(R.id.action_fragment4_to_fragment2, bundle);
                                                    }
                                                } else {
                                                    Log.d(TAG, "User data not found");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                Log.w(TAG, "Failed to read value.", error.toException());
                                            }
                                        });
                                    }else {
//                                    Toast.makeText(requireContext(), "login fail", Toast.LENGTH_SHORT).show();
                                        warning.setVisibility(View.VISIBLE);
                                        warning.setText("Make Sure To Fill The Field Correctly");
                                    }
                                }
                            });
                }

            }
        });

        return view;
    }
}