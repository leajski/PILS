import java.sql.Timestamp;

public class Score {

    private int nbDeco = 0;
    private long tempsInit;
    private int nbJoueur;
    private int scoreActuel = 0;

    Score(int n, long t){
        this.nbJoueur = n;
        this.tempsInit = t;
    }

    public void setScoreActuel(int a){
        this.scoreActuel = a;
    }
    
    public void setnbDeco(int a){
        this.nbDeco = a;
    }

    public void setnbJoueur(int a){
        this.nbJoueur = a;
    }

    public int getnbDeco(){
        return this.nbDeco;
    }

    public int getScoreActuel(){
        this.updateScore();
        return this.scoreActuel;
    }

    public long gettempsInit(){
        return this.tempsInit;
    }

    public int getnbJoueur(){
        return this.nbJoueur;
    }

    public void updateScore(){
        Timestamp tempsActuel = new Timestamp(System.currentTimeMillis()/1000);
        double scoreDec =  (10.0*this.nbJoueur*((tempsActuel.getTime()-tempsInit)/60.0))*(1.0-0.2*this.nbDeco);
        this.scoreActuel = (int) Math.floor(scoreDec);
    }
}