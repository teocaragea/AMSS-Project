package com.example.licienta2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;


public class produsAdapter extends FirebaseRecyclerAdapter<Produs, produsAdapter.produsViewholder>{
   Context context;
   final Integer result =1;
    public produsAdapter(Context context,@NonNull FirebaseRecyclerOptions<Produs> options) {
        super(options);
        this.context=context;
    }
    @Override
    protected void onBindViewHolder(@NonNull produsViewholder holder, int position, @NonNull Produs model) {
        holder.rvPret.setText("Pret: "+model.getPret()+ " lei");
        holder.rvNume.setText(model.getNume());
        holder.rvCantitate.setText("Cantitate: "+model.getCantitate()+" grame");
        Picasso.get().load(model.getImage()).into(holder.rvImage);
        try{
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(),ProdusEntity.class);
                    intent.putExtra("image",model.getImage());
                    intent.putExtra("nume",model.getNume());
                    intent.putExtra("cantitate",model.getCantitate());
                    intent.putExtra("pret", model.getPret());
                    intent.putExtra("categorie", model.getCategorie());
                    v.getContext().startActivity(intent);
                    ((Activity)v.getContext()).startActivityForResult(intent,result);
                }
            });
        }catch (Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }

    @NonNull
    @Override
    public produsViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.prod_row,parent,false);
        produsViewholder pp = new produsViewholder(view);
        return pp;
    }

     static public class produsViewholder extends RecyclerView.ViewHolder {
        TextView rvNume, rvPret, rvCantitate, rvQt;
        ImageView rvImage;
        CardView cardView;
        ImageView rvCls;

        public produsViewholder(@NonNull View itemView) {
            super(itemView);
            rvNume = itemView.findViewById(R.id.rvNume);
            rvCantitate = itemView.findViewById(R.id.rvCantitate);
            rvPret = itemView.findViewById(R.id.rvPret);
            rvImage = itemView.findViewById(R.id.rvImage);
            cardView = itemView.findViewById(R.id.cv);
            rvQt = itemView.findViewById(R.id.rvQT);
            rvCls = itemView.findViewById(R.id.rvCls);
            rvCls.setVisibility(View.GONE);
            rvQt.setVisibility(View.GONE);
        }
    }

}
