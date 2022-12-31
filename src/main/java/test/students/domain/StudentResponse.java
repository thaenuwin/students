package test.students.domain;


import lombok.Builder;
import lombok.Data;
import test.students.persistence.entity.StudentPk;
import test.students.persistence.entity.Students;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@Builder(toBuilder = true)
public class StudentResponse {


    private String studentId;


    private String studentName;

    private String studentMajor;


    private String studentGrade;


    private String stuPhoneNumber;


    private Date createdDate;

    private Date updatedDate;
    public static StudentResponse fromEntityToQueryItem(Students stuEntity) {
        return StudentResponse.builder()
                .studentId(stuEntity.getStudentPk().getStudentId())
                .studentMajor(stuEntity.getStudentPk().getStudentMajor())
                .studentGrade(stuEntity.getStudentPk().getStudentGrade())
                .studentName(stuEntity.getStudentPk().getStudentName())
                .createdDate(stuEntity.getCreatedDate())
                .updatedDate(stuEntity.getUpdatedDate())
                .stuPhoneNumber(stuEntity.getStuPhoneNumber())
                .build();
    }
}
