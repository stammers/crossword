import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;


/**
 * THe main frame of the GUI. Contains an initialisation methods to create
 * all the components, initialise them and display them in the correct places.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class CrossFrame extends JFrame {

	/**
	 * A reference to itself, needed for showing pop-up messages on
	 * screen.
	 */
	private CrossFrame frame;
	/**
	 * The panel containing the visual representation of the currently
	 * loaded crossword.
	 */
	private CrossPanel panel;
	/**
	 * The side panel containing all the clues for the current
	 * crossword.
	 */
	private Clues side;
	/**
	 * The main panel containing all the other componenets.
	 */
	private JPanel main;
	/**
	 * The crossword being displayed on screen.
	 */
	private Crossword cross;
	/**
	 * The panel containing the solved clue log.
	 */
	private SolvedPanel solvedClueSupport;
	/**
	 * The panel at the bottom of the frame, contains the anagrams
	 * and the solved clue support.
	 */
	private JPanel bottom;
	/**
	 * The panel containing the anagram generator.
	 */
	private AnagramPanel anagrams;
	protected static String user = "Default";

	/**
	 * Creates the main frame with a title.
	 * 
	 * @param title The title of the frame.
	 */
	public CrossFrame(String title){
		super(title);
		frame = this;
	}

	/**
	 * Initialises the whole GUI. Creates all the various components and adds them to the main 
	 * panel in the correct places. Maximises the screen if it can, otherwise sets a default size.
	 */
	public void init(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//sets size if not maximized
		this.setSize(900, 700);

		//sets frame to be maximized
		this.setExtendedState(Frame.MAXIMIZED_BOTH);

		MyMenu menu = new MyMenu();
		menu.init();
		this.setJMenuBar(menu);
		main = new JPanel(new BorderLayout());
		this.setContentPane(main);
		bottom = new JPanel(new GridLayout(1, 2));
		solvedClueSupport = new SolvedPanel();
		main.add(bottom, BorderLayout.SOUTH);
		anagrams = new AnagramPanel();
		anagrams.init();
		bottom.add(anagrams);
		bottom.add(solvedClueSupport);

		solvedClueSupport.setVisible(false);
		side = new Clues();

		main.add(side, BorderLayout.EAST);

		this.setVisible(true);



	}

	/**
	 * Enables solved clue support. Shows the solved log and will enable
	 * strike through of solved clues in the lists on the side.
	 */
	public void solvedCluesSupport(){
		for(Clue c: cross.acrossClues){
			c.setSupport(true);
		}
		for(Clue c: cross.downClues){
			c.setSupport(true);
		}
		solvedClueSupport.setVisible(true);
		solvedClueSupport.populate();
		side.populate();
	}

	/**
	 * Limits a file chooser to only be able to see file with a .crossword or
	 * .xml extension.
	 * 
	 * @author Daniel
	 *
	 */
	class MultiFileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			if(f.isDirectory()){
				return true;
			}else{
				String filename = f.getName();
				return filename.endsWith(".crossword") || filename.endsWith(".xml");
			}

		}

		@Override
		public String getDescription() {
			return "\"*.crossword\", \"*.xml\"";
		}

	}

	/**
	 * Limits a file chooser to only be able to see file with a .crossword
	 * extension.
	 * 
	 * @author Daniel
	 *
	 */
	class CrosswordFileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			if(f.isDirectory()){
				return true;
			}else{
				String filename = f.getName();
				return filename.endsWith(".crossword");
			}

		}

		@Override
		public String getDescription() {
			return "\"*.crossword\"";
		}

	}

	/**
	 * Limits a file chooser to only be able to see file with a .xml
	 * extension.
	 * 
	 * @author Daniel
	 *
	 */
	class XMLFileFilter extends FileFilter{

		@Override
		public boolean accept(File f) {
			if(f.isDirectory()){
				return true;
			}else{
				String filename = f.getName();
				return filename.endsWith(".xml");
			}

		}

		@Override
		public String getDescription() {
			return "\"*.xml\"";
		}

	}



	/**
	 * The menu bar at the top of the screen. Contains all the menu items
	 * and also contains the various listeners attached to them.
	 * @author Daniel
	 *
	 */
	class MyMenu extends JMenuBar{

		/**
		 * The file menu subsection.
		 */
		private JMenu file;
		/**
		 * The options menu subsection.
		 */
		private JMenu options;
		/**
		 * Represents whether solved clue support is enable or not.
		 */
		private JCheckBoxMenuItem solvedSupport;
		/**
		 * Allows the text in JTextAreas to be aligned.
		 */
		private DefaultListCellRenderer textAlign = new DefaultListCellRenderer();

		/**
		 * Initialises the menu, creates all the menu items and attached listeners
		 * where needed.
		 */
		public void init(){
			textAlign.setHorizontalAlignment(DefaultListCellRenderer.CENTER);
			file = new JMenu("File");
			this.add(file);
			options = new JMenu("Options");
			this.add(options);

			JMenuItem load = new JMenuItem("Load Crossword");
			file.add(load);

			JMenuItem loadEG = new JMenuItem("Load Example Crossword");
			file.add(loadEG);
			
			JMenuItem loadBigEG = new JMenuItem("Load Big Example Crossword");
			file.add(loadBigEG);
			
			JMenuItem loadMassiveEG = new JMenuItem("Load Massive Example Crossword");
			file.add(loadMassiveEG);
			
			JMenuItem loadHyphenEG = new JMenuItem("Load Hyphenated Example Crossword");
			file.add(loadHyphenEG);

			file.addSeparator();

			JMenuItem saveCrossword = new JMenuItem("Save \"*.crossword\"");
			file.add(saveCrossword);

			JMenuItem saveXML = new JMenuItem("Save \"*.xml\"");
			file.add(saveXML);

			solvedSupport = new JCheckBoxMenuItem("Enable Solved Clue Support?");
			options.add(solvedSupport);

			JMenuItem changeUser = new JMenuItem("Change Username");
			options.add(changeUser);

			final JCheckBoxMenuItem allAnagrams = new JCheckBoxMenuItem("Show all anagrams?");
			options.add(allAnagrams);

			final JMenuItem changeAnagramDelay = new JMenuItem("Anagram Delay");
			options.add(changeAnagramDelay);

			JMenuItem spacer = new JMenuItem();
			this.add(spacer);
			final JLabel userNameLbl = new JLabel("User: ");
			this.add(userNameLbl);

			final JTextField userName = new JTextField("Default", 5);
			this.add(userName);


			load.addActionListener(new ActionListener(){

				/* 
				 * Loads a file, either .xml or .crossword, and shows the crossword
				 * contained in that file.
				 */
				@Override
				public void actionPerformed(ActionEvent arg0) {
					
					JFileChooser fileChoose = new JFileChooser();
					fileChoose.setCurrentDirectory(new File(System.getProperty("user.dir")));
					fileChoose.setFileFilter(new MultiFileFilter());
					int result = fileChoose.showOpenDialog(frame);
					switch (result){
					case JFileChooser.APPROVE_OPTION:
						File input = fileChoose.getSelectedFile();
						
						//removes the old crossword
						if(panel != null){
							main.remove(panel);
						}
						panel = new CrossPanel(side);
						main.add(panel, BorderLayout.CENTER);

						//loads the crossword from a .crossword file
						if(input.getName().endsWith(".crossword")){
							try {
								cross = CrosswordIO.readPuzzle(input.getPath());
							} catch (IOException e) {
								JOptionPane.showMessageDialog(frame, "Error loading crossword");
							} catch (ClassNotFoundException e) {
								JOptionPane.showMessageDialog(frame, e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
							}

							//loads the crossword from a .xml file
						}else if(input.getName().endsWith(".xml")){
							try {
								cross = CrosswordIO.readXML(input.getPath());
							} catch (ParserConfigurationException e) {
								JOptionPane.showMessageDialog(frame, e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
							} catch (SAXException e) {
								JOptionPane.showMessageDialog(frame, e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
							} catch (IOException e) {
								JOptionPane.showMessageDialog(frame, "Error loading crossword");
							}
						}
						if(cross == null){
							JOptionPane.showMessageDialog(frame, "Invalid file");
							return;
						}

						//sets the newly loaded crossword up to be displayed
						frame.setTitle(cross.title);
						panel.setCrossWord(cross);
						panel.setSolved(solvedClueSupport);
						panel.init();

						side.setCrossWord(cross);
						side.populate();

						if(solvedSupport.isSelected()){
							solvedCluesSupport();

						}

						panel.fillCorrect();
						revalidate();
						break;
					}

				}

			});

			saveCrossword.addActionListener(new ActionListener(){

				/* 
				 * Saves the currently open crossword into a .crossword file.
				 */
				@Override
				public void actionPerformed(ActionEvent args) {

					//will only save if a crossword is open
					if(cross !=null){
						final JFileChooser fileChoose = new JFileChooser();
						fileChoose.setCurrentDirectory(new File(System.getProperty("user.dir")));
						fileChoose.setFileFilter(new CrosswordFileFilter());
						final int result = fileChoose.showSaveDialog(frame);
						@SuppressWarnings("rawtypes")
						SwingWorker save = new SwingWorker(){

							@Override
							protected Object doInBackground() throws Exception {
								switch (result){
								case JFileChooser.APPROVE_OPTION:

									//creates the file chosen by the user
									File output = fileChoose.getSelectedFile();
									String filename = output.getPath();

									//adds the extension if they forgot when naming it
									if(!filename.endsWith(".crossword")){
										filename = filename + ".crossword";
									}
									try {
										CrosswordIO.writePuzzle(cross, filename);
										JOptionPane.showMessageDialog(frame, "Crossword Saved");
									} catch (IOException e) {
										JOptionPane.showMessageDialog(frame, "Error saving, please try again");
									}

								}								
								return null;
							}

						};
						save.execute();

					}else{
						JOptionPane.showMessageDialog(frame, "No Crossword open to save");
					}

				}

			});

			saveXML.addActionListener(new ActionListener(){

				/* 
				 * Saves the currently open crossword as a .xml file.
				 */
				@Override
				public void actionPerformed(ActionEvent arg0) {

					//will only save if a crossword is open
					if(cross !=null){
						final JFileChooser fileChoose = new JFileChooser();
						fileChoose.setCurrentDirectory(new File(System.getProperty("user.dir")));
						fileChoose.setFileFilter(new XMLFileFilter());
						final int result = fileChoose.showSaveDialog(frame);
						@SuppressWarnings("rawtypes")
						SwingWorker save = new SwingWorker(){
							@Override
							protected Object doInBackground() throws Exception {

								switch (result){
								case JFileChooser.APPROVE_OPTION:

									//creates the file chosen by the user
									File output = fileChoose.getSelectedFile();
									String filename = output.getPath();

									//adds the extension if forgotten
									if(!filename.endsWith(".xml")){
										filename = filename + ".xml";
									}
									try {
										CrosswordIO.writeXML(cross, filename);
										JOptionPane.showMessageDialog(frame, "Crossword Saved");
									} catch (IOException e) {
										JOptionPane.showMessageDialog(frame, "Error saving, please try again", "Error Saving", JOptionPane.ERROR_MESSAGE);
									} catch (ParserConfigurationException e) {
										JOptionPane.showMessageDialog(frame, e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
									} catch (SAXException e) {
										JOptionPane.showMessageDialog(frame, e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
									} catch (TransformerException e) {
										JOptionPane.showMessageDialog(frame, e.getMessage(), "Error Saving", JOptionPane.ERROR_MESSAGE);
									}

								}
								return null;
							}

						};
						save.execute();

					}else{
						JOptionPane.showMessageDialog(frame, "No Crossword open to save");
					}

				}

			});

			solvedSupport.addActionListener(new ActionListener(){

				/* 
				 * Enables or disables solved clue support depending on selected
				 * option.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					if(cross !=null){
						if(solvedSupport.isSelected()){
							solvedCluesSupport();
						}else{
							solvedClueSupport.setVisible(false);
							for(Clue c: cross.acrossClues){
								c.setSupport(false);
							}
							for(Clue c: cross.downClues){
								c.setSupport(false);
							}
							side.populate();
						}
					}

				}

			});

			loadEG.addActionListener(new ActionListener(){

				/* 
				 * Loads the example crossword and sets it up to be displayed.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					if(panel != null){
						main.remove(panel);
					}
					panel = new CrossPanel(side);
					main.add(panel, BorderLayout.CENTER);
					CrosswordExample eg = new CrosswordExample();
					cross = eg.getPuzzle();


					frame.setTitle(cross.title);
					panel.setCrossWord(cross);
					panel.setSolved(solvedClueSupport);
					panel.init();

					side.setCrossWord(cross);
					side.populate();

					if(solvedSupport.isSelected()){
						solvedCluesSupport();
					}
					revalidate();


				}

			});
			
			loadBigEG.addActionListener(new ActionListener(){

				/* 
				 * Loads the example crossword and sets it up to be displayed.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					if(panel != null){
						main.remove(panel);
					}
					panel = new CrossPanel(side);
					main.add(panel, BorderLayout.CENTER);
					CrosswordBigExample eg = new CrosswordBigExample();
					cross = eg.getPuzzle();


					frame.setTitle(cross.title);
					panel.setCrossWord(cross);
					panel.setSolved(solvedClueSupport);
					panel.init();

					side.setCrossWord(cross);
					side.populate();

					if(solvedSupport.isSelected()){
						solvedCluesSupport();
					}
					revalidate();


				}

			});
			
			loadMassiveEG.addActionListener(new ActionListener(){

				/* 
				 * Loads the example crossword and sets it up to be displayed.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					if(panel != null){
						main.remove(panel);
					}
					panel = new CrossPanel(side);
					main.add(panel, BorderLayout.CENTER);
					CrosswordMassiveExample eg = new CrosswordMassiveExample();
					cross = eg.getPuzzle();


					frame.setTitle(cross.title);
					panel.setCrossWord(cross);
					panel.setSolved(solvedClueSupport);
					panel.init();

					side.setCrossWord(cross);
					side.populate();

					if(solvedSupport.isSelected()){
						solvedCluesSupport();
					}
					revalidate();


				}

			});
			
			loadHyphenEG.addActionListener(new ActionListener(){

				/* 
				 * Loads the example crossword and sets it up to be displayed.
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
					if(panel != null){
						main.remove(panel);
					}
					panel = new CrossPanel(side);
					main.add(panel, BorderLayout.CENTER);
					CrosswordHyphenExample eg = new CrosswordHyphenExample();
					cross = eg.getPuzzle();


					frame.setTitle(cross.title);
					panel.setCrossWord(cross);
					panel.setSolved(solvedClueSupport);
					panel.init();

					side.setCrossWord(cross);
					side.populate();

					if(solvedSupport.isSelected()){
						solvedCluesSupport();
					}
					revalidate();


				}

			});

			changeUser.addActionListener(new ActionListener(){

				/* 
				 * Changes the username of the current user using the input box.
				 */
				@Override
				public void actionPerformed(ActionEvent arg0) {
						String temp = JOptionPane.showInputDialog(frame, "Enter new username: ", "Username", 1);
						if(temp.equals("SOLVEALL")){
							panel.solve();
						}else{
							user = temp;
							JOptionPane.showMessageDialog(frame, "User changed to " + temp);
						}

				}

			});

			userName.addActionListener(new ActionListener(){

				/* 
				 * Changes the user name using the textfield
				 */
				@Override
				public void actionPerformed(ActionEvent e) {
						String temp = userName.getText();
						if(temp.equals("SOLVEALL")){
							panel.solve();
							userName.setText(user);
						}else{
							user = temp;
							JOptionPane.showMessageDialog(frame, "User changed to " + temp);
						}

				}

			});

			allAnagrams.addActionListener(new ActionListener(){

				/* 
				 * Enables or disables the option to see all the anagrams depending on user
				 * chosen option.
				 */
				@Override
				public void actionPerformed(ActionEvent arg0) {
					if(allAnagrams.isSelected()){
						anagrams.setShow(true);
					}else{
						anagrams.setShow(false);
					}

				}

			});

			changeAnagramDelay.addActionListener(new ActionListener(){

				/* 
				 * Changes the timing of anagram display to the selected user option.
				 */
				@Override
				public void actionPerformed(ActionEvent arg0) {

					//creates an option frame containing the combo box with all the available
					//options
					JFrame optionsFrame = new JFrame("Set Anagram Delay");
					int x = changeAnagramDelay.getX();
					int y = changeAnagramDelay.getY();
					optionsFrame.setBounds(x, y, 250, 100);
					optionsFrame.setResizable(false);
					JPanel optionsPanel = new JPanel();

					optionsPanel.setLayout(new BorderLayout());
					JLabel timer = new JLabel("Select Delay:", SwingConstants.CENTER);
					optionsPanel.add(timer, BorderLayout.NORTH);
					String[] times = {"0.1s", "0.5s", "1s", "2s", "3s", "4s", "5s", "10s"};
					final JComboBox<String> timesList = new JComboBox<String>(times);
					timesList.setRenderer(textAlign);

					//sets the selected option to the current anagram delay
					int delay = anagrams.getDelay();
					switch (delay){
					case 100:
						timesList.setSelectedIndex(0);
						break;
					case 500:
						timesList.setSelectedIndex(1);
						break;
					case 1000:
						timesList.setSelectedIndex(2);
						break;
					case 2000:
						timesList.setSelectedIndex(3);
						break;
					case 3000:
						timesList.setSelectedIndex(4);
						break;
					case 4000:
						timesList.setSelectedIndex(5);
						break;
					case 5000:
						timesList.setSelectedIndex(6);
						break;
					case 10000:
						timesList.setSelectedIndex(7);
						break;
					}
					optionsPanel.add(timesList, BorderLayout.CENTER);
					timesList.addActionListener(new ActionListener(){

						/* 
						 * Converts the user choice to the anagram display delay.
						 */
						@Override
						public void actionPerformed(ActionEvent arg0) {
							int selected = timesList.getSelectedIndex();
							switch (selected){
							case 0:
								anagrams.setDelay(100);
								break;
							case 1:
								anagrams.setDelay(500);
								break;
							case 2:
								anagrams.setDelay(1000);
								break;
							case 3:
								anagrams.setDelay(2000);
								break;
							case 4:
								anagrams.setDelay(3000);
								break;
							case 5:
								anagrams.setDelay(4000);
								break;
							case 6:
								anagrams.setDelay(5000);
								break;
							case 7:
								anagrams.setDelay(10000);
								break;
							}
						}
					});
					optionsFrame.setContentPane(optionsPanel);
					optionsFrame.setVisible(true);

				}

			});
		}
	}

	/**
	 * The solved clues support panel. When enabled it will show all solved clues,
	 * and will also show the user and time it was solved.
	 * 
	 * @author Daniel
	 *
	 */
	class SolvedPanel extends JPanel {

		/**
		 * The text area showing all the solved clues.
		 */
		private JTextArea solved;

		public SolvedPanel(){
			this.setLayout(new BorderLayout());
			solved = new JTextArea();
			JScrollPane solvedScroll = new JScrollPane(solved);
			this.add(solvedScroll, BorderLayout.CENTER);
			solved.setRows(7);
		}

		/**
		 * Populates the textarea with all the currently solved clues, who they were
		 * solved by and at what time and date.
		 */
		public void populate(){
			solved.setText("");
			solved.append("Across:\n");
			ArrayList<Clue> across = cross.acrossClues;
			ArrayList<Clue> down = cross.downClues;
			for(Clue c: across){
				if(c.solved()){
					//removes the html strike through formatting of the clue
					//before adding to the solved list
					String clue = c.toString().replaceAll("\\<.*?>","");
					solved.append(clue +  " " + c.getSolved()+"\n");
				}
			}
			solved.append("Down:\n");
			for(Clue c: down){
				if(c.solved()){
					//removes the html strike through formatting of the clue
					//before adding to the solved list
					String clue = c.toString().replaceAll("\\<.*?>","");
					solved.append(clue + " " + c.getSolved()+"\n");
				}
			}
		}



	}

}
