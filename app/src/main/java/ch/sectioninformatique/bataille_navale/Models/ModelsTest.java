package ch.sectioninformatique.bataille_navale.Models;

import android.graphics.Color;
import android.util.Log;

/**
 * Created by ToRe on 22.09.2017.
 */

public class ModelsTest {
    private Player P1;
    private Ship S1;

    public ModelsTest(){
        int PlayerColor = 0;
        Player P1 = new Player("Player 1", PlayerColor);
        Ship S1 = new Ship((byte)3,'U');

        P1.getPlayerGrid().getCase(0,0).setShip(S1);
        P1.getPlayerGrid().getCase(1,0).setShip(S1);
        P1.getPlayerGrid().getCase(2,0).setShip(S1);

        for(byte i = 0; i < 3; i++){
            P1.getPlayerGrid().getCase(i,0).touchedCase();
            Log.d("Ship sinking",Boolean.toString(S1.isSinking()));
            Log.d("Case state",Boolean.toString(P1.getPlayerGrid().getCase(i,0).getTouched()));
        }

//        Log.d(P1.getPlayerGrid().getCase(1,1).touchedCase());
    }
}
