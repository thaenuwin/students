/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.students;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import test.students.utils.JsonUtil;

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
@Log4j2
public class TokenConfig {

    private static final Class TAG=TokenConfig.class;
    @Value("${accessTokenValidityMinutes}")
    private int accesstokenMin;
    @Autowired
    private KeyPairProvider keyPairProvider;


    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter(){


            @Override
            public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
                final Map<String,Object> additionalInfo = new HashMap<>();
                System.out.println(authentication.getOAuth2Request().getRequestParameters().toString());
                Map<String,String> params = authentication.getOAuth2Request().getRequestParameters();

                String principalJsonString= JsonUtil.toJsonString(authentication.getPrincipal());
                log.info( "========= Principal ==========");
                log.info( principalJsonString);
                log.info( "==============================");

                log.info( "========= Permission ==========");
                log.info( "==============================");

                additionalInfo.putAll(params);
                additionalInfo.put("randUuid", UUID.randomUUID().toString().replace("-", ""));

                DefaultOAuth2AccessToken accToken=((DefaultOAuth2AccessToken)accessToken);

                accToken.setAdditionalInformation(additionalInfo);

                long now=System.currentTimeMillis();
                accToken=((DefaultOAuth2AccessToken)accessToken);

                //1 hour to expire
                accToken.setExpiration(new Date(now+(1000*60*accesstokenMin)));

                return super.enhance(accessToken, authentication);
            }


        };



        KeyPair keyPair=keyPairProvider.fetchKeystore();
        converter.setKeyPair(keyPair);

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
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(accessTokenConverter());
        return defaultTokenServices;
    }
}
