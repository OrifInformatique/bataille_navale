package ch.sectioninformatique.bataille_navale.Models;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Ship {
    // Attributes
    /**
     * That Ship's number of occupied Cases.
     */
    private byte nbCases;
    /**
     * This Ship's Orientation.
     * 'U' for Up.
     * 'D' for Down.
     * 'L' for Left.
     * 'R' for Right.
     */
    private char Orientation;
    /**
     * That Ship's number of times it got a hit.
     */
    private byte nbHit;

    // Constructors
    public Ship(){
        this.nbCases = 0;
        this.Orientation = 'U';
        this.nbHit = 0;
    }

    public Ship(byte cases,char orientation){
        this.nbCases = cases;
        this.Orientation = orientation;
        this.nbHit = 0;
    }

    // Setters
    public void setNbCases(byte setNbCases){
        this.nbCases = setNbCases;
    }

    public void setDefaultOrientation(char setOrientation){
        this.Orientation = setOrientation;
    }

    public void setNbHit(byte setNbHit){
        this.nbHit = setNbHit;
    }

    // Getters
    public byte getNbCases(){
        return this.nbCases;
    }

    public char getDefaultOrientation(){
        return this.Orientation;
    }

    public byte getNbHit(){
        return this.nbHit;
    }

    // Methods

    /**
     * Check if this Ship got enough hits to sink.
     * @return True if the Ship got enough hits to sink, False if not.
     */
    public boolean isSinking(){

        if(this.nbCases == this.nbHit){
            return true;
        }else if(this.nbCases < this.nbHit){
            return true;
        }else {
            return false;
        }
    }

    /**
     * Hit this Ship, increase this Ship's hit counter.
     */
    public void hitShip(){
        this.nbHit++;
    }

}
