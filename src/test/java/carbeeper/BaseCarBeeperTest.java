package carbeeper;

import javax.swing.*;
import java.awt.event.MouseEvent;

abstract class BaseCarBeeperTest {

    /**
     * Creates a mock MouseEvent for testing purposes.
     * @param button the button to be clicked
     * @param mouseEvent the event of the button
     * @param clicks the number of button clicks
     * @return the MouseEvent
     */
    MouseEvent createMouseEvent(JButton button, int mouseEvent, int clicks) {
        return new MouseEvent(button, mouseEvent, System.currentTimeMillis(), 0, 10, 10, clicks, false);
    }
}
