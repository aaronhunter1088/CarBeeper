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
    private ButtonClicked testButton;
    @Mock
    MouseEvent me = mock(MouseEvent.class);

    @Test
    public void testImagesAreBeingCreated()
    {
        assertNotNull("The lock image wasn't set", beeper.lockImage);
    }

    @Test
    public void testClearButtonWasClicked()
    {
        beeper.setClearButton(new JButton("Clear"));
        testButton = new ButtonClicked(beeper);
        when(me.getSource()).thenReturn(beeper.clearButton);
        testButton.mouseClicked(me);

        assertSame("Source should be clear button", me.getSource(), beeper.clearButton);
        assertEquals("Text area should be cleared", "", beeper.textArea.getText());
        assertFalse("WindowsStatesPrinted should be false", beeper.windowStatesPrinted);
    }

    @Test
    public void testPowerButtonWasClicked()
    {
        beeper.setLockButton(new JButton("Lock"));
        beeper.setPowerState(State.OFF);
        testButton = new ButtonClicked(beeper);
        when(me.getSource()).thenReturn(beeper.powerButton);
        testButton.mouseClicked(me);

        assertEquals("The car's power should be ON", State.ON, beeper.getPowerState());
        testButton.mouseClicked(me);
        assertEquals("The car's power should be OFF", State.OFF, beeper.getPowerState());
    }
}
