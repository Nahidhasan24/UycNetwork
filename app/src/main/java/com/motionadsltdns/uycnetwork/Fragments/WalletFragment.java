package com.motionadsltdns.uycnetwork.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motionadsltdns.uycnetwork.Activitys.AddMoneyActivity;
import com.motionadsltdns.uycnetwork.Activitys.WithdrawActivity;
import com.motionadsltdns.uycnetwork.Modles.UserModle;
import com.motionadsltdns.uycnetwork.R;
import com.motionadsltdns.uycnetwork.databinding.FragmentWalletBinding;


public class WalletFragment extends Fragment {

    FragmentWalletBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    UserModle userModle;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentWalletBinding.inflate(inflater,container,false);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        inite();
        loadAd();
        binding.addMoneyBtn.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), AddMoneyActivity.class));
        });
        binding.withdrawBtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), WithdrawActivity.class));
        });


        return binding.getRoot();
    }
    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }
    private void inite() {
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userModle = snapshot.getValue(UserModle.class);
                            binding.balanceTV.setText(userModle.getCoin() + "");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}