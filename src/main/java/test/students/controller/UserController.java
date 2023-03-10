package test.students.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import test.students.opr.UserCreation;
import test.students.utils.ResponseMessageUtil;

import javax.validation.Valid;

@RestController
public class UserController {

    @Autowired
    private UserCreation userCreation;

    @PostMapping(path = "/create-user")

    public ResponseEntity<?> perform(@Valid @RequestBody UserCreation.UserCreationCmd cmd, BindingResult result) {

       UserCreation.CreateUserResponse statusCode= userCreation.userCreate(cmd);
        if(UserCreation.CreateUserResponse.SUCCESS.equals(statusCode)){
            return ResponseEntity.ok(ResponseMessageUtil.createSuccessResponse());
        }
        return ResponseEntity.badRequest().body(ResponseMessageUtil.createFailResponseMessage(statusCode.toString()));

    }
}
