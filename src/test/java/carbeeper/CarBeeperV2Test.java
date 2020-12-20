package carbeeper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import static carbeeper.CarBeeperV2.LOGGER;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class CarBeeperV2Test {

    CarBeeperV2 beeper = new CarBeeperV2();
    private ButtonClicked testButton;
    private String clickType;
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

        assertTrue("Source should be clear button", me.getSource() == beeper.clearButton);
        assertTrue("Text area should be cleared", beeper.textArea.getText().equals(""));
        assertFalse("WindowsStatesPrinted should be false", beeper.windowStatesPrinted);
    }
}
