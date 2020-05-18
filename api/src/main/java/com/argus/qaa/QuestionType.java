/*
 * QuestionType.java
 *
 * Created on 19 November 2001, 12:32
 */

package com.argus.qaa;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 * 
 * (1, 'RadioBox (Single Answer)') (2, 'CheckBox (Multiple Answer)') (3,
 * 'ComboBox (Single Answer)') (4, 'ListBox (Multiple Answer)') (5, 'FreeText')
 * 
 */

public interface QuestionType {

    public static final Integer RADIO_BOX = new Integer(1);

    public static final Integer CHECK_BOX = new Integer(2);

    public static final Integer COMBO_BOX = new Integer(3);

    public static final Integer LIST_BOX = new Integer(4);

    public static final Integer FREE_TEXT = new Integer(5);

}
