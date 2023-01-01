package test.students.utils;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.students.utils.email.EmailDetails;
import test.students.utils.email.EmailService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


@Component
@Log4j2
public class ExcelReportEmailSender {

    @Autowired
    EmailService emailService;
    public void sendSingleSheetExcelReport(String subj, String body,
            String sheetName,
            List<List<String>> dataContent,
            String emails)
            throws IOException {

        byte[] bytes;
        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            ExcelWriter.writeSingleSheet(sheetName, dataContent, os);
            bytes = os.toByteArray();
        }
        EmailDetails emailDetails = EmailDetails.builder()
                .sheetName(sheetName)
                .attachment(bytes)
                .msgBody(body)
                .subject(subj )
                .recipient(emails)
                .build();
        emailService.sendMailWithAttachment(emailDetails);

    }

}
