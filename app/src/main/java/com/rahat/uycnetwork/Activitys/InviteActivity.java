package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basusingh.beautifulprogressdialog.BeautifulProgressDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rahat.uycnetwork.Modles.UserModle;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityInviteBinding;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

public class InviteActivity extends AppCompatActivity {

    ActivityInviteBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    UserModle userModle;
    Dialog dialog;
    BeautifulProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new BeautifulProgressDialog(InviteActivity.this,
                BeautifulProgressDialog.withGIF,
                "Please wait");
        progressDialog.setGifLocation(Uri.fromFile(new File("//android_asset/loading.gif")));
        progressDialog.setLottieLoop(true);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        progressDialog.show();
        inits();
        binding.shareRef.setOnClickListener(v->{
            String msg="Hello My refer code "+userModle.getRefercode()+" Use This \n to get extra coin For Free App Link "+sendRevire();
            shareText(msg);
        });
        binding.copyRef.setOnClickListener(v->{
            String msg="Hello My refer code "+userModle.getRefercode()+" Use This \n to get extra coin For Free App Link "+sendRevire();
            setClipboard(getApplicationContext(),userModle.getRefercode());
        });


    }

    private void loadDailoag() {
        dialog = new Dialog(InviteActivity.this);
        dialog.setContentView(R.layout.rating_dialoag);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        EditText editText = dialog.findViewById(R.id.refer_et);
        Button button = dialog.findViewById(R.id.referBtn);
        button.setOnClickListener(v -> {

            String code = editText.getText().toString().trim();
            if (code.isEmpty()) {
                editText.setError("Not Be Empty");
                return;
            } else if (code.equals(userModle.getRefercode())) {
                Toast.makeText(getApplicationContext(), "You Cant you your code", Toast.LENGTH_SHORT).show();
                return;
            }
            editText.setText("");

            Query myTopPostsQuery = mRef.orderByChild("refercode").equalTo(code);

            myTopPostsQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        getUserData(code);
                    } else {
                        Toast.makeText(getApplicationContext(), "User Not Found !", Toast.LENGTH_SHORT).show();
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });
    }

    private void getUserData(String code) {

        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (dataSnapshot.child("refercode").getValue(String.class).equals(code)) {
                            //Log.d(TAG, "UID Found Successfully");
                            getModel(dataSnapshot.child("uid").getValue(String.class));
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getModel(String id) {
        //
        //SM46168NS
        mRef.child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            UserModle finalModel = snapshot.getValue(UserModle.class);
                            updatedata(finalModel);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void updatedata(UserModle userModle1) {

        int current = userModle1.getRefercount();
        int fin = current + 1;
        String count = String.valueOf(fin);

        /////
        //String pot=userModle1.getCoin().replace(",","");
        double currentCoin = userModle1.getCoin();
        int increment = 1000;
        double mainPoint = currentCoin + increment;
        /////


        BigDecimal bd = new BigDecimal(mainPoint).setScale(5, RoundingMode.HALF_UP);
        double newInput = bd.doubleValue();
        double p = newInput;

        HashMap<String, Object> map = new HashMap<>();
        map.put("refercount", count);
        map.put("coin", p);


        mRef.child(userModle1.getUid())
                .updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            updateownSild();
                        }
                    }
                });
    }

    private void updateownSild() {

        mRef.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        UserModle userModle = snapshot.getValue(UserModle.class);

                        //String cop=userModle.getCoin().replace(",","");
                        double currentCoin = userModle.getCoin();
                        double mainPoint = currentCoin + 5000;
                        BigDecimal bd = new BigDecimal(mainPoint).setScale(5, RoundingMode.HALF_UP);
                        double newInput = bd.doubleValue();
                        double p = newInput;

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("coin", p);
                        hashMap.put("referuse", "yes");

                        mRef.child(mAuth.getUid())
                                .updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(getApplicationContext(), "Refer Completed", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();


                                    }
                                });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void inits() {
        mRef.child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressDialog.dismiss();
                        userModle = snapshot.getValue(UserModle.class);
                        binding.totalRefer.setText("" + userModle.getRefercount());
                        binding.referCodeTV.setText("" + userModle.getRefercode());
                        if (userModle.getReferuse().equals("")) {
                            loadDailoag();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });
    }

    public void shareText(String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent .setType("text/plain");
        txtIntent .putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite And Eran");
        txtIntent .putExtra(android.content.Intent.EXTRA_TEXT, body);
        startActivity(Intent.createChooser(txtIntent ,"Share"));
    }
    private void setClipboard(Context context, String text) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copy", Toast.LENGTH_SHORT).show();
    }
    private String sendRevire(){
        final String appPackageName = getApplication().getPackageName();
        String url="https://play.google.com/store/apps/details?id=" + appPackageName;
        return  url;
    }
}