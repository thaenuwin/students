package test.students.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import test.students.opr.StudentOpr;
import test.students.opr.StudentOpr.StudentCmd;
import test.students.opr.StudentOpr.StudentResponse;
import test.students.utils.ResponseMessageUtil;

@RestController
@Log4j2
public class StuentController {

    @Autowired
    private StudentOpr studentOpr;
    @PostMapping(path = "/create-student")
    public ResponseEntity<?> perform(@RequestHeader(value = "Authorization") String authorization, @RequestBody StudentCmd cmd) {
        StudentResponse statusCode= studentOpr.studentCreate(cmd);
        if(StudentResponse.SUCCESS.equals(statusCode)){
            return ResponseEntity.ok(ResponseMessageUtil.createSuccessResponse());
        }
        return ResponseEntity.badRequest().body(ResponseMessageUtil.createFailResponseMessage(statusCode.toString()));
    }
}
