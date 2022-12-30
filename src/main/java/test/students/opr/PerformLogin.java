package test.students.opr;


import test.students.domain.UserLoginResponse;

public interface PerformLogin {

    UserLoginResponse performUserLogin(String username, String password);

}
