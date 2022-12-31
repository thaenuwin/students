package test.students.persistence;


import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import test.students.domain.StudentQueryParam;
import test.students.domain.StudentResponse;
import test.students.persistence.entity.QStudents;
import test.students.persistence.entity.Students;
import test.students.utils.JsonUtil;
import test.students.utils.search.QueryResultPageHelper;
import test.students.utils.search.QueryStudent;
import test.students.utils.search.ResultItemConverter;
import test.students.utils.search.comp.QueryParam;
import test.students.utils.search.comp.QueryResultPage;

import javax.persistence.EntityManager;

@Log4j2
@Component
@AllArgsConstructor
public class QueryStudentAdapter implements QueryStudent {

    private final EntityManager em;

    @Override
    public QueryResultPage<StudentResponse> query(StudentQueryParam queryParam) {
        log.info("queryParam:{}", JsonUtil.toJsonString(queryParam));


        return QueryResultPageHelper.query(queryParam, QStudents.students, em, new ResultItemConverter<Students, StudentResponse>() {

            @Override
            public StudentResponse from(Students stuEnt) {
                log.info("Item:{}",stuEnt);
                return StudentResponse.fromEntityToQueryItem(stuEnt);
            }
        });
    }
}
