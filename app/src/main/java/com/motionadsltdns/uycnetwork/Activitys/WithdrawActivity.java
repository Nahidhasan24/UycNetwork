package com.motionadsltdns.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motionadsltdns.uycnetwork.Modles.Config;
import com.motionadsltdns.uycnetwork.Modles.UserModle;
import com.motionadsltdns.uycnetwork.Modles.WithdrawModle;
import com.motionadsltdns.uycnetwork.R;
import com.motionadsltdns.uycnetwork.databinding.ActivityWithdrawBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class WithdrawActivity extends AppCompatActivity {

    ActivityWithdrawBinding binding;
    DatabaseReference mRef;
    DatabaseReference mConfig;
    DatabaseReference mWithdraw;
    FirebaseAuth mAuth;
    UserModle userModle;
    Config config;
    ProgressDialog progressDialog;
    String TAG="MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWithdrawBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(WithdrawActivity.this);
        progressDialog.setTitle("Loading....");
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mConfig = FirebaseDatabase.getInstance().getReference().child("Config");
        mWithdraw = FirebaseDatabase.getInstance().getReference().child("Withdraw");
        inits();
        binding.bnbBtn.setOnClickListener(v -> {
            if (userModle.getCoin()<=config.getMinwithdraw()){
                Toast.makeText(this, "You Don't have enough coin !", Toast.LENGTH_SHORT).show();
            }else  {
                showForgotDialog(WithdrawActivity.this, "BNB");
            }
        });
        binding.uycBtn.setOnClickListener(v -> {

            if (userModle.getCoin()<=config.getMinwithdraw()){
                Toast.makeText(this, "You Don't have enough coin !", Toast.LENGTH_SHORT).show();
            }else  {
                showForgotDialog(WithdrawActivity.this, "UYC");
            }

        });
        binding.usdBtn.setOnClickListener(v -> {
            if (userModle.getCoin()<=config.getMinwithdraw()){
                Toast.makeText(this, "You Don't have enough coin !", Toast.LENGTH_SHORT).show();
            }else  {
                showForgotDialog(WithdrawActivity.this, "USD");
            }
        });
    }

    private void inits() {
        mRef.child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModle = snapshot.getValue(UserModle.class);
                        binding.balanceTV.setText(""+userModle.getCoin());
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
        mConfig.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    config=snapshot.getValue(Config.class);
                    binding.usdTV.setText(config.getMinwithdraw()+" coin");
                    binding.bnbTV.setText(config.getMinwithdraw()+" coin");
                    binding.uycTV.setText(config.getMinwithdraw()+" coin");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mWithdraw.child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.viewHolder.setVisibility(View.INVISIBLE);
                            Toast.makeText(WithdrawActivity.this, "Wait Until Check..", Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void showForgotDialog(Context c, String method) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter Wallet Address")
                .setMessage("Enter your "+method+" wallet address")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        double tmpCoin=userModle.getCoin()-config.getMinwithdraw();
                        HashMap<String ,Object> hashMap=new HashMap<>();
                        hashMap.put("coin",tmpCoin);
                        WithdrawModle withdrawModle = new WithdrawModle(userModle.getName(), userModle.getMail(), task, getTime(), "", "pending", method,mAuth.getUid(), config.getMinwithdraw());
                        mRef.child(mAuth.getUid())
                                .updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            mWithdraw.child(mAuth.getUid())
                                                    .setValue(withdrawModle)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                Toast.makeText(WithdrawActivity.this, "Success", Toast.LENGTH_SHORT).show();

                                                            }else{
                                                                Toast.makeText(WithdrawActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                    }
                })
                .setNegativeButton("Close", null)
                .create();
        dialog.show();
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}