package ch.sectioninformatique.bataille_navale;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import ch.sectioninformatique.bataille_navale.Models.Ship;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class PlacementShipTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("", new Ship((byte)3,'R', R.color.color1));

    }
}
