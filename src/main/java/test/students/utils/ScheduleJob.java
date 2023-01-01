package test.students.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import test.students.persistence.StudentRepo;
import test.students.persistence.entity.StudentPk;
import test.students.persistence.entity.Students;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class ScheduleJob {

    @Autowired
    private ExcelReportEmailSender excelReportEmailSender;
    @Autowired
    private StudentRepo studentRepo;
    @Value("${mail.message.body}")
    private String body;

    @Value("${mail.message.subject}")
    private String subj;

    @Value("${mail.receiver.user}")
    private String receiver;

    @Scheduled(cron = "0 0 */1 * * *")
    public void runEveyHour() throws IOException {
        excelReportEmailSender.sendSingleSheetExcelReport( subj,body,"students_"+DateUtil.format(new Date()).replace(":","_")+".xlsx", prepareDataContent(), receiver);
    }

    private List<List<String>> prepareDataContent()  {

        List<List<String>> dataContent = new ArrayList<>();
        try {
            List<String> headers = new ArrayList<>();
            headers.add("Student Grade");
            headers.add("Student Name");
            headers.add("Student Major");
            headers.add("Student Phone Number");
            headers.add("Student Enable");
            headers.add("Created Data Date Time");
            headers.add("Updated Data Date Time");
            dataContent.add(headers);
            List<Students> list = studentRepo.findAll();
            for(int j =0;j<list.size();j++) {
                Field[] fields = Students.class.getDeclaredFields();
                List<String> row = new ArrayList<>();
                for (int i = 0; i < fields.length; i++) {
                    Object value = fields[i].get(list.get(j));
                    if(value instanceof StudentPk){
                        row.add(((StudentPk) value).getStudentGrade());
                        row.add(((StudentPk) value).getStudentName());
                        row.add(((StudentPk) value).getStudentMajor());
                    }else {
                        row.add((value == null) ? "" : processValue(value).toString());
                    }
                }
                dataContent.add(row);
            }

        }catch (Exception ex){
            ex.printStackTrace();

        }
        return dataContent;
    }


    public static Object processValue(Object value) {
        Object result = "";
        if (value != null) {
            if (value instanceof Date) {
                result = DateUtil.format((Date) value);
            }else if (value instanceof Integer) {
                result =  Integer.valueOf(1).equals(value) ? "Y" : "N";
            }else {
                result = value.toString();
            }
        }
        return result;
    }

}
