package test.students.opr;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public interface UserCreation {
    CreateUserResponse userCreate ( UserCreationCmd usercmd);

    @Data
    public class UserCreationCmd {


        @NotEmpty
        private String userId;

        @NotEmpty
        private String displayName;

        @NotEmpty
        @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")
        private String email;

        private String phoneNumber;

        @NotEmpty
        private String loginPassword;
    }
    public enum CreateUserResponse {
        ERROR_USER_NOT_FOUND,
        ERROR_DUPLICATE_USER_FOUND,
        ERROR_DUPLICATE_EMAIL_WITH_OTHER_USER,
        ERROR_INVALID_PASSWORD,
        SUCCESS
    }
}
