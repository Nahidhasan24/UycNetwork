package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityProfileBinding;

public class Profile extends AppCompatActivity {

    ActivityProfileBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    UserModle userModle;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog=new ProgressDialog(Profile.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        init();
    }

    private void init() {
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            progressDialog.dismiss();
                            userModle=snapshot.getValue(UserModle.class);
                            binding.profileName.setText("Name : "+userModle.getName());
                            binding.profileNumber.setText("Phone : "+userModle.getPhone());
                            binding.profileEmail.setText("Email : "+userModle.getMail());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Profile.this, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}