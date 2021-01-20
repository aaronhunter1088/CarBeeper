# CarBeeper
This quick guide will inform you how the car beeper works.
There are six buttons on the top. 
Below is a text area which displays the results of the button clicks.
There is one button below the mentioned textarea, a clear button.
The Car Beeper starts by displaying the default states of the car's properties.

When you click the power button, the car comes on. When you click it again, the car is off.
When you click the trunk button, the trunk opens up. When you click it again, the trunk closes.
When you click the alarm button, the car alarm turns on. When you click it again, the alarm turns off.

Those buttons were simple. The flat tire button is the most emulated button of them all. This works 
purely by chance of a random number. By default, all tires are inflated. Whenever you click on any other
button, a random number is set. For example, when we click the Power button, a random number is set. If
that number is 1, the master tire becomes flat. Then clicking the flat tire button, we recognize this and
display the master tire is set. Clicking any other button again, sets the random number. The master tire
will be flat until the random number is 5, when we then set the master tire to be inflated. 

The lock button is a bit more complex. As you can see, there is only one lock button.
When you click the button once, it will lock just the driver door. If you click it once again, it will unlock the 
driver's door. 
When you click the button twice, it will lock all doors. When you click it twice again, all doors will be unlocked.
However, when you look at the code, what really happens is this:
When you double-click the lock button, it switches the state of the driver's door, and then mimics this state for 
the remaining doors.

Last but not least, we have the windows button. It is the most complex of them all. Not only can you single click or 
double click the button, but you can also single/double click <i>and</i> hold the button, which incrementally raises 
or lowers the window.
When you click the windows button, it will lower the drivers window 100%. All windows are displayed for verification. 
Do this again, and the window is back up. Double clicking performs these actions on all windows.
When you click once <i>and</i> hold the windows button, the drivers window will begin to raise or lower for as long as 
you hold the button, until it's 100% up or down. This all depends on the current state. 
If you double-click and hold the windows button, again, the actions performed are done to all windows.

Finally, when you click the clear button, all the text in the text area is removed.