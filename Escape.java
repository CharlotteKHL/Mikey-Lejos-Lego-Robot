import lejos.hardware.Button;
import lejos.robotics.subsumption.Behavior;
//class written by Fatima Benazza

public class Escape implements Behavior {
	private state E;
	
	
	/**
	 * Escape() constructor 
	 * @param state variable to tell when the game has ended, */
	public Escape (state E) {
		this.E= E;
	}
	

	/**
	 * action() method
	 * if the escape button is pressed set state to false that would end the program*/

	public void action() {
        E.setState(false);
	}
	

	public void suppress() {}

	/**
	 * takeControl() method
	 * takes control when the escape button is pressed */
	@Override
	public boolean takeControl() {
		int buttons = Button.readButtons();
		
		return buttons == Button.ID_ESCAPE && E.getState(); 
	}
	

}
