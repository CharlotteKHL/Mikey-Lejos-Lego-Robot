import java.util.Arrays;

import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

// class  coded by Fatima, Bradely and Yann.

public class Play implements Behavior{


	private EV3ColorSensor cs;
	private float[] level = new float[1];           
	private SampleProvider redLevel;
	private int[][] counterArray = new int[7][6];
	
	/*Defining variables including the scanner, main motor to move the robot (bottomMotor),
	 *  the motor to push the counters (pushPull),
	the touch sensor, sample provider and turn.*/
	private BaseRegulatedMotor scanner;
	private BaseRegulatedMotor bottomMotor;
	private BaseRegulatedMotor pushPull = new EV3MediumRegulatedMotor(MotorPort.D);
	private NXTTouchSensor touchSensor;
	private SampleProvider sp;
	private Turn yes;

	final int one_fifty = 1500;
	final int two_one = 2100;
	final int oneSec=1000;
	final int twoSec = 2000;
	final int threeSec = 3000;
	
	
	private NXTTouchSensor touchS;
	private SampleProvider ts;
	private float[] spl = new float[1];
	
	
	private final int Mikey_turn = 2;
	private final int Player_turn = 3;
	
	/*Initialises the speed of the main motor and the other motors to be a set speed, in this case 100.*/
	private final int bottomMotor_speed = 100;
	private final int MOTOR_SPEED = 100;

	private int count = 0;
	private int M_count = 0;
	private int P_count = 0;
	private int currColumn = 0;
	
	
	/**
	 * Play() constructor
	 * @param State variable to tell when the game has ended, turn variable to know if it should take its turn, main motor, scanner motor, scanner colour sensor, left touch sensor and right touch sensor.
	 * 
	 * Initialises and stores variables passed from the arbitrator.
	 * the main Array that stores the mikey and the player moves.
	 */
	public Play(Turn yes,EV3ColorSensor cs, BaseRegulatedMotor scanner2, BaseRegulatedMotor bottomMotor2,NXTTouchSensor touchSensor,  NXTTouchSensor touchs) {
		this.cs = cs;
		this.yes = yes;
		this.scanner = scanner2;
		this.touchSensor = touchSensor;
		this.touchS = touchs;
		bottomMotor = bottomMotor2;
		ts = touchS.getTouchMode();
		sp = this.touchSensor.getTouchMode();
		redLevel = this.cs.getRedMode();
		
		for (int i=0; i<7; i++)
        {
            counterArray[i] = new int[] {0, 0, 0, 0, 0, 0};
        }
		
    }
	
	/**
	 * takeControl() method
	 * @return True when the left touch sensor  is pressed and it is the Play behaviours turn.
	 * 
	 * Method tells the arbitrator when the behaviour needs to take control.
	 */
	@Override
	public boolean takeControl() { 
		ts.fetchSample(spl, 0);
		int touchV = (int)spl[0];
       

		return(touchV == 1 && yes.getTurn());
	}
	
	/**
	 * action() method
	 * when the touch sensor is pressed mikey would move backward till the other touch sensor is pressed 
	 * the scanner() is called to scan the board 
	 * the getNextMove() determine the robot's next move. and returns an integer 
	 * PositioningAndDropping() method make mikey play  */
	@Override
	public void action() {
		LCD.clear();

		LCD.drawString("2nd Behaviour ",  2, 2);
		bottomMotor.setSpeed(50);
		bottomMotor.backward();

		float[] sample = new float[1];
		sp.fetchSample(sample, 0);
		int touchValue = (int) sample[0];
		while(true) {
			sp.fetchSample(sample, 0);
			touchValue = (int)sample[0];
			
			if(touchValue == 1){
				break;
			}
				
		}
		

		bottomMotor.stop();
		scanner();

		int position = getNextMove();			
			
		PositioningAndDropping(position);
		yes.setTurn (false);

	}
	
	


	/**
	 * supress() method
	 * Empty since here is no case where it supressed, unless the program has ended.
	 */
	@Override
	public void suppress() {}
	

	
	
	private void scanner() { // Scanner method written by Bradley King
		
		bottomMotor.setSpeed(bottomMotor_speed);
		
		redLevel = cs.getRedMode();
		redLevel.fetchSample(level, 0);
		
		scanner.setSpeed(100);
		
		/* 0.01 = frame
		 * ~0.46-0.80 = Yellow (white)
		 * ~0.45-0.2 = Red 
		 * 0 = Empty
		 */
		
		while (true) // First loop to scan columns
		{
			scanner.forward();
			
			while (true) // Second loop to scan rows
			{	
				// Shows user the value the colour scanner is currently seeing
				redLevel = cs.getRedMode();
				redLevel.fetchSample(level, 0);
				LCD.drawString(Float.toString(level[0]), 1, 3);
				
				// if value scanned is not the frame or background
				if(level[0] > 0.2f) {
					
					// Scanner is stopped and the value it sees is noted
					scanner.stop();
					
					redLevel.fetchSample(level, 0);
					
					// Checks the value for a red or yellow token and updates the array
					if(level[0] >= 0.46f) {
						P_count++;
						counterArray[currColumn][count] = Player_turn;
					} 
					
					if ((level[0] <= 0.45f) && (level[0] > 0.1f)) {
						M_count++;
						counterArray[currColumn][count] = Mikey_turn;
					}
					
					// If the scanner has not reached the top the scanner will continue upwards
					if (count<7)
					{
						count++;
						scanner.forward();
						Delay.msDelay(one_fifty);
					}
					
				}
				
				// If the value scanned is empty the scanner stops and returns back to the bottom
				if (level[0] <= 0.01f)
				{
					scanner.stop();
					
					// If the scanner has moved up at least one, this is not the case if an empty position is scanned first
					if (count >=0) {
						scanner.backward();
						Delay.msDelay(one_fifty*count+100);
						scanner.stop();
					}
					
					break;
				}
			} // end of inner while loop
			
			// In each column the LCD displays the counters scanned and the column in the array
			LCD.clear();
			LCD.drawString(Integer.toString(currColumn) + Arrays.toString(counterArray[currColumn]), 1, 2);
			LCD.drawString("M_count = " + M_count, 1, 4);
			LCD.drawString("P_count = " + P_count, 1, 5);
			LCD.drawString(Integer.toString(count), 1, 6);
			Delay.msDelay(twoSec);
			
			// If not reached the last column on the board continue, otherwise break the loop 
			if (currColumn < 6)
			{
				bottomMotor.forward();
				Delay.msDelay(1100);
				bottomMotor.stop();
				currColumn++;
				M_count = 0;
				P_count = 0;
				count = 0;
			} else
			{
				break;
			}
			
			
		} // end of outer while loop
		
		
		// Resets the scanner to the bottom position
		if (count >1) {
			scanner.backward();
			Delay.msDelay(one_fifty *count);
			scanner.stop();
		}
		// Resets the bottom motor so Mikey is in the default position
		if(currColumn >0)
		{
			bottomMotor.backward();
			Delay.msDelay(currColumn*1100);
			bottomMotor.stop();
		}
		scanner.stop();
		bottomMotor.stop();
	}

	/**
	 * getNextMove() method 
	 * @return an integer that shows mikey where to go 
	 * a basic algorithm that just chooses the first available column starting from the right.*/
	
	private int getNextMove() {
		// Fatima Benazza's method
		// The AI algorithm used to determine the robot's next move.

	    int minCount = 5;
	    int minCol = -1;
	    
	    // Iterate over each column in the counterArray and count the number of counters in each column
	    for (int col = 0; col < 7; col++) {
	        int count = 0;
	        for (int row = 0; row < 5; row++) {
	            if (counterArray[col][row] == 2 || counterArray[col][row] == 3) {
	                count++;
	            }
	        }
	        
	        // Update the minimum count and column index if the current column has fewer counters than the previous minimum
	        if (count < minCount) {
	            minCount = count;
	            minCol = col;
	        }
	    }
	    
	    return minCol;
	}
	
	
	public void PositioningAndDropping(int position) {
		// Yann Cooke's method 
		
		/*Setting the motors for the dropping the counter and for moving along as the set speed of "MOTOR_SPEED".*/
		pushPull.setSpeed(MOTOR_SPEED);
		bottomMotor.setSpeed(MOTOR_SPEED);
		
		/*The main motor moving the robot goes forward at the calculated
		 *  distance in order for it to be lined up to drop the counter in the calculated column.*/
		bottomMotor.forward();
		//Delay.msDelay(moveDistance);
		Delay.msDelay(oneSec*(position+1));
		
		bottomMotor.stop();
		
		
		
		
		Delay.msDelay(threeSec);
		
		/*This is the code that controls the motor for pushing out a counter.
		Once the main motor has lined itself up with the desired column, the code for dropping the counter activates.
		It first goes forward, pushing the counter until the counter has been dropped (delay of 2100 seconds).
		It then stops and goes backwards the same amount to free up space for another counter to be dropped.*/

		pushPull.forward();
		Delay.msDelay(two_one);
		pushPull.stop();
		pushPull.backward();
		Delay.msDelay(two_one);
		pushPull.stop();
		//The main motor moving the robot then goes back the same distance it travelled, effectively returning it to its default position.
		
		bottomMotor.backward();
		Delay.msDelay(oneSec*(position+1));
		bottomMotor.stop();
		
		bottomMotor.stop();
		pushPull.close();
		
		
	}
}







