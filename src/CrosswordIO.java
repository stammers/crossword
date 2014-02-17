import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Contains all the methods needed in order to read or write a crossword
 * to a file, either a .crossword file or a .xml file.
 * 
 * @author Daniel
 *
 */
public class CrosswordIO {


	/**
	 * Creates a crossword object from a given file, and then returns it.
	 * 
	 * @param path The Path of the file being loaded.
	 * @return The created crossword object.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Crossword readPuzzle(String path) throws IOException, ClassNotFoundException{
		File inFile = new File(path);
		FileInputStream fileIn = new FileInputStream(inFile);
		ObjectInputStream objectIn = new ObjectInputStream(fileIn);
		Crossword returnable = (Crossword) objectIn.readObject();
		objectIn.close();
		return returnable;
	}

	/**
	 * Writes the passed in crossword to the given file.
	 * 
	 * @param cross The crossword to be written to file.
	 * @param path The file to be written to.
	 * @throws IOException
	 */
	public static void writePuzzle(Crossword cross, String path) throws IOException{
		File outFile = new File(path);
		FileOutputStream fileOut = new FileOutputStream(outFile);
		ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
		objectOut.writeObject(cross);
		objectOut.flush();
		objectOut.close();
	}

	/**
	 * Writes the passed in crossword to the given xml file, firstly formatting
	 * to xml.
	 * 
	 * @param cross The crossword to be written to file.
	 * @param path The file to be created and written to.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static void writeXML(Crossword cross, String path) throws ParserConfigurationException, SAXException, IOException, TransformerException{
		//creates a new document
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		//adds a root node with title and size attributes
		Element rootElement = doc.createElement("crossword");
		doc.appendChild(rootElement);
		rootElement.setAttribute("title", cross.title);
		rootElement.setAttribute("size", ""+cross.size);

		//creates an across node, child of the root node
		Element across = doc.createElement("Across");
		rootElement.appendChild(across);

		//adds all the clues across as children of the across node
		//adds all the information for each clue as text node children of the clue
		for(Clue c: cross.acrossClues){
			Element acrossClue = doc.createElement("Clue");
			acrossClue.setAttribute("Number", "" + c.number);
			across.appendChild(acrossClue);

			Element x = doc.createElement("X");
			x.appendChild(doc.createTextNode(""+c.x));
			acrossClue.appendChild(x);

			Element y = doc.createElement("Y");
			y.appendChild(doc.createTextNode(""+c.y));
			acrossClue.appendChild(y);

			Element clue = doc.createElement("Clue");
			clue.appendChild(doc.createTextNode(c.clue));
			acrossClue.appendChild(clue);

			Element answer = doc.createElement("Answer");
			answer.appendChild(doc.createTextNode(c.answer));
			acrossClue.appendChild(answer);

			Element solved = doc.createElement("Solved");
			solved.appendChild(doc.createTextNode(""+c.solved));
			acrossClue.appendChild(solved);

			//only stores the user and time if the clue has been solved
			if(c.solved()){
				Element user = doc.createElement("User");
				user.appendChild(doc.createTextNode(c.user));
				acrossClue.appendChild(user);

				Element time = doc.createElement("Time");
				time.appendChild(doc.createTextNode(c.solveTime));
				acrossClue.appendChild(time);
			}
		}

		//creates a down node, child of the root node.
		Element down = doc.createElement("Down");
		rootElement.appendChild(down);
		
		//for every down clue a node is created as a child of the down node
		//all the information for each clue is added to the node as children
		for(Clue c: cross.downClues){
			Element downClue = doc.createElement("ClueMain");
			downClue.setAttribute("Number", "" + c.number);
			down.appendChild(downClue);

			Element x = doc.createElement("X");
			x.appendChild(doc.createTextNode(""+c.x));
			downClue.appendChild(x);

			Element y = doc.createElement("Y");
			y.appendChild(doc.createTextNode(""+c.y));
			downClue.appendChild(y);

			Element clue = doc.createElement("Clue");
			clue.appendChild(doc.createTextNode(c.clue));
			downClue.appendChild(clue);

			Element answer = doc.createElement("Answer");
			answer.appendChild(doc.createTextNode(c.answer));
			downClue.appendChild(answer);

			Element solved = doc.createElement("Solved");
			solved.appendChild(doc.createTextNode(""+c.solved));
			downClue.appendChild(solved);

			//only adds the user and time if the clue has been solved
			if(c.solved()){
				Element user = doc.createElement("User");
				user.appendChild(doc.createTextNode(c.user));
				downClue.appendChild(user);

				Element time = doc.createElement("Time");
				time.appendChild(doc.createTextNode(c.solveTime));
				downClue.appendChild(time);
			}
		}

		//creates the xml file from the original document containing the tree
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(path));

		//writes the file to the given location
		transformer.transform(source, result);
	}

	/**
	 * Reads a crossword from a given xml file.
	 * 
	 * @param path The path to the xml file to be loaded.
	 * @return THe created crossword from the file
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Crossword readXML(String path) throws ParserConfigurationException, SAXException, IOException{
		ArrayList<Clue> across;
		ArrayList<Clue> down;

		//loads in the xml file into a created document tree
		File input = new File(path);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(input);
		doc.getDocumentElement().normalize();

		//will only continue loading if the xml contains a crossword, otherwise returns null
		if(doc.getDocumentElement().getNodeName().equals("crossword")){
			
			//loads all the across clues from the tree
			NodeList nodesAcross = doc.getElementsByTagName("Across");
			NodeList cluesAcross = nodesAcross.item(0).getChildNodes();
			across = readData(cluesAcross);
			
			//loads all the down clues from the tree
			NodeList nodesDown = doc.getElementsByTagName("Down");
			NodeList cluesDown = nodesDown.item(0).getChildNodes();
			down = readData(cluesDown);
			
			//gets the title and size of the crossword from the tree
			Element root = doc.getDocumentElement();
			String title = root.getAttribute("title");
			int size = Integer.parseInt(root.getAttribute("size"));
			
			//creates the crossword object from the loaded data
			return new Crossword(title, size, across, down);
		}else{
			return null;
		}
	}

	/**
	 * Reads all the clues from a given list of nodes. Creates each clue from it's
	 * given data and adds all the clues to a returned arraylist.
	 * 
	 * @param nodes The nodelist containing all the clues to be read.
	 * @return An arraylist of all the created clues.
	 */
	private static ArrayList<Clue> readData(NodeList nodes){
		ArrayList<Clue> clues = new ArrayList<Clue>();

		//loops through all the nodes in the nodelist
		for(int i = 0; i < nodes.getLength(); i++){
			Node node = nodes.item(i);
			
			//reads the various data associated with the clue into temporary variables
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element e = (Element) node;
				String number = e.getAttribute("Number");
				int num = Integer.parseInt(number);
				NodeList xList = e.getElementsByTagName("X").item(0).getChildNodes();
				Node xValue = (Node) xList.item(0);
				int x = Integer.parseInt(xValue.getNodeValue());

				NodeList yList = e.getElementsByTagName("Y").item(0).getChildNodes();
				Node yValue = (Node) yList.item(0);
				int y = Integer.parseInt(yValue.getNodeValue());

				NodeList clueList = e.getElementsByTagName("Clue").item(0).getChildNodes();
				Node clueValue = (Node) clueList.item(0);
				String clue = clueValue.getNodeValue();

				NodeList answerList = e.getElementsByTagName("Answer").item(0).getChildNodes();
				Node answerValue = (Node) answerList.item(0);
				String answer = answerValue.getNodeValue();

				NodeList solvedList = e.getElementsByTagName("Solved").item(0).getChildNodes();
				Node solvedValue = (Node) solvedList.item(0);
				boolean solved = Boolean.parseBoolean(solvedValue.getNodeValue());

				//creates a clue from the loaded temporary variables and adds it to the arraylist
				clues.add(new Clue(num, x, y, clue, answer, solved));
				
				//only if the clue is solved is the user and time read from the nodelist
				if(solved){
					NodeList userList = e.getElementsByTagName("User").item(0).getChildNodes();
					Node userValue = (Node) userList.item(0);
					String user = userValue.getNodeValue();

					NodeList timeList = e.getElementsByTagName("Time").item(0).getChildNodes();
					Node timeValue = (Node) timeList.item(0);
					String time = timeValue.getNodeValue();
					clues.get(i).setSolved(solved, user, time);
				}
			}
		}
		return clues;
	}
}
