/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * @author thaenuwin
 */

@Configuration
public class TokenConfig {




    
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter(){
            

            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                final Map<String,Object> additionalInfo = new HashMap<>();
                Map<String,String> params = authentication.getOAuth2Request().getRequestParameters();
                

                
                additionalInfo.putAll(params);
                additionalInfo.put("randUuid", UUID.randomUUID().toString().replace("-", ""));
                
                DefaultOAuth2AccessToken accToken=((DefaultOAuth2AccessToken)accessToken);
                
                accToken.setAdditionalInformation(additionalInfo);                

                long now=System.currentTimeMillis();
                accToken=((DefaultOAuth2AccessToken)accessToken);
                
                //1 hour to expire
                accToken.setExpiration(new Date(now+(1000*60*60)));

                return super.enhance(accessToken, authentication); 
            }
            
            
        };
        
        return converter;
    }
    
    @Bean
    public TokenStore tokenStore(){
        return new JwtTokenStore(accessTokenConverter());
    }
    
    @Bean
    @Primary
    public DefaultTokenServices tokenServices(){
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
    }
}
