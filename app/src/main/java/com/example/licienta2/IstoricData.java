package com.example.licienta2;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IstoricData {
    String email;// PK user
    String nume;// PK mancare
    String rezolvat;
    String image;
    String pret;
    Boolean finalizat;
    Integer hour;
    Integer minute;
    HashMap<String, Integer> order;

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }

    public String getPret() {
        return pret;
    }

    public void setPret(String pret) {
        this.pret = pret;
    }

    public IstoricData(){

    }

    public HashMap<String, Integer> getOrder() {
        return order;
    }

    public void setOrder(HashMap<String, Integer> order) {
        this.order = order;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getFinalizat() {
        return finalizat;
    }

    public void setFinalizat(Boolean finalizat) {
        this.finalizat = finalizat;
    }


    public IstoricData(String email, String rezolvat, String image, String pret, Boolean finalizat, Integer hour, Integer minute, HashMap<String, Integer> order) {
        this.email = email;
        this.nume = nume;
        this.rezolvat = rezolvat;
        this.image = image;
        this.order = order;
        this.pret = pret;
        this.finalizat = finalizat;
        this.hour = hour;
        this.minute = minute;
    }

    public String getEmail() {
        return email;
    }

    public String getNume() {
        return nume;
    }

    public String getRezolvat() {
        return rezolvat;
    }

    public void setRezolvat(String rezolvat) {
        this.rezolvat = rezolvat;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }


    public String toString(){
        String title = "";
        if(order != null) {
            for (Map.Entry<String, Integer> ord : order.entrySet()) {
                title += ord.getValue() + " x " + ord.getKey() + " ";
            }
        }
        return title;
    }
}
