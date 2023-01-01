package test.students.opr;

import lombok.Data;
import test.students.annotation.FieldsValueMatch;
import test.students.annotation.Password;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;


public interface UserCreation {
    CreateUserResponse userCreate ( UserCreationCmd usercmd);

    @FieldsValueMatch.List({
            @FieldsValueMatch(
                    field = "loginPassword",
                    fieldMatch = "verifyPassword",
                    message = "Passwords do not match!"
            )
    })
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
        @Password
        private String loginPassword;

        @NotEmpty
        private String verifyPassword;
    }
    public enum CreateUserResponse {
        ERROR_DUPLICATE_USER_FOUND,
        ERROR_DUPLICATE_EMAIL_WITH_OTHER_USER,
        SUCCESS
    }
}
