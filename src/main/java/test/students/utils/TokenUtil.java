/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.utils;

import org.springframework.util.Base64Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zeyarlinhtike
 */
public class TokenUtil {

    private TokenUtil() {
    }

    public static final Map<String, Object> retrieveUserInfo(String authorization) {
        try {
            String enc = authorization;
            String prefix = "Bearer ";
            if (authorization.startsWith(prefix)) {
                enc = authorization.replace(prefix, "").trim().split("\\.")[1];
            }
            String dec = new String(Base64Utils.decodeFromString(enc));
            return JsonUtil.fromJsonString(dec, Map.class);
        } catch (Exception ex) {
            return null;
        }
    }

    public static final String retrieveUserId(String authorization) {
        try {
            String enc = authorization;
            String prefix = "Bearer ";
            if (authorization.startsWith(prefix)) {
                enc = authorization.replace(prefix, "").trim().split("\\.")[1];
            }
            String dec = new String(Base64Utils.decodeFromString(enc));
            return JsonUtil.fromJsonString(dec, Map.class).get("user_name").toString();
        } catch (Exception ex) {
            return null;
        }
    }

}
