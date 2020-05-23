package au.com.totemsoft.myplanner.report.api;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public interface ReportService {

    /**
     * 
     * @param template - PDF document
     * @param dataMap or dataStream
     * @param output
     * @throws Exception
     */
    void createPdf(InputStream template, Map<String, Object> dataMap, OutputStream output) throws Exception;

}