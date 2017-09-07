package ch.sectioninformatique.bataille_navale.Models.Ships;

import ch.sectioninformatique.bataille_navale.Models.Ship;

/**
 * Created by ToRe on 07.09.2017.
 */

public class Destroyer extends Ship {
    // Attributes
    private byte nbDestroyerCases = 2;

    // Constructors
    Destroyer(){
        super();
        this.nbCases = this.nbDestroyerCases;
    }

    Destroyer(String orientation){
        this.nbCases = this.nbDestroyerCases;
        this.DefaultOrientation = orientation;
        this.nbHit = 0;
    }
}
