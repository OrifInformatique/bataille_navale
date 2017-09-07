package ch.sectioninformatique.bataille_navale.Models.Ships;

import ch.sectioninformatique.bataille_navale.Models.Ship;

/**
 * Created by ToRe on 07.09.2017.
 */

public class Cruiser extends Ship {
    // Attributes
    private byte nbCruiserCases = 3;

    // Constructors
    Cruiser(){
        super();
        this.nbCases = this.nbCruiserCases;
    }

    Cruiser(String orientation){
        this.nbCases = this.nbCruiserCases;
        this.DefaultOrientation = orientation;
        this.nbHit = 0;
    }
}
