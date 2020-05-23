package au.com.totemsoft.swing.text;

// JDK
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class MaskDocumentFilter extends DocumentFilter {

  private String mask;
  private final int maxLength;

  public MaskDocumentFilter(String mask) {
    this.mask = mask;
    this.maxLength = mask.length();
  }

  public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attrs)
      throws BadLocationException
  {
      replace(fb, offset, 0, str, attrs);
  }

  public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
      throws BadLocationException
  {
    String text = "";
    for (int i = 0; maxLength > length && i < length; i++)
      text += ' ';

    Document doc = fb.getDocument();
    if (doc.getLength() == offset + length)
      // we are at the end of date string
      super.remove(fb, offset, length);
    else
      // we are inside date string
      super.replace(fb, offset, length, text, null);

    //getJTextField().setCaretPosition(offset);
  }

  public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs)
      throws BadLocationException
  {
    if (length == 0)
      length = str.length();
    if (maxLength < offset + length)
      length = maxLength - offset;
    if (length <= 0 || !isValid(offset, str))
      return;

    str = str.toUpperCase();

    Document doc = fb.getDocument();
    if (maxLength >= offset + length && doc.getLength() < maxLength)
      // append to incomplete date string
      super.insertString(fb, offset, str, attrs);
    else
      super.replace(fb, offset, length, str, attrs);
  }

  /**
   *
   * @param offset int
   * @param str String
   * @return boolean
   *
   Character  Description (see javax.swing.text.MaskFormatter)
   # Any valid number, uses Character.isDigit.
   ' Escape character, used to escape any of the special formatting characters.
   U Any character (Character.isLetter). All lowercase letters are mapped to upper case.
   L Any character (Character.isLetter). All upper case letters are mapped to lower case.
   A Any character or number (Character.isLetter or Character.isDigit)
   ? Any character (Character.isLetter).
   * Anything.
   H Any hex character (0-9, a-f or A-F).
   */
  private boolean isValid(int offset, String str)
  {
    if (maxLength < offset + str.length())
      return false;

    for (int i = offset; i < offset + str.length(); i++)
    {
      char m = mask.charAt(i);
      char c = str.charAt(i - offset);
      switch (m) {
        case ('#'): // Any valid number
          if ( !Character.isDigit(c) ) return false;
          break;
        case ('\''): // Escape character
          break;
        case ('U'): // Any character
          if ( !Character.isLetter(c) ) return false;//Character.isUpperCase(c);
          break;
        case ('L'): // Any character
          if ( !Character.isLetter(c) ) return false;//Character.isLowerCase(c);
          break;
        case ('A'): // Any character or number
          if ( !Character.isLetterOrDigit(c) ) return false;
          break;
        case ('?'): // Any character
          if ( !Character.isLetter(c) ) return false;
          break;
        case ('H'): // Any hex character
          if ( !( Character.isDigit(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F') ) ) return false;
          break;
        case ('*'): // Anything
          break;
        default:
          if (m != c) return false;
      }
    }

    return true;
  }

}
