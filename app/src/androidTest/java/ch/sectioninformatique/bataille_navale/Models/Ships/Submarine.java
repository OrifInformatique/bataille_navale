package ch.sectioninformatique.bataille_navale.Models.Ships;

import ch.sectioninformatique.bataille_navale.Models.Ship;

/**
 * Created by ToRe on 07.09.2017.
 */

public class Submarine extends Ship {
    // Attributes
    private byte nbSubmarineCases = 3;

    // Constructors
    Submarine(){
        super();
        this.nbCases = this.nbSubmarineCases;
    }

    Submarine(String orientation){
        this.nbCases = this.nbSubmarineCases;
        this.DefaultOrientation = orientation;
        this.nbHit = 0;
    }
}
