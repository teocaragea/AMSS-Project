package com.example.licienta2;

import android.app.Service;
import android.content.Context;
import android.provider.ContactsContract;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.concurrent.TimeUnit;
import java.util.logging.Handler;

public class NrComenzi {
    private int nrRefuzuri;
    private int nrComenziActive;
    private String email;
    Context context;
    int k;
    private DatabaseReference mDatabase;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public NrComenzi(){

    }
    public NrComenzi(Context context,String email){
        this.context = context;
        this.email = email;
        this.mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
    }
    public void loadNrRefuzuri(TextView tv){
        mDatabase.orderByChild("email").equalTo(email).limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    for (DataSnapshot datas : snapshot.getChildren()) {

                        if (datas.getKey().toString().equals("nrRefuzuri")) {
                            k = Integer.valueOf(datas.getValue().toString());
                            tv.setText("Numar de comenzi refuzate: " + datas.getValue().toString());
                        }

                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void increaseNrRefuzuri(){
        int k =0;
         mDatabase.orderByChild("email").equalTo(email).limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int k =0;
                try {
                    for (DataSnapshot datas : snapshot.getChildren()) {
                        if (datas.getKey().toString().equals("nrRefuzuri")) {
                        k = Integer.valueOf(datas.getValue().toString());
                        }
                    }
                    snapshot.getRef().child("nrRefuzuri").setValue(k +1);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void decreaseNrComenziActive(){
        int k =0;
        mDatabase.orderByChild("email").equalTo(email).limitToFirst(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                int k =0;
                try {
                    for (DataSnapshot datas : snapshot.getChildren()) {
                        if (datas.getKey().toString().equals("nrActive")) {
                            k = Integer.valueOf(datas.getValue().toString());
                        }
                    }
                    snapshot.getRef().child("nrActive").setValue(k - 1);
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
