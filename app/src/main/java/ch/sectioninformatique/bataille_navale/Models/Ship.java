package ch.sectioninformatique.bataille_navale.Models;

import android.util.Log;

import java.io.Serializable;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Ship implements Serializable {
    // Attributes
    private static final int nbMinCases = 2;
    private static final int nbMaxCases = 5;
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
    /**
     * The color the boat has on the grie
     */
    private int color;
    /**
     * The start Coordinates (0-9)
     */
    private int x;
    private int y;

    // Constructors
    public Ship(){
        this.nbCases = 0;
        this.Orientation = 'U';
        this.color = 0;
        this.nbHit = 0;
        this.x = 0;
        this.y = 0;
    }

    public Ship(byte cases,char orientation, int color){
        this.nbCases = cases;
        this.Orientation = orientation;
        this.color = color;
        this.nbHit = 0;
        this.x = 0;
        this.y = 0;
    }

    public Ship(byte cases,char orientation, int color, int x, int y){
        this.nbCases = cases;
        this.Orientation = orientation;
        this.color = color;
        this.nbHit = 0;
        this.x = x;
        this.y = y;
    }

    /**
     * That Ship's maximal number of Cases.
     */
    public static int getNbMinCases() {
        return nbMinCases;
    }

    /**
     * That Ship's minimal number of Cases.
     */
    public static int getNbMaxCases() {
        return nbMaxCases;
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

    public void setColorShip(int color){
        this.color = color;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y){
        this.y = y;
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

    public int getColorShip(){
        return this.color;
    }

    public int getX(){return  this.x;}

    public int getY(){return  this.y;}

    // Methods

    /**
     * Check if this Ship got enough hits to sink.
     * @return True if the Ship got enough hits to sink, False if not.
     */
    public boolean isSinking(){

        if(nbHit == nbCases){
            return true;
        }else if(nbCases < nbHit){
            Log.d("isSinking()!","The number of Hit is above the number of Cases !");
            return false;
        }else {
            return false;
        }
    }

    /**
     * Hit this Ship, increase this Ship's hit counter.
     */
    public void hitShip(){
        if(nbHit < nbCases){
            nbHit++;
        }
    }

}
