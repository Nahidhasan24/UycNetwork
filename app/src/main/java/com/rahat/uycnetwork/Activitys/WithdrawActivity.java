package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
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
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.Modles.WithdrawModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityWithdrawBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WithdrawActivity extends AppCompatActivity {

    ActivityWithdrawBinding binding;
    DatabaseReference mRef;
    DatabaseReference mWithdraw;
    FirebaseAuth mAuth;
    UserModle userModle;
    ProgressDialog progressDialog;

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
        mWithdraw = FirebaseDatabase.getInstance().getReference().child("Withdraw");
        inits();
        binding.bnbBtn.setOnClickListener(v -> {
            showForgotDialog(WithdrawActivity.this, "BNB");
        });
        binding.uycBtn.setOnClickListener(v -> {
            showForgotDialog(WithdrawActivity.this, "UYC");
        });
        binding.usdBtn.setOnClickListener(v -> {
            showForgotDialog(WithdrawActivity.this, "USD");
        });
    }

    private void inits() {
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    private void showForgotDialog(Context c, String method) {
        final EditText taskEditText = new EditText(c);
        AlertDialog dialog = new AlertDialog.Builder(c)
                .setTitle("Enter Wallet Address")
                .setMessage("Enter your wallet address")
                .setView(taskEditText)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String task = String.valueOf(taskEditText.getText());
                        WithdrawModle withdrawModle = new WithdrawModle(userModle.getName(), userModle.getMail(), task, getTime(), "", "pending", method, 100);
                        mWithdraw.child(mAuth.getUid())
                                .push()
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