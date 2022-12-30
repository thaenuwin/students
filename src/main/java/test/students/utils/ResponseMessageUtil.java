/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.utils;

import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author thaenuwin
 */
public class ResponseMessageUtil {

    public static Map<String, Object> createSuccessResponseWithMessage(Object data, String message) {
        Map<String, Object> map = new TreeMap<>();
        map.put("status", "SUCCESS");
        map.put("data", data);
        map.put("message", message);
        return map;
    }
    public static Map<String, Object> createSuccessResponse() {
        Map<String, Object> map = new TreeMap<>();
        map.put("status", "SUCCESS");
        map.put("data", null);
        return map;
    }
    public static Map<String, Object> createSuccessResponse(Object data) {
        Map<String, Object> map = new TreeMap<>();
        map.put("status", "SUCCESS");
        map.put("data", data);
        return map;
    }

    public static Map<String, Object> createFailResponseMessage(String message) {
        Map<String, Object> map = new TreeMap<>();
        map.put("status", "FAIL");
        map.put("error_description", message);
        return map;
    }

    public static Map<String, Object> createFailResponse(Object error, String message) {
        Map<String, Object> map = new TreeMap<>();
        map.put("status", "FAIL");
        map.put("error", error);
        map.put("error_description", message);
        return map;
    }
    public static Map<String, Object> createFailResponse() {
        Map<String, Object> map = new TreeMap<>();
        map.put("status", "FAIL");
        return map;
    }
}
