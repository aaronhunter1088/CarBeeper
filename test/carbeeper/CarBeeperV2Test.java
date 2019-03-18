package carbeeper;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author aaron
 */
public class CarBeeperV2Test {
    static CarBeeperV2 beeper;
    public CarBeeperV2Test() {
    }
    
    @BeforeClass
    public static void setUpClass() throws Exception {
        beeper = new CarBeeperV2();
    }
    
//    /**
//     * Test of setBeeper method, of class CarBeeperV2.
//     */
//    @Test
//    public void testSetBeeper() {
////        beeper.setBeeper();
//        String beeperDefaultText = 
//                "Master Door is UNLOCKED.\n" +
//                "Passenger Door is UNLOCKED.\n" +
//                "Left Door is UNLOCKED.\n" +
//                "Right Door is UNLOCKED.\n" +
//                "Master Window is UP.\n" +
//                "Passenger Window is UP.\n" +
//                "Left Window is UP.\n" +
//                "Right Window is UP.\n" +
//                "Car is OFF.\n" +
//                "Trunk is CLOSED.\n" +
//                "Alarm is OFF."+"\n\n";
//        assertEquals(beeper.getTextArea().getText(), ""); // minus String beeper... should be alone
////        assertEquals(beeper.getTextArea().getText(), beeperDefaultText);
//    }

    /**
     * Test of lock_Unlock method, of class CarBeeperV2.
     */
    @Test
    public void testLockingDoor() {
        beeper.setMasterDoorLockState(State.UNLOCKED);
        beeper.lock_Unlock(); 
        assertEquals(beeper.getMasterDoorLockState(), State.LOCKED); 
    }
    
    @Test
    public void testUnlockingDoor() {
        beeper.setMasterDoorLockState(State.UNLOCKED);
        assertEquals(beeper.getMasterDoorLockState(), State.UNLOCKED); 
    }

    /**
     * Test of lock_UnlockAll method, of class CarBeeperV2.
     */
    @Test
    public void testUnlockAllDoors() {
        // by default, all locks are UNLOCKED
        beeper.setMasterDoorLockState(State.LOCKED);
        beeper.lock_UnlockAll();
        assertEquals(beeper.getMasterDoorLockState(), State.UNLOCKED);
        assertEquals(beeper.getPassengerDoorLockState(), State.UNLOCKED);
        assertEquals(beeper.getLeftDoorLockState(), State.UNLOCKED);
        assertEquals(beeper.getRightDoorLockState(), State.UNLOCKED);
    }
    
    @Test
    public void testLockAllDoors() {
        // by default, all locks are UNLOCKED
        beeper.setMasterDoorLockState(State.UNLOCKED);
        beeper.lock_UnlockAll();
        assertEquals(beeper.getMasterDoorLockState(), State.LOCKED);
        assertEquals(beeper.getPassengerDoorLockState(), State.LOCKED);
        assertEquals(beeper.getLeftDoorLockState(), State.LOCKED);
        assertEquals(beeper.getRightDoorLockState(), State.LOCKED);        
    }

    /**
     * Test of windowUp_Down method, of class CarBeeperV2.
     */
    @Test
    public void testWindowUp_Down() {
        // by default, master window is UP
        beeper.windowUp_Down(); // comment out to test other assertEquals statement
        assertEquals(beeper.getMasterWindowState(), State.DOWN); // comment out if below is uncommented
//        assertEquals(beeper.getMasterWindowState(), State.UP); // comment out if above is uncommented; should be only statement
    }

    /**
     * Test of windowUp_DownAll method, of class CarBeeperV2.
     */
    @Test
    public void testWindowUp_DownAll() {
        // by default, all windows are UP
        beeper.windowUp_DownAll();
        assertEquals(beeper.getMasterWindowState(), State.DOWN);
        assertEquals(beeper.getPassengerWindowState(), State.DOWN);
        assertEquals(beeper.getLeftWindowState(), State.DOWN);
        assertEquals(beeper.getRightWindowState(), State.DOWN);
        beeper.windowUp_DownAll(); // needed because it was actually messing up testWindowUpDown()
    }

    /**
     * Test of getPowerState method, of class CarBeeperV2.
     */
    @Test
    public void testGetPowerState() {
        // by default, power is OFF
        assertEquals(beeper.getPowerState(), State.OFF);
//        assertEquals(beeper.getPowerState(), State.ON);
    }

    /**
     * Test of getTrunkState method, of class CarBeeperV2.
     */
    @Test
    public void testGetTrunkState() {
        assertEquals(beeper.getTrunkState(), State.CLOSED);
//        assertEquals(beeper.getTrunkState(), State.OPEN);
    }

    /**
     * Test of getAlarmState method, of class CarBeeperV2.
     */
    @Test
    public void testGetAlarmState() {
        assertEquals(beeper.getAlarmState(), State.OFF);
//        assertEquals(beeper.getAlarmState(), State.ON);
    }

    /**
     * Test of getWindowStates method, of class CarBeeperV2.
     */
    @Test
    public void testGetWindowStates() {
        // no need to test
    }

    /**
     * Test of getLockState method, of class CarBeeperV2.
     */
    @Test
    public void testGetLockState() {
        // no need to test
    }

    /**
     * Test of getMasterDoorLockState method, of class CarBeeperV2.
     */
    @Test
    public void testGetMasterDoorLockState() {
    }

    /**
     * Test of getPassengerDoorLockState method, of class CarBeeperV2.
     */
    @Test
    public void testGetPassengerDoorLockState() {
    }

    /**
     * Test of getLeftDoorLockState method, of class CarBeeperV2.
     */
    @Test
    public void testGetLeftDoorLockState() {
    }

    /**
     * Test of getRightDoorLockState method, of class CarBeeperV2.
     */
    @Test
    public void testGetRightDoorLockState() {
    }

    /**
     * Test of getMasterWindowState method, of class CarBeeperV2.
     */
    @Test
    public void testGetMasterWindowState() {
    }

    /**
     * Test of getPassengerWindowState method, of class CarBeeperV2.
     */
    @Test
    public void testGetPassengerWindowState() {
    }

    /**
     * Test of getLeftWindowState method, of class CarBeeperV2.
     */
    @Test
    public void testGetLeftWindowState() {
    }

    /**
     * Test of getRightWindowState method, of class CarBeeperV2.
     */
    @Test
    public void testGetRightWindowState() {
    }

    /**
     * Test of getTextArea method, of class CarBeeperV2.
     */
    @Test
    public void testGetTextArea() {
    }
    
}
