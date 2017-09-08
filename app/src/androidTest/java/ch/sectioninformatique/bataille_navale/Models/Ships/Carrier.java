package ch.sectioninformatique.bataille_navale.Models.Ships;

import ch.sectioninformatique.bataille_navale.Models.Ship;

/**
 * Created by ToRe on 07.09.2017.
 */

public class Carrier extends Ship {
    // Attributes
    private byte nbCarrierCases = 5;

    // Constructors
    Carrier(){
        super();
        this.nbCases = this.nbCarrierCases;
    }

    Carrier(String orientation){
        this.nbCases = this.nbCarrierCases;
        this.DefaultOrientation = orientation;
        this.nbHit = 0;
    }
}
