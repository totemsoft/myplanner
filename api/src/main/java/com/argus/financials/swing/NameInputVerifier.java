/*
 * NameInputVerifier.java
 *
 * Created on 10 July 2002, 14:21
 */

package com.argus.financials.swing;

import java.util.StringTokenizer;

import javax.swing.JTextField;

/**
 * The NameInputVerifier transforms every word's first letter into a capital
 * letter. The " " and the "-" characters are used to separate words in a given
 * String.
 * <p>
 * </p>
 * Example 1: "john doe" will be transformed into "John Doe" Example 2:
 * "john-bill doe" will be transformed into "John-Bill Doe"
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public class NameInputVerifier extends javax.swing.InputVerifier {

    private static NameInputVerifier inputVerifier = new NameInputVerifier();

    public static NameInputVerifier getInstance() {
        return inputVerifier;
    }

    /** Creates new NameInputVerifier */
    public NameInputVerifier() {
    }

    public boolean verify(javax.swing.JComponent jComponent) {
        boolean validate = true;

        if (jComponent instanceof JTextField) {
            jComponent.setToolTipText("for example: Mr. John Doe");
            String text = ((JTextField) jComponent).getText();

            // change the first letter of each word in the string to a capital
            // letter
            text = toUpperCaseString(text, " ");
            text = toUpperCaseString(text, "-");

            // update JTextField
            ((JTextField) jComponent).setText(text);
        }
        return validate;
    }

    /**
     * Transform the first letter of a String into a capital letter and returns
     * a new String object.
     * <p>
     * </p>
     * Example: "john" will be tranformed into "John"
     * 
     * @param str -
     *            a string to transform
     * @return upper_str - a new string, starting with a capital letter
     */
    private String toUpperCaseFirstLetterOnly(String str) {
        // transform first letter into upper case
        String upper_str; // the new string, starting with a capital letter
        char[] c = { ' ' }; // first letter of the given str, has to be an
                            // array, so we easly can create a String object out
                            // of it

        // get first character
        c[0] = str.charAt(0);

        // creates a new String which contains only the first letter from the
        // given String
        String upper_c = new String(c);

        // check string length
        if (str.length() >= 1) {
            upper_str = upper_c.toUpperCase() + str.substring(1, str.length());
        } else {
            upper_str = upper_c.toUpperCase();
        }

        return upper_str;
    }

    /**
     * Transform the first letter of each token into upper case and return a new
     * string.
     * <p>
     * </p>
     * Example: str = "john doe" delimiter = " " <br>
     * </br> "john doe" will be transformed into "John Doe"
     * 
     * @param str -
     *            a string
     * @return a new string, each token will start with a capital letter
     */
    private String toUpperCaseString(String str, String delimiter) {
        // split text into token
        StringTokenizer st = new StringTokenizer(str, delimiter);
        String[] token = new String[st.countTokens()];

        int index = 0;
        // process every token, transform first letter into upper case
        while (st.hasMoreTokens()) {
            token[index] = st.nextToken();
            // transform first letter into uppercase
            token[index] = toUpperCaseFirstLetterOnly(token[index]);
            index++;
        }
        // create new text for JTextField with with capital letters for each
        // token
        StringBuffer str_buffer = new StringBuffer();
        for (int i = 0; i < index; i++) {
            // processing last token ?
            if (i + 1 != index) {
                // no, than add token plus ' '
                str_buffer.append(token[i] + delimiter);
            } else {
                // yes, add token only
                str_buffer.append(token[i]);
            }
        }

        return str_buffer.toString();
    }
}
