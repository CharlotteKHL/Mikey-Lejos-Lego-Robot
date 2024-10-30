import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;

// Class written by Charlotte Lam

public class Default implements Behavior {
	private static BaseRegulatedMotor mid;
	private static NXTTouchSensor touchSensor;
	private static SampleProvider sp;
	private float[] sample = new float[1];
	private state b ;
	private boolean supress = false;

	/**
	 * Default() constructor
	 * @param State variable to tell when the game has ended, Main motor, Default touch sensor.
	 * Initialises and stores variables passed from the arbitrator.
	 */
	
	public Default(state b, BaseRegulatedMotor main, NXTTouchSensor ts){
		this.b=b;
		mid = main;
		touchSensor = ts;
		
		sp = touchSensor.getTouchMode();
		mid.setSpeed(100);
		
	}
	
	/**
	 * takeControl() method
	 * Takes control if the game has not ended (state is false)
	 */
	public boolean takeControl(){
		supress = false;
		return (b.getState());
	}
	
	/**
	 * action() method
	 * If behaviour is not suppressed, moves Mikey back to his original position, this is in the case the robot is lost or is not where it should be  
	 * Movement is stopped when the right touch sensor is pressed.
	 * 
	 */
	public void action() {
		
			if(!supress) {
		 
			sp.fetchSample(sample, 0);
			int touchValue = (int)sample[0];
		
			mid.backward();
			while(true) {
			
				sp.fetchSample(sample, 0);
				touchValue = (int)sample[0];
			
				if(touchValue == 1) {
					break;
				}	
			}
			mid.stop();
	
		
			}
	}
	
	/**
	 * supress() method
	 * Suppresses behaviour if another is running
	 */

	public void suppress() {
		supress = true;
	}
	
}