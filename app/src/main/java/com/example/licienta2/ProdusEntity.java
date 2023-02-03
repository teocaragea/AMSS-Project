package com.example.licienta2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

public class ProdusEntity extends AppCompatActivity {

    ImageView ivProd;
    TextView tvNume, tvCantitate, tvPret, tvQt;
    Button btnClose, btnAdd, btnMinus, btnPlus;
    Integer k=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produs_entity);

        try {
            ivProd = findViewById(R.id.ivOrd);
            tvNume = findViewById(R.id.tvUser);
            btnClose = findViewById(R.id.btnCloseOrder);
            tvCantitate = findViewById(R.id.tvQT);
            tvPret = findViewById(R.id.tvPretOrder);
            btnAdd = findViewById(R.id.btnFinish);
            btnMinus = findViewById(R.id.btnMinus);
            btnPlus = findViewById(R.id.btnPlus);
            tvQt = findViewById(R.id.tvQt);


            String nume = getIntent().getStringExtra("nume");
            String image = getIntent().getStringExtra("image");
            String pret = getIntent().getStringExtra("pret");
            String cantitate = getIntent().getStringExtra("cantitate");
            String categorie = getIntent().getStringExtra("categorie");

            tvNume.setText(nume);
            Picasso.get().load(image).into(ivProd);
            tvPret.setText("Pret: " + pret + " lei");
            tvCantitate.setText("Gramaj: " + cantitate + " grame");


            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(ProdusEntity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });
            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        Produs p = new Produs(nume,pret,cantitate,image,categorie);
                        p.setQt(k);
                        ProdusBase.get().addProdus(p);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(ProdusEntity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

            btnPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    k++;
                    tvQt.setText(k.toString());
                }
            });
            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(k>0)
                    {
                        k--;
                        tvQt.setText(k.toString());
                    }
                }
            });

        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}