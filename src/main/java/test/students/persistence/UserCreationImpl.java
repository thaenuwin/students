package test.students.persistence;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import test.students.opr.UserCreation;
import test.students.persistence.entity.Users;
import test.students.utils.PasswordUtil;
import test.students.utils.ValidatorUtil;

import java.util.Date;

@Component
@Log4j2
public class UserCreationImpl implements UserCreation {

    @Autowired
    UserDataRepo userDataRepo;

    @Override
    @Transactional
    public CreateUserResponse userCreate(UserCreationCmd usercmd) {
        ValidatorUtil.validate(usercmd);

        String userId = usercmd.getUserId();
        if (isExistingUserLogin(userId)) {
            return CreateUserResponse.ERROR_DUPLICATE_USER_FOUND;
        }

        if(isExistingEmail(usercmd.getEmail())){
            return CreateUserResponse.ERROR_DUPLICATE_EMAIL_WITH_OTHER_USER;
        }
        String hashPassword= PasswordUtil.hashPassword(usercmd.getLoginPassword());
        Users userData = Users.builder()
                .userId(usercmd.getUserId())
                .email(usercmd.getEmail())
                .displayName(usercmd.getDisplayName())
                .phoneNumber(usercmd.getPhoneNumber())
                .loginPassword(hashPassword)
                .enabled(1)
                .updatedDate(new Date())
                .createdDate(new Date())
                .build();
        userDataRepo.save(userData);


        return CreateUserResponse.SUCCESS;
    }

    private boolean isExistingUserLogin(String userId) {
        log.debug("isExistingUserId: "+userId);
        Users data = userDataRepo.findByUserId(userId);
        return data != null && data.getUserId() != null;
    }

    private boolean isExistingEmail(String email) {
        log.debug( "isExistingEmail: "+email);
        Users data = userDataRepo.findByEmail(email);
        return data != null && data.getEmail() != null;
    }
}
