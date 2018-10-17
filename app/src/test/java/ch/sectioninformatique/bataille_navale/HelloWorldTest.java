package ch.sectioninformatique.bataille_navale;

import org.junit.Test;

import ch.sectioninformatique.bataille_navale.Models.Ship;

public class HelloWorldTest {
    @Test
    public void addShip() throws Exception{
        Ship myShip = new Ship((byte) 3, 'U' ,R.color.ship3);
    }
}
