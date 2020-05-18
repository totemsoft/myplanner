package com.argus.financials.exchange;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.xml.sax.InputSource;

//import org.apache.fop.apps.Driver;
//import org.apache.fop.apps.Version;

public final class XMLTransformService {

    private XMLTransformService() {
    }

    public static String transform(InputStream theXML, Object theXSL,
            String[] parameter, String[] value)
    // throws XMLTransformException
    {
        StringWriter output = new StringWriter();
        try {
            Templates template = (Templates) theXSL;
            // produce the transformation

            Transformer transformer = template.newTransformer();
            for (int i = 0; i < parameter.length; i++) {
                if (parameter[i] != null && parameter[i] != ""
                        && value[i] != null && value[i] != "")
                    transformer.setParameter(parameter[i], value[i]);
            }
            transformer.transform(new StreamSource(theXML), new StreamResult(
                    output));
        }

        catch (javax.xml.transform.TransformerException ex) {
            ex.printStackTrace(System.out);
            error(ex);
            return null;
        }

        return output.toString();
    }

    private static void error(Exception e) {
        System.err.println(e.getMessage());
    }

    public static String transform(InputStream theXML, Object theXSL)
    // throws XMLTransformException
    {
        StringWriter output = new StringWriter();
        try {
            Templates template = (Templates) theXSL;
            // produce the transformation
            Transformer transformer = template.newTransformer();
            StreamSource ss = new StreamSource(new InputStreamReader(theXML,
                    "utf-8"));
            transformer.transform(ss, new StreamResult(output));
        } catch (javax.xml.transform.TransformerException ex) {
            error(ex);
            return null;
        }

        catch (UnsupportedEncodingException ex) {
            error(ex);
            return null;
        }

        return output.toString();
    }

    public static String transform(Reader theXML, Object theXSL)
    // throws XMLTransformException
    {
        StringWriter output = new StringWriter();
        try {
            Templates template = (Templates) theXSL;
            // produce the transformation
            Transformer transformer = template.newTransformer();
            StreamSource ss = new StreamSource(theXML);
            transformer.transform(ss, new StreamResult(output));
        } catch (javax.xml.transform.TransformerException ex) {
            ex.printStackTrace(System.out);
            error(ex);
            return null;
        }

        return output.toString();
    }

    /**
     * Transforms (XSLT processing) the xml using the xsl and returns the result
     * of the transform.
     * 
     * @param theXML
     *            the xml string
     * @param theXSLFileName
     *            the xsl string
     * @return the string resulting from the transformation
     */
    public static String transform(String xml, String xsl)
    // throws XMLTransformException
    {
        StringWriter output = new StringWriter();
        try {
            // produce the xsl:fo
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(
                    xsl));
            transformer.transform(new StreamSource(new StringReader(xml)),
                    new StreamResult(output));
        } catch (Exception ex) {
            error(ex);
            return null;
        }

        return output.toString();

    }

    public static String transform(String xml, java.io.InputStream xsl)
    // throws XMLTransformException
    {
        StringWriter output = new StringWriter();
        try {
            // produce the xsl:fo
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(
                    xsl));
            transformer.transform(new StreamSource(new StringReader(xml)),
                    new StreamResult(output));
        } catch (Exception ex) {
            error(ex);
            return null;
        }

        return output.toString();

    }

    /**
     * Transforms (XSLT processing) the xml using the xsl and returns the result
     * of the transform.
     * 
     * @param theXML
     *            the xml string
     * @param theXSLFileName
     *            the xsl string
     * @param writeToDebugFiles
     *            a boolean indicating whether the XML & XSL:FO should be
     *            written to files for debugging purposes
     * @param xmlFileName
     *            the file name to write the xml if required by writeToFiles
     * @param xslFoFileName
     *            the file name to write the xsl:fo if required by writeToFiles
     * @return the string resulting from the transformation
     */
    public static String transform(String xml, String xsl,
            boolean writeToDebugFiles, String xmlFileName, String xslFoFileName)
    // throws XMLTransformException
    {

        StringWriter output = new StringWriter();
        try {
            // produce the xsl:fo
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(
                    xsl));
            transformer.transform(new StreamSource(new StringReader(xml)),
                    new StreamResult(output));
        } catch (Exception ex) {
            error(ex);
            return null;
        }

        return output.toString();
    }

    /**
     * Render the xslFO to PDF and store the output to the OutputStream.
     * 
     * @param xslFO
     *            the fo string input
     * @param out
     *            the file the render output should be written to
     */
    public static void renderToPDF(String xslFO, OutputStream out)
    // throws XMLTransformException
    {
        renderToPDF(xslFO, out, false, null);
    }

    /**
     * Render the xslFO to PDF and store the output to the OutputStream.
     * 
     * @param xslFO
     *            the fo string input
     * @param out
     *            the file the render output should be written to
     */
    public static void renderToPDF(String xslFO, OutputStream out,
            boolean writeToDebugFile, String pdfDebugFileName)
    // throws XMLTransformException
    {

        try {
            InputSource foFile = new InputSource(new StringReader(xslFO));
            ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
            // Driver driver = new Driver(foFile, outBytes);
            // driver.run();

            byte[] content = outBytes.toByteArray();
            out.write(content);
            out.flush();
            out.close();

        } catch (Exception ex) {
            error(ex);

        }

    }

    /**
     * Transform the xml into pdf via xsl transformation to xslfo and then fo
     * rendering to pdf.
     * 
     * @param theXML
     *            the input xml String
     * @param theXSLFileName
     *            the file name for the xsl transformation stylesheet String
     * @param out
     *            the OutputStream that the final pdf will be written
     * @param writeToDebugFiles
     *            indicates whether xml, xsl:fo & pdf should be written to files
     *            for debug purposes
     * @param filePathPrefix
     *            the prefix of the file names for creating debug files, just
     *            add suffix e.g. .xml
     */
    public static void xmlToPDF(String theXML, String theXSLFileName,
            OutputStream out, boolean writeToDebugFiles, String filePathPrefix)
    // throws XMLTransformException
    {
        String xmlDebugFileName = null;
        String xslfoDebugFileName = null;
        String pdfDebugFileName = null;
        if (writeToDebugFiles && (filePathPrefix != null)) {
            xmlDebugFileName = filePathPrefix + ".xml";
            xslfoDebugFileName = filePathPrefix + ".fo";
            pdfDebugFileName = filePathPrefix + ".pdf";
        }

        // produce the xsl:fo
        String transformed = transform(theXML, theXSLFileName,
                writeToDebugFiles, xmlDebugFileName, xslfoDebugFileName);

        // render to pdf
        renderToPDF(transformed, out, writeToDebugFiles, pdfDebugFileName);
    }

    public static Object getTemplate(String theXSLFileName)
    // throws XMLTransformException

    {

        try {
            TransformerFactory tfactory = javax.xml.transform.sax.SAXTransformerFactory
                    .newInstance();
            // TransformerFactory.newInstance();
            Templates templates = tfactory.newTemplates(new StreamSource(
                    theXSLFileName));
            return templates;
        } catch (javax.xml.transform.TransformerException ex) {

            System.out.println("Exception :" + ex);
            ex.printStackTrace(System.out);
            error(ex);
            return null;
        }

    }

    // public
    static void main(String[] args) {

        String theXMLFileName = "d:/temp/bean.xml";
        String theXSLFileName = "d:/projects/jam2/jsp/xml/xsl/fo/consolidatedClientReport.xsl";
        String pdfFileName = "d:/a.pdf";

        Runtime.getRuntime().gc();

        for (int i = 0; i < 20; i++) {

            try {
                FileOutputStream outPdfFile = new FileOutputStream(pdfFileName);
                xmlToPDF(theXMLFileName, theXSLFileName, outPdfFile, false,
                        null);
            } catch (Exception ex) {
                System.out.println("XMLTransformService.main");
                ex.printStackTrace();
            } finally {
            }
        }
    }

}
