import java.util.ArrayList;


public class CrosswordHyphenExample {

	Crossword getPuzzle(){
		Crossword c;
		String title = "Puzzle Number 92 (www.crosswordsite.com)";
		int size = 15;
		ArrayList<Clue> acrossClues = new ArrayList<Clue>();
		ArrayList<Clue> downClues = new ArrayList<Clue>();
		
		acrossClues.add( new Clue( 3, 5, 0, "Academic world", "academia", false) );
		acrossClues.add( new Clue( 9, 0, 1, "Narrow Street", "alley", false) );
		acrossClues.add( new Clue( 10, 12, 1, "Acquire", "get", false) );
		acrossClues.add( new Clue( 11, 10, 2, "New Zealand parrot", "kea", false) );
		acrossClues.add( new Clue( 12, 0, 3, "Defraud", "short-change", false) );
		acrossClues.add( new Clue( 14, 10, 4, "Meat", "steak", false) );
		acrossClues.add( new Clue( 16, 5, 5, "Drama", "play", false) );
		acrossClues.add( new Clue( 17, 0, 6, "Summed", "added", false) );
		acrossClues.add( new Clue( 19, 9, 6, "God-Like", "divine", false) );
		acrossClues.add( new Clue( 20, 6, 7, "Drinking Vessel", "cup", false) );
		acrossClues.add( new Clue( 22, 0, 8, "Opera by Bizet", "carmen", false) );
		acrossClues.add( new Clue( 23, 10, 8, "Former Russian Rulers", "tsars", false) );
		acrossClues.add( new Clue( 25, 6, 9, "Wind Instrument", "horn", false) );
		acrossClues.add( new Clue( 26, 0, 10, "Raw Hides", "pelts", false) );
		acrossClues.add( new Clue( 28, 4, 11, "At Once", "immediately", false) );
		acrossClues.add( new Clue( 30, 2, 12, "Storage Container", "bin", false) );
		acrossClues.add( new Clue( 31, 0, 13, "Ingot", "bar", false) );
		acrossClues.add( new Clue( 32, 10, 13, "Eccentric", "potty", false) );
		acrossClues.add( new Clue( 33, 2, 14, "Engrossed", "absorbed", false) );
		downClues.add( new Clue( 1, 0, 0, "24 Hour Period", "days", false) );
		downClues.add( new Clue( 2, 3, 0, "Body of Peers", "peerage", false) );
		downClues.add( new Clue( 4, 6, 0, "Universal in extent", "catholic", false) );
		downClues.add( new Clue( 5, 8, 0, "Of Delicate Beauty", "dainty", false) );
		downClues.add( new Clue( 6, 10, 0, "Temporary Expedient", "makeshift", false) );
		downClues.add( new Clue( 7, 12, 0, "Wide Open", "agape", false) );
		downClues.add( new Clue( 8, 14, 0, "Pile", "stack", false) );
		downClues.add( new Clue( 13, 1, 3, "Made by Hand", "handmade", false) );
		downClues.add( new Clue( 15, 13, 4, "Not Normal", "abnormal", false) );
		downClues.add( new Clue( 18, 4, 6, "Bandages", "dressings", false) );
		downClues.add( new Clue( 21, 8, 7, "Heaven", "paradise", false) );
		downClues.add( new Clue( 24, 11, 8, "Stopping Place", "station", false) );
		downClues.add( new Clue( 25, 6, 9, "Tool Used for Driving Nails", "hammer", false) );
		downClues.add( new Clue( 26, 0, 10, "Common People", "plebs", false) );
		downClues.add( new Clue( 27, 2, 10, "Seventh Sign of the Zodiac", "libra", false) );
		downClues.add( new Clue( 29, 14, 11, "Spool Like Toy", "yoyo", false) );
		
		c = new Crossword(title,size,acrossClues,downClues);
		return c;
	}
}
