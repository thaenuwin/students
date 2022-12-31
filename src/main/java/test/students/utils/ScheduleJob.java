package test.students.utils;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import test.students.domain.StudentQueryParam;
import test.students.persistence.StudentRepo;
import test.students.persistence.entity.Students;
import test.students.utils.search.QueryResultPageHelper;
import test.students.utils.search.comp.PaginationParam;
import test.students.utils.search.comp.QueryParam;
import test.students.utils.search.comp.QueryResultPage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ScheduleJob {
    private final ExcelReportEmailSender excelReportEmailSender;
    private final StudentRepo studentRepo;
    @Scheduled(cron = "*/10 * * * * *")
    public void runEvey5Minutes() throws IOException {
        System.out.println("Current time is :: "+ LocalDate.now());
        excelReportEmailSender.sendSingleSheetExcelReport( "sheetname", prepareDataContent(), "thaenu@biziolab.com");
    }

    private List<List<String>> prepareDataContent(){
        List<List<String>> dataContent = new ArrayList<>();
        List<String> headers = new ArrayList<>();
        headers.add("Student Grade");
        headers.add("Student Name");
        dataContent.add(headers);
        List<Students> list=studentRepo.findAll();
        List<String> strings = list.stream()
                .map(object -> Objects.toString(object, null))
                .collect(Collectors.toList());
            dataContent.add(strings);

            System.out.println(dataContent);
        return dataContent;
    }

}
