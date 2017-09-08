package ch.sectioninformatique.bataille_navale.Models;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Grid {
    // Attributes
    private Case[][] CasesGrid;
    private String playerName;

    // Constructors
    Grid(){
        this.CasesGrid = new Case[10][10];
        this.playerName = "None";
    }

    Grid(String Name){
        this.CasesGrid = new Case[10][10];
        this.playerName = Name;
    }

    // Setters

    public void setPlayerName(String setPlayerName){
        this.playerName = setPlayerName;
    }

    // Getters
    public Case getCase(byte vertical,byte horizontal){
        return this.CasesGrid[vertical][horizontal];
    }

    public String getPlayerName(){
        return this.playerName;
    }

}
