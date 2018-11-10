package carbeeper;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.PrintStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;

/**
 *
 * @author aaron
 */
public class CarBeeperV2 extends JFrame {
    // Buttons
    private final JButton lockButton;
    private final JButton windowButton;
    private final JButton powerButton;
    private final JButton trunkButton;
    private final JButton alarmButton;
    private final JButton clearButton;
    // Images for the Buttons
    Icon lockImage = new ImageIcon(getClass().getResource("lock.jpg"));
    Icon windowImage = new ImageIcon(getClass().getResource("window.jpg"));
    Icon powerImage = new ImageIcon(getClass().getResource("power.jpg"));
    Icon trunkImage = new ImageIcon(getClass().getResource("trunk2.jpg"));
    Icon alarmImage = new ImageIcon(getClass().getResource("alarm.jpg"));
    // Button States
    private State carState;
    private State trunkState;
    private State alarmState;
    private State masterDoorLockState;
    private State passengerDoorLockState;
    private State leftDoorLockState;
    private State rightDoorLockState;
//    private State masterDoorState = State.CLOSED; // not used in this version
//    private State passengerDoorState = State.CLOSED;
//    private State leftDoorState = State.CLOSED;
//    private State rightDoorState = State.CLOSED;
    private State masterWindowState;
    private State passengerWindowState;
    private State leftWindowState;
    private State rightWindowState;
    // TextArea
    public final JTextArea textArea = new JTextArea("", 10, 1); // textArea of rows and columns
    // Layout and Constraints
    private final GridBagLayout layout;
    private final GridBagConstraints constraints; 
    // Other Attributes
    private boolean wasHeld = false;
    private boolean holding = false;
    private boolean singleClick = false, doubleClick = false;
    private int beingHeldTimer = 0;
    private int counter2 = 0;
    // GUI Component
    public CarBeeperV2() {
        super("Car Beeper");
        setBeeper();
        setResizable(false);
//        setVisible(true);
        layout = new GridBagLayout();
        setLayout(layout);
        constraints = new GridBagConstraints();
        // Buttons
        lockButton = new JButton(lockImage);
        windowButton = new JButton(windowImage);
        powerButton = new JButton(powerImage);
        trunkButton = new JButton(trunkImage);
        alarmButton = new JButton(alarmImage);
        clearButton = new JButton("Clear");
        // Adding lockButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(lockButton, 1, 1, 1, 1); // row, column, size, size
        // Adding windowButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(windowButton, 1, 3, 1, 1); // row, column, size, size
        // Adding powerButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(powerButton, 2, 1, 1, 1); // row, column, size, size
        // Adding trunkButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(trunkButton, 2, 2, 1, 1); // row, column, size, size
        // Adding alarmButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(alarmButton, 2, 3, 1, 1); // row, column, size, size
        // Adding clearButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(clearButton, 6, 2, 1, 1); // row, column, size, size
        // Adding jTextArea for verification of actions
        
        addComponent(new JScrollPane(textArea), 5, 1, 3, 1); // row, column, size, size
        // Lock Button Functionality
        lockButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                //System.out.println("\nInside mouseClicked...");
                Integer timerinterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
                if (me.getClickCount() == 2) {
                    singleClick = false;
                    doubleClick = true;
                } else {
                    singleClick = true;
                    doubleClick = false;
                }
                Timer timer = new Timer(timerinterval, evt -> {
                    System.out.println("Timer for Lock Button started.");
                    if (singleClick) {
                        //System.out.println("Single click");
                        lock_Unlock();
                        getLockState();
                    } else if (doubleClick) {
                        //System.out.println("Double click");
                        lock_UnlockAll();
                        getLockState();
                    }
                    singleClick = false;
                    doubleClick = false;
                });
                timer.setRepeats(false);
                timer.start();
//                if (me.getID() == 500) {
//                    System.out.println("Timer for Lock Button stopped.");
//                    timer.stop();
//                }
            }
        });
        // Window Button Functionality
        windowButton.addMouseListener(new WindowButtonHandler() {});
        // Car Power Functionality 100%
        powerButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                System.out.println(me.getComponent());
                if (me.getSource() == powerButton && carState.equals(State.OFF)) {
                    carState = State.ON;
                    textArea.append("Car is " + carState + "\n");
                    getPowerState();
                } else if (me.getSource() == powerButton && carState.equals(State.ON)) {
                    carState = State.OFF;
                    textArea.append("Car is " + carState + "\n");
                    getPowerState();
                }
            }
        });
        // Car Trunk Functionality
        trunkButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getSource() == trunkButton && trunkState.equals(State.CLOSED)) {
                    trunkState = State.OPEN;
                    textArea.append("Trunk is " + trunkState + "\n");
                    getTrunkState();
                } else if (me.getSource() == trunkButton && trunkState.equals(State.OPEN)) {
                    trunkState = State.CLOSED;
                    textArea.append("Trunk is " + trunkState + "\n");
                    getTrunkState();
                }
            }
        });
        // Car Alarm Functionality
        alarmButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getSource() == alarmButton && alarmState.equals(State.OFF)) {
                    alarmState = State.ON;
                    textArea.append("Alarm is " + alarmState + "\n");
                    getAlarmState();
                } else if (me.getSource() == alarmButton && alarmState.equals(State.ON)) {
                    alarmState = State.OFF;
                    textArea.append("Alarm is " + alarmState + "\n");
                    getAlarmState();
                }
            }
        });
        // Clear Button Functionality
        clearButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getSource() == clearButton) {
                    textArea.setText(null);
                    //System.out.println("Textarea cleared.");
                }
            }
        });
    } // end GUI constructor
    
    /**
     * Displays to the textArea the default values of the states of each button. 
     */
    public void setBeeper()
    {
        carState = State.OFF;
        trunkState = State.CLOSED;
        alarmState = State.OFF;
        masterDoorLockState = State.UNLOCKED;
        passengerDoorLockState = State.UNLOCKED;
        leftDoorLockState = State.UNLOCKED;
        rightDoorLockState = State.UNLOCKED;
        masterWindowState = State.UP;
        passengerWindowState = State.UP;
        leftWindowState = State.UP;
        rightWindowState = State.UP;
        textArea.setLineWrap(true); // wraps the text to the next line
        textArea.setWrapStyleWord(rootPaneCheckingEnabled); // ensures wrapping wraps full words
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        textArea.append("Master Door is " + masterDoorLockState + ".\n");
        textArea.append("Passenger Door is " + passengerDoorLockState + ".\n");
        textArea.append("Left Door is " + leftDoorLockState + ".\n");
        textArea.append("Right Door is " + rightDoorLockState + ".\n");
        textArea.append("Master Window is " + masterWindowState + ".\n");
        textArea.append("Passenger Window is " + passengerWindowState + ".\n");
        textArea.append("Left Window is " + leftWindowState + ".\n");
        textArea.append("Right Window is " + rightWindowState + ".\n");
        textArea.append("Car is " + carState + ".\n");
        textArea.append("Trunk is " + trunkState + ".\n");
        textArea.append("Alarm is " + alarmState + ".\n\n");
    }
    
    private class MouseClickHandler extends MouseAdapter { //implements ActionListener {
        public MouseClickHandler() {}
        @Override
        public void mouseEntered(MouseEvent me) {}
        @Override
        public void mouseExited(MouseEvent me) {}
        @Override
        public void mousePressed(MouseEvent event) {}
        @Override
        public void mouseClicked(MouseEvent me)  {}
        @Override
        public void mouseReleased(MouseEvent me) {}
//        @Override
//        public void actionPerformed(ActionEvent e) {}
    }
    
    /**
     * My WindowButtonHandler class takes care of any and all mouse clicks 
     * and/or holding that may occur.
     * Inside mousePressed...
     * Inside mouseReleased...
     * Inside mouseClicked...
     * Inside actionPerformed...
     */
    private class WindowButtonHandler extends MouseAdapter implements ActionListener {
        private final int TIMER_INTERVAL = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
        private final Timer timer;
        private final Timer beingHeld = new Timer(250, actionEvent -> {
            if ((beingHeldTimer < 3 && holding == false) ) { // 0, 1, 2 < 3
                textArea.append(++beingHeldTimer + " Window button not being held...\n");
            } else if (beingHeldTimer >= 3) {
                textArea.append(++counter2 + " Window button is being held...\n");
                holding = true;
                beingHeldTimer++;
            } else {
                System.out.println("Research unknown error");
            }
        });
        
        public WindowButtonHandler() {
            singleClick = false;
            doubleClick = false;
            timer = new Timer(TIMER_INTERVAL, this);
        }
        
        @Override
        public void mousePressed(MouseEvent e) {
            System.out.println("\nInside mousePressed...");
            if (e.getClickCount() == 1) {
                //System.out.println("Start beingHeld timer for singleClick...");
                beingHeld.start();
                singleClick = true;
                doubleClick = false;
            } else {
                //System.out.println("Start beingHeld timer for doubleClick...");
                beingHeld.start();
                singleClick = false;
                doubleClick = true;
            }
        }
        
        @Override
        public void mouseReleased(MouseEvent e) {
            System.out.println("Inside mouseReleased...");
            beingHeld.stop();
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            System.out.println("Inside mouseClicked...");
            if (doubleClick) {
                //System.out.println("Double click");
                if (holding == true && !singleClick) {
                    if (wasHeld == true) {
                        //System.out.println("We double click and held last time...");
                        getWindowStates(2, counter2);
                        holding = false;
                    } else {
                        //System.out.println("Perform logic of holding here...");
                        //singleClick(previousEvent);
                        windowUp_DownAll();
                        getWindowStates(2, counter2);
                        // set wasHeld to true and holding to false
                        wasHeld = true;
                        holding = false;
                    }
                } else if (holding == false && !singleClick) {
                    if (wasHeld == true) { // held on last round, clicked this round (reversing states and button logic)
                        //System.out.println("We double click and held last time...");
                        //windowUp_DownAll();
                        getWindowStates(2, 0);
                        counter2 = 0;
                        beingHeldTimer = 0;
                        wasHeld = false;
                        // if not holding (clicked) but wasHeld previously (true) (and previous click was a singleClick (true))
                    } else { // not holding this round (click) and didn't hold last round (click)
                        //System.out.println("Perform logic of double click here...");
                        windowUp_DownAll();
                        getWindowStates(2, counter2);
                        counter2 = 0;
                        holding = false;
                        //wasHeld = false;
                    }
                }
                timer.stop();
            } else {
                System.out.println("Single click");
                timer.restart();
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e){
            System.out.println("Running actionPerformed...");
            if (holding == true && !doubleClick) {
                if (wasHeld == true) {
                    //System.out.println("We single click and held last time...");
                    getWindowStates(1, counter2);
                    holding = false;
                    // if doubleClick == true
                    // getWindowStates(2, counter2, true);
                    // holding = false
                } else {
                    //System.out.println("Perform logic of holding here...");
                    //singleClick(previousEvent);
                    windowUp_Down();
                    getWindowStates(1, counter2);
                    // set wasHeld to true and holding to false
                    wasHeld = true;
                    holding = false;
                }
            } else if (holding == false && !doubleClick) {
                if (wasHeld == true) { // held on last round, clicked this round (reversing states and button logic)
                    //System.out.println("We single click and held last time...");
                    //windowUp_Down();
                    getWindowStates(1, 0);
                    beingHeldTimer = 0;
                    counter2 = 0;
                    wasHeld = false;
                    // if not holding (clicked) but wasHeld previously (true) (and previous click was a doubleClick (true))
                } else { // not holding this round (click) and didn't hold last round (click)
                    //System.out.println("Perform logic of single click here...");
                    windowUp_Down();
                    getWindowStates(1, counter2);
                    counter2 = 0;
                    holding = false;
                    //wasHeld = false;
                }
            }
            timer.stop();
        }
    }
    // method to set constraints on
    private void addComponent(Component component, int gridy, int gridx, double gwidth, double gheight) {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = (int)Math.ceil(gwidth);
        constraints.gridheight = (int)Math.ceil(gheight);
        layout.setConstraints(component, constraints);
        add(component);
    } // end addComponent 
    /**
     * This method locks or unlocks the master door,
     * which is also the drivers door; it only operates
     * on the master door. 
     */
    public void lock_Unlock() {
        if (masterDoorLockState.equals(State.UNLOCKED))
            masterDoorLockState = State.LOCKED; 
        else 
            masterDoorLockState = State.UNLOCKED; 
    }
    /**
     * This method locks or unlocks the passenger door, and
     * both the back doors based on the current state of the 
     * master door; It takes one variable: a Boolean that tests
     * whether or not the masterDoor was just locked or not.
     */
    public void lock_UnlockAll() {
        lock_Unlock();
        if (!passengerDoorLockState.equals(masterDoorLockState)) { 
            passengerDoorLockState = masterDoorLockState;
        } if (!leftDoorLockState.equals(masterDoorLockState)) {
            leftDoorLockState = masterDoorLockState;
        } if (!rightDoorLockState.equals(masterDoorLockState)) {
            rightDoorLockState = masterDoorLockState;
        }
    }
    /**
     * This method will roll up or down the master door window. 
     */
    public void windowUp_Down() {
        if (masterWindowState.equals(State.UP)) {
            masterWindowState = State.DOWN; 
        } else {
            masterWindowState = State.UP; 
        }
    }
    /**
     * This method rolls up or down the passenger door, and
     * both the back door windows based on the current state of the 
     * master door window; It takes one variable: a Boolean that tests
     * whether or not the master window was just rolled up or not.
     */
    public void windowUp_DownAll() {
        windowUp_Down();
        if (!passengerWindowState.equals(masterWindowState)) {
            passengerWindowState = masterWindowState;
        }if (!leftWindowState.equals(masterWindowState)) {
            leftWindowState = masterWindowState;
        } if (!rightWindowState.equals(masterWindowState)) {
            rightWindowState = masterWindowState;
        }
    }
    // State Methods
    /**
     * This method returns the state of the car.
     * @return 
     */
    public State getPowerState() {
        System.out.printf("Car is: %s\n", carState);
        return carState;
    }
    /**
     * This method returns the state of the trunk.
     * @return 
     */
    public State getTrunkState() {
        System.out.printf("Trunk is: %s\n", trunkState);
        return trunkState;
    }
    /**
     * This method returns the state of the alarm.
     * @return 
     */
    public State getAlarmState() {
        System.out.printf("Alarm is: %s\n", alarmState);
        return alarmState;
    }
    /**
     * This method simply returns the current state of all the windows.
     * @return 
     */
    public PrintStream getLockState() {
        textArea.append("\nMaster Door lock is " + masterDoorLockState + "\n");
        textArea.append("Passenger Door lock is " + passengerDoorLockState + "\n");
        textArea.append("Left Door lock is " + leftDoorLockState + "\n");
        textArea.append("Right Door lock is " + rightDoorLockState + "\n");
        return System.out.printf(""
            + "Door Lock States:\n"
            + "Master door lock: \t%s\n"
            + "Passenger door lock: \t%s\n"
            + "Left door lock: \t%s\n"
            + "Right door lock: \t%s\n",
            masterDoorLockState, passengerDoorLockState, leftDoorLockState, rightDoorLockState);
    }
    /**
     * This method returns the master door lock state
     * @return
     */
    public State getMasterDoorLockState() {
        System.out.println("Master door lock state is : " + masterDoorLockState);
        return masterDoorLockState;
    }
    
    /**
     * This method is only used for testing purposes only.
     * @param state 
     */
    protected void setMasterDoorLockState(State state) {
        this.masterDoorLockState = state;
    }
    
    /**
     * This method returns the passenger door lock state
     * @return 
     */
    public State getPassengerDoorLockState() {
        System.out.println("Passenger door lock is : " + passengerDoorLockState);
        return passengerDoorLockState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setPassengerDoorLockState(State state) {
        this.passengerDoorLockState = state;
    }
    
    /**
     * This method returns the left door lock state
     * @return 
     */
    public State getLeftDoorLockState() {
        System.out.println("Left door lock is : " + leftDoorLockState);
        return leftDoorLockState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setLeftDoorLockState(State state) {
        this.leftDoorLockState = state;
    }
    
    /**
     * This method returns the right door lock state
     * @return 
     */
    public State getRightDoorLockState() {
        System.out.println("Right door lock is : " + rightDoorLockState);
        return rightDoorLockState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setRightDoorLockState(State state) {
        this.rightDoorLockState = state;
    }
    
    /**
     * This method simply returns the current state of all the windows.
     * @param clicks : integer; one click or two
     * @param windowCounter : counter2
     * @return 
     */
    public PrintStream getWindowStates(int clicks, int windowCounter) {
        if ((clicks == 1 || clicks == 2) && windowCounter == 0) { 
            textArea.append("\nMaster window is " + masterWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n");
            return System.out.printf(
            "Window States:\n"
            + "Master Window: \t\t%s\n"
            + "Passenger Window: \t%s\n"
            + "Left Window: \t\t%s\n"
            + "Right Window: \t\t%s\n",
            masterWindowState, passengerWindowState, leftWindowState, rightWindowState);
        } else if (clicks == 1 && windowCounter > 0) {
            textArea.append("\nMaster window is " + windowCounter + "% " + masterWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n");
            return System.out.printf(
            "Window States:\n"
            + "Master Window is \t%d%% %s\n"
            + "Passenger Window: \t%s\n"
            + "Left Window: \t\t%s\n"
            + "Right Window: \t\t%s\n",
            windowCounter, masterWindowState, passengerWindowState, leftWindowState, rightWindowState);
        } else { // clicks == 2 && windowCounter > 0
            textArea.append("\nMaster window is " + windowCounter + "% " + masterWindowState + "\n");
            textArea.append("Passenger window is " + windowCounter + "% " + passengerWindowState + "\n");
            textArea.append("Left window is " + windowCounter + "% " + leftWindowState + "\n");
            textArea.append("Right window is " + windowCounter + "% " + rightWindowState + "\n");
            return System.out.printf(
            "Window States:\n"
            + "Master Window is \t%d%% %s\n"
            + "Passenger Window is \t%d%% %s\n"
            + "Left Window is \t\t%d%% %s\n"
            + "Right Window is \t%d%% %s\n",
            windowCounter, masterWindowState, windowCounter, passengerWindowState,
            windowCounter, leftWindowState, windowCounter, rightWindowState);
        } 
    }
    /**
     * This method returns the master window state
     * @return
     */
    protected State getMasterWindowState() {
        System.out.println("Master window is : " + masterWindowState);
        return masterWindowState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setMasterWindowState(State state) {
        this.masterWindowState = state;
    }
    
    /**
     * This method returns the passenger door window state
     * @return
     */
    public State getPassengerWindowState() {
        System.out.println("Passenger window is : " + passengerWindowState);
        return passengerWindowState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setPassengerWindowState(State state) {
        this.passengerWindowState = state;
    }
    
    /**
     * This method returns the left door window state
     * @return
     */
    public State getLeftWindowState() {
        System.out.println("Left window is : " + leftWindowState);
        return leftWindowState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setLeftWindowState(State state) {
        this.leftWindowState = state;
    }
    
    /**
     * This method returns the right door window state
     * @return
     */
    public State getRightWindowState() {
        System.out.println("Right window is : " + rightWindowState);
        return rightWindowState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setRightWindowState(State state) {
        this.rightWindowState = state;
    }
    
    /**
     * This method returns the text area
     * @return
     */
    public JTextArea getTextArea() {
        return textArea;
    }
}