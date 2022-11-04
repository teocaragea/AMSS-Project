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

public class IstoricEntity extends AppCompatActivity {
    ImageView ivOrd;
    TextView tvUser, tvPretOrder, tvComanda, tvStatus, tvTime;
    Button btnAnulare, btnCloseOrder;
    DatabaseReference mDatabase, mDatabase2, mDatabase3;
    String pret;
    HashMap<String,Integer> order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istoric_entity);

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
        btnAnulare = findViewById(R.id.btnAnulare);
        btnCloseOrder = findViewById(R.id.btnCloseOrder);
        tvComanda = findViewById(R.id.tvQT);
        tvTime = findViewById(R.id.tvTime);
        tvStatus = findViewById(R.id.tvSts);
        tvStatus.setText("Status: " + rezolvat);
        tvPretOrder.setText("Pret: " + pret + " lei");
        tvComanda.setText(id.toString());
        tvTime.setText("Se ridica la ora " + ora.toString());

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
        } catch (Exception e) {
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
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


        btnCloseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Istoric.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnAnulare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rezolvat.equals(Constants.RIDICATA)) {
                    btnAnulare.setError("Nu poti anula o comanda finalizata!");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setCancelable(true);
                    builder.setTitle("Vrei sa anulezi comanda?");
                    builder.setPositiveButton("Confirm",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Istoric");
                                    mDatabase.orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
                                        @Override
                                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                            Boolean schimba = false;
                                            try {
                                                for (DataSnapshot datas : snapshot.getChildren()) {
                                                    if (datas.getKey().toString().equals("image") && datas.getValue().equals(image)) {
                                                        schimba = true;
                                                    }
                                                    if (datas.getKey().toString().equals("rezolvat") && !datas.getValue().equals(Constants.IN_PREGATIRE)) {
                                                        schimba = false;
                                                    }
                                                }
                                                if (schimba) {
                                                    snapshot.getRef().child("rezolvat").setValue(Constants.ANULAT_BY_USER);
                                                    Toast.makeText(IstoricEntity.this, "Comanda anulata cu succes!", Toast.LENGTH_SHORT).show();
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
                                    try {
                                        Intent intent = new Intent(getApplicationContext(), Istoric.class);
                                        startActivity(intent);
                                    } catch (Exception e) {
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
            }
        });

    }
}