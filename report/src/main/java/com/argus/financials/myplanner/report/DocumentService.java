package com.argus.financials.myplanner.report;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.stereotype.Service;

/*
 * @author Valeri SHIBAEV (mailto:shibaevv@apollosoft.net)
 */
@Service
public interface DocumentService
{

    /**
     * FILLING OUT INTERACTIVE FORMS
     * http://itextpdf.com/book/chapter.php?id=8
     * http://examples.itextpdf.com/src/part2/chapter08/ReaderEnabledForm.java
     * @param template
     * @param dataMap
     * @throws Exception
     */
    void readDocument(InputStream template, Map<String, Object> dataMap)
        throws Exception;

    /**
     * @param template
     * @param dataMap - template parameters
     * @param output
     * @throws Exception
     */
    void writeDocument(InputStream template, Map<String, Object> dataMap, OutputStream output)
        throws Exception;

}