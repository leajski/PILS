package com.pils.disconnect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class socketHandler implements Runnable {

    private static Socket socket;
    private boolean socketinit = false;
    private PrintWriter out;
    private BufferedReader in;
    private Boolean isInit = false;
    private partieHandler ph;
    private Boolean running = true;
    private Boolean isStarted = false;
    private Boolean isOver = false;
    private Boolean BonnePartie = true;
    private Boolean tentativeJoin = false;

    public Boolean getTentativeJoin(){ return this.tentativeJoin;}

    public void setTentativeJoin(Boolean a){ this.tentativeJoin = a;}

    public Boolean getRunning(){
        return this.running;
    }

    public Boolean getBonnePartie(){ return this.BonnePartie;}

    public void setBonnePartie(Boolean a){this.BonnePartie = a;}

    public Socket getSocket(){
        return this.socket;
    }

    public PrintWriter getOut(){
        return this.out;
    }

    public BufferedReader getIn(){
        return this.in;
    }

    public Boolean getIsInit(){
        return this.isInit;
    }

    public Boolean getIsStarted(){ return this.isStarted;}

    public Boolean getIsOver(){ return this.isOver;}

    public partieHandler getPh(){
        return this.ph;
    }

    public boolean getSocketInit(){
        return this.socketinit;
    }

    public void setRunning(boolean a){
        this.running = a;
    }

    public void setSocket(){
        try{

            this.socket = new Socket("ns3093604.ip-54-36-122.eu",2223); //ns3093604.ip-54-36-122.eu 192.168.0.26
            this.socketinit = true;
        }catch(Exception e) {
            System.out.println(e);
        }
    }

    public void send(String s){
        try{
            System.out.println("SENT: "+s);
            this.out.println(s);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void syncpartieHandler(partieHandler p){
        this.ph = p;
    }

    public void initSocket(){
        try {
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream())), true);
            this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.isInit = true;
        } catch (Exception e) {
            System.out.println(e);
        }

    }

    socketHandler() {
    }

    public void run(){
        try {
            System.out.println("STARTED: "+Thread.currentThread().getId());
            if(this.isInit == false){
                initSocket();
            }
            while(this.running){
                String msg_rcv;
                if(this.in.ready()){
                    msg_rcv = this.in.readLine();
                }else{
                    msg_rcv = "";
                }
                String[] separateur = {"NULL","NULL","NULL"};
                if(msg_rcv.equals("")==false){
                    System.out.println("Recu:");
                    System.out.println(msg_rcv);
                    System.out.println("");
                }
                if(msg_rcv != null){
                    separateur = msg_rcv.split(":");
                }

                if(separateur[0].equals("CONFIRMCREATE")){
                    this.ph.setPartie_ID(Integer.parseInt(separateur[1]));
                    this.ph.setIsHost(true);
                    this.ph.setIsConfirm(true);
                }

                if(separateur[0].equals("ACTUALSCORE")){
                    this.ph.setScore(Integer.parseInt(separateur[1]));
                }

                if(separateur[0].equals("CONFIRMJOIN")){
                    this.tentativeJoin = true;
                    if(separateur[1].equals("NONE")){
                        this.BonnePartie = false;
                    }else {
                        this.ph.setIsJoin(true);
                        String[] param = separateur[1].split("&", 2);
                        String[] pseudos = param[0].split(";");
                        ArrayList<String> s = new ArrayList<String>();
                        for (int i = 0; i < pseudos.length; i++) {
                            s.add(pseudos[i]);
                        }
                        this.ph.setGage(param[1]);
                        this.ph.pseudos_joueurs = s;
                        this.ph.setIsJoin(true);
                    }
                }

                if(separateur[0].equals("UPDATEPLAYER")){
                    if(this.ph.getNbjoueur() <= Integer.parseInt(separateur[1])){
                        this.ph.setNbjoueur(Integer.parseInt(separateur[1]));
                        this.ph.pseudos_joueurs.add(separateur[2]);
                    }else if(this.ph.getNbjoueur() > Integer.parseInt(separateur[1])){
                        this.ph.setNbjoueur(Integer.parseInt(separateur[1]));
                        this.ph.pseudos_joueurs.remove(separateur[2]);
                    }
                }

                if(separateur[0].equals("BEGIN")){
                    this.isStarted = true;
                }

                if(separateur[0].equals("END")){
                    this.ph.setScoreFinal(separateur[1]);
                    this.isOver = true;
                }

            }
            System.out.println("ENDED: "+Thread.currentThread().getId());
            Thread.currentThread().interrupt();
        }catch (Exception e){
            System.out.println("sh err: "+e);
        }
    }
}

