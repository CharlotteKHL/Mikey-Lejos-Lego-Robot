import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;
import lejos.hardware.Battery;

// class written by Fatima Benazza
public class BatteryLow implements Behavior {
	private state f ;

    final int fiveSec = 5000;
	
	
	/**
	 * BatteryLow() constructor 
	 * @param State variable to tell when the program ends  */
	public BatteryLow(state f) {
		this.f = f;
	}

	
	
	
	/**
	 * takeControl() method
	 * @return True when the battery voltage is low and the state is set to true.
	
	 */
    public boolean takeControl() {
    	
        return (Battery.getVoltage()  < 6.5 && f.getState()); // adjust the threshold value as needed
    }
    
    
    
    /**action() method 
     * when the battery voltage is low and the state is still set to true  
     * print in the LCD that the program is going to stop in 5 seconds 
     * then set the state to false that will end the program 
     * */
    public void action() {
  
            // Flash the screen and beep continuously
            LCD.clear();
            LCD.drawString("Battery Level Low!", 0, 3);
            Sound.buzz();
            LCD.drawString("Gonna close the programe ", 0, 4);
            LCD.drawString(" in 5 sec", 0, 5);
            Delay.msDelay(fiveSec);
            f.setState(false);
            
           
    }
    /**
	 * supress() method
	 * Empty since here is no case where it supressed, unless the program has ended.
	 */
    public void suppress() {}
}






























