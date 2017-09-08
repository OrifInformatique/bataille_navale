package ch.sectioninformatique.bataille_navale.Models;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Player {
    // Attributes
    private String name;
    private String color;
    private Grid playerGrid;

    // Constructors
    Player(){
        this.name = "None";
        this.color = "None";
        this.playerGrid = new Grid();
    }

    Player(String Name,String Color){
        this.name = Name;
        this.color = Color;
        this.playerGrid = new Grid(this);
    }

    // Setters
    public void setName(String setName){
        this.name = setName;
    }

    public void setColor(String setColor){
        this.color = setColor;
    }

    // Getters
    public String getName(){
        return this.name;
    }

    public String getColor(){
        return this.color;
    }

    public Grid getPlayerGrid(){
        return this.playerGrid;
    }
}