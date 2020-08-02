package com.tedm.musicblog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment implements View.OnClickListener {

    private CircleImageView userImage;
    private Uri mainImageURI = null;
    private String user_id;

    private TextView tvName;
    private TextView tvEmail;
    private Button btnShowTaskList;
    private Button btnLoadInfo;

    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String myUrl = "";


    public AccountFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        tvName = view.findViewById(R.id.fragAcc_Username);
        tvEmail = view.findViewById(R.id.fragAcc_Email);
        userImage = view.findViewById(R.id.fragAcc_Picture);
        btnShowTaskList = view.findViewById(R.id.fragAcc_btnTaskList);
        btnLoadInfo = view.findViewById(R.id.fragAcc_btnLoadInfo);

        btnShowTaskList.setOnClickListener(this);
        btnLoadInfo.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.fragAcc_btnTaskList:
                Intent intent = new Intent(getActivity(), TaskListActivity.class);
                startActivity(intent);

                break;

            case R.id.fragAcc_btnLoadInfo:
                firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String name = task.getResult().getString("name");
                                String email = firebaseAuth.getCurrentUser().getEmail();
                                String image = task.getResult().getString("image");

                                mainImageURI = Uri.parse(image);
                                tvName.setText("Account username: " + name);
                                tvEmail.setText("Account email: " + email);

                                RequestOptions placeholderRequest = new RequestOptions();
                                placeholderRequest.placeholder(R.drawable.default_image);

                                Glide.with(AccountFragment.this).setDefaultRequestOptions(placeholderRequest).load(image).into(userImage);

                            } else {
                                String error = task.getException().getMessage();
                            }
                        }
                    }
                });
                break;
        }
    }
}
