import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;


/**
 * A panel containing components needed for entry of a string and output 
 * of all anagrams of that string. Also contains the thread which will calculate
 * those anagrams,
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class AnagramPanel extends JPanel {
	/**
	 * The textfield into which the word for which anagrams need to
	 * be found is entered.
	 */
	private JTextField anagramString;
	/**
	 * The text area which will show the anagrams as they are found.
	 */
	private JTextArea anagrams;
	/**
	 * Whether all the anagrams should be shown or just one.
	 */
	private boolean showAll;
	/**
	 * The time delay for anagram output in milliseconds.
	 */
	private int delay;

	/**
	 * Initialises all the components of the panel.
	 */
	public void init(){
		this.setLayout(new BorderLayout());
		JPanel top = new JPanel(new BorderLayout());
		JPanel entry = new JPanel(new FlowLayout());
		top.add(new JLabel("Anagrams", SwingConstants.CENTER), BorderLayout.NORTH);
		entry.add(new JLabel("Enter Word: ", SwingConstants.CENTER));
		anagramString = new JTextField(20);
		entry.add(anagramString);
		JButton go = new JButton("Go");
		entry.add(go);
		delay = 4000;

		//starts the anagram thread outputting anagrams for the given input
		//only if the input is longer than two characters
		go.addMouseListener(new MouseAdapter(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				String original = anagramString.getText();
				if(original.length() > 2){
					AnagramWorker solver = new AnagramWorker(original);
					solver.execute();
				}

			}

		});
		top.add(entry, BorderLayout.CENTER);
		this.add(top, BorderLayout.NORTH);
		anagrams = new JTextArea();
		JScrollPane anagramScroller = new JScrollPane(anagrams);
		this.add(anagramScroller, BorderLayout.CENTER);
		showAll = false;
		anagrams.setRows(5);
	}

	/**
	 * Sets whether to show all anagrams of the word, or to overwrite each one after the
	 * delay time.
	 * 
	 * @param showAll Show all the anagrams.
	 */
	public void setShow(boolean showAll){
		this.showAll = showAll;
	}
	
	/**
	 * Returns the current set delay time of anagram output.
	 * 
	 * @return The time delay in milliseconds.
	 */
	public int getDelay(){
		return delay;
	}
	
	/**
	 * Sets the delay in milliseconds of the output of anagrams.
	 * 
	 * @param delay Time delay in milliseconds.
	 */
	public void setDelay(int delay){
		this.delay = delay;
	}

	

	/**
	 * A thread which will output all the anagrams of any given string.
	 * 
	 * @author Daniel
	 *
	 */
	class AnagramWorker extends SwingWorker<String, String>{

		private String original;
		private StringBuffer buffer = new StringBuffer();

		public AnagramWorker(String original){
			this.original = original;
		}

		@Override
		protected String doInBackground() throws Exception {
			anagram("", original);
			return null;
		}
		
		private void anagram(String front, String end){
			if(end.length() == 0){
				buffer.append(front + end+"\n");
				publish(buffer.toString());
				buffer.delete(0, front.length()+end.length()+1);
				try {Thread.sleep(delay);} catch (InterruptedException ie){}
			}else{
				
				//loops for each letter in the suffix
				for(int i = 0; i < end.length(); i++){
					//the new prefix is the old prefix + the letter at point i in the remaining suffix
					String newFront = front + end.charAt(i);
					
					//the new suffix is the rest of the suffix, excluding the character taken out above
					String newEnd = end.substring(0, i)+end.substring(i+1);
					
					anagram(newFront, newEnd);
				}
			}
		}

		/* 
		 * If show all is selected then all the anagrams of the word are
		 * printed out, if not then every time a new word is output the 
		 * older one is overwritten.
		 */
		protected void process(List<String> cs){
			if(showAll){
				for (String c : cs) {
					anagrams.append(c);
				}
			}else{
				for (String c : cs) {
					anagrams.setText(c);
				}
			}

		}

	}
}
