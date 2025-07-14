package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The WindowButtonHandler class takes care of any and all mouse clicks
 * and/or holding that may occur for the window button.
 * Inside mouseEntered
 * Inside mousePressed...
 * Inside mouseReleased...
 * Inside mouseClicked...
 * Inside actionPerformed...
 * Inside mouseExited...
 */
public class WindowButtonHandler extends MouseAdapter implements ActionListener
{
    protected final Logger LOGGER = LogManager.getLogger(WindowButtonHandler.class);
    protected final Timer timer;
    protected final Timer beingHeld;
    protected int clicks = 0;
    private String buttonName;
    private final CarBeeper carBeeper;

    /**
     * Constructor for the WindowButtonHandler class.
     * @param carBeeper the CarBeeper instance that this handler will control.
     */
    public WindowButtonHandler(CarBeeper carBeeper)
    {
        LOGGER.debug("Initializing WindowButtonHandler");
        this.carBeeper = carBeeper;
        this.carBeeper.setSingleClick(false);
        this.carBeeper.setDoubleClick(false);
        timer = new Timer(carBeeper.TIMER_INTERVAL, this);
        beingHeld = new Timer(250, this::beingHeldAction);
        this.buttonName = carBeeper.getWindowButton().getName();
        LOGGER.info("End WindowButtonHandler constructor");
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
    public void mousePressed(MouseEvent me)
    {
        LOGGER.info("Inside {} mousePressed.", buttonName);
        if (me.getClickCount() == 1) {
            LOGGER.info("Start beingHeld timer for singleClick.");
            beingHeld.start();
            carBeeper.setSingleClick(true);
            carBeeper.setDoubleClick(false);
            clicks = 1;
        }
        else {
            LOGGER.info("Start beingHeld timer for doubleClick.");
            beingHeld.start();
            carBeeper.setSingleClick(false);
            carBeeper.setDoubleClick(true);
            clicks = 2;
        }
    }
    @Override
    public void mouseReleased(MouseEvent e)
    {
        LOGGER.info("Inside {} mouseReleased.", buttonName);
        beingHeld.stop();
        LOGGER.info("Stopping beingHeld timer");
    }
    @Override
    public void mouseClicked(MouseEvent e)
    {
        LOGGER.info("Inside {} mouseClicked.", buttonName);
        if (carBeeper.getDoubleClick())
        {
            LOGGER.info("Double clicked");
            LOGGER.info("holding = {}, doubleClick = {}, clicks = {}", carBeeper.holding, carBeeper.doubleClick, clicks);
            if (carBeeper.holding)
            {
                if (carBeeper.wasHeld)
                {
                    LOGGER.info("We double clicked and held last time...");
                    carBeeper.getWindowStates(2, carBeeper.counter2);
                    carBeeper.holding = false;
                }
                else
                {
                    LOGGER.info("We double clicked but we didn't hold it this time...");
                    carBeeper.windowUp_DownAll();
                    carBeeper.getWindowStates(2, carBeeper.counter2);
                    // set wasHeld to true and holding to false
                    carBeeper.wasHeld = true;
                    carBeeper.holding = false;
                }
            }
            else
            {
                if (carBeeper.wasHeld)
                { // held on last round, clicked this round (reversing states and button logic)
                    LOGGER.info("We double clicked and held last time...");
                    carBeeper.getWindowStates(2, 0);

                    carBeeper.beingHeldTimer = 0;
                    carBeeper.wasHeld = false;
                    carBeeper.windowStatesPrinted = false;
                    // if not holding (clicked) but wasHeld previously (true) (and previous click was a singleClick (true))
                }
                else
                { // not holding this round (click) and didn't hold last round (click)
                    LOGGER.info("We double clicked but didn't hold this time.");
                    carBeeper.windowUp_DownAll();
                    carBeeper.getWindowStates(2, carBeeper.counter2);
                    carBeeper.holding = false;
                    carBeeper.windowStatesPrinted = false;
                }
                carBeeper.counter2 = 0;
            }
            timer.stop();
        }
        else
        {
            LOGGER.info("Single click.");
            timer.restart();
        }
        carBeeper.buttonClicks += 1;
        setFlatTire();
        LOGGER.info("total buttons click count: {}", carBeeper.buttonClicks);

        LOGGER.info("End {} mouseClicked.", buttonName);
    }
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        LOGGER.info("Running {} actionPerformed...", buttonName);
        LOGGER.info("Timer for Window Button stopped.");
        if (timer.isRunning()) {
            timer.stop();
        }
        LOGGER.info("holding = {} | singleClick = {}",carBeeper.holding , carBeeper.singleClick);
        if (carBeeper.holding && carBeeper.singleClick)
        {
            if (carBeeper.wasHeld)
            {
                LOGGER.info("We single click and held last time.");
                carBeeper.getWindowStates(1, carBeeper.counter2);
                carBeeper.holding = false;
                carBeeper.windowStatesPrinted = false;
            }
            else
            {
                LOGGER.info("We single clicked and held it this time.");
                carBeeper.windowUp_Down();
                carBeeper.getWindowStates(1, carBeeper.counter2);
                // set wasHeld to true and holding to false
                carBeeper.wasHeld = true;
                carBeeper.holding = false;
            }
        }
        else
        {
            if (carBeeper.wasHeld)
            { // held on last round, clicked this round (reversing states and button logic)
                LOGGER.info("We double clicked and held last time.");
                //windowUp_Down();
                carBeeper.getWindowStates(1, 0);
                carBeeper.wasHeld = false;
                // if not holding (clicked) but wasHeld previously (true) (and previous click was a doubleClick (true))
            }
            else
            { // not holding this round (click) and didn't hold last round (click)
                LOGGER.info("We double clicked and didn't hold it this time.");
                carBeeper.windowUp_Down();
                carBeeper.getWindowStates(1, carBeeper.counter2);
                carBeeper.holding = false;
            }
            carBeeper.beingHeldTimer = 0;
            carBeeper.counter2 = 0;
            carBeeper.windowStatesPrinted = false;
        }
        LOGGER.info("actionPerformed finished.");
    }
    public void beingHeldAction(ActionEvent ae)
    {
        LOGGER.info("Inside beingHeld Timer actionEvent.");
        LOGGER.info("windowStatesPrinted {}", carBeeper.getWindowStatesPrinted());
        if ((carBeeper.beingHeldTimer < 3 && !carBeeper.holding) )
        { // 0, 1, 2 < 3
            carBeeper.textArea.append(++carBeeper.beingHeldTimer + " Window button held: no\n");
            LOGGER.info("{} Timer is not being held.", carBeeper.beingHeldTimer);
        }
        else if (carBeeper.beingHeldTimer == 3)
        {
            carBeeper.textArea.append(++carBeeper.counter2 + " Window button held: yes\n");
            LOGGER.info(carBeeper.counter2 + " Timer is being held.");
            carBeeper.setHolding(true);
            carBeeper.beingHeldTimer++;
        }
        else if (carBeeper.beingHeldTimer > 3 && !carBeeper.windowStatesPrinted)
        {
            if (carBeeper.counter2 < 10)
                carBeeper.textArea.setText(carBeeper.textArea.getText().substring(0, carBeeper.textArea.getText().length()-26) + ++carBeeper.counter2 + " Window button held: yes\n");
            else if (carBeeper.counter2 <= 100) {
                carBeeper.textArea.setText(carBeeper.textArea.getText().substring(0, carBeeper.textArea.getText().length()-27) + ++carBeeper.counter2 + " Window button held: yes\n");
            }
            else {
                LOGGER.error("counter2: {} shouldn't go any higher than 100.", carBeeper.counter2);
            }
            LOGGER.info(carBeeper.counter2 + " Timer is being held.");
            carBeeper.setHolding(true);
            carBeeper.beingHeldTimer++;
        }
        else if (carBeeper.beingHeldTimer > 3 && carBeeper.windowStatesPrinted)
        {
            // or maybe other states have been printed, we just need the entire text area
            if (carBeeper.counter2 < 10) {
                String temp = carBeeper.textArea.getText(); //.substring(0, textArea.getText().length()-26);
                carBeeper.textArea.setText(temp + ++carBeeper.counter2 + " Window button held: yes\n");
            }
            else if (carBeeper.counter2 <= 100) {
                carBeeper.textArea.setText(carBeeper.textArea.getText() + ++carBeeper.counter2 + " Window button held: yes\n");
            }
            else {
                LOGGER.error("counter2: {} shouldn't go any higher than 100.", carBeeper.counter2);
            }
            LOGGER.info("{} Timer is being held.", carBeeper.counter2);
            carBeeper.setWindowStatesPrinted(false);
            carBeeper.setHolding(true);
            carBeeper.beingHeldTimer++;
        }
        else
        {
            LOGGER.error("Research unknown error");
        }
        LOGGER.info("End beingHeld Timer actionEvent.");
    }

    @Override
    public String toString()
    {
        return "WindowButtonHandler: " +
                "timer, type of Timer " +
                "beingHeldTimer, type of Timer " +
                "LOGGER, type of Logger.";

    }

    public void printWindowStates(int clicks, int windowCounter)
    {
        if ((clicks == 1 || clicks == 2) && windowCounter == 0)
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window: " + carBeeper.driverWindowState);
            LOGGER.info("Passenger Window: " + carBeeper.passengerWindowState);
            LOGGER.info("Back Left Window: " + carBeeper.leftWindowState);
            LOGGER.info("Back Right Window: " + carBeeper.rightWindowState);
            LOGGER.info("----- End Window States -----");
        }
        else if (clicks == 1 && windowCounter > 0)
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window is " + windowCounter + "% " +  carBeeper.driverWindowState);
            LOGGER.info("Passenger Window: " + carBeeper.passengerWindowState);
            LOGGER.info("Back Left Window: " + carBeeper.leftWindowState);
            LOGGER.info("Back Right Window: " + carBeeper.rightWindowState);
            LOGGER.info("----- End Window States -----");
        }
        else
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window is " + windowCounter + "% " +  carBeeper.driverWindowState);
            LOGGER.info("Passenger Window is " + windowCounter + "% " + carBeeper.passengerWindowState);
            LOGGER.info("Back Left Window is " + windowCounter + "% " + carBeeper.leftWindowState);
            LOGGER.info("Back Right Window is " + windowCounter + "% " +  carBeeper.rightWindowState);
            LOGGER.info("----- End Window States -----");
        }
        carBeeper.windowStatesPrinted = true;
    }

    /**
     * Sets a flat tire based on a random number generated by the CarBeeper.
     * The random number determines which tire will be set to flat.
     */
    private void setFlatTire()
    {
        if (carBeeper.triggerFlatTire()) {
            if (0 <= carBeeper.randomNumber && carBeeper.randomNumber < 25) { carBeeper.driverTireState = State.FLAT; }
            else if (25 <= carBeeper.randomNumber && carBeeper.randomNumber < 50) { carBeeper.passengerTireState = State.FLAT; }
            else if (50 <= carBeeper.randomNumber && carBeeper.randomNumber < 75) { carBeeper.leftTireState = State.FLAT; }
            else if (75 <= carBeeper.randomNumber && carBeeper.randomNumber <= 100) { carBeeper.rightTireState = State.FLAT; }
        }
    }
}
