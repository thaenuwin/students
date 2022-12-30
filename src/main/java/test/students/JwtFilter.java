/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students;

import lombok.extern.log4j.Log4j2;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import test.students.persistence.UserDataRepo;
import test.students.persistence.entity.Users;
import test.students.utils.TokenUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;


@Component
@Order(1)
@Log4j2
public class JwtFilter implements Filter{


    
    private long lastPubKeyLoadedTime;

    @Autowired
    private UserDataRepo userLoginRepo;



    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            HttpServletRequest req = (HttpServletRequest) request;
            String header = req.getHeader("Authorization");
            Integer jwtValid = 200;
        if (header != null && header.startsWith("Bearer ")) {
            if (header != null) {
                jwtValid = verifyJwt(header.replace("Bearer ", ""));
            }
            if (jwtValid != 200) {
                HttpServletResponse res = (HttpServletResponse) response;
                res.setStatus(jwtValid);
            }
            if (jwtValid == 200) {
                chain.doFilter(request, response);
            }
        }else {
            chain.doFilter(request, response);
        }

    }


    private Integer verifyJwt(String payload) {
        try {
            String[] arr = payload.split("\\.");

            String header = arr[0];
            String body = arr[1];
            String message = header + "." + body;
            String signature = arr[2];

            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] toVerify = Base64.decodeBase64URLSafe(signature);

            boolean isValid = sign.verify(toVerify);

            boolean notExpired = notExpiredToken(body);

            String userId = TokenUtil.retrieveUserId("Bearer "+payload);
            Users userLogin =userLoginRepo.findByUserId(userId);

            if(isValid && notExpired ){
                return 200;//success
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug("Error on JWT verification: ", ex);
        }
        return 401;
    }






    private boolean notExpiredToken(String enc) {
        try {
            String dec = new String(Base64.decodeBase64(enc), StandardCharsets.UTF_8);
            String[] arr = dec.split(",");
            String exp = "";
            String search = "\"exp\":";
            for (String str : arr) {
                if (str.startsWith(search)) {
                    exp = str.replace(search, "");
                    break;
                }
            }

            long l = Long.parseLong(exp + "000");
            Date date = new Date(l);
            log.debug("JWT expired time: {}", date);

            Date now = new Date();
            return date.after(now);

        } catch (Exception ex) {
            log.debug("Error while validating expiry time: ", ex);
        }
        return false;
    }


}
