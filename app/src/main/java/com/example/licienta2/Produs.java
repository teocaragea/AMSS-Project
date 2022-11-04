package com.example.licienta2;

import android.net.Uri;

import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.Objects;

public class Produs implements Serializable {
   private String nume;
   private String pret;
   private String cantitate;
   private String image;
   private Integer qt;
   private String categorie;

    public Produs(){

    }

    public String getCategorie() {
        return categorie;
    }

    public void setCategorie(String categorie) {
        this.categorie = categorie;
    }

    public Produs(String nume, String pret, String cantitate, String Image, String categorie) {
        this.nume = nume;
        this.pret = pret;
        this.cantitate = cantitate;
        this.image = Image;
        this.categorie = categorie;
    }
    public Integer getQt(){return qt;}

    public void setQt(Integer qt){this.qt=qt;}

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public String getCantitate() {
        return cantitate;
    }

    public void setCantitate(String cantitate) {
        this.cantitate = cantitate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int hashCode() {
        return nume.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof Produs) {
            Produs p = (Produs) obj;
            return this.nume.equals(p.nume);
        }
        return false;
    }
}
