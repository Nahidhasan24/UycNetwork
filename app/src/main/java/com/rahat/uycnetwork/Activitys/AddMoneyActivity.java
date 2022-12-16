package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahat.uycnetwork.Modles.AddMoneyModel;
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityAddMoneyBinding;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMoneyActivity extends AppCompatActivity {

    ActivityAddMoneyBinding binding;
    String method;
    DatabaseReference mRef;
    DatabaseReference mUser;
    DatabaseReference mAddMoney;
    FirebaseAuth mAuth;
    UserModle userModle;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAddMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(AddMoneyActivity.this);
        progressDialog.setTitle("Loading....");
        progressDialog.setCancelable(false);
        progressDialog.show();
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("config");
        mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mAddMoney = FirebaseDatabase.getInstance().getReference().child("addmoney");
        progressDialog.show();
        inite();
        binding.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                method = getName(i);

            }
        });
        binding.submitBtn.setOnClickListener(v -> {
            String coin, tran;
            coin = binding.amount.getText().toString();
            tran = binding.tranID.getText().toString();
            if (coin.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }
            if (tran.isEmpty()) {
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (method==null){
                Toast.makeText(this, "Select Method", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            AddMoneyModel addMoneyModel = new AddMoneyModel(userModle.getName(), userModle.getPhone(), method, getTodayTime(), mAuth.getUid(), tran, Integer.parseInt(coin));
            mAddMoney.child(mAuth.getUid())
                    .setValue(addMoneyModel)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(AddMoneyActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                binding.tranID.setText("");
                                binding.amount.setText("");
                                binding.submitBtn.setEnabled(false);
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(AddMoneyActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });
    }

    private void inite() {
        mUser.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userModle=snapshot.getValue(UserModle.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            progressDialog.dismiss();
                            binding.submitBtn.setEnabled(false);
                        }else{
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private String getName(int i) {
        String me = null;
        if (i == R.id.trc) {
            me = "USDT TRC20";
        } else if (i == R.id.binance) {
            me = "Binance";
        }
        return me;
    }
    private String getTodayTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }
}