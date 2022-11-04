package com.example.licienta2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class Account extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    TextView tv_accMail,tv_accName,tv_accPhone, tv_accNrMat, tv_accFacultate, tvRefuzuri;
    TextView nav_name,nav_adresa;
    Button btnEdit;
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(),Login.class));
                break;
            case R.id.nav_location:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Cantina Mihail Kogalniceanu")));
                break;
            case R.id.nav_account:
                startActivity(new Intent(getApplicationContext(),Account.class));
                break;
            case R.id.nav_info:
                startActivity(new Intent(getApplicationContext(),Info.class));
                break;
            case R.id.nav_menu:
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                break;
            case R.id.nav_istCom:
                startActivity(new Intent(getApplicationContext(),Istoric.class));
                break;
            case R.id.nav_order:
                startActivity(new Intent(getApplicationContext(),Order.class));
                break;

        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        tv_accMail=findViewById(R.id.tv_accMail);
        tv_accName=findViewById(R.id.tv_accName);
        tv_accPhone=findViewById(R.id.tv_accPhone);
        tv_accNrMat=findViewById(R.id.tv_accNrMat);
        tv_accFacultate=findViewById(R.id.tv_accFacultate);
        tvRefuzuri=findViewById(R.id.tvRefuzuri);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        NrComenzi nc = new NrComenzi(getApplicationContext(),currentUser.getEmail());
        nc.loadNrRefuzuri(tvRefuzuri);
        DatabaseReference mDatabase2;
        nav_name = header.findViewById(R.id.nav_name);
        nav_adresa = header.findViewById(R.id.nav_adresa);
        nav_adresa.setText(currentUser.getEmail());
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        try {

            mDatabase2.orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
//s
                        for (DataSnapshot datas : snapshot.getChildren()) {
                            if (datas.getKey().toString().equals("name")) {
                                nav_name.setText(datas.getValue().toString());
                                tv_accName.setText("Nume: " + datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("email")) {
                                tv_accMail.setText("Email: " + datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("phone")) {
                                tv_accPhone.setText("CNP: " + datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("nrMat")) {
                                tv_accNrMat.setText("Numar Matricol: " + datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("facultate")) {
                                tv_accFacultate.setText(datas.getValue().toString());
                            }
                            if(datas.getKey().toString().equals("admin")){
                                NavigationView navigationView = findViewById(R.id.navigation_view);
                                Menu nav_Menu = navigationView.getMenu();
                                if (datas.getValue().toString().equals("false")){
                                    nav_Menu.findItem(R.id.nav_order).setVisible(false);
                                }
                                else{
                                    nav_Menu.findItem(R.id.nav_order).setVisible(true);
                                }
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
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        try
        {

            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
            drawerLayout = findViewById(R.id.my_drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            btnEdit = findViewById(R.id.btnEdit);
            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(),Edit.class));
                }
            });
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }
    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }
}