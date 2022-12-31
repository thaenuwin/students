package test.students.utils;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;

    public class ExcelExportProcessor {

        private ExcelExportProcessor() {
        }

        private static final String ATTACHMENT = "attachment; filename=";
        private static final String CONTENT_TYPE = "application/octet-stream";
        private static final String CONTENT_DISPOSITION = "Content-disposition";
        private static final String EXPORTED_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";
        private static final String XLSX = ".xlsx";
        private static final String CSV = ".csv";

        public static void processExportResponse(HttpServletResponse res, String fileNamePrefix) {
            processReport(res, fileNamePrefix, XLSX);
        }

        public static void processCSVResponse(HttpServletResponse res, String fileNamePrefix) {
            processReport(res, fileNamePrefix, CSV);
        }

        private static void processReport(HttpServletResponse res, String fileNamePrefix, String extension) {
            res.setContentType(CONTENT_TYPE);
            SimpleDateFormat formatter = new SimpleDateFormat(EXPORTED_DATE_FORMAT);
            Date now = DateUtil.now();
            String reportedGeneratedDate = formatter.format(now);
            res.setHeader(CONTENT_DISPOSITION, ATTACHMENT + fileNamePrefix + reportedGeneratedDate + extension);
        }
}
