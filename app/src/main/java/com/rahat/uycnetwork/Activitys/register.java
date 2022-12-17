package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahat.uycnetwork.Modles.DeviceIDModel;
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityRegisterBinding;

import java.io.File;
import java.util.Random;

public class register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    DatabaseReference mRef;
    DatabaseReference mID;
    FirebaseAuth mAuth;
    BeautifulProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new BeautifulProgressDialog(register.this,
                BeautifulProgressDialog.withGIF,
                "Please wait");
        progressDialog.setGifLocation(Uri.fromFile(new File("//android_asset/loading.gif")));
        progressDialog.setLottieLoop(true);
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mID = FirebaseDatabase.getInstance().getReference().child("DeviceID");
        inti();
        binding.registerbtn.setOnClickListener(v -> {

            String name, mail, number, password, conPassword;
            name = binding.regName.getText().toString();
            mail = binding.regEmail.getText().toString();
            number = binding.regNumber.getText().toString();
            password = binding.regPassword.getText().toString();
            conPassword = binding.regConPassword.getText().toString();
            if (name.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (mail.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (number.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (conPassword.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(conPassword)) {
                Toast.makeText(this, "Password Not Same", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(mail, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                registerUser(name, mail, number, password);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(register.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

        binding.sendRegisterBtn.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
        });
    }

    private void inti() {
        mID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    progressDialog.dismiss();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        DeviceIDModel deviceIDModel = dataSnapshot.getValue(DeviceIDModel.class);
                        if (deviceIDModel.getId().equals(getDeviceIMEI(getApplicationContext()))) {
                            loadDiaload();
                            return;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();
            }
        });
    }

    private void loadDiaload() {
        progressDialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(register.this);
        builder.setTitle("Warning Alert !");
        builder.setMessage("You can't create multiple account same device !");
        builder.setCancelable(false);
        builder.setNeutralButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void registerUser(String name, String mail, String number, String password) {
        UserModle userModle = new UserModle(mAuth.getUid(), name, mail, number, "on", getReferCode(), "", 0,0);
        mRef.child(mAuth.getUid())
                .setValue(userModle)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            DeviceIDModel deviceIDModel = new DeviceIDModel(getDeviceIMEI(getApplicationContext()));
                            mID.child(mAuth.getUid())
                                    .setValue(deviceIDModel)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                Toast.makeText(register.this, "Account Created", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                                finishAffinity();
                                            }
                                        }
                                    });
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(register.this, "" + task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String getReferCode() {

        int number = new Random().nextInt((991212 - 111111) + 1) + 11111;

        return "UYC" + number + "NS";
    }

    private String getDeviceIMEI(Context context) {
        String deviceId;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            deviceId = Settings.Secure.getString(
                    context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } else {
            final TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (mTelephony.getDeviceId() != null) {
                deviceId = mTelephony.getDeviceId();
            } else {
                deviceId = Settings.Secure.getString(
                        context.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
            }
        }

        return deviceId;
    }
}