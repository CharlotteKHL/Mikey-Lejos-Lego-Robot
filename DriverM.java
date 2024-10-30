import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.BaseRegulatedMotor;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.NXTTouchSensor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;
import lejos.utility.Delay;

// Class written by Charlotte Lam and Fatima Benazza

public class DriverM {
	
	public static Arbitrator ab;
	private static EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S2);
	private static BaseRegulatedMotor scanner = new EV3MediumRegulatedMotor(MotorPort.A);
	private static BaseRegulatedMotor bottomMotor = new EV3LargeRegulatedMotor(MotorPort.C);
	private static NXTTouchSensor touchSensor = new NXTTouchSensor(SensorPort.S3);
	private static NXTTouchSensor touchS = new NXTTouchSensor(SensorPort.S1);
	
	public static void main(String args[]) {	
		
		// Start of the game the authors are presented as well as the project
		
		LCD.clear();
		LCD.drawString("Bradley King",  0, 0);
		LCD.drawString("Charlotte Lam",  0, 1);
		LCD.drawString("Fatima Benazza",  0, 2);
		LCD.drawString("Yann Cooke",  0, 3);
		Delay.msDelay(4000);
		LCD.clear();
		
		// Waits for an enter button press to begin
		LCD.drawString("EV3 connect4",  0, 0);
		LCD.drawString("Press Enter",  0, 1);
		LCD.drawString("To Start",  0, 2);
		
		state state = new state(true); 
		Turn turn = new Turn (false);
		while(true) {
			
			int buttons = Button.readButtons();
			if (buttons == Button.ID_ENTER)
			{
				LCD.clear();
				break;
			}
		}
		
		// Behaviour creation
		Behavior BatteryLow = new BatteryLow(state);
		Behavior Escape = new Escape(state);
		Behavior Play= new Play(turn, cs, scanner, bottomMotor, touchSensor, touchS );
		Behavior Endgame = new Endgame(state, turn, bottomMotor, touchS, scanner, cs);
		Behavior Default = new Default(state, bottomMotor,touchSensor);

		// Loop for arbitrator, this allows the second parameter to be true so the the program can end
		while (state.getState()) {
			ab = new Arbitrator(new Behavior[] {Default, Play,  Endgame, BatteryLow, Escape}, true);
			
		System.out.println("\n");
		System.out.println("\n");
			ab.go();
			ab.stop();
			
			cs.close();
			scanner.close();
			bottomMotor.close();
			touchSensor.close();
			touchS.close();
		}
	//	
		
		
		//main.close();
		
	}
}