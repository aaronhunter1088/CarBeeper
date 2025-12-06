package carbeeper;
import javax.swing.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Main class to launch the CarBeeper application.
 * It initializes the GUI and sets up the button functionalities.
 *
 * @author michael ball
 * @version since 1.0
 */
public class Main {
    private final static Logger LOGGER = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                LOGGER.info("Inside main");
                CarBeeper carBeeper = new CarBeeper();
                carBeeper.setButtonFunctionalities();
                LOGGER.info("Beeper loaded. Starting GUI");
            }
            catch (Exception e) {
                LOGGER.error("Could not create Car Beeper because {}", e.getMessage());
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread(() -> LOGGER.info("Closing Car Beeper")));
    }
}
/*
 PROGRAMMER: Michael Ball

 DATE: May 18, 2016

 ENVIRONMENT: Mac OS Sequoia 15.4.1

 FILES INCLUDED:
 carbeeper/ButtonClicked.class,
 carbeeper/CarBeeper.class,
 carbeeper/Constants.class,
 carbeeper/Main.class,
 carbeeper/State.class,
 carbeeper/WindowButtonHandler.class,
 images/Alarm.jpg,
 images/flatTire.jpg,
 images/lock.jpg,
 images/power.jpg,
 images/trunk.jpg,
 images/window.jpg,
 log4j2.xml,
 pom.properties,
 META-INF/MANIFEST.MF
 *
 * PURPOSE: To simulate a car beeper with buttons that can take care of more than one action.
 *   For example, I always thought that having a lock and unlock button was unnecessary. Why not
 *   have a single button that can lock and unlock the doors depending on their state? This is
 *   what I attempt to accomplish with this simulator.
 *
 * PRECONDITIONS: From the launch of the application, the following preconditions are as follows:
 *  1. All doors are unlocked
 *  2. All door windows are 100% rolled up
 *  3. Car is off.
 *  4. Alarm is off.
 *  5. Trunk is closed.
 *  6. All tires are inflated.
 *
 * INPUT: User clicks on buttons.
 *
 * OUTPUT: Depending on the button clicks, the following are several outputs:
 *  1. The driver door and/or all other doors are locked or unlocked.
 *  2. The driver window is rolled up or down
 *     depending on the number of clicks.
 *  3. The car may be turned on or off.
 *  4. The car alarm may be turned on or off.
 *  5. The trunk to the car is opened or closed (using hydraulics).
 *  6. Based on a random number a tire becomes flat or inflated.
 *     Each click of a button is a single click count, whether it was a single or
 *     a double click, the click count is incremented by 1.
 *
 * POSTCONDITIONS: The "car" will now be in one of several different states after multiple
 *  clicks using the car beeper.
 *
 * ALGORITHM: The beeper will start up and display a GUI with buttons that have images on them.
 *  The user will click on the buttons to perform actions. The actions will be simple to complex
 *  depending on the button clicked. The buttons will output their actions to the text area,
 *  simulating what would happen if used on a real car.
 *
 * ERRORS: Version 1.0
 *  1) I was having trouble getting the textArea height to the size I wanted it to be.
 *  2) I was having difficulty figuring out how to send output to the textArea.
 *  3) I was having trouble determining how to differentiate which button was clicked.
 *  4) I AM HAVING TROUBLE WITH THE LOCK BUTTON. IT HAS TO BE CLICKED 3 TIMES FOR THE
 *     GETCLICKCOUNT to register as 2.
 *  Version 1.1
 *  1) I was having trouble still figuring out how to determine whether the windows button
 *     was being held down and if it was for just the master window or for all windows.
 *  2) The getClickCount() method is not registering the appropriate clicks properly. The evidence
 *     is when I click the lock/windows button twice. It technically calls the methods for clicking
 *     the button once and then calls the method for clicking it twice.
 *     Version 1.2
 *  1) The double click and hold is printing out what it would for a single click and release.
 *  2) When I click the window button once, and then again but hold it and let it lower incrementally,
 *     and then hold it again, it would continue to lower it. This logic should be reversed to where
 *     the window should raise after being lowered.
 *  3) For some reason when I click the windows button twice and hold, it runs the code that should only
 *     be run when I click the windows button once and hold and then runs the appropriate code.
 *  4) On v1.1 we fixed the above error but introduced logic errors.
 *  5) The error for version 1.2 has been fixed for all but one issue: the double click and hold is not
 *     incrementally increasing the window. It resets the value with each press.
 *  6) The issues on the double click + hold where the states were flipping back and forth for all windows has been solved.
 *     The issue was occurring because it was registering the first click as a single action, thereby flipping
 *     the state of the master window. This was resolved by flipping the state once again immediately before returning
 *     the results. Also, the issue with the windows not keeping count while incrementally increasing was resolved.
 *     This issue was resolved by stopping the auto reset when the window was down. The reset is now only taking place
 *     when a single or double click occurs.
 *     Version 2 (CarBeeper)
 *  1) The main goal of version 2 was to condense the code and simplify the logic. As of 3/5/2018, I was able
 *     to reduce the code by as much as 310 lines. I also added enums to make the code more readable. Finally,
 *     I fixed the windows button handler so that it uses a more unique class to handle the click actions.
 *     As of 3/5/2018, I have tested all of my logic by writing test cases following TDD.
 *  2) As of 7/6/25, CarBeeper is on 2.3.
 *     Some of the changes includes: updates to the pom.xml, the log4j2.xml, some code cleanup, tests were
 *     added, and the code was updated to use Java 21.
 *
 * EXAMPLE: A sample execution in terms of input and corresponding output  (if appropriate)
 *  Click the power button once. The screen will display: "Car is ON". Click it again and it
 *  will display: "Car is OFF".

 * Click the lock button once. The screen will display: "Driver door lock is LOCKED". Click it again
 * and it will display: "Driver door lock is UNLOCKED". Now, click the lock button twice.
 * The screen will display:
 *
 * HISTORY: This is version 1.0 of this program finished on May 31, 2016.
 *          1) The power button, the trunk button, and the alarm button were all implemented and fully functional.
 *          2) The lock button for clicking it once works.
 *          3) The windows button for clicking it once works.
 *          Version 1.1 was finished on July 15, 2016:
 *          1) The second version of the car beeper fixed the issue where the clicks for locking/unlocking the doors only worked
 *             for 3 clicks versus 2 as in the original specs. This same error also applied for the windows button and this
 *             issue was also resolved for the windows button. However, as seen in the errors for version 2, there is still
 *             a slight technical difficulty with distinguishing between one and two clicks.
 *          Version 1.2 was finished on August 4, 2015:
 *          1) I was able to fix the issue on when the lock/windows button was calling the method for clicking the
 *             button once and then calling the method for clicking it twice. It now appropriately calls the correct
 *             method when I click either button once or twice.
 *          Version 1.3 was finished on August 12, 2016
 *          1) The functionality of click and release, click and hold, click twice and release, and
 *             click twice and hold ALL function. There are some logic errors embedded in the programming but other
 *             than them, the clicker is functioning at 99%.
 *          The fourth version had its bugs fixed on August 25, 2016.
 *          As of this day (provided further testing), the beeper is functioning at 100%.
 *
 *          February 15, 2018:
 *          First I was able to eliminate 7 variables: carMode, trunkMode, and alarmMode. I replaced the
 *          getMethod, which for some reason used this variable to 1) check the state and 2) then flip the state.
 *          Then it proceeded to return the state. So I first replaced all these variables with their counterpart
 *          variables: carState, trunkState, and alarmState, all which still existed but wasn't used here. Then
 *          I renamed the method names accordingly.
 *          Second, I wanna say again, that I was switching the mode of the car, trunk or alarm before simply
 *          getting the state.... which is totally wrong. That shouldn't have happened at all. We simply return
 *          the state here. So I solved this issue in a few places.
 *          Third thing I did tonight was to eliminate all comments that were repetitive. Like when I said
 *          carState = State.ON; // car state in now on. The reason I made these States is so whenever someone
 *          read the code, or even myself years from now, it will still be 100% legible, even to someone
 *          inexperienced in coding.
 *
 *          March 18, 2019:
 *          Today, in about 3 hours, I fully implemented LOGGING! I have replaced all System.out statements
 *          with log statements. During so, I cleaned up the code some. I also replaced the window button timer
 *          beingHeld printout to overwrite itself, like a timer should, instead of adding the new time to the
 *          textarea. This makes the simulator easier to read. The logs still print out everything in full.
 *          As of today, I am wrapping this project up in CarBeeper-1.1-jar-with-dependencies.jar
 *
 * 	        December 10, 2020:
 * 	        Just checking on on this project and noticed a couple bugs. The alarm and lock images got mixed up.
 * 	        Eliminated a bit more code. Updated the pom.
 *
 * 	        January 20, 2021:
 * 	        Added a new button, Flat Tire. This button is truly a simulation. Without any actual car inputs,
 * 	        this button truly just emulates what would happen if a tire is flat or not.
 *
 * 	        July 6, 2025:
 * 	        Updated the code to use Java 21. Updated the pom.xml to use the latest dependencies.
 *
 * -        December 5, 2025:
 *          Updated files to include since <version> tags. Updated the pom.xml accordingly.
 */
