package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.SecureRandom;
import java.util.Random;

public class ButtonClicked extends MouseAdapter
{
    protected final Logger LOGGER = LogManager.getLogger(ButtonClicked.class);
    protected Timer timer;
    protected CarBeeperV2 beeper;
    public ButtonClicked(CarBeeperV2 beeper) {
        LOGGER.info("Inside ButtonClicked constructor.");
        this.beeper = beeper;
        timer = new Timer(beeper.TIMER_INTERVAL, evt -> {
            if (beeper.singleClick) {
                LOGGER.info("Single click");
                beeper.lock_Unlock();
                beeper.printDoorStates();
            } else if (beeper.doubleClick)
            {
                LOGGER.info("Double click");
                beeper.lock_UnlockAll();
                beeper.printDoorStates();
            }
            beeper.singleClick = false;
            beeper.doubleClick = false;
        });
        timer.setRepeats(false);
        timer.setDelay(beeper.TIMER_INTERVAL * 2);
        LOGGER.info("Timer's event set for " + beeper.TIMER_INTERVAL + " seconds.");
        //timer.start();
    }
    @Override
    public void mouseEntered(MouseEvent me) {}
    @Override
    public void mouseExited(MouseEvent me) {}
    @Override
    public void mousePressed(MouseEvent event) {}
    @Override
    public void mouseReleased(MouseEvent me) {}
    @Override
    public void mouseClicked(MouseEvent me)
    {
        LOGGER.info("Inside ButtonClicked.mouseClicked(me="+((JButton)me.getSource()).getText()+" Button).");
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
            LOGGER.info("Timer for Lock Button started.");
            this.timer.start();
        }
        else if (me.getSource() == beeper.alarmButton)
        {
            if (beeper.alarmState.equals(State.OFF))
            {
                beeper.setAlarmState(State.ON);
                beeper.textArea.append("Alarm is " + beeper.alarmState + "\n");
                beeper.getAlarmState();
            }
            else
            {
                beeper.setAlarmState(State.OFF);
                beeper.textArea.append("Alarm is " + beeper.alarmState + "\n");
                beeper.getAlarmState();
            }
        }
        else if (me.getSource() == beeper.trunkButton)
        {
            if (beeper.trunkState.equals(State.CLOSED))
            {
                beeper.trunkState = State.OPEN;
                beeper.textArea.append("Trunk is " + beeper.trunkState + "\n");
                beeper.getTrunkState();
            }
            else
            {
                beeper.trunkState = State.CLOSED;
                beeper.textArea.append("Trunk is " + beeper.trunkState + "\n");
                beeper.getTrunkState();
            }
        }
        else if (me.getSource() == beeper.powerButton)
        {
            if (beeper.powerState.equals(State.OFF))
            {
                beeper.powerState = State.ON;
                beeper.printCarState();
            }
            else
            {
                beeper.powerState = State.OFF;
                beeper.textArea.append("Car is " + beeper.powerState + "\n");
                beeper.getPowerState();
            }
        }
        else if (me.getSource() == beeper.flatTireButton) {
            if (me.getClickCount() == 1) {
                if (beeper.masterTireState == State.FLAT)
                {
                    beeper.textArea.append("The car's master tire is flat. Raising hydraulic press" +
                            " for master tire\n");
                    beeper.getMasterTireState();
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
                    if (beeper.masterTireState == State.FLAT)
                    {
                        beeper.textArea.append("The car's master tire is fixed\n");
                        beeper.masterTireState = State.INFLATED;
                        beeper.getMasterTireState();
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
            if (0 <= beeper.randomNumber && beeper.randomNumber < 25) { beeper.masterTireState = State.FLAT; }
            else if (25 <= beeper.randomNumber && beeper.randomNumber < 50) { beeper.passengerTireState = State.FLAT; }
            else if (50 <= beeper.randomNumber && beeper.randomNumber < 75) { beeper.leftTireState = State.FLAT; }
            else if (75 <= beeper.randomNumber && beeper.randomNumber <= 100) { beeper.rightTireState = State.FLAT; }
        }
        LOGGER.info("number of clicks: " + beeper.buttonClicks);
        LOGGER.info("End ButtonClicked.mouseClicked().");
    }
}
