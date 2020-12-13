package carbeeper;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class CarBeeperV2Test {

    CarBeeperV2 beeper = new CarBeeperV2();
    @Test
    public void testImagesAreBeingCreated()
    {
        assertNotNull("The lock image wasn't set", beeper.lockImage);
    }

}
