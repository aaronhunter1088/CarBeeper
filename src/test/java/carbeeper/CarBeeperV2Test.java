package carbeeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import javax.swing.*;
import java.awt.event.MouseEvent;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CarBeeperV2Test {

    CarBeeperV2 beeper = new CarBeeperV2();
    private JButton testButton;
    @Mock
    MouseEvent me = mock(MouseEvent.class);

    @Test
    public void testImagesAreBeingCreated()
    {
        assertNotNull("The lock image wasn't set", beeper.lockImage);
    }


}
