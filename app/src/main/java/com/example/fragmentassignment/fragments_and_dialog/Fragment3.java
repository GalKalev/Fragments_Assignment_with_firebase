package com.example.fragmentassignment.fragments_and_dialog;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fragmentassignment.R;
import com.example.fragmentassignment.activities.MainActivity;
import com.example.fragmentassignment.userInfo.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment3#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment3 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment3.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment3 newInstance(String param1, String param2) {
        Fragment3 fragment = new Fragment3();
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

    String TAG = "frg3";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        Log.d(TAG, "enter frg");
        View view = inflater.inflate(R.layout.fragment_3, container, false);

        mAuth = FirebaseAuth.getInstance();

        MainActivity mainActivity = (MainActivity) getActivity();

        EditText emailFrg3 = view.findViewById(R.id.emailInputSignUp);
        EditText passwordFrg3 = view.findViewById(R.id.passwordInputSignUp);
        EditText phoneFrg3 = view.findViewById(R.id.phoneInputSignUp);
        EditText nameFrg3 = view.findViewById(R.id.nameInputSignUp);

        TextView warning = view.findViewById(R.id.inputWarningSignUp);

        Button goBtnFrg3 = (Button) view.findViewById(R.id.submitBtnSignUp);

        if(mainActivity != null){
            mainActivity.updateHeaderText("JOIN OUR FAMILY :)","VISIBLE");
        }


//
        goBtnFrg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "goBtn clicked");

                String email = emailFrg3.getText().toString().trim();
                String password = passwordFrg3.getText().toString().trim();
                String phone = phoneFrg3.getText().toString().trim();
                String name = nameFrg3.getText().toString().trim();
                if(email.isEmpty() || password.isEmpty() || phone.isEmpty() || name.isEmpty()){
                    warning.setVisibility(View.VISIBLE);
                    warning.setText("!Fill All Fields!");
                }else{
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        warning.setVisibility(View.GONE);
                                        // Sign in success, update UI with the signed-in user's information
                                        Toast.makeText(getContext(), "signin ok", Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();

                                        UserInfo userInfo = new UserInfo(email,password,phone,name);

                                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                                        DatabaseReference myRef = database.getReference("users").child(uid);

                                        myRef.setValue(userInfo);

                                        Bundle nameBundle = new Bundle();
                                        nameBundle.putString("name", name);
                                        nameBundle.putString("uid", uid);
                                        Navigation.findNavController(view).navigate(R.id.action_fragment3_to_fragment2, nameBundle);
                                    } else {
                                        Log.w(TAG, "createUserWithEmail:failure", task.getException());

                                        warning.setVisibility(View.VISIBLE);
                                        warning.setText("Make Sure To Fill The Field Correctly");

                                    }
                                }
                            });

//
                    }


                }



        });

        return view;
    }


}