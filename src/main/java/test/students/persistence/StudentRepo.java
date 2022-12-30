package test.students.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.students.persistence.entity.StudentPk;
import test.students.persistence.entity.Students;
import test.students.persistence.entity.Users;

@Repository
public interface StudentRepo extends JpaRepository<Students, StudentPk> {

    Students findByStudentPk_StudentName(String stuName);

}
