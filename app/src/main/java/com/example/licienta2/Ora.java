package com.example.licienta2;

import androidx.annotation.NonNull;

public class Ora {
    private Integer ora;
    private Integer minut;

    public Ora(){

    }
    public Ora(Integer ora, Integer minut){
        this.ora=ora;
        this.minut=minut;
    }

    public Integer getOra() {
        return ora;
    }

    public void setOra(Integer ora) {
        this.ora = ora;
    }

    public Integer getMinut() {
        return minut;
    }

    public void setMinut(Integer minut) {
        this.minut = minut;
    }
    public void add(Ora ora) {
        this.minut += ora.minut;
        if (this.minut > 60) {
            this.ora += 1;
            this.minut = this.minut % 60;
        }
    }
    public void add(Integer minute){
        this.minut += minute;
        if (this.minut > 60) {
            this.ora += 1;
            this.minut = this.minut % 60;
        }
    }
    public boolean isOnSchedule(){
        if(this.ora >= 16 || this.ora <= 9){
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        if(minut<10){ return String.valueOf(ora) + ":0" + String.valueOf(minut);
        }
        return String.valueOf(ora) + ":" + String.valueOf(minut);
    }
}
