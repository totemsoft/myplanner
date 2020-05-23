package au.com.totemsoft.myplanner.report.pdf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
@Component
public interface Pdf
{

    /** context bean */
    String CONTEXT_BEAN = "bean";

    /**
     * FILLING OUT INTERACTIVE FORMS
     * http://itextpdf.com/book/chapter.php?id=8
     * http://examples.itextpdf.com/src/part2/chapter08/ReaderEnabledForm.java
     * @param template
     * @param dataMap
     * @throws IOException
     */
    void read(InputStream template, Map<String, Object> dataMap) throws IOException;

    /**
     * @param template
     * @param dataMap - template parameters
     * @param output
     * @throws IOException
     */
    void write(byte[] template, Map<String, Object> dataMap, OutputStream output) throws IOException;

}