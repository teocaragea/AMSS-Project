package com.example.licienta2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class OrderEntity extends AppCompatActivity {
    ImageView ivOrd;
    TextView tvUser, tvPretOrder, tvOrder, tvStatus, tvTime;
    Button btnFinish, btnCloseOrder, btnAnulare, btnRidicat;
    DatabaseReference mDatabase, mDatabase2, mDatabase3;
    String pret;
    HashMap<String,Integer> order;
    Boolean gata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_entity);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String rezolvat = getIntent().getStringExtra("rezolvat");
        String image = getIntent().getStringExtra("image");
        String email = getIntent().getStringExtra("email");
        Integer hour = getIntent().getIntExtra("hour",0);
        Integer minute = getIntent().getIntExtra("minute",0);
        Boolean finalizat = getIntent().getBooleanExtra("finalizat",false);
        Ora ora = new Ora(hour,minute);
        order = (HashMap<String, Integer>) getIntent().getSerializableExtra("hashMap");
        pret = getIntent().getStringExtra("pret");
        IstoricData id = new IstoricData(email,rezolvat,image,pret,finalizat,hour,minute,order);

        ivOrd = findViewById(R.id.ivOrd);
        tvPretOrder = findViewById(R.id.tvPretOrder);
        tvUser = findViewById(R.id.tvUser);
        tvTime = findViewById(R.id.tvTme);
        tvOrder = findViewById(R.id.tvQT);
        btnFinish = findViewById(R.id.btnFinish);
        btnAnulare = findViewById(R.id.btnAnulareFromHost);
        btnCloseOrder = findViewById(R.id.btnCloseOrder);
        btnRidicat = findViewById(R.id.btnRidicat);
        tvStatus = findViewById(R.id.tvStat);
        tvStatus.setText("Status: " + rezolvat);
        tvPretOrder.setText("Pret: " + pret + " lei");
        tvOrder.setText(id.toString());
        tvTime.setText("Comanda pentru ora " + ora.toString());
        if(rezolvat.equals(Constants.FINALIZAT)){
            btnRidicat.setVisibility(View.VISIBLE);
            btnFinish.setVisibility(View.GONE);
        }else{
            btnRidicat.setVisibility(View.GONE);
            btnFinish.setVisibility(View.VISIBLE);
        }

        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        try {
            mDatabase2.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        for (DataSnapshot datas : snapshot.getChildren()) {

                            if (datas.getKey().toString().equals("name")) {
                                tvUser.setText("Comanda plasata de: " + datas.getValue().toString());
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        mDatabase3 = FirebaseDatabase.getInstance().getReference().child("Produs");
        try {
            mDatabase3.orderByChild("nume").equalTo(image).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        for (DataSnapshot datas : snapshot.getChildren()) {

                            if (datas.getKey().toString().equals("image")) {
                                Picasso.get().load(datas.getValue().toString()).into(ivOrd);
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
        catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }



        btnCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(getApplicationContext(),Order.class);
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setTitle("Comanda este finalizata?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Istoric");
                                mDatabase.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        Boolean schimba = false;
                                        Boolean gata = false;
                                        try {
                                            for (DataSnapshot datas : snapshot.getChildren()) {
                                                if (datas.getKey().toString().equals("image") && datas.getValue().equals(image)) {
                                                    schimba = true;
                                                }
                                                if(datas.getKey().toString().equals("rezolvat") && datas.getValue().equals(Constants.ANULAT_BY_USER)){
                                                    gata = true;
                                                }
                                                if(datas.getKey().toString().equals("rezolvat") && !datas.getValue().equals(Constants.IN_PREGATIRE)){
                                                    schimba = false;
                                                }
                                            }
                                            if(gata){
                                                snapshot.getRef().child("finalizat").setValue(true);
                                            }
                                            if (schimba) {
                                                    snapshot.getRef().child("rezolvat").setValue(Constants.FINALIZAT);
                                                    Toast.makeText(OrderEntity.this, "Comanda Finalizata!", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                try{
                                    Intent intent = new Intent(getApplicationContext(),Order.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        btnAnulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setTitle("Comanda anulata sau neridicata?");
                builder.setMessage("Utilizatorul va fi blocat daca nu va ridica 3 comenzi.");
                builder.setPositiveButton("Anulata",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Istoric");
                                mDatabase.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        Boolean schimba = false;
                                        try {
                                            for (DataSnapshot datas : snapshot.getChildren()) {
                                                if (datas.getKey().toString().equals("image") && datas.getValue().equals(image)) {
                                                    schimba = true;
                                                }
                                                if(datas.getKey().toString().equals("rezolvat") && datas.getValue().equals(Constants.RIDICATA)){
                                                    schimba = false;
                                                }
                                            }
                                            if (schimba){
                                                snapshot.getRef().child("rezolvat").setValue(Constants.ANULAT_BY_HOST);
                                                snapshot.getRef().child("finalizat").setValue(true);
                                                Toast.makeText(OrderEntity.this, "Comanda anulata de cantina!", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                try{
                                    Intent intent = new Intent(getApplicationContext(),Order.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                NrComenzi nc = new NrComenzi(getApplicationContext(),currentUser.getEmail());
                                nc.decreaseNrComenziActive();
                            }
                        });
                builder.setNegativeButton("Neridicata", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("Istoric");
                        mDatabase.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                Boolean schimba = false;
                                try {
                                    for (DataSnapshot datas : snapshot.getChildren()) {
                                        if (datas.getKey().toString().equals("image") && datas.getValue().equals(image)) {
                                            schimba = true;
                                        }
                                        if(datas.getKey().toString().equals("rezolvat") && !datas.getValue().equals(Constants.FINALIZAT)){
                                            schimba = false;
                                        }
                                    }
                                    if (schimba){
                                        NrComenzi nc = new NrComenzi(getApplicationContext(),currentUser.getEmail());
                                        nc.increaseNrRefuzuri();
                                        nc.decreaseNrComenziActive();
                                        snapshot.getRef().child("rezolvat").setValue(Constants.NERIDICATA);
                                        snapshot.getRef().child("finalizat").setValue(true);
                                        Toast.makeText(OrderEntity.this, "Comanda anulata de utilizator!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                        try{
                            Intent intent = new Intent(getApplicationContext(),Order.class);
                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
        btnRidicat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setCancelable(true);
                builder.setTitle("Comanda a fost ridicata?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("Istoric");
                                mDatabase.orderByChild("email").equalTo(email).addChildEventListener(new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                        Boolean schimba = false;
                                        try {
                                            for (DataSnapshot datas : snapshot.getChildren()) {
                                                if (datas.getKey().toString().equals("image") && datas.getValue().equals(image)) {
                                                    schimba = true;
                                                }
                                                if(datas.getKey().toString().equals("rezolvat") && !datas.getValue().equals(Constants.FINALIZAT)){
                                                    schimba = false;
                                                }
                                            }
                                            if (schimba){
                                                snapshot.getRef().child("rezolvat").setValue(Constants.RIDICATA);
                                                snapshot.getRef().child("finalizat").setValue(true);
                                                Toast.makeText(OrderEntity.this, "Comanda ridicata!", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
                                try{
                                    Intent intent = new Intent(getApplicationContext(),Order.class);
                                    startActivity(intent);
                                }catch (Exception e){
                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                NrComenzi nc = new NrComenzi(getApplicationContext(),currentUser.getEmail());
                                nc.decreaseNrComenziActive();
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}