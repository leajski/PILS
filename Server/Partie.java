import java.util.*;
import java.sql.Timestamp;

public class Partie {

    int id;
    private int nbjoueur;
    private String password;
    private List<Threadconn> liste_joueurs = new ArrayList<Threadconn>();
    private String alreadypseudos = "";
    private String gage = "";
    private boolean isStarted = false;

    public Partie(int name){
        this.id = name;
        this.nbjoueur = 0;
    }

    public boolean getStarted(){
        return this.isStarted;
    }

    public void setStarted(boolean a){
        this.isStarted = a;
    }

    public String getAlreadyPseudos(){
        return this.alreadypseudos;
    }

    public String getGage(){
        return this.gage;
    }

    public void setGage(String a){
        this.gage = a;
    }

    public String syntClassement(){
        String score = "";
        for (Threadconn th : liste_joueurs){
            score += th.getPseudo()+" -> "+th.getScore().getScoreActuel()+" point(s);";
        }
        return score;
    }

    public int getnbjoueur(){
        return this.nbjoueur;
    }

    public Partie(int name, String password){
        this.id = name;
        this.nbjoueur = 0;
        this.password = password;
    }

    public void initScores(){
        long init = new Timestamp(System.currentTimeMillis()/1000).getTime();
        for(Threadconn joueur : this.liste_joueurs){
            joueur.setScore( new Score(this.nbjoueur,init));
        }
    }

    public void ajoutJoueur(Threadconn t){
        this.alreadypseudos += t.getPseudo()+";";
        this.nbjoueur ++;
        liste_joueurs.add(t);
        sendBroadcast("UPDATEPLAYER:"+this.nbjoueur+":"+t.getPseudo());
    }

    public void suppressionJoueur(Threadconn t){
        this.nbjoueur --;
        liste_joueurs.remove(t);
        sendBroadcast("UPDATEPLAYER:"+this.nbjoueur+":"+t.getPseudo());
    }

    public void sendHost(String m){
        for(Threadconn joueur : this.liste_joueurs){
            if(joueur.getisHost() == true){
                joueur.send(m);
            }
        }
    }

    public void sendBroadcast(String m){
        for(Threadconn joueur : this.liste_joueurs){
            joueur.send(m);
        }
    }

    public String toString(){
        if(this.password == null){
            return "Partie: "+this.id+" crée";
        }else{
            return "Partie: "+this.id+" crée ("+this.password+")";
        }

    }
}
