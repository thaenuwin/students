package test.students.persistence;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import test.students.opr.StudentOpr;
import test.students.opr.UserCreation;
import test.students.persistence.entity.StudentPk;
import test.students.persistence.entity.Students;
import test.students.persistence.entity.Users;
import test.students.utils.IdUtil;
import test.students.utils.PasswordUtil;
import test.students.utils.ValidatorUtil;

import java.util.Date;

@Component
@Log4j2
public class StudentImpl implements StudentOpr {

    @Autowired
    StudentRepo stuRepo;
    @Override
    public StudentResponse studentCreate(StudentCmd stucmd) {
        ValidatorUtil.validate(stucmd);

        if (isExistingStudent(stucmd.getStudentName())) {
            return StudentResponse.ERROR_DUPLICATE_USER_FOUND;
        }

        StudentPk stupk = StudentPk.builder()
                .studentGrade(stucmd.getStudentGrade())
                .studentName(stucmd.getStudentName())
                .studentMajor(stucmd.getStudentMajor())
                .build();
        Students stuData = Students.builder()
                .studentEnable(1)
                .studentPk(stupk)
                .stuPhoneNumber(stucmd.getPhoneNumber())
                .updatedDate(new Date())
                .createdDate(new Date())
                .build();
        stuRepo.save(stuData);


        return StudentResponse.SUCCESS;
    }

    @Override
    public StudentResponse studentUpdate(StudentCmd stucmd) {
        return null;
    }

    @Override
    public StudentResponse studentDelete(String stuId) {
        return null;
    }

    private boolean isExistingStudent(String stuName) {
        log.debug("isExistingUserId: "+stuName);
        Students data = stuRepo.findByStudentPk_StudentName(stuName);
        return data != null && data.getStudentPk() != null;
    }

}
