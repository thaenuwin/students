/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import test.students.utils.KeyPairUtil;

import java.io.File;
import java.security.KeyPair;

/**
 *
 * @author thaenuwin
 */
@Component
@Log4j2
public class KeyPairProvider {

    @Value("${app.jks_secret:changeitauth}")
    private String pString;
    @Value("${app.jks_path:null}")
    private String jks;
    @Value("${app.jks_alias:auth-server}")
    private String alias;

    public KeyPair fetchKeystore() {
        log.info("jks:{}",jks);
        log.info("alias:{}",alias);
        File file=new File(jks);
        if (!file.exists()) {
            log.info("Reading jks from classpath...");
            return KeyPairUtil.retrieveKeyPair(new ClassPathResource("/auth-testing-server.jks"), "auth-server", "changeitauth");
        } else {
            log.info("Reading from external file...");
            return KeyPairUtil.retrieveKeyPair(new FileSystemResource(jks), alias, pString);
        }
    }
}

