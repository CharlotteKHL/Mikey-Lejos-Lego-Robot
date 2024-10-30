// class written by Fatima Benazza

public class state {
	private boolean s ;
	// in endgame we set this to false 
	// any other behaviour it should be true 
	
	/**
	 * State() constructor 
	 * @param Turn variable to tell the program to keep working */
	public state (boolean s ) {
		this.s =s;
	}
	
//The get method returns the value of the variable s
	public boolean getState() {
		return s;
		}
	
	//The set method takes a parameter boolean s and assign it to the s variable 

	public void setState(boolean s) { 
		this.s  = s;
		}
	
}
