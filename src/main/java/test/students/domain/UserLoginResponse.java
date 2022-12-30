/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.domain;

import java.util.Map;

/**
 *
 * @author thaenuwin
 */
public class UserLoginResponse {
    
    private Integer httpStatusCode;
    private Map<?,?> body;

    /**
     * @return the httpStatusCode
     */
    public Integer getHttpStatusCode() {
        return httpStatusCode;
    }

    /**
     * @param httpStatusCode the httpStatusCode to set
     */
    public void setHttpStatusCode(Integer httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    /**
     * @return the body
     */
    public Map<?,?> getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(Map<?,?> body) {
        this.body = body;
    }
}
