package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityLoginBinding;

import java.io.File;

public class Login extends AppCompatActivity {

    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    BeautifulProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new BeautifulProgressDialog(Login.this,
                BeautifulProgressDialog.withGIF,
                "Please wait");
        progressDialog.setGifLocation(Uri.fromFile(new File("//android_asset/loading.gif")));
        progressDialog.setLottieLoop(true);
        mAuth=FirebaseAuth.getInstance();
        binding.loginBtn.setOnClickListener(v->{
            String mail,pass;
            mail=binding.loginEmail.getText().toString();
            pass=binding.loginPassword.getText().toString();
            if (mail.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (pass.isEmpty()){
                Toast.makeText(this, "Empty", Toast.LENGTH_SHORT).show();
                return;
            }if (!mail.contains("@")){
                Toast.makeText(this, "Invalid Mail", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog.show();
            login(mail,pass);
        });

        binding.sendRegisterBtn.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(),register.class));
        });
    }

    private void login(String mail, String pass) {
        mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finishAffinity();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}