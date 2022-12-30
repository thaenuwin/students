package test.students.controller;



import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import test.students.domain.UserLoginResponse;
import test.students.opr.PerformLogin;
import test.students.utils.JsonUtil;
import test.students.utils.ResponseMessageUtil;

import java.util.Map;



@RestController
@Log4j2
public class LoginController {

    @Autowired
    private PerformLogin performLogin;

    @PostMapping(path = "/login")
    public ResponseEntity<?> perform(@RequestBody String body) {

        try {
            Map<?, ?> payload = JsonUtil.fromJsonString(body, Map.class);
            String username = (String) payload.get("username");
            String password = (String) payload.get("password");

            UserLoginResponse result = performLogin.performUserLogin(username, password);
            if (result != null && result.getHttpStatusCode() != null) {
                return ResponseEntity.status(result.getHttpStatusCode()).body(result.getBody());
            }

            throw new RuntimeException("Invalid response!");

        } catch (Exception ex) {
            log.error("Exception in login controller : ", ex.getMessage());
            return ResponseEntity.badRequest().body(ResponseMessageUtil.createFailResponseMessage("Error occurred while processing request"));
        }


    }
}