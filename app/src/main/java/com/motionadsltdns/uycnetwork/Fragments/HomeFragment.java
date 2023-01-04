package com.motionadsltdns.uycnetwork.Fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motionadsltdns.uycnetwork.Adapters.PackageAdapter;
import com.motionadsltdns.uycnetwork.Modles.Config;
import com.motionadsltdns.uycnetwork.Modles.MiningModle;
import com.motionadsltdns.uycnetwork.Modles.PackagesModle;
import com.motionadsltdns.uycnetwork.Modles.PlanModle;
import com.motionadsltdns.uycnetwork.Modles.SliderModle;
import com.motionadsltdns.uycnetwork.Modles.UserModle;
import com.motionadsltdns.uycnetwork.R;
import com.motionadsltdns.uycnetwork.databinding.FragmentHomeBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    DatabaseReference mRef;
    DatabaseReference mConfig;
    DatabaseReference mWithdraw;
    DatabaseReference mPlan;
    DatabaseReference mMining;
    DatabaseReference mPackages;
    DatabaseReference mSlider;
    String TAG="MyTag";
    PlanModle planModle;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    ArrayList<PackagesModle> arrayList=new ArrayList<>();
    UserModle userModle;
    Config config;
    MiningModle miningModle;
    boolean isDataValid=false;
    boolean isMining=false;
    int one,two,three;
    int speed;
    int oneSpeed,TwoSpeed,ThreeSpeed;
    int oneEndTime,TwoEndTime,ThreeEndTime;
    PackageAdapter packageAdapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(inflater,container,false);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("Loading....");
        progressDialog.setCancelable(false);
        mRef = FirebaseDatabase.getInstance().getReference().child("Users");
        mConfig = FirebaseDatabase.getInstance().getReference().child("Config");
        mWithdraw = FirebaseDatabase.getInstance().getReference().child("Withdraw");
        mPlan = FirebaseDatabase.getInstance().getReference().child("packages");
        mPackages = FirebaseDatabase.getInstance().getReference().child("Plans");
        mMining = FirebaseDatabase.getInstance().getReference().child("Mining");
        mSlider = FirebaseDatabase.getInstance().getReference().child("Slider");
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        binding.packageRecycler.setHasFixedSize(true);
        binding.packageRecycler.setLayoutManager(linearLayoutManager);

        init();
        loadAd();
        loadSlider();

        //Log.d(TAG, "onCreateView: "+getEndPackage());
//        binding.planOneBtn.setOnClickListener(v->{
//            if (isCoin(userModle.getCoin(),one)){
//                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
//                builder.setTitle("Purchase Plan");
//                builder.setMessage("Do you want to buy the plan ?");
//                builder.setCancelable(false);
//                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        double oneCoin=userModle.getCoin()-one;
//                        HashMap<String,Object> hashMap=new HashMap<>();
//                        hashMap.put("coin",oneCoin);
//                        PlanModle planModle=new PlanModle(mAuth.getUid(),userModle.getName(),getTodayTime(),"Plan0",getEndPackage(oneEndTime),oneSpeed);
//                        mRef.child(mAuth.getUid())
//                                .updateChildren(hashMap)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            mPackages.child(mAuth.getUid())
//                                                    .setValue(planModle)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()){
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
//
//                                                            }else{
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(getActivity(), "Error "+task.getException(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                });
//
//                    }
//                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog alertDialog=builder.create();
//                alertDialog.show();
//            }else{
//                Toast.makeText(getActivity(), "You don't have coin please add money fast.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        binding.planTwoBtn.setOnClickListener(v->{
//            if (isCoin(userModle.getCoin(),two)){
//                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
//                builder.setTitle("Purchase Plan");
//                builder.setMessage("Do you want to buy the plan ?");
//                builder.setCancelable(false);
//                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        double oneCoin=userModle.getCoin()-two;
//                        HashMap<String,Object> hashMap=new HashMap<>();
//                        hashMap.put("coin",oneCoin);
//                        PlanModle planModle=new PlanModle(mAuth.getUid(),userModle.getName(),getTodayTime(),"Plan1",getEndPackage(TwoEndTime),ThreeSpeed);
//                        mRef.child(mAuth.getUid())
//                                .updateChildren(hashMap)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            mPackages.child(mAuth.getUid())
//                                                    .setValue(planModle)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()){
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
//                                                            }else{
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(getActivity(), "Error "+task.getException(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                });
//
//                    }
//                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog alertDialog=builder.create();
//                alertDialog.show();
//            }else{
//                Toast.makeText(getActivity(), "You don't have coin please add money fast.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        binding.planThreeBtn.setOnClickListener(v->{
//            if (isCoin(userModle.getCoin(),three)){
//                AlertDialog.Builder builder =new AlertDialog.Builder(getActivity());
//                builder.setTitle("Purchase Plan");
//                builder.setMessage("Do you want to buy the plan ?");
//                builder.setCancelable(false);
//                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        double oneCoin=userModle.getCoin()-three;
//                        HashMap<String,Object> hashMap=new HashMap<>();
//                        hashMap.put("coin",oneCoin);
//                        PlanModle planModle=new PlanModle(mAuth.getUid(),userModle.getName(),getTodayTime(),"Plan2",getEndPackage(ThreeEndTime),ThreeSpeed);
//                        mRef.child(mAuth.getUid())
//                                .updateChildren(hashMap)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()){
//                                            mPackages.child(mAuth.getUid())
//                                                    .setValue(planModle)
//                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                        @Override
//                                                        public void onComplete(@NonNull Task<Void> task) {
//                                                            if (task.isSuccessful()){
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(getActivity(), "Purchased", Toast.LENGTH_SHORT).show();
//                                                            }else{
//                                                                progressDialog.dismiss();
//                                                                Toast.makeText(getActivity(), "Error "+task.getException(), Toast.LENGTH_SHORT).show();
//                                                            }
//                                                        }
//                                                    });
//                                        }
//                                    }
//                                });
//
//                    }
//                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        dialogInterface.dismiss();
//                    }
//                });
//                AlertDialog alertDialog=builder.create();
//                alertDialog.show();
//            }else{
//                Toast.makeText(getActivity(), "You don't have coin please add money fast.", Toast.LENGTH_SHORT).show();
//            }
//        });

        binding.startMimingBtn.setOnClickListener(v->{
            if (!isMining){
                if (isDataValid){
                    Toast.makeText(getActivity(), "we Can mine", Toast.LENGTH_SHORT).show();
                }else{
                   //
                    getTimeFuture();
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           getTimeLeft();
                       }
                   },3000);
                }
            }else{
                Toast.makeText(getActivity(), "Mining Running !", Toast.LENGTH_SHORT).show();
            }
        });
        binding.collectCoinBtn.setOnClickListener(v->{
           if (miningModle.getMining()!=null){
               if (miningModle.getMining().equals("")){
                   Toast.makeText(getActivity(), "Not Finish Yet", Toast.LENGTH_SHORT).show();
               }
               else{
                   double tmpCoin=userModle.getCoin()+getSpeed(planModle.getPlan());
                   HashMap<String,Object> map=new HashMap<>();
                   map.put("coin",tmpCoin);

                   mRef.child(mAuth.getUid())
                           .updateChildren(map)
                           .addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   if (task.isSuccessful()){
                                       mMining.child(mAuth.getUid())
                                               .removeValue()
                                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                   @Override
                                                   public void onComplete(@NonNull Task<Void> task) {
                                                       if (task.isSuccessful()){
                                                           Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                       }else{
                                                           Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                                                       }
                                                   }
                                               });
                                   }
                               }
                           });
               }
           }
           else{
               Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
           }
        });

        return binding.getRoot();
    }

    private void loadSlider() {
        ArrayList<SlideModel> slideModelArrayList=new ArrayList<>();
        mSlider.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        SliderModle slideModel=dataSnapshot.getValue(SliderModle.class);
                        slideModelArrayList.add(new SlideModel(slideModel.getImage(), ScaleTypes.FIT));

                    }
                    binding.imageSlider.setImageList(slideModelArrayList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    void init(){

        mPlan.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        PackagesModle packagesModle=dataSnapshot.getValue(PackagesModle.class);
                        arrayList.add(packagesModle);
                    }

                    packageAdapter=new PackageAdapter(getActivity(),arrayList);
                    binding.packageRecycler.setAdapter(packageAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        mRef.child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            userModle=snapshot.getValue(UserModle.class);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        mPackages.child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            binding.planSection.setVisibility(View.INVISIBLE);
                            planModle=snapshot.getValue(PlanModle.class);
                            if (getTimeForEndPackage().compareTo(planModle.getEndtime()) < 0)
                            {
                                //not done
                                //Toast.makeText(getActivity(), "Not Match", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //time done
                                mPackages.child(mAuth.getUid())
                                        .removeValue();
                                Toast.makeText(getActivity(), "Package Time Over", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        mMining.child(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            isDataValid=true;
                            miningModle=snapshot.getValue(MiningModle.class);
                            if (!miningModle.getTime().equals("")) {
                                getTimeLeft();
                                isMining = true;
                            } else {
                                isMining = false;
                            }
                        }else{
                            isDataValid=false;
                            //getTimeFuture();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        isDataValid=false;
                    }
                });
        mConfig.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                config=snapshot.getValue(Config.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    boolean isCoin(double coin,int com){
        if (coin<=com){
            return false;
        }else{
            return true;
        }
    }
    private String getTodayTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        Date date = new Date();
        return dateFormat.format(date);
    }
    private void getTimeFuture() {

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        final Date date;
        try {
            date = sdf.parse(getTodayTime());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.HOUR, 12);
            System.out.println("Time here " + sdf.format(calendar.getTime()));
            String time = sdf.format(calendar.getTime());
            //Toast.makeText(getActivity(), "" + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            MiningModle miningModle=new MiningModle(mAuth.getUid(),sdf.format(calendar.getTime()),"");
            mMining.child(mAuth.getUid())
                    .setValue(miningModle);

        } catch (ParseException e) {
            e.printStackTrace();
        }


        //return sdf.format(calendar);
    }
    private void getTimeLeft() {

            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
            try {
                Date oldTime = sdf.parse(miningModle.getTime());
                Date currentDate = sdf.parse(getTodayTime());

                long diff = oldTime.getTime() - currentDate.getTime();
                // Log.d("MyTag", "Days: " + TimeUnit.MINUTES.convert(diff, TimeUnit.HOURS));
//                if (diff <= 0) {
//                    isMining = false;
//                    Toast.makeText(getActivity(), "Diff Section", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(getActivity(), "Not End Yat", Toast.LENGTH_SHORT).show();
//                }


                CountDownTimer count = new CountDownTimer(diff, 1000) {
                    @Override
                    public void onTick(long milliseconds) {

//                        long hours = TimeUnit.MILLISECONDS.toHours(milliseconds);
//                        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
//                        long seconds = TimeUnit.MILLISECONDS.toSeconds(milliseconds);
//                        binding.timeTV.setText(hours+" : "+minutes+" : "+seconds);
//
//                        System.out.println("hours: " + hours);
//                        System.out.println("minutes: " + minutes);
//                        System.out.println("seconds: " + seconds);

                        int seconds = (int) (milliseconds / 1000) % 60;
                        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
                        int hours = (int) ((milliseconds / (1000 * 60 * 60)) % 24);
                        binding.timeCounter.setText("" + hours + " : " + minutes + " : " + seconds);

                        //Toast.makeText(getActivity(), ""+doneTime, Toast.LENGTH_SHORT).show();


                    }

                    @Override
                    public void onFinish() {
                        //long seconds = TimeUnit.MILLISECONDS.toSeconds(oldTime.getTime());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("mining","done");
                        mMining.child(mAuth.getUid())
                                .updateChildren(hashMap);
                        //Toast.makeText(getActivity(), "You Can Claim Now", Toast.LENGTH_SHORT).show();
                        isMining = false;
                    }
                }.start();


            } catch (ParseException e) {
                e.printStackTrace();
            }

        }

    private int getSpeed(String plan) {
        int tmp=0;
        if (plan.equals("plan0")){
            tmp=oneSpeed;
        }else if (plan.equals("plan1")){
            tmp=TwoSpeed;
        }else if (plan.equals("plan2")){
            tmp=ThreeSpeed;
        }

        return tmp;
    }

    private String getEndPackage(int end) {

        String timeDate=null;

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        final Date date;
        try {
            date = sdf.parse(getTodayTime());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DAY_OF_YEAR, end);
            System.out.println("Time here " + sdf.format(calendar.getTime()));
            String time = sdf.format(calendar.getTime());
            //Toast.makeText(getActivity(), "" + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
          timeDate= sdf.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return timeDate;


    }

    private String getTimeForEndPackage() {

        String timeDate=null;

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss aa");
        final Date date;
        try {
            date = sdf.parse(getTodayTime());
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            System.out.println("Time here " + sdf.format(calendar.getTime()));
            String time = sdf.format(calendar.getTime());
            //Toast.makeText(getActivity(), "" + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            timeDate= sdf.format(calendar.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }


        return timeDate;


    }

}