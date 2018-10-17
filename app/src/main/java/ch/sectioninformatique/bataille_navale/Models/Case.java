package ch.sectioninformatique.bataille_navale.Models;

import java.io.Serializable;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Case implements Serializable{
    public enum Etat{
        Libre,
        Center,
        Placable,
        NoPlacable,
        Placed,
        Touched;
    }


    // Attributes

    private Etat etat = Etat.Libre;

    /**
     * That Case's Ship, is null if don't contain a Ship.
     */
    private Ship ship;

    // Constructors
    Case(){
        this.ship = null;
    }

    public Case(Ship ship){
        this.ship = ship;
    }
    
    // Setters
    public void setEtat(Etat setEtat){
        this.etat = setEtat;
    }
    
    public void setShip(Ship setShip){
        this.ship = setShip;
    }
    
    // Getters
    public Etat getEtat(){
        return this.etat;
    }
    
    public Ship getShip(){
        return this.ship;
    }

    // Methods

    /**
     * Mark this case.
     * If this Case contain a Ship, then the Ship will be hit
     *
     * @return The Ship who is on the Case, null if don't contain a Ship.
     */
    public Ship touchedCase(){
        this.etat = Etat.Touched;

        if(this.ship != null){
            this.ship.hitShip();
        }

        return this.ship;
    }



    public boolean isShipPlaced(){ return (this.ship != null); }
}
