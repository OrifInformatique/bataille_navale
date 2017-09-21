package ch.sectioninformatique.bataille_navale.Models;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Case {
    // Attributes
    private boolean touched;
    private Ship ship;

    // Constructors
    public Case(){
        this.touched = false;
        this.ship = null;
    }
    
    // Setters
    public void setTouched(boolean setHited){
        this.touched = setHited;
    }
    
    public void setShip(Ship setShip){
        this.ship = setShip;
    }
    
    // Getters
    public boolean getTouched(){
        return this.touched;
    }
    
    public Ship getShip(){
        return this.ship;
    }

    // Methods
    public Ship touchedCase(){
        this.touched = true;
        return this.ship;
    }
}
