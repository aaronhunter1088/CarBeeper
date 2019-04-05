package carbeeper;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.io.PrintStream;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.event.MouseEvent;
import static java.lang.System.exit;
import javax.swing.Timer;
import javax.swing.text.DefaultCaret;

public class CarBeeper extends JFrame {
    private static final long serialVersionUID = 1L;
	// Buttons
    private final JButton lockButton;
    private final JButton windowButton;
    private final JButton powerButton;
    private final JButton trunkButton;
    private final JButton alarmButton;
    private final JButton clearButton;
    
    public JTextArea textArea = new JTextArea("", 10, 1); // textArea of rows and columns
    
    // Layout and Constraints
    private final GridBagLayout layout;
    private final GridBagConstraints constraints; 
    
    // GUI Component
    public CarBeeper() {
        super("Car Beeper");
        setResizable(false);
        setVisible(true);
        layout = new GridBagLayout();
        setLayout(layout);
        constraints = new GridBagConstraints();
        
        // Icons
        Icon lock = new ImageIcon(getClass().getResource("lock.jpg"));
        Icon window = new ImageIcon(getClass().getResource("window.jpg"));
        Icon power = new ImageIcon(getClass().getResource("power.jpg"));
        Icon trunk = new ImageIcon(getClass().getResource("trunk.jpg"));
        Icon alarm = new ImageIcon(getClass().getResource("alarm.jpg"));
        
        // Buttons
        lockButton = new JButton(lock);
        windowButton = new JButton(window);
        powerButton = new JButton(power);
        trunkButton = new JButton(trunk);
        alarmButton = new JButton(alarm);
        clearButton = new JButton("Clear");
        
        // Adding lockButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(lockButton, 1, 1, 1, 1);
        
        // Adding windowButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(windowButton, 1, 3, 1, 1);
        
        // Adding powerButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(powerButton, 3, 1, 1, 1);
        
        // Adding trunkButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(trunkButton, 3, 2, 1, 1);
        
        // Adding alarmButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(alarmButton, 3, 3, 1, 1);
        
        // Adding clearButton
        constraints.fill = GridBagConstraints.BOTH;
        addComponent(clearButton, 6, 2, 1, 1);
        
        // Adding jTextArea for verification of actions
        
        textArea.setLineWrap(true); // wraps the text to the next line
        textArea.setWrapStyleWord(rootPaneCheckingEnabled); // ensures wrapping wraps full words
        DefaultCaret caret = (DefaultCaret)textArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
        addComponent(new JScrollPane(textArea), 5, 1, 3, 1);
        
        // Lock Button Functionality
        lockButton.addMouseListener(new MouseClickHandler() {
            boolean singleClick = true;
            boolean doubleClick = false;
            
            @Override
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    singleClick = false;
                    doubleClick = true;
                } else {
                    singleClick = true;
                    doubleClick = false;
                }
                
                Integer timerinterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
            
                Timer timer = new Timer(timerinterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (singleClick) {
                            System.out.println("Single click");
                            lock_Unlock();
                            textArea.append("Master Door lock is " + masterDoorState + "\n");
                        } else if (doubleClick) {
                            System.out.println("Double click");
                            lock_UnlockAll();
                            textArea.append("Master Door is " + masterDoorState + "\n");
                            textArea.append("Passenger Door is " + passengerDoorState + "\n");
                            textArea.append("Left Door is " + leftDoorState + "\n");
                            textArea.append("Right Door is " + rightDoorState + "\n");
                        }
                        singleClick = false;
                        doubleClick = false;
                    }
                });
            
                timer.setRepeats(false);
                timer.start();
                if (me.getID() == MouseEvent.MOUSE_RELEASED)
                    timer.stop();
            }
        });
        
        // Car Window Functionality
        windowButton.addMouseListener(new MouseClickHandler() {
            boolean singleClick = false;
            boolean doubleClick = false;
            boolean held = false;
            //boolean wasHeld = false;
            int counter1 = 0;
            int counter2 = 0; 
            
            // Used for press and hold on window button
            Timer timer1 = new Timer(250, new ActionListener() {
                @Override 
                public void actionPerformed(ActionEvent ae) {
                    counter1 += 1;
                    
                    if (counter1 > 3) {
                        if (counter1 == 4) System.out.println("Timer1 Counting");                        
                        //held = true;
                        counter2 += 1;
                        System.out.printf("Counter2: %d\n", counter2);
                        if (wasHeld == true) {
                            if (masterWindowState.equals(State.UP)) {
                                // Count up
                                if (masterWindow != 0) {
                                    masterWindow += 1;
                                }
                                else if (masterWindow >= 100)
                                    masterWindow = 100;
                            } 
                            else if (masterWindowState.equals(State.DOWN)) {
                                // Count down
                                if (masterWindow != 100) {
                                    masterWindow += 1;
                                }
                                else if (masterWindow >= 100)
                                    masterWindow = 100;
                                
                            }
                        } else if (wasHeld == false) {
                            /*if (masterWindowState.equals(State.UP)) {
                                if (masterWindow != 100) {
                                    masterWindow += 1;
                                }
                                else if (masterWindow >= 100)
                                    masterWindow = 100;
                                
                            } else if (masterWindowState.equals(State.DOWN)) { // Down = 100
                                if (masterWindow != 100) {
                                    masterWindow += 1; 
                                }
                                else if (masterWindow <= 100)
                                    masterWindow = 100;
                            } */
                            if (masterWindowState.equals(State.UP)) {
                                masterWindowState = State.DOWN;
                            }
                            else
                                masterWindowState = State.UP;
                            
                            wasHeld = true;
                        }
                    } else { // counter2 <= 3 
                        //held = false;
                        //wasHeld = false;
                        System.out.printf("%d not being held\n", counter1);
                    }      
                }
            });
            @Override
            public void mousePressed(MouseEvent me)
            {
                counter1 = 0;
                counter2 = 0;
                doubleClickAndHeld = true;
                if (me.getClickCount() == 1) {
                    System.out.println("Timer1 Starting");
                    timer1.start(); 
                }
                else {
                    System.out.println("Timer2 Starting");
                    timer1.start();
                }
            } 
            @Override
            public void mouseReleased(MouseEvent me)
            {
                // Try with: if (!timer1.isRunning()) {timer1.stop();}
                
                if (me.getClickCount() == 1)
                {
                    System.out.println("Timer1 Stopped");
                    timer1.stop();
                }
                else {
                    System.out.println("Timer2 Stopped");
                    timer1.stop();
                }
                    //System.out.println("End timer for two clicks here");
                
//                if (wasHeld == false)
//                {
//                    if (masterWindowState.equals("Up"))
//                        masterWindowState = "Down";
//                    else
//                        masterWindowState = "Up";
//                }    
                    
                if (counter1 > 3) {
                    held = true;
                    wasHeld = true;
                    doubleClickAndHeld = true;
                }
                if (counter1 <= 3) {
                    held = false;
                    wasHeld = false;
                    doubleClickAndHeld = false;
                }
                
                
            }
            
            @Override
            public void mouseClicked(MouseEvent me)
            {
//                if (me.getClickCount() == 3) 
//                {
//                    //System.out.println("Double Click");
//                    singleClick = false;
//                    doubleClick = false;
//                    tripleClick = true;
//                }
                if (me.getClickCount() == 2) 
                {
                    //System.out.println("Double Click");
                    singleClick = false;
                    doubleClick = true;
                    
                    //tripleClick = false;
                }
                else //if (me.getClickCount() == 1) 
                {
                    //System.out.println("Single Click");
                    //System.out.println(wasHeld);
                    //System.out.println(masterWindowState);
                    singleClick = true;
                    doubleClick = false;
                    //doubleClickAndHeld = false;
                    //tripleClick = false;
                }

                // Car Window Functionality
                Integer timerinterval = (Integer)Toolkit.getDefaultToolkit().
                        getDesktopProperty("awt.multiClickInterval");
                        
                Timer timer = new Timer(timerinterval, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent evt) {
                        if (singleClick == true && held == false && doubleClickAndHeld == false){
                            System.out.println("Single Click");
                            windowUp_Down();
                            // Try deleting line if two clicks were given
                            textArea.append("Master window is " + masterWindowState + "\n");
                            wasHeld = false;
                        }   
                        else if (singleClick == true && held == true)
                        {
                            System.out.println("Single Click + Hold");
                            if (masterWindowState.equals(State.UP))
                            {
                                textArea.append("Master window is " + masterWindow + "% " + masterWindowState + "\n");
                            }
                            else //if (masterWindowState.equals("Down"))
                            {
                                textArea.append("Master window is " + masterWindow + "% " + masterWindowState + "\n");
                            }
                            
                            wasHeld = true;
                        }
                        else if (doubleClick == true && held == false)
                        {
                            System.out.println("Double Click");
                            windowUp_DownAll();
                            textArea.append("Master window is " + masterWindowState + "\n");
                            textArea.append("Passenger window is " + passengerWindowState + "\n");
                            textArea.append("Left window is " + leftWindowState + "\n");
                            textArea.append("Right window is " + rightWindowState + "\n");
                            wasHeld = false;
                        }
                        else if (doubleClick == true && held == true && doubleClickAndHeld == true)
                        {
                            passengerWindow = masterWindow;
                            passengerWindowState = masterWindowState;
                            leftWindow = masterWindow;
                            leftWindowState = masterWindowState;
                            rightWindow = masterWindow;
                            rightWindowState = masterWindowState;
                        
                            System.out.println("Double Click + Hold\n");
                            // Not sure where it is resetting the values so I am 
                            // changing the states back here. 
                        
                        
                            if (masterWindowState.equals(State.UP))
                            {
                                //windowUp_DownAndHoldTwo();
                                textArea.append("Master window is " + masterWindow + "% Up\n");
                                textArea.append("Passenger window is " + passengerWindow + "% Up\n");
                                textArea.append("Left window is " + leftWindow + "% Up\n");
                                textArea.append("Right window is " + rightWindow + "% Up\n");
                            }
                            else if (masterWindowState.equals(State.DOWN))
                            {
                                //windowUp_DownAndHoldTwo();
                                textArea.append("Master window is " + masterWindow + "% Down\n");
                                textArea.append("Passenger window is " + passengerWindow + "% Down\n");
                                textArea.append("Left window is " + leftWindow + "% Down\n");
                                textArea.append("Right window is " + rightWindow + "% Down\n");
                            }
                            wasHeld = true;
                            doubleClickAndHeld = true;
                            //System.out.println(wasHeld);
                        }
                    
                        singleClick = false;
                        doubleClick = false;
                        held = false;
                        //wasHeld = true;
                    }
                });
            
                timer.setRepeats(false);
                timer.start();
                if (me.getID() == MouseEvent.MOUSE_RELEASED)
                    timer.stop();
            }
        });
        
        // Car Power Functionality
        powerButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me)
            {
            if (me.getSource() == powerButton && carState.equals(State.OFF))
            {
                carState = State.ON;
                textArea.append("Car is " + carState + "\n");
            }
            else if (me.getSource() == powerButton && carState.equals(State.ON))
            {
                carState = State.OFF;
                textArea.append("Car is " + carState + "\n");
            }
            }
        });
        
        // Car Trunk Functionality
        trunkButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if (me.getSource() == trunkButton && trunkState.equals(State.CLOSED))
                {
                    trunkState = State.OPEN;
                    textArea.append("Trunk is " + trunkState + "\n");
                }
                else if (me.getSource() == trunkButton && trunkState.equals(State.OPEN))
                {
                    trunkState = State.CLOSED;
                textArea.append("Trunk is " + trunkState + "\n");
                }
            }
        });
        
        // Car Alarm Functionality
        alarmButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me)
            {
                if (me.getSource() == alarmButton && alarmState.equals(State.OFF))
                {
                    alarmState = State.ON;
                    textArea.append("Alarm is " + alarmState + "\n");
                }
                else if (me.getSource() == alarmButton && alarmState.equals(State.ON))
                {
                    alarmState = State.OFF;
                    textArea.append("Alarm is " + alarmState + "\n");
                }
            }
        });
        
        // Clear Button Functionality
        clearButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) 
            {
                if (me.getSource() == clearButton)
                    textArea.setText(null);

            }
        });
    } // end GUI constructor
    
    // Mouse Click's
    private class MouseClickHandler extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent me) {}
        @Override
        public void mousePressed(MouseEvent me) {}
        @Override
        public void mouseReleased(MouseEvent me) {}
    }
    
    // method to set constraints on
    private void addComponent(Component component, 
            int gridy, int gridx, double gwidth, double gheight)
    {
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = (int)Math.ceil(gwidth);
        constraints.gridheight = (int)Math.ceil(gheight);
        layout.setConstraints(component, constraints);
        add(component);
    } // end addComponent 
    
    // Variables
    
    // Lock/Unlock Variables
    //int masterDoor = 0; // master door is unlocked
    //int passengerDoor = 0; // passenger door is unlocked
    //int leftDoor = 0; // left rear door is unlocked
    //int rightDoor = 0; // right rear door is unlocked
    //String masterDoorState = "Unlocked"; 
    //String passengerDoorState = "Unlocked";
    //String leftDoorState = "Unlocked";
    //String rightDoorState = "Unlocked";
    State masterDoorState = State.UNLOCKED;
    State passengerDoorState = State.UNLOCKED;
    State leftDoorState = State.UNLOCKED;
    State rightDoorState = State.UNLOCKED;
    
    // Windows Up/Down Variables
    //int masterWindowUp = 100; // master window is 100% rolled up 
    //int masterWindowDown = 0; // reflects master window status
    int masterWindow = 0; // left because there is incremental/decremental changes in window height!!!
    int passengerWindow = 0; // passenger window is 100% rolled up
    int leftWindow = 0; // left rear window is 100% rolled up 
    int rightWindow = 0; // right rear window is 100% rolled up
    //String masterWindowState = "Up"; // else "Down"
    //String passengerWindowState = "Up";
    //String leftWindowState = "Up";
    //String rightWindowState = "Up";
    static State masterWindowState = State.UP;
    static State passengerWindowState = State.UP;
    static State leftWindowState = State.UP;
    static State rightWindowState = State.UP;
    boolean wasHeld = false;
    boolean doubleClickAndHeld = true;
    
    // Car On/Off Variables
    //String carState = "Off";
    State carState = State.OFF;
    
    // Trunk Open/Closed Variables
    //String trunkState = "Closed";
    State trunkState = State.CLOSED;
    
    // Alarm On/Off Variables
    //String alarmState = "Off";
    State alarmState = State.OFF;
    
    // Methods
    
    // Functioning
    /**
     * Displays to the textArea the default values of the states
     * of each button. 
     */
    public void setBeeper()
    {
        textArea.append("Master Door is " + masterDoorState + ".\n");
        textArea.append("Passenger Door is " + passengerDoorState + ".\n");
        textArea.append("Left Door is " + leftDoorState + ".\n");
        textArea.append("Right Door is " + rightDoorState + ".\n");
        textArea.append("Master Window is " + masterWindowState + ".\n");
        textArea.append("Passenger Window is " + passengerWindowState + ".\n");
        textArea.append("Left Window is " + leftWindowState + ".\n");
        textArea.append("Right Window is " + rightWindowState + ".\n");
        textArea.append("Car is " + carState + ".\n");
        textArea.append("Trunk is " + trunkState + ".\n");
        textArea.append("Alarm is " + alarmState + ".\n\n");
    }
    
    /**
     * NOT FUNCTIONING IN THE CATCH FEATURE
     * @param state
     * @param door 
     */
    protected void setLock_UnlockState(State state, Door door) throws IllegalArgumentException {
        try {
            switch (door) {
                case MASTER:
                    masterDoorState = state;
                    break;
                case PASSENGER:
                    passengerDoorState = state;
                    break;
                case LEFT:
                    leftDoorState = state;
                    break;
                case RIGHT:
                    rightDoorState = state;
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            exit(1);
        }
    }
    
    // Door Lock/Unlock Methods
    /**
     * Returns the current status of all doors
     * @return 
     */
    public PrintStream getLock_UnlockState() {
        return System.out.printf(""
            + "Door States:\n"
            + "Master Door: \t%s.\n"
            + "Passenger Door: %s.\n"
            + "Left Door: \t%s.\n"
            + "Right Door: \t%s.\n", 
            masterDoorState, passengerDoorState, leftDoorState, rightDoorState);
    }
    
    // Functioning
    /**
     * This method locks or unlocks the master door,
     * which is also the drivers door; it only operates
     * on the master door. 
     */
    public void lock_Unlock() {
        if (masterDoorState.equals(State.UNLOCKED))
            masterDoorState = State.LOCKED; 
        else 
            masterDoorState = State.UNLOCKED; 
    }
    
    // Functioning
    /**
     * This method locks or unlocks the passenger door, and
     * both the back doors based on the current state of the 
     * master door; It takes one variable: a Boolean that tests
     * whether or not the masterDoor was just locked or not.
     */
    public void lock_UnlockAll() {
        lock_Unlock();
        if (!passengerDoorState.equals(masterDoorState)) {
            passengerDoorState = masterDoorState;
        } if (!leftDoorState.equals(masterDoorState)) {
            leftDoorState = masterDoorState;
        } if (!rightDoorState.equals(masterDoorState)) {
            rightDoorState = masterDoorState;
        }
    }
    
    // Windows Up/Down Methods
    /**
     * This method simply returns the current
     * state of all the windows.
     * @return 
     */
    public PrintStream getWindowState() {
        return System.out.printf(""
            + "Window States:\n"
            + "Master Window: \t%s.\n"
            + "Passenger Window: %s.\n"
            + "Left Window: \t%s.\n"
            + "Right Window: \t%s.\n", 
            masterWindowState, passengerWindowState, leftWindowState, rightWindowState);
    }
    
    public PrintStream getLockState() {
        return System.out.printf(""
            + "Door Lock States:\n"
            + "Master Lock: \t%s\n"
            + "Passenger Lock: %s\n"
            + "Left Lock: \t%s\n"
            + "Right Lock: \t%s\no",
            masterDoorState, passengerDoorState, leftDoorState, rightDoorState);
    }
    
    // Functioning
    /**
     * This method will roll up or down the master door window. 
     */
    public static void windowUp_Down() {
        if (masterWindowState.equals(State.UP)) {
            masterWindowState = State.DOWN; 
        } else {
            masterWindowState = State.UP; 
        }
    }
    
    // Functioning
    /**
     * This method rolls up or down the passenger door, and
     * both the back door windows based on the current state of the 
     * master door window; It takes one variable: a Boolean that tests
     * whether or not the master window was just rolled up or not.
     */
    public static void windowUp_DownAll() {
        windowUp_Down();
        if (!passengerWindowState.equals(masterWindowState))
            passengerWindowState = masterWindowState;
        if (!leftWindowState.equals(masterWindowState))
            leftWindowState = masterWindowState;
        if (!rightWindowState.equals(masterWindowState))
            rightWindowState = masterWindowState;
    }
    
    // Car On/Off Methods
    /**
     * This method returns the state of the car:
     * Car is + carState
     * @return 
     */
    public PrintStream getCarState() {
        return System.out.printf("Car is: %s.\n", carState);
    }
    
    /**
     * This method sets the car state.
     */
    protected void setCarState() {
        if (carState.equals(State.OFF))
            carState = State.ON; 
        else
            carState = State.OFF; 
    }
    
    // Trunk Open/Close Methods
    /**
     * This method returns the state of the trunk:
     * Trunk is + trunkState
     * @return 
     */
    public PrintStream getTrunkState() {
        return System.out.printf("Trunk is: %s.\n", trunkState);
    }
    
    // Alarm On/Off Methods
    /**
     * This method returns the state of the alarm.
     * Alarm is + alarmState.
     * @return 
     */
    public PrintStream getAlarmState() {
        if (alarmState.equals(State.ON))
            alarmState = State.OFF;
        else
            alarmState = State.ON;
        return System.out.printf("Alarm is: %s.\n", alarmState);
    }
}

