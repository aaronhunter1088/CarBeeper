package carbeeper;

/**
 * Enum used to set the state of
 * the properties of the Car Beeper
 */
public enum State {
    // for power and alarm
    ON, OFF,
    // for trunk
    OPEN, CLOSED, 
    // for doors 
    LOCKED, UNLOCKED,
    // for windows
    UP, DOWN,
    // for tires
    FLAT, INFLATED
}
