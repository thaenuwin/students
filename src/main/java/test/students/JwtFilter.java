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

    @Value("${app.jwt.public_key_path:null}")
    private String publicKeyPath;

    private byte[] keyBytes;

    private long lastPubKeyLoadedTime;



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

            long current = System.currentTimeMillis();
            long diff=current-lastPubKeyLoadedTime;
            //public key will be refreshed again,
            //if it was loaded more than 15 mins ago
            if(diff>=(1000*60*15)){
                keyBytes=null;
                lastPubKeyLoadedTime=current;
            }

            if(keyBytes==null){
                String rawKey = readPubKeyStringFromExtPath();

                if(rawKey==null||rawKey.trim().length()==0){
                    rawKey = readPubKeyInClassPath();

                }
                if(rawKey!=null) {
                    String urlSafePubKeyString = rawKey.replace("-----BEGIN PUBLIC KEY-----", "")
                            .replace("-----END PUBLIC KEY-----", "").replace("\n", "").trim();

                    keyBytes = Base64.decodeBase64(urlSafePubKeyString);
                }
            }

            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey = kf.generatePublic(spec);
            Signature sign = Signature.getInstance("SHA256withRSA");

            sign.initVerify(publicKey);
            sign.update(message.getBytes(StandardCharsets.UTF_8));
            byte[] toVerify = Base64.decodeBase64URLSafe(signature);

            boolean isValid = sign.verify(toVerify);

            boolean notExpired = notExpiredToken(body);

            if(isValid && notExpired ){
                return 200;//success
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            log.debug("Error on JWT verification: ", ex);
        }
        return 401;
    }


    private String readPubKeyStringFromExtPath() throws IOException {

        if(publicKeyPath==null||publicKeyPath.trim().length()==0){
            return null;
        }

        File file = new File(publicKeyPath);
        if (!file.exists()) {
            return null;
        }

        StringBuilder rawKey = new StringBuilder();
        try ( BufferedReader bf = new BufferedReader(new FileReader(file))) {
            while (true) {
                String line = bf.readLine();
                if (line == null) {
                    break;
                }
                rawKey.append(line.trim());
            }
        }

        String data = rawKey.toString();

        if (data == null || data.trim().length() == 0) {
            return null;
        }

        return data;

    }

    private String readPubKeyInClassPath() throws IOException {
        StringBuilder rawKey = new StringBuilder();
        try ( BufferedReader bf = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream("/public.key")))) {
            while (true) {
                String line = bf.readLine();
                if (line == null) {
                    break;
                }
                rawKey.append(line.trim());
            }
        }

        String data = rawKey.toString();

        if (data == null || data.trim().length() == 0) {
            return null;
        }

        return data;
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

