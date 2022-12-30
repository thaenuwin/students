/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.utils;

import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.GeneralSecurityException;
import java.util.regex.Pattern;

import static org.springframework.security.crypto.util.EncodingUtils.concatenate;
import static org.springframework.security.crypto.util.EncodingUtils.subArray;


public class PasswordUtil {
    
    private PasswordUtil(){}
    
    public static final PasswordEncoder createPasswordEncorder(){
        
        return new Pbkdf2PasswordEncoder(){
            
            @Override
            public String encode(CharSequence rawPassword) {
                byte[] salt = KeyGenerators.secureRandom(64).generateKey();
		return String.valueOf(Hex.encode(enc(rawPassword,salt)));
		
            }
            
            byte[] enc(CharSequence rawPassword,byte[] salt){
                try {
                        
			PBEKeySpec spec = new PBEKeySpec(rawPassword.toString().toCharArray(),
					concatenate(salt, rawPassword.toString().getBytes()), 100000, 32);
			SecretKeyFactory skf = SecretKeyFactory.getInstance(SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256.name());
			return concatenate(salt, skf.generateSecret(spec).getEncoded());

		}
		catch (GeneralSecurityException e) {
			throw new IllegalStateException("Could not create hash", e);
		}
            }
            
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                    byte[] expected = Hex.decode(encodedPassword);
                    byte[] salt = subArray(expected, 0, 64);
                    return mtch(expected, enc(rawPassword, salt));
            }
            
            boolean mtch(byte[] expected, byte[] actual) {
		if (expected.length != actual.length) {
			return false;
		}

		int result = 0;
		for (int i = 0; i < expected.length; i++) {
			result |= expected[i] ^ actual[i];
		}
		return result == 0;
            }
        };
    
    }
    
    
    public static final String hashPassword(String password) {        
        PasswordEncoder encoder = createPasswordEncorder();
        return encoder.encode(password);
        
    }
    
    public static final boolean matches(String raw,String enc) {        
        PasswordEncoder encoder = createPasswordEncorder();
        return encoder.matches(raw, enc);
    }

    public static final boolean isPasswordValid(String pwd) {

        Pattern specailCharPatten = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Pattern upperCasePatten = Pattern.compile("[A-Z ]");
        Pattern lowerCasePatten = Pattern.compile("[a-z ]");
        Pattern digitCasePatten = Pattern.compile("[0-9 ]");
        Pattern notContainsPatten = Pattern.compile("[=]");

        boolean flag=true;

        if (pwd.length() < 12) {
            flag=false;
        }
        if (!specailCharPatten.matcher(pwd).find()) {
            flag=false;
        }
        if (!upperCasePatten.matcher(pwd).find()) {
            flag=false;
        }
        if (!lowerCasePatten.matcher(pwd).find()) {
            flag=false;
        }
        if (!digitCasePatten.matcher(pwd).find()) {
            flag=false;
        }
        if (notContainsPatten.matcher(pwd).find()) {
            flag=false;
        }

        return flag;

    }


}
