// class written by Fatima Benazza

public class Turn {
	
	
	private boolean turn ;
	
	/**
	 * Turn() constructor 
	 * @param Turn variable to tell when is the behaviour's turn to run*/
	public Turn (boolean turn ) {
		this.turn=turn;
	}
	
	//The get method returns the value of the variable turn
	public boolean getTurn() {
		return turn;
		}
	
	
	//The set method takes a parameter boolean turn and assign it to the turn variable 
	public void setTurn(boolean turn) {
		this.turn  = turn;
		}
	
}
