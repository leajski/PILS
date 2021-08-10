package com.pils.disconnect;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class partieHandler implements Serializable{

    private int partie_ID;
    private volatile boolean running = true;
    private boolean isConfirm = false;
    private int nbjoueur = 0;
    private boolean isUpdate = false;
    private int score = 0;
    private String scoreFinal = "";

    //private boolean modeBar = false;
    private boolean isHost = false;
    private boolean isJoin = false;
    private String gage = "";
    private String nickname = "";
    List<String> pseudos_joueurs = new ArrayList<String>();

    public String getListedesjouers(){
        String liste = "";
        for(String pseudo : this.pseudos_joueurs) {
            liste += pseudo + "\n";
        }
        return liste;
    }

    public int getScore(){
        return this.score;
    }

    public void setScore(int a){
        this.score = a;
    }

    public String getScoreFinal(){ return this.scoreFinal;}

    public void setScoreFinal(String a){ this.scoreFinal = a;};

    public boolean getIsJoin(){
        return this.isJoin;
    }

    public void setIsJoin(boolean a){
        this.isJoin = a;
    }

    public boolean getIsHost(){
        return this.isHost;
    }

    public void setIsHost(Boolean a){
        this.isHost = a;
    }

    public boolean getIsUpdate(){
        return this.isUpdate;
    }

    public int getPartie_ID(){
        return this.partie_ID;
    }

    public boolean getIsConfirm(){
        return this.isConfirm;
    }

    public int getNbjoueur(){
        return this.nbjoueur;
    }

    public boolean getRunning(){
        return this.running;
    }

    //public boolean getModeBar() {return this.modeBar; }

    public String getGage() {return this.gage;}

    public String getNickname() {return this.nickname;}


    public void setPartie_ID(int a){
        this.partie_ID = a;
    }

    public void setIsUpdate(boolean a){
        this.isUpdate = a;
    }

    public void setRunning(boolean a){
        this.running = a;
    }

    public void setIsConfirm(boolean a){
        this.isConfirm = a;
    }

    public void setNbjoueur(int a){
        this.nbjoueur = a;
    }

    //public void setModeBar(boolean a) {this.modeBar = a; }

    public void setGage(String a) {this.gage = a; }

    public void setNickname(String a) {this.nickname = a; }

    public String toString() {
        return "PH{ partie_ID: " + this.partie_ID + " , running: " + this.running + " , isconfirm: " + this.isConfirm + " , nbjoueur: " + this.nbjoueur /*+ " modeBar: " + this.modeBar */+ " }";
    }

    partieHandler(){
    }

}
