package ch.sectioninformatique.bataille_navale;

import android.util.Log;

import ch.sectioninformatique.bataille_navale.Models.Player;
import ch.sectioninformatique.bataille_navale.Models.Ship;

/**
 * Created by ToRe on 22.09.2017.
 */

public class ModelsTest {
    private Player P1;
    private Ship S1;

    public ModelsTest(){
        int PlayerColor = 0;
        Player P1 = new Player("Player 1", PlayerColor);
        Ship S1 = new Ship((byte)3,'U',0);

//        P1.getPlayerGrid().getCase(0,0).setShip(S1);
//        P1.getPlayerGrid().getCase(1,0).setShip(S1);
//        P1.getPlayerGrid().getCase(2,0).setShip(S1);

        P1.getPlayerGrid().addShip(S1,0,0);

        for(byte i = 0; i < 3; i++){
            P1.getPlayerGrid().getCase(i,0).touchedCase();
            Log.d("Ship sinking",Boolean.toString(S1.isSinking()));
            Log.d("Case state",Boolean.toString(P1.getPlayerGrid().getCase(i,0).getTouched()));
        }

//        Log.d(P1.getPlayerGrid().getCase(1,1).touchedCase());
    }
}
