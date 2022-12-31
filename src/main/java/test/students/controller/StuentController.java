package test.students.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping(path = "/update-student")
    public ResponseEntity<?> performUpdate(@RequestHeader(value = "Authorization") String authorization, @RequestBody StudentCmd cmd, @RequestParam("stuId") String itemid) {
        StudentResponse statusCode= studentOpr.studentUpdate(itemid,cmd);
        if(StudentResponse.SUCCESS.equals(statusCode)){
            return ResponseEntity.ok(ResponseMessageUtil.createSuccessResponse());
        }
        return ResponseEntity.badRequest().body(ResponseMessageUtil.createFailResponseMessage(statusCode.toString()));
    }

    @PostMapping(path = "/delete-student")
    public ResponseEntity<?> performDelete(@RequestHeader(value = "Authorization") String authorization, @RequestParam("stuId") String itemid) {
        StudentResponse statusCode= studentOpr.studentDelete(itemid);
        if(StudentResponse.SUCCESS.equals(statusCode)){
            return ResponseEntity.ok(ResponseMessageUtil.createSuccessResponse());
        }
        return ResponseEntity.badRequest().body(ResponseMessageUtil.createFailResponseMessage(statusCode.toString()));
    }
}
