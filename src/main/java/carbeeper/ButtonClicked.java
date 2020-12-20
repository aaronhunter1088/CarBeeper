package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ButtonClicked extends MouseAdapter
{
    protected final Logger LOGGER = LogManager.getLogger(ButtonClicked.class);
    protected Timer timer;
    protected CarBeeperV2 beeper = null;
    public ButtonClicked(CarBeeperV2 beeper) {
        LOGGER.info("Inside ButtonClicked constructor.");
        this.beeper = beeper;
        timer = new Timer(beeper.TIMER_INTERVAL, evt -> {
            if (beeper.singleClick) {
                LOGGER.info("Single click");
                beeper.lock_Unlock();
                beeper.getLockState();
            } else if (beeper.doubleClick)
            {
                LOGGER.info("Double click");
                beeper.lock_UnlockAll();
                beeper.getLockState();
            }
            beeper.singleClick = false;
            beeper.doubleClick = false;
            LOGGER.info("Timer's event set for " + beeper.TIMER_INTERVAL + " seconds.");
        });
        timer.setRepeats(false);
        timer.setDelay(beeper.TIMER_INTERVAL + beeper.TIMER_INTERVAL);
        timer.start();
        LOGGER.info("timer is running: " + this.timer.isRunning());
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
        LOGGER.info("Inside ButtonClicked.mouseClicked(me="+me.getSource()+").");
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
                beeper.alarmState = State.ON;
                beeper.textArea.append("Alarm is " + beeper.alarmState + "\n");
                beeper.getAlarmState();
            }
            else
            {
                beeper.alarmState = State.OFF;
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
                beeper.textArea.append("Car is " + beeper.powerState + "\n");
                beeper.getPowerState();
            }
            else
            {
                beeper.powerState = State.OFF;
                beeper.textArea.append("Car is " + beeper.powerState + "\n");
                beeper.getPowerState();
            }
        }
        else
        {
            LOGGER.error("Source: " + me.getSource());
        }
        LOGGER.info("End ButtonClicked.mouseClicked().");
    }
}
