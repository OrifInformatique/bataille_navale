package ch.sectioninformatique.bataille_navale.Models;

import java.io.Serializable;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Player implements Serializable {
    // Attributes
    /**
     * That Player's Name.
     */
    private String name;
    /**
     * That Player's Color.
     */
    private int color;
    /**
     * That Player's Grid.
     */
    private Grid playerGrid;

    // Constructors
    public Player(){
        this.name = "None";
        this.color = 0;
        this.playerGrid = new Grid();
    }

    public Player(String Name,int color){
        this.name = Name;
        this.color = color;
        this.playerGrid = new Grid();
    }

    // Setters
    public void setName(String setName){
        this.name = setName;
    }

    public void setColor(int setColor){
        this.color = setColor;
    }

    // Getters
    public String getName(){
        return this.name;
    }

    public int getColor(){
        return this.color;
    }

    public Grid getPlayerGrid(){
        return this.playerGrid;
    }
}