package com.example.licienta2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Cos extends AppCompatActivity {
    ListView listView;
    Button btnTrm, btnCls, timeButton;
    TextView tvTotal;
    DatabaseReference mDatabase, mDatabase2;
    String email;
    Double total = 0.0;
    ArrayAdapter<Produs> adapter;
    Integer hour=0, minute=0;
    private View mProgressView;
    private View mLoginFormView;
    private TextView tvLoad;
    String image = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cos);

        mLoginFormView = findViewById(R.id.linearLayout);
        mProgressView = findViewById(R.id.login_progress);
        tvLoad = findViewById(R.id.tvLoad);
        tvLoad.setVisibility(View.GONE);

        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        email = currentUser.getEmail();
        tvTotal = findViewById(R.id.tvTotal);
        btnTrm = findViewById(R.id.btnTrm);
        timeButton = findViewById(R.id.btnTime);
        Intent i = getIntent();
       // list = (ArrayList<Produs>) i.getSerializableExtra("set");
        adapter = new cosAdapter(this, 0, (ArrayList<Produs>)ProdusBase.get().getProduse());

        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        for (Produs p : ProdusBase.get().getProduse()) {
            if(currentHour == 15
                    && !p.getCategorie().equals("bauturi")
                    && !p.getCategorie().equals("grup"))
            {
                total += Double.valueOf(p.getPret()) * p.getQt() / 2;
            }else{
                total += Double.valueOf(p.getPret()) * p.getQt();
            }
        }
        tvTotal.setText("Total: " + String.valueOf(total) + " lei");
        btnTrm = findViewById(R.id.btnTrm);
        btnCls = findViewById(R.id.btnCls);
        listView = findViewById(R.id.listView);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Istoric");
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Users");
        try {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Toast.makeText(Cos.this, "click on", Toast.LENGTH_SHORT).show();
                }
            });
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        btnCls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(Cos.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnTrm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar rightNow = Calendar.getInstance();
                Ora ora = new Ora(hour, minute);
                if(hour == 0 && minute == 0) {
                    ora.setOra(rightNow.get(Calendar.HOUR_OF_DAY));
                    ora.setMinut(rightNow.get(Calendar.MINUTE));
                    ora.add(30);
                }
                HashMap<String, Integer> order = new HashMap<String, Integer>();
                for (Produs p : ProdusBase.get().getProduse()) {
                    if (image.isEmpty()) {
                        image = p.getNume();
                    }
                    order.put(p.getNume(), p.getQt());
                }
                mDatabase2.orderByChild("email").equalTo(currentUser.getEmail()).limitToFirst(1).addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Boolean ok = false;
                        int k = 0;
                        for (DataSnapshot datas : snapshot.getChildren()) {
                            if (datas.getKey().toString().equals("nrActive")
                                    && Integer.valueOf(datas.getValue().toString()) < 3) {
                                if(ora.isOnSchedule()){
                                    ok = true;
                                    k =Integer.valueOf(datas.getValue().toString());
                                    showProgress(true);
                                    snapshot.getRef().child("nrActive").setValue(k + 1);
                                    IstoricData id = new IstoricData(email, "In pregatire", image, String.valueOf(total), false, ora.getOra(), ora.getMinut(), order);
                                    Task t = mDatabase.push().setValue(id);
                                    t.addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(Cos.this, "Comanda a fost trimisa cu succes", Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });
                                    t.addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Cos.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                            showProgress(false);
                                        }
                                    });
                                    ProdusBase.get().setProduse(null);
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }else{
                                    btnTrm.setError("Comanda in afara programului");
                                    Toast.makeText(Cos.this, "Comanda se afla in afara programului!", Toast.LENGTH_SHORT).show();
                                }
                            }else  if (datas.getKey().toString().equals("nrActive")
                                    && Integer.valueOf(datas.getValue().toString()) == 3){
                                btnTrm.setError("Ai deja 3 comenzi active");
                                Toast.makeText(Cos.this, "Nu mai poti plasa o comanda deoarece ai deja 3 comenzi active!", Toast.LENGTH_SHORT).show();
                            }
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

        });
    }

    public void remove(int positon) {
        ProdusBase.get().removeProdus(positon);
        listView.setAdapter(adapter);
        Double total2 = 0.0;
        for (Produs p : ProdusBase.get().getProduse()) {
            total2 += Double.valueOf(p.getPret()) * p.getQt();
        }
        tvTotal.setText("Total: " + String.valueOf(total2) + " lei");
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });

            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            tvLoad.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }

    }
    public void popTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
            }
        };


        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}