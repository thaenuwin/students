package test.students.persistence;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.students.opr.StudentOpr;
import test.students.persistence.entity.StudentPk;
import test.students.persistence.entity.Students;
import test.students.utils.IdUtil;
import test.students.utils.ValidatorUtil;

import java.util.Date;

@Component
@Log4j2

public class StudentImpl implements StudentOpr {

    @Autowired
    StudentRepo stuRepo;
    @Override
    @Transactional
    public StudentResponse studentCreate(StudentCmd stucmd) {
        ValidatorUtil.validate(stucmd);

        if (isExistingStudent(stucmd)) {
            return StudentResponse.ERROR_DUPLICATE_USER_FOUND;
        }

        StudentPk stupk = StudentPk.builder()
                .studentId(IdUtil.generate())
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
    @Transactional
    public StudentResponse studentUpdate(String id, StudentCmd stucmd) {
        ValidatorUtil.validate(stucmd);

        Students existstudent = stuRepo.findByStudentPk_StudentId(id);
        if (existstudent.getStudentEnable() == 0 ) {
            return StudentResponse.ERROR_USER_NOT_FOUND;
        }
        StudentPk stupk = StudentPk.builder()
                .studentId(existstudent.getStudentPk().getStudentId())
                .studentGrade(stucmd.getStudentGrade())
                .studentName(stucmd.getStudentName())
                .studentMajor(stucmd.getStudentMajor())
                .build();
        Students stuData = Students.builder()
                .studentEnable(1)
                .studentPk(stupk)
                .stuPhoneNumber(stucmd.getPhoneNumber())
                .updatedDate(new Date())
                .createdDate(existstudent.getCreatedDate())
                .build();

        stuRepo.delete(existstudent);

        stuRepo.save(stuData);

        return StudentResponse.SUCCESS;
    }

    @Override
    @Transactional
    public StudentResponse studentDelete(String stuId) {
        Students existstudent = stuRepo.findByStudentPk_StudentId(stuId);
        if (existstudent == null || existstudent.getStudentEnable()==0) {
            return StudentResponse.ERROR_USER_NOT_FOUND;
        }
        existstudent.setStudentEnable(0);

        return StudentResponse.SUCCESS;
    }

    private boolean isExistingStudent(StudentCmd studentCmd) {
        log.debug("isExistingUserId: "+studentCmd);
        Students data = stuRepo.findByStudentPk_StudentNameAndStudentPk_StudentMajorAndStudentPk_StudentGrade(studentCmd.getStudentName(),studentCmd.getStudentMajor(),studentCmd.getStudentGrade());
        return data != null && data.getStudentPk() != null;
    }

}
