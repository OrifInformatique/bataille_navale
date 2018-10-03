package ch.sectioninformatique.bataille_navale.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ToRe on 01.09.2017.
 */

public class Player implements Parcelable {
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

    public Player(Parcel in){
        name = in.readString();
        color = in.readInt();
        playerGrid = in.readParcelable(Grid.class.getClassLoader());
    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(name);
        out.writeInt(color);
        out.writeParcelable(playerGrid, flags);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>(){
        public Player createFromParcel(Parcel in){
            return new Player(in);
        }

        public Player[] newArray(int size){
            return new Player[size];
        }
    };

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