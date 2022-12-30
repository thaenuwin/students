/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Log4j2
public class HttpPostUtil {
    
    public static class PostResponse{
        private Integer statusCode;
        private String responseBody;

        /**
         * @return the statusCode
         */
        public int getStatusCode() {
            return statusCode;
        }

        /**
         * @param statusCode the statusCode to set
         */
        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        
        public String getResponseBody() {
            return responseBody;
        }

        
        public void setResponseBody(String responseBody) {
            this.responseBody = responseBody;
        }
        
        
    }
    
    
    public static PostResponse post(String url,UrlEncodedFormEntity entity,
            UsernamePasswordCredentials credentials){
    
        try{
            CredentialsProvider provider = new BasicCredentialsProvider();
            provider.setCredentials(AuthScope.ANY, credentials);
            
            HttpClient client = HttpClientBuilder.create()
                    .setDefaultCredentialsProvider(provider).build();
            
            HttpPost request = new HttpPost(url);
            
            request.setEntity(entity);

            HttpResponse response = client.execute(request);
            PostResponse postResponse=new PostResponse();
            int statuscode = response.getStatusLine().getStatusCode();
            postResponse.statusCode=statuscode;
            try(BufferedReader buf=new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()))){
                StringBuilder sb = new StringBuilder();
                while(true){
                    String line = buf.readLine();
                    if(line == null){
                        break;
                    }        
                    sb.append(line);
                }                
                postResponse.responseBody=sb.toString();
            }
            
            return postResponse;
        }catch(IOException ex){
            ex.printStackTrace();
            log.error(HttpPostUtil.class, ex);
        }catch(Exception ex){
            ex.printStackTrace();
            log.error(HttpPostUtil.class, ex);
        }
        
        return null;
    }

}
