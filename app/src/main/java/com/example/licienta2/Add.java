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

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class Add extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference mDatabase;
    EditText etNume, etCantitate, etPret, etCategorie;
    TextView nav_name, nav_adresa;
    Button btnSubmit, btnImage;
    Uri mImage;
    ImageView ivTest;
    public static String path="";
    StorageReference mStorageRef;
    final int GALLERY_INTENT = 2;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

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
        mAuth=FirebaseAuth.getInstance();
        FirebaseUser currentUser =mAuth.getCurrentUser();
        currentUser.getEmail();
        nav_name = header.findViewById(R.id.nav_name);
        nav_adresa = header.findViewById(R.id.nav_adresa);
        nav_adresa.setText(currentUser.getEmail());
        DatabaseReference mDatabase2;
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        try {

            mDatabase2.orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    try {
                        for (DataSnapshot datas : snapshot.getChildren()){
                            if( datas.getKey().toString().equals("name")){
                                nav_name.setText(datas.getValue().toString());
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
                    }catch (Exception e){
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
            });}
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        drawerLayout = findViewById(R.id.my_drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        etNume = findViewById(R.id.etNume);
        etCantitate = findViewById(R.id.etCantitate);
        etPret = findViewById(R.id.etPret);
        etCategorie = findViewById(R.id.etCategorie);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnImage = findViewById(R.id.btnImage);
        ivTest =findViewById(R.id.ivTest);
        mStorageRef = FirebaseStorage.getInstance().getReference();

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


        btnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i, GALLERY_INTENT);
            }
        });



        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nume, pret,cantitate, categorie;
                nume = etNume.getText().toString().trim();
                pret = etPret.getText().toString().trim();
                cantitate = etCantitate.getText().toString().trim();
                categorie = etCategorie.getText().toString().trim();

                if(TextUtils.isEmpty(nume)){
                    etNume.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(pret)){
                    etPret.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(cantitate)){
                    etCantitate.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(categorie)){
                    etCategorie.setError("Email is required.");
                    return;
                }
                StorageReference file = mStorageRef.child("Produs").child(nume + ".jpg");
                UploadTask uploadTask= file.putFile(mImage);
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){
                            throw task.getException();
                        }

                        return file.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            Uri downloadUri =task.getResult();
                            path = downloadUri.toString();
                            Produs p=new Produs(nume,pret,cantitate,path,categorie);
                            Task task1 =mDatabase.child("Produs").child(nume).setValue(p);
                            task1.addOnSuccessListener(new OnSuccessListener() {
                                @Override
                                public void onSuccess(Object o) {
                                    Toast.makeText(Add.this, "Produsul a fost adaugat cu succes", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                }
                            });

                            task1.addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Add.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImage = data.getData();
            ivTest.setImageURI(mImage);
        }
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }



}