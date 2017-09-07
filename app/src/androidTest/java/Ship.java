/**
 * Created by ToRe on 01.09.2017.
 */

public class Ship {
    // Attributes
    private byte nbCases;
    private String DefaultOrientation;
    private byte nbHit;
    // Constructors
    Ship(){
        this.nbCases = 0;
        this.DefaultOrientation = "up";
        this.nbHit = 0;
    }

    Ship(byte cases,String orientation){
        this.nbCases = cases;
        this.DefaultOrientation = orientation;
        this.nbHit = 0;
    }

    // Setters
    public void setNbCases(byte setNbCases){
        this.nbCases = setNbCases;
    }

    public void setDefaultOrientation(String setDefaultOrientation){
        this.DefaultOrientation = setDefaultOrientation;
    }

    public void setNbHit(byte setNbHit){
        this.nbHit = setNbHit;
    }

    // Getters
    public byte getNbCases(){
        return this.nbCases;
    }

    public String getDefaultOrientation(){
        return this.DefaultOrientation;
    }

    public byte getNbHit(){
        return this.nbHit;
    }
}
