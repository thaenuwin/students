package test.students.domain;

import lombok.Data;
import test.students.utils.search.comp.QueryParam;


@Data
public class StudentQueryParam extends QueryParam {
    private String studentId;

    private String studentName;

    private String studentMajor;

    private String studentGrade;
}
