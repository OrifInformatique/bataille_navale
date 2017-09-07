package ch.sectioninformatique.bataille_navale.Models.Ships;

import ch.sectioninformatique.bataille_navale.Models.Ship;

/**
 * Created by ToRe on 07.09.2017.
 */

public class Battleship extends Ship {
    // Attributes
    private byte nbBattleshipCases = 4;

    // Constructors
    Battleship(){
        super();
        this.nbCases = this.nbBattleshipCases;
    }

    Battleship(String orientation){
        this.nbCases = this.nbBattleshipCases;
        this.DefaultOrientation = orientation;
        this.nbHit = 0;
    }
}
