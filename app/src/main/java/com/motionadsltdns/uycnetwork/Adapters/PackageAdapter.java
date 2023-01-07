package com.motionadsltdns.uycnetwork.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.motionadsltdns.uycnetwork.Modles.PackagesModle;
import com.motionadsltdns.uycnetwork.Modles.PlanModle;
import com.motionadsltdns.uycnetwork.Modles.UserModle;
import com.motionadsltdns.uycnetwork.R;
import com.rahat.uycnetwork.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.viewholder> {

    private Context context;
    private List<PackagesModle> packagesModleList;
    DatabaseReference mUser;
    DatabaseReference mPackages;
    FirebaseAuth mAuth;
    UserModle userModle;
    ProgressDialog progressDialog;

    public PackageAdapter(Context context, List<PackagesModle> packagesModleList) {
        this.context = context;
        this.packagesModleList = packagesModleList;
    }

    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.package_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {
        progressDialog=new ProgressDialog(context);
        progressDialog.setTitle("Loading...");
        progressDialog.setCancelable(false);
        mAuth=FirebaseAuth.getInstance();
        mUser= FirebaseDatabase.getInstance().getReference().child("Users");
        mPackages = FirebaseDatabase.getInstance().getReference().child("Plans");
        getData();


        PackagesModle packagesModle=packagesModleList.get(position);
        holder.name.setText(""+packagesModle.getName());
        holder.price.setText(""+packagesModle.getPrice());
        holder.itemView.setOnClickListener(v->{

            if (isCoin(userModle.getCoin(),packagesModle.getPrice())){
                AlertDialog.Builder builder =new AlertDialog.Builder(context);
                builder.setTitle("Purchase Plan");
                builder.setMessage("Do you want to buy the plan ?");
                builder.setCancelable(false);
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        double oneCoin=userModle.getCoin()-packagesModle.getPrice();
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("coin",oneCoin);
                        PlanModle planModle=new PlanModle(mAuth.getUid(),userModle.getName(),getTodayTime(),packagesModle.getName(),getEndPackage(packagesModle.getTime()),packagesModle.getSpeed());
                        progressDialog.show();
                        mUser.child(mAuth.getUid())
                                .updateChildren(hashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            progressDialog.dismiss();
                                            mPackages.child(mAuth.getUid())
                                                    .setValue(planModle)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()){
                                                                progressDialog.dismiss();
                                                                Toast.makeText(context, "Purchased", Toast.LENGTH_SHORT).show();

                                                            }else{
                                                                progressDialog.dismiss();
                                                                Toast.makeText(context, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    }
                                });

                    }
                }).setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }else{
                Toast.makeText(context, "You don't have coin please add money fast.", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void getData() {
        mUser.child(mAuth.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
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
    }

    @Override
    public int getItemCount() {
        return packagesModleList.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView name,price;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.packageName);
            price=itemView.findViewById(R.id.packagePrice);
        }
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
}
