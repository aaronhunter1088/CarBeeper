/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package carbeeper;

/**
 *
 * @author aaron
 */
public enum State {
    // for power and alarm
    ON, OFF,
    // for trunk
    OPEN, CLOSED, 
    // for doors 
    LOCKED, UNLOCKED,
    // for windows
    UP, DOWN;
}
