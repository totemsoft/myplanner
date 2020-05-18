/*
 * ColumnData.java
 *
 * Created on 29 May 2002, 21:50
 */

package com.argus.financials.projection.data;

/**
 * Helper class used to define the column properties used for a JTable and its
 * associated TableModel.
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 * @version
 */
public class ColumnData {

    public String m_title;

    public int m_width;

    public int m_alignment;

    /** Creates new ColumnData */
    public ColumnData(String title, int width, int alignment) {
        m_title = title;
        m_width = width;
        m_alignment = alignment;
    }

}
