package carbeeper;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.security.SecureRandom;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
/**
 * @author aaron hunter
 */
public class CarBeeperV2 extends JFrame {
    protected static final long serialVersionUID = 1L;
	protected static final Logger LOGGER = LogManager.getLogger(CarBeeperV2.class);
	protected final int TIMER_INTERVAL = 500;
    public int randomNumber;
    // Buttons
    protected JButton lockButton;
    protected JButton windowButton;
    protected JButton powerButton;
    protected JButton trunkButton;
    protected JButton alarmButton;
    protected JButton clearButton;
    protected JButton flatTireButton;
    // Images for the Buttons
    protected Icon lockImage;
    protected Icon windowImage;
    protected Icon powerImage;
    protected Icon trunkImage;
    protected Icon alarmImage;
    protected Icon flatTireImage;
    // Button States
    protected State powerState;
    protected State trunkState;
    protected State alarmState;
    protected State masterDoorLockState;
    protected State passengerDoorLockState;
    protected State leftDoorLockState;
    protected State rightDoorLockState;
    protected State masterWindowState;
    protected State passengerWindowState;
    protected State leftWindowState;
    protected State rightWindowState;
    protected State masterTireState;
    protected State passengerTireState;
    protected State leftTireState;
    protected State rightTireState;
    // TextArea
    public final JTextArea textArea = new JTextArea("", 10, 1); // textArea of rows and columns
    // Layout and Constraints
    protected GridBagLayout layout;
    protected GridBagConstraints constraints;
    // Other Attributes
    protected boolean wasHeld = false;
    protected boolean holding = false;
    protected boolean singleClick = false, doubleClick = false, windowStatesPrinted = false;
    protected int beingHeldTimer = 0;
    protected int counter2 = 0;
    // Getters
    /**
     * This method returns the state of the car's power.
     * @return State of the car's power
     */
    public State getPowerState() {
        LOGGER.info("Car is: " + powerState);
        return powerState;
    }
    /**
     * This method returns the state of the trunk.
     * @return State of the trunk
     */
    public State getTrunkState() {
        LOGGER.info("Trunk is: " + trunkState);
        return trunkState;
    }
    /**
     * This method returns the state of the alarm.
     * @return State the state of the alarm
     */
    public State getAlarmState() {
        LOGGER.info("Alarm is: " + alarmState);
        return alarmState;
    }
    /**
     * This method simply returns the current state of all the windows.\
     */
    public void getLockState() {
        textArea.append("\nMaster Door lock is " + masterDoorLockState + "\n");
        textArea.append("Passenger Door lock is " + passengerDoorLockState + "\n");
        textArea.append("Left Door lock is " + leftDoorLockState + "\n");
        textArea.append("Right Door lock is " + rightDoorLockState + "\n");
        printDoorStates();
    }
    /**
     * This method returns the master door lock state
     * @return State the state of the master door's lock
     */
    public State getMasterDoorLockState() {
        LOGGER.info("Master door lock state is : " + masterDoorLockState);
        return masterDoorLockState;
    }
    /**
     * This method returns the passenger door lock state
     * @return State the state of the passenger doors lock state
     */
    public State getPassengerDoorLockState() {
        LOGGER.info("Passenger door lock is : " + passengerDoorLockState);
        return passengerDoorLockState;
    }
    /**
     * This method returns the left door lock state
     * @return State the state of the left door lock state
     */
    public State getLeftDoorLockState() {
        LOGGER.info("Left door lock is : " + leftDoorLockState);
        return leftDoorLockState;
    }
    /**
     * This method returns the right door lock state
     * @return rightDoorLockState the state of the right door
     */
    public State getRightDoorLockState() {
        LOGGER.info("Right door lock is : " + rightDoorLockState);
        return rightDoorLockState;
    }
    /**
     * This method simply returns the current state of all the windows.
     * @param clicks : integer; one click or two
     * @param windowCounter : counter2
     */
    public void getWindowStates(int clicks, int windowCounter) {
        if ((clicks == 1 || clicks == 2) && windowCounter == 0)
        {
            textArea.append("\nMaster window is " + masterWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n\n");
            printWindowStates(clicks, 0);
        }
        else if (clicks == 1 && windowCounter > 0) {
            textArea.append("\nMaster window is " + windowCounter + "% " + masterWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n\n");
            printWindowStates(clicks, windowCounter);
        } else { // clicks == 2 && windowCounter > 0
            textArea.append("\nMaster window is " + windowCounter + "% " + masterWindowState + "\n");
            textArea.append("Passenger window is " + windowCounter + "% " + passengerWindowState + "\n");
            textArea.append("Left window is " + windowCounter + "% " + leftWindowState + "\n");
            textArea.append("Right window is " + windowCounter + "% " + rightWindowState + "\n\n");
            printWindowStates(clicks, windowCounter);
        }
    }
    /**
     * This method returns the master window state
     * @return masterWindowState the state of the master window
     */
    protected State getMasterWindowState() {
        LOGGER.info("Master window is : " + masterWindowState);
        return masterWindowState;
    }
    /**
     * This method returns the passenger door window state
     * @return State the state of the passenger door window's state
     */
    public State getPassengerWindowState() {
        LOGGER.info("Passenger window is : " + passengerWindowState);
        return passengerWindowState;
    }
    /**
     * This method returns the left door window state
     * @return State the state of the left window state
     */
    public State getLeftWindowState() {
        LOGGER.info("Left window is : " + leftWindowState);
        return leftWindowState;
    }
    /**
     * This method returns the right door window state
     * @return State the state of the right window state
     */
    public State getRightWindowState() {
        LOGGER.info("Right window is : " + rightWindowState);
        return rightWindowState;
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
    public JButton getLockButton() { return lockButton; }
    /**
     * This method returns the window button
     * @return JButton the window button
     */
    public JButton getWindowButton() { return windowButton; }
    /**
     * This method returns the power button
     * @return JButton the power button
     */
    public JButton getPowerButton() { return powerButton; }
    /**
     * This method returns the trunk button
     * @return JButton the trunk button
     */
    public JButton getTrunkButton() { return trunkButton; }
    /**
     * This method returns the alarm button
     * @return JButton the alarm button
     */
    public JButton getAlarmButton() { return alarmButton; }
    /**
     * This method returns the clear button
     * @return JButton the clear button
     */
    public JButton getClearButton() { return clearButton; }
    /**
     * This method returns the flat tire button
     */
    public JButton getFlatTireButton() { return flatTireButton; }
    /**
     * This method returns the lock image
     * @return Icon the lock image
     */
    public Icon getLockImage() { return lockImage; }
    /**
     * This method returns the window image
     * @return Icon the window image
     */
    public Icon getWindowImage() { return windowImage; }
    /**
     * This method returns the power image
     * @return Icon the power image
     */
    public Icon getPowerImage() { return powerImage; }
    /**
     * This method returns the trunk image
     * @return Icon the trunk image
     */
    public Icon getTrunkImage() { return trunkImage; }
    /**
     * This method returns the alarm image
     * @return Icon the alarm image
     */
    public Icon getAlarmImage() { return alarmImage; }
    /**
     * This method returns the flat tire image
     */
    public Icon getFlatTireImage() { return flatTireImage; }
    /**
     * This method returns the master tire state
     * @return masterTireState the state of the master tire
     */
    protected State getMasterTireState() {
        LOGGER.info("Master tire state is : " + masterTireState);
        return masterTireState;
    }
    /**
     * This method returns the passenger tire state
     * @return passengerTireState the state of the passenger tire
     */
    public State getPassengerTireState() {
        LOGGER.info("Passenger tire state is : " + passengerTireState);
        return passengerTireState;
    }
    /**
     * This method returns the left tire state
     * @return leftTireState the state of the left tire
     */
    public State getLeftTireState() {
        LOGGER.info("Left tire state is : " + leftTireState);
        return leftTireState;
    }
    /**
     * This method returns the right tire state
     * @return rightTireState the state of the right tire
     */
    public State getRightTireState() {
        LOGGER.info("Right tire state is : " + rightTireState);
        return rightTireState;
    }
    /** This method returns the layout for the car beeper */
    public GridBagLayout getThisLayout() { return layout; }
    /** This method returns the constraints for the component */
    public GridBagConstraints getConstraints() { return this.constraints; }
    /**
     * This method returns the random number, used to set the state
     * of a tire
     */
    public int getRandomNumber() { return randomNumber; }

    // Setters
    /**
     * This method is only used for testing purposes only.
     * @param state the state of the master door
     */
    protected void setMasterDoorLockState(State state) {
        this.masterDoorLockState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the passenger door
     */
    protected void setPassengerDoorLockState(State state) {
        this.passengerDoorLockState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the left door
     */
    protected void setLeftDoorLockState(State state) {
        this.leftDoorLockState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the right door
     */
    protected void setRightDoorLockState(State state) {
        this.rightDoorLockState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the master window
     */
    protected void setMasterWindowState(State state) {
        this.masterWindowState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the passenger window
     */
    protected void setPassengerWindowState(State state) {
        this.passengerWindowState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the left window
     */
    protected void setLeftWindowState(State state) {
        this.leftWindowState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the right window
     */
    protected void setRightWindowState(State state) {
        this.rightWindowState = state;
    }
    /** This method is used for setting the car's power state
     * @param state the state of the car's power
     */
    protected void setPowerState(State state) { this.powerState = state; }
    /** This method is used for setting the car's trunk state
     * @param state the state of the car's trunk
     */
    protected void setTrunkState(State state) { this.trunkState = state; }
    /** This method is used for setting the car's alarm state
     * @param state the state of the car's alarm
     */
    protected void setAlarmState(State state) { this.alarmState = state; }
    /**
     * This method is only used for testing purposes only.
     * @param state the state of the master door
     */
    protected void setMasterTireState(State state) {
        this.masterTireState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the passenger door
     */
    protected void setPassengerTireState(State state) {
        this.passengerTireState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the left door
     */
    protected void setLeftTireState(State state) {
        this.leftTireState = state;
    }
    /**
     * This method is used for testing purposes only.
     * @param state the state of the right door
     */
    protected void setRightTireState(State state) {
        this.rightTireState = state;
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
     * @param nextInt
     */
    protected void setRandomNumber(int nextInt)
    {
        this.randomNumber = nextInt;
    }

    // Helper methods
    /**
     * Displays to the textArea the default values of the states of each button.
     */
    public void setDefaultValues()
    {
        LOGGER.info("Inside setDefaultValues(). Setting initial states...");
        setPowerState(State.OFF);
        setTrunkState(State.CLOSED);
        setAlarmState(State.OFF);
        setMasterDoorLockState(State.UNLOCKED);
        setPassengerDoorLockState(State.UNLOCKED);
        setLeftDoorLockState(State.UNLOCKED);
        setRightDoorLockState(State.UNLOCKED);
        setMasterTireState(State.INFLATED);
        setPassengerTireState(State.INFLATED);
        setLeftTireState(State.INFLATED);
        setRightTireState(State.INFLATED);
        setMasterWindowState(State.UP);
        setPassengerWindowState(State.UP);
        setLeftWindowState(State.UP);
        setRightWindowState(State.UP);
        getTextArea().setLineWrap(true); // wraps the text to the next line
        getTextArea().setWrapStyleWord(rootPaneCheckingEnabled); // ensures wrapping wraps full words
        ((DefaultCaret)getTextArea().getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        updateAllStatesInTextArea();
        LOGGER.info("End setDefaultValues()");
    }
    /**
     * This method locks or unlocks the master door,
     * which is also the drivers door; it only operates
     * on the master door.
     */
    public void lock_Unlock()
    {
        if (getMasterDoorLockState().equals(State.UNLOCKED))
            setMasterDoorLockState(State.LOCKED);
        else
            setMasterDoorLockState(State.UNLOCKED);
    }
    /**
     * This method locks or unlocks the passenger door, and
     * both the back doors based on the current state of the
     * master door; It takes one variable: a Boolean that tests
     * whether or not the masterDoor was just locked or not.
     */
    public void lock_UnlockAll()
    {
        lock_Unlock();
        if (!getPassengerDoorLockState().equals(getMasterDoorLockState()))
        {
             setPassengerDoorLockState(getMasterDoorLockState());
        }
        if (!getLeftDoorLockState().equals(getMasterDoorLockState()))
        {
            setLeftDoorLockState(getMasterDoorLockState());
        }
        if (!getRightDoorLockState().equals(getMasterDoorLockState()))
        {
            setRightDoorLockState(getMasterDoorLockState());
        }
    }
    /**
     * This method will roll up or down the master door window.
     */
    public void windowUp_Down()
    {
        if (getMasterWindowState().equals(State.UP))
        {
            setMasterWindowState(State.DOWN);
        }
        else
        {
            setMasterWindowState(State.UP);
        }
    }
    /**
     * This method rolls up or down the passenger door, and
     * both the back door windows based on the current state of the
     * master door window; It takes one variable: a Boolean that tests
     * whether or not the master window was just rolled up or not.
     */
    public void windowUp_DownAll()
    {
        windowUp_Down();
        if (!getPassengerWindowState().equals(getMasterWindowState()))
        {
            setPassengerWindowState(getMasterWindowState());
        }
        if (!getLeftWindowState().equals(getMasterWindowState()))
        {
            setLeftWindowState(getMasterWindowState());
        }
        if (!getRightWindowState().equals(getMasterWindowState()))
        {
            setRightWindowState(getMasterWindowState());
        }
    }
    /**
     * This method randomly will set the state of a random tire after
     * clicking on any button, including the flat tire button
     */
    public void setFlatTireRandomizer()
    {
        // if the random number is 1, master tire is flat
        // if 2, passenger tire is flat
        // if 3, left tire is flat
        // if 4, right tire is flat
        // any other number, tires are inflated
        if (getRandomNumber() == 1) { setMasterTireState(State.FLAT); }
        else if (getRandomNumber() == 2) { setPassengerTireState(State.FLAT); }
        else if (getRandomNumber() == 3) { setLeftTireState(State.FLAT); }
        else if (getRandomNumber() == 4) { setRightTireState(State.FLAT); }
        else setMasterTireState(State.INFLATED);
    }
    public void updateAllStatesInTextArea()
    {
        textArea.append("Master Door is " + getMasterDoorLockState() + ".\n");
        textArea.append("Passenger Door is " + getPassengerDoorLockState() + ".\n");
        textArea.append("Left Door is " + getLeftDoorLockState() + ".\n");
        textArea.append("Right Door is " + getRightDoorLockState() + ".\n");
        textArea.append("Master Window is " + getMasterWindowState() + ".\n");
        textArea.append("Passenger Window is " + getPassengerWindowState() + ".\n");
        textArea.append("Left Window is " + getLeftWindowState() + ".\n");
        textArea.append("Right Window is " + getRightWindowState() + ".\n");
        textArea.append("Car is " + getPowerState() + ".\n");
        textArea.append("Trunk is " + getTrunkState() + ".\n");
        textArea.append("Alarm is " + getAlarmState() + ".\n");
        textArea.append("Master Tire is " + getMasterTireState() + ".\n");
        textArea.append("Passenger Tire is " + getPassengerTireState() + ".\n");
        textArea.append("Left Tire is " + getLeftTireState() + ".\n");
        textArea.append("Right Tire is " + getRightTireState() + ".\n");
    }
    public void printWindowStates(int clicks, int windowCounter)
    {
        if ((clicks == 1 || clicks == 2) && windowCounter == 0)
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window: " + masterWindowState);
            LOGGER.info("Passenger Window: " + passengerWindowState);
            LOGGER.info("Back Left Window: " + leftWindowState);
            LOGGER.info("Back Right Window: " + rightWindowState);
            LOGGER.info("----- End Window States -----");
        }
        else if (clicks == 1 && windowCounter > 0)
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window is " + windowCounter + "% " +  masterWindowState);
            LOGGER.info("Passenger Window: " + passengerWindowState);
            LOGGER.info("Back Left Window: " + leftWindowState);
            LOGGER.info("Back Right Window: " + rightWindowState);
            LOGGER.info("----- End Window States -----");
        }
        else
        {
            LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window is " + windowCounter + "% " +  masterWindowState);
            LOGGER.info("Passenger Window is " + windowCounter + "% " + passengerWindowState);
            LOGGER.info("Back Left Window is " + windowCounter + "% " + leftWindowState);
            LOGGER.info("Back Right Window is " + windowCounter + "% " +  rightWindowState);
            LOGGER.info("----- End Window States -----");
        }
        windowStatesPrinted = true;
    }
    public void printDoorStates()
    {
        LOGGER.info("----- Door States -----");
        LOGGER.info("Master door lock: " + masterDoorLockState);
        LOGGER.info("Passenger door lock: " + passengerDoorLockState);
        LOGGER.info("Back Left door lock: " + leftDoorLockState);
        LOGGER.info("Back Right doorLock: " + rightDoorLockState);
        LOGGER.info("----- End Door States -----");
    }
    /**
     * method to set constraints on
     */
    protected void addComponent(Component component, int gridy, int gridx, double gwidth, double gheight) {
        LOGGER.info("Inside addComponent()");
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = (int)Math.ceil(gwidth);
        constraints.gridheight = (int)Math.ceil(gheight); // (int)Math.ceil(gheight); *actual value of parameter is always 1
        getThisLayout().setConstraints(component, constraints);
        add(component);
        LOGGER.info("Component added.");
    } // end addComponent
    /** Returns an ImageIcon, or null if the path was invalid.
     * @param path the path of the image
     * @param description a description of the image being set
     */
    protected ImageIcon createImageIcon(String path, String description)
    {
        LOGGER.info("Inside createImageIcon(): creating image for " + description);
        ImageIcon retImageIcon = null;
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resource = classLoader.getResource(path.substring(19));
        if (resource != null) {
            retImageIcon = new ImageIcon(resource);
            LOGGER.info("the path '" + path + "' created the '"+description+"'! the ImageIcon is being returned...");
            LOGGER.info("End createImageIcon()");
        }
        else {
            LOGGER.debug("The path '" + path + "' could not find an image there!. Trying again by removing 'src/main/resources/' from path!");
            resource = classLoader.getResource(path.substring(19));
            if (resource != null) {
                retImageIcon = new ImageIcon(resource);
                LOGGER.info("the path '" + path + "' created an image after removing 'src/main/resources/'! the ImageIcon is being returned...");
                LOGGER.info("End createImageIcon()");
            } else {
                LOGGER.error("The path '" + path + "' could not find an image there after removing src/main/resources/!. Returning null!");
            }

        }
        return retImageIcon;
    }
    /**
     * Add all the components to the frame
     */
    protected void addComponents()
    {
        LOGGER.info("Adding components.");
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
     * This method sets the button functionalities
     */
    protected void setButtonFunctionalities()
    {
        LOGGER.info("Setting up all button functionalities....");
        powerButton.addMouseListener(new ButtonClicked(this) {});
        trunkButton.addMouseListener(new ButtonClicked(this) {});
        alarmButton.addMouseListener(new ButtonClicked(this) {});
        lockButton.addMouseListener (new ButtonClicked(this) {});
        clearButton.addMouseListener(new ButtonClicked(this) {});
        flatTireButton.addMouseListener(new ButtonClicked(this) {});
        LOGGER.info("Initializing window button listener with custom window listener");
        windowButton.addMouseListener(new WindowButtonHandler() {});
        LOGGER.info("Button functionalities are all set up.");
    }
    // Constructor
    public CarBeeperV2()
    {
        super("Car Beeper");
        LOGGER.info("Inside CarBeeperV2 constructor.");
        setDefaultValues();
        setFlatTireRandomizer();
        setThisLayout(new GridBagLayout());
        setConstraints(new GridBagConstraints());
        // Images
        setLockImage(createImageIcon("src/main/resources/images/lock.jpg", "Lock image"));
        setWindowImage(createImageIcon("src/main/resources/images/window.jpg", "Window image"));
        setPowerImage(createImageIcon("src/main/resources/images/power.jpg", "Power image"));
        setTrunkImage(createImageIcon("src/main/resources/images/trunk2.jpg", "Trunk image"));
        setAlarmImage(createImageIcon("src/main/resources/images/alarm.jpg", "Alarm image"));
        setFlatTireImage(createImageIcon("src/main/resources/images/flatTire.jpg", "Flat tire image"));
        // Buttons
        setLockButton(new JButton("Lock", getLockImage()));
        setWindowButton(new JButton("Window", getWindowImage()));
        setPowerButton(new JButton("Power", getPowerImage()));
        setTrunkButton(new JButton("Trunk", getTrunkImage()));
        setAlarmButton(new JButton("Alarm", getAlarmImage()));
        setFlatTireButton(new JButton("Flat tire", getFlatTireImage()));
        setClearButton(new JButton("Clear"));
        addComponents();
        LOGGER.info("End CarBeeperV2 constructor.");
    }

    /**
     * My WindowButtonHandler class takes care of any and all mouse clicks 
     * and/or holding that may occur.
     * Inside mousePressed...
     * Inside mouseReleased...
     * Inside mouseClicked...
     * Inside actionPerformed...
     */
    protected class WindowButtonHandler extends MouseAdapter implements ActionListener
    {
        protected final Logger LOGGER = LogManager.getLogger(WindowButtonHandler.class);
    	protected final Timer timer;
        protected final Timer beingHeld = new Timer(250, actionEvent -> {
            LOGGER.info("Inside beingHeld Timer actionEvent.");
            LOGGER.info("windowStatesPrinted = " + windowStatesPrinted);
        	if ((beingHeldTimer < 3 && !holding) )
        	{ // 0, 1, 2 < 3
                textArea.append(++beingHeldTimer + " Window button held: no\n");
                LOGGER.info(beingHeldTimer + " Timer is not being held.");
            }
        	else if (beingHeldTimer == 3)
        	{
                textArea.append(++counter2 + " Window button held: yes\n");
                LOGGER.info(counter2 + " Timer is being held.");
                holding = true;
                beingHeldTimer++;
            }
        	else if (beingHeldTimer > 3 && !windowStatesPrinted)
        	{
            	if (counter2 < 10)
            		textArea.setText(textArea.getText().substring(0, textArea.getText().length()-26) + ++counter2 + " Window button held: yes\n");
            	else if (counter2 <= 100) {
            		textArea.setText(textArea.getText().substring(0, textArea.getText().length()-27) + ++counter2 + " Window button held: yes\n");
            	} else {
            		LOGGER.error("counter2: " + counter2 + " shouldn't go any higher than 100.");
            	}
            	LOGGER.info(counter2 + " Timer is being held.");
            	holding = true;
            	beingHeldTimer++;
            }
        	else if (beingHeldTimer > 3 && windowStatesPrinted)
        	{
            	// or maybe other states have been printed, we just need the entire text area
            	if (counter2 < 10) {
            		String temp = textArea.getText(); //.substring(0, textArea.getText().length()-26);
            		textArea.setText(temp + ++counter2 + " Window button held: yes\n");
            	} else if (counter2 <= 100) {
            		textArea.setText(textArea.getText() + ++counter2 + " Window button held: yes\n");
            	} else {
            		LOGGER.error("counter2: " + counter2 + " shouldn't go any higher than 100.");
            	}
            	LOGGER.info(counter2 + " Timer is being held.");
            	windowStatesPrinted = false;
            	holding = true;
            	beingHeldTimer++;
            }
        	else
        	{
                LOGGER.error("Research unknown error");
            }
        	LOGGER.info("End beingHeld Timer actionEvent.");
        });
        protected int clicks = 0;
        public WindowButtonHandler() {
        	LOGGER.info("Inside WindowButtonHandler constructor");
            singleClick = false;
            doubleClick = false;
            timer = new Timer(TIMER_INTERVAL, this);
            LOGGER.info(this);
            LOGGER.info("End WindowButtonHandler constructor");
        }
        @Override
        public void mousePressed(MouseEvent e) {
            LOGGER.info("Inside mousePressed.");
            if (e.getClickCount() == 1) {
                LOGGER.info("Start beingHeld timer for singleClick.");
                beingHeld.start();
                singleClick = true;
                doubleClick = false;
                clicks = 1;
            }
            else {
                LOGGER.info("Start beingHeld timer for doubleClick.");
                beingHeld.start();
                singleClick = false;
                doubleClick = true;
                clicks = 2;
            }
            LOGGER.info("End mousePressed.");
        }
        @Override
        public void mouseReleased(MouseEvent e) {
            LOGGER.info("Inside mouseReleased.");
            beingHeld.stop();
            LOGGER.info("End mouseReleased.");
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            LOGGER.info("Inside mouseClicked...");
            if (doubleClick)
            {
                LOGGER.info("Double clicked");
                LOGGER.info("holding = " + holding + " | singleClick = " + !singleClick + " | clicks = " + clicks);
                if (holding && !singleClick)
                {
                    if (wasHeld)
                    {
                        LOGGER.info("We double clicked and held last time...");
                        getWindowStates(2, counter2);
                        holding = false;
                    }
                    else
                    {
                        LOGGER.info("We double clicked but we didn't hold it this time...");
                        windowUp_DownAll();
                        getWindowStates(2, counter2);
                        // set wasHeld to true and holding to false
                        wasHeld = true;
                        holding = false;
                    }
                }
                else if (!holding && !singleClick)
                {
                    if (wasHeld)
                    { // held on last round, clicked this round (reversing states and button logic)
                        LOGGER.info("We double clicked and held last time...");
                        getWindowStates(2, 0);
                        counter2 = 0;
                        beingHeldTimer = 0;
                        wasHeld = false;
                        windowStatesPrinted = false;
                        // if not holding (clicked) but wasHeld previously (true) (and previous click was a singleClick (true))
                    }
                    else
                    { // not holding this round (click) and didn't hold last round (click)
                    	LOGGER.info("We double clicked but didn't hold this time.");
                        windowUp_DownAll();
                        getWindowStates(2, counter2);
                        counter2 = 0;
                        holding = false;
                        windowStatesPrinted = false;
                    }
                }
                timer.stop();
            }
            else
            {
            	LOGGER.info("Single click.");
                timer.restart();
            }
            getWindowStates(clicks, counter2);
            setRandomNumber(new SecureRandom().nextInt(10));
            LOGGER.info("End mouseClicked.");
        }
        @Override
        public void actionPerformed(ActionEvent e)
        {
        	LOGGER.info("Running actionPerformed...");
        	LOGGER.info("holding = " + holding + " | singleClick = " + singleClick);
            if (holding && !doubleClick)
            {
                if (wasHeld)
                {
                	LOGGER.info("We single click and held last time.");
                    getWindowStates(1, counter2);
                    holding = false;
                    windowStatesPrinted = false;
                }
                else
                {
                	LOGGER.info("We single clicked and held it this time.");
                    windowUp_Down();
                    getWindowStates(1, counter2);
                    // set wasHeld to true and holding to false
                    wasHeld = true;
                    holding = false;
                }
            }
            else if (!holding && !doubleClick)
            {
                if (wasHeld)
                { // held on last round, clicked this round (reversing states and button logic)
                    LOGGER.info("We single click and held last time.");
                    //windowUp_Down();
                    getWindowStates(1, 0);
                    beingHeldTimer = 0;
                    counter2 = 0;
                    wasHeld = false;
                    windowStatesPrinted = false;
                    // if not holding (clicked) but wasHeld previously (true) (and previous click was a doubleClick (true))
                }
                else
                { // not holding this round (click) and didn't hold last round (click)
                	LOGGER.info("We single clicked and didn't hold it this time.");
                    windowUp_Down();
                    getWindowStates(1, counter2);
                    counter2 = 0;
                    holding = false;
                    beingHeldTimer = 0;
                    windowStatesPrinted = false;
                }
            }
            LOGGER.info("Timer for Window Button stopped.");
            timer.stop();
        }
        @Override
        public String toString() {
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
                LOGGER.info("Master Window: " + masterWindowState);
                LOGGER.info("Passenger Window: " + passengerWindowState);
                LOGGER.info("Back Left Window: " + leftWindowState);
                LOGGER.info("Back Right Window: " + rightWindowState);
                LOGGER.info("----- End Window States -----");
            }
            else if (clicks == 1 && windowCounter > 0)
            {
                LOGGER.info("----- Window States -----");
                LOGGER.info("Master Window is " + windowCounter + "% " +  masterWindowState);
                LOGGER.info("Passenger Window: " + passengerWindowState);
                LOGGER.info("Back Left Window: " + leftWindowState);
                LOGGER.info("Back Right Window: " + rightWindowState);
                LOGGER.info("----- End Window States -----");
            }
            else
            {
                LOGGER.info("----- Window States -----");
                LOGGER.info("Master Window is " + windowCounter + "% " +  masterWindowState);
                LOGGER.info("Passenger Window is " + windowCounter + "% " + passengerWindowState);
                LOGGER.info("Back Left Window is " + windowCounter + "% " + leftWindowState);
                LOGGER.info("Back Right Window is " + windowCounter + "% " +  rightWindowState);
                LOGGER.info("----- End Window States -----");
            }
            windowStatesPrinted = true;
        }
    }
}