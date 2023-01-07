package com.motionadsltdns.uycnetwork.Fragments;

import android.content.Intent;
import android.net.Uri;
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
import com.motionadsltdns.uycnetwork.Activitys.ComingSoon;
import com.motionadsltdns.uycnetwork.Activitys.InviteActivity;
import com.motionadsltdns.uycnetwork.Activitys.Login;
import com.motionadsltdns.uycnetwork.Modles.UserModle;
import com.motionadsltdns.uycnetwork.databinding.FragmentProfileBinding;


public class ProfileFragment extends Fragment {

    FragmentProfileBinding binding;
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
        binding=FragmentProfileBinding.inflate(inflater,container,false);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        init();

        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
        binding.telegramBtn.setOnClickListener(v -> {
            GoToURL("https://t.me/+_7B_HBssHs01ZThl");
        });
        binding.youtubeBtn.setOnClickListener(v -> {
            GoToURL("https://www.youtube.com/channel/UCp5mwfEcWBeA8QV7oopG1sQ");
        });
        binding.facebookBtn.setOnClickListener(v -> {
            GoToURL("https://twitter.com/home");
        });
        binding.invitedFriendBtn.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), InviteActivity.class));
        });

        binding.privacy.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ComingSoon.class));
        });
        binding.Ratingapss.setOnClickListener(v->{
            startActivity(new Intent(getActivity(), ComingSoon.class));
        });
        binding.logout.setOnClickListener(v->{
            mAuth.signOut();
            startActivity(new Intent(getActivity(), Login.class));
        });

        return binding.getRoot();
    }
    private void init() {
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            userModle = snapshot.getValue(UserModle.class);
                            binding.profileName.setText("" + userModle.getName());
                            binding.profileNumber.setText("" + userModle.getPhone());
                            binding.profileEmail.setText("" + userModle.getMail());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        //Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void GoToURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}