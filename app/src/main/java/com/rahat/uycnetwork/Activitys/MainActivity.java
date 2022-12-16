package com.rahat.uycnetwork.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahat.uycnetwork.Fragments.HomeFragment;
import com.rahat.uycnetwork.Modles.Config;
import com.rahat.uycnetwork.R;
import com.rahat.uycnetwork.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setBottomView();
        //loadFragment(new HomeFragment());
        mAuth=FirebaseAuth.getInstance();
        mRef= FirebaseDatabase.getInstance().getReference().child("Config");
        inte();
        binding.bottomNav.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                if (item.getId()==1){
                    loadFragment(new HomeFragment());
                }else  if (item.getId()==2){
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                }else if (item.getId()==5){
                    startActivity(new Intent(getApplicationContext(),wallet.class));
                }
            }
        });
        binding.bottomNav.show(1, true);
        binding.bottomNav.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {

            }
        });
        binding.bottomNav.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {

            }
        });

    }
    private void loadFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FrameLayout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private void inte(){
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Config config=snapshot.getValue(Config.class);
                    if (config.getServer().equals("off")){
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Warning Alert !");
                        builder.setMessage("Server on maintenance wait some time");
                        builder.setCancelable(false);
                        builder.setNeutralButton("Exit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                quit();
                            }
                        });
                        AlertDialog alertDialog=builder.create();
                        alertDialog.show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void quit() {
        int pid = android.os.Process.myPid();
        android.os.Process.killProcess(pid);
        System.exit(0);
    }

    @Override
    public void onBackPressed() {
        quit();
    }

    private void setBottomView() {
        binding.bottomNav.add(new MeowBottomNavigation.Model(1, R.drawable.ic_home));
        binding.bottomNav.add(new MeowBottomNavigation.Model(2, R.drawable.ic_account));
        binding.bottomNav.add(new MeowBottomNavigation.Model(5, R.drawable.ic_wallet));
    }
}