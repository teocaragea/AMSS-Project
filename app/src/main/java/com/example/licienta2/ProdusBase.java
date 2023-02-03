package com.example.licienta2;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ProdusBase {
    private List<Produs> produse = new ArrayList<Produs>();
    private ProdusBase(){

    }
    public List<Produs> getProduse(){
        return produse;
    }
    public void setProduse(List<Produs> produse){
        this.produse = produse;
    }
    public void addProdus(Produs p){
        this.produse.add(p);
    }
    public void removeProdus(int index){
        this.produse.remove(index);
    }
    private static ProdusBase mProdusBase;
    public static ProdusBase get(){
        if(mProdusBase == null){
            mProdusBase = new ProdusBase();
        }
        return mProdusBase;
    }

    @NonNull
    @Override
    public String toString() {
        String mess="";
        for(Produs p : produse){
            mess += p.getNume() + " ";
        }
        return  mess;
    }
}
