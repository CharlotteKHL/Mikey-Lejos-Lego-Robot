import java.util.Arrays;
import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

// Class written by Charlotte Lam

public class Endgame implements Behavior {

	boolean s = false;
	// The current state of the game board, represented as a 2D array of integers.
	// 0 = empty cell, 3 = player's piece, 2 = robot's piece.
	private int[][] map = new int[7][6];
	final int Mikey_turn = 2;
	final int Player_turn = 3;
	final int Success = 4;

	final int oneSec =1000;
	final int twosec = 2000;
	final int five = 5000;
	final int onefive = 1500;

	
	private state f;
	private Turn no;
	
	private BaseRegulatedMotor bottomMotor;
	private NXTTouchSensor touchS;
	
	private BaseRegulatedMotor scannerMotor;
	private EV3ColorSensor cs;

	/**
	 * Endgame() constructor
	 * @param State variable to tell when the game has ended, turn variable to know if it should take its turn, main motor, scanner motor, scanner colour sensor.
	 * 
	 * Initialises and stores variables passed from the arbitrator.
	 */
	
	public Endgame(state f,Turn no, BaseRegulatedMotor bottomMotor2, NXTTouchSensor touchs, BaseRegulatedMotor scanner, EV3ColorSensor cs) {
		this.f = f;
		this.no = no;
		this.bottomMotor = bottomMotor2;
		touchS = touchs;
		scannerMotor = scanner;
		this.cs = cs;
	}
	
	/**
	 * takeControl() method
	 * @return True when the UP button is pressed and it is not the Play behaviours turn.
	 * 
	 * Method tells the arbitrator when the behaviour needs to take control.
	 */
	
	@Override
	public boolean takeControl() {
		
		int buttons = Button.readButtons();
		return (buttons == Button.ID_UP) && !no.getTurn() ;
	}
	
	/**
	 * supress() method
	 * Empty since here is no case where it supressed, unless the program has ended.
	 */
	
	@Override
	public void suppress() {}

	/**
	 * action() method
	 * 
	 * Scans the board and checks the 2D array for if the Player or Mikey has won.
	 * Switch statement here is used to only check certain directions for each position on the board, this is to avoid an IndexOutOfBounds error and saves time on checking directions.
	 */
	
	@Override
	public void action() {
		
		LCD.clear();
		LCD.drawString("Endgame initiated",  0, 3);
		Delay.msDelay(oneSec);


			//0 = blank
			//1 = frame
			//2 = red
			//3 = yellow
			
			// Variables to help find the area in which the token being checked resides.
			int row = 0;
			int column = 0;
			
			// Keeping track who has won and how many times on the current board
			int M_win = 0;
			int P_win = 0;
			boolean win = false;
			
			// Keep track of the current counter (0, 2 or 3) and the number of counters Mikey has placed on the board
			int current = 0;
			int M_count = 0;
			
			// Check for a 4 in a row in these directions.
			int up = 0;
			int right = 0;
			int left = 0;
			int down = 0;
			int uR_diagonal = 0;
			int uL_diagonal = 0;
			int dR_diagonal = 0;
			int dL_diagonal = 0;
				
			scan();
				
				for(int i = 0; i < 7; i++){
					
					int empty = 0;
					
					for(int j = 0; j < 6; j++){
						
						current = 0;
						
						// resets to check for each position on the board.
						up = 0;
						right = 0;
						left = 0;
						down = 0; 
						uR_diagonal = 0;
						uL_diagonal = 0;
						dR_diagonal = 0;
						dL_diagonal = 0;
						
						// Here it checks 
						
						if(j < 3){
							//too down
							row = 1;
						} else if (j > 2){
							//too up
							row = 2;
						}
	
						if(i < 3){		
							//too left
							column = 3;			
						} else if (i > 2){
							//too right
							column = 6;
						}
						
						// checks the current position, if empty move onto the next position.
						if(map[i][j] == 0){
							
							empty++;
							continue;
							
						} else {
							
							current = map[i][j];
							
						}
							
						switch((row + column)){
						case 1:
							// The position is in the lower middle part of the board
							// check up 
							// check top right and left diagonals
							// check left and right 
							for(int k = 0; k < 4; k++)
							{
								if(map[i][j+k] == current) {
									up++;
								} 
								if (map[i+k][j+k] == current) {
									uR_diagonal++;
								} 
								if (map[i-k][j] == current) {
									left++;
								} 
								if (map[i+k][j] == current) {
									right++;
								} 
								if (map[i-k][j+k] == current) {
									uL_diagonal ++;
								}
							}
									
						break;	
	
						case 2:
							// The position is in the upper middle part of the board
							// check left and right 
							// check down and down diagonals 
							for(int k = 0; k < 4; k++)
							{
								if (map[i-k][j] == current) {
									left++;
								} 
								if (map[i+k][j] == current) {
									right++;
								} 
								if (map[i-k][j-k] == current) {
									dL_diagonal++;
								} 
								if (map[i+k][j-k] == current) {
									dR_diagonal++;
								}
								if (map[i-k][j] == current) {
									down++;
								}
							}
						
							break;
						
						case 3:
							// The position is in the left halfway up part of the board.
							// check up and down 
							// check right up and down diagonals 
							for(int k = 0; k < 4; k++)
							{
								if(map[i][j+k] == current) {
									up++;
								} 
								if (map[i-k][j] == current) {
									down++;
								} 
								if (map[i+k][j-k] == current) {
									dR_diagonal++;
								} 
								if (map[i+k][j+k] == current) {
									uR_diagonal++;
								}
								if (map[i+k][j] == current) {
									right++;
								} 
							}
							
						
							break;
					
						case 4:
							// The position is in the bottom left corner of the board
							// check up and right, and right up diagonal
							for(int k = 0; k < 4; k++)
							{
								if(map[i][j+k] == current) {
									up++;
								} 
								if (map[i+k][j+k] == current) {
									uR_diagonal++;
								}
								if(map[i+k][j] == current) {
									right++;
								}
							}
							
							break;
	
						case 5:
							// The position is in the top left corner of the board
							// check right and down 
							// check down right diagonals
							for(int k = 0; k < 4; k++)
							{
								if (map[i+k][j] == current) {
									right++;
								} 
								if (map[i-k][j] == current) {
									down++;
								} 
								if (map[i+k][j-k] == current) {
									dR_diagonal++;
								} 
							}
							
							break;
						
						case 6:
							// The position is in the right section, halfway up the board
							// check up and down 
				 			// check left up and down diagonals 
							for(int k = 0; k < 4; k++)
							{
								if(map[i][j+k] == current) {
									up++;
								} 
								if (map[i-k][j] == current) {
									down++;
								} 
								if (map[i-k][j-k] == current) {
									dL_diagonal++;
								}  
								if (map[i-k][j+k] == current) {
									uL_diagonal ++;
								}
								if (map[i-k][j] == current) {
									left++;
								}
							}
							
							break;
							
						case 7:
							// The position is in the bottom right corner of the board
							// check left and up 
							// check left and up diagonal 
							for(int k = 0; k < 4; k++)
							{
								if(map[i][j+k] == current) {
									up++;
								} 
								if (map[i-k][j] == current) {
									left++;
								} 
								if (map[i-k][j+k] == current) {
									uL_diagonal ++;
								}
							}
							
							break;
	
						case 8:
							// The position is in the top right corner of the board
							// check left, down and left down diagonal
							for(int k = 0; k < 4; k++)
							{
								if (map[i-k][j] == current) {
									down++;
								} 
								if (map[i-k][j] == current) {
									left++;
								} 
								if (map[i-k][j-k] == current) {
									dL_diagonal++;
								}
							}
							break;
	
						default:
							// The position is in the middle of the board, check all directions
							for(int k = 0; k < 4; k++)
							{
								if(map[i][j+k] == current) {
									up++;
								} 
								if (map[i-k][j] == current) {
									down++;
								} 
								if (map[i-k][j] == current) {
									left++;
								} 
								if (map[i+k][j] == current) {
									right++;
								} 
								if (map[i-k][j+k] == current) {
									uL_diagonal ++;
								} 
								if (map[i-k][j-k] == current) {
									dL_diagonal++;
								} 
								if (map[i+k][j+k] == current) {
									uR_diagonal++;
								} 
								if (map[i+k][j-k] == current) {
									dR_diagonal++;
								} 
							}
			
						}
						// switch statement ends here.
						
						// Counts number of tokens that Mikey has placed.
						if(current == 2) {
							M_count++;
						} 
						
						// Checks for a win in all directions, if so breaks the loop.
						if((left == Success) || (right == Success) || (up == Success) || (down == Success) || (uL_diagonal == Success) || (dL_diagonal == Success) || (uR_diagonal == Success) || (dR_diagonal == Success)) {
							// If someone has won, identifies who has won.
							if(current == 2) {
								M_win++;
							} else {
								P_win++;
							}
							win = true;
							break;
						}
						
					} // end of inner for loop.
					
					/* Checks if the row is empty, if so no need to check above the empty row.
					 * Also checks for a win, if so breaks for loop.
					 */
					if(empty == 7 || win) 
						break;
				
				} // end of outer for loop.
				
				
				// If someone has won checks if it is Mikey or the Player and congratulates accordingly
				if(win) {
					if(M_win >= P_win) {
						LCD.clear();
						LCD.drawString("Mikey wins!",  0, 3);
						Delay.msDelay(five);
					} else {
						LCD.clear();
						LCD.drawString("Player wins!",  0, 3);
						Delay.msDelay(five);
					}
		            f.setState(false);
		       
		        // else if Mikey has 10 counters on the board, he has ran out and the game ends
				} else if(M_count == 10) {
					LCD.clear();
					LCD.drawString("Ran out of tokens, no one has won",  0, 3);
					Delay.msDelay(oneSec);
					f.setState(false);
	
				}
				
				// In both cases the game ends so the state is set to false to end the program
				
			// Turn is set to true to tell Play it can now run
			no.setTurn (true);
			
			// Motor then moves to trigger the touch sensor that runs Play
			bottomMotor.forward();
			
			SampleProvider ts  = touchS.getTouchMode();
			float[] spl = new float[1];
			
			int touchValue = (int)spl[0];
			ts.fetchSample(spl, 0);
			touchValue = (int)spl[0];
				
			if(touchValue == 1) {
					
				bottomMotor.stop();
				no.setTurn (true);
			}
			LCD.clear();
			
				
			
			
			
		}

	/**
	 * scan() method
	 * @see Play.java for comments on this method, the code here is the same
	 * The return to default code here is taken off in order to move the other way and trigger the Play behaviour.
	 * This scan method is written by Bradley King
	 */
	
	public void scan() {
		bottomMotor.setSpeed(100);
		
		float[] level = new float[1];           
		SampleProvider redLevel = cs.getRedMode();
		
		redLevel = cs.getRedMode();
		redLevel.fetchSample(level, 0);
		for (int i=0; i<7; i++)
		{
			map[i] = new int[] {0, 0, 0, 0, 0, 0};
		}
		
		
		
		scannerMotor.setSpeed(100);
		
		int count = 0;
		int M_count = 0;
		int P_count = 0;
		int currColumn = 0;
		while (true)
		{
			scannerMotor.forward();
			while (true)
			{	
				redLevel = cs.getRedMode();
				redLevel.fetchSample(level, 0);
				LCD.drawString(Float.toString(level[0]), 1, 4);
				if(level[0] > 0.2f) {
					scannerMotor.stop();
					
					redLevel.fetchSample(level, 0);
					if(level[0] >= 0.46f) {
						P_count++;
						map[currColumn][count] = 3;
					} 
					
					if ((level[0] <= 0.45f) && (level[0] > 0.1f)) {
						M_count++;
						map[currColumn][count] = 2;
					}
					if (count<7)
					{
						count++;
						scannerMotor.forward();
						Delay.msDelay(onefive);
					}
					
				}
				
				
				if (level[0] <= 0.01f)
				{
					scannerMotor.stop();
					
					if (count >=1) {
						scannerMotor.backward();
						Delay.msDelay(onefive*count + 100);
						scannerMotor.stop();
					}
					
					break;
				}
			}
			LCD.clear();
			LCD.drawString(Integer.toString(currColumn) + Arrays.toString(map[currColumn]), 1, 0);
			LCD.drawString("M_count = " + M_count, 1, 1);
			LCD.drawString("P_count = " + P_count, 1, 2);
			LCD.drawString(Integer.toString(count), 1, 5);
			Delay.msDelay(twosec);
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
			
			
		}
		
		if (count >1) {
			scannerMotor.backward();
			Delay.msDelay(onefive*count);
			scannerMotor.stop();
		}
	
	}
	
	
}

