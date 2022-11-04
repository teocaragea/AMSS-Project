package com.example.licienta2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Edit extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawerLayout;
    public ActionBarDrawerToggle actionBarDrawerToggle;
    TextView edit_nume;
    TextView nav_name,nav_adresa;
    EditText edit_Mail, edit_Tel;
    Button btnSubmit;
    TextView tvCNP, tvNrMat, tvFacultate;
    User user=new User();

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
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        edit_nume = findViewById(R.id.edit_nume);
        edit_Mail = findViewById(R.id.edit_Mail);
        tvCNP = findViewById(R.id.tvCNP);
        tvFacultate = findViewById(R.id.tvFacultate);
        tvNrMat = findViewById(R.id.tvNrMat);
        btnSubmit = findViewById(R.id.btnSubmit);
        nav_name = header.findViewById(R.id.nav_name);
        nav_adresa = header.findViewById(R.id.nav_adresa);
        nav_adresa.setText(currentUser.getEmail());

        try {
            mDatabase2.orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {

                        for (DataSnapshot datas : snapshot.getChildren()) {
                            if (datas.getKey().toString().equals("name")) {
                                nav_name.setText(datas.getValue().toString());
                                edit_nume.setText("Bine ai venit, " + datas.getValue().toString());
                                user.setName(datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("email")) {
                                edit_Mail.setHint(datas.getValue().toString());
                                user.setEmail(datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("phone")) {
                                tvCNP.setText("CNP: " + datas.getValue().toString());
                                user.setPhone(datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("nrMat")) {
                                tvNrMat.setText("Numar matricol: " + datas.getValue().toString());
                                user.setNrMat(datas.getValue().toString());
                            }
                            if (datas.getKey().toString().equals("facultate")) {
                                tvFacultate.setText(datas.getValue().toString());
                                user.setFacultate(datas.getValue().toString());
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
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
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

        setContentView(R.layout.activity_edit);
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        edit_nume = findViewById(R.id.edit_nume);
        edit_Mail = findViewById(R.id.edit_Mail);
        btnSubmit = findViewById(R.id.btnSubmit);

        try
        {
            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
            drawerLayout = findViewById(R.id.my_drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    btnSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            String email = edit_Mail.getText().toString().trim();
            String phone = edit_Tel.getText().toString().trim();

            if(!TextUtils.isEmpty(email) || !TextUtils.isEmpty(phone))
            {
                if(TextUtils.isEmpty(email))
                {
                    email = user.getEmail();
                }
                if(TextUtils.isEmpty(phone)) {
                    phone = user.getPhone();
                }
            }

        }

    });
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