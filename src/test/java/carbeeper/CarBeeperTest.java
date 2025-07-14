package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import static org.junit.jupiter.api.Assertions.*;

class CarBeeperTest extends BaseCarBeeperTest {

    private static final Logger LOGGER = LogManager.getLogger(CarBeeperTest.class);

    private CarBeeper beeper;

    @Mock
    ActionEvent aeMock;

    @BeforeEach
    void setUp()
    {
        MockitoAnnotations.initMocks(this);
        beeper = new CarBeeper();
        beeper.setButtonFunctionalities();
    }

    @Test
    @DisplayName("Default values are set upon creation")
    void testDefaultValuesAreSet()
    {
        beeper.getDoorLockStates().forEach(state -> assertSame(State.UNLOCKED, state, "All doors should be " + State.UNLOCKED.name() + " by default"));
        beeper.getTireStates().forEach(state -> assertSame(State.INFLATED, state, "All tires should be " + State.INFLATED.name() + " by default"));
        beeper.getWindowStates().forEach(state -> assertSame(State.UP, state, "All windows should be " + State.UP.name() + " by default"));
        assertSame(State.OFF, beeper.getPowerState(), "Power should be " + State.OFF.name() + " by default");
        assertSame(State.CLOSED, beeper.getTrunkState(), "Trunk should be " + State.CLOSED.name() + " by default");
        assertSame(State.OFF, beeper.getAlarmState(), "Alarm should be " + State.OFF.name() + " by default");
        assertNotNull(beeper.getThisLayout(), "This layout should be set");
        assertNotNull(beeper.getConstraints(), "Constraints should be set");

    }

    // Test general setup of the CarBeeper
    @Test
    @DisplayName("Images are set")
    void testImagesAreBeingCreated()
    {
        assertNotNull(beeper.getLockButton().getIcon(), "The lock image wasn't set");
        assertNotNull(beeper.getFlatTireButton().getIcon(), "The flat tire image wasn't set");
        assertNotNull(beeper.getWindowButton().getIcon(), "The window image wasn't set");
        assertNotNull(beeper.getPowerButton().getIcon(), "The power image wasn't set");
        assertNotNull(beeper.getTrunkButton().getIcon(), "The trunk image wasn't set");
        assertNotNull(beeper.getAlarmButton().getIcon(), "The alarm image wasn't set");
        assertNull(beeper.getClearButton().getIcon(), "The clear button does not have an icon");
    }

    @Test
    @DisplayName("Functionalities are set")
    void testFunctionalitiesAreSet()
    {
        assertNotNull(beeper.getLockButton().getActionListeners(), "The lock button should have an action attached");
        assertNotNull(beeper.getFlatTireButton().getActionListeners(), "The flat tire button should have an action attached");
        assertNotNull(beeper.getWindowButton().getActionListeners(), "The window button should have an action attached");
        assertNotNull(beeper.getPowerButton().getActionListeners(), "The power button should have an action attached");
        assertNotNull(beeper.getTrunkButton().getActionListeners(), "The trunk button should have an action attached");
        assertNotNull(beeper.getAlarmButton().getActionListeners(), "The alarm button should have an action attached");
        assertNotNull(beeper.getClearButton().getActionListeners(), "The clear button should have an action attached");
    }

    // Test functionalities
    @Test
    @DisplayName("Clicking clear button clears screen")
    void testClearButtonSingleClickClearsScreen()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getClearButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getClearButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getClearButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getClearButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                    } else {
                        LOGGER.warn("There is a listener attached to the clear button that is not a ButtonClicked instance");
                        LOGGER.debug("Listener class: {}", listener.getClass().getName());
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }

        assertEquals("", beeper.getTextArea().getText(), "The text area should be empty after clicking clear button");
        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
    }

    @Test
    @DisplayName("Clicking lock button once locks the driver door")
    void testLockButtonSingleClickLocksDriverDoor()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getLockButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.timerAction(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.LOCKED, beeper.getDriverDoorLockState(), "Driver door should be " + State.LOCKED.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking lock button once unlocks the driver door")
    void testLockButtonSingleClickUnLocksDriverDoor()
    {
        // Initial setup of beeper
        beeper.driverDoorLockState = State.LOCKED;

        // Test clicking the lock button
        MouseEvent enteredEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getLockButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.timerAction(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.UNLOCKED, beeper.getDriverDoorLockState(), "Driver door should be " + State.UNLOCKED.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking lock button two times unlocks the driver door")
    void testLockButtonTwoSingleClicksLocksDriverDoor()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent firstClickEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent secondClickEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getLockButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(firstClickEvent);
                        buttonClicked.timerAction(aeMock);

                        assertSame(State.LOCKED, beeper.getDriverDoorLockState(), "Driver door should be " + State.LOCKED.name() + " after two single clicks");

                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(secondClickEvent);
                        buttonClicked.timerAction(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after two single clicks");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.UNLOCKED, beeper.getDriverDoorLockState(), "Driver door should be " + State.UNLOCKED.name() + " after two single clicks");
                        assertSame(State.UNLOCKED, beeper.getPassengerDoorLockState(), "Passenger door should be " + State.UNLOCKED.name() + " after two single clicks");
                        assertSame(State.UNLOCKED, beeper.getLeftDoorLockState(), "Left door should be " + State.UNLOCKED.name() + " after two single clicks");
                        assertSame(State.UNLOCKED, beeper.getRightDoorLockState(), "Right door should be " + State.UNLOCKED.name() + " after two single clicks");
                        assertEquals(2, beeper.buttonClicks, "We clicks the beeper two times");
                    }
                });


            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking lock button once locks all doors")
    void testLockButtonDoubleClickLocksAllDoors()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 2);
        for (MouseListener listener : beeper.getLockButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.timerAction(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getDoorLockStates().forEach(state -> assertSame(State.LOCKED, state, "All doors should be " + State.LOCKED.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking lock button once unlocks all doors")
    void testLockButtonDoubleClickUnLocksAllDoors()
    {
        // Initial setup of beeper state
        beeper.driverDoorLockState = State.LOCKED;
        beeper.passengerDoorLockState = State.UNLOCKED;
        beeper.leftDoorLockState = State.UNLOCKED;
        beeper.rightDoorLockState = State.UNLOCKED;

        // Test double click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent event = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 2);
        for (MouseListener listener : beeper.getLockButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(event);
                        buttonClicked.timerAction(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getDoorLockStates().forEach(state -> assertSame(State.UNLOCKED, state, "All doors should be " + State.UNLOCKED.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Single clicking flat tire button when all tires are inflated")
    void testFlatTireButtonWhenAllTiresAreInflated()
    {
        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 1);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        beeper.getTireStates().forEach(state -> assertSame(State.INFLATED, state, "All tires should be " + State.INFLATED.name() + " after single click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking the beeper triggers drivers tire to go flat")
    void testClickingTheBeeperTriggersDriverTireToGoFlat()
    {
        // Set up
        beeper.setRandomNumber(5); // Set a fixed random number for predictable behavior
        beeper.buttonClicks = 4; // simulate clicks before this test

        // Click a button... click count goes up
        MouseEvent enteredLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 1);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredLockEvent);
                        buttonClicked.mousePressed(pressedLockEvent);
                        buttonClicked.mouseReleased(releasedLockEvent);
                        buttonClicked.mouseClicked(clickLockEvent);
                        buttonClicked.mouseExited(exitedLockEvent);

                        assertTrue(beeper.isAnyTireFlat(), "Driver tire should be flat");

                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertEquals(State.FLAT, beeper.getDriverTireState(), "Driver tire should be flat");
                        assertEquals(6, beeper.buttonClicks, "We clicked the beeper six times (total of 4 + lock + flat tire click)");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking the beeper triggers passenger tire to go flat")
    void testClickingTheBeeperTriggersPassengerTireToGoFlat()
    {
        // Set up
        beeper.setRandomNumber(35); // Set a fixed random number for predictable behavior
        beeper.buttonClicks = 34; // simulate clicks before this test

        // Click a button... click count goes up
        MouseEvent enteredLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 1);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredLockEvent);
                        buttonClicked.mousePressed(pressedLockEvent);
                        buttonClicked.mouseReleased(releasedLockEvent);
                        buttonClicked.mouseClicked(clickLockEvent);
                        buttonClicked.mouseExited(exitedLockEvent);

                        assertTrue(beeper.isAnyTireFlat(), "Passenger tire should be flat");

                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertEquals(State.FLAT, beeper.getPassengerTireState(), "Passenger tire should be flat");
                        assertEquals(36, beeper.buttonClicks, "We clicked the beeper six times (total of 34 + lock + flat tire click)");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking the beeper triggers left rear tire to go flat")
    void testClickingTheBeeperTriggersLeftRearTireToGoFlat()
    {
        // Set up
        beeper.setRandomNumber(55); // Set a fixed random number for predictable behavior
        beeper.buttonClicks = 54; // simulate clicks before this test

        // Click a button... click count goes up
        MouseEvent enteredLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 1);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredLockEvent);
                        buttonClicked.mousePressed(pressedLockEvent);
                        buttonClicked.mouseReleased(releasedLockEvent);
                        buttonClicked.mouseClicked(clickLockEvent);
                        buttonClicked.mouseExited(exitedLockEvent);

                        assertTrue(beeper.isAnyTireFlat(), "Left rear tire should be flat");

                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertEquals(State.FLAT, beeper.getLeftTireState(), "Left rear tire should be flat");
                        assertEquals(56, beeper.buttonClicks, "We clicked the beeper 56 times (total of 54 + lock + flat tire click)");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking the beeper triggers right rear tire to go flat")
    void testClickingTheBeeperTriggersRightRearTireToGoFlat()
    {
        // Set up
        beeper.setRandomNumber(75); // Set a fixed random number for predictable behavior
        beeper.buttonClicks = 74; // simulate clicks before this test

        // Click a button... click count goes up
        MouseEvent enteredLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedLockEvent = createMouseEvent(beeper.getLockButton(), MouseEvent.MOUSE_EXITED, 1);

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 1);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredLockEvent);
                        buttonClicked.mousePressed(pressedLockEvent);
                        buttonClicked.mouseReleased(releasedLockEvent);
                        buttonClicked.mouseClicked(clickLockEvent);
                        buttonClicked.mouseExited(exitedLockEvent);

                        assertTrue(beeper.isAnyTireFlat(), "Right rear tire should be flat");

                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertEquals(State.FLAT, beeper.getRightTireState(), "Right rear tire should be flat");
                        assertEquals(76, beeper.buttonClicks, "We clicked the beeper 76 times (total of 74 + lock + flat tire click)");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Single clicking flat tire when a tire is flat")
    void testFlatTireButtonWhenATireIsFlat()
    {
        // Set up driver tire to be flat
        beeper.driverTireState = State.FLAT;

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 1);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 1);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 1);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertEquals(State.FLAT, beeper.getDriverTireState());
                        assertEquals(State.INFLATED, beeper.getPassengerTireState());
                        assertEquals(State.INFLATED, beeper.getLeftTireState());
                        assertEquals(State.INFLATED, beeper.getRightTireState());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking flat tire when driver tire is flat")
    void testDoubleClickingFlatTireButtonWhenDriverTireIsFlat()
    {
        // Set up driver tire to be flat
        beeper.driverTireState = State.FLAT;

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 2);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 2);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 2);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after two clicks");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after two clicks");
                        assertEquals(State.INFLATED, beeper.getDriverTireState());
                        assertEquals(State.INFLATED, beeper.getPassengerTireState());
                        assertEquals(State.INFLATED, beeper.getLeftTireState());
                        assertEquals(State.INFLATED, beeper.getRightTireState());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking flat tire when passenger tire is flat")
    void testDoubleClickingFlatTireButtonWhenPassengerTireIsFlat()
    {
        // Set up passenger tire to be flat
        beeper.passengerTireState = State.FLAT;

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 2);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 2);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 2);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after two clicks");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after two clicks");
                        assertEquals(State.INFLATED, beeper.getDriverTireState());
                        assertEquals(State.INFLATED, beeper.getPassengerTireState());
                        assertEquals(State.INFLATED, beeper.getLeftTireState());
                        assertEquals(State.INFLATED, beeper.getRightTireState());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking flat tire when left rear tire is flat")
    void testDoubleClickingFlatTireButtonWhenLeftRearTireIsFlat()
    {
        // Set up left rear tire to be flat
        beeper.leftTireState = State.FLAT;

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 2);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 2);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 2);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after two clicks");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after two clicks");
                        assertEquals(State.INFLATED, beeper.getDriverTireState());
                        assertEquals(State.INFLATED, beeper.getPassengerTireState());
                        assertEquals(State.INFLATED, beeper.getLeftTireState());
                        assertEquals(State.INFLATED, beeper.getRightTireState());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking flat tire when right rear tire is flat")
    void testDoubleClickingFlatTireButtonWhenRightRearTireIsFlat()
    {
        // Set up right rear tire to be flat
        beeper.rightTireState = State.FLAT;

        // Test click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent pressedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_PRESSED, 2);
        MouseEvent releasedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_RELEASED, 2);
        MouseEvent clickEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_CLICKED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getFlatTireButton(), MouseEvent.MOUSE_EXITED, 2);
        for (MouseListener listener : beeper.getFlatTireButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mousePressed(pressedEvent);
                        buttonClicked.mouseReleased(releasedEvent);
                        buttonClicked.mouseClicked(clickEvent);
                        buttonClicked.mouseExited(exitedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after two clicks");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after two clicks");
                        assertEquals(State.INFLATED, beeper.getDriverTireState());
                        assertEquals(State.INFLATED, beeper.getPassengerTireState());
                        assertEquals(State.INFLATED, beeper.getLeftTireState());
                        assertEquals(State.INFLATED, beeper.getRightTireState());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking window button once rolls down the driver window")
    void testWindowButtonSingleClickRollsDownDriverWindow()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.DOWN, beeper.getDriverWindowState(), "Driver window should be " + State.DOWN.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Single click and holding windows button rolls down drivers window incrementally")
    void testWindowButtonSingleClickAndHoldRollsDownDriverWindowIncrementally()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 1);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.DOWN, beeper.getDriverWindowState(), "Driver window should be " + beeper.counter2 + "% " + State.DOWN.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Single clicking and holding windows button after holding continues to roll down drivers window incrementally")
    void testWindowButtonDoubleClickAndWasHeldContinuesToRollDownDriverWindowIncrementally()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.DOWN;
        beeper.wasHeld = true;
        beeper.counter2 = 10;
        beeper.beingHeldTimer = 14;

        // Test single click and hold functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 1);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.DOWN, beeper.getDriverWindowState(), "Driver window should be " + beeper.counter2 + "% " + State.DOWN.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking window button once rolls up the driver window")
    void testWindowButtonSingleClickRollsUpDriverWindow()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.DOWN;

        // Test clicking the window button
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.UP, beeper.getDriverWindowState(), "Driver window should be " + State.UP.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Single clicking windows button and holding rolls up drivers window incrementally")
    void testWindowButtonSingleClickAndHoldRollsUpDriverWindowIncrementally()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.DOWN;

        // Test single click and hold functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 1);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        assertSame(State.UP, beeper.getDriverWindowState(), "Driver window should be " + beeper.counter2 + "% " + State.UP.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Single clicking and holding windows button after holding continues to roll up drivers window incrementally")
    void testWindowButtonDoubleClickAndWasHeldContinuesToRollUpDriverWindowIncrementally()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.UP;
        beeper.wasHeld = true;
        beeper.counter2 = 10;
        beeper.beingHeldTimer = 14;

        // Test single click and hold functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 1);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertTrue(beeper.getSingleClick(), "Single click should be true after one click");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.UP, state, "All windows should be " + beeper.counter2 + "% " + State.UP.name()));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking window button once rolls down all window")
    void testWindowButtonDoubleClickRollsDownAllWindows()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 2);
        MouseEvent clickedEvent1 = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_CLICKED, 2);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent1);
                        //buttonClicked.actionPerformed(aeMock);
                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.DOWN, state, "All windows should be " + State.DOWN.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double click and holding windows button rolls down all windows incrementally")
    void testWindowButtonDoubleClickAndHoldRollsDownAllWindowsIncrementally()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 2);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 2);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.DOWN, state, "All windows should be " + beeper.counter2 + "% " + State.DOWN.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking and holding windows button after holding continues to roll down all windows incrementally")
    void testWindowButtonDoubleClickAndWasHeldContinuesToRollDownAllWindowsIncrementally()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.DOWN;
        beeper.passengerWindowState = State.DOWN;
        beeper.leftWindowState = State.DOWN;
        beeper.rightWindowState = State.DOWN;
        beeper.wasHeld = true;
        beeper.counter2 = 10;
        beeper.beingHeldTimer = 14;

        // Test single click and hold functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 2);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 2);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.DOWN, state, "All windows should be " + beeper.counter2 + "% " + State.DOWN.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking window button once rolls up all windows")
    void testWindowButtonDoubleClickRollsUpAllWindows()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.DOWN;
        beeper.passengerWindowState = State.DOWN;
        beeper.leftWindowState = State.DOWN;
        beeper.rightWindowState = State.DOWN;

        // Test double click functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 2);
        MouseEvent event = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_CLICKED, 2);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(event);
                        //buttonClicked.actionPerformed(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.UP, state, "All windows should be " + State.UP.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking and holding windows button rolls up all windows incrementally")
    void testWindowButtonDoubleClickAndHoldRollsUpAllWindowsIncrementally()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.DOWN;
        beeper.passengerWindowState = State.DOWN;
        beeper.leftWindowState = State.DOWN;
        beeper.rightWindowState = State.DOWN;

        // Test double click and hold functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 2);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 2);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.UP, state, "All windows should be " + beeper.counter2 + "% " + State.UP.name()));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Double clicking and holding windows button after holding continues to roll up all windows incrementally")
    void testWindowButtonDoubleClickAndWasHeldContinuesToRollUpAllWindowsIncrementally()
    {
        // Initial setup of beeper state
        beeper.driverWindowState = State.UP;
        beeper.passengerWindowState = State.UP;
        beeper.leftWindowState = State.UP;
        beeper.rightWindowState = State.UP;
        beeper.wasHeld = true;
        beeper.counter2 = 10;
        beeper.beingHeldTimer = 14;

        // Test single click and hold functionality
        MouseEvent enteredEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_ENTERED, 2);
        MouseEvent exitedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_EXITED, 2);
        MouseEvent clickedEvent = createMouseEvent(beeper.getWindowButton(), MouseEvent.MOUSE_PRESSED, 2);
        for (MouseListener listener : beeper.getWindowButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof WindowButtonHandler buttonClicked) {
                        buttonClicked.mousePressed(enteredEvent);
                        for(int i=0; i<14; i++) { // simulate holding
                            buttonClicked.beingHeldAction(aeMock);
                        }
                        buttonClicked.mouseReleased(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.actionPerformed(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false after one click");
                        assertTrue(beeper.getDoubleClick(), "Double click should be true after one click");
                        beeper.getWindowStates().forEach(state -> assertSame(State.UP, state, "All windows should be " + beeper.counter2 + "% " + State.UP.name() + " after double click"));
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking power button turns on the car")
    void testPowerButtonSingleClickTurnsOnCar()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getPowerButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getPowerButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getPowerButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getPowerButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.timerAction(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.ON, beeper.getPowerState(), "Power state should be " + State.ON.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking power button turns off the car")
    void testPowerButtonSingleClickTurnsOffCar()
    {
        // Initial setup of beeper state
        beeper.powerState = State.ON;

        // Test clicking the power button
        MouseEvent enteredEvent = createMouseEvent(beeper.getPowerButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getPowerButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getPowerButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getPowerButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);
                        buttonClicked.timerAction(aeMock);

                        assertFalse(beeper.getSingleClick(), "Single click should be false");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.OFF, beeper.getPowerState(), "Power state should be " + State.OFF.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking trunk button opens the trunk")
    void testTrunkButtonSingleClickOpensTrunk()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getTrunkButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getTrunkButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getTrunkButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getTrunkButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.OPEN, beeper.getTrunkState(), "Trunk state should be " + State.OPEN.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking trunk button closes the trunk")
    void testTrunkButtonSingleClickClosesTrunk()
    {
        // Initial setup of beeper state
        beeper.trunkState = State.OPEN;

        // Test clicking the trunk button
        MouseEvent enteredEvent = createMouseEvent(beeper.getTrunkButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getTrunkButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getTrunkButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getTrunkButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.CLOSED, beeper.getTrunkState(), "Trunk state should be " + State.CLOSED.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking alarm button turns on the alarm")
    void testAlarmButtonSingleClickTurnsOnAlarm()
    {
        MouseEvent enteredEvent = createMouseEvent(beeper.getAlarmButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getAlarmButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getAlarmButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getAlarmButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.ON, beeper.getAlarmState(), "Alarm state should be " + State.ON.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Clicking alarm button turns off the alarm")
    void testAlarmButtonSingleClickTurnsOffAlarm()
    {
        // Initial setup of beeper state
        beeper.alarmState = State.ON;

        // Test clicking the alarm button
        MouseEvent enteredEvent = createMouseEvent(beeper.getAlarmButton(), MouseEvent.MOUSE_ENTERED, 1);
        MouseEvent exitedEvent = createMouseEvent(beeper.getAlarmButton(), MouseEvent.MOUSE_EXITED, 1);
        MouseEvent clickedEvent = createMouseEvent(beeper.getAlarmButton(), MouseEvent.MOUSE_CLICKED, 1);
        for (MouseListener listener : beeper.getAlarmButton().getMouseListeners()) {
            try {
                javax.swing.SwingUtilities.invokeAndWait(() -> {
                    if (listener instanceof ButtonClicked buttonClicked) {
                        buttonClicked.mouseEntered(enteredEvent);
                        buttonClicked.mouseExited(exitedEvent);
                        buttonClicked.mouseClicked(clickedEvent);

                        assertFalse(beeper.getSingleClick(), "Single click should be false");
                        assertFalse(beeper.getDoubleClick(), "Double click should be false");
                        assertSame(State.OFF, beeper.getAlarmState(), "Alarm state should be " + State.OFF.name());
                        assertEquals(1, beeper.buttonClicks, "We clicked the beeper once");
                    }
                });
            } catch (Exception e) {
                fail("EDT interrupted: " + e.getMessage());
            }
        }
    }

}
