/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.services;


import lombok.extern.log4j.Log4j2;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import test.students.domain.UserLoginResponse;
import test.students.opr.PerformLogin;
import test.students.persistence.UserDataRepo;
import test.students.persistence.entity.Users;
import test.students.utils.*;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author thaenuwin
 */
@Component
@Log4j2
class LoginService implements PerformLogin {


    @Autowired
    UserDataRepo dataRepo;

    @Value("${server.port}")
    protected String port;
    @Value("${clientName}")
    private String clientName;
    @Value("${clientSecret}")
    private String clientSecret;




    @Autowired
    private UserDataRepo userDataRepo;

    @Override    
    public UserLoginResponse performUserLogin(String loginName, String password) {
        
        try {
            UsernamePasswordCredentials credentials
                    = new UsernamePasswordCredentials(clientName, clientSecret);
            List<NameValuePair> parameters = new ArrayList<>();
            parameters.add(new BasicNameValuePair("grant_type", "password"));
            parameters.add(new BasicNameValuePair("username", loginName));
            parameters.add(new BasicNameValuePair("password", password));
            Users userdata=dataRepo.findByUserId(loginName);

            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parameters);

            HttpPostUtil.PostResponse res =
                    HttpPostUtil.post(
                            "http://localhost:" + port + "/oauth/token", 
                            entity, credentials);

            UserLoginResponse userLoginResponse = new UserLoginResponse();

            userLoginResponse.setBody(JsonUtil.fromJsonString(res.getResponseBody(), Map.class));

            userLoginResponse.setHttpStatusCode(res.getStatusCode());

            return userLoginResponse;

        } catch (UnsupportedEncodingException ex) {
            log.error(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        
        return null;
    }


}
