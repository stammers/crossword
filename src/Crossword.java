import java.io.Serializable;
import java.util.ArrayList;


class Crossword implements Serializable{
	
	private static final long serialVersionUID = 555768116023483762L;
	/**
	 * Arraylists containing all the clues for across and down the crossword.
	 */
	final ArrayList<Clue> acrossClues, downClues;
	/**
	 * The title of the crossword.
	 */
	final String title;
	/**
	 * The size, number of squares across and down, of the crossword.
	 */
	final int size;

	Crossword(String title, int size, ArrayList<Clue> acrossClues, ArrayList<Clue> downClues){
		this.title = title;
		this.size = size;
		this.acrossClues = acrossClues;
		this.downClues = downClues;
	}
	
}