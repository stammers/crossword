import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

/**
 * The class which will show all the clues in the crossword. Shows each direction of
 * clue in a separate list. Allows clues to be selected, causing it's corresponding
 * text entry area to be highlighted in the main crossword.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class Clues extends JPanel{
	/**
	 * The list containing all the clues for across.
	 */
	private JList<Clue> across;
	/**
	 * The list containing all the clues for down.
	 */
	private JList<Clue> down;
	private DefaultListModel<Clue> acrossClues;
	private DefaultListModel<Clue> downClues;
	/**
	 * The current crossword being used.
	 */
	private Crossword cross;
	/**
	 * The panel containing the visual representation of the crossword.
	 */
	private CrossPanel main;


	/**
	 * Sets up the two list, containting the across clue and the down clues.
	 */
	public Clues(){
	
		super(new GridLayout(2, 1));

		acrossClues = new DefaultListModel<Clue>();
		downClues = new DefaultListModel<Clue>();

		across = new JList<Clue>(acrossClues);
		across.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//sets the clicked on clue to be highlighted
		across.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!acrossClues.isEmpty()){

					int index = across.getSelectedIndex();
					Clue selected = (Clue) acrossClues.get(index);
					highlightSelected(selected, 1);



				}

			}

		});

		down = new JList<Clue>(downClues);
		down.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		//sets the clicked on clue to be highlighted
		down.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(!downClues.isEmpty()){
					int index = down.getSelectedIndex();
					Clue selected = (Clue) downClues.get(index);
					highlightSelected(selected, 2);
				}

			}

		});

		//adds all the labels and scroll panels to the main panel
		JScrollPane acrossScrollPane = new JScrollPane(across);
		JScrollPane downScrollPane = new JScrollPane(down);

		JPanel top = new JPanel(new BorderLayout());
		JPanel bottom = new JPanel(new BorderLayout());

		JLabel topTitle = new JLabel("Across:");
		top.add(topTitle, BorderLayout.NORTH);

		JLabel bottomTitle = new JLabel("Down:");
		bottom.add(bottomTitle, BorderLayout.NORTH);

		top.add(acrossScrollPane, BorderLayout.CENTER);
		bottom.add(downScrollPane, BorderLayout.CENTER);

		this.add(top);
		this.add(bottom);
	}

	/**
	 * Sets the crossword to use. 
	 * 
	 * @param cross The given crossword.
	 */
	public void setCrossWord(Crossword cross){
		this.cross = cross;
	}

	/**
	 * Populates the two lists with the clues. Adds every across clue to the
	 * across list and every down clue to the down list.
	 */
	public void populate(){
		acrossClues.clear();
		downClues.clear();
		ArrayList<Clue> across = cross.acrossClues;
		ArrayList<Clue> down = cross.downClues;
		for(Clue c: across){
			acrossClues.addElement(c);
		}
		for(Clue c: down){
			downClues.addElement(c);
		}
	}

	/**
	 * Selects and highlights the given clue number.
	 * 
	 * @param number The clue number.
	 */
	public void highlightClueAcross(int number){
		Clue[] array;
		Object[] original = acrossClues.toArray();
		array = new Clue[original.length];
		for(int i = 0; i < original.length; i++){
			array[i] = (Clue) original[i];
		}
		
		//loops through all the across clues checking if the number matches
		for(int i = 0; i < array.length; i++){
			if(array[i].number == number){
				across.setSelectedIndex(i);
			}
		}

	}

	/**
	 * Selects and highlights the given clue number.
	 * 
	 * @param number The clue number.
	 */
	public void highlightClueDown(int number){
		Clue[] array;
		Object[] original = downClues.toArray();
		array = new Clue[original.length];
		for(int i = 0; i < original.length; i++){
			array[i] = (Clue) original[i];
		}
		
		//loops through every clue checking if the number matches
		for(int i = 0; i < array.length; i++){
			if(array[i].number == number){

				down.setSelectedIndex(i);

			}
		}
	}

	/**
	 * Highlights the inputs on the crossword for the given clue.
	 * 
	 * @param selected The clue currently selected to be highlighted.
	 * @param direction The direction the clue is in. 1 for across and
	 * 					2 for down.
	 */
	public void highlightSelected(Clue selected, int direction){
			main.highlight(main.panelHolder[selected.y][selected.x], direction, 0);
	}

	/**
	 * Deselects any selection in both the across and down clues lists.
	 */
	public void clearAllSelection(){
		down.clearSelection();
		across.clearSelection();
	}

	/**
	 * Sets the panel containing the crossword.
	 * 
	 * @param main The crossword panel.
	 */
	public void setMainPanel(CrossPanel main){
		this.main = main;
	}

}