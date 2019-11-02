# CarBeeper
This quick guide will inform you how the car beeper works.
There are five buttons on the top. 
Below is a text area which displays the results of the button clicks.
There is one button below the mentioned textarea.

When you click the power button, the car comes on. When you click it again, the car is off.
When you click teh trunk button, the trunk opens up. When you click it again, the trunk closes.
When you click the alarm button, the car alarm turns on. When you click it again, the alarm turns off.

Those were simple. The lock button is a bit more complex. As you see there is only one lock button.
When you click the button once, it will lock just the driver door. If you click it once again, it will unlock the drivers door. 
When you click the button twice, it will lock all doors. When you click it twice again, all doors will be unlocked.
However, when you look at the code, what really happens is this:
When you double click the lock button, it switches the state of the drivers door, and then mimics this state for all doors.

Last but not least, we have the windows button. It is the most complex of them all. Not only can you single click or double click the button but you can single/double click and hold the button.
When you click the windows button, it will lower the drivers window. All windows are displayed for verification. Do this again, and the window is back up. Double clicking performs these actions on all windows.
When you click once and hold the windows button, the drivers window will begin to raise or lower for as long as you hold the button. This all depends on the current state and performing doing opposite. If you double click and hold the windows button, again the actions performed are done to all windows.

Finally, when you click the clear button, all the text in the text area is removed. 
