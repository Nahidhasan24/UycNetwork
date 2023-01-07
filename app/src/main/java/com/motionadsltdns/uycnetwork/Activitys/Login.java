package com.motionadsltdns.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.motionadsltdns.uycnetwork.R;
import com.motionadsltdns.uycnetwork.databinding.ActivityLoginBinding;

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
        binding.forgetpassword.setOnClickListener(v->{
            showDiaload();
        });

    }

    private void showDiaload() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Login.this);
        alertDialog.setTitle("Enter Mail");
        alertDialog.setMessage("Enter Mail to Reset Password");

        final EditText input = new EditText(Login.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setIcon(R.drawable.lock);

        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        String password = input.getText().toString();
                        if (password.equals("")){
                            Toast.makeText(Login.this, "Empty Mail", Toast.LENGTH_SHORT).show();
                        }else{
                            mAuth.sendPasswordResetEmail(password)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(Login.this, "Password Change Link Sent on mail.", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    });
                        }
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }


    private void login(String mail, String pass) {
        mAuth.signInWithEmailAndPassword(mail,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (mAuth.getCurrentUser().isEmailVerified()){
                                progressDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                finishAffinity();
                            }else{
                                progressDialog.dismiss();
                                Toast.makeText(Login.this, "Email Not Verified.", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(Login.this, ""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}