import java.util.ArrayList;
/**
 * An example crossword.
 * 
 * @author Daniel
 *
 */
class CrosswordExample {

	/**
	 * Returns a created example crossword.
	 * 
	 * @return The created crossword.
	 */
	Crossword getPuzzle(){

		//sets a title and size for the crossword
		Crossword c;
		String title = "An example puzzle";
		int size = 11;
		ArrayList<Clue> acrossClues = new ArrayList<Clue>();
		ArrayList<Clue> downClues = new ArrayList<Clue>();

		//creates and adds all the across clue
		acrossClues.add( new Clue( 1, 1, 0, "Eager Involvement", "enthusiasm", false) );
		acrossClues.add( new Clue( 8, 0, 2, "Stream of water", "river", false) );
		acrossClues.add( new Clue( 9, 6, 2, "Take as one's own", "adopt", false) );
		acrossClues.add( new Clue( 10, 0, 4, "Ball game", "golf", false) );
		acrossClues.add( new Clue( 12, 5, 4, "Guard", "sentry", false) );
		acrossClues.add( new Clue( 14, 0, 6, "Language communication", "speech", false) );
		acrossClues.add( new Clue( 17, 7, 6, "Fruit", "plum", false) );
		acrossClues.add( new Clue( 21, 0, 8, "In addition", "extra", false) );
		acrossClues.add( new Clue( 22, 6, 8, "Boundary", "limit", false) );
		acrossClues.add( new Clue( 23, 0, 10, "Executives", "management", false) );
		
		//creates and adds all the down clues
		downClues.add( new Clue( 2, 2, 0, "Pertaining to warships", "naval", false) );
		downClues.add( new Clue( 3, 4, 0, "Solid", "hard", false) );
		downClues.add( new Clue( 4, 6, 0, "Apportion", "share", false) );
		downClues.add( new Clue( 5, 8, 0, "Concerning", "about", false) );
		downClues.add( new Clue( 6, 10, 0, "Friendly", "matey", false) );
		downClues.add( new Clue( 7, 0, 1, "Boast", "brag", false) );
		downClues.add( new Clue( 11, 3, 4, "Enemy", "foe", false) );
		downClues.add( new Clue( 13, 7, 4, "Doze", "nap", false) );
		downClues.add( new Clue( 14, 0, 6, "Water vapour", "steam", false) );
		downClues.add( new Clue( 15, 2, 6, "Consumed", "eaten", false) );
		downClues.add( new Clue( 16, 4, 6, "Loud, resonant sound", "clang", false) );
		downClues.add( new Clue( 18, 8, 6, "Yellowish, citrus fruit", "lemon", false) );
		downClues.add( new Clue( 19, 10, 6 , "Mongrel dog", "mutt", false) );
		downClues.add( new Clue( 20, 6, 7, "Shut with force", "slam", false) );

		//creates the crossword to be returned with all the above created data
		c = new Crossword(title,size,acrossClues,downClues);

		return c;
	}
	

}