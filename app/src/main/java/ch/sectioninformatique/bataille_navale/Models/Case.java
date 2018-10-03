package ch.sectioninformatique.bataille_navale.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Case implements Parcelable {
    // Attributes
    /**
     * Describe if that Case has been already touched.
     */
    private boolean touched;
    /**
     * That Case's Ship, is null if don't contain a Ship.
     */
    private Ship ship;

    // Constructors
    Case(){
        this.touched = false;
        this.ship = null;
    }

    public Case(Ship ship){
        this.touched = false;
        this.ship = ship;
    }

    private Case(Parcel in){

    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){

    }

    public static final Parcelable.Creator<Case> CREATOR = new Parcelable.Creator<Case>(){
        public Case createFromParcel(Parcel in){
            return new Case(in);
        }

        public Case[] newArray(int size){
            return new Case[size];
        }
    };

    // Setters
    public void setTouched(boolean setTouched){
        this.touched = setTouched;
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

    /**
     * Mark this case.
     * If this Case contain a Ship, then the Ship will be hit
     *
     * @return The Ship who is on the Case, null if don't contain a Ship.
     */
    public Ship touchedCase(){
        this.touched = true;

        if(this.ship != null){
            this.ship.hitShip();
        }

        return this.ship;
    }



    public boolean isShipPlaced(){ return (this.ship != null); }
}
