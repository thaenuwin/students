package test.students.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.students.persistence.entity.StudentPk;
import test.students.persistence.entity.Students;
import test.students.persistence.entity.Users;

@Repository
public interface StudentRepo extends JpaRepository<Students, StudentPk> {

    Students findByStudentPk_StudentNameAndStudentPk_StudentMajorAndStudentPk_StudentGrade(String stuName, String stuMajor, String stuGrade);

    Students findByStudentPk_StudentId(String id);
}
