/**
 * Created by ToRe on 01.09.2017.
 */

public class Grid {
    // Attributes
    public Case[][] nbCases;
    public String playerName;

    // Constructors
    Grid(){
        this.nbCases = new Case[10][10];
        this.playerName = "None";
    }

    Grid(String Name){
        this.nbCases = new Case[10][10];
        this.playerName = Name;
    }
}
