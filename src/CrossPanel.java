import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * The panel containing the visual representation of the crossword puzzle
 * and contains all the methods needed in order to highlight clues and enable
 * the user to type correctly flowing across the textfields.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class CrossPanel extends JPanel {

	/**
	 * The currently loaded crossword.
	 */
	private Crossword cross;
	/**
	 * An array containing all the panels in the crossword.
	 */
	protected GridPanel[][] panelHolder;
	/**
	 * The panel at the side containing a display of all the clues.
	 */
	private Clues side;
	/**
	 * The size of the crossword.
	 */
	private int size;
	/**
	 * Allows the time and date to be output in a specific format.
	 */
	private SimpleDateFormat df = new SimpleDateFormat("E dd/MM/yyyy 'at' HH:mm:ss");
	/**
	 * The panel containing the log of solved clues if turned on.
	 */
	private CrossFrame.SolvedPanel solved;

	public CrossPanel(Clues side){
		this.side = side;
	}

	/**
	 * Sets the currently loaded crossword.
	 * 
	 * @param c The current crossword.
	 */
	public void setCrossWord(Crossword c){
		this.cross = c;
	}

	/**
	 * Sets the solved clue log panel.
	 * 
	 * @param solved
	 */
	public void setSolved(CrossFrame.SolvedPanel solved){
		this.solved = solved;
	}

	/**
	 * Initialises the crossword, drawing out all the grid with the numbers and
	 * text entry in the correct places.
	 */
	public void init(){

		side.setMainPanel(this);


		size = cross.size;

		this.setLayout(new GridLayout(size, size));


		panelHolder = new GridPanel[size][size];

		//creates the grid of panels showing the crossword
		for(int m = 0; m < size; m++) {
			for(int n = 0; n < size; n++) {
				panelHolder[m][n] = new GridPanel();
				panelHolder[m][n].setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));
				panelHolder[m][n].setBackground(Color.BLACK);
				this.add(panelHolder[m][n]);
			}
		}

		ArrayList<Clue> across = cross.acrossClues;
		ArrayList<Clue> down = cross.downClues;

		//loops adding textfields for each clue in the across clues
		for(Clue c: across){

			int answer = c.answer.length();
			for(int i = 0; i < answer; i++){
				panelHolder[c.y][c.x+i].setLayout(new BorderLayout());

				//adds the text entry field to each grid position
				JTextField enter = new JTextField();
				enter.setBorder(null);
				enter.setHorizontalAlignment(JTextField.CENTER);
				enter.addMouseListener(new ToggleListener(panelHolder[c.y][c.x+i]));
				enter.addKeyListener(new KeyboardFocus(panelHolder[c.y][c.x+i]));
				enter.setDocument(new CharacterLimit(1));
				enter.setFocusTraversalKeysEnabled(false);
				enter.setOpaque(false);

				panelHolder[c.y][c.x+i].add(enter, BorderLayout.CENTER);
				panelHolder[c.y][c.x+i].setBackground(Color.WHITE);

				//sets the pointer to the next panel in the current clue
				int nextX;
				if(i+1 == answer){
					nextX = -1;
				}else{
					nextX = c.x+i+1;
				}

				//sets the pointer to the previous panel in the current clue
				int previousX;
				if(i-1 < 0){
					previousX = -1;
				}else{
					previousX = c.x+i-1;
				}
				panelHolder[c.y][c.x+i].set(0, c.number, -1,  nextX, -1, previousX, -1, c.x, c.y);
				//panelHolder[c.y][c.x+i].repaint();
			}

		}

		//loops adding textfields for each clue in the down clues
		for(Clue c: down){

			int answer = c.answer.length();
			boolean alreadyExists;
			for(int i = 0; i < answer; i++){
				alreadyExists = false;

				//checks if the grid position already has a text field
				JTextField exists = panelHolder[c.y+i][c.x].textEnter();
				if(exists !=null){
					alreadyExists = true;
				}

				//sets the pointer to the next panel in the current clue
				int nextY;
				if(i+1 == answer){
					nextY = -1;
				}else{
					nextY = c.y+i+1;
				}
				int nextX = panelHolder[c.y+i][c.x].getNextX();
				int direction = panelHolder[c.y+i][c.x].getDirection();
				if(direction == -1){
					direction = 1;
				}else if(direction == 0){
					direction = 2;
				}

				//sets the pointer to the previous panel in the current clue
				int previousX = panelHolder[c.y+i][c.x].getPreviousX();
				int previousY;
				if(i-1 < 0){
					previousY = -1;
				}else{
					previousY = c.y+i-1;
				}
				int numberAcross = panelHolder[c.y+i][c.x].getNumberAcross();
				
				//if it already exits then the new data is just added to the existing panel
				if(alreadyExists){
					int y = panelHolder[c.y+i][c.x].getYCo();
					panelHolder[c.y+i][c.x].set(direction, numberAcross, c.number, nextX, nextY, previousX, previousY, c.x, y);

				}else{
					//adds the text entry field to each grid position
					panelHolder[c.y+i][c.x].set(direction, numberAcross, c.number, nextX, nextY, previousX, previousY, c.x, c.y);

					panelHolder[c.y+i][c.x].setLayout(new BorderLayout());
					JTextField enter = new JTextField();
					enter.setBorder(null);
					enter.setHorizontalAlignment(JTextField.CENTER);
					enter.addMouseListener(new ToggleListener(panelHolder[c.y+i][c.x]));
					enter.addKeyListener(new KeyboardFocus(panelHolder[c.y+i][c.x]));
					enter.setDocument(new CharacterLimit(1));
					enter.setOpaque(false);
					panelHolder[c.y+i][c.x].add(enter, BorderLayout.CENTER);
					panelHolder[c.y+i][c.x].setBackground(Color.WHITE);
				}
			}

		}

	}


	/**
	 * This was just used for testing but can be activated by entering "SOLVEALL" as the
	 * username. It will solve all the clues and input the answers into the correct
	 * places on the display.
	 */
	public void solve(){
		ArrayList<Clue> across = cross.acrossClues;
		ArrayList<Clue> down = cross.downClues;

		//solves all the across clues
		for(Clue c: across){

			int answer = c.answer.length();
			for(int i = 0; i < answer; i++){
				Component[] text = panelHolder[c.y][c.x+i].getComponents();
				JTextField answerLetter = null;
				for(Component comp: text){
					if(comp instanceof JTextField){
						answerLetter = (JTextField) comp;
					}
				}
				String letter = ""+ c.answer.charAt(i);
				answerLetter.setText(letter);
			}

		}

		//solves all the down clues
		for(Clue c: down){

			int answer = c.answer.length();
			for(int i = 0; i < answer; i++){
				Component[] text = panelHolder[c.y+i][c.x].getComponents();
				JTextField answerLetter = null;
				for(Component comp: text){
					if(comp instanceof JTextField){
						answerLetter = (JTextField) comp;
					}
				}
				String letter = "" + c.answer.charAt(i);
				answerLetter.setText(letter);

			}

		}

	}

	/**
	 * Highlights the selected panel in the grid one colour, and will highlight all the
	 * other panels in that clue another colour. Will toggle the highlighted clue for those
	 * panels which are in both an across and a down clue. Will highlight the panel with the keyboard
	 * focus as the selected panel when typing in an answer. 
	 * 
	 * @param clicked The selected panel to be highlighted.
	 * @param clueDirection The direction of the clue clicked on in the sidepanel. 1 means across and 2 means down.
	 * @param typing Whether the user is typing or not, 0 means not, 1 means typing.
	 */
	public void highlight(GridPanel clicked, int clueDirection, int typing){
		//resets the highlighting of all the panels
		for(int m = 0; m < size; m++) {
			for(int n = 0; n < size; n++) {
				if(panelHolder[m][n].textEnter() !=null){
					panelHolder[m][n].setBackground(Color.WHITE);
				}
			}
		}
		side.clearAllSelection();
		int direction = clicked.getDirection();
		if(typing == 1){
			direction = clueDirection;
		}
		Color highlight = Color.YELLOW;

		//toggles between directions when a panel is in both
		if(direction == 2){
			if(clueDirection == 1){
				direction = 0;
			}else if(clueDirection == 2){
				direction = 1;
			}else{

				//will only change direction if you're not typing
				if(typing == 0){
					direction = clicked.getToggle();
					if(direction == 1){
						clicked.setToggleDirection(0);
					}else{
						clicked.setToggleDirection(1);
					}
				}else{
					direction = clueDirection;
				}

			}
		}
		if(typing == 0){
			clicked.setTypeDirection(direction);
		}
		
		//highlights the selected panel one colour
		clicked.setBackground(Color.GREEN);

		//if direction is across loops forward and backward from selected over all the other
		//panels in the clue, highlighting them.
		if(direction == 0){
			int nextX = clicked.getNextX();
			int y = clicked.getYCo();
			int numberAcross = clicked.getNumberAcross();
			side.highlightClueAcross(numberAcross);
			while(nextX != -1){
				panelHolder[y][nextX].setBackground(highlight);

				//will only change direction if you're not typing
				if(typing == 0){
					panelHolder[y][nextX].setTypeDirection(direction);
				}
				nextX = panelHolder[y][nextX].getNextX();
			}
			int previousX = clicked.getPreviousX();
			while(previousX !=-1){
				panelHolder[y][previousX].setBackground(highlight);

				//will only change direction if you're not typing
				if(typing == 0){
					panelHolder[y][previousX].setTypeDirection(direction);
				}
				previousX = panelHolder[y][previousX].getPreviousX();
			}
		}
		//if direction is down loops up and down from selected over all the other
		//panels in the clue, highlighting them.
		if(direction == 1){
			int nextY = clicked.getNextY();
			int x = clicked.getXCo();
			int numberDown = clicked.getNumberDown();
			side.highlightClueDown(numberDown);
			while(nextY != -1){
				panelHolder[nextY][x].setBackground(highlight);

				//will only change direction if you're not typing
				if(typing == 0){
					panelHolder[nextY][x].setTypeDirection(direction);
				}
				nextY = panelHolder[nextY][x].getNextY();
			}
			int previousY = clicked.getPreviousY();
			while(previousY !=-1){
				panelHolder[previousY][x].setBackground(highlight);

				//will only change direction if you're not typing
				if(typing == 0){
					panelHolder[previousY][x].setTypeDirection(direction);
				}
				previousY = panelHolder[previousY][x].getPreviousY();
			}
		}
	}

	/**
	 * Fills in all the answers for the clues which have been marked 
	 * as solved. Used when loading a crossword from a file.
	 */
	public void fillCorrect() {
		ArrayList<Clue> across = cross.acrossClues;
		ArrayList<Clue> down = cross.downClues;

		//loops for all the clues across, checking if they are solved.
		for(Clue c: across){
			if(c.solved()){
				int answer = c.answer.length();
				for(int i = 0; i < answer; i++){
					JTextField answerLetter = panelHolder[c.y][c.x+i].textEnter();
					String letter = ""+ c.answer.charAt(i);
					answerLetter.setText(letter);
				}	
			}
		}

		//loops for all the clues down, checking if they are solved
		for(Clue c: down){
			if(c.solved()){
				int answer = c.answer.length();
				for(int i = 0; i < answer; i++){
					JTextField answerLetter = panelHolder[c.y+i][c.x].textEnter();
					String letter = "" + c.answer.charAt(i);
					answerLetter.setText(letter);

				}
			}

		}

	}

	/**
	 * Checks a given answer against a given clue and it's direction. 
	 * If the answer is correct the clue is marked as solved and the user and
	 * time and date it was solved are saved, if not then it is marked as unsolved.
	 * 
	 * @param clue The clue number to check against.
	 * @param direction The direction the clue is in, 0 means across and 1 means down.
	 * @param answer The given answer to check against the clue.
	 */
	public void checkCorrect(int clue, int direction, String answer){
		if(direction == 0){
			int id = -1;
			
			//finds the correct clue from the given number
			for(int i = 0; i < cross.acrossClues.size(); i++){
				if(cross.acrossClues.get(i).number == clue){
					id = i; 
				}
			}
			String correctAnswer = cross.acrossClues.get(id).answer.toLowerCase();
			//if correct then marked as solved and the lists are updated.
			if(correctAnswer.equals(answer.toLowerCase())){
				cross.acrossClues.get(id).setSolved(true, CrossFrame.user, df.format(new Date()));
				side.populate();
				solved.populate();
				
			//if wrong then the clue is marked as unsolved and the lists updated
			}else{
				cross.acrossClues.get(id).setSolved(false, null, null);
				side.populate();
				solved.populate();
			}
		}else{
			int id = -1;
			
			//finds the correct clue from the given number
			for(int i = 0; i < cross.downClues.size(); i++){
				if(cross.downClues.get(i).number == clue){
					id = i; 
				}
			}
			String correctAnswer = cross.downClues.get(id).answer.toLowerCase();
			
			//if the answer is correct the clue is marked as solved and the lists updated
			if(correctAnswer.equals(answer.toLowerCase())){
				cross.downClues.get(id).setSolved(true, CrossFrame.user, df.format(new Date()));
				side.populate();
				solved.populate();
				
			//if wrong then the clue is marked as unsolved and the lists updated
			}else{
				cross.downClues.get(id).setSolved(false, null, null);
				side.populate();
				solved.populate();
			}
		}
	}

	/**
	 * Moves the keyboard focus along once a character has been typed.
	 * @author Daniel
	 *
	 */
	class KeyboardFocus implements KeyListener{

		/**
		 * The panel this instance of the listener is attached to
		 */
		private GridPanel typed;

		public KeyboardFocus(GridPanel typed){
			this.typed = typed;
		}

		@Override
		public void keyPressed(KeyEvent e) {}

		@Override
		public void keyReleased(KeyEvent arg0) {}

		/* 
		 * Moves the keyboard focus along to the next or previous panel in the current clue
		 * depending on the character typed in. Checks if the currently entered answer in that
		 * clue is correct everytime a character is entered.
		 */
		@Override
		public void keyTyped(KeyEvent e) {
			int direction = typed.getTypeDirection();
			
			//if a backspace was pressed the focus is passed back to the previous panel
			if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE){
				JTextField enter = null;
				
				//for across clues
				if(direction == 0){
					if(typed.getPreviousX() != -1){
						enter = panelHolder[typed.getYCo()][typed.getPreviousX()].textEnter();
						highlight(panelHolder[typed.getYCo()][typed.getPreviousX()], 0, 1);
					}
					
				//for down clues
				}else{
					if(typed.getPreviousY() != -1){
						enter = panelHolder[typed.getPreviousY()][typed.getXCo()].textEnter();

						highlight(panelHolder[typed.getPreviousY()][typed.getXCo()], 1, 1);
					}
				}
				if(enter != null){
					enter.requestFocus();
				}
			//if any other character was entered the focus is passed onto the next panel
			}else{
				JTextField enter = null;
				
				//for across clues
				if(direction == 0){
					if(typed.getNextX() != -1){
						enter = panelHolder[typed.getYCo()][typed.getNextX()].textEnter();
						highlight(panelHolder[typed.getYCo()][typed.getNextX()], 0, 1);
					}
					int atEnd = typed.getNextX();
					GridPanel tempPanel = typed;
					while(atEnd !=-1){
						tempPanel = panelHolder[typed.getYCo()][atEnd];
						atEnd = tempPanel.getNextX();
					}
					int previousX = tempPanel.getPreviousX();
					String answer = "" + e.getKeyChar();
					while(previousX != -1){
						answer = panelHolder[tempPanel.getYCo()][previousX].textEnter().getText() + answer;
						previousX = panelHolder[tempPanel.getYCo()][previousX].getPreviousX();
					}
					checkCorrect(tempPanel.numberAcross, 0, answer);
				
				//for down clues
				}else{
					if(typed.getNextY() != -1){
						enter = panelHolder[typed.getNextY()][typed.getXCo()].textEnter();
						highlight(panelHolder[typed.getNextY()][typed.getXCo()], 1, 1);


					}
					int atEnd = typed.getNextY();
					GridPanel tempPanel = typed;
					while(atEnd !=-1){
						tempPanel = panelHolder[atEnd][typed.getXCo()];
						atEnd = tempPanel.getNextY();
					}
					int previousY = tempPanel.getPreviousY();
					String answer = "" + e.getKeyChar();
					while(previousY != -1){
						answer = panelHolder[previousY][tempPanel.getXCo()].textEnter().getText() + answer;
						previousY = panelHolder[previousY][tempPanel.getXCo()].getPreviousY();
					}
					checkCorrect(tempPanel.numberDown, 1, answer);
				}
				if(enter != null){
					enter.requestFocus();
				}
			}


		}

	}

	/**
	 * Sets the panel clicked on and the rest for the clue it belongs
	 * to to be highlighted.
	 * 
	 * @author Daniel
	 *
	 */
	class ToggleListener implements MouseListener{

		/**
		 * The panel this listener is attached to.
		 */
		private GridPanel clicked;

		public ToggleListener(GridPanel clicked){
			this.clicked = clicked;
		}

		/* 
		 * When a panel is clicked on it is passed to highlight to be highlighted.
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			highlight(clicked, 0, 0);
		}

		@Override
		public void mouseEntered(MouseEvent e) {}

		@Override
		public void mouseExited(MouseEvent e) {}

		@Override
		public void mousePressed(MouseEvent e) {}

		@Override
		public void mouseReleased(MouseEvent e) {}
	}



	/**
	 * The panels which make up the grid of the crossword.
	 * 
	 * @author Daniel
	 *
	 */
	class GridPanel extends JPanel{

		/**
		 * The direction of the clue the panel belongs to, 0 for across, 1 for down and 2 for both.
		 */
		private int direction;
		/**
		 * The number of the across clue it belongs to, null if none.
		 */
		private int numberAcross;
		/**
		 * The number of the down clue it belongs to, null if none.
		 */
		private int numberDown;
		/**
		 * The x co-ordinate of the next panel in the clues across, -1 if none.
		 */
		private int nextX;
		/**
		 * The y co-ordinate of the next panel in the clues down, -1 if none.
		 */
		private int nextY;
		/**
		 * The x co-ordinate of the previous panel in the clues across, -1 if none.
		 */
		private int previousX;
		/**
		 * The y co-ordinate of the previous panel in the clues across, -1 if none.
		 */
		private int previousY;
		/**
		 * If the panel is in both directions this allows holds a secondary direction, 0 for across 
		 * and 1 for down, allowing the direction to be toggled.
		 */
		private int toggleDirection;
		/**
		 * Holds the direction currently being typed in across this panel.
		 */
		private int typeDirection;
		/**
		 * Holds the x co-ordinate of the panel relative to the crossword size.
		 */
		private int xCo;
		/**
		 * Holds the y co-ordinate of the panel relative to the crossword size.
		 */
		private int yCo;

		public GridPanel(){
			direction = -1;
			nextX = -1;
			nextY = -1;
			previousX = -1;
			previousY = -1;
			toggleDirection = 0;
		}



		/**
		 * Sets all the parameters for the panel.
		 * 
		 * @param direction The direction of the clue.
		 * @param numberAcross The number of the across clue.
		 * @param numberDown The number of the down clue.
		 * @param nextX Co-ordinate of next across panel.
		 * @param nextY Co-ordinate of next down panel.
		 * @param previousX Co-ordinate of previous across panel.
		 * @param previousY Co-ordinate of previous down panel.
		 * @param xCo X co-ordinate of this panel.
		 * @param yCo Y co-ordinate of this panel.
		 */
		public void set(int direction, int numberAcross, int numberDown, int nextX, int nextY, int previousX, int previousY, int xCo, int yCo){
			this.direction = direction;
			this.numberAcross = numberAcross;
			this.numberDown = numberDown;
			this.nextX = nextX;
			this.nextY = nextY;
			this.previousX = previousX;
			this.previousY = previousY;
			this.xCo = xCo;
			this.yCo = yCo;
		}
		
		/* 
		 * Paints the clue number into the top left corner of the panel only if it the beginning of a clue.
		 */
		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(previousX == -1){
				if(numberAcross > 0){
					g.drawString(""+numberAcross, 1, 11);
				}
			}
			if(previousY == -1){
				if(numberDown > 0){
					g.drawString(""+numberDown, 1, 11);
				}
			}
		}

		/**
		 * Sets the togglable direction to the direction given.
		 * 
		 * @param toggle Int representation of the direction.
		 */
		public void setToggleDirection(int toggle){
			toggleDirection = toggle;
		}

		/**
		 * Sets the direction being typed in to the given direction.
		 * 
		 * @param direction Int representation of the direction.
		 */
		public void setTypeDirection(int direction){
			typeDirection = direction;
		}

		/**
		 * Returns the togglable direction of clue this panel is part of.
		 * 
		 * @return Int representation of the direction
		 */
		public int getToggle(){
			return toggleDirection;
		}

		/**
		 * Returns the direction being typed in on this panel.
		 * 
		 * @return Int representation of the direction.
		 */
		public int getTypeDirection(){
			return typeDirection;
		}

		/**
		 * Returns the direction of clue this panel is part of.
		 * 
		 * @return Int representation of the direction.
		 */
		public int getDirection(){
			return direction;
		}

		/**
		 * Returns the number of the clue down this panel belongs to.
		 * 
		 * @return The clue number
		 */
		public int getNumberAcross(){
			return numberAcross;
		}

		/**
		 * Returns the number of the clue down this panel belongs to.
		 * 
		 * @return The clue number
		 */
		public int getNumberDown(){
			return numberDown;
		}

		/**
		 * Returns the x co-ordinate of the next panel in the across clue.
		 * 
		 * @return The x co-ordinate.
		 */
		public int getNextX(){
			return nextX;
		}

		/**
		 * Returns the y co-ordinate of the next panel in the down clue.
		 * 
		 * @return The y co-ordinate.
		 */
		public int getNextY(){
			return nextY;
		}

		/**
		 * Returns the x co-ordinate of the previous panel in the across clue.
		 * 
		 * @return The x co-ordinate.
		 */
		public int getPreviousX(){
			return previousX;
		}

		/**
		 * Returns the y co-ordinate of the previous panel in the down clue.
		 * 
		 * @return The y co-ordinate.
		 */
		public int getPreviousY(){
			return previousY;
		}

		/**
		 * Returns the y co-ordinate of this panel.
		 * 
		 * @return The y co-ordinate.
		 */
		public int getYCo() {
			return yCo;
		}

		/**
		 * Returns the x co-ordinate of this panel.
		 * 
		 * @return The x co-ordinate.
		 */
		public int getXCo() {
			return xCo;
		}

		/**
		 * Allows the textfield attached to this panel to easily be accessed
		 * if there is one.
		 * 
		 * @return The textfield attached to this panel.
		 */
		public JTextField textEnter(){
			Component[] c = this.getComponents();
			JTextField returnable = null;
			for(Component comp: c){
				if(comp instanceof JTextField){
					returnable = (JTextField) comp;
				}
			}
			return returnable;
		}

	}



}


