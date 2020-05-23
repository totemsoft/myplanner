package au.com.totemsoft.myplanner.report.pdf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.pdfbox.cos.COSStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.common.COSObjectable;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDChoice;
import org.apache.pdfbox.pdmodel.interactive.form.PDField;
import org.apache.pdfbox.pdmodel.interactive.form.PDRadioButton;
import org.apache.pdfbox.pdmodel.interactive.form.PDSignatureField;
import org.apache.pdfbox.pdmodel.interactive.form.PDXFAResource;
//import org.apache.pdfbox.pdmodel.interactive.form.PDXFA;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * 
 * @author Valeri CHIBAEV (mailto:apollosoft.net.au@gmail.com)
 */
public class PdfBox extends BasePdf implements Pdf {

    /** logger. */
    private static final Logger LOG = Logger.getLogger(PdfBox.class);

    /*
     * (non-Javadoc)
     */
    @SuppressWarnings("unchecked")
    public void read(InputStream template, Map<String, Object> dataMap) throws IOException {
        PDDocument pdf = PDDocument.load(template);
        PDDocumentCatalog catalog = pdf.getDocumentCatalog();
        PDAcroForm form = catalog.getAcroForm();
        Iterator<PDField> iter = form.getFields().iterator();
        while (iter.hasNext()) {
            PDField field = iter.next();
            pdfTemplateField(dataMap, field, field.getPartialName());
        }
        pdf.close();
    }

    /*
     * (non-Javadoc)
     */
    @Override
    public void write(byte[] template, Map<String, Object> dataMap, OutputStream output) throws IOException {
        write(new ByteArrayInputStream(template), dataMap, null, output);
    }

    /**
     * 
     * @param template
     * @param dataMap - template parameters
     * @param dataStream - optional xml feed (can be null)
     * @param output
     * @throws IOException
     */
    private void write(InputStream template, Map<String, Object> dataMap, InputStream dataStream, OutputStream output) throws IOException {
        PDDocument pdfTemplate = null;
        try {
            //
            pdfTemplate = PDDocument.load(template);
            pdfTemplate.setAllSecurityToBeRemoved(true);
            PDDocumentCatalog catalog = pdfTemplate.getDocumentCatalog();
            PDAcroForm form = catalog.getAcroForm();
            //
            if (dataMap != null) {
                Object contextBean = dataMap.get(CONTEXT_BEAN);
                JXPathContext context = null;
                if (contextBean != null) {
                    context = JXPathContext.newContext(contextBean);
                    context.setLenient(true);
                }
                for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                    String name = entry.getKey();
                    PDField field = form.getField(name);
                    if (field == null) {
                        continue; // eg CONTEXT
                    }
                    if (field instanceof PDChoice) {
                        // continue;
                    }
                    if (field instanceof PDRadioButton) {
                        continue;
                    }
                    if (field instanceof PDSignatureField) {
                        continue;
                    }
                    //
                    Object value = entry.getValue();
                    if (value instanceof String && StringUtils.isNotBlank(value.toString())) {
                        if (context != null) {
                            value = context.getValue(value.toString());
                        }
                        if (value != null) {
                            field.setValue(value.toString());
                        }
                    }
                }
            }
            // xml input
            if (dataStream != null) {
                PDXFAResource xfa = form.getXFA();
                if (xfa == null) {
                    throw new IOException("The XFA resource does not exist.");
                }
                // dataStream = IOUtils.toInputStream("my_data.xml");
                // if (!hasXFAheaders(new FileInputStream("my_data.xml")))
                // {
                // dataStream = addXFAheaders(dataStream);
                // }
                //
                Document xfaDoc = xfa.getDocument();
                Node dataNode = xfaDoc.getElementsByTagName(XFA_DATA_NODE).item(0);
                //
                final DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document dataDoc = builder.parse(dataStream);
                Node dataRoot = dataDoc.getDocumentElement();
                Node xfaDataNode = xfaDoc.createElement(XFA_DATA_NODE);
                xfaDataNode.appendChild(xfaDoc.importNode(dataRoot, true));
                dataNode.getParentNode().replaceChild(xfaDataNode, dataNode);
                //
                Source source = new DOMSource(xfaDoc);
                //
                //COSStream cosout = new COSStream(new RandomAccessBuffer());
                COSStream cosout = new COSStream();
                OutputStream out = cosout.createRawOutputStream();
                StreamResult result = new StreamResult(out);
                TransformerFactory.newInstance().newTransformer().transform(source, result);
                dataStream.close();
                //
                PDXFAResource xfaout = new PDXFAResource(cosout);
                form.setXFA(xfaout);
            }
            //
            pdfTemplate.save(output);
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        } finally {
            if (pdfTemplate != null) {
                pdfTemplate.close();
            }
        }
    }

    private void pdfTemplateField(Map<String, Object> params, PDField field, String parentName) throws IOException {
        //List<COSObjectable> kids = field.getKids();
        List<PDAnnotationWidget> kids = field.getWidgets();
        if (kids != null) {
            Iterator<PDAnnotationWidget> iter = kids.iterator();
            if (!parentName.equals(field.getPartialName())) {
                parentName = parentName + "." + field.getPartialName();
            }
            if (LOG.isDebugEnabled())
                LOG.debug(parentName);
            while (iter.hasNext()) {
                COSObjectable pdfObj = iter.next();
                if (pdfObj instanceof PDField) {
                    PDField kid = (PDField) pdfObj;
                    pdfTemplateField(params, kid, parentName);
                }
            }
        } else {
            // PDSignature doesn't have a value
            String name = parentName + "." + field.getPartialName();
            String value;
            if (field instanceof PDSignatureField) {
                value = "PDSignatureField";
            } else if (field instanceof PDRadioButton) {
                value = ((PDRadioButton) field).getPartialName();
            } else {
                value = field.getValueAsString();
            }
            if (LOG.isDebugEnabled())
                LOG.debug(name + " = " + value);
            LOG.info(name + "=" + field.getFullyQualifiedName());
            params.put(name, value);
        }
    }

}