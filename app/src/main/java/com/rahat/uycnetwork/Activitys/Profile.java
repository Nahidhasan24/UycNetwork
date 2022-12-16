package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(Profile.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        init();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
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
    }

    private void init() {
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            progressDialog.dismiss();
                            userModle = snapshot.getValue(UserModle.class);
                            binding.profileName.setText("Name : " + userModle.getName());
                            binding.profileNumber.setText("Phone : " + userModle.getPhone());
                            binding.profileEmail.setText("Email : " + userModle.getMail());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Profile.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void GoToURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}