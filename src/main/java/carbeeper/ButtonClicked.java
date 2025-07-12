package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class ButtonClicked extends MouseAdapter
{
    protected final Logger LOGGER = LogManager.getLogger(ButtonClicked.class);
    protected Timer timer;
    protected boolean timerIsRunning = false;
    protected CarBeeper beeper;
    public ButtonClicked(CarBeeper beeper) {
        LOGGER.info("Inside ButtonClicked constructor.");
        this.beeper = beeper;
        createTimer();
        LOGGER.info("Timer's event set for " + beeper.TIMER_INTERVAL + " seconds.");
    }
    @Override
    public void mouseEntered(MouseEvent me)
    {
        String buttonName = ((JButton)me.getSource()).getText();
        LOGGER.debug("Entered {} Button.", buttonName);
    }
    @Override
    public void mouseExited(MouseEvent me)
    {
        String buttonName = ((JButton)me.getSource()).getText();
        LOGGER.debug("Leaving {} Button.", buttonName);
    }
    // Don't need these methods.
//    @Override
//    public void mousePressed(MouseEvent event) {}
//    @Override
//    public void mouseReleased(MouseEvent me) {}
    @Override
    public void mouseClicked(MouseEvent me)
    {
        LOGGER.info("Inside ButtonClicked.mouseClicked(me={} Button).", ((JButton)me.getSource()).getText());
        if (me.getSource() == beeper.clearButton)
        {
            beeper.textArea.setText("");
            beeper.windowStatesPrinted = false;
            LOGGER.info("Textarea cleared.");
        }
        else if (me.getSource() == beeper.lockButton)
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
            timerIsRunning = true;
            LOGGER.info("Timer for Lock Button started.");
        }
        else if (me.getSource() == beeper.alarmButton)
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
        else if (me.getSource() == beeper.trunkButton)
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
        else if (me.getSource() == beeper.powerButton)
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
        else if (me.getSource() == beeper.flatTireButton) {
            if (me.getClickCount() == 1) {
                if (beeper.driverTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's master tire is flat. Raising hydraulic press" +
                            " for master tire\n");
                    beeper.getDriverTireState();
                }
                else if (beeper.passengerTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's passenger tire is flat. Raising hydraulic press" +
                            " for passenger tire\n");
                    beeper.getPassengerTireState();
                }
                else if (beeper.leftTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's left tire is flat. Raising hydraulic press" +
                            " for left tire\n");
                    beeper.getLeftTireState();
                }
                else if (beeper.rightTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's right tire is flat. Raising hydraulic press" +
                            " for right tire\n");
                    beeper.getRightTireState();
                }
                else
                {
                    beeper.textArea.append("The tires are all " + State.INFLATED + "\n");
                }
            } else {
                if (beeper.isAnyTireFlat()) {
                    if (beeper.driverTireState == State.FLAT)
                    {
                        beeper.textArea.append("The car's master tire is fixed\n");
                        beeper.driverTireState = State.INFLATED;
                        beeper.getDriverTireState();
                    }
                    else if (beeper.passengerTireState == State.FLAT)
                    {
                        beeper.textArea.append("The car's passenger tire is fixed\n");
                        beeper.passengerTireState = State.INFLATED;
                        beeper.getPassengerTireState();
                    }
                    else if (beeper.leftTireState == State.FLAT)
                    {
                        beeper.textArea.append("The car's left tire is fixed\n");
                        beeper.leftTireState = State.INFLATED;
                        beeper.getLeftTireState();
                    }
                    else if (beeper.rightTireState == State.FLAT)
                    {
                        beeper.textArea.append("The car's right tire is fixed\n");
                        beeper.rightTireState = State.INFLATED;
                        beeper.getRightTireState();
                    }
                    beeper.randomNumber = new Random().nextInt(100);
                    beeper.buttonClicks = 0;
                    LOGGER.info("Random Number is now {}", beeper.randomNumber);
                } else {
                    LOGGER.info("No tire is flat");
                }
            }
        }
        beeper.buttonClicks += 1;
        if (beeper.buttonClicks == beeper.randomNumber) {
            if (0 <= beeper.randomNumber && beeper.randomNumber < 25) { beeper.driverTireState = State.FLAT; }
            else if (25 <= beeper.randomNumber && beeper.randomNumber < 50) { beeper.passengerTireState = State.FLAT; }
            else if (50 <= beeper.randomNumber && beeper.randomNumber < 75) { beeper.leftTireState = State.FLAT; }
            else if (75 <= beeper.randomNumber && beeper.randomNumber <= 100) { beeper.rightTireState = State.FLAT; }
        }
        LOGGER.info("total buttons click count: {}", beeper.buttonClicks);
        if (timerIsRunning) {
            this.timer.start();
        }
        LOGGER.info("End ButtonClicked.mouseClicked().");
    }

    public CarBeeper getBeeper() {
        return beeper;
    }
    public void setBeeper(CarBeeper beeper) {
        this.beeper = beeper;
    }
    public Timer getTimer()
    {
        return timer;
    }
    public void setTimer(Timer timer)
    {
        this.timer = timer;
    }
    public boolean isTimerRunning()
    {
        return timerIsRunning;
    }
    public void setTimerIsRunning(boolean timerIsRunning)
    {
        this.timerIsRunning = timerIsRunning;
    }

    /**
     * Creates a timer that will be
     * used to handle the actions
     */
    private void createTimer()
    {
        timer = new Timer(beeper.TIMER_INTERVAL, this::timerAction);
        timer.setRepeats(false);
    }

    /**
     * Handles the timer action logic
     * @param ae the actionEvent
     */
    protected void timerAction(ActionEvent ae)
    {
        if (timerIsRunning) {
            if (beeper.singleClick) {
                LOGGER.info("Single click");
                beeper.lock_Unlock();
            }
            else if (beeper.doubleClick) {
                LOGGER.info("Double click");
                beeper.lock_UnlockAll();
            }
            beeper.printDoorStates();
            //beeper.singleClick = false;
            //beeper.doubleClick = false;
            timerIsRunning = false;
            LOGGER.info("Timer ended for Button.");
        }
        if (!timerIsRunning) {
            timer.stop();
        }
    }
}
