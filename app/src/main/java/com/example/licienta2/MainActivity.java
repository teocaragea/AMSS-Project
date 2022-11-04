package com.example.licienta2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.AsyncQueryHandler;
import android.content.Intent;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    TextView nav_name,nav_adresa;
    FloatingActionButton btnComanda;
    DatabaseReference mDatabase;
    RecyclerView rvProd;
    produsAdapter adapter;
    ActionBar actionBar;
    Button btnToate,btnPrincipal,btnCiorba,btnDesert, btnGarnituri,btnBauturi, btnDejun,btnGrup;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    static Boolean admin = false;
    static List<Produs> produse = new ArrayList<Produs>();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK){
            String test=data.getStringExtra("qt");
        }
        if(requestCode == RESULT_CANCELED){
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
        actionBar = getSupportActionBar();
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
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
            mDatabase2.orderByChild("email").equalTo(currentUser.getEmail()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               try {
                    for (DataSnapshot datas : snapshot.getChildren()){
                        if( datas.getKey().toString().equals("name")){
                            nav_name.setText(datas.getValue().toString());
                        }
                        if(datas.getKey().toString().equals("confirmat")){
                            if (datas.getValue().toString().equals("false")) {
                                startActivity(new Intent(getApplicationContext(), Wait.class));
                            }
                        }
                        if(datas.getKey().toString().equals("nrRefuzuri")){
                            if(Integer.valueOf(datas.getValue().toString()) == 3){
                                startActivity(new Intent(getApplicationContext(), Wait.class));
                            }
                        }
                        if(datas.getKey().toString().equals("admin")){
                            NavigationView navigationView = findViewById(R.id.navigation_view);
                            Menu nav_Menu = navigationView.getMenu();
                            drawerLayout = findViewById(R.id.my_drawer_layout);
                            if (datas.getValue().toString().equals("false")){
                                admin = false;
                                nav_Menu.findItem(R.id.nav_order).setVisible(false);
                               //drawerLayout.getRootView().findViewById(R.id.add).setVisibility(View.GONE);

                            }
                            else{
                                admin = true;
                                nav_Menu.findItem(R.id.nav_order).setVisible(true);
                               // drawerLayout.findViewById(R.id.add).setVisibility(View.VISIBLE);
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
        });
            adapter.startListening();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            String qt = getIntent().getStringExtra("qt");
            String nume = getIntent().getStringExtra("nume");
            String pret = getIntent().getStringExtra("pret");
            String image = getIntent().getStringExtra("image");
            String cantitate = getIntent().getStringExtra("cantitate");
            String categorie = getIntent().getStringExtra("categorie");
            if ((ArrayList<Produs>) getIntent().getSerializableExtra("set") != null) {
                produse = (ArrayList<Produs>) getIntent().getSerializableExtra("set");
            }

            if(nume != null){
                Produs p = new Produs(nume,pret,cantitate,image,categorie);
                Boolean isProdus = true;
                for(Produs produs : produse){
                    if(p.getNume().equals(produs.getNume())){
                        isProdus = false;
                        produs.setQt(produs.getQt() + Integer.parseInt(qt));
                    }
                }
                    if(isProdus){
                        p.setQt(Integer.parseInt(qt));
                        produse.add(p);
                    }
            }
        }catch (Exception e){
        }
        try{
            setContentView(R.layout.activity_main);
            btnToate = findViewById(R.id.btnToate);
            btnToate.setBackgroundColor(getResources().getColor(R.color.dark_grey));
            btnDesert = findViewById(R.id.btnDesert);
            btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnBauturi = findViewById(R.id.btnBauturi);
            btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnGrup = findViewById(R.id.btnGrup);
            btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnDejun = findViewById(R.id.btnDejun);
            btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnGarnituri= findViewById(R.id.btnGarnituri);
            btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnPrincipal = findViewById(R.id.btnPrincipal);
            btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnCiorba = findViewById(R.id.btnCiorba);
            btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
            btnComanda=findViewById(R.id.btnComanda);
            NavigationView navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
            drawerLayout = findViewById(R.id.my_drawer_layout);
            actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);
            drawerLayout.addDrawerListener(actionBarDrawerToggle);
            actionBarDrawerToggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mDatabase = FirebaseDatabase.getInstance().getReference().child("Produs");
            rvProd = findViewById(R.id.rvProd);
            rvProd.setLayoutManager(new WrapContentLinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
            FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                    .setQuery(mDatabase, Produs.class)
                    .build();
            adapter = new produsAdapter(this,options);
            rvProd.setAdapter(adapter);
            if(admin){
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(rvProd);
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        try {
            btnDesert.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("desert");
                    FirebaseRecyclerOptions<Produs> options2 = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options2);
                }
            });
            btnToate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnToate.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(mDatabase, Produs.class)
                            .build();
                   adapter.updateOptions(options);
                }
            });
            btnPrincipal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("principal");
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options);
                }
            });
            btnDejun.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("dejun");
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options);
                }
            });
            btnGrup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("grup");
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options);
                }
            });
            btnCiorba.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("ciorba");
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options);
                }
            });
            btnGarnituri.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("garnituri");
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options);
                }
            });
            btnBauturi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnToate.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDesert.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnBauturi.setBackgroundColor(getResources().getColor(R.color.dark_grey));
                    btnGarnituri.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnPrincipal.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnCiorba.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnDejun.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    btnGrup.setBackgroundColor(getResources().getColor(R.color.light_grey));
                    Query query = FirebaseDatabase.getInstance().getReference().child("Produs").orderByChild("categorie").equalTo("bauturi");
                    FirebaseRecyclerOptions<Produs> options = new FirebaseRecyclerOptions.Builder<Produs>()
                            .setQuery(query, Produs.class)
                            .build();
                    adapter.updateOptions(options);
                }
            });
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        btnComanda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent= new Intent(getApplicationContext(),Cos.class);
                    intent.putExtra("set",(Serializable)produse);
                    startActivity(intent);
                }
                catch (Exception e){
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(MainActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.red))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    adapter.notifyItemRemoved(position);
                    adapter.getRef(position).removeValue();
                    break;
            }

        }
    };
    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.getItem(0).setVisible(admin);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                startActivity(new Intent(getApplicationContext(), Add.class));
                break;
        }
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return  true;
        }
        return super.onOptionsItemSelected(item);
    }


}