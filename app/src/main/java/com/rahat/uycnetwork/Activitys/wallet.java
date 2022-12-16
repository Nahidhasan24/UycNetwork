package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityWalletBinding;

public class wallet extends AppCompatActivity {

    ActivityWalletBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    UserModle userModle;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWalletBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(wallet.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        inite();
        binding.addMoneyBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), AddMoneyActivity.class));
        });
        binding.withdrawBtn.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),WithdrawActivity.class));
        });
    }

    private void inite() {
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            progressDialog.dismiss();
                            userModle = snapshot.getValue(UserModle.class);
                            binding.balanceTV.setText(userModle.getCoin() + "");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
    }
}