package com.example.licienta2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class cosAdapter extends ArrayAdapter<Produs> {
    private Context context;
    ArrayList<Produs> produse;
    public cosAdapter(Context context, int resource, ArrayList<Produs> objects){
        super(context, resource, objects);
        this.context = context;
        this.produse = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RecyclerView.ViewHolder holder;LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView rvNume, rvPret, rvCantitate, rvQT;
        ImageView rvImage;
        CardView cardView;
        ImageView rvCls;
        Produs model = produse.get(position);
        View view = inflater.inflate(R.layout.prod_row, null);
        View cos = inflater.inflate(R.layout.activity_cos, null);
        rvNume = view.findViewById(R.id.rvNume);
        rvCantitate = view.findViewById(R.id.rvCantitate);
        rvPret = view.findViewById(R.id.rvPret);
        rvImage = view.findViewById(R.id.rvImage);
        rvQT = view.findViewById(R.id.rvQT);
        rvCls = view.findViewById(R.id.rvCls);
        rvPret.setText("Pret: "+model.getPret()+ " lei");
        rvNume.setText(model.getNume());
        rvCantitate.setText("Cantitate: "+model.getCantitate()+" grame");
        rvQT.setText("Numar: x" +String.valueOf(model.getQt()));
        Picasso.get().load(model.getImage()).into(rvImage);
        final int finalPosition = position;
        rvCls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // produse.remove(model);
               // notifyDataSetChanged();
                ((Cos)context).remove(finalPosition);
            }
        });
        return view;
    }
}


