package test.students.opr;

import lombok.Data;
import javax.validation.constraints.NotEmpty;

public interface StudentOpr {

    StudentResponse studentCreate (StudentCmd stucmd);

    StudentResponse studentUpdate (StudentCmd stucmd);

    StudentResponse studentDelete (String stuId);

    @Data
    public class StudentCmd {


        @NotEmpty
        private String studentName;

        @NotEmpty
        private String phoneNumber;

        @NotEmpty
        private String studentMajor;

        @NotEmpty
        private String studentGrade;


    }
    public enum StudentResponse {
        ERROR_USER_NOT_FOUND,
        ERROR_DUPLICATE_USER_FOUND,
        ERROR_DUPLICATE_EMAIL_WITH_OTHER_USER,
        ERROR_INVALID_PASSWORD,
        SUCCESS
    }
}
