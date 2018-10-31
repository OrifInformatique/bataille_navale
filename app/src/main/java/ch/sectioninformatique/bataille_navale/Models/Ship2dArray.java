package ch.sectioninformatique.bataille_navale.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ship2dArray implements Parcelable {
    private Ship[][] shipsPlayer;

    public Ship2dArray(Ship[][] array){
        shipsPlayer = array;
    }

    public Ship[][] getArray(){
        return  shipsPlayer;
    }

    public void setArray(Ship[][] shipsPlayer){
        this.shipsPlayer = shipsPlayer;
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags){
        int numOfArrays = shipsPlayer.length;
        out.writeInt(numOfArrays);
        for(int i = 0; i < numOfArrays; i++){
            out.writeTypedArray(shipsPlayer[i], flags);
            //out.writeParcelableArray(shipsPlayer[i],flags);
        }
    }

    private Ship2dArray(Parcel in){
        int numOfArrays = in.readInt();
        shipsPlayer = new Ship[numOfArrays][];
        for(int i = 0;i < numOfArrays; i++){
            shipsPlayer[i] = in.createTypedArray(Ship.CREATOR);
            //shipsPlayer[i] = (Ship[]) in.readParcelableArray(Ship.class.getClassLoader());
        }
    }

    // The four possibilities seems to work without noticeable differences

    public static final Parcelable.Creator<Ship2dArray> CREATOR = new Parcelable.Creator<Ship2dArray>() {
        @Override
        public Ship2dArray createFromParcel(Parcel parcel) {
            return new Ship2dArray(parcel);
        }

        @Override
        public Ship2dArray[] newArray(int size) {
            return new Ship2dArray[size];
        }
    };
}
