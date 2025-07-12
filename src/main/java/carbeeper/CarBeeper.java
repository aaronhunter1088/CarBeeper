package carbeeper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.io.Serial;
import java.net.URL;
import java.util.List;
import java.util.Random;

import static carbeeper.Constants.*;

/**
 * @author aaron hunter
 */
public class CarBeeper extends JFrame {
    @Serial
    private static final long serialVersionUID = 2L;
	protected static final Logger LOGGER = LogManager.getLogger(CarBeeper.class);
	protected final int TIMER_INTERVAL = 500;
    protected int randomNumber,
                  buttonClicks = 0,
                  beingHeldTimer = 0,
                  counter2 = 0;
    // Buttons
    protected JButton lockButton,
                      flatTireButton,
                      windowButton,
                      powerButton,
                      trunkButton,
                      alarmButton,
                      clearButton;
    // Images for the Buttons
    protected Icon lockImage,
                   flatTireImage,
                   windowImage,
                   powerImage,
                   trunkImage,
                   alarmImage;
    // Button States
    protected State powerState,
                    trunkState,
                    alarmState,
    // All door States
                    driverDoorLockState,
                    passengerDoorLockState,
                    leftDoorLockState,
                    rightDoorLockState,
    // All window States
                    driverWindowState,
                    passengerWindowState,
                    leftWindowState,
                    rightWindowState,
    // All tire States
                    driverTireState,
                    passengerTireState,
                    leftTireState,
                    rightTireState;
    // TextArea
    protected JTextArea textArea = new JTextArea("", 10, 1); // textArea of rows and columns
    // Layout and Constraints
    protected GridBagLayout layout;
    protected GridBagConstraints constraints;
    // Other Attributes
    protected boolean wasHeld = false,
                      holding = false,
                      singleClick = false,
                      doubleClick = false,
                      windowStatesPrinted = false;

    /**
     * Constructor
     */
    public CarBeeper()
    {
        super("Car Beeper");
        LOGGER.info("Inside CarBeeper constructor.");
        setDefaultValues();
        setThisLayout(new GridBagLayout());
        setConstraints(new GridBagConstraints());
        // Images
        setLockImage(createImageIcon("images/lock.jpg", "Lock image"));
        setFlatTireImage(createImageIcon("images/flatTire.jpg", "Flat tire image"));
        setWindowImage(createImageIcon("images/window.jpg", "Window image"));
        setPowerImage(createImageIcon("images/power.jpg", "Power image"));
        setTrunkImage(createImageIcon("images/trunk.jpg", "Trunk image"));
        setAlarmImage(createImageIcon("images/alarm.jpg", "Alarm image"));
        // Buttons
        setLockButton(new JButton(LOCK, getLockImage()));
        setFlatTireButton(new JButton(FLAT_TIRE, getFlatTireImage()));
        setWindowButton(new JButton(WINDOW, getWindowImage()));
        setPowerButton(new JButton(POWER, getPowerImage()));
        setTrunkButton(new JButton(TRUNK, getTrunkImage()));
        setAlarmButton(new JButton(ALARM, getAlarmImage()));
        setClearButton(new JButton(CLEAR));
        setFlatTireRandomizer();
        // Button functionalities
        //setButtonFunctionalities();
        addComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(290, 400);
        setLocationRelativeTo(null); // loads the GUI in the center of the screen
        setResizable(false);
        setVisible(true);
        pack();
        LOGGER.info("End CarBeeper constructor.");
    }

    /**
     * Displays to the textArea the default values of the states of each button.
     */
    private void setDefaultValues()
    {
        LOGGER.debug("Inside setDefaultValues(). Setting initial states...");
        setPowerState(State.OFF);
        setTrunkState(State.CLOSED);
        setAlarmState(State.OFF);
        setDriverDoorLockState(State.UNLOCKED);
        setPassengerDoorLockState(State.UNLOCKED);
        setLeftDoorLockState(State.UNLOCKED);
        setRightDoorLockState(State.UNLOCKED);
        setDriverTireState(State.INFLATED);
        setPassengerTireState(State.INFLATED);
        setLeftTireState(State.INFLATED);
        setRightTireState(State.INFLATED);
        setDriverWindowState(State.UP);
        setPassengerWindowState(State.UP);
        setLeftWindowState(State.UP);
        setRightWindowState(State.UP);
        getTextArea().setLineWrap(true); // wraps the text to the next line
        getTextArea().setWrapStyleWord(rootPaneCheckingEnabled); // ensures wrapping wraps full words
        ((DefaultCaret)getTextArea().getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        updateAllStatesInTextArea();
        LOGGER.info("Default values set");
    }

    /** Returns an ImageIcon, or null if the path was invalid.
     * @param path the path of the image
     * @param description a description of the image being set
     */
    protected ImageIcon createImageIcon(String path, String description)
    {
        LOGGER.debug("Inside createImageIcon(): creating image for {}", description);
        ImageIcon retImageIcon = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path);
        if (resource != null) {
            retImageIcon = new ImageIcon(resource);
            LOGGER.debug("the path '{}' created the '{}'! the ImageIcon is being returned...", path, description);
            LOGGER.info("Image set for {}", description);
        }
        else {
            LOGGER.warn("The path '{}' could not find an image there!. Trying again by removing 'src/main/resources/' from path!", path);
            resource = classLoader.getResource(path.substring(19));
            if (resource != null) {
                retImageIcon = new ImageIcon(resource);
                LOGGER.debug("the path '{}' created an image after removing 'src/main/resources/'! the ImageIcon is being returned...", path);
                LOGGER.info("Image set for {}", description);
            }
            else {
                LOGGER.error("The path '{}' could not find an image there after removing src/main/resources/!. Returning null!", path);
            }

        }
        return retImageIcon;
    }

    /**
     * This method sets the state of the tires to flat or inflated
     */
    private void setFlatTireRandomizer()
    {
        LOGGER.debug("Random Number {}", getRandomNumber());
        setRandomNumber(new Random().nextInt(100));
    }

    /**
     * This method sets the button functionalities
     */
    protected void setButtonFunctionalities()
    {
        LOGGER.debug("Setting up all button functionalities....");
        powerButton.addMouseListener(new ButtonClicked(this) {});
        trunkButton.addMouseListener(new ButtonClicked(this) {});
        alarmButton.addMouseListener(new ButtonClicked(this) {});
        lockButton.addMouseListener (new ButtonClicked(this) {});
        clearButton.addMouseListener(new ButtonClicked(this) {});
        flatTireButton.addMouseListener(new ButtonClicked(this) {});
        windowButton.addMouseListener(new WindowButtonHandler(this) {});
        LOGGER.info("Button functionalities are all set up.");
    }

    /**
     * Add all the components to the frame
     */
    protected void addComponents()
    {
        LOGGER.debug("Adding components.");
        getConstraints().fill = GridBagConstraints.BOTH;
        addComponent(getLockButton(), 1, 1, 1, 1); // row, column, size, size
        addComponent(getFlatTireButton(), 1, 2, 1, 1); // row, column, size, size
        addComponent(getWindowButton(), 1, 3, 1, 1); // row, column, size, size
        addComponent(getPowerButton(), 2, 1, 1, 1); // row, column, size, size
        addComponent(getTrunkButton(), 2, 2, 1, 1); // row, column, size, size
        addComponent(getAlarmButton(), 2, 3, 1, 1); // row, column, size, size
        addComponent(getClearButton(), 6, 2, 1, 1); // row, column, size, size
        addComponent(new JScrollPane(getTextArea()), 5, 1, 3, 1); // row, column, size, size
        LOGGER.info("All components added.");
    }

    /**
     * method to set constraints on
     */
    private void addComponent(Component component, int gridy, int gridx, double gwidth, double gheight)
    {
        LOGGER.debug("Adding component {}", component.getName() == null ? "unknown name" : component.getName());
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = (int)Math.ceil(gwidth);
        constraints.gridheight = (int)Math.ceil(gheight); // (int)Math.ceil(gheight); *actual value of parameter is always 1
        getThisLayout().setConstraints(component, constraints);
        add(component);
        LOGGER.info("Component added.");
    }

    // Getters
    /**
     * This method returns the state of the car's power.
     * @return State of the car's power
     */
    public State getPowerState() {
        LOGGER.debug("Car is: {}", powerState);
        return powerState;
    }
    /**
     * This method returns the state of the trunk.
     * @return State of the trunk
     */
    public State getTrunkState() {
        LOGGER.debug("Trunk is: {}", trunkState);
        return trunkState;
    }
    /**
     * This method returns the state of the alarm.
     * @return State the state of the alarm
     */
    public State getAlarmState() {
        LOGGER.debug("Alarm is: {}", alarmState);
        return alarmState;
    }
    /**
     * This method returns the driver door lock state
     * @return State the state of the driver door's lock
     */
    public State getDriverDoorLockState() {
        LOGGER.debug("Driver door lock state is {}", driverDoorLockState);
        return driverDoorLockState;
    }
    /**
     * This method returns the passenger door lock state
     * @return State the state of the passenger doors lock state
     */
    public State getPassengerDoorLockState() {
        LOGGER.debug("Passenger door lock is {}", passengerDoorLockState);
        return passengerDoorLockState;
    }
    /**
     * This method returns the left door lock state
     * @return State the state of the left door lock state
     */
    public State getLeftDoorLockState() {
        LOGGER.debug("Left door lock is {}", leftDoorLockState);
        return leftDoorLockState;
    }
    /**
     * This method returns the right door lock state
     * @return rightDoorLockState the state of the right door
     */
    public State getRightDoorLockState() {
        LOGGER.debug("Right door lock is {}", rightDoorLockState);
        return rightDoorLockState;
    }
    /**
     * This method returns all the lock states of the doors
     */
    public List<State> getDoorLockStates() {
        return List.of(
            getDriverDoorLockState(),
            getPassengerDoorLockState(),
            getLeftDoorLockState(),
            getRightDoorLockState()
        );
    }
    /**
     * This method simply returns the current state of all the windows.
     * @param clicks : integer; one click or two
     * @param windowCounter : counter2
     */
    public void getWindowStates(int clicks, int windowCounter) {
        LOGGER.debug("getWindowStates");
        if ((clicks == 1 || clicks == 2) && windowCounter == 0) {
            printWindowStates(clicks, 0);
        }
        else if (clicks == 1 && windowCounter > 0) {
            printWindowStates(clicks, windowCounter);
        }
        else { // clicks == 2 && windowCounter > 0
            printWindowStates(clicks, windowCounter);
        }
    }
    /**
     * This method returns the states of all the windows
     * @return a list of all the window states
     */
    public List<State> getWindowStates() {
        return List.of(
            getDriverWindowState(),
            getPassengerWindowState(),
            getLeftWindowState(),
            getRightWindowState()
        );
    }
    /**
     * This method returns the driver window state
     * @return driverWindowState the state of the driver window
     */
    protected State getDriverWindowState() {
        LOGGER.debug("Driver window is {}", driverWindowState);
        return driverWindowState;
    }
    /**
     * This method returns the passenger door window state
     * @return State the state of the passenger door window's state
     */
    public State getPassengerWindowState() {
        LOGGER.debug("Passenger window is {}", passengerWindowState);
        return passengerWindowState;
    }
    /**
     * This method returns the left door window state
     * @return State the state of the left window state
     */
    public State getLeftWindowState() {
        LOGGER.debug("Left window is {}", leftWindowState);
        return leftWindowState;
    }
    /**
     * This method returns the right door window state
     * @return State the state of the right window state
     */
    public State getRightWindowState() {
        LOGGER.debug("Right window is {}", rightWindowState);
        return rightWindowState;
    }
    /**
     * This method returns the driver tire state
     * @return driverTireState the state of the driver tire
     */
    protected State getDriverTireState() {
        LOGGER.debug("Driver tire state is {}", driverTireState);
        return driverTireState;
    }
    /**
     * This method returns the passenger tire state
     * @return passengerTireState the state of the passenger tire
     */
    public State getPassengerTireState() {
        LOGGER.debug("Passenger tire state is {}", passengerTireState);
        return passengerTireState;
    }
    /**
     * This method returns the left tire state
     * @return leftTireState the state of the left tire
     */
    public State getLeftTireState() {
        LOGGER.debug("Left tire state is {}", leftTireState);
        return leftTireState;
    }
    /**
     * This method returns the right tire state
     * @return rightTireState the state of the right tire
     */
    public State getRightTireState() {
        LOGGER.debug("Right tire state is {}", rightTireState);
        return rightTireState;
    }
    /**
     * This method returns true if any of the tires are flat or false
     * if none are flat
     */
    public boolean isAnyTireFlat() {
        LOGGER.debug("Checking if any tire is flat");
        return getDriverTireState() == State.FLAT || getPassengerTireState() == State.FLAT
                || getLeftTireState() == State.FLAT || getRightTireState() == State.FLAT;
    }
    public List<State> getTireStates() {
        return List.of(
            getDriverTireState(),
            getPassengerTireState(),
            getLeftTireState(),
            getRightTireState()
        );
    }
    /**
     * This method returns the text area
     * @return JTextArea the textarea that displays results
     */
    public JTextArea getTextArea() {
        return textArea;
    }
    /**
     * This method returns the lock button
     * @return JButton the lock button
     */
    public JButton getLockButton() {
        LOGGER.debug("Lock button is {}", lockButton == null ? "null" : "set");
        return lockButton;
    }
    /**
     * This method returns the window button
     * @return JButton the window button
     */
    public JButton getWindowButton() {
        LOGGER.debug("Window button is {}", windowButton == null ? "null" : "set");
        return windowButton;
    }
    /**
     * This method returns the power button
     * @return JButton the power button
     */
    public JButton getPowerButton() {
        LOGGER.debug("Power button is {}", powerButton == null ? "null" : "set");
        return powerButton;
    }
    /**
     * This method returns the trunk button
     * @return JButton the trunk button
     */
    public JButton getTrunkButton() {
        LOGGER.debug("Trunk button is {}", trunkButton == null ? "null" : "set");
        return trunkButton;
    }
    /**
     * This method returns the alarm button
     * @return JButton the alarm button
     */
    public JButton getAlarmButton() {
        LOGGER.debug("Alarm button is {}", alarmButton == null ? "null" : "set");
        return alarmButton;
    }
    /**
     * This method returns the clear button
     * @return JButton the clear button
     */
    public JButton getClearButton() {
        LOGGER.debug("Clear button is {}", clearButton == null ? "null" : "set");
        return clearButton;
    }
    /**
     * This method returns the flat tire button
     */
    public JButton getFlatTireButton() {
        LOGGER.debug("Flat tire button is {}", flatTireButton == null ? "null" : "set");
        return flatTireButton;
    }
    /**
     * This method returns the lock image
     * @return Icon the lock image
     */
    public Icon getLockImage() {
        LOGGER.debug("Lock image is {}", lockImage == null ? "null" : "set");
        return lockImage;
    }
    /**
     * This method returns the window image
     * @return Icon the window image
     */
    public Icon getWindowImage() {
        LOGGER.debug("Window image is {}", windowImage == null ? "null" : "set");
        return windowImage;
    }
    /**
     * This method returns the power image
     * @return Icon the power image
     */
    public Icon getPowerImage() {
        LOGGER.debug("Power image is {}", powerImage == null ? "null" : "set");
        return powerImage;
    }
    /**
     * This method returns the trunk image
     * @return Icon the trunk image
     */
    public Icon getTrunkImage() {
        LOGGER.debug("Trunk image is {}", trunkImage == null ? "null" : "set");
        return trunkImage;
    }
    /**
     * This method returns the alarm image
     * @return Icon the alarm image
     */
    public Icon getAlarmImage() {
        LOGGER.debug("Alarm image is {}", alarmImage == null ? "null" : "set");
        return alarmImage;
    }
    /**
     * This method returns the flat tire image
     */
    public Icon getFlatTireImage() {
        LOGGER.debug("Flat tire image is {}", flatTireImage == null ? "null" : "set");
        return flatTireImage;
    }
    /** This method returns the layout for the car beeper */
    public GridBagLayout getThisLayout() { return layout; }
    /** This method returns the constraints for the component */
    public GridBagConstraints getConstraints() { return this.constraints; }
    /**
     * This method returns the random number, used to set the state
     * of a tire
     */
    public int getRandomNumber() {
        LOGGER.debug("Random number is {}", randomNumber);
        return randomNumber;
    }
    /**
     * Get wasHeld
     */
    public boolean getWasHeld() {
        return wasHeld;
    }
    /**
     * Get holding
     */
    public boolean getHolding() {
        return holding;
    }
    /**
     * Get singleClick
     */
    public boolean getSingleClick() {
        return singleClick;
    }
    /**
     * Get doubleClick
     */
    public boolean getDoubleClick() {
        return doubleClick;
    }
    /**
     * Get windowStatesPrinted
     */
    public boolean getWindowStatesPrinted() {
        return windowStatesPrinted;
    }
    /**
     * Get beingHeldTimer
     */
    public int getBeingHeldTimer() {
        return beingHeldTimer;
    }
    /**
     * Get counter2
     */
    public int getCounter2() {
        return counter2;
    }

    // Setters
    /**
     * This method is only used for testing purposes only.
     * @param driverDoorLockState the state of the master door
     */
    protected void setDriverDoorLockState(State driverDoorLockState) {
        this.driverDoorLockState = driverDoorLockState;
    }
    /**
     * This method is used for testing purposes only.
     * @param passengerDoorLockState the state of the passenger door
     */
    protected void setPassengerDoorLockState(State passengerDoorLockState) {this.passengerDoorLockState = passengerDoorLockState;}
    /**
     * This method is used for testing purposes only.
     * @param leftDoorLockState the state of the left door
     */
    protected void setLeftDoorLockState(State leftDoorLockState) {
        this.leftDoorLockState = leftDoorLockState;
    }
    /**
     * This method is used for testing purposes only.
     * @param rightDoorLockState the state of the right door
     */
    protected void setRightDoorLockState(State rightDoorLockState) {
        this.rightDoorLockState = rightDoorLockState;
    }
    /**
     * This method is used for testing purposes only.
     * @param driverWindowState the state of the master window
     */
    protected void setDriverWindowState(State driverWindowState) {
        this.driverWindowState = driverWindowState;
    }
    /**
     * This method is used for testing purposes only.
     * @param passengerWindowState the state of the passenger window
     */
    protected void setPassengerWindowState(State passengerWindowState) {this.passengerWindowState = passengerWindowState;}
    /**
     * This method is used for testing purposes only.
     * @param leftWindowState the state of the left window
     */
    protected void setLeftWindowState(State leftWindowState) {
        this.leftWindowState = leftWindowState;
    }
    /**
     * This method is used for testing purposes only.
     * @param rightWindowState the state of the right window
     */
    protected void setRightWindowState(State rightWindowState) {
        this.rightWindowState = rightWindowState;
    }
    /** This method is used for setting the car's power state
     * @param powerState the state of the car's power
     */
    protected void setPowerState(State powerState) { this.powerState = powerState; }
    /** This method is used for setting the car's trunk state
     * @param trunkState the state of the car's trunk
     */
    protected void setTrunkState(State trunkState) { this.trunkState = trunkState; }
    /** This method is used for setting the car's alarm state
     * @param alarmState the state of the car's alarm
     */
    protected void setAlarmState(State alarmState) { this.alarmState = alarmState; }
    /**
     * This method is only used for testing purposes only.
     * @param driverTireState the state of the master door
     */
    protected void setDriverTireState(State driverTireState) {
        this.driverTireState = driverTireState;
    }
    /**
     * This method is used for testing purposes only.
     * @param passengerTireState the state of the passenger door
     */
    protected void setPassengerTireState(State passengerTireState) {
        this.passengerTireState = passengerTireState;
    }
    /**
     * This method is used for testing purposes only.
     * @param leftTireState the state of the left door
     */
    protected void setLeftTireState(State leftTireState) {
        this.leftTireState = leftTireState;
    }
    /**
     * This method is used for testing purposes only.
     * @param rightTireState the state of the right door
     */
    protected void setRightTireState(State rightTireState) {
        this.rightTireState = rightTireState;
    }
    /**
     * This method sets the lock button
     * @param lockButton the lock button
     */
    public void setLockButton(JButton lockButton) { this.lockButton = lockButton; }
    /**
     * This method sets the window button
     * @param windowButton the window button
     */
    public void setWindowButton(JButton windowButton) { this.windowButton = windowButton; }
    /**
     * This method sets the power button
     * @param powerButton the power button
     */
    public void setPowerButton(JButton powerButton) { this.powerButton = powerButton; }
    /**
     * This method sets the trunk button
     * @param trunkButton the trunk button
     */
    public void setTrunkButton(JButton trunkButton) { this.trunkButton = trunkButton; }
    /**
     * This method sets the alarm button
     * @param alarmButton the alarm button
     */
    public void setAlarmButton(JButton alarmButton) { this.alarmButton = alarmButton; }
    /**
     * This method sets the clear button
     * @param clearButton the clear button
     */
    public void setClearButton(JButton clearButton) {
        this.clearButton = clearButton;
    }
    /**
     * This method sets the flat tire button
     */
    public void setFlatTireButton(JButton flatTireButton) { this.flatTireButton = flatTireButton; }
    /** This method sets the lock icon
     * @param icon sets the lock icon for the lock button
     */
    protected void setLockImage(ImageIcon icon) { lockImage = icon; }
    /** This method sets the window icon
     * @param icon sets the window icon for the window button
     */
    protected void setWindowImage(ImageIcon icon) { windowImage = icon; }
    /**
     * This method sets the lock icon for the lock button
     */
    protected void setPowerImage(ImageIcon icon) { powerImage = icon; }
    /**
     * This method sets the trunk icon for the trunk button
     */
    protected void setTrunkImage(ImageIcon icon) { trunkImage = icon; }
    /** This method sets the alarm icon
     * @param icon sets the alarm icon for the alarm button
     */
    protected void setAlarmImage(ImageIcon icon) { alarmImage = icon; }
    /**
     * This method sets the flat tire icon for the flat tire button
     */
    protected void setFlatTireImage(ImageIcon icon) { flatTireImage = icon; }
    /** Sets the layout for the car beeper */
    protected void setThisLayout(GridBagLayout layout) { setLayout(layout); this.layout = layout; }
    /** Sets the constraints for the car beeper
     * @param constraints the constraints to set on the component
     */
    protected void setConstraints(GridBagConstraints constraints) { this.constraints = constraints; }
    /**
     * Sets the random number for the flat tire functionality
     * @param nextInt the random number to set
     */
    protected void setRandomNumber(int nextInt)
    {
        this.randomNumber = nextInt;
    }
    /**
     * Set wasHeld
     */
    protected void setWasHeld(boolean wasHeld) {
        this.wasHeld = wasHeld;
    }
    /**
     * Set holding
     */
    protected void setHolding(boolean holding) {
        this.holding = holding;
    }
    /**
     * Set singleClick
     */
    protected void setSingleClick(boolean singleClick) {
        this.singleClick = singleClick;
    }
    /**
     * Set doubleClick
     */
    protected void setDoubleClick(boolean doubleClick) {
        this.doubleClick = doubleClick;
    }
    /**
     * Set windowStatesPrinted
     */
    protected void setWindowStatesPrinted(boolean windowStatesPrinted) {
        this.windowStatesPrinted = windowStatesPrinted;
    }
    /**
     * Set beingHeldTimer
     */
    protected void setBeingHeldTimer(int beingHeldTimer) {
        this.beingHeldTimer = beingHeldTimer;
    }
    /**
     * Set counter2
     */
    protected void setCounter2(int counter2) {
        this.counter2 = counter2;
    }

    // Helper methods
    /**
     * This method locks or unlocks the master door,
     * which is also the drivers door; it only operates
     * on the master door.
     */
    protected void lock_Unlock()
    {
        if (getDriverDoorLockState().equals(State.UNLOCKED)) {
            LOGGER.debug("Locking the master door");
            setDriverDoorLockState(State.LOCKED);
        }
        else {
            LOGGER.debug("Unlocking the master door");
            setDriverDoorLockState(State.UNLOCKED);
        }
    }

    /**
     * This method locks or unlocks the passenger door, and
     * both the back doors based on the current state of the
     * driver door; It takes one variable: a Boolean that tests
     * whether or not the driver door was just locked or not.
     */
    protected void lock_UnlockAll()
    {
        lock_Unlock();
        if (!getPassengerDoorLockState().equals(driverDoorLockState))
        {
            LOGGER.debug("Setting passenger door lock state to {}", driverDoorLockState);
             setPassengerDoorLockState(driverDoorLockState);
        }
        if (!getLeftDoorLockState().equals(driverDoorLockState))
        {
            LOGGER.debug("Setting left door lock state to {}", driverDoorLockState);
            setLeftDoorLockState(driverDoorLockState);
        }
        if (!getRightDoorLockState().equals(driverDoorLockState))
        {
            LOGGER.debug("Setting right door lock state to {}", driverDoorLockState);
            setRightDoorLockState(driverDoorLockState);
        }
    }

    /**
     * This method will roll up or down the driver door window.
     */
    public void windowUp_Down()
    {
        if (getDriverWindowState().equals(State.UP))
        {
            LOGGER.debug("Rolling down the driver window");
            setDriverWindowState(State.DOWN);
        }
        else
        {
            LOGGER.debug("Rolling up the driver window");
            setDriverWindowState(State.UP);
        }
    }

    /**
     * This method rolls up or down the passenger door, and
     * both the back door windows based on the current state of the
     * driver door window; It takes one variable: a Boolean that tests
     * whether or not the driver window was just rolled up or not.
     */
    public void windowUp_DownAll()
    {
        windowUp_Down();
        if (!getPassengerWindowState().equals(getDriverWindowState()))
        {
            LOGGER.debug("Setting passenger window state to {}", getDriverWindowState());
            setPassengerWindowState(getDriverWindowState());
        }
        if (!getLeftWindowState().equals(getDriverWindowState()))
        {
            LOGGER.debug("Setting left window state to {}", getDriverWindowState());
            setLeftWindowState(getDriverWindowState());
        }
        if (!getRightWindowState().equals(getDriverWindowState()))
        {
            LOGGER.debug("Setting right window state to {}", getDriverWindowState());
            setRightWindowState(getDriverWindowState());
        }
    }

    /**
     * Prints the current states of the windows
     * @param clicks the number of clicks
     * @param windowCounter the window percent counter
     */
    public void printWindowStates(int clicks, int windowCounter)
    {
        if ((clicks == 1 || clicks == 2) && windowCounter == 0)
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Driver Window: {}", driverWindowState);
            LOGGER.info("Passenger Window: {}", passengerWindowState);
            LOGGER.info("Back Left Window: {}", leftWindowState);
            LOGGER.info("Back Right Window: {}", rightWindowState);
            LOGGER.info("----- End Window States -----");
            textArea.append("\nDriver window is " + driverWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n");
        }
        else if (clicks == 1 && windowCounter > 0)
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Driver Window is {}% {}", windowCounter, driverWindowState);
            LOGGER.info("Passenger Window: {}", passengerWindowState);
            LOGGER.info("Back Left Window: {}", leftWindowState);
            LOGGER.info("Back Right Window: {}", rightWindowState);
            LOGGER.info("----- End Window States -----");
            textArea.append("\nDriver window is " + windowCounter + "% " + driverWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n");
        }
        else
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Driver Window is {}% {}", windowCounter, driverWindowState);
            LOGGER.info("Passenger Window is {}% {}", windowCounter, passengerWindowState);
            LOGGER.info("Back Left Window is {}% {}", windowCounter, leftWindowState);
            LOGGER.info("Back Right Window is {}% {}", windowCounter,  rightWindowState);
            LOGGER.info("----- End Window States -----");
            textArea.append("\nDriver window is " + windowCounter + "% " + driverWindowState + "\n");
            textArea.append("Passenger window is " + windowCounter + "% " + passengerWindowState + "\n");
            textArea.append("Left window is " + windowCounter + "% " + leftWindowState + "\n");
            textArea.append("Right window is " + windowCounter + "% " + rightWindowState + "\n");
        }
        windowStatesPrinted = true;
    }

    /**
     * Prints the current states of the doors
     */
    public void printDoorStates()
    {
        LOGGER.info("----- Door States -----");
        LOGGER.info("Driver door lock: {}", driverDoorLockState);
        LOGGER.info("Passenger door lock: {}", passengerDoorLockState);
        LOGGER.info("Back Left door lock: {}", leftDoorLockState);
        LOGGER.info("Back Right doorLock: {}", rightDoorLockState);
        LOGGER.info("----- End Door States -----");
        textArea.append("\nDriver Door lock is " + driverDoorLockState + "\n");
        textArea.append("Passenger Door lock is " + passengerDoorLockState + "\n");
        textArea.append("Left Door lock is " + leftDoorLockState + "\n");
        textArea.append("Right Door lock is " + rightDoorLockState + "\n");
    }

    /**
     * Prints the current state of the car power
     */
    public void printCarState()
    {
        LOGGER.info("----- Car State -----");
        LOGGER.info("Car is {}", powerState);
        LOGGER.info("----- End Car State -----");
        textArea.append("Car is " + powerState + "\n");
    }

    /**
     * This method updates the text area with
     * the current states of all the buttons.
     */
    public void updateAllStatesInTextArea()
    {
        textArea.append("Driver Door is " + getDriverDoorLockState() + ".\n");
        textArea.append("Passenger Door is " + getPassengerDoorLockState() + ".\n");
        textArea.append("Left Door is " + getLeftDoorLockState() + ".\n");
        textArea.append("Right Door is " + getRightDoorLockState() + ".\n");
        textArea.append("Driver Window is " + getDriverWindowState() + ".\n");
        textArea.append("Passenger Window is " + getPassengerWindowState() + ".\n");
        textArea.append("Left Window is " + getLeftWindowState() + ".\n");
        textArea.append("Right Window is " + getRightWindowState() + ".\n");
        textArea.append("Car is " + getPowerState() + ".\n");
        textArea.append("Trunk is " + getTrunkState() + ".\n");
        textArea.append("Alarm is " + getAlarmState() + ".\n");
        textArea.append("Driver Tire is " + getDriverTireState() + ".\n");
        textArea.append("Passenger Tire is " + getPassengerTireState() + ".\n");
        textArea.append("Left Tire is " + getLeftTireState() + ".\n");
        textArea.append("Right Tire is " + getRightTireState() + ".\n");
    }
}