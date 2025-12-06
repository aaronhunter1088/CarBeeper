package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.lang.Thread.sleep;

/**
 * ButtonClicked class handles mouse events
 * for buttons in the CarBeeper application.
 * It extends MouseAdapter to provide custom
 * behavior for mouse events.
 * Inside mouseEntered
 * Inside mousePressed...
 * Inside mouseReleased...
 * Inside mouseClicked...
 * Inside actionPerformed (for lock button only)
 * Inside mouseExited...
 *
 * @author michael ball
 * @version since 2.0
 */
public class ButtonClicked extends MouseAdapter
{
    protected final Logger LOGGER = LogManager.getLogger(ButtonClicked.class);
    protected Timer timer;
    private boolean timerIsRunning;
    protected CarBeeper beeper;
    private JButton button;
    private String buttonName;

    /**
     * Constructor for ButtonClicked
     * @param beeper the CarBeeper instance
     */
    public ButtonClicked(CarBeeper beeper)
    {
        LOGGER.info("Inside ButtonClicked constructor.");
        this.beeper = beeper;
        createTimer();
    }

    /**
     * Sets the button for this ButtonClicked instance.
     * @param button the JButton to set
     * @return this ButtonClicked instance for method chaining
     */
    public ButtonClicked withButton(JButton button)
    {
        this.button = button;
        buttonName = this.button.getText();
        LOGGER.debug("Created ButtonClicked for {} Button.", buttonName);
        return this;
    }

    @Override
    public void mouseEntered(MouseEvent me)
    {
        LOGGER.debug("Entered {} Button.", buttonName);
    }
    @Override
    public void mouseExited(MouseEvent me)
    {
        LOGGER.debug("Leaving {} Button.", buttonName);
    }
    @Override
    public void mousePressed(MouseEvent event)
    {
        LOGGER.info("Inside {} mousePressed.", buttonName);
    }
    @Override
    public void mouseReleased(MouseEvent me)
    {
        LOGGER.info("Inside {} mouseReleased.", buttonName);
    }
    @Override
    public void mouseClicked(MouseEvent me)
    {
        LOGGER.info("Inside {} mouseClicked.", buttonName);
        if (button == beeper.clearButton)
        {
            beeper.textArea.setText("");
            //beeper.windowStatesPrinted = false;
            LOGGER.info("Textarea cleared.");
        }
        else if (button == beeper.lockButton)
        {
            if (me.getClickCount() == 2)
            {
                beeper.singleClick = false;
                beeper.doubleClick = true;
            }
            else
            {
                beeper.singleClick = true;
                beeper.doubleClick = false;
            }
            setTimerIsRunning(true);
            LOGGER.info("Timer for Lock Button started.");
        }
        else if (button == beeper.alarmButton)
        {
            if (beeper.alarmState.equals(State.OFF))
            {
                beeper.setAlarmState(State.ON);
            }
            else
            {
                beeper.setAlarmState(State.OFF);
            }
            beeper.textArea.append("Alarm is " + beeper.alarmState + "\n");
            beeper.getAlarmState();
        }
        else if (button == beeper.trunkButton)
        {
            if (beeper.trunkState.equals(State.CLOSED))
            {
                beeper.trunkState = State.OPEN;
            }
            else
            {
                beeper.trunkState = State.CLOSED;
            }
            beeper.textArea.append("Trunk is " + beeper.trunkState + "\n");
            beeper.getTrunkState();
        }
        else if (button == beeper.powerButton)
        {
            if (beeper.powerState.equals(State.OFF))
            {
                beeper.powerState = State.ON;
            }
            else
            {
                beeper.powerState = State.OFF;
            }
            beeper.printCarState();
        }
        else if (button == beeper.flatTireButton)
        {
            if (me.getClickCount() == 1) {
                beeper.setSingleClick(true);
                beeper.setDoubleClick(false);
            }
            else {
                beeper.setSingleClick(false);
                beeper.setDoubleClick(true);
            }
            // TODO: Fix. Not as smooth as it could be
            if (beeper.isAnyTireFlat()) {
                if (beeper.driverTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's driver tire is flat. Raising hydraulic press" +
                            " for master tire\n");
                    beeper.getDriverTireState();
                    try { sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    beeper.textArea.append("The car's driver tire is fixed\n");
                    beeper.driverTireState = State.INFLATED;
                    beeper.getDriverTireState();
                }
                else if (beeper.passengerTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's passenger tire is flat. Raising hydraulic press" +
                            " for passenger tire\n");
                    beeper.getPassengerTireState();
                    try { sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    beeper.textArea.append("The car's passenger tire is fixed\n");
                    beeper.passengerTireState = State.INFLATED;
                    beeper.getPassengerTireState();
                }
                else if (beeper.leftTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's left tire is flat. Raising hydraulic press" +
                            " for left tire\n");
                    beeper.getLeftTireState();
                    try { sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    beeper.textArea.append("The car's left tire is fixed\n");
                    beeper.leftTireState = State.INFLATED;
                    beeper.getLeftTireState();
                }
                else // if (beeper.rightTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's right tire is flat. Raising hydraulic press" +
                            " for right tire\n");
                    beeper.getRightTireState();
                    try { sleep(3000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
                    beeper.textArea.append("The car's right tire is fixed\n");
                    beeper.rightTireState = State.INFLATED;
                    beeper.getRightTireState();
                }
                beeper.setFlatTireRandomizer();
            }
            else {
                LOGGER.info("No tire is flat");
            }
        }
        beeper.buttonClicks += 1;
        setFlatTire();
        LOGGER.info("total buttons click count: {}", beeper.buttonClicks);
        if (isTimerRunning())
        { this.timer.start(); }
        LOGGER.info("End {} mouseClicked.", buttonName);
    }

    /**
     * Returns the Timer instance
     * @return the Timer instance
     */
    public Timer getTimer()
    { return timer; }
    /**
     * Sets the Timer instance
     * @param timer the Timer instance to set
     */
    public void setTimer(Timer timer)
    { this.timer = timer; }

    /**
     * Returns whether the timer is running
     * @return true if the timer is running, false otherwise
     */
    public boolean isTimerRunning()
    { return timerIsRunning; }
    /**
     * Sets whether the timer is running
     * @param timerIsRunning true if the timer should be running, false otherwise
     */
    public void setTimerIsRunning(boolean timerIsRunning)
    { this.timerIsRunning = timerIsRunning; }

    /**
     * Creates a timer that will be
     * used to handle the actions
     */
    private void createTimer()
    {
        timer = new Timer(beeper.TIMER_INTERVAL, this::timerAction);
        timer.setRepeats(false);
        LOGGER.debug("Timer's event set for " + beeper.TIMER_INTERVAL + " seconds.");
    }

    /**
     * Handles the timer action logic
     * @param ae the actionEvent
     */
    protected void timerAction(ActionEvent ae)
    {
        if (isTimerRunning()) {
            if (beeper.singleClick) {
                LOGGER.info("Single click");
                beeper.lock_Unlock();
            }
            else if (beeper.doubleClick) {
                LOGGER.info("Double click");
                beeper.lock_UnlockAll();
            }
            beeper.printDoorStates();
            setTimerIsRunning(false);
            LOGGER.info("Timer ended for {} button.", buttonName);
        }
        if (!isTimerRunning()) {
            timer.stop();
        }
    }

    /**
     * Sets a flat tire based on the random number generated
     * and the number of button clicks.
     * If the button clicks match the random number, it sets
     * one of the tires to flat based on the range of the random number.
     */
    private void setFlatTire()
    {
        if (beeper.triggerFlatTire()) {
            if (0 <= beeper.randomNumber && beeper.randomNumber < 25) { beeper.driverTireState = State.FLAT; }
            else if (25 <= beeper.randomNumber && beeper.randomNumber < 50) { beeper.passengerTireState = State.FLAT; }
            else if (50 <= beeper.randomNumber && beeper.randomNumber < 75) { beeper.leftTireState = State.FLAT; }
            else if (75 <= beeper.randomNumber && beeper.randomNumber <= 100) { beeper.rightTireState = State.FLAT; }
            beeper.printFlatTireState();
        }
    }
}
