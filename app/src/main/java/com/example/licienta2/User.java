package com.example.licienta2;

public class User {
    String name;
    String email;
    String phone;//CNP
    String facultate;
    String nrMat;
    Integer refuzuri;
    Integer nrActive;
    boolean confirmat;
    boolean  Admin;

    public Integer getNrActive() {
        return nrActive;
    }

    public void setNrActive(Integer nrActive) {
        this.nrActive = nrActive;
    }

    public Integer getNrRefuzuri() {
        return refuzuri;
    }

    public void setNrRefuzuri(Integer nrRefuzuri) {
        this.refuzuri = nrRefuzuri;
    }

    public User(){

    }
    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }


    public User(String name, String email, String phone, boolean admin, String nrMat, String facultate, Integer nrRefuzuri,Integer nrActive, boolean confirmat) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.Admin = admin;
        this.facultate = facultate;
        this.nrMat = nrMat;
        this.confirmat = confirmat;
        this.refuzuri = nrRefuzuri;
        this.nrActive = nrActive;
    }

    public boolean isAdmin() {
        return Admin;
    }

    public void setAdmin(boolean admin) {
        Admin = admin;
    }
    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public boolean isConfirmat() {
        return confirmat;
    }

    public void setConfirmat(boolean confirmat) {
        this.confirmat = confirmat;
    }

    public String getFacultate() {
        return facultate;
    }

    public void setFacultate(String facultate) {
        this.facultate = facultate;
    }

    public String getNrMat() {
        return nrMat;
    }

    public void setNrMat(String nrMat) {
        this.nrMat = nrMat;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
