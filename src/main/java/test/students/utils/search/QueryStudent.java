package test.students.utils.search;


import test.students.domain.StudentQueryParam;
import test.students.domain.StudentResponse;
import test.students.utils.search.comp.*;

public interface QueryStudent {

    QueryResultPage<StudentResponse> query(StudentQueryParam queryParam);
}
