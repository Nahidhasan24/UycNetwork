package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityRegisterBinding;

import java.util.Random;

public class register extends AppCompatActivity {

    ActivityRegisterBinding binding;
    DatabaseReference mRef;
    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegisterBinding.inflate(getLayoutInflater());
        progressDialog=new ProgressDialog(register.this);
        progressDialog.setTitle("Loading....");
        progressDialog.setCancelable(false);
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference().child("Users");
        binding.registerbtn.setOnClickListener(v->{

            String name,mail,number,password,conPassword;
            name=binding.regName.getText().toString();
            mail=binding.regEmail.getText().toString();
            number=binding.regNumber.getText().toString();
            password=binding.regPassword.getText().toString();
            conPassword=binding.regConPassword.getText().toString();
            if (name.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (mail.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (number.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (password.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (conPassword.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (!password.equals(conPassword)){
                Toast.makeText(this, "Password Not Same", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(mail,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                registerUser(name,mail,number,password);
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(register.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

        });

        binding.sendRegisterBtn.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),Login.class));
        });
    }

    private void registerUser(String name, String mail, String number, String password) {
        UserModle userModle=new UserModle(mAuth.getUid(),name,mail,number,"on",getReferCode(),"",0);
        mRef.child(mAuth.getUid())
                .setValue(userModle)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(register.this, "Account Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finishAffinity();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(register.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private String getReferCode(){

        int number=new Random().nextInt((991212- 111111) + 1) + 11111;

        return "UYC"+number+"NS";
    }
}