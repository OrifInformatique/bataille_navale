package ch.sectioninformatique.bataille_navale.Models;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Grid {
    // Attributes
    private Case[][] CasesGrid;
    private Player player;

    // Constructors
    Grid(){
        this.CasesGrid = new Case[10][10];
        this.player = null;
    }

    Grid(Player setPlayer){
        this.CasesGrid = new Case[10][10];
        this.player = setPlayer;
    }

    // Setters

    public void setPlayer(Player setPlayer){
        this.player = setPlayer;
    }

    // Getters
    public Case getCase(byte vertical,byte horizontal){
        return this.CasesGrid[vertical][horizontal];
    }

    public Player getPlayer(){
        return this.player;
    }

}
