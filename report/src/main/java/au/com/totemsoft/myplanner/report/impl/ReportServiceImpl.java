package au.com.totemsoft.myplanner.report.impl;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.inject.Inject;

import au.com.totemsoft.myplanner.report.api.DocumentService;
import au.com.totemsoft.myplanner.report.api.ReportService;

public class ReportServiceImpl implements ReportService {

    @Inject private DocumentService documentService;

    /* (non-Javadoc)
     * @see au.com.totemsoft.myplanner.report.ReportService#createPdf(java.io.InputStream, java.util.Map, java.io.OutputStream)
     */
    @Override
    public void createPdf(InputStream template, Map<String, Object> dataMap, OutputStream output) throws Exception {
//        InputStream dataStream = null;
//        File file = File.createTempFile(template.getCode(), ".pdf");
//        OutputStream output = new FileOutputStream(file);
//        Map<String, Object> params = new LinkedHashMap<String, Object>();
//        params.put(Pdf.CONTEXT_BEAN, contextBean);
//        if (pdfRequreXml) {
//            MyForm form = ConvertUtils.convert(bean, null);
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            Result result = new StreamResult(baos);
//            marshaller.marshal(form, result);
//            dataStream = new ByteArrayInputStream(baos.toByteArray());
//        }
        documentService.writeDocument(template, dataMap, output);
    }

}