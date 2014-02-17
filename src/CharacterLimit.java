import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;



/**
 * Limits the number of characters which can be entered to the given limit. If more characters
 * are entered then the previous ones are overwritten.
 * 
 * @author Daniel
 *
 */
@SuppressWarnings("serial")
public class CharacterLimit extends PlainDocument {
	/**
	 * The character limit.
	 */
	private int limit;

	CharacterLimit(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString( int offset, String  str, AttributeSet attr ){
		if (str == null) {
			return;
		}

		//only shows the string less than the limit
		if ((getLength() + str.length()) <= limit) {
			try {
				super.insertString(offset, str, attr);
			} catch (BadLocationException e) {}
		//if more characters are entered the previous ones are replaced
		}else{
			try {
				super.replace(0, limit, str, attr);
			} catch (BadLocationException e) {}
		}
	}
}