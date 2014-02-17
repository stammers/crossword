import java.io.Serializable;



public class Clue implements Serializable{
	
	private static final long serialVersionUID = 8795372207066358863L;
	/**
	 * Holds the clue number.
	 */
	final int number;
	/**
	 * Holds the x co-ordinate relative to the crossword size.
	 */
	final int x;
	/**
	 * Holds the y co-ordinate relative to the crossword size.
	 */
	final int y;
	/**
	 * Holds the written clue.
	 */
	final String clue;
	/**
	 * Holds the answer to the above clue.
	 */
	final String answer;
	/**
	 * Holds if the clue has been solved.
	 */
	boolean solved;
	/**
	 * Holds the name of the user who solved it if solved.
	 */
	String user;
	/**
	 * Holds the time and date it was solved if solved.
	 */
	String solveTime;
	/**
	 * Holds whether solved clue support is turned on or not.
	 */
	private transient boolean solvedSupport;

	Clue(int number, int x, int y, String clue, String answer, boolean solved){
		this.number = number;
		this.x = x;
		this.y = y;
		this.clue = clue;
		this.answer = answer;
		this.solved = solved;
	}
	
	/* 
	 * Returns a string representation of the clue. Contains the clues number, the clue text 
	 * itself and the length of the clue.
	 */
	public String toString(){
		String length = null;
		//calculates the length of the answer if it includes any -'s
		if(answer.contains("-")){
			String first = answer;
			length = " (" + first.indexOf("-");
			first = first.substring(first.indexOf("-")+1);
			while(first.indexOf("-") !=-1){
				length = length + "-" + (first.indexOf("-"));
				first = first.substring(first.indexOf("-")+1);
			}
			length = length + "-" + first.length()+ ")";
			
		//calculates the length of the answer if it includes any spaces
		}else if(answer.contains(" ")){
			String first = answer;
			length = " (" + first.indexOf(" ");
			first = first.substring(first.indexOf(" ")+1);
			while(first.indexOf(" ") !=-1){
				length = length + "," + (first.indexOf(" "));
				first = first.substring(first.indexOf(" ")+1);
			}
			length = length + "," + first.length()+ ")";
		}else{
			length = " (" + answer.length() + ")";
		}
		//if it's been solved and solved clue support is on then the returned string will have a strike
		//through it to show it's been solved.
		if(solved && solvedSupport){
			return "<html><strike>" + (number + ". " + clue + length) + " </strike></html>";

		}else{
			return (number + ". " + clue + length);

		}
	}
	
	/**
	 * Sets this clue to have been solved by a given user at a given time.
	 * 
	 * @param solved Whether the clue is solved or not.
	 * @param user The user who solved the clue.
	 * @param time The time and day the clue was solved.
	 */
	public void setSolved(boolean solved, String user, String time){
		this.solved = solved;
		this.user = user;
		this.solveTime = time;
	}
	
	/**
	 * Returns a string representation of whether it's been solved yet.
	 * If it has shows who the user was and at what time it was solved.
	 * 
	 * @return String representation of solved boolean.
	 */
	public String getSolved(){
		if(!solved){
			return "Hasn't been solved";
		}else{
			return ("Solved by: " + user + " on: " + solveTime);
		}
	}
	
	/**
	 * Returns whether the clue has been solved or not.
	 * 
	 * @return Boolean represented clue solved.
	 */
	public boolean solved(){
		return solved;
	}
	
	/**
	 * Sets whether the solved clue support is being shown or not.
	 * 
	 * @param support True if solved support selected, false otherwise.
	 */
	public void setSupport(boolean support){
		this.solvedSupport = support;
	}
}