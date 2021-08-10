package test;
import java.io.BufferedReader;
import java.io.BufferedWriter;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;

public class testthread implements Runnable{

    private Socket s;
    private int partie_ID;
    private volatile boolean running = true;
    private PrintWriter out;
    private BufferedReader in;
    boolean init = false;


    testthread(){

    }

    public void inittestthead(Socket s){
        try{
            this.s = s;
            this.out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this.s.getOutputStream())), true);
            this.in = new BufferedReader(new InputStreamReader(this.s.getInputStream()));
        }catch (Exception e){
            System.out.println(e);
        }

    }

    public void send(String s){
        try{
            this.out.println(s);
            System.out.println("SENT: "+s);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public void run(){
        try {
            Socket so = new Socket("ns3093604.ip-54-36-122.eu",2223); //ns3093604.ip-54-36-122.eu
            inittestthead(so);
            this.init = true;
            while(running){
                String msg_rcv = in.readLine();
                System.out.println("Recu:");
                System.out.println(msg_rcv);
                System.out.println("");
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

}
