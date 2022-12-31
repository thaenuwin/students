/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.utils;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.Resource;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;


/**
 *
 * @author thaenuwin
 */
public class KeyPairUtil {
    
    public static final KeyPair retrieveKeyPair(Resource jks,String alias,String pString){
        KeyStoreKeyFactory keyStoreKeyFactory= new KeyStoreKeyFactory(jks, pString.toCharArray());
        return keyStoreKeyFactory.getKeyPair(alias);
    }
    
    public static final String exportPublicKey(KeyPair keyPair){
        
        return "-----BEGIN PUBLIC KEY-----\n"+new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()))+"\n-----END PUBLIC KEY-----";
    } 
}
