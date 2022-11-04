package com.example.licienta2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class orderAdapter extends FirebaseRecyclerAdapter<IstoricData, orderAdapter.orderViewholder>{
    Context context;
    final Integer result =1;
    DatabaseReference mDatabase2;
    public orderAdapter(Context context,@NonNull FirebaseRecyclerOptions<IstoricData> options) {
        super(options);
        this.context=context;
    }
    @Override
    protected void onBindViewHolder(@NonNull orderViewholder holder, int position, @NonNull IstoricData model) {
        holder.tvNume.setText(model.toString());
        holder.tvPret.setText("Pret: " + model.getPret() + " lei");
        holder.tvStatus.setText("Status: " + model.getRezolvat());


        mDatabase2 = FirebaseDatabase.getInstance().getReference().child("Produs");
        mDatabase2.orderByChild("nume").equalTo(model.getImage()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                try {
                    for (DataSnapshot datas : snapshot.getChildren()) {
                        if (datas.getKey().toString().equals("image")) {
                            Picasso.get().load(datas.getValue().toString()).into(holder.ivImg);

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
        try {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),OrderEntity.class);
                    intent.putExtra("image", model.getImage());
                    intent.putExtra("email",model.getEmail());
                    intent.putExtra("hashMap", model.getOrder());
                    intent.putExtra("rezolvat",model.getRezolvat());
                    intent.putExtra("pret", model.getPret());
                    intent.putExtra("hour", model.getHour());
                    intent.putExtra("minute", model.getMinute());
                    intent.putExtra("finalizat", model.getFinalizat());
                    v.getContext().startActivity(intent);
                    ((Activity)v.getContext()).startActivityForResult(intent,result);
                }
            });
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    @NonNull
    @Override
    public orderViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.istoric_row,parent,false);
        orderViewholder pp = new orderViewholder(view);
        return pp;
    }

    static public class orderViewholder extends RecyclerView.ViewHolder {
        TextView tvNume, tvPret, tvStatus;
        ImageView ivImg;

        public orderViewholder(@NonNull View itemView) {
            super(itemView);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvNume = itemView.findViewById(R.id.tvUser);
            ivImg = itemView.findViewById(R.id.ivImg);
            tvPret = itemView.findViewById(R.id.tvQt);
        }
    }

}
