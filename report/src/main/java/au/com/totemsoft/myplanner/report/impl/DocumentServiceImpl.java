package au.com.totemsoft.myplanner.report.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import au.com.totemsoft.myplanner.report.api.DocumentService;
import au.com.totemsoft.myplanner.report.pdf.Pdf;
import au.com.totemsoft.myplanner.report.pdf.PdfIText;

public class DocumentServiceImpl implements DocumentService {

    private final Pdf pdf;

    public DocumentServiceImpl() {
        pdf = new PdfIText(); // lowagie free (itextpdf $2,000 license for commercial distribution)
        //pdf = new PdfBox(); // apache.pdfbox free
        //pdf = new PdfJt();  // com.adobe.pdfjt $5,000 license
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.report.DocumentService#readDocument(java.io.InputStream, java.util.Map)
     */
    @Override
    public void readDocument(InputStream template, Map<String, Object> dataMap) throws Exception {
        pdf.read(template, dataMap);
    }

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.report.DocumentService#writeDocument(java.io.InputStream, java.util.Map, java.io.OutputStream)
     */
    @Override
    public void writeDocument(InputStream template, Map<String, Object> dataMap, OutputStream output) throws Exception {
        pdf.write(IOUtils.toByteArray(template), dataMap, output);
    }

}