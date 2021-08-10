import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Timestamp;

public class Threadconn implements Runnable{

    private Socket client;
    private int partie_id;
    private int thread_id;
    public static int compteurpartie = 1;
    public static List<Partie> liste_partie = new ArrayList<Partie>();
    private volatile boolean running = true;
    private boolean isHost = false;
    private BufferedReader in;
    private PrintWriter out;
    private String pseudo;
    private Score scoreJoueur;
    

    Threadconn(Socket client, int thread_id){
        this.client = client;
        this.thread_id = thread_id;
    }

    public String alreadyinPartie(int index){
        for (Partie partiea : liste_partie){
            if(partiea.id == index){
                return partiea.getAlreadyPseudos();
            }
        }
        return "";
    }

    public String partieGage(int index){
        for(Partie partiea : liste_partie){
            if(partiea.id == index){
                return partiea.getGage();
            }
        }
        return "";
    }

    public String getClassement(int index){
        String s = "Pas de classement";
        for (Partie partie : liste_partie){
            if(partie.id == index){
                s = partie.syntClassement();
                
            }
        }
        return(s);
    }

    public void setScore(Score s){
        this.scoreJoueur = s;
    }

    public Score getScore(){
        return this.scoreJoueur;
    }

    //renvoie true si la connexion est celle de l'hote
    public boolean getisHost(){
        return this.isHost;
    }

    public String getPseudo(){
        return this.pseudo;
    }

    //ajout d'une nouvelle partie
    public void addPartie(int ID){
        Partie partie = new Partie(ID);
        //on ajoute l'host comme joueur
        partie.ajoutJoueur(this);
        liste_partie.add(partie);
    }

    //ajout d'un nouveau joueur
    public void addPlayer(int ID){
        for (Partie partie : liste_partie){
          if (partie.id == ID) {
              partie.ajoutJoueur(this);
          }
        }
    }

    public boolean checkStart(int ID){
        for(Partie partie : liste_partie){
            if(partie.id == ID){
                if( partie.getStarted() == true){
                    return true;
                }else{
                    return false;
                }
            }
        }
        return false;
    }

    //retrait d'un joueur
    public void removePlayer(int ID){
        for (Partie partie : liste_partie){
          if (partie.id == ID) {
              partie.suppressionJoueur(this);
          }
        }
    }

    //fin de partie
    public void stop(){
        this.running = false;
    }

    public void putlog(String s) {
        try{
            Date today = new Date();
            File log = new File("logs/"+ new SimpleDateFormat("dd-MM-yyyy").format(today) +".txt");
            log.createNewFile();
            FileWriter myWriter = new FileWriter(log,true);
            myWriter.write(new SimpleDateFormat("[HH:mm:ss.SSS]").format(today)+"-socket"+this.thread_id+" -> "+s+"\n");
            myWriter.close();
        }catch(Exception e){
            System.out.println(this.thread_id+" : "+e);
        }

    }

    public boolean checkExist(int ID){
        for(Partie partie : liste_partie){
            if(partie.id == ID){
                return true;
            }
        }
        return false;
    }

    public void send(String s){
        this.out.println(s);
        putlog("envoi \""+s+"\"");
    }

    public void run(){
        String msg_rcv;
        try{
        this.in = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
        this.out = new PrintWriter(this.client.getOutputStream(), true);
        } catch (IOException e) {
            putlog("in or out failed");
            System.exit(-1);
        }

        while(this.running){
            try{
                String[] separateur = {"NULL","NULL","NULL"};
                msg_rcv = in.readLine();
                putlog("recu \""+msg_rcv+"\"");
                if(msg_rcv != null){
                    separateur = msg_rcv.split(":");
                }
                if(msg_rcv == null){

                    putlog("end of connection");
                    this.client.close();
                    this.running = false;

                } else if(separateur[0].equals("CREATE")){
                    
                    String[] param = separateur[1].split("&",2);
                    this.pseudo = param[0];
                    String gage = param[1];
                    //ajout d'une nouvelle partie
                    addPartie(Threadconn.compteurpartie);
                    this.partie_id = Threadconn.compteurpartie;
                    //la connexion est celle de l'hote puisque
                    //il a cree la partie
                    for(Partie partie : liste_partie){
                        if(this.partie_id == partie.id){
                            partie.setGage(gage);
                        }
                    }
                    this.isHost = true;
                    Threadconn.compteurpartie++;
                    //on envoie au telephone que la partie a ete cree
                    this.send("CONFIRMCREATE:"+this.partie_id);


                } else if(separateur[0].equals("JOIN")){

                    this.partie_id = Integer.parseInt(separateur[1]);
                    this.pseudo = separateur[2];
                    if(checkStart(this.partie_id) == false && checkExist(this.partie_id) == true){
                        //on met a jour le nombre de joueur de la partie
                        addPlayer(this.partie_id);
                        //on envoie au telephone qu'un nouveau joueur est arrive
                        this.send("CONFIRMJOIN:"+this.alreadyinPartie(this.partie_id)+"&"+this.partieGage(this.partie_id));
                    }else{
                        this.send("CONFIRMJOIN:NONE");
                    }

                } else if(separateur[0].equals("START")){

                    for (Partie partie : liste_partie){
                      if (this.partie_id == partie.id) {
                          partie.initScores();
                          partie.setStarted(true);
                          partie.sendBroadcast("BEGIN:");
                      }
                    }

                } else if(separateur[0].equals("GETSCORE")){

                    this.send("ACTUALSCORE:"+this.getScore().getScoreActuel());

                } else if(separateur[0].equals("DECO")){

                    this.scoreJoueur.setnbDeco(this.scoreJoueur.getnbDeco()+1);

                } else if(separateur[0].equals("STOP")){

                    //si c'est l'host qui quitte
                    if (getisHost() == true) {
                        //on arrete la partie
                        stop();
                        for (Partie partie : liste_partie){
                          if (this.partie_id == partie.id) {
                              partie.sendBroadcast("END:"+getClassement(this.partie_id));
                          }
                        }
                    //si c'est un joueur qui quitte la partie
                    } else {
                        //on met a jour le nombre de joueurs
                        this.send("END:Vous avez quitté la partie");
                        removePlayer(this.partie_id);
                    } 
                } else {
                    putlog("le message reçu est inconnu");

                }
            } catch (SocketException e){
                //System.out.println("Th-s : "+e);
                this.running = false;
                try{
                  this.client.close();
                } catch (Exception er) {
                  System.out.println("Th-s : "+er);
                }
            } catch (Exception e) {
                System.out.println("Th : "+e);
            }
        }
        Thread.currentThread().interrupt();
    }

    /*private static String genere_code(){
        //retourne un code a 4 chiffres de type String généré aléatoirement.
        String nombreAleatoire = String.valueOf((int)(Math.random() * (10000)));
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 4 - nombreAleatoire.length()){
            sb.append('0');
        }
        sb.append(nombreAleatoire);
        return sb.toString();
    }*/
}
