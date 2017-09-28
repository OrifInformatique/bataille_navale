package ch.sectioninformatique.bataille_navale.Models;

import android.graphics.Color;

/**
 * Created by ToRe on 22.09.2017.
 */

public class ModelsTest {
    private Player P1;
    private Ship S1;

    public ModelsTest(){
        Player P1 = new Player("Player 1", new Color());
        Ship S1 = new Ship((byte)2,'U');

        for(byte i = 0; i < 3; i++){
            System.out.println(P1.getPlayerGrid().getCase(0,0).touchedCase());
            System.out.println(S1.isSunken());
        }

        System.out.println(P1.getPlayerGrid().getCase(1,1).touchedCase());
    }
}
