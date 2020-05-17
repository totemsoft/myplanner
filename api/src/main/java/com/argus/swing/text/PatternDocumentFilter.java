package com.argus.swing.text;

// JDK
import java.util.regex.Pattern;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class PatternDocumentFilter extends DocumentFilter {

  private Pattern pattern;

  public PatternDocumentFilter(Pattern pattern) {
    this.pattern = pattern;
  }

  public void insertString(DocumentFilter.FilterBypass fb, int offset, String str, AttributeSet attrs)
      throws BadLocationException
  {
    Document doc = fb.getDocument();
    String s = doc.getText(0, doc.getLength());
    s = s.substring(0, offset) + str + s.substring(offset);
    //System.out.println(s);
    if (isValid(s))
      super.insertString(fb, offset, str, attrs);
  }

  public void remove(DocumentFilter.FilterBypass fb, int offset, int length)
      throws BadLocationException
  {
    Document doc = fb.getDocument();
    String s = doc.getText(0, doc.getLength());
    s = s.substring(0, offset) + s.substring(offset + length);
    //System.out.println(s);
    if (isValid(s))
      super.remove(fb, offset, length);
  }

  public void replace(DocumentFilter.FilterBypass fb, int offset, int length, String str, AttributeSet attrs)
      throws BadLocationException
  {
    Document doc = fb.getDocument();
    String s = doc.getText(0, doc.getLength());
    s = s.substring(0, offset) + str + s.substring(offset + length);
    //System.out.println(s);
    if (isValid(s))
      super.replace(fb, offset, length, str, attrs);
  }

  private boolean isValid(String str)
  {
    String s = str.trim();
    return pattern.matcher(s).matches();
  }

}
