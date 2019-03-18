package carbeeper;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
//import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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
 *
 * @author aaron hunter
 */
public class CarBeeperV2 extends JFrame {
    private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = LogManager.getLogger(CarBeeperV2.class);
	private final int TIMER_INTERVAL = 500;
	// Buttons
    private final JButton lockButton;
    private final JButton windowButton;
    private final JButton powerButton;
    private final JButton trunkButton;
    private final JButton alarmButton;
    private final JButton clearButton;
    // Images for the Buttons
    private Icon lockImage;
    private Icon windowImage;
    private Icon powerImage;
    private Icon trunkImage;
    private Icon alarmImage;
    // Button States
    private State carState;
    private State trunkState;
    private State alarmState;
    private State masterDoorLockState;
    private State passengerDoorLockState;
    private State leftDoorLockState;
    private State rightDoorLockState;
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
    private boolean singleClick = false, doubleClick = false, windowStatesPrinted = false;
    private int beingHeldTimer = 0;
    private int counter2 = 0;
    // GUI Component
    public CarBeeperV2() throws IOException {
        super("Car Beeper");
        LOGGER.info("Inside CarBeeperV2 constructor.");
        setBeeper();
        setResizable(false);
        setLayout(layout = new GridBagLayout());
        constraints = new GridBagConstraints();
        // Images
        lockImage = createImageIcon("resources/images/lock.jpg");
        windowImage = createImageIcon("resources/images/window.jpg");
        powerImage = createImageIcon("resources/images/power.jpg");
        trunkImage = createImageIcon("resources/images/trunk2.jpg");
        alarmImage = createImageIcon("resources/images/alarm.jpg");
        // Buttons
        lockButton = new JButton(lockImage);
        windowButton = new JButton(windowImage);
        powerButton = new JButton(powerImage);
        trunkButton = new JButton(trunkImage);
        alarmButton = new JButton(alarmImage);
        clearButton = new JButton("Clear");
        LOGGER.info("Adding components.");
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
        LOGGER.info("Initializing lock button listener.");
        lockButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                LOGGER.info("Inside mouseClicked.");
                //Integer timerinterval = (Integer)Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
                if (me.getClickCount() == 2) {
                    singleClick = false;
                    doubleClick = true;
                } else {
                    singleClick = true;
                    doubleClick = false;
                }
                LOGGER.info("Timer for Lock Button started.");
                this.timer.start();
                LOGGER.info("End mouseClicked.");
            }
        });
        LOGGER.info("Initializing window button listener with custom window listener");
        windowButton.addMouseListener(new WindowButtonHandler() {});
        LOGGER.info("Initializing power button listener.");
        powerButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
                LOGGER.info("Inside mouseClicked.");
                if (me.getSource() == powerButton && carState.equals(State.OFF)) {
                    carState = State.ON;
                    textArea.append("Car is " + carState + "\n");
                    getPowerState();
                } else if (me.getSource() == powerButton && carState.equals(State.ON)) {
                    carState = State.OFF;
                    textArea.append("Car is " + carState + "\n");
                    getPowerState();
                }
                LOGGER.info("End mouseClicked.");
            }
        });
        LOGGER.info("Initializing trunk button listener.");
        trunkButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
            	LOGGER.info("Inside mouseClicked.");
            	if (me.getSource() == trunkButton && trunkState.equals(State.CLOSED)) {
                    trunkState = State.OPEN;
                    textArea.append("Trunk is " + trunkState + "\n");
                    getTrunkState();
                } else if (me.getSource() == trunkButton && trunkState.equals(State.OPEN)) {
                    trunkState = State.CLOSED;
                    textArea.append("Trunk is " + trunkState + "\n");
                    getTrunkState();
                }
            	LOGGER.info("End mouseClicked.");
            }
        });
        LOGGER.info("Initializing alarm button listener");
        alarmButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
            	LOGGER.info("Inside mouseClicked.");
            	if (me.getSource() == alarmButton && alarmState.equals(State.OFF)) {
                    alarmState = State.ON;
                    textArea.append("Alarm is " + alarmState + "\n");
                    getAlarmState();
                } else if (me.getSource() == alarmButton && alarmState.equals(State.ON)) {
                    alarmState = State.OFF;
                    textArea.append("Alarm is " + alarmState + "\n");
                    getAlarmState();
                }
                LOGGER.info("End mouseClicked.");
            }
        });
        // Clear Button Functionality
        clearButton.addMouseListener(new MouseClickHandler() {
            @Override
            public void mouseClicked(MouseEvent me) {
            	LOGGER.info("Inside mouseClicked.");
                if (me.getSource() == clearButton) {
                    textArea.setText("");
                    windowStatesPrinted = false;
                    LOGGER.info("windowStatesPrinted = false. Textarea cleared.");
                }
                LOGGER.info("End mouseClicked.");
            }
        });
        LOGGER.info("End CarBeeperV2 constructor.");
    } // end GUI constructor
    
    /**
     * Displays to the textArea the default values of the states of each button. 
     */
    public void setBeeper() {
    	LOGGER.info("Inside setBeeper(). Setting initial states...");
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
        LOGGER.info("End setBeeper()");
    }
    
    private class MouseClickHandler extends MouseAdapter { //implements ActionListener {
        private final Logger LOGGER = LogManager.getLogger(MouseClickHandler.class);
    	protected Timer timer = null;
    	public MouseClickHandler() {
    		LOGGER.info("Inside MouseClickHandler constructor.");
    		timer = new Timer(TIMER_INTERVAL, evt -> {
                if (singleClick) {
                	LOGGER.info("Single click");
                    lock_Unlock();
                    getLockState();
                } else if (doubleClick) {
                	LOGGER.info("Double click");
                    lock_UnlockAll();
                    getLockState();
                }
                singleClick = false;
                doubleClick = false;
                LOGGER.info("Timer's event set for " + TIMER_INTERVAL + " seconds.");
            });
    		timer.setRepeats(false);
            timer.setDelay(TIMER_INTERVAL + TIMER_INTERVAL);
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
        //private final int TIMER_INTERVAL = (Integer) Toolkit.getDefaultToolkit().getDesktopProperty("awt.multiClickInterval");
        private final Logger LOGGER = LogManager.getLogger(WindowButtonHandler.class);
    	private final Timer timer;
        private final Timer beingHeld = new Timer(250, actionEvent -> {
            LOGGER.info("Inside beingHeld Timer actionEvent.");
            LOGGER.info("windowStatesPrinted = " + windowStatesPrinted);
        	if ((beingHeldTimer < 3 && holding == false) ) { // 0, 1, 2 < 3
                textArea.append(++beingHeldTimer + " Window button held: no\n");
                LOGGER.info(beingHeldTimer + " Timer is not being held.");
            } else if (beingHeldTimer == 3) {
                textArea.append(++counter2 + " Window button held: yes\n");
                LOGGER.info(counter2 + " Timer is being held.");
                holding = true;
                beingHeldTimer++;
            } else if (beingHeldTimer > 3 && !windowStatesPrinted) {
            	if (counter2 < 10)
            		textArea.setText(textArea.getText().substring(0, textArea.getText().length()-26) + ++counter2 + " Window button held: yes\n");
            	else if (counter2 < 100 && counter2 >= 10) {
            		textArea.setText(textArea.getText().substring(0, textArea.getText().length()-27) + ++counter2 + " Window button held: yes\n");
            	} else if (counter2 > 100) {
            		LOGGER.error("counter2: " + counter2 + " shouldn't go any higher than 100.");
            	}
            	LOGGER.info(counter2 + " Timer is being held.");
            	holding = true;
            	beingHeldTimer++;
            } else if (beingHeldTimer > 3 && windowStatesPrinted) {
            	// or maybe other states have been printed, we just need the entire text area
            	if (counter2 < 10) {
            		String temp = textArea.getText(); //.substring(0, textArea.getText().length()-26);
            		textArea.setText(temp + ++counter2 + " Window button held: yes\n");
            	} else if (counter2 < 100 && counter2 >= 10) {
            		textArea.setText(textArea.getText() + ++counter2 + " Window button held: yes\n");
            	} else if (counter2 > 100) {
            		LOGGER.error("counter2: " + counter2 + " shouldn't go any higher than 100.");
            	}
            	LOGGER.info(counter2 + " Timer is being held.");
            	windowStatesPrinted = false;
            	holding = true;
            	beingHeldTimer++;
            } else {
                LOGGER.error("Research unknown error");
            }
        	LOGGER.info("End beingHeld Timer actionEvent.");
        });
        
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
            } else {
                LOGGER.info("Start beingHeld timer for doubleClick.");
                beingHeld.start();
                singleClick = false;
                doubleClick = true;
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
            if (doubleClick) {
                LOGGER.info("Double clicked");
                LOGGER.info("holding = " + holding + " | singleClick = " + !singleClick);
                if (holding == true && !singleClick) {
                    if (wasHeld == true) {
                        LOGGER.info("We double clicked and held last time...");
                        getWindowStates(2, counter2);
                        holding = false;
                    } else {
                        LOGGER.info("We double clicked but we didn't hold it this time...");
                        windowUp_DownAll();
                        getWindowStates(2, counter2);
                        // set wasHeld to true and holding to false
                        wasHeld = true;
                        holding = false;
                    }
                } else if (holding == false && !singleClick) {
                    if (wasHeld == true) { // held on last round, clicked this round (reversing states and button logic)
                        LOGGER.info("We double clicked and held last time...");
                        getWindowStates(2, 0);
                        counter2 = 0;
                        beingHeldTimer = 0;
                        wasHeld = false;
                        windowStatesPrinted = false;
                        // if not holding (clicked) but wasHeld previously (true) (and previous click was a singleClick (true))
                    } else { // not holding this round (click) and didn't hold last round (click)
                    	LOGGER.info("We double clicked but didn't hold this time.");
                        windowUp_DownAll();
                        getWindowStates(2, counter2);
                        counter2 = 0;
                        holding = false;
                        windowStatesPrinted = false;
                    }
                }
                timer.stop();
            } else {
            	LOGGER.info("Single click.");
                timer.restart();
            }
            LOGGER.info("End mouseClicked.");
        }
        
        @Override
        public void actionPerformed(ActionEvent e){
        	LOGGER.info("Running actionPerformed...");
        	LOGGER.info("holding = " + holding + " | singleClick = " + singleClick);
            if (holding == true && !doubleClick) {
                if (wasHeld == true) {
                	LOGGER.info("We single click and held last time.");
                    getWindowStates(1, counter2);
                    holding = false;
                    windowStatesPrinted = false;
                } else {
                	LOGGER.info("We single clicked and held it this time.");
                    windowUp_Down();
                    getWindowStates(1, counter2);
                    // set wasHeld to true and holding to false
                    wasHeld = true;
                    holding = false;
                }
            } else if (holding == false && !doubleClick) {
                if (wasHeld == true) { // held on last round, clicked this round (reversing states and button logic)
                    LOGGER.info("We single click and held last time.");
                    //windowUp_Down();
                    getWindowStates(1, 0);
                    beingHeldTimer = 0;
                    counter2 = 0;
                    wasHeld = false;
                    windowStatesPrinted = false;
                    // if not holding (clicked) but wasHeld previously (true) (and previous click was a doubleClick (true))
                } else { // not holding this round (click) and didn't hold last round (click)
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
        } // end actionPerformed()
        @Override
        public String toString() {
        	return "WindowButtonHandler: " + 
        		   "timer, type of Timer " +
        		   "beingHeldTimer, type of Timer " +
        		   "LOGGER, type of Logger.";
        	
        }
    } // end WindowButtonHandler.class
    
    // method to set constraints on
    private void addComponent(Component component, int gridy, int gridx, double gwidth, double gheight) {
        LOGGER.info("Inside addComponent()");
    	constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.gridwidth = (int)Math.ceil(gwidth);
        constraints.gridheight = (int)Math.ceil(gheight);
        layout.setConstraints(component, constraints);
        add(component);
        LOGGER.info("Component added.");
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
        LOGGER.info("Car is: " + carState);
        return carState;
    }
    /**
     * This method returns the state of the trunk.
     * @return 
     */
    public State getTrunkState() {
    	LOGGER.info("Trunk is: " + trunkState);
        return trunkState;
    }
    /**
     * This method returns the state of the alarm.
     * @return 
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
     * @return
     */
    public State getMasterDoorLockState() {
    	LOGGER.info("Master door lock state is : " + masterDoorLockState);
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
    	LOGGER.info("Passenger door lock is : " + passengerDoorLockState);
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
    	LOGGER.info("Left door lock is : " + leftDoorLockState);
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
    	LOGGER.info("Right door lock is : " + rightDoorLockState);
        return rightDoorLockState;
    }
    
    /**
     * This method is used for testing purposes only.
     * @param state
     */
    protected void setRightDoorLockState(State state) {
        this.rightDoorLockState = state;
    }
    
    public void printWindowStates(int clicks, int windowCounter) {
    	if ((clicks == 1 || clicks == 2) && windowCounter == 0) {
    		LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window: " + masterWindowState);
            LOGGER.info("Passenger Window: " + passengerWindowState);
            LOGGER.info("Back Left Window: " + leftWindowState);
            LOGGER.info("Back Right Window: " + rightWindowState);
            LOGGER.info("----- End Window States -----");
    	} else if (clicks == 1 && windowCounter > 0) {
    		LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window is " + windowCounter + "% " +  masterWindowState);
            LOGGER.info("Passenger Window: " + passengerWindowState);
            LOGGER.info("Back Left Window: " + leftWindowState);
            LOGGER.info("Back Right Window: " + rightWindowState);
            LOGGER.info("----- End Window States -----");
    	} else {
    		LOGGER.info("----- Window States -----");
            LOGGER.info("Master Window is " + windowCounter + "% " +  masterWindowState);
            LOGGER.info("Passenger Window is " + windowCounter + "% " + passengerWindowState);
            LOGGER.info("Back Left Window is " + windowCounter + "% " + leftWindowState);
            LOGGER.info("Back Right Window is " + windowCounter + "% " +  rightWindowState);
            LOGGER.info("----- End Window States -----");
    	}
    	windowStatesPrinted = true;
    }
    
    public void printDoorStates() {
    	LOGGER.info("----- Door States -----");
        LOGGER.info("Master door lock: " + masterDoorLockState);
        LOGGER.info("Passenger door lock: " + passengerDoorLockState);
        LOGGER.info("Back Left door lock: " + leftDoorLockState);
        LOGGER.info("Back Right doorLock: " + rightDoorLockState);
        LOGGER.info("----- End Door States -----");
    }
    
    /**
     * This method simply returns the current state of all the windows.
     * @param clicks : integer; one click or two
     * @param windowCounter : counter2
     * @return 
     */
    public void getWindowStates(int clicks, int windowCounter) {
        if ((clicks == 1 || clicks == 2) && windowCounter == 0) { 
            textArea.append("\nMaster window is " + masterWindowState + "\n");
            textArea.append("Passenger window is " + passengerWindowState + "\n");
            textArea.append("Left window is " + leftWindowState + "\n");
            textArea.append("Right window is " + rightWindowState + "\n\n");
            printWindowStates(clicks, 0);
        } else if (clicks == 1 && windowCounter > 0) {
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
     * @return
     */
    protected State getMasterWindowState() {
    	LOGGER.info("Master window is : " + masterWindowState);
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
    	LOGGER.info("Passenger window is : " + passengerWindowState);
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
    	LOGGER.info("Left window is : " + leftWindowState);
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
    	LOGGER.info("Right window is : " + rightWindowState);
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
    /** Calls createImageIcon(String path, String description 
     * @throws IOException */
    public ImageIcon createImageIcon(String path) throws IOException {
    	return createImageIcon(path, "No description given.");
    }
    /** Returns an ImageIcon, or null if the path was invalid. 
     * @throws IOException */
    @SuppressWarnings("unused")
	protected ImageIcon createImageIcon(String path, String description) throws IOException {
        LOGGER.info("Inside createImageIcon()");
        File sourceimage = new File(path);
        //java.net.URL imgURL = getClass().getResource(path);
        try {
    	    LOGGER.info("the path '" + path + "' created an image! the imageicon is being returned...");
    	    LOGGER.info("End createImageIcon()");
    	    return new ImageIcon(ImageIO.read(sourceimage), description);
    	    //return new ImageIcon(imgURL, description);
        } catch (IOException c) {
        	LOGGER.error("The path '" + path + "' could not find an image there!");
        }
        return null;
    }
}