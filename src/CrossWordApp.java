import javax.swing.SwingUtilities;


/**
 * Starts the crossword GUI being shown.
 * 
 * @author Daniel
 *
 */
public class CrossWordApp {

	public static void main(String[] args){
		SwingUtilities.invokeLater(new Runnable(){

			@Override
			public void run() {
				CrossFrame frame = new CrossFrame("Crossword");
				frame.init();
			}
			
		});
	}
}
