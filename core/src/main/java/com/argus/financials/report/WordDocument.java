/*
 * WordDocumentWriter.java
 *
 * Created on 13 March 2002, 16:17
 */

package com.argus.financials.report;

import com.argus.activex.wordreport.IWordReport;
import com.argus.activex.wordreport.WordReportJava2COM;


/**
 * 
 * @author valeri chibaev
 * @version
 */

public class WordDocument extends BaseDocument {

    /**
     * 
     */
    //private WordAppBase word;
    private IWordReport word;

    private String template;

    public WordDocument(Object _data, String template) {
        super(_data);

        this.template = template;
    }

    public void initialize() throws ReportException {
        uninitialize();

        word = new WordReportJava2COM();
        word.setTemplate(template);
        
    }

    public void uninitialize() throws ReportException {
        if (word == null)
            return;

        word = null;
    }

    public void updateDocument() throws ReportException {
        if (word != null)
            word.setData(getFields());
    }

}
