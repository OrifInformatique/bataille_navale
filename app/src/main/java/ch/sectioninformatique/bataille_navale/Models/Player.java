package ch.sectioninformatique.bataille_navale.Models;

import android.graphics.Color;
/**
 * Created by ToRe on 01.09.2017.
 */

public class Player {
    // Attributes
    /**
     * That Player's Name.
     */
    private String name;
    /**
     * That Player's Color.
     */
    private Color color;
    /**
     * That Player's Grid.
     */
    private Grid playerGrid;

    // Constructors
    public Player(){
        this.name = "None";
        this.color = new Color();
        this.playerGrid = new Grid();
    }

    public Player(String Name,Color Color){
        this.name = Name;
        this.color = Color;
        this.playerGrid = new Grid(this);
    }

    // Setters
    public void setName(String setName){
        this.name = setName;
    }

    public void setColor(Color setColor){
        this.color = setColor;
    }

    // Getters
    public String getName(){
        return this.name;
    }

    public Color getColor(){
        return this.color;
    }

    public Grid getPlayerGrid(){
        return this.playerGrid;
    }
}