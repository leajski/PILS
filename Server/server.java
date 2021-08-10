import java.net.*;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;

public class server{

    public static void main(String[] arg){

        
        int portNumber = 2223;
        System.out.println("Waiting for connections on port :"+portNumber);
        int thread_id = 1; //debug
        ServerSocket server = null;
        try{
            server = new ServerSocket(portNumber);
        }catch (Exception e) {
            System.out.println(e);
        }
        while(true){
            Threadconn tc;
            try{
                tc = new Threadconn(server.accept(),thread_id);
                putlog("connection recieved, socket"+thread_id+" created");
                Thread t = new Thread(tc);
                thread_id ++;
                t.start();
            }catch(Exception e){
                System.out.println("S : "+e);
            }
        }
    }

    /**/

    public static void putlog(String s) {
        try{
            Date today = new Date();
            File log = new File("logs/"+ new SimpleDateFormat("dd-MM-yyyy").format(today) +".txt");
            log.createNewFile();
            FileWriter myWriter = new FileWriter(log,true);
            myWriter.write(new SimpleDateFormat("[HH:mm:ss]").format(today)+"-SERVER -> "+s+"\n");
            myWriter.close();
        }catch(Exception e){
            System.out.println(e);
        }

    }


}
